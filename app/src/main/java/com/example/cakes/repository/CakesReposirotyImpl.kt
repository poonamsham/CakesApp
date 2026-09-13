package com.example.cakes.repository

import android.util.Log
import com.example.cakes.data.model.CakeModel
import com.example.cakes.data.service.CakeApiService
import javax.inject.Inject

class CakeRepositoryImpl @Inject constructor(
    private val apiService: CakeApiService
) : CakeRepository {

    override suspend fun getCakes(): List<CakeModel> {
        val cakeList = apiService.getCakes()
        Log.d("CakeRepositoryImpl", "response = $cakeList")
        return cakeList
    }

}