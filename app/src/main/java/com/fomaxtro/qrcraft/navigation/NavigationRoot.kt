package com.fomaxtro.qrcraft.navigation

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.fomaxtro.core.presentation.designsystem.appbars.NavDestination
import com.fomaxtro.core.presentation.designsystem.appbars.QRCraftBottomAppBar
import com.fomaxtro.core.presentation.screen.create_qr.CreateQRRoot
import com.fomaxtro.core.presentation.screen.create_qr_text.CreateQRTextRoot
import com.fomaxtro.core.presentation.screen.scan.ScanRoot
import com.fomaxtro.core.presentation.screen.scan_result.ScanResultRoot
import com.fomaxtro.qrcraft.R

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(Route.Scan)

    NavDisplay(
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },
        sceneStrategy = SinglePaneNavigationSceneStrategy(
            bottomAppBar = {
                val currentDestination = when (backStack.lastOrNull()) {
                    Route.Scan -> NavDestination.SCAN
                    Route.CreateQR -> NavDestination.CREATE_QR
                    else -> NavDestination.SCAN
                }

                QRCraftBottomAppBar(
                    onClick = { navDestination ->
                        when (navDestination) {
                            NavDestination.HISTORY -> {}
                            NavDestination.SCAN -> {
                                if (backStack.lastOrNull() !is Route.Scan) {
                                    backStack.add(Route.Scan)
                                }
                            }

                            NavDestination.CREATE_QR -> {
                                if (backStack.lastOrNull() !is Route.CreateQR) {
                                    backStack.add(Route.CreateQR)
                                }
                            }
                        }
                    },
                    currentDestination = currentDestination
                )
            }
        ),
        entryProvider = entryProvider {
            entry<Route.Scan>(
                metadata = SinglePaneNavigationScene.withNavigation()
            ) {
                val activity = LocalActivity.current
                val context = LocalContext.current

                ScanRoot(
                    onCloseApp = {
                        activity!!.finish()
                    },
                    onCameraPermissionDenied = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.camera_permission_denied),
                            Toast.LENGTH_LONG
                        ).show()

                        activity!!.finish()
                    },
                    onAlwaysDeniedCameraPermission = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.camera_permission_always_denied),
                            Toast.LENGTH_LONG
                        ).show()

                        activity!!.finish()
                    },
                    navigateToScanResult = { qr, imagePath ->
                        if (backStack.lastOrNull() !is Route.ScanResult) {
                            backStack.add(
                                Route.ScanResult(
                                    qr = qr,
                                    imagePath = imagePath
                                )
                            )
                        }
                    }
                )
            }

            entry<Route.ScanResult> { scanResult ->
                ScanResultRoot(
                    qr = scanResult.qr,
                    imagePath = scanResult.imagePath,
                    navigateBack = {
                        if (backStack.lastOrNull() is Route.ScanResult) {
                            backStack.removeLastOrNull()
                        }
                    }
                )
            }

            entry<Route.CreateQR>(
                metadata = SinglePaneNavigationScene.withNavigation()
            ) {
                CreateQRRoot(
                    navigateToCreateContactQR = {},
                    navigateToCreateGeolocationQR = {},
                    navigateToCreateLinkQR = {},
                    navigateToCreatePhoneQR = {},
                    navigateToCreateTextQR = {
                        if (backStack.lastOrNull() !is Route.CreateQRText) {
                            backStack.add(Route.CreateQRText)
                        }
                    },
                    navigateToCreateWifiQR = {}
                )
            }

            entry<Route.CreateQRText> {
                CreateQRTextRoot(
                    navigateToScanResult = { qr, imagePath ->
                        if (backStack.lastOrNull() !is Route.ScanResult) {
                            backStack.add(
                                Route.ScanResult(
                                    qr = qr,
                                    imagePath = imagePath
                                )
                            )
                        }
                    },
                    navigateBack = {
                        if (backStack.lastOrNull() is Route.CreateQRText) {
                            backStack.removeLastOrNull()
                        }
                    }
                )
            }
        }
    )
}