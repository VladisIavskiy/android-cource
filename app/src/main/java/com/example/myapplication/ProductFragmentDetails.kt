package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.View
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
        _binding = FragmentProductDetailsBinding.bind(view)
        val currentProductId = 2L
        loadProductDetails(currentProductId)

        binding.buttonBuy.setOnClickListener {
            // Создаём новый фрагмент (PostFragment)
            val fragment = PostFragment()

            // Заменяем текущий фрагмент на PostFragment в том же контейнере
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)   // используем тот же ID контейнера, что и в Activity
                .addToBackStack("MAIN_SCREEN")
                .commit()
        }

        binding.retryButton.setOnClickListener {
            loadProductDetails(currentProductId)
        }

        binding.productDescription.setOnClickListener {
            val isExpanded = binding.productDescription.maxLines == Integer.MAX_VALUE
            binding.productDescription.maxLines = if (isExpanded) 2 else Integer.MAX_VALUE
        }
    }

    private fun showProductDetails(details: ProductResponse) {

        // Подготовка данных
        val priceLabel = details.price?.let {
            "$it ₽" + (details.priceUnit?.let { unit -> " / $unit" } ?: "")
        }

        val carts = listOfNotNull(details.cartStep, details.cartUnit)
            .joinToString(" ")
            .takeIf { it.isNotEmpty() }

        val discountText = details.discount?.let { "-$it%" }
        val oldPriceLabel = details.oldPrice?.let { "$it ₽" }

        // Поехали сеттеры
        with(binding) {
            productName.setTextOrInvisible(details.name)
            realPriceText.setTextOrInvisible(priceLabel)
            rate.setTextOrInvisible(details.rating)
            cart.setTextOrInvisible(carts)
            discount.setTextOrInvisible(discountText)
            productDescription.setTextOrInvisible(details.description)

            oldPrice.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTextOrInvisible(oldPriceLabel)
            }

            Glide.with(root.context)
                .load(details.image?.url)
                .into(productMainImage)
        }
    }

    private fun loadProductDetails(productId: Long) {
        // Перед началом запроса: показываем крутилку, прячем всё остальное
        binding.progressBar.visibility = View.VISIBLE
        binding.contentGroup.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE

        val productDetailsCall = apiService.getProductDetails(productId)
        productDetailsCall.enqueue(object : Callback<ProductResponse> {

            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                // Ответ пришел — убираем крутилку
                binding.progressBar.visibility = View.GONE

                val productDetails = response.body()
                if (productDetails?.requestError?.isNotEmpty() == true || productDetails == null) {
                    showError("Товар не найден", R.drawable.good_not_found)
                } else {
                    binding.contentGroup.visibility = View.VISIBLE
                    showProductDetails(productDetails)
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                // Ошибка сети
                binding.progressBar.visibility = View.GONE
                showError("Нет подключения к интернету", R.drawable.no_internet)
            }
        })
    }

    private fun showError(message: String, imageRes: Int) {
        with(binding) {
            contentGroup.visibility = View.GONE
            errorLayout.visibility = View.VISIBLE
            errorText.text = message
            errorImg.setImageResource(imageRes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}