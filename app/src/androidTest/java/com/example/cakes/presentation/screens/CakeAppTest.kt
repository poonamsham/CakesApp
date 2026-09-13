package com.example.cakes.presentation.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.cakes.MainActivity
import com.example.cakes.util.CakeConstants
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

/**
 * End-to-end instrumented test for the main application entry point.
 * Uses [HiltAndroidTest] to enable dependency injection in tests.
 */
@HiltAndroidTest
class CakeAppTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    /**
     * Verifies that the app launches successfully and displays its name in the top bar.
     */
    @Test
    fun cakeApp_launches_andShowsAppName() {
        // Checks if the application name defined in constants is visible on the screen.
        composeTestRule.onNodeWithText(CakeConstants.APP_NAME).assertIsDisplayed()
    }
}
