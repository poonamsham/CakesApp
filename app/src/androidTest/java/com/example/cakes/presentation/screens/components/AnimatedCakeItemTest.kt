package com.example.cakes.presentation.screens.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import com.example.cakes.data.model.CakeModel
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for the [AnimatedCakeItem] component.
 */
class AnimatedCakeItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Verifies that the component content eventually appears after the entry animation.
     */
    @Test
    fun animatedCakeItem_eventuallyShowsContent() {
        val cake = CakeModel("Animated Cake", "Description", "url")
        composeTestRule.setContent {
            AnimatedCakeItem(cakeModel = cake)
        }

        // Wait for the internal animation delay and fade-in to complete.
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Animated Cake").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Animated Cake").assertIsDisplayed()
    }
}
