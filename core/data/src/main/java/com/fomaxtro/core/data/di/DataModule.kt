package com.fomaxtro.core.data.di

import androidx.room.Room
import com.fomaxtro.core.data.AndroidPatternMatching
import com.fomaxtro.core.data.AndroidPermissionChecker
import com.fomaxtro.core.data.AndroidShareManager
import com.fomaxtro.core.data.database.QRCraftDatabase
import com.fomaxtro.core.data.database.dao.QrCodeDao
import com.fomaxtro.core.data.database.migrations.MIGRATION_1_2
import com.fomaxtro.core.data.repository.QrCodeRepositoryImpl
import com.fomaxtro.core.domain.PatternMatching
import com.fomaxtro.core.domain.PermissionChecker
import com.fomaxtro.core.domain.ShareManager
import com.fomaxtro.core.domain.repository.QrCodeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::AndroidPermissionChecker).bind<PermissionChecker>()
    singleOf(::AndroidPatternMatching).bind<PatternMatching>()

    single<QRCraftDatabase> {
        Room.databaseBuilder(
            androidContext(),
            QRCraftDatabase::class.java,
            "qrcraft"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }
    single<QrCodeDao> { get<QRCraftDatabase>().qrCodeDao() }
    singleOf(::QrCodeRepositoryImpl).bind<QrCodeRepository>()
    singleOf(::AndroidShareManager).bind<ShareManager>()
}