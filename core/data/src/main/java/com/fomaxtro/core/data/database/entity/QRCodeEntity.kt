package com.fomaxtro.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qr_codes")
data class QRCodeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val title: String,
    val data: String,
    val source: QRCodeEntitySource,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
