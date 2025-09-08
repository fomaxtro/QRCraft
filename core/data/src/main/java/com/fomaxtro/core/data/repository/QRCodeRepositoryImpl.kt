package com.fomaxtro.core.data.repository

import com.fomaxtro.core.data.database.dao.QRCodeDao
import com.fomaxtro.core.data.mapper.toQRCodeEntity
import com.fomaxtro.core.data.mapper.toQRCodeEntitySource
import com.fomaxtro.core.data.mapper.toQRCodeEntry
import com.fomaxtro.core.data.util.safeDatabaseCall
import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.domain.model.QRCodeEntry
import com.fomaxtro.core.domain.model.QRCodeSource
import com.fomaxtro.core.domain.qr.QRParser
import com.fomaxtro.core.domain.repository.QRCodeRepository
import com.fomaxtro.core.domain.util.Result

class QRCodeRepositoryImpl(
    private val qrCodeDao: QRCodeDao,
    private val qrParser: QRParser
) : QRCodeRepository {
    override suspend fun save(entry: QRCodeEntry): Result<Long, DataError> {
        return safeDatabaseCall {
            qrCodeDao.upsert(entry.toQRCodeEntity(qrParser))
        }
    }

    override suspend fun findById(id: Long): Result<QRCodeEntry, DataError> {
        return safeDatabaseCall {
            qrCodeDao.findById(id)
                .toQRCodeEntry(qrParser)
        }
    }

    override suspend fun findAllRecentBySource(
        source: QRCodeSource
    ): Result<List<QRCodeEntry>, DataError> {
        return safeDatabaseCall {
            qrCodeDao.findAllRecentBySource(source.toQRCodeEntitySource())
                .map { it.toQRCodeEntry(qrParser) }
        }
    }
}