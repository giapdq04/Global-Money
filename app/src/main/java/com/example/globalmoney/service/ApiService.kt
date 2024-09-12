package com.example.globalmoney.service

import retrofit2.http.GET
import retrofit2.http.Path

data class CurrencyResponse(
    val time_last_update_utc: String,
    val conversion_rate: Float,
)

data class CurrencyReq(
    val base_code: String,
    val target_code: String,
)

interface ApiService {

    @GET("{base_code}/{target_code}")
    suspend fun getCurrencies(
        @Path("base_code") baseCode: String,
        @Path("target_code") targetCode: String
    ): CurrencyResponse

}