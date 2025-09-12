package com.fomaxtro.core.domain.repository

import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.domain.model.QRCodeEntry
import com.fomaxtro.core.domain.model.QRCodeSource
import com.fomaxtro.core.domain.util.EmptyResult
import com.fomaxtro.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface QRCodeRepository {
    suspend fun save(entry: QRCodeEntry): Result<Long, DataError>
    suspend fun findById(id: Long): Result<QRCodeEntry, DataError>
    fun findAllRecentBySource(source: QRCodeSource): Flow<List<QRCodeEntry>>
    suspend fun deleteById(id: Long): EmptyResult<DataError>
}