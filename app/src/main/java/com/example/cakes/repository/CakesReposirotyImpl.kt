package com.example.cakes.repository

import com.example.cakes.data.model.CakeModel
import com.example.cakes.data.service.CakeApiService
import javax.inject.Inject

class CakeRepositoryImpl @Inject constructor(
    private val apiService: CakeApiService
) : CakeRepository {

    override suspend fun getCakes(): List<CakeModel> {
        return apiService.getCakes()
    }

}