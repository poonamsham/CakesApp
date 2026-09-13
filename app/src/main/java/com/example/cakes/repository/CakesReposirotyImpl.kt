package com.example.cakes.repository

import android.util.Log
import com.example.cakes.data.model.CakeModel
import com.example.cakes.data.service.CakeApiService
import javax.inject.Inject

/**
 * Concrete implementation of [CakeRepository] that fetches data from a remote API.
 *
 * @property apiService The [CakeApiService] used to make network requests.
 */
class CakeRepositoryImpl @Inject constructor(
    private val apiService: CakeApiService
) : CakeRepository {

    /**
     * Fetches cakes from the network using the provided API service.
     * Logs the response for debugging purposes.
     */
    override suspend fun getCakes(): List<CakeModel> {
        val cakeList = apiService.getCakes()
        Log.d("CakeRepositoryImpl", "response = $cakeList")
        return cakeList
    }

}
