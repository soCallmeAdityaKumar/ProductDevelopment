package com.example.productdevelopment.API.Response

data class ProductItem(
    val image: String,
    val price: Double,
    val product_name: String,
    val product_type: String,
    val tax: Double
)