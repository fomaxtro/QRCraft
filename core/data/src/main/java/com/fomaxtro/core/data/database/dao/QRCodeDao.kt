package com.fomaxtro.core.data.database.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.fomaxtro.core.data.database.entity.QRCodeEntity

@Dao
interface QRCodeDao {
    @Upsert
    suspend fun upsert(qrCode: QRCodeEntity): Long
}