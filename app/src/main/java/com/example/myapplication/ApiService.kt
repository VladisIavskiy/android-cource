package com.example.myapplication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("rpc/get_product")
    fun getProductDetails(
        @Query("id") productId: Long
    ): Call<ProductResponse>

    @GET("rpc/get_product_images")
    fun getProductImages(@Query("id") productId: Long): Call<ProductImagesResponse>
    @Serializable
    data class ProductImagesResponse(
        @SerialName("images") val images: List<ProductImage>
    )

    @GET("rpc/get_products_category")
    fun getProductsCategory(@Query("id") categoryId: Long): Call<Any> // todo поменять тип тут
}