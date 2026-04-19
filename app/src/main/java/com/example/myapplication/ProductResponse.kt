package com.example.myapplication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ProductResponse(
    @SerialName("requestError") val requestError: String? = null, // когда продукта нет
    @SerialName("id") val id: Long? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("image") val image: ProductImage? = null,
    @SerialName("price") val price: Double? = null,
    @SerialName("rating") val rating: Int? = null,
    @SerialName("cartStep") val cartStep: Int? = null,
    @SerialName("cartUnit") val cartUnit: String? = null,
    @SerialName("discount") val discount: Int? = null,
    @SerialName("oldPrice") val oldPrice: Double? = null,
    @SerialName("priceUnit") val priceUnit: String? = null,
    @SerialName("description") val description: String? = null
)
