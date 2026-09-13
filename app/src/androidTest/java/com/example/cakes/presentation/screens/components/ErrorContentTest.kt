package com.example.cakes.presentation.screens.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

/**
 * UI tests for the [ErrorContent] component.
 */
class ErrorContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Verifies that the error message and retry button are visible.
     */
    @Test
    fun errorContent_showsMessageAndRetryButton() {
        composeTestRule.setContent {
            ErrorContent(onRetry = {})
        }

        composeTestRule.onNodeWithText("Something went wrong!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    /**
     * Verifies that clicking the retry button correctly triggers the onRetry callback.
     */
    @Test
    fun errorContent_callsOnRetryWhenClicked() {
        val retryCalled = AtomicBoolean(false)
        composeTestRule.setContent {
            ErrorContent(onRetry = { retryCalled.set(true) })
        }

        composeTestRule.onNodeWithText("Retry").performClick()
        assert(retryCalled.get())
    }
}
