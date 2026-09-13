package com.example.cakes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cakes.data.model.CakeUiState
import com.example.cakes.repository.CakeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the Cake catalog screen.
 * Handles data fetching, error states, and user interactions like refresh and retry.
 *
 * @property repository The repository used to fetch cake data.
 */
@HiltViewModel
class CakesViewModel @Inject constructor(
    private val repository: CakeRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(CakeUiState(isLoading = true))

    /**
     * Observable [StateFlow] of the [CakeUiState].
     */
    val uiState: StateFlow<CakeUiState> =
        _uiState.asStateFlow()

    init {
        // Automatically fetch data on initialization.
        loadCakes()
    }

    /**
     * Internal helper to load cakes from the repository.
     * Updates [uiState] with loading, success, or error status.
     */
    private fun loadCakes() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                val cakes = repository.getCakes()
                Log.d(
                    "CakesViewModel",
                    "getCakes -${cakes}",
                )
                _uiState.update {
                    it.copy(
                        cakes = cakes,
                        isRefreshing = false,
                        isLoading = false,
                    )
                }
            } catch (e: Exception) {
                Log.e(
                    "CakesViewModel",
                    "getCakes failed",
                    e
                )
                _uiState.update {
                    it.copy(isRefreshing = false, error = e.message, isLoading = false)
                }
            }
        }
    }

    /**
     * Re-triggers data loading when the user clicks the retry button in an error state.
     */
    fun retry() {
        loadCakes()
    }

    /**
     * Performs a pull-to-refresh operation.
     * Clears the current list and shows a refreshing indicator while fetching new data.
     */
    fun refresh() {
        viewModelScope.launch {

            // 1. Clear existing data immediately to show fresh load
            _uiState.update {
                it.copy(
                    cakes = emptyList(),
                    isRefreshing = true,
                    error = null
                )
            }

            try {
                val cakes = repository.getCakes()

                _uiState.update {
                    it.copy(
                        cakes = cakes,
                        isRefreshing = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isRefreshing = false)
                }
            }
        }
    }

}
