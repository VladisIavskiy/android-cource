package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
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
        val currentProductId = 5L
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
            productName.setTextOrGone(details.name)
            realPriceText.setTextOrGone(priceLabel)
            rate.setTextOrGone(details.rating)
            cart.setTextOrGone(carts)
            discount.setTextOrGone(discountText)
            productDescription.setTextOrGone(details.description)

            oldPrice.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTextOrGone(oldPriceLabel)
            }

            Glide.with(root.context)
                .load(details.image?.url)
                .into(productMainImage)
        }
    }

    fun loadProductDetails(productId: Long) {
        binding.progressBar.visibility = View.VISIBLE
        binding.contentGroup.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE

        apiService.getProductDetails(productId).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                binding.progressBar.visibility = View.GONE
                val productDetails = response.body()
                if (productDetails?.requestError?.isNotEmpty() == true || productDetails == null) {
                    showError("Товар не найден", R.drawable.good_not_found)
                } else {
                    binding.contentGroup.visibility = View.VISIBLE
                    showProductDetails(productDetails)
                    loadAdditionalImages(productId)
                }
            }
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                showError("Нет подключения к интернету", R.drawable.no_internet)
            }
        })
    }

    private fun loadAdditionalImages(productId: Long) {
        apiService.getProductImages(productId).enqueue(object : Callback<ApiService.ProductImagesResponse> {
            override fun onResponse(call: Call<ApiService.ProductImagesResponse>, response: Response<ApiService.ProductImagesResponse>) {
                val imagesBody = response.body()
                if (imagesBody != null) {
                    showProductImages(imagesBody.images)
                }
            }

            override fun onFailure(call: Call<ApiService.ProductImagesResponse>, t: Throwable) {
                // не показываем ошибку, просто не загружаем картинки
            }
        })
    }
    private fun showProductImages(images: List<ProductImage>) {
        Log.d("MY_LOGGI", "showProductImages called with ${images.size} images")
        images.forEachIndexed { index, image ->
            Log.d("MY_LOGGI", "Image $index URL: ${image.url}")
        }
        // Используем контекст из binding.root
        val context = binding.root.context

        val imageViews = listOf(binding.productImage1, binding.productImage2, binding.productImage3)

        // Скрываем все ImageView в начале
        imageViews.forEach { it.visibility = View.GONE }

        if (images.isEmpty()) {
            binding.scrollImages.visibility = View.GONE
            return
        }

        // Показываем блок
        binding.scrollImages.visibility = View.VISIBLE

        images.forEachIndexed { index, productImage ->
            if (index < imageViews.size) {
                val imageView = imageViews[index]
                Glide.with(context)
                    .load(productImage.url)
                    .into(imageView)
                imageView.visibility = View.VISIBLE
            }
        }
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