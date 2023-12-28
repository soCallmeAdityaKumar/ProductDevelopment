package com.example.productdevelopment.Fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddProductLiveData: ViewModel() {
    val productName=MutableLiveData<String>()

}