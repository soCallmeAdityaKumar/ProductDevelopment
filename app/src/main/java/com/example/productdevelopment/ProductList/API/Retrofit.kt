package com.example.productdevelopment.ProductList.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class  Retrofit {
    companion object{
         private const val BASE_URL = "https://app.getswipe.in/api/public"
    }

    fun  <api> buildRetrofit (apiService:Class<api>):api {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(apiService)

    }

}