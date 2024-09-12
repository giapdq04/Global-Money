package com.example.globalmoney

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
//            DropDown()
        }
    }
}

data class CurrencyCountry(
    val code: String,
    val country: String,
    val flag: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DropDown() {
    val list = listOf(
        CurrencyCountry("USD", "United States Dollar", R.drawable.usa_flag),
        CurrencyCountry("VND", "Vietnamese Dong", R.drawable.vietnam_flag),
        CurrencyCountry("JPY", "Japanese Yen", R.drawable.japan_flag),
        CurrencyCountry("EUR", "Euro", R.drawable.euro_flag),
        CurrencyCountry("GBP", "British Pound Sterling", R.drawable.england_flag),
    )
    var isExpanded by remember { mutableStateOf(false) }
    var selectedChoice by remember { mutableStateOf(list[0]) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        Text(text = "Selected: ${selectedChoice.country}")

    }

}