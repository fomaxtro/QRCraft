package com.fomaxtro.core.domain.repository

import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.domain.model.QrCodeEntry
import com.fomaxtro.core.domain.model.QrCodeSource
import com.fomaxtro.core.domain.util.EmptyResult
import com.fomaxtro.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface QrCodeRepository {
    suspend fun save(entry: QrCodeEntry): Result<Long, DataError>
    suspend fun findById(id: Long): Result<QrCodeEntry, DataError>
    fun findAllRecentBySource(source: QrCodeSource): Flow<List<QrCodeEntry>>
    suspend fun deleteById(id: Long): EmptyResult<DataError>
}