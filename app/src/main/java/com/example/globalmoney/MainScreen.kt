package com.example.globalmoney

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.globalmoney.service.CurrencyReq

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun MainScreen(viewModelApp: ViewModelApp = ViewModelApp()) {
    val currency by viewModelApp.currency
    var firstCurrency by remember { mutableStateOf(1f) }
    var loading by remember { mutableStateOf(false) }

//    LaunchedEffect(currency) {
//        if (!loading) {
//            loading = true
//        }
//    }

    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // tiêu đề
            Text("Chuyển đổi tiền tệ", fontSize = 30.sp, fontWeight = FontWeight.Bold)

            // mệnh giá cần chuyển đổi
            TextField(
                value = firstCurrency.toString(),
                onValueChange = {
                    firstCurrency = it.toFloat()
                }
            )

            // mệnh giá sau khi chuyển đổi
            Text("${firstCurrency * currency.conversion_rate}", fontSize = 25.sp)

            Text(
                viewModelApp.convertUtcToVietNamTime(currency.time_last_update_utc),
                fontSize = 25.sp
            )

            Button(onClick = {
                loading = true
                viewModelApp.currencyViewModel(
                    currencyReq = CurrencyReq(
                        "USD", "VND"
                    )
                )
            }) {
                Text(if (loading) "Loading..." else "Chuyển đổi")
            }
        }
    }
}