package com.example.myapplication

import android.app.Dialog
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentProductDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductFragmentDetails : Fragment(R.layout.fragment_product_details) {

    private val apiService = retrofit.create(ApiService::class.java)

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val currentProductId = 0L

    private val productImagesAdapter = ProductImagesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProductDetailsBinding.bind(view)

        setupRecyclerView()
        setupClickListeners()

        loadProductDetails(currentProductId)
    }

    private fun setupRecyclerView() {
        binding.productImagesView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = productImagesAdapter
        }

        binding.productImagesView.adapter = productImagesAdapter

        // Перехватываем клик из адаптера
        productImagesAdapter.onImageClickListener = { productImage ->
            showFullScreenImage(productImage.url)
        }
    }

    private fun setupClickListeners() {

        binding.buttonBuy.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PostFragment())
                .addToBackStack("MAIN_SCREEN")
                .commit()
        }

        binding.retryButton.setOnClickListener {
            loadProductDetails(currentProductId)
        }

        binding.productDescription.setOnClickListener {
            val expanded =
                binding.productDescription.maxLines == Int.MAX_VALUE

            binding.productDescription.maxLines =
                if (expanded) 2 else Int.MAX_VALUE
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            goodGroup.visibility = View.GONE
            errorLayout.visibility = View.GONE
        }
    }

    private fun showContent() {
        with(binding) {
            progressBar.visibility = View.GONE
            goodGroup.visibility = View.VISIBLE
            errorLayout.visibility = View.GONE
        }
    }

    private fun showError(message: String, imageRes: Int) {
        with(binding) {
            progressBar.visibility = View.GONE
            goodGroup.visibility = View.GONE
            errorLayout.visibility = View.VISIBLE

            errorText.text = message
            errorImg.setImageResource(imageRes)
        }
    }

    private fun loadProductDetails(productId: Long) {

        Log.d("API_TEST", "Loading product: $productId")

        showLoading()

        apiService.getProductDetails(productId)
            .enqueue(object : Callback<ProductResponse> {

                override fun onResponse(
                    call: Call<ProductResponse>,
                    response: Response<ProductResponse>
                ) {

                    if (_binding == null) return

                    Log.d("API_TEST", "Response code: ${response.code()}")

                    if (!response.isSuccessful) {

                        Log.d(
                            "API_TEST",
                            "Error body: ${response.errorBody()?.string()}"
                        )

                        when (response.code()) {

                            401 -> showError(
                                "Требуется авторизация",
                                R.drawable.no_internet
                            )

                            404 -> showError(
                                "Товар не найден",
                                R.drawable.good_not_found
                            )

                            else -> showError(
                                "Ошибка сервера: ${response.code()}",
                                R.drawable.good_not_found
                            )
                        }

                        return
                    }

                    val productDetails = response.body()

                    Log.d("PRODUCT_DEBUG", "product = $productDetails")

                    if (productDetails?.requestError?.isNotEmpty() == true || productDetails == null) {
                        showError("Товар не найден", R.drawable.good_not_found)
                        return
                    }

                    showProductDetails(productDetails)
                    showContent()

                    loadAdditionalImages(productId)
                }

                override fun onFailure(
                    call: Call<ProductResponse>,
                    t: Throwable
                ) {

                    if (_binding == null) return

                    Log.e(
                        "API_TEST",
                        "Failure: ${t.javaClass.simpleName}: ${t.message}",
                        t
                    )

                    showError(
                        "Нет подключения к интернету",
                        R.drawable.no_internet
                    )
                }
            })
    }

    private fun loadAdditionalImages(productId: Long) {

        apiService.getProductImages(productId)
            .enqueue(object : Callback<ApiService.ProductImagesResponse> {

                override fun onResponse(
                    call: Call<ApiService.ProductImagesResponse>,
                    response: Response<ApiService.ProductImagesResponse>
                ) {
                    Log.d("IMAGES_DEBUG", "code = ${response.code()}")

                    val images = response.body()?.images.orEmpty()

                    Log.d("IMAGES_DEBUG", "count = ${images.size}")

                    images.forEachIndexed { index, image ->
                        Log.d("IMAGES_DEBUG", "[$index] ${image.url}")
                    }

                    showProductImages(images)
                }

                override fun onFailure(
                    call: Call<ApiService.ProductImagesResponse>,
                    t: Throwable
                ) {

                    if (_binding == null) return

                    binding.productImagesView.visibility = View.GONE
                }
            })
    }

    private fun showProductDetails(details: ProductResponse) {

        with(binding) {

            productName.setTextOrGone(details.name)

            realPriceText.setTextOrGone(
                details.price?.let { price ->
                    "$price ₽" +
                            (details.priceUnit?.let { unit ->
                                " / $unit"
                            } ?: "")
                }
            )

            rate.setTextOrGone(details.rating)

            cart.setTextOrGone(
                listOfNotNull(
                    details.cartStep,
                    details.cartUnit
                ).joinToString(" ")
            )

            discount.setTextOrGone(
                details.discount?.let { "-$it%" }
            )

            productDescription.setTextOrGone(details.description)

            oldPrice.apply {
                paintFlags =
                    paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                text = details.oldPrice?.let { "$it ₽" }

                visibility =
                    if (details.oldPrice != null) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }

            Glide.with(root.context)
                .load(details.image?.url)
                .into(productMainImage)
        }
    }

    private fun showFullScreenImage(imageUrl: String) {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)

        val imageView = ImageView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.FIT_CENTER

            Glide.with(this).load(imageUrl).into(this)

            // Повторный клик закрывает полноэкранный режим и возвращает назад
            setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.setContentView(imageView)
        dialog.show()
    }

    private fun showProductImages(images: List<ProductImage>) {

        if (images.isEmpty()) {
            binding.productImagesView.visibility = View.GONE
            return
        }

        binding.productImagesView.visibility = View.VISIBLE

        productImagesAdapter.setImages(images)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}