package com.example.cakes.presentation.viewmodel

import app.cash.turbine.test
import com.example.cakes.data.model.CakeModel
import com.example.cakes.repository.CakeRepository
import com.example.cakes.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CakesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: CakeRepository = mockk()

    @Test
    fun `refresh updates cakes and sets isRefreshing to false on success`() = runTest {
        val cakes1 = listOf(CakeModel("Cake 1", "Desc 1", "Image 1"))
        val cakes2 = listOf(CakeModel("Cake 2", "Desc 2", "Image 2"))
        
        coEvery { repository.getCakes() } returns cakes1 andThen cakes2

        val viewModel = CakesViewModel(repository)

        viewModel.uiState.test {
            // Initial state from init call
            val state1 = awaitItem()
            assertEquals(cakes1, state1.cakes)

            // Explicit refresh
            viewModel.refresh()
            
            // Should see the change to cakes2
            val state2 = awaitItem()
            assertEquals(cakes2, state2.cakes)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh handles empty list`() = runTest {
        coEvery { repository.getCakes() } returns emptyList()
        val viewModel = CakesViewModel(repository)
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(emptyList<CakeModel>(), state.cakes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh sets isRefreshing to false on error`() = runTest {
        coEvery { repository.getCakes() } throws Exception("Network error")

        val viewModel = CakesViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isRefreshing)
            assertEquals(emptyList<CakeModel>(), state.cakes)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
