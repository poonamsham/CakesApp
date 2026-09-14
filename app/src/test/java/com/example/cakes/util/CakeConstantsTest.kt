package com.example.cakes.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Trivial test to ensure [CakeConstants] are covered.
 */
class CakeConstantsTest {

    @Test
    fun testConstants() {
        assertNotNull(CakeConstants)
        assertEquals("Waracle/mobile-coding-test-api/refs/heads/main/cakes", CakeConstants.ENDPOINT)
        assertEquals("Dream Cakes", CakeConstants.APP_NAME)
    }
}
