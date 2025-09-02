package com.fomaxtro.core.domain.di

import com.fomaxtro.core.domain.qr.QRPatterns
import com.fomaxtro.core.domain.validator.CreateQRContactValidator
import com.fomaxtro.core.domain.validator.CreateQRGeolocationValidator
import com.fomaxtro.core.domain.validator.CreateQRLinkValidator
import com.fomaxtro.core.domain.validator.CreateQRPhoneNumberValidator
import com.fomaxtro.core.domain.validator.CreateQRTextValidator
import com.fomaxtro.core.domain.validator.CreateQRWifiValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::CreateQRTextValidator)
    singleOf(::CreateQRLinkValidator)
    singleOf(::CreateQRContactValidator)
    singleOf(::CreateQRPhoneNumberValidator)
    singleOf(::CreateQRGeolocationValidator)
    singleOf(::CreateQRWifiValidator)

    singleOf(::QRPatterns)
}