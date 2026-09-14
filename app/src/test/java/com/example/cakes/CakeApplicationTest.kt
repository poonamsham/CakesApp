package com.example.cakes

import org.junit.Assert.assertNotNull
import org.junit.Test

class CakeApplicationTest {

    @Test
    fun testApplicationCreation() {
        val app = CakeApplication()
        assertNotNull(app)
    }
}
