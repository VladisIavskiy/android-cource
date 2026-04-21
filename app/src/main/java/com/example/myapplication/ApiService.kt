package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("rpc/get_product")
    fun getProductDetails(
        @Query("id") productId: Long
    ): Call<ProductResponse>
}