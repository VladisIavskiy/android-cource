package com.example.myapplication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ProductDetails(
    @SerialName("id") val id: Long?,
    @SerialName("name") val name: String?,
    @SerialName("image") val image: ProductImage?, // может прилететь пустая строка
    @SerialName("price") val price: Double?,
    @SerialName("rating") val rating: Int?,
    @SerialName("cartStep") val cartStep: Int?,
    @SerialName("cartUnit") val cartUnit: String?,
    @SerialName("discount") val discount: Int?,
    @SerialName("oldPrice") val oldPrice: Double?,
    @SerialName("priceUnit") val priceUnit: String?,
    @SerialName("description") val description: String?
)
