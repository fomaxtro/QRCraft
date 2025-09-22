package com.fomaxtro.core.presentation.qr

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class QrDetector(
    private val context: Context
) {
    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE
            )
            .build()
    )

    suspend fun detect(image: Uri): Barcode? = suspendCoroutine { continuation ->
        val inputImage = InputImage.fromFilePath(context, image)

        scanner.process(inputImage)
            .addOnSuccessListener {
                continuation.resume(it.firstOrNull())
            }
            .addOnFailureListener {
                continuation.resume(null)
            }
    }
}