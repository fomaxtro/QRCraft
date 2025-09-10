package com.fomaxtro.core.data.di

import androidx.room.Room
import com.fomaxtro.core.data.AndroidPatternMatching
import com.fomaxtro.core.data.AndroidPermissionChecker
import com.fomaxtro.core.data.AndroidShareManager
import com.fomaxtro.core.data.database.QRCraftDatabase
import com.fomaxtro.core.data.database.dao.QRCodeDao
import com.fomaxtro.core.data.repository.QRCodeRepositoryImpl
import com.fomaxtro.core.domain.PatternMatching
import com.fomaxtro.core.domain.PermissionChecker
import com.fomaxtro.core.domain.ShareManager
import com.fomaxtro.core.domain.repository.QRCodeRepository
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
        ).build()
    }
    single<QRCodeDao> { get<QRCraftDatabase>().qrCodeDao() }
    singleOf(::QRCodeRepositoryImpl).bind<QRCodeRepository>()
    singleOf(::AndroidShareManager).bind<ShareManager>()
}