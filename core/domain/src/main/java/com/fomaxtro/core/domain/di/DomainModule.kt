package com.fomaxtro.core.domain.di

import com.fomaxtro.core.domain.validator.CreateQRTextValidator
import org.koin.dsl.module

val domainModule = module {
    single<CreateQRTextValidator> { CreateQRTextValidator }
}