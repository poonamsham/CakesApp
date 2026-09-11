package com.example.cakes.data.service

import com.example.cakes.data.model.CakeModel
import com.example.cakes.util.CakeConstants
import retrofit2.http.GET

interface CakeApiService {

    @GET(CakeConstants.ENDPOINT)
    suspend fun getCakes(): List<CakeModel>
}