package com.example.cakes.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cakes.presentation.screens.components.CakeCard
import com.example.cakes.presentation.viewmodel.CakesViewModel
import com.example.cakes.ui.theme.CakeBackground
import com.example.cakes.ui.theme.CakePeachBackground
import com.example.cakes.ui.theme.CakeTypography
import com.example.cakes.util.CakeConstants

@Composable
fun CakeScreen(
    viewModel: CakesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when {
        state.isLoading -> {
            // todo in cernter of the screen
            CircularProgressIndicator()
        }

        state.error != null -> {
            Text(
                text = state.error ?: "Something went wrong"
            )
        }

        else -> {
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = viewModel::refresh,
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
                        items(processedCakes) { cake ->
                            CakeCard(cake)
                            Divider()
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