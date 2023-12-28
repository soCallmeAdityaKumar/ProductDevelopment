package com.example.productdevelopment.Fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.productdevelopment.API.Response.Product
import com.example.productdevelopment.API.Response.ProductItem
import com.example.productdevelopment.R
import com.example.productdevelopment.databinding.FragmentProductListBinding
import com.example.productdevelopment.databinding.ProductListitemBinding
import timber.log.Timber

class RVAdapter(var ItemList:ArrayList<ProductItem>):RecyclerView.Adapter<RVAdapter.MyViewHolder>() {
    class MyViewHolder(val binding:ProductListitemBinding) :RecyclerView.ViewHolder(binding.root) {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ProductListitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(ItemList[position]){
                binding.productName.text=this.product_name
                binding.productName.isSelected=true
                binding.productType.text=this.product_type
                binding.tax.text="Tax: ${this.tax.toString()}%"
                binding.price.text="Rs. ${this.price.toString()}"
                if(this.image.isNotEmpty()){
                    Glide.with(binding.root)
                        .load(this.image)
                        .centerCrop()
                        .into(binding.imageView)
                }else{
                    Glide.with(binding.root)
                        .load("https://idestiny.in/wp-content/uploads/2023/09/iPhone_15_Pink_PDP_Image_Position-1__WWEN.jpg")
                        .centerCrop()
                        .into(binding.imageView)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return ItemList.size
    }
    fun refreshData(newData: ArrayList<ProductItem>){
        ItemList=newData
    }
}
