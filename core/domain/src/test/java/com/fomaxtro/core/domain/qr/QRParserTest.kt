package com.fomaxtro.core.domain.qr

import com.fomaxtro.core.domain.fake.FakePatternMatching
import com.fomaxtro.core.domain.model.QrCode
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs

class QrParserTest {
    private lateinit var patternMatching: FakePatternMatching
    private lateinit var qrParser: QrParser

    @BeforeTest
    fun setUp() {
        patternMatching = FakePatternMatching()
        qrParser = QrParser(patternMatching)
    }

    @Test
    fun `Test parseFromString with contact - expect contact QR`() {
        val content = """
            BEGIN:VCARD
            VERSION:3.0
            N:Olivia Schmidt
            TEL:+1 (555) 284-7390
            EMAIL:olivia.schmidt@example.com
            END:VCARD
        """.trimIndent()
        val qrResult = qrParser.parseFromString(content)

        assertIs<QrCode.Contact>(qrResult)
    }

    @Test
    fun `Test parseFromString with geolocation - expect geolocation QR`() {
        val content = "geo:50.4501,30.5234"
        val qrResult = qrParser.parseFromString(content)

        assertIs<QrCode.Geolocation>(qrResult)
    }

    @Test
    fun `Test parseFromString with link - expect link QR`() {
        patternMatching.isValidUrl = true

        val content = "http://https://pl-coding.mymemberspot.io"
        val qrResult = qrParser.parseFromString(content)

        assertIs<QrCode.Link>(qrResult)
    }

    @Test
    fun `Test parseFromString with phone number - expect phone number QR`() {
        patternMatching.isValidPhone = true

        val content = "tel:+49 170 1234567"
        val qrResult = qrParser.parseFromString(content)

        assertIs<QrCode.PhoneNumber>(qrResult)
    }

    @Test
    fun `Test parseFromString with wifi - expect wifi QR`() {
        val content = "WIFI:S:DevHub_WiFi;T:WPA;P:QrCraft2025;H:false;;"
        val qrResult = qrParser.parseFromString(content)

        assertIs<QrCode.Wifi>(qrResult)
    }

    @Test
    fun `Test parseFromString with text - expect text QR`() {
        val content = """
            Meeting notes:
            - Review UI components
            - Finalize QR saving logic
            - Test gallery import feature
        """.trimIndent()
        val qrResult = qrParser.parseFromString(content)

        assertIs<QrCode.Text>(qrResult)
    }
}