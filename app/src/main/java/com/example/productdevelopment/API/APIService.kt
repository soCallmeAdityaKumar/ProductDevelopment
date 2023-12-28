package com.example.productdevelopment.API

import com.example.productdevelopment.API.Response.AddResult
import com.example.productdevelopment.API.Response.Product
import com.example.productdevelopment.API.Response.ProductItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import java.io.File

interface APIService {

    @GET("get")
    suspend fun getProduct(): Product


    @Multipart
    @POST("add")
    suspend fun addProduct(@PartMap partMap: MutableMap<String, RequestBody>,
                           @Part file:MultipartBody.Part?): AddResult

}