package com.example.cakes.data.model

data class CakeUiState(
    val isLoading: Boolean = false,
    val cakes: List<CakeModel> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
)