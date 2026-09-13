package com.example.cakes.data.service

import com.example.cakes.data.model.CakeModel
import com.example.cakes.util.CakeConstants
import retrofit2.http.GET

/**
 * Retrofit service interface for fetching cake data from the network.
 */
interface CakeApiService {

    /**
     * Fetches the list of cakes from the API endpoint.
     * @return A list of [CakeModel] objects.
     */
    @GET(CakeConstants.ENDPOINT)
    suspend fun getCakes(): List<CakeModel>
}
