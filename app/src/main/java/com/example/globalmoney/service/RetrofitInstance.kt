package com.example.globalmoney.service

object RetrofitInstance {
    private const val BASE_URL = "https://v6.exchangerate-api.com/v6/2657cf92bb48d633c9a37c16/pair/"

    val apiService: ApiService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}