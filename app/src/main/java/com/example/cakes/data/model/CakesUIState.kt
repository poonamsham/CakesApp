package com.example.cakes.data.model

/**
 * UI State for the Cake Screen.
 *
 * @param Loading Indicates if the initial data is being loaded.
 * @property cakes The list of [CakeModel] objects to display.
 * @property error An optional error message if data fetching fails.
 * @property isRefreshing Indicates if a pull-to-refresh operation is in progress.
 */
sealed interface CakeUiState {

    data object Loading : CakeUiState

    data class Content(
        val cakes: List<CakeModel>,
        val isRefreshing: Boolean = false,
        val refreshError: String? = null
    ) : CakeUiState

    data class Error(
        val message: String
    ) : CakeUiState

    data object Empty : CakeUiState
}