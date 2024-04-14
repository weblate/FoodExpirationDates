package com.lorenzovainigli.foodexpirationdates.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.BarCodeScannerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BarcodeScannerActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var permissionGranted = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionGranted = let {
                ContextCompat.checkSelfPermission(
                    it.applicationContext,
                    Manifest.permission.CAMERA
                )
            } == PackageManager.PERMISSION_GRANTED
        }
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                permissionGranted = isGranted
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        setContent {
            BarCodeScannerScreen()
        }
    }

}