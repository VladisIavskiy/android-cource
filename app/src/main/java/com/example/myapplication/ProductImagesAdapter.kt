package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductImagesAdapter : RecyclerView.Adapter<ProductImageViewHolder>() {
    private val images: MutableList<ProductImage> = mutableListOf()

    // переменная-слушатель для клика наружу
    var onImageClickListener: ((ProductImage) -> Unit)? = null

    // todo отобразить для отображения новый список [images]
    fun setImages(images: List<ProductImage>) {
        this.images.clear()
        this.images.addAll(images)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_image, parent, false)
        return ProductImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductImageViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image)

        // вешаем клик на весь список
        holder.itemView.setOnClickListener {
            onImageClickListener?.invoke(image)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}
