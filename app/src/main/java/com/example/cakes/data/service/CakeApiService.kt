package com.example.cakes.data.service

import com.example.cakes.data.model.CakeModel
import retrofit2.http.GET

interface CakeApiService {

    @GET("Waracle/mobile-coding-test-api/refs/heads/main/cakes")
    suspend fun getCakes(): List<CakeModel>
}