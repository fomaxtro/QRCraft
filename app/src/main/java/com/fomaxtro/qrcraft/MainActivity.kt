package com.fomaxtro.qrcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.qrcraft.navigation.NavigationRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            QRCraftTheme {
                NavigationRoot()
            }
        }
    }
}
