package com.example.globalmoney

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.globalmoney.service.CurrencyReq

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun MainScreen(viewModelApp: ViewModelApp = viewModel()) {
    val currency by viewModelApp.currency
//    Log.e("TAG", "time_last_update_utc: " + currency?.time_last_update_utc)
    var firstCurrency by remember { mutableStateOf("0") }
    var loading by remember { mutableStateOf(false) }
    var convertedAmount by remember { mutableFloatStateOf(0f) }
    val lastUpdate by remember {
        mutableStateOf(
            viewModelApp.convertUtcToVietNamTime(
                currency?.time_last_update_utc ?: ""
            )
        )
    }

    fun onClick() {
        loading = true
        viewModelApp.currencyViewModel(
            currencyReq = CurrencyReq(
                "USD", "VND"
            )
        )
    }

    LaunchedEffect(currency, loading) {
        loading = false
        convertedAmount = firstCurrency.toInt() * (currency?.conversion_rate ?: 0f)
    }

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
                value = firstCurrency,
                onValueChange = {
                    firstCurrency = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onClick()
                    }
                )
            )

            // mệnh giá sau khi chuyển đổi
            Text(
                (if (convertedAmount % 1 == 0f) convertedAmount.toInt()
                    .toString() else convertedAmount.toString()) + " VND",
                fontSize = 25.sp
            )

            Text(
                "Cập nhật lần cuối: $lastUpdate",
                fontSize = 25.sp
            )
            Button(onClick = { onClick() }, enabled = !loading) {
                Text(if (loading) "Loading..." else "Chuyển đổi")
            }
        }
    }
}