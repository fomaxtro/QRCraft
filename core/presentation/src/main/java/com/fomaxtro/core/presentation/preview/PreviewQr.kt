package com.fomaxtro.core.presentation.preview

import com.fomaxtro.core.domain.model.QRCode
import com.fomaxtro.core.domain.model.WifiEncryptionType

object PreviewQr {
    val contact = QRCode.Contact(
        name = "John Doe",
        phoneNumber = "+1234567890",
        email = "s0F2o@example.com"
    )
    val geoLocation = QRCode.Geolocation(
        latitude = 48.85852536332933,
        longitude = 2.294459839289565
    )
    val phoneNumber = QRCode.PhoneNumber(
        phoneNumber = "+1 123 456 7890"
    )
    val wifi = QRCode.Wifi(
        ssid = "wifi-5G",
        password = "qwerty@123",
        encryptionType = WifiEncryptionType.WPA
    )
    val link = QRCode.Link(
        url = "https://www.google.com/maps"
    )
    val text = QRCode.Text(
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
    )
}