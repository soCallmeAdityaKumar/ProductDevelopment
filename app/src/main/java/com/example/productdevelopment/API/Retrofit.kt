package com.example.productdevelopment.API

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class  Retrofit {
    companion object{
         private const val BASE_URL = "https://app.getswipe.in/api/public/"
    }

    fun  <api> buildRetrofit (apiService:Class<api>):api {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().also {builder->
                val interceptor=HttpLoggingInterceptor()
                interceptor.level=HttpLoggingInterceptor.Level.BASIC
                builder.addInterceptor(interceptor)
                builder.callTimeout(30,TimeUnit.SECONDS)
                builder.readTimeout(30,TimeUnit.SECONDS)

            }.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(apiService)

    }

}