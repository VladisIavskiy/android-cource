package com.example.myapplication

import android.app.Dialog
import android.widget.ImageView
import androidx.fragment.app.Fragment

fun Fragment.setupFullScreenOpening(vararg imageViews: ImageView) {
    imageViews.forEach { imageView ->
        imageView.setOnClickListener {
            // Создаем полноэкранный диалог
            val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)

            val fullImageView = ImageView(requireContext()).apply {
                // Копируем картинку
                setImageDrawable(imageView.drawable)
                // Растягиваем по центру
                scaleType = ImageView.ScaleType.FIT_CENTER
                // Закрываем при нажатии
                setOnClickListener { dialog.dismiss() }
            }

            dialog.setContentView(fullImageView)
            dialog.show()
        }
    }
}
