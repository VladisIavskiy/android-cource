package com.example.myapplication

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

interface ApiService {

    @GET("rpc/get_product")
    fun getProductDetails(
        @Query("id") productId: Long
    ): Call<ProductDetails>
}