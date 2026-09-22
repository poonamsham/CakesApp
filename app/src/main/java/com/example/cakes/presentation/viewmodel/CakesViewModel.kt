package com.example.cakes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cakes.data.model.CakeUiState
import com.example.cakes.repository.CakeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

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
    val uiState: StateFlow<CakeUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCakes()
    }

    fun retry() {
        loadCakes(isRefresh = false)
    }

    fun refresh() {
        loadCakes(isRefresh = true)
    }

    fun clearRefreshError() {
        _uiState.update {
            it.copy(refreshError = null)
        }
    }

    private fun loadCakes(isRefresh: Boolean = false) {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {

            _uiState.update { currentState ->
                if (isRefresh) {
                    currentState.copy(
                        isRefreshing = true,
                        refreshError = null
                    )
                } else {
                    currentState.copy(
                        isLoading = true,
                        error = null
                    )
                }
            }

            try {
                val cakes = repository.getCakes()

                _uiState.update {
                    it.copy(
                        cakes = cakes,
                        isLoading = false,
                        isRefreshing = false,
                        error = null,
                        refreshError = null
                    )
                }

            } catch (e: CancellationException) {
                // Cancellation is part of normal coroutine behaviour.
                throw e

            } catch (e: IOException) {
                handleError(
                    isRefresh = isRefresh,
                    message = e.message ?: "Unable to connect. Check your internet connection."
                )

            } catch (e: HttpException) {
                handleError(
                    isRefresh = isRefresh,
                    message = e.message ?: "Server error. Please try again."
                )

            } catch (e: Exception) {
                handleError(
                    isRefresh = isRefresh,
                    message = e.message ?: "Something went wrong. Please try again."
                )
            }
        }
    }

    private fun handleError(
        isRefresh: Boolean,
        message: String
    ) {
        _uiState.update { currentState ->

            if (isRefresh) {
                // Keep existing cakes visible.
                currentState.copy(
                    isRefreshing = false,
                    refreshError = message
                )
            } else {
                currentState.copy(
                    isLoading = false,
                    error = message
                )
            }
        }
    }
}
