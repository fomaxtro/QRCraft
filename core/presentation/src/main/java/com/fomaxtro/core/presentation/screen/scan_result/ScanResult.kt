package com.fomaxtro.core.presentation.screen.scan_result

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.buttons.QRCraftButton
import com.fomaxtro.core.presentation.designsystem.theme.Link
import com.fomaxtro.core.presentation.designsystem.theme.LinkBg
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.onOverlay
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHigher
import com.fomaxtro.core.presentation.mapper.toFormattedText
import com.fomaxtro.core.presentation.model.QR
import com.fomaxtro.core.presentation.preview.PreviewQr
import com.fomaxtro.core.presentation.screen.scan_result.components.ExpandableText
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ScanResultRoot(
    qr: QR,
    imagePath: String,
    navigateBack: () -> Unit,
    viewModel: ScanResultViewModel = koinViewModel {
        parametersOf(qr, imagePath)
    }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ScanResultEvent.NavigateBack -> navigateBack()
        }
    }

    ScanResultScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScanResultScreen(
    onAction: (ScanResultAction) -> Unit = {},
    state: ScanResultState
) {
    val isInPreviewMode = LocalInspectionMode.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.scan_result),
                        color = MaterialTheme.colorScheme.onOverlay,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(ScanResultAction.OnNavigateBackClick)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onOverlay
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        val qrSize = 160.dp

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(top = 32.dp)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = qrSize / 2)
                    .padding(bottom = qrSize / 2),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = qrSize / 2
                        )
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = when (state.qr) {
                            is QR.Contact -> stringResource(R.string.contact)
                            is QR.Geolocation -> stringResource(R.string.geolocation)
                            is QR.Link -> stringResource(R.string.link)
                            is QR.PhoneNumber -> stringResource(R.string.phone_number)
                            is QR.Text -> stringResource(R.string.text)
                            is QR.Wifi -> stringResource(R.string.wifi)
                        },
                        style = MaterialTheme.typography.titleMedium
                    )

                    when (state.qr) {
                        is QR.Contact,
                        is QR.Geolocation,
                        is QR.PhoneNumber,
                        is QR.Wifi -> {
                            Text(
                                text = state.qr.toFormattedText(),
                                textAlign = TextAlign.Center
                            )
                        }

                        is QR.Link -> {
                            val url = buildAnnotatedString {
                                withLink(
                                    link = LinkAnnotation.Url(
                                        url = state.qr.url,
                                        styles = TextLinkStyles(
                                            style = SpanStyle(
                                                background = LinkBg,
                                                color = Link
                                            )
                                        )
                                    )
                                ) {
                                    append(state.qr.url)
                                }
                            }

                            Text(url)
                        }

                        is QR.Text -> {
                            ExpandableText(
                                text = state.qr.text,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        QRCraftButton(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceHigher,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.share),
                                contentDescription = stringResource(R.string.share)
                            )

                            Text(
                                text = stringResource(R.string.share)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        QRCraftButton(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceHigher,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.copy),
                                contentDescription = stringResource(R.string.copy)
                            )

                            Text(
                                text = stringResource(R.string.copy)
                            )
                        }
                    }
                }
            }

            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                if (isInPreviewMode) {
                    Image(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(qrSize)
                    )
                } else if (state.qrImage != null) {
                    Image(
                        bitmap = state.qrImage,
                        contentDescription = null,
                        modifier = Modifier.size(qrSize)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ScanResultScreenPreview() {
    QRCraftTheme {
        ScanResultScreen(
            state = ScanResultState(
                qr = PreviewQr.link
            )
        )
    }
}