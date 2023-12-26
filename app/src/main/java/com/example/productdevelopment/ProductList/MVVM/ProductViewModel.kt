package com.example.productdevelopment.ProductList.MVVM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productdevelopment.ProductList.API.Response.AddResult
import com.example.productdevelopment.ProductList.API.Response.Product
import com.example.productdevelopment.ProductList.API.Response.ProductItem
import com.example.productdevelopment.ProductList.API.Result
import kotlinx.coroutines.launch

class ProductViewModel(private  val repo:Repository) :ViewModel(){

    //When GET Request
    private var itemList:MutableLiveData<Result<Product>> = MutableLiveData()
    val item:LiveData<Result<Product>>
        get() =itemList

    //When POST Request
    private val addItemlist:MutableLiveData<Result<AddResult>> = MutableLiveData()
    val addItem:LiveData<Result<AddResult>>
        get() =addItemlist

    suspend fun getItemVM()=viewModelScope.launch {
        itemList.value=repo.getItem()
    }
    suspend fun addItemVM(item:ProductItem)=viewModelScope.launch {
        addItemlist.value=repo.addItem(item)
    }


}