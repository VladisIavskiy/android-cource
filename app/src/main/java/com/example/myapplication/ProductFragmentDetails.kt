package com.example.myapplication

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentProductDetailsBinding
import androidx.core.graphics.toColorInt

class ProductFragmentDetails : Fragment(R.layout.fragment_product_details) {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("UseCompatTextViewDrawableApis")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductDetailsBinding.bind(view)

        val details = ProductDetails(
            name = "Крутой банан",
            description = "Единственный в мире банан, который прошел курсы по Android разработке",
            mainImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRCSWizn7nNDsJT3GPaRbN7vPcg_wycI_wzSw&s"
        )

        showProductDetails(details)

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
            .load(details.mainImage)
            .into(binding.productMainImage)
        binding.productName.text = details.name
        binding.productDescription.text = details.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}