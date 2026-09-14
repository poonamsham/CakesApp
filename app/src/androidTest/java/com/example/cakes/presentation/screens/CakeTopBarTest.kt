package com.example.cakes.presentation.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.example.cakes.util.CakeConstants
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for the [CakeTopBar] Composable.
 */
class CakeTopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cakeTopBar_showsAppNameAndLogo() {
        composeTestRule.setContent {
            CakeTopBar()
        }

        // Verify app name.
        composeTestRule.onNodeWithText(CakeConstants.APP_NAME).assertIsDisplayed()
        
        // Verify logo exists via content description.
        composeTestRule.onNodeWithContentDescription("Cake logo").assertIsDisplayed()
    }
}
