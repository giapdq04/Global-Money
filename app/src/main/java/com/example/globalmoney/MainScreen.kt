package com.example.globalmoney

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.globalmoney.service.CurrencyReq

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun MainScreen(viewModelApp: ViewModelApp = viewModel()) {
    val currency by viewModelApp.currency
    var firstCurrency by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var convertedAmount by remember { mutableFloatStateOf(0f) }
    var lastUpdate by remember {
        mutableStateOf(
            viewModelApp.convertUtcToVietNamTime(
                currency?.time_last_update_utc ?: ""
            )
        )
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val list = listOf(
        CurrencyCountry("USD", "United States Dollar", R.drawable.usa_flag),
        CurrencyCountry("VND", "Vietnamese Dong", R.drawable.vietnam_flag),
        CurrencyCountry("JPY", "Japanese Yen", R.drawable.japan_flag),
        CurrencyCountry("EUR", "Euro", R.drawable.euro_flag),
        CurrencyCountry("GBP", "British Pound Sterling", R.drawable.england_flag),
    )
    var isExpanded by remember { mutableStateOf(false) }
    var selectedChoice by remember { mutableStateOf(list[0]) }


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
        convertedAmount =
            if (firstCurrency.isBlank()) 0f else firstCurrency.toInt() * (currency?.conversion_rate
                ?: 0f)
        lastUpdate = viewModelApp.convertUtcToVietNamTime(currency?.time_last_update_utc ?: "")
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
            Row {
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = {
                        isExpanded = !isExpanded
                    },
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(),
                        value = selectedChoice.code,
                        onValueChange = {},
                        readOnly = true,
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = selectedChoice.flag),
                                contentDescription = null,
                                Modifier.size(24.dp)
                            )
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        list.forEachIndexed { index, s ->
                            DropdownMenuItem(
                                text = {
                                    Row {
                                        Image(
                                            painter = painterResource(id = s.flag),
                                            contentDescription = null,
                                            Modifier.size(24.dp)
                                        )
                                        Text(s.country, modifier = Modifier.padding(start = 20.dp))
                                    }
                                },
                                onClick = {
                                    selectedChoice = list[index]
                                    isExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }

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
                            keyboardController?.hide() // khi bấm done sẽ dóng bàn phím
                        }
                    )
                )
            }

            // mệnh giá sau khi chuyển đổi
            Text(
                (if (convertedAmount % 1 == 0f) convertedAmount.toInt()
                    .toString() else convertedAmount.toString()) + " VND",
                fontSize = 25.sp
            )

            Text(
                "Cập nhật lần cuối: $lastUpdate",
                fontSize = 18.sp
            )
            Button(onClick = { onClick() }, enabled = !loading) {
                Text(if (loading) "Loading..." else "Chuyển đổi")
            }
        }
    }
}