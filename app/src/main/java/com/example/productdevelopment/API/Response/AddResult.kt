package com.example.productdevelopment.API.Response

data class AddResult(
    val message: String,
    val product_details: ProductItem,
    val product_id: Int,
    val success: Boolean
)