package com.example.cakes.presentation.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cakes.R
import com.example.cakes.data.model.CakeUiState
import com.example.cakes.presentation.screens.components.AnimatedCakeItem
import com.example.cakes.presentation.screens.components.ErrorContent
import com.example.cakes.presentation.viewmodel.CakesViewModel
import com.example.cakes.ui.theme.CakeBackground
import com.example.cakes.ui.theme.CakePeachBackground
import com.example.cakes.ui.theme.CakeTypography
import com.example.cakes.util.CakeConstants

/**
 * Test tag used to identify the progress indicator in UI tests.
 */
const val PROGRESS_INDICATOR_TAG = "progress_indicator"

/**
 * Main screen Composable that connects the ViewModel to the UI content.
 *
 * @param viewModel The [CakesViewModel] provided by Hilt.
 */
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

/**
 * Stateless content of the Cake Screen, responsible for layout and state rendering.
 *
 * @param state The current [CakeUiState] to render.
 * @param onRefresh Callback triggered when a pull-to-refresh is performed.
 * @param onRetry Callback triggered when the retry button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CakeScreenContent(
    state: CakeUiState,
    onRefresh: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        modifier = Modifier.background(CakeBackground),
        topBar = {
            CakeTopBar()
        }
    ) { paddingValues ->
        when(state) {
            CakeUiState.Loading -> {
                // Displays a loading spinner centered on the screen.
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .testTag(PROGRESS_INDICATOR_TAG),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

           is CakeUiState.Error -> {
                // Displays an error message and a retry button.
                Log.d("Cake Screen", "Error: ${state.message}")
                Box(modifier = Modifier.padding(paddingValues)) {
                    ErrorContent(
                        onRetry = onRetry
                    )
                }
            }
            CakeUiState.Empty -> {
                Text("No cakes available")
            }

            is CakeUiState.Content -> {
                // Displays the list of cakes with pull-to-refresh support.
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = onRefresh,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {

                        itemsIndexed(
                            items = state.cakes,
                            key = { _, movie -> movie.title }
                        ) { _, cake ->

                            // Renders each cake with an entry animation.
                            AnimatedCakeItem(
                                cakeModel = cake,
                            )
                        }

                    }
                }
            }
        }
    }
}

/**
 * Custom TopAppBar for the Cake Screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CakeTopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CakePeachBackground,
            scrolledContainerColor = CakePeachBackground,
            navigationIconContentColor = Color.Black,
            titleContentColor = CakeBackground,
            actionIconContentColor = Color.Black
        ),
        title = {
            Text(
                text = CakeConstants.APP_NAME,
                style = CakeTypography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        // Image on top-right
        actions = {
            Image(
                painter = painterResource(
                    id = R.drawable.cake_icon
                ),
                contentDescription = "Cake logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(end = 24.dp)
                    .size(30.dp)
            )
        }

    )
}
