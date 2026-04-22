package com.example.myapplication

sealed class ProductState {
    object Loading : ProductState()
    data class Success(val product: ProductResponse) : ProductState()
    data class Error(val message: String) : ProductState()

}