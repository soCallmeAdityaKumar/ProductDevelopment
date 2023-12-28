package com.example.productdevelopment.API


sealed class Result<out T> {
    data class Success<out T>(val value:T): Result<T>()
    data class Failure(
        val isNetworkError:Boolean,
        val errorCode:Int?,
        val errormessage:String?
    ): Result<Nothing>()
}