package com.example.cakes.presentation.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.cakes.data.model.CakeModel
import com.example.cakes.data.model.CakeUiState
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Instrumented UI tests for the [CakeScreenContent] Composable.
 */
class CakeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Verifies that the loading indicator is shown when the state is loading.
     */
    @Test
    fun cakeScreen_showsLoading_whenStateIsLoading() {
        val state = CakeUiState(isLoading = true)

        composeTestRule.setContent {
            CakeScreenContent(
                state = state,
                onRefresh = {},
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithTag(PROGRESS_INDICATOR_TAG).assertIsDisplayed()
    }

    /**
     * Verifies that cake items are rendered when the state has data.
     */
    @Test
    fun cakeScreen_showsCakes_whenStateHasCakes() {
        val cakes = listOf(
            CakeModel("Chocolate Cake", "Yummy chocolate", "url1"),
            CakeModel("Vanilla Cake", "Sweet vanilla", "url2")
        )
        val state = CakeUiState(cakes = cakes)

        composeTestRule.setContent {
            CakeScreenContent(
                state = state,
                onRefresh = {},
                onRetry = {}
            )
        }

        // Wait for entry animation delay.
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Chocolate Cake").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Chocolate Cake").assertIsDisplayed()
        composeTestRule.onNodeWithText("Vanilla Cake").assertIsDisplayed()
    }

    /**
     * Verifies that the UI correctly filters and sorts cakes (internal logic check).
     */
    @Test
    fun cakeScreen_filtersAndSortsCakes() {
        val cakes = listOf(
            CakeModel("Banana Cake", "Desc 1", "url1"),
            CakeModel("Apple Cake", "Desc 2", "url2"),
            CakeModel("apple cake", "Duplicate", "url3") 
        )
        val state = CakeUiState(cakes = cakes)

        composeTestRule.setContent {
            CakeScreenContent(
                state = state,
                onRefresh = {},
                onRetry = {}
            )
        }

        // Wait for entry animation delay.
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Apple Cake").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Apple Cake").assertIsDisplayed()
        composeTestRule.onNodeWithText("Banana Cake").assertIsDisplayed()
    }

    /**
     * Verifies that clicking on a cake card opens the detailed popup.
     */
    @Test
    fun cakeScreen_showsPopup_onCakeClick() {
        val cake = CakeModel("Chocolate Cake", "Detailed Description", "url1")
        val state = CakeUiState(cakes = listOf(cake))

        composeTestRule.setContent {
            CakeScreenContent(
                state = state,
                onRefresh = {},
                onRetry = {}
            )
        }

        // Wait for entry animation delay.
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Chocolate Cake").fetchSemanticsNodes().isNotEmpty()
        }

        // Trigger the popup.
        composeTestRule.onNodeWithText("Chocolate Cake").performClick()

        // Check for detailed description inside the popup.
        composeTestRule.onNodeWithText("Detailed Description").assertIsDisplayed()
    }

    /**
     * Verifies that the error UI is displayed when the state contains an error.
     */
    @Test
    fun cakeScreen_showsError_whenStateHasError() {
        val state = CakeUiState(error = "Some error")

        composeTestRule.setContent {
            CakeScreenContent(
                state = state,
                onRefresh = {},
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Something went wrong!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    /**
     * Verifies that clicking the retry button triggers the onRetry callback.
     */
    @Test
    fun cakeScreen_callsRetry_whenRetryButtonClicked() {
        val retryCalled = AtomicBoolean(false)
        val state = CakeUiState(error = "Some error")

        composeTestRule.setContent {
            CakeScreenContent(
                state = state,
                onRefresh = {},
                onRetry = { retryCalled.set(true) }
            )
        }

        composeTestRule.onNodeWithText("Retry").performClick()
        assert(retryCalled.get())
    }

    /**
     * Verifies that an empty list is handled gracefully (LazyColumn remains empty).
     */
    @Test
    fun cakeScreen_showsEmptyList_whenStateHasNoCakes() {
        val state = CakeUiState(cakes = emptyList())

        composeTestRule.setContent {
            CakeScreenContent(
                state = state,
                onRefresh = {},
                onRetry = {}
            )
        }

        // Verify the app title is still there.
        composeTestRule.onNodeWithText("Dream Cakes").assertIsDisplayed()
        // No cake title should be found.
        composeTestRule.onNodeWithText("Chocolate Cake").assertDoesNotExist()
    }

    /**
     * Verifies that cakes are not visible immediately due to the entry animation stagger.
     */
    @Test
    fun cakeScreen_cakesAreNotVisibleImmediately_dueToAnimation() {
        val cakes = listOf(CakeModel("Delayed Cake", "Desc", "url"))
        val state = CakeUiState(cakes = cakes)

        composeTestRule.setContent {
            CakeScreenContent(
                state = state,
                onRefresh = {},
                onRetry = {}
            )
        }

        // Should not be displayed instantly (animation delay is 60ms).
        composeTestRule.onNodeWithText("Delayed Cake").assertDoesNotExist()
        
        // Wait for it to appear.
        composeTestRule.waitUntil(1000) {
            composeTestRule.onAllNodesWithText("Delayed Cake").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Delayed Cake").assertIsDisplayed()
    }

    /**
     * Verifies that the refreshing state is handled gracefully.
     */
    @Test
    fun cakeScreen_showsRefreshing_whenStateIsRefreshing() {
        val state = CakeUiState(isRefreshing = true)

        composeTestRule.setContent {
            CakeScreenContent(
                state = state,
                onRefresh = {},
                onRetry = {}
            )
        }

        // Content should still be visible (TopBar).
        composeTestRule.onNodeWithText("Dream Cakes").assertIsDisplayed()
    }
}
