package com.example.cakes.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cakes.data.model.CakeUiState
import com.example.cakes.presentation.screens.components.AnimatedCakeItem
import com.example.cakes.presentation.screens.components.ErrorContent
import com.example.cakes.presentation.viewmodel.CakesViewModel
import com.example.cakes.ui.theme.CakeBackground
import com.example.cakes.ui.theme.CakePeachBackground
import com.example.cakes.ui.theme.CakeTypography
import com.example.cakes.util.CakeConstants

const val PROGRESS_INDICATOR_TAG = "progress_indicator"

@Composable
fun CakeScreen(
    viewModel: CakesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CakeScreenContent(
        state = state,
        onRefresh = viewModel::refresh,
        onRetry = viewModel::retry
    )
}

@Composable
fun CakeScreenContent(
    state: CakeUiState,
    onRefresh: () -> Unit,
    onRetry: () -> Unit
) {
    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Log.d("Cake Screen", "Error: ${state.error}")
            ErrorContent (
                onRetry = onRetry
            )
        }

        else -> {
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize()
            ) {
                Scaffold(
                    modifier = Modifier.background(CakeBackground),
                    topBar = {
                        CakeTopBar()
                    }
                ) { paddingValues ->
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = paddingValues
                    ) {
                        val processedCakes = state.cakes
                            .distinctBy { it.title.lowercase() }
                            .sortedBy { it.title.lowercase() }

                        itemsIndexed(
                            items = processedCakes,
                            key = { _, movie -> movie.title }
                        ) { _, cake ->

                            AnimatedCakeItem(
                                cakeModel= cake,
                            )
                        }

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CakeTopBar() {
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = CakePeachBackground,
            scrolledContainerColor = CakePeachBackground,
            navigationIconContentColor = MaterialTheme.colors.primary,
            titleContentColor = MaterialTheme.colors.onSurface,
            actionIconContentColor = MaterialTheme.colors.onSurface,
            subtitleContentColor = MaterialTheme.colors.primary,
        ),
        title = {
            Text(
                text = CakeConstants.APP_NAME,
                style = CakeTypography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    )
}