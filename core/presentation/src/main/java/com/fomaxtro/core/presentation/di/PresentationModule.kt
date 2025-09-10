package com.fomaxtro.core.presentation.di

import com.fomaxtro.core.presentation.screen.create_qr.CreateQRViewModel
import com.fomaxtro.core.presentation.screen.create_qr_contact.CreateQRContactViewModel
import com.fomaxtro.core.presentation.screen.create_qr_geolocation.CreateQRGeolocationViewModel
import com.fomaxtro.core.presentation.screen.create_qr_link.CreateQRLinkViewModel
import com.fomaxtro.core.presentation.screen.create_qr_phone_number.CreateQRPhoneNumberViewModel
import com.fomaxtro.core.presentation.screen.create_qr_text.CreateQRTextViewModel
import com.fomaxtro.core.presentation.screen.create_qr_wifi.CreateQRWifiViewModel
import com.fomaxtro.core.presentation.screen.scan.ScanViewModel
import com.fomaxtro.core.presentation.screen.scan_history.ScanHistoryViewModel
import com.fomaxtro.core.presentation.screen.scan_result.ScanResultViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::ScanViewModel)

    viewModel<ScanResultViewModel> { (id: Long) ->
        ScanResultViewModel(
            id = id,
            qrParser = get(),
            qrCodeRepository = get(),
            shareManager = get()
        )
    }
    viewModelOf(::CreateQRViewModel)
    viewModelOf(::CreateQRTextViewModel)
    viewModelOf(::CreateQRLinkViewModel)
    viewModelOf(::CreateQRContactViewModel)
    viewModelOf(::CreateQRPhoneNumberViewModel)
    viewModelOf(::CreateQRGeolocationViewModel)
    viewModelOf(::CreateQRWifiViewModel)
    viewModelOf(::ScanHistoryViewModel)
}