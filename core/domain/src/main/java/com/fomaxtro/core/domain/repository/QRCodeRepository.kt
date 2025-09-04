package com.fomaxtro.core.domain.repository

import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.domain.model.CreateQRCodeRequest
import com.fomaxtro.core.domain.util.Result

interface QRCodeRepository {
    suspend fun save(request: CreateQRCodeRequest): Result<Long, DataError>
}