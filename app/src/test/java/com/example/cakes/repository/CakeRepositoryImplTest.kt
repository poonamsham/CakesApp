package com.example.cakes.repository

import com.example.cakes.data.model.CakeModel
import com.example.cakes.data.service.CakeApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CakeRepositoryImplTest {

    private val apiService: CakeApiService = mockk()
    private val repository = CakeRepositoryImpl(apiService)

    @Test
    fun `getCakes returns list from api service`() = runTest {
        val expectedCakes = listOf(
            CakeModel("Title", "Desc", "Image")
        )
        coEvery { apiService.getCakes() } returns expectedCakes

        val result = repository.getCakes()

        assertEquals(expectedCakes, result)
    }
}
