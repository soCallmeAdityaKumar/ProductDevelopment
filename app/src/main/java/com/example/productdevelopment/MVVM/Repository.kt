package com.example.productdevelopment.MVVM

import com.example.productdevelopment.API.APIService
import com.example.productdevelopment.API.BaseRepository
import com.example.productdevelopment.API.Response.ProductItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap
import java.io.File

class Repository(private val apiService: APIService): BaseRepository() {
    suspend fun  getItem()=apiCall {
            apiService.getProduct()
        }

    suspend fun addItem(@PartMap partMap: MutableMap<String, RequestBody>, @Part file: MultipartBody.Part?)=apiCall {
            apiService.addProduct(partMap,file)
        }

}