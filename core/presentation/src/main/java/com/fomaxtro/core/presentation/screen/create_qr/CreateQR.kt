package com.fomaxtro.core.presentation.screen.create_qr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.components.QRContactIcon
import com.fomaxtro.core.presentation.components.QRGeolocationIcon
import com.fomaxtro.core.presentation.components.QRLinkIcon
import com.fomaxtro.core.presentation.components.QRPhoneNumberIcon
import com.fomaxtro.core.presentation.components.QRTextIcon
import com.fomaxtro.core.presentation.components.QRWifiIcon
import com.fomaxtro.core.presentation.designsystem.scaffolds.QRCraftScaffold
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.screen.create_qr.components.ClickableCard
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateQRRoot(
    navigateToCreateContactQR: () -> Unit,
    navigateToCreateGeolocationQR: () -> Unit,
    navigateToCreateLinkQR: () -> Unit,
    navigateToCreatePhoneQR: () -> Unit,
    navigateToCreateTextQR: () -> Unit,
    navigateToCreateWifiQR: () -> Unit,
    viewModel: CreateQRViewModel = koinViewModel()
) {
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CreateQREvent.NavigateToCreateContactQR -> navigateToCreateContactQR()
            CreateQREvent.NavigateToCreateGeolocationQR -> navigateToCreateGeolocationQR()
            CreateQREvent.NavigateToCreateLinkQR -> navigateToCreateLinkQR()
            CreateQREvent.NavigateToCreatePhoneQR -> navigateToCreatePhoneQR()
            CreateQREvent.NavigateToCreateTextQR -> navigateToCreateTextQR()
            CreateQREvent.NavigateToCreateWifiQR -> navigateToCreateWifiQR()
        }
    }

    CreateQRScreen(
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateQRScreen(
    onAction: (CreateQRAction) -> Unit = {}
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val paddingAmount = if (
        windowSizeClass
            .isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    ) {
        24.dp
    } else {
        16.dp
    }
    val columnCount = if (
        windowSizeClass
            .isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    ) {
        3
    } else {
        2
    }

    QRCraftScaffold(
        title = stringResource(R.string.create_qr)
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnCount),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                horizontal = paddingAmount
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ClickableCard(
                    onClick = {
                        onAction(CreateQRAction.OnTextClick)
                    },
                    icon = {
                        QRTextIcon()
                    },
                    text = stringResource(R.string.text)
                )
            }

            item {
                ClickableCard(
                    onClick = {
                        onAction(CreateQRAction.OnLinkClick)
                    },
                    icon = {
                        QRLinkIcon()
                    },
                    text = stringResource(R.string.link)
                )
            }

            item {
                ClickableCard(
                    onClick = {
                        onAction(CreateQRAction.OnContactClick)
                    },
                    icon = {
                        QRContactIcon()
                    },
                    text = stringResource(R.string.contact),
                )
            }

            item {
                ClickableCard(
                    onClick = {
                        onAction(CreateQRAction.OnPhoneClick)
                    },
                    icon = {
                        QRPhoneNumberIcon()
                    },
                    text = stringResource(R.string.phone_number),
                )
            }

            item {
                ClickableCard(
                    onClick = {
                        onAction(CreateQRAction.OnGeolocationClick)
                    },
                    icon = {
                        QRGeolocationIcon()
                    },
                    text = stringResource(R.string.geolocation),
                )
            }

            item {
                ClickableCard(
                    onClick = {
                        onAction(CreateQRAction.OnWifiClick)
                    },
                    icon = {
                        QRWifiIcon()
                    },
                    text = stringResource(R.string.wifi),
                )
            }
        }
    }
}

@Preview
@Composable
private fun CreateQRScreenPreview() {
    QRCraftTheme {
        CreateQRScreen()
    }
}