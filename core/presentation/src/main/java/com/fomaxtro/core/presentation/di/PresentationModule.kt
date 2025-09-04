package com.fomaxtro.core.presentation.di

import com.fomaxtro.core.domain.model.QRCode
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.screen.create_qr.CreateQRViewModel
import com.fomaxtro.core.presentation.screen.create_qr_contact.CreateQRContactViewModel
import com.fomaxtro.core.presentation.screen.create_qr_geolocation.CreateQRGeolocationViewModel
import com.fomaxtro.core.presentation.screen.create_qr_link.CreateQRLinkViewModel
import com.fomaxtro.core.presentation.screen.create_qr_phone_number.CreateQRPhoneNumberViewModel
import com.fomaxtro.core.presentation.screen.create_qr_text.CreateQRTextViewModel
import com.fomaxtro.core.presentation.screen.create_qr_wifi.CreateQRWifiViewModel
import com.fomaxtro.core.presentation.screen.scan.ScanViewModel
import com.fomaxtro.core.presentation.screen.scan_result.ScanResultViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModel<ScanViewModel> {
        val context = androidContext()

        val defaultTitles = mapOf(
            QRCode.Text::class to context.getString(R.string.text),
            QRCode.Link::class to context.getString(R.string.link),
            QRCode.Wifi::class to context.getString(R.string.wifi),
            QRCode.Geolocation::class to context.getString(R.string.geolocation),
            QRCode.Contact::class to context.getString(R.string.contact),
            QRCode.PhoneNumber::class to context.getString(R.string.phone_number)
        )

        ScanViewModel(
            permissionChecker = get(),
            qrParser = get(),
            qrCodeRepository = get(),
            defaultTitles = defaultTitles
        )
    }
    viewModel<ScanResultViewModel> { (qr: String) ->
        ScanResultViewModel(
            qr = qr,
            qrParser = get()
        )
    }
    viewModelOf(::CreateQRViewModel)
    viewModelOf(::CreateQRTextViewModel)
    viewModelOf(::CreateQRLinkViewModel)
    viewModelOf(::CreateQRContactViewModel)
    viewModelOf(::CreateQRPhoneNumberViewModel)
    viewModelOf(::CreateQRGeolocationViewModel)
    viewModelOf(::CreateQRWifiViewModel)
}