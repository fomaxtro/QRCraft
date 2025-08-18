package com.fomaxtro.core.presentation.di

import com.fomaxtro.core.presentation.model.QR
import com.fomaxtro.core.presentation.screen.create_qr.CreateQRViewModel
import com.fomaxtro.core.presentation.screen.create_qr_text.CreateQRTextViewModel
import com.fomaxtro.core.presentation.screen.scan.ScanViewModel
import com.fomaxtro.core.presentation.screen.scan_result.ScanResultViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::ScanViewModel)
    viewModel<ScanResultViewModel> { (qr: QR, imagePath: String) ->
        ScanResultViewModel(
            qr = qr,
            imagePath = imagePath,
            fileManager = get()
        )
    }
    viewModelOf(::CreateQRViewModel)
    viewModelOf(::CreateQRTextViewModel)
}