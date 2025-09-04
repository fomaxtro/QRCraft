package com.fomaxtro.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fomaxtro.core.data.database.dao.QRCodeDao
import com.fomaxtro.core.data.database.entity.QRCodeEntity

@Database(
    entities = [
        QRCodeEntity::class
    ],
    version = 1
)
abstract class QRCraftDatabase : RoomDatabase() {
    abstract fun qrCodeDao(): QRCodeDao
}