package com.example.myapplication

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imageView = view.findViewById<ImageView>(R.id.image_view)

    fun bind(image: ProductImage) {
        Glide.with(imageView.context)
            .load(image.url)
            .into(imageView)
    }
}