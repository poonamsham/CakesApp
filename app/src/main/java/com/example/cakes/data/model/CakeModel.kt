package com.example.cakes.data.model

/**
 * Data class representing a single Cake entry.
 *
 * @property title The name of the cake.
 * @property desc A detailed description of the cake.
 * @property image The URL string for the cake's image.
 */
data class CakeModel (
    val title: String,
    val desc: String,
    val image: String,
)
