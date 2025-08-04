package com.fomaxtro.core.presentation.screen.camera

import android.graphics.Bitmap
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.fomaxtro.core.presentation.mapper.toQR
import com.fomaxtro.core.presentation.model.QR
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import timber.log.Timber
import kotlin.math.roundToInt

class QRAnalyzer(
    private val frameSize: Int,
    private val windowWidth: Int,
    private val windowHeight: Int,
    private val onResult: (QR) -> Unit
) : ImageAnalysis.Analyzer {
    private var lastAnalysisTime = 0L
    private val analysisInterval = 100L
    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE
            )
            .build()
    )
    private var lastScannedResult: String? = null

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastAnalysisTime < analysisInterval) {
            image.close()

            return
        }

        lastAnalysisTime = currentTime

        val cropImage = image
            .toBitmap()
            .centerCrop(
                size = frameSize,
                windowWidth = windowWidth,
                windowHeight = windowHeight
            )

        val inputImage = InputImage.fromBitmap(cropImage, image.imageInfo.rotationDegrees)

        scanner.process(inputImage)
            .addOnSuccessListener {
                val barcode = it.firstOrNull()

                if (barcode != null) {
                    if (lastScannedResult != barcode.rawValue) {
                        lastScannedResult = barcode.rawValue

                        try {
                            onResult(barcode.toQR())
                        } catch (e: IllegalArgumentException) {
                            Timber.e(e)
                        }
                    }
                } else {
                    lastScannedResult = null
                }

                image.close()
            }
            .addOnFailureListener {
                image.close()
            }
    }
}

private fun Bitmap.centerCrop(
    size: Int,
    windowWidth: Int,
    windowHeight: Int
): Bitmap {
    val scaleX = height.toFloat() / windowWidth.toFloat()
    val scaleY = width.toFloat() / windowHeight.toFloat()

    val scaledSizeWidth = (size * scaleY).roundToInt()
    val scaledSizeHeight = (size * scaleX).roundToInt()
    val scaledSize = minOf(scaledSizeWidth, scaledSizeHeight)

    val startX = (width - scaledSize) / 2
    val startY = (height - scaledSize) / 2

    return Bitmap.createBitmap(
        this,
        startX,
        startY,
        scaledSize,
        scaledSize
    )
}