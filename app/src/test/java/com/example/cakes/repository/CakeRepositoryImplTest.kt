package com.example.cakes.repository

import android.util.Log
import com.example.cakes.data.model.CakeModel
import com.example.cakes.data.service.CakeApiService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [CakeRepositoryImpl].
 */
class CakeRepositoryImplTest {

    private val apiService: CakeApiService = mockk()
    private val repository = CakeRepositoryImpl(apiService)

    @Before
    fun setUp() {
        // Mocks the Android Log class to prevent runtime exceptions during unit tests.
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    /**
     * Verifies that the repository correctly delegates data fetching to the API service.
     */
    @Test
    fun `getCakes returns list from api service`() = runTest {
        val expectedCakes = listOf(
            CakeModel("Title", "Desc", "Image")
        )
        // Mock the network response.
        coEvery { apiService.getCakes() } returns expectedCakes

        val result = repository.getCakes()

        // Assert that the repository returns exactly what the API service provides.
        assertEquals(expectedCakes, result)
    }
}
