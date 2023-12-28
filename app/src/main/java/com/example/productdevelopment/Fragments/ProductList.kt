package com.example.productdevelopment.Fragments

import android.animation.ObjectAnimator
import android.nfc.Tag
import android.os.Bundle
import android.text.style.TtsSpan.TimeBuilder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.productdevelopment.API.APIService
import com.example.productdevelopment.API.Response.Product
import com.example.productdevelopment.API.Response.ProductItem
import com.example.productdevelopment.API.Result
import com.example.productdevelopment.API.Retrofit
import com.example.productdevelopment.MVVM.ProductViewModel
import com.example.productdevelopment.MVVM.Repository
import com.example.productdevelopment.MVVM.ViewModelfactory
import com.example.productdevelopment.R
import com.example.productdevelopment.databinding.FragmentProductListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

class ProductList : Fragment() {

    lateinit var adapater:RVAdapter
    lateinit var binding:FragmentProductListBinding
    lateinit var productList:ArrayList<ProductItem>
    var filteredData:ArrayList<ProductItem> = arrayListOf()
    lateinit var viewModel:ProductViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentProductListBinding.inflate(inflater,container,false)
        val view=binding.root

        binding.FAB.setOnClickListener {
            findNavController().navigate(R.id.action_productList_to_addProduct)
        }
            binding.Progressbar.visibility=View.VISIBLE
            binding.productRecyclerView.visibility=View.INVISIBLE
            binding.Progressbar.animate()



        val apiService=Retrofit().buildRetrofit(APIService::class.java)
        val repo=Repository(apiService)
        viewModel=ViewModelProvider(requireActivity(),ViewModelfactory(repo)).get(ProductViewModel::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("ProductList","calling getItemVm()")
            viewModel.getItemVM()
        }
        viewModel.item.observe(viewLifecycleOwner,Observer{
            when(it){
                is Result.Success->{
                    Log.d("ProductList",it.value.toString())

                    productList=it.value
                    binding.productRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    if(productList.isNotEmpty()){
                        binding.productRecyclerView.visibility=View.VISIBLE
                        binding.Progressbar.animate().cancel()
                        binding.Progressbar.visibility=View.INVISIBLE
                    }
                    adapater=RVAdapter(productList)
                    binding.productRecyclerView.adapter=adapater
                }
                is Result.Failure->{
                    Timber.d(it.errormessage.toString())
                    Toast.makeText(requireContext(),"Failed to get Result",Toast.LENGTH_LONG).show()
                }

            }
        })
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredData.clear()
                val searchtext = newText!!.toLowerCase(Locale.getDefault())
                Log.i(TAG,searchtext)
                if (searchtext.isNotEmpty()) {
                    Log.i(TAG,searchtext)
                    productList.forEach {
                        if (it.product_name.lowercase(Locale.getDefault()).contains(searchtext)||
                            it.product_type.lowercase(Locale.getDefault()).contains(searchtext)) {
                            filteredData.add(it)
                        }
                    }
                    adapater.refreshData(filteredData)
                    adapater.notifyDataSetChanged()

                } else {
                    filteredData.clear()
                        filteredData.addAll(productList)
                    adapater.refreshData(filteredData)
                    adapater.notifyDataSetChanged()

                }
                return false
            }

        })

        return view

    }

    companion object{
        const val  TAG="ProductList"
    }
}