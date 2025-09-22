package com.fomaxtro.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.fomaxtro.core.data.database.entity.QrCodeEntity
import com.fomaxtro.core.data.database.entity.QrCodeEntitySource
import kotlinx.coroutines.flow.Flow

@Dao
interface QrCodeDao {
    @Upsert
    suspend fun upsert(qrCode: QrCodeEntity): Long

    @Query("SELECT * FROM qr_codes WHERE id = :id")
    suspend fun findById(id: Long): QrCodeEntity

    @Query("SELECT * FROM qr_codes WHERE source = :source ORDER BY created_at DESC")
    fun findAllRecentBySource(source: QrCodeEntitySource): Flow<List<QrCodeEntity>>

    @Query("DELETE FROM qr_codes WHERE id = :id")
    suspend fun deleteById(id: Long)
}