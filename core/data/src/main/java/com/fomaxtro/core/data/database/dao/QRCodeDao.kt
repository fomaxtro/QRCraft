package com.fomaxtro.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.fomaxtro.core.data.database.entity.QRCodeEntity

@Dao
interface QRCodeDao {
    @Upsert
    suspend fun upsert(qrCode: QRCodeEntity): Long

    @Query("SELECT * FROM qr_codes WHERE id = :id")
    suspend fun findById(id: Long): QRCodeEntity
}