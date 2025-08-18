package com.fomaxtro.core.presentation.service

import com.fomaxtro.core.domain.FileManager
import com.fomaxtro.core.presentation.model.QR
import com.fomaxtro.core.presentation.util.QRGenerator

class QRImageService(
    private val fileManager: FileManager
) {
    suspend fun generateAndSaveQR(qr: QR): ImagePath {
        return fileManager.saveImage(
            QRGenerator.generate(qr)
        )
    }
}

typealias ImagePath = String