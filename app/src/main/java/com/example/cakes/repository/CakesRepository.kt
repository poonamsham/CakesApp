package com.example.cakes.repository

import com.example.cakes.data.model.CakeModel

/**
 * Interface defining data operations for Cakes.
 * Decouples the UI and ViewModel from specific data source implementations.
 */
interface CakeRepository {

    /**
     * Retrieves the list of cakes.
     * @return A list of [CakeModel] objects.
     */
    suspend fun getCakes(): List<CakeModel>

}
