package com.example.productdevelopment.Fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.content.ContentResolverCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.productdevelopment.API.APIService
import com.example.productdevelopment.API.ImageGen
import com.example.productdevelopment.API.Response.ProductItem
import com.example.productdevelopment.API.Result
import com.example.productdevelopment.API.Retrofit
import com.example.productdevelopment.MVVM.ProductViewModel
import com.example.productdevelopment.MVVM.Repository
import com.example.productdevelopment.MVVM.ViewModelfactory
import com.example.productdevelopment.R
import com.example.productdevelopment.databinding.FragmentAddProductBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.internal.`$Gson$Types`
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.sql.Time


class AddProduct : Fragment() {

    lateinit var binding: FragmentAddProductBinding
    lateinit var viewModel: ProductViewModel
    lateinit var productName:EditText
    lateinit var productPrice:EditText
    lateinit var productType:Spinner
    lateinit var productTax:EditText
    var part:MultipartBody.Part?=null
    var selectedImageUri:Uri?=null
    var selectedType=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAddProductBinding.inflate(layoutInflater,container,false)
        val view=binding.root


         productName=binding.ProductName
         productType=binding.ProductType
         productPrice=binding.ProductPrice
         productTax=binding.Tax

        val types=resources.getStringArray(R.array.ProductType)
        productType.adapter=ArrayAdapter(requireContext(),R.layout.snipper_back,types)
        productType.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedType=types[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        binding.backArrow.setOnClickListener {
            findNavController().navigate(R.id.action_addProduct_to_productList)
        }

        val apiService= Retrofit().buildRetrofit(APIService::class.java)
        val repo= Repository(apiService)
        viewModel=ViewModelProvider(requireActivity(), ViewModelfactory(repo)).get(ProductViewModel::class.java)


        binding.imageArea.setOnClickListener {
            checkPermission()
        }

        binding.submit.setOnClickListener {
            checkValue(it)
        }

        return view
    }

    private fun checkPermission() {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
           requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_CODE_PERMISSION)
        }

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }
        else {
            Snackbar.make(requireContext(),
                requireView(),"Request not granted to read internal storage ${grantResults[0]}",Snackbar.LENGTH_LONG).show()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
             selectedImageUri = data!!.data!!
            if(selectedImageUri!=null){
                val file= File(selectedImageUri.toString())
                binding.imageArea.setImageURI(selectedImageUri)
                binding.addImagePhoto.visibility=View.INVISIBLE
                Timber.d(TAG+file.absolutePath.toString())
            }
        }

    }

    private fun checkValue(it:View) {
        val name=productName.text.toString()
        val price=productPrice.text.toString()
        val tax=productTax.text.toString()

        if(name.isEmpty()){
            Snackbar.make(requireContext(),it,"Name should not be empty!!",Snackbar.LENGTH_LONG).show()
        }
        else if(!checkDigit(price)||!checkDigit(tax)){
            Timber.i(TAG+":inside the check digits")
            if(!checkDigit(price)&&!checkDigit(tax)) Snackbar.make(requireContext(),it,"Please enter valid Price and Tax !!",Snackbar.LENGTH_SHORT).show()
            else if(!checkDigit(price)) Snackbar.make(requireContext(),it,"Please enter valid Price !!",Snackbar.LENGTH_SHORT).show()
            else if(!checkDigit(tax)) Snackbar.make(requireContext(),it,"Please enter valid tax !!",Snackbar.LENGTH_SHORT).show()
        }
        else if(price.isNotEmpty() && name.isNotEmpty() && selectedType.isNotEmpty() && tax.isNotEmpty()){

            if(selectedImageUri!=null){
                part=ImageGen(it.context).generatePart(selectedImageUri)

            }

            CoroutineScope(Dispatchers.IO).launch {
                viewModel.addItemVM(ProductItem(selectedImageUri.toString(),price.toDouble(),name.toString(),selectedType,tax.toDouble()),part)
            }
            binding.progressbar.visibility=View.VISIBLE
            binding.progressbar.animate().start()

            viewModel.addItem.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Result.Success -> {
                        Timber.i(TAG + ":Product Added success " + it.value.product_id + " ")
                        binding.progressbar.visibility=View.INVISIBLE
                        binding.ProductName.text.clear()
                        binding.ProductPrice.text.clear()
                        binding.Tax.text.clear()
                        binding.imageArea.setImageBitmap(null)
                        binding.imageArea.background=resources.getDrawable(R.drawable.image)
                        binding.addImagePhoto.background=resources.getDrawable(R.drawable.baseline_add_photo_alternate_24)
                        val snack=Snackbar.make(requireContext(),requireView(),"Product added Successfully",1000)
                        snack.setAction("Close"){
                            snack.dismiss()
                        }
                            snack.show()
                    }
                    is Result.Failure -> {
                        binding.progressbar.visibility=View.INVISIBLE
                        Timber.i(TAG + " :" + it.errormessage + " " + it.isNetworkError + " " + it.errorCode)
                        Snackbar.make(requireContext(),requireView(),it.errormessage.toString(),Snackbar.LENGTH_SHORT).show()
                    }
                }
            })

        }
        else{
            Snackbar.make(requireContext(),it,"Please Fill All Details!!",Snackbar.LENGTH_LONG).show()
        }
    }

    private fun checkDigit(value:String):Boolean{
        return when{
            value.toIntOrNull()!=null->true
            value.toDoubleOrNull()!=null->true
            value.toFloatOrNull()!=null->true
            else ->false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.addItem.removeObserver{}
    }
    companion object {
        const val TAG="Add_Product"

        private const val REQUEST_CODE_PERMISSION = 123
        private  const val REQUEST_CODE_PICK_IMAGE=456
        }

    }