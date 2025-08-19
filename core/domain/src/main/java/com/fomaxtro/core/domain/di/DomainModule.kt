package com.fomaxtro.core.domain.di

import com.fomaxtro.core.domain.validator.CreateQRContactValidator
import com.fomaxtro.core.domain.validator.CreateQRLinkValidator
import com.fomaxtro.core.domain.validator.CreateQRPhoneNumberValidator
import com.fomaxtro.core.domain.validator.CreateQRTextValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::CreateQRTextValidator)
    singleOf(::CreateQRLinkValidator)
    singleOf(::CreateQRContactValidator)
    singleOf(::CreateQRPhoneNumberValidator)
}