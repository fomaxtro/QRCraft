package com.fomaxtro.qrcraft

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.screen.ScanRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            QRCraftTheme {
                ScanRoot(
                    onCloseApp = {
                        finish()
                    },
                    onCameraPermissionDenied = {
                        Toast.makeText(
                            this,
                            getString(R.string.camera_permission_denied),
                            Toast.LENGTH_LONG
                        ).show()

                        finish()
                    },
                    onAlwaysDeniedCameraPermission = {
                        Toast.makeText(
                            this,
                            getString(R.string.camera_permission_always_denied),
                            Toast.LENGTH_LONG
                        ).show()

                        finish()
                    }
                )
            }
        }
    }
}
