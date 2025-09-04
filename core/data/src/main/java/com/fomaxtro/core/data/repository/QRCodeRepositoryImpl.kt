package com.fomaxtro.core.data.repository

import com.fomaxtro.core.data.database.dao.QRCodeDao
import com.fomaxtro.core.data.database.entity.QRCodeEntity
import com.fomaxtro.core.data.mapper.toQRCodeEntitySource
import com.fomaxtro.core.data.util.safeDatabaseCall
import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.domain.model.CreateQRCodeRequest
import com.fomaxtro.core.domain.qr.QRParser
import com.fomaxtro.core.domain.repository.QRCodeRepository
import com.fomaxtro.core.domain.util.Result

class QRCodeRepositoryImpl(
    private val qrCodeDao: QRCodeDao,
    private val qrParser: QRParser
) : QRCodeRepository {
    override suspend fun save(request: CreateQRCodeRequest): Result<Long, DataError> {
        return safeDatabaseCall {
            qrCodeDao.upsert(
                QRCodeEntity(
                    title = request.title,
                    data = qrParser.convertToString(request.qrCode),
                    source = request.source.toQRCodeEntitySource()
                )
            )
        }
    }
}