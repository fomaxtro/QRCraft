package com.fomaxtro.core.data.repository

import com.fomaxtro.core.data.database.dao.QrCodeDao
import com.fomaxtro.core.data.mapper.toQrCodeEntity
import com.fomaxtro.core.data.mapper.toQrCodeEntitySource
import com.fomaxtro.core.data.mapper.toQrCodeEntry
import com.fomaxtro.core.data.util.safeDatabaseCall
import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.domain.model.QrCodeEntry
import com.fomaxtro.core.domain.model.QrCodeSource
import com.fomaxtro.core.domain.qr.QrParser
import com.fomaxtro.core.domain.repository.QrCodeRepository
import com.fomaxtro.core.domain.util.EmptyResult
import com.fomaxtro.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QrCodeRepositoryImpl(
    private val qrCodeDao: QrCodeDao,
    private val qrParser: QrParser
) : QrCodeRepository {
    override suspend fun save(entry: QrCodeEntry): Result<Long, DataError> {
        return safeDatabaseCall {
            qrCodeDao.upsert(entry.toQrCodeEntity(qrParser))
        }
    }

    override suspend fun findById(id: Long): Result<QrCodeEntry, DataError> {
        return safeDatabaseCall {
            qrCodeDao.findById(id)
                .toQrCodeEntry(qrParser)
        }
    }

    override fun findAllRecentBySource(source: QrCodeSource): Flow<List<QrCodeEntry>> {
        return qrCodeDao.findAllRecentBySource(source.toQrCodeEntitySource())
            .map { entries ->
                entries.map { it.toQrCodeEntry(qrParser) }
            }
    }

    override suspend fun deleteById(id: Long): EmptyResult<DataError> {
        return safeDatabaseCall {
            qrCodeDao.deleteById(id)
        }
    }
}