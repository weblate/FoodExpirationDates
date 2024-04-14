package com.lorenzovainigli.foodexpirationdates.view.composable.screen

import android.app.Activity
import android.util.Log
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.lorenzovainigli.foodexpirationdates.view.composable.BarcodeScannerResult
import com.lorenzovainigli.foodexpirationdates.view.composable.CameraPreview
import com.lorenzovainigli.foodexpirationdates.viewmodel.APIServiceViewModel

@Composable
fun BarCodeScannerScreen() {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val apiServiceViewModel: APIServiceViewModel = viewModel()
    val barcodeScanner = BarcodeScanning.getClient()
    val previousBarcodeDetected = remember {
        mutableStateOf("")
    }
    val detectedProduct = apiServiceViewModel.product.collectAsState()
    Log.i("detectedProduct", detectedProduct.value.toString())
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_ANALYSIS
            )
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                MlKitAnalyzer(
                    listOf(barcodeScanner),
                    COORDINATE_SYSTEM_VIEW_REFERENCED,
                    ContextCompat.getMainExecutor(context)
                ) { result: MlKitAnalyzer.Result? ->
                    val barcodeResults = result?.getValue(barcodeScanner)
                    if (!barcodeResults.isNullOrEmpty()) {
                        for (barcode in barcodeResults) {
                            barcode.displayValue?.let {
                                if (it != previousBarcodeDetected.value) {
                                    apiServiceViewModel.run(it)
                                    previousBarcodeDetected.value = it
                                    Log.i("detectedProduct", detectedProduct.value.toString())
                                }
                            }
                        }
                    }
                }
            )
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            CameraPreview(
                controller = controller,
                modifier = Modifier.fillMaxSize()
            )
        }
        BarcodeScannerResult(
            activity = activity,
            productName = detectedProduct.value?.product?.productName ?: "",
            brand = detectedProduct.value?.product?.brands ?: "",
            imageThumbUrl = detectedProduct.value?.product?.imageThumbUrl
        )
    }
}