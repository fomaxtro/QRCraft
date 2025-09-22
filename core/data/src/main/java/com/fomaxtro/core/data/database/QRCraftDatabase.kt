package com.fomaxtro.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fomaxtro.core.data.database.dao.QrCodeDao
import com.fomaxtro.core.data.database.entity.QrCodeEntity

@Database(
    entities = [
        QrCodeEntity::class
    ],
    version = 2
)
abstract class QRCraftDatabase : RoomDatabase() {
    abstract fun qrCodeDao(): QrCodeDao
}