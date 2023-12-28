package com.example.productdevelopment.API

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun<T> apiCall(call:suspend ()->T): Result<T> {
        return withContext(Dispatchers.IO){
            try {
                Result.Success(call.invoke())
            }catch (e:Exception){
                when(e){
                    is HttpException->{
                        Result.Failure(true, e.code(), e.message())
                    }else->{
                    Result.Failure(false, null, null)
                    }
                }
            }
        }
    }
}