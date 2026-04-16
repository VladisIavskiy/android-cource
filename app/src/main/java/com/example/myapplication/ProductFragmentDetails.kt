package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentProductDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductFragmentDetails : Fragment(R.layout.fragment_product_details) {


    private val apiService = retrofit.create(ApiService::class.java)
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("UseCompatTextViewDrawableApis")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadProductDetails(1)
        _binding = FragmentProductDetailsBinding.bind(view)

        binding.crossedOutPriceText.paintFlags = binding.crossedOutPriceText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        binding.buttonBuy.setOnClickListener {
            // Создаём новый фрагмент (PostFragment)
            val fragment = PostFragment()

            // Заменяем текущий фрагмент на PostFragment в том же контейнере
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)   // используем тот же ID контейнера, что и в Activity
                .addToBackStack(null)
                .commit()
        }

    }

    private fun showProductDetails(details: ProductDetails) {
        Glide.with(requireContext())
            .load(details.image.url)
            .into(binding.productMainImage)
        binding.productName.text = details.name
        binding.productDescription.text = details.description
    }

    private fun loadProductDetails(productId: Long) {
        val productDetailsCall = apiService.getProductDetails(productId)
        productDetailsCall.enqueue(object : Callback<ProductDetails> {

            override fun onResponse(
                call: Call<ProductDetails>,
                response: Response<ProductDetails>,
            ) {
                val productDetails = response.body() ?: return
                showProductDetails(productDetails)
            }
            override fun onFailure(call: Call<ProductDetails>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}