package com.example.serviciosapp.data.model

data class ServiceProvider(
    val id: String,
    val name: String,
    val trade: String,
    val rating: Double,
    val city: String,
    val description: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val badges: List<String> = emptyList(),
    val portfolioSwatches: List<Long> = emptyList(),
    val reviews: List<Review> = emptyList()
)
