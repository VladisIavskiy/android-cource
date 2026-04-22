//package com.example.myapplication
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.launch
//
//class ProductViewModel (
//    private val productId: Long,
//    private val api: ProductFragmentDetails
//): ViewModel() {
//    private val _uiState = MutableStateFlow<ProductState>(ProductState.Loading)
//    val uiState: StateFlow<ProductState> = _uiState.asStateFlow()
//
//    init {
//        loadProduct()
//    }
//
//    private fun loadProduct() {
//        viewModelScope.launch {
//            _uiState.value = ProductState.Loading
//            try {
//                val response = api.loadProductDetails(productId)
//
//                // Проверка твоей специфичной ошибки из JSON
//                if (response.requestError != null) {
//                    _uiState.value = ProductState.Error(response.requestError)
//                } else {
//                    _uiState.value = ProductState.Success(response)
//                }
//            } catch (e: Exception) {
//                _uiState.value = ProductState.Error("Сетевая ошибка: ${e.message}")
//            }
//        }
//    }
//}