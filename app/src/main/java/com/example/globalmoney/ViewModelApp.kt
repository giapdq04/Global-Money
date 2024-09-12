package com.example.globalmoney

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.globalmoney.service.CurrencyReq
import com.example.globalmoney.service.CurrencyResponse
import com.example.globalmoney.service.RetrofitInstance
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

class ViewModelApp : ViewModel() {
    private val _currency = mutableStateOf(CurrencyResponse("", 0.0f))
    val currency: State<CurrencyResponse> = _currency

    fun currencyViewModel(currencyReq: CurrencyReq) {
        viewModelScope.launch {
            try {
                _currency.value = RetrofitInstance.apiService.getCurrencies(
                    currencyReq.base_code,
                    currencyReq.target_code
                )
                Log.d("TAG", "thanhcong: ${_currency.value}")
            } catch (e: Exception) {
                Log.d("TAG", "thatbai: ${e.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertUtcToVietNamTime(utcTime: String): String {
        return try {
            if (utcTime.isBlank()) {
                "Invalid UTC time"
            } else {
                val formatter =
                    DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
                val zonedDateTime = ZonedDateTime.parse(utcTime, formatter)
                val vietnamTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"))
                vietnamTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            }
        } catch (e: DateTimeParseException) {
            "Invalid UTC time format"
        }
    }
}