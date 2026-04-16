package com.example.myapplication
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductImage(
    @SerialName("filePath") val path: String,
) {
    val url: String
        get() = "$IMAGES_URL/$path"
}