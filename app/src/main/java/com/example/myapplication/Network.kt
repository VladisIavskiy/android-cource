package com.example.myapplication

import com.example.myapplication.key.API_KEYS.API_KEY
import com.example.myapplication.key.API_KEYS.SERVER_URL
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

const val IMAGES_URL = "$SERVER_URL/storage/v1/object/public/images"

private const val API_URL = "$SERVER_URL/rest/v1/"

// HTTP-клиент для совершения http-запросов
private val httpClient: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val url = chain.request().url.newBuilder()
            // к любому запросу добавляем query-параметр с ключом доступа к API
            .addQueryParameter("apikey", API_KEY)
            .build()
        val request = chain.request().newBuilder()
            .url(url)
            .build()
        chain.proceed(request)
    }
    .build()

// Настраиваем библиотеку для сериализации JSON-данных
private val json = Json { ignoreUnknownKeys = true }
private val jsonMediaType = "application/json; charset=UTF8".toMediaType()

// Объект библиотеки Retrofit для создания и запуска http-запросов
val retrofit: Retrofit = Retrofit.Builder()
    .client(httpClient)
    .baseUrl(API_URL)
    .addConverterFactory(json.asConverterFactory(jsonMediaType))
    .build()
