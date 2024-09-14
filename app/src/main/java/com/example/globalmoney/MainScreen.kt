package com.example.globalmoney

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
    val code by viewModelApp.code
    var firstCurrency by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(true) }
    var convertedAmount by remember { mutableFloatStateOf(0f) }
    var lastUpdate by remember {
        mutableStateOf(
            viewModelApp.convertUtcToVietNamTime(
                currency?.time_last_update_utc ?: ""
            )
        )
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isExpanded by remember { mutableStateOf(false) }
    var isExpanded2 by remember { mutableStateOf(false) }
    var selectedChoice by remember {
        mutableStateOf(
            listOf("Chọn nước")
        )
    }
    var selectedChoice2 by remember {
        mutableStateOf(
            listOf("Chọn nước")
        )
    }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModelApp.codeViewModel()
        loading = false
//        code?.let {
//            selectedChoice = it.supported_codes[0]
//            selectedChoice2 = it.supported_codes[1]
//        }
    }

    LaunchedEffect(currency, loading) {
        loading = false
        convertedAmount =
            if (firstCurrency.isBlank()) 0f else firstCurrency.toInt() * (currency?.conversion_rate
                ?: 0f)
        lastUpdate = viewModelApp.convertUtcToVietNamTime(currency?.time_last_update_utc ?: "")
    }

    fun onClick() {
        loading = true
        viewModelApp.currencyViewModel(
            currencyReq = CurrencyReq(
                selectedChoice[0], selectedChoice2[0]
            )
        )
    }

    if (!loading) {
        Box {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // tiêu đề
                Text("Chuyển đổi tiền tệ", fontSize = 30.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ExposedDropdownMenuBox(
                        expanded = isExpanded,
                        onExpandedChange = {
                            isExpanded = !isExpanded
                            Log.e("TAG", "code: " + code?.supported_codes!![0][0])
                        },
                    ) {
                        TextField(
                            modifier = Modifier
                                .menuAnchor()
                                .width(150.dp),
                            value = selectedChoice[0],
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
                        )

                        ExposedDropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false }
                        ) {
                            code?.supported_codes?.forEachIndexed { index, s ->
                                DropdownMenuItem(
                                    text = {
                                        Text(s[1], modifier = Modifier.padding(start = 20.dp))
                                    },
                                    onClick = {
                                        selectedChoice = s
                                        isExpanded = false
                                        Toast.makeText(
                                            context,
                                            "Đã chọn đơn vị tiền tệ của ${selectedChoice[1]}: ${selectedChoice[0]}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                    // mệnh giá cần chuyển đổi
                    TextField(
                        value = firstCurrency,
                        onValueChange = {
                            firstCurrency = it
                        },
                        modifier = Modifier.width(200.dp),
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

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ExposedDropdownMenuBox(
                        expanded = isExpanded2,
                        onExpandedChange = {
                            isExpanded2 = !isExpanded2
                        },
                    ) {
                        TextField(
                            modifier = Modifier
                                .menuAnchor()
                                .width(150.dp),
                            value = selectedChoice2[0],
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded2) }
                        )

                        ExposedDropdownMenu(
                            expanded = isExpanded2,
                            onDismissRequest = { isExpanded2 = false }
                        ) {
                            code?.supported_codes?.forEachIndexed { index, s ->
                                DropdownMenuItem(
                                    text = {
                                        Text(s[1], modifier = Modifier.padding(start = 20.dp))
                                    },
                                    onClick = {
                                        selectedChoice2 = s
                                        isExpanded2 = false
                                        Toast.makeText(
                                            context,
                                            "Đã chọn đơn vị tiền tệ của ${selectedChoice2[1]}: ${selectedChoice2[0]}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                    // mệnh giá sau khi chuyển đổi
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            (if (convertedAmount % 1 == 0f) convertedAmount.toInt()
                                .toString() else convertedAmount.toString()) + " VND",
                            fontSize = 25.sp
                        )
                    }

                }


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
}