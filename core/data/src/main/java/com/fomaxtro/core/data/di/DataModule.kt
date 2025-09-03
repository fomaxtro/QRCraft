package com.fomaxtro.core.data.di

import com.fomaxtro.core.data.AndroidPatternMatching
import com.fomaxtro.core.data.AndroidPermissionChecker
import com.fomaxtro.core.domain.PatternMatching
import com.fomaxtro.core.domain.PermissionChecker
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::AndroidPermissionChecker).bind<PermissionChecker>()
    singleOf(::AndroidPatternMatching).bind<PatternMatching>()
}