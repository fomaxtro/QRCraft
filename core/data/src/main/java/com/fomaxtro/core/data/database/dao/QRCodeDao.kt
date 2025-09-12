package com.fomaxtro.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.fomaxtro.core.data.database.entity.QRCodeEntity
import com.fomaxtro.core.data.database.entity.QRCodeEntitySource
import kotlinx.coroutines.flow.Flow

@Dao
interface QRCodeDao {
    @Upsert
    suspend fun upsert(qrCode: QRCodeEntity): Long

    @Query("SELECT * FROM qr_codes WHERE id = :id")
    suspend fun findById(id: Long): QRCodeEntity

    @Query("SELECT * FROM qr_codes WHERE source = :source ORDER BY created_at DESC")
    fun findAllRecentBySource(source: QRCodeEntitySource): Flow<List<QRCodeEntity>>

    @Query("DELETE FROM qr_codes WHERE id = :id")
    suspend fun deleteById(id: Long)
}