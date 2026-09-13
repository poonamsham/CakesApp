package com.example.cakes.presentation.screens.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.cakes.data.model.CakeModel
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for the [CakeCard] component.
 */
class CakeCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Verifies that the cake title is correctly displayed on the card.
     */
    @Test
    fun cakeCard_showsTitle() {
        val cake = CakeModel("Lemon Cake", "Sour and sweet", "url")
        composeTestRule.setContent {
            CakeCard(cake)
        }

        composeTestRule.onNodeWithText("Lemon Cake").assertIsDisplayed()
    }

    /**
     * Verifies that clicking the card opens the detailed popup.
     */
    @Test
    fun cakeCard_opensPopupOnClick() {
        val cake = CakeModel("Lemon Cake", "Sour and sweet", "url")
        composeTestRule.setContent {
            CakeCard(cake)
        }

        // Simulate user click.
        composeTestRule.onNodeWithText("Lemon Cake").performClick()
        
        // Check if the detailed description from the popup is now visible.
        composeTestRule.onNodeWithText("Sour and sweet").assertIsDisplayed()
    }
}
