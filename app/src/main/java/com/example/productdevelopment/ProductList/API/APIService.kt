package com.example.productdevelopment.ProductList.API

import com.example.productdevelopment.ProductList.API.Response.AddResult
import com.example.productdevelopment.ProductList.API.Response.Product
import com.example.productdevelopment.ProductList.API.Response.ProductItem
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {

    @GET("/get")
    suspend fun getProduct():Product

    @POST("/add")
    suspend fun addProduct(item: ProductItem):AddResult

}