package com.example.cakes.presentation.screens.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.cakes.data.model.CakeModel
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

/**
 * UI tests for the [CakePopup] component.
 */
class CakePopupTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Verifies that the popup correctly displays the cake's title and description.
     */
    @Test
    fun cakePopup_showsCakeDetails() {
        val cake = CakeModel("Delicious Cake", "This is a very delicious cake.", "url")
        composeTestRule.setContent {
            CakePopup(cakeModel = cake, onDismiss = {})
        }

        composeTestRule.onNodeWithText("Delicious Cake").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is a very delicious cake.").assertIsDisplayed()
    }

    /**
     * Verifies that clicking the close button triggers the dismissal callback.
     */
    @Test
    fun cakePopup_callsOnDismiss_whenCloseButtonClicked() {
        val dismissCalled = AtomicBoolean(false)
        val cake = CakeModel("Cake", "Desc", "url")
        composeTestRule.setContent {
            CakePopup(cakeModel = cake, onDismiss = { dismissCalled.set(true) })
        }

        // "×" is the text used for the close button.
        composeTestRule.onNodeWithText("×").performClick()

        assert(dismissCalled.get())
    }
}
