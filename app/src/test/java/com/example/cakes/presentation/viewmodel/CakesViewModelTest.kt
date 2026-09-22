package com.example.cakes.presentation.viewmodel

import android.util.Log
import app.cash.turbine.test
import com.example.cakes.data.model.CakeModel
import com.example.cakes.repository.CakeRepository
import com.example.cakes.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for [CakesViewModel].
 * Uses [Turbine] for Flow testing and [MockK] for dependency mocking.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CakesViewModelTest {

    /**
     * Rule to override the Main dispatcher with a test dispatcher.
     * Uses StandardTestDispatcher for controlled execution.
     */
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: CakeRepository

    @Before
    fun setUp() {
        repository = mockk()
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @Test
    fun `initial load updates cakes and sets isLoading to false`() = runTest {
        val cakes = listOf(CakeModel("Cake 1", "Desc 1", "Image 1"))
        coEvery { repository.getCakes() } returns cakes

        val viewModel = CakesViewModel(repository)

        viewModel.uiState.test {
            // First item is initial state from MutableStateFlow constructor
            val state1 = awaitItem()
            assertTrue(state1.isLoading)

            // runCurrent() will execute the loadCakes coroutine
            runCurrent()

            val state2 = awaitItem()
            assertEquals(cakes, state2.cakes)
            assertFalse(state2.isLoading)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh updates cakes and toggles isRefreshing`() = runTest {
        val cakes1 = listOf(CakeModel("Cake 1", "Desc 1", "Image 1"))
        val cakes2 = listOf(CakeModel("Cake 2", "Desc 2", "Image 2"))
        
        coEvery { repository.getCakes() } returns cakes1 andThen cakes2

        val viewModel = CakesViewModel(repository)

        viewModel.uiState.test {
            awaitItem() // initial
            runCurrent()
            assertEquals(cakes1, awaitItem().cakes)

            viewModel.refresh()
            
            // First emission from refresh: isRefreshing = true
            val refreshingState = awaitItem()
            assertTrue(refreshingState.isRefreshing)
            assertEquals(cakes1, refreshingState.cakes)

            // Execute the rest of refresh
            runCurrent()

            val successState = awaitItem()
            assertEquals(cakes2, successState.cakes)
            assertFalse(successState.isRefreshing)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry calls loadCakes again`() = runTest {
        val cakes = listOf(CakeModel("Cake 1", "Desc 1", "Image 1"))
        coEvery { repository.getCakes() } throws Exception("Error") andThen cakes

        val viewModel = CakesViewModel(repository)

        viewModel.uiState.test {
            awaitItem() // initial
            runCurrent()
            val errorState = awaitItem()
            assertEquals("Error", errorState.error)

            viewModel.retry()
            
            // First emission from retry: isLoading = true
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            runCurrent()

            val successState = awaitItem()
            assertEquals(cakes, successState.cakes)
            assertFalse(successState.isLoading)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadCakes sets error on failure`() = runTest {
        coEvery { repository.getCakes() } throws Exception("Network error")

        val viewModel = CakesViewModel(repository)

        viewModel.uiState.test {
            awaitItem() // initial
            runCurrent()
            val state = awaitItem()
            assertEquals("Network error", state.error)
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh sets isRefreshing to false on failure`() = runTest {
        coEvery { repository.getCakes() } returns emptyList() andThenThrows Exception("Refresh failed")

        val viewModel = CakesViewModel(repository)

        viewModel.uiState.test {
            awaitItem() // initial
            runCurrent()
            awaitItem() // success empty

            viewModel.refresh()

            assertTrue(awaitItem().isRefreshing)

            runCurrent()

            val finalState = awaitItem()
            assertFalse(finalState.isRefreshing)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry sets error on failure`() = runTest {
        coEvery { repository.getCakes() } throws Exception("First error") andThenThrows Exception("Retry error")

        val viewModel = CakesViewModel(repository)

        viewModel.uiState.test {
            awaitItem() // initial
            runCurrent()
            assertEquals("First error", awaitItem().error)

            viewModel.retry()

            assertTrue(awaitItem().isLoading)

            runCurrent()

            val finalState = awaitItem()
            assertEquals("Retry error", finalState.error)
            assertFalse(finalState.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
