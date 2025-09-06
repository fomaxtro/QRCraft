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
import com.fomaxtro.core.presentation.screen.create_qr_contact.CreateQRContactRoot
import com.fomaxtro.core.presentation.screen.create_qr_geolocation.CreateQRGeolocationRoot
import com.fomaxtro.core.presentation.screen.create_qr_link.CreateQRLinkRoot
import com.fomaxtro.core.presentation.screen.create_qr_phone_number.CreateQRPhoneNumberRoot
import com.fomaxtro.core.presentation.screen.create_qr_text.CreateQRTextRoot
import com.fomaxtro.core.presentation.screen.create_qr_wifi.CreateQRWifiRoot
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
                    navigateToScanResult = { id ->
                        if (backStack.lastOrNull() !is Route.ScanResult) {
                            backStack.add(Route.ScanResult(id))
                        }
                    }
                )
            }

            entry<Route.ScanResult> { scanResult ->
                ScanResultRoot(
                    qr = scanResult.qr,
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
                    navigateToCreateContactQR = {
                        if (backStack.lastOrNull() !is Route.CreateQRContact) {
                            backStack.add(Route.CreateQRContact)
                        }
                    },
                    navigateToCreateGeolocationQR = {
                        if (backStack.lastOrNull() !is Route.CreateQRGeolocation) {
                            backStack.add(Route.CreateQRGeolocation)
                        }
                    },
                    navigateToCreateLinkQR = {
                        if (backStack.lastOrNull() !is Route.CreateQRLink) {
                            backStack.add(Route.CreateQRLink)
                        }
                    },
                    navigateToCreatePhoneQR = {
                        if (backStack.lastOrNull() !is Route.CreateQRPhoneNumber) {
                            backStack.add(Route.CreateQRPhoneNumber)
                        }
                    },
                    navigateToCreateTextQR = {
                        if (backStack.lastOrNull() !is Route.CreateQRText) {
                            backStack.add(Route.CreateQRText)
                        }
                    },
                    navigateToCreateWifiQR = {
                        if (backStack.lastOrNull() !is Route.CreateQRWifi) {
                            backStack.add(Route.CreateQRWifi)
                        }
                    }
                )
            }

            entry<Route.CreateQRText> {
                CreateQRTextRoot(
                    navigateToScanResult = { qr ->
                        if (backStack.lastOrNull() !is Route.ScanResult) {
                            backStack.add(Route.ScanResult(qr))
                        }
                    },
                    navigateBack = {
                        if (backStack.lastOrNull() is Route.CreateQRText) {
                            backStack.removeLastOrNull()
                        }
                    }
                )
            }

            entry<Route.CreateQRLink> {
                CreateQRLinkRoot(
                    navigateToScanResult = { qr ->
                        if (backStack.lastOrNull() !is Route.ScanResult) {
                            backStack.add(Route.ScanResult(qr))
                        }
                    },
                    navigateBack = {
                        if (backStack.lastOrNull() is Route.CreateQRLink) {
                            backStack.removeLastOrNull()
                        }
                    }
                )
            }

            entry<Route.CreateQRContact> {
                CreateQRContactRoot(
                    navigateToScanResult = { qr ->
                        if (backStack.lastOrNull() !is Route.ScanResult) {
                            backStack.add(Route.ScanResult(qr))
                        }
                    },
                    navigateBack = {
                        if (backStack.lastOrNull() is Route.CreateQRContact) {
                            backStack.removeLastOrNull()
                        }
                    }
                )
            }

            entry<Route.CreateQRPhoneNumber> {
                CreateQRPhoneNumberRoot(
                    navigateToScanResult = { qr ->
                        if (backStack.lastOrNull() !is Route.ScanResult) {
                            backStack.add(Route.ScanResult(qr))
                        }
                    },
                    navigateBack = {
                        if (backStack.lastOrNull() is Route.CreateQRPhoneNumber) {
                            backStack.removeLastOrNull()
                        }
                    }
                )
            }

            entry<Route.CreateQRGeolocation> {
                CreateQRGeolocationRoot(
                    navigateToScanResult = { qr ->
                        if (backStack.lastOrNull() !is Route.ScanResult) {
                            backStack.add(Route.ScanResult(qr))
                        }
                    },
                    navigateBack = {
                        if (backStack.lastOrNull() is Route.CreateQRGeolocation) {
                            backStack.removeLastOrNull()
                        }
                    }
                )
            }

            entry<Route.CreateQRWifi> {
                CreateQRWifiRoot(
                    navigateToScanResult = { qr ->
                        if (backStack.lastOrNull() !is Route.ScanResult) {
                            backStack.add(Route.ScanResult(qr))
                        }
                    },
                    navigateBack = {
                        if (backStack.lastOrNull() is Route.CreateQRWifi) {
                            backStack.removeLastOrNull()
                        }
                    }
                )
            }
        }
    )
}