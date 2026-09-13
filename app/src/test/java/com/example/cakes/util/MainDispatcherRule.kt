package com.example.cakes.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A JUnit rule that overrides the Main dispatcher during tests.
 * This allows testing ViewModels or other components that use [Dispatchers.Main].
 *
 * @property testDispatcher The dispatcher to use as Main. Defaults to [UnconfinedTestDispatcher].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        // Redirect Dispatchers.Main to the test dispatcher.
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        // Revert Dispatchers.Main to its original state.
        Dispatchers.resetMain()
    }
}
/**
 * Run tests with:
 * ./gradlew :app:testDebugUnitTest :app:connectedDebugAndroidTest
 */
