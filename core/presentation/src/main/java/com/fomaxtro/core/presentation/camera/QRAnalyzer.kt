package com.fomaxtro.core.presentation.camera

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.fomaxtro.core.presentation.mapper.toQR
import com.fomaxtro.core.presentation.model.QRScanResult
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlin.math.roundToInt

class QRAnalyzer(
    private val frameSize: Int,
    private val windowWidth: Int,
    private val windowHeight: Int,
    private val onResult: (QRScanResult) -> Unit
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
                windowHeight = windowHeight,
                rotationDegrees = image.imageInfo.rotationDegrees
            )

        val inputImage = InputImage.fromBitmap(cropImage, 0)

        scanner.process(inputImage)
            .addOnSuccessListener {
                val barcode = it.firstOrNull()

                if (barcode != null && lastScannedResult != barcode.rawValue) {
                    lastScannedResult = barcode.rawValue

                    onResult(
                        QRScanResult(
                            qr = barcode.toQR(),
                            image = cropImage
                        )
                    )
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
    windowHeight: Int,
    rotationDegrees: Int
): Bitmap {
    val rotatedBitmap = if (rotationDegrees != 0) {
        val matrix = Matrix().apply {
            postRotate(rotationDegrees.toFloat())
        }

        Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    } else {
        this
    }

    val imageWidth = rotatedBitmap.width
    val imageHeight = rotatedBitmap.height

    val fixedWindowWidth = when (rotationDegrees) {
        90, 270 -> windowWidth
        else -> windowHeight
    }
    val fixedWindowHeight = when (rotationDegrees) {
        90, 270 -> windowHeight
        else -> windowWidth
    }

    val scaleX = imageWidth.toFloat() / fixedWindowWidth.toFloat()
    val scaleY = imageHeight.toFloat() / fixedWindowHeight.toFloat()
    val scale = minOf(scaleX, scaleY)

    val cropSize = (size * scale).roundToInt()

    val startX = (imageWidth - cropSize) / 2
    val startY = (imageHeight - cropSize) / 2

    return Bitmap.createBitmap(
        rotatedBitmap,
        startX,
        startY,
        cropSize,
        cropSize
    )
}