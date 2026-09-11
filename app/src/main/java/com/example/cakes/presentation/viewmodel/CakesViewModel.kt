package com.example.cakes.presentation.viewmodel

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

@HiltViewModel
class CakesViewModel @Inject constructor(
    private val repository: CakeRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(CakeUiState())

    val uiState: StateFlow<CakeUiState> =
        _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isRefreshing = true)
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

//    fun loadCakes() {
//        viewModelScope.launch {
//            _uiState.update {
//                it.copy(
//                    isLoading = true,
//                    error = null
//                )
//            }
//            try {
//                val cakes = repository.getCakes()
//
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        cakes = cakes
//                    )
//                }
//            } catch (e: Exception) {
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        error = e.message
//                    )
//                }
//            }
//        }
//    }
}