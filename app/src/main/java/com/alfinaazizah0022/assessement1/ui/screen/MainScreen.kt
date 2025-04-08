package com.alfinaazizah0022.assessement1.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alfinaazizah0022.assessement1.R
import com.alfinaazizah0022.assessement1.ui.theme.Assessement1Theme
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ){ innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var harga by remember { mutableStateOf("") }
    var hargaError by remember { mutableStateOf(false) }

    var diskon by remember { mutableStateOf("") }
    var diskonError by remember { mutableStateOf(false) }

    var selectedCategoryResId by remember { mutableIntStateOf(R.string.umum) }
    var hasil by remember { mutableStateOf("") }
    var hemat by remember { mutableStateOf("") }

    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    Column (
        modifier = modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = stringResource(id = R.string.intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = harga,
            onValueChange = { harga = it},
            label = { Text(text = stringResource(R.string.harga)) },
            trailingIcon = { IconPicker(hargaError, "IDR") },
            supportingText = { ErrorHint(hargaError) },
            isError = hargaError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = diskon,
            onValueChange = { diskon = it},
            label = { Text(text = stringResource(R.string.diskon)) },
            trailingIcon = { IconPicker(diskonError, "%") },
            supportingText = { ErrorHint(diskonError) },
            isError = diskonError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        CategoryDropdown(
            selectedCategoryResId = selectedCategoryResId,
            onCategorySelected = { selectedCategoryResId = it}
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Button(onClick = {
                hargaError = (harga == "" || harga == "0")
                diskonError = (diskon == "" || diskon == "0")
                if (hargaError || diskonError) return@Button

                val hargaValue = harga.toDoubleOrNull() ?: 0.0
                val diskonValue = diskon.toDoubleOrNull() ?: 0.0
                val potonganInput = hargaValue * (diskonValue / 100)

                val kategoriDiskon = when (selectedCategoryResId) {
                    R.string.makanan -> 0.05
                    R.string.elektronik -> 0.10
                    else -> 0.0
                }
                val potonganKategori = hargaValue * kategoriDiskon
                val totalPotongan = potonganInput + potonganKategori
                val total = hargaValue - totalPotongan

                hasil = context.getString(R.string.hasil, currencyFormatter.format(total))
                hemat = context.getString(R.string.hemat, currencyFormatter.format(totalPotongan))

            },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.hitung))
            }
            Button(
                onClick = {
                    harga = ""
                    diskon = ""
                    hasil= ""
                    hemat = ""
                    hargaError = false
                    diskonError = false
                    selectedCategoryResId = R.string.umum
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = "Reset")
            }
        }

        if (hasil.isNotEmpty()) {
            Text(text = hasil)
            Text(text = hemat)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    selectedCategoryResId: Int,
    onCategorySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryIds = listOf(R.string.umum,R.string.makanan, R.string.elektronik)
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded =!expanded},
        modifier = modifier
    ) {
        OutlinedTextField(
            value = stringResource(selectedCategoryResId),
            onValueChange = {},
            readOnly = true,
            label = { Text(text = stringResource(R.string.kategori)) },
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false}
        ) {
            categoryIds.forEach { category ->
                DropdownMenuItem(
                    text = { Text(stringResource(id = category)) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
        } else {
            Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(R.string.input_invalid))
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Assessement1Theme {
        MainScreen()
    }
}