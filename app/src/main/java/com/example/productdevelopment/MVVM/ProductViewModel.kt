package com.example.productdevelopment.MVVM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productdevelopment.API.Response.AddResult
import com.example.productdevelopment.API.Response.Product
import com.example.productdevelopment.API.Response.ProductItem
import com.example.productdevelopment.API.Result
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
import java.io.File


class ProductViewModel(private  val repo: Repository) :ViewModel(){

    //When GET Request
    private var itemList:MutableLiveData<Result<Product>> = MutableLiveData()
    val item:LiveData<Result<Product>>
        get() =itemList

    //When POST Request
    private val addItemlist:MutableLiveData<Result<AddResult>> = MutableLiveData()
    val addItem:LiveData<Result<AddResult>>
        get() =addItemlist


    val map: MutableMap<String, RequestBody> = mutableMapOf()
    var multipartImage: MultipartBody.Part? = null

    suspend fun getItemVM()=viewModelScope.launch {
        itemList.value=repo.getItem()
    }
    suspend fun addItemVM(item:ProductItem, @Part file:MultipartBody.Part? )=viewModelScope.launch {
        val name=createPartFromString(item.product_name)
        val type=createPartFromString(item.product_type)
        val price=createPartFromString(item.price.toString())
        val tax=createPartFromString(item.tax.toString())
        map.put("product_name",name)
        map.put("product_type",type)
        map.put("price",price)
        map.put("tax",tax)

        addItemlist.value=repo.addItem(map,file)
    }

    fun createPartFromString(stringData: String): RequestBody {
        return stringData.toRequestBody("text/plain".toMediaTypeOrNull())
    }



}