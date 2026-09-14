package com.example.cakes.di

import com.example.cakes.repository.CakeRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DITest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: CakeRepository

    @Test
    fun testDependencyInjection() {
        hiltRule.inject()
        assertNotNull(repository)
    }
}
