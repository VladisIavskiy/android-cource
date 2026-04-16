package com.example.myapplication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ProductDetails(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("image") val image: ProductImage,
    @SerialName("description") val description: String
)
