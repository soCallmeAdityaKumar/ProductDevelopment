package com.example.productdevelopment.API

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class ImageGen(val context: Context) {

    fun generatePart(uri: Uri?):MultipartBody.Part{
        val filedir=context.applicationContext.filesDir
        val file= File(filedir,"image.jpg")
        val inputStream=context.contentResolver.openInputStream(uri!!)
        val fileOutputStream= FileOutputStream(file)
        inputStream!!.copyTo(fileOutputStream)

        val requestBody=file.asRequestBody("image/*".toMediaTypeOrNull())
        val part= MultipartBody.Part.createFormData("files[]",file.name,requestBody)
        return part
    }
}