package com.example.cakes.repository

import com.example.cakes.data.model.CakeModel

interface CakeRepository {

    suspend fun getCakes(): List<CakeModel>

}