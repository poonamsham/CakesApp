package com.example.cakes.data.model

/**
 * UI State for the Cake Screen.
 *
 * @property isLoading Indicates if the initial data is being loaded.
 * @property cakes The list of [CakeModel] objects to display.
 * @property error An optional error message if data fetching fails.
 * @property isRefreshing Indicates if a pull-to-refresh operation is in progress.
 */
data class CakeUiState(
    val isLoading: Boolean = false,
    val cakes: List<CakeModel> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val refreshError: String? = null
)
