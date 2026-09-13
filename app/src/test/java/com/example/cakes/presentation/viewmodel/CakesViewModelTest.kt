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
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

/**
 * Unit tests for [CakesViewModel].
 * Uses [Turbine] for Flow testing and [MockK] for dependency mocking.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CakesViewModelTest {

    /**
     * Rule to override the Main dispatcher with a test dispatcher.
     */
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: CakeRepository = mockk()

    @Before
    fun setUp() {
        // Mocking Android Log class to avoid "Method not mocked" errors.
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    /**
     * Verifies that the ViewModel correctly fetches data on initialization.
     */
    @Test
    fun `initial load updates cakes and sets isLoading to false`() = runTest {
        val cakes = listOf(CakeModel("Cake 1", "Desc 1", "Image 1"))
        coEvery { repository.getCakes() } coAnswers {
            delay(10.milliseconds)
            cakes
        }

        val viewModel = CakesViewModel(repository)

        viewModel.uiState.test {
            // Check initial loading state.
            val state1 = awaitItem()
            assertTrue(state1.isLoading)

            // Check success state after loading completes.
            val state2 = awaitItem()
            assertEquals(cakes, state2.cakes)
            assertFalse(state2.isLoading)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Verifies that the pull-to-refresh operation correctly updates the UI state.
     */
    @Test
    fun `refresh updates cakes and toggles isRefreshing`() = runTest {
        val cakes1 = listOf(CakeModel("Cake 1", "Desc 1", "Image 1"))
        val cakes2 = listOf(CakeModel("Cake 2", "Desc 2", "Image 2"))
        
        var callCount = 0
        coEvery { repository.getCakes() } coAnswers {
            callCount++
            if (callCount == 1) {
                cakes1
            } else {
                delay(10.milliseconds)
                cakes2
            }
        }

        val viewModel = CakesViewModel(repository)

        viewModel.uiState.test {
            // Verify initial load.
            val firstState = awaitItem()
            val state1 = if (firstState.isLoading) awaitItem() else firstState
            assertEquals(cakes1, state1.cakes)

            // Perform refresh.
            viewModel.refresh()
            
            // Check intermediate refreshing state.
            val refreshingState = awaitItem()
            assertTrue(refreshingState.isRefreshing)

            // Check final success state.
            val state2 = awaitItem()
            assertEquals(cakes2, state2.cakes)
            assertFalse(state2.isRefreshing)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Verifies that the retry operation correctly re-triggers data loading.
     */
    @Test
    fun `retry calls loadCakes again`() = runTest {
        val cakes = listOf(CakeModel("Cake 1", "Desc 1", "Image 1"))
        var callCount = 0
        coEvery { repository.getCakes() } coAnswers {
            callCount++
            if (callCount == 1) throw Exception("Error")
            else {
                delay(10.milliseconds)
                cakes
            }
        }

        val viewModel = CakesViewModel(repository)

        viewModel.uiState.test {
            // Capture initial load failure.
            val firstState = awaitItem()
            val errorState = if (firstState.isLoading) awaitItem() else firstState
            assertEquals("Error", errorState.error)

            // Trigger retry.
            viewModel.retry()
            
            // Check loading state from retry.
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            // Check final success state.
            val successState = awaitItem()
            assertEquals(cakes, successState.cakes)
            assertFalse(successState.isLoading)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Verifies that the ViewModel handles repository errors correctly.
     */
    @Test
    fun `loadCakes sets error on failure`() = runTest {
        coEvery { repository.getCakes() } coAnswers {
            throw Exception("Network error")
        }

        val viewModel = CakesViewModel(repository)

        viewModel.uiState.test {
            val firstState = awaitItem()
            val state = if (firstState.isLoading) awaitItem() else firstState
            assertEquals("Network error", state.error)
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
