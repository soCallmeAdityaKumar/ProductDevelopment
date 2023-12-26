package com.example.productdevelopment.ProductList.MVVM

import com.example.productdevelopment.ProductList.API.APIService
import com.example.productdevelopment.ProductList.API.BaseRepository
import com.example.productdevelopment.ProductList.API.Response.ProductItem

class Repository(private val apiService: APIService):BaseRepository() {
    suspend fun  getItem()=apiCall {
            apiService.getProduct()
        }

    suspend fun addItem(item: ProductItem)=apiCall {
            apiService.addProduct(item)
        }

}