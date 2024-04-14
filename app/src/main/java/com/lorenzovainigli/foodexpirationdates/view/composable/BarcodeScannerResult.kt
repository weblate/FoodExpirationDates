package com.lorenzovainigli.foodexpirationdates.view.composable

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme

@Composable
fun BarcodeScannerResult(
    activity: Activity?,
    productName: String,
    brand: String,
    imageThumbUrl: String? = null
) {
    val height = 100.dp
    Column {
        Row(
            modifier = Modifier
                .height(height)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (productName.isEmpty()){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(height/2),
                        painter = painterResource(id = R.drawable.ic_barcode_scan),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                    Text(
                        text = "Please scan a barcode",
                        color = Color.Gray
                    )
                }
            } else {
                AsyncImage(
                    modifier = Modifier
                        .size(height)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageThumbUrl)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.icons8_cesto_96),
                    onError = {
                        Log.e("Coil", "Error: ${it.result.throwable.message}")
                        it.result.throwable.printStackTrace()
                    }
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp, end = height / 2)
                ) {
                    Text(
                        text = brand,
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = productName,
                        fontSize = 24.sp,
                    )
                }
            }
        }
        Row {
            OutlinedButton(
                onClick = { activity?.finish() },
                modifier = Modifier
                    .weight(0.5f)
                    .padding(8.dp),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.primary
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
            Button(
                onClick = {
                    activity?.let {
                        val data = Intent().apply {
                            putExtra("productName", productName)
                        }
                        it.setResult(Activity.RESULT_OK, data)
                        it.finish()
                    }
                },
                modifier = Modifier
                    .weight(0.5f)
                    .padding(8.dp),
                enabled = productName.isNotEmpty()
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        }
    }
}

@Preview
@Composable
fun BarcodeScannerResultNoResultPreview() {
    FoodExpirationDatesTheme {
        Surface {
            BarcodeScannerResult(
                activity = null,
                productName = "",
                brand = "",
                imageThumbUrl = ""
            )
        }
    }
}

@Preview
@Composable
fun BarcodeScannerResultPreview() {
    FoodExpirationDatesTheme {
        Surface {
            BarcodeScannerResult(
                activity = null,
                productName = "Nutella",
                brand = "Ferrero",
                imageThumbUrl = "https://images.openfoodfacts.org/images/products/301/762/042/5035/front_de.474.100.jpg"
            )
        }
    }
}