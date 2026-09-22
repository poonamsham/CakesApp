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
        MutableStateFlow<CakeUiState>(CakeUiState.Loading)

    val uiState: StateFlow<CakeUiState> =
        _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCakes()
    }

    fun retry() {
        loadCakes()
    }

    fun loadCakes() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {

            _uiState.value = CakeUiState.Loading

            try {
                val cakes = repository.getCakes()

                _uiState.value =
                    if (cakes.isEmpty()) {
                        CakeUiState.Empty
                    } else {
                        CakeUiState.Content(
                            cakes = cakes
                        )
                    }

            } catch (e: CancellationException) {
                throw e

            } catch (e: IOException) {
                _uiState.value = CakeUiState.Error(
                    message = "Please check your internet connection"
                )

            } catch (e: HttpException) {
                _uiState.value = CakeUiState.Error(
                    message = "Server error. Please try again."
                )

            } catch (e: Exception) {
                _uiState.value = CakeUiState.Error(
                    message = "Something went wrong"
                )
            }
        }
    }

    fun refresh() {

        val currentState = _uiState.value

        if (currentState !is CakeUiState.Content) {
            loadCakes()
            return
        }

        loadJob?.cancel()

        loadJob = viewModelScope.launch {

            _uiState.value = currentState.copy(
                isRefreshing = true,
                refreshError = null
            )

            try {
                val cakes = repository.getCakes()

                _uiState.value =
                    if (cakes.isEmpty()) {
                        CakeUiState.Empty
                    } else {
                        CakeUiState.Content(
                            cakes = cakes,
                            isRefreshing = false
                        )
                    }

            } catch (e: CancellationException) {
                throw e

            } catch (e: IOException) {
                _uiState.value = currentState.copy(
                    isRefreshing = false,
                    refreshError = "Please check your internet connection"
                )

            } catch (e: HttpException) {
                _uiState.value = currentState.copy(
                    isRefreshing = false,
                    refreshError = "Unable to refresh cakes"
                )

            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isRefreshing = false,
                    refreshError = "Something went wrong"
                )
            }
        }
    }
}
