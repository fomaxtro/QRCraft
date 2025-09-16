package com.fomaxtro.core.presentation.screen.scan_result

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.domain.model.QRCode
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.buttons.QRCraftButton
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftIcons
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.link
import com.fomaxtro.core.presentation.designsystem.theme.linkBg
import com.fomaxtro.core.presentation.designsystem.theme.onOverlay
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHigher
import com.fomaxtro.core.presentation.mapper.toFormattedUiText
import com.fomaxtro.core.presentation.mapper.toTitle
import com.fomaxtro.core.presentation.preview.PreviewQr
import com.fomaxtro.core.presentation.screen.scan_result.components.EditableTitle
import com.fomaxtro.core.presentation.screen.scan_result.components.ExpandableText
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import com.fomaxtro.core.presentation.util.ShareManager
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun ScanResultRoot(
    id: Long,
    navigateBack: () -> Unit,
    viewModel: ScanResultViewModel = koinViewModel {
        parametersOf(id)
    }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val shareManager = koinInject<ShareManager>()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ScanResultEvent.NavigateBack -> navigateBack()

            is ScanResultEvent.CopyToClipboard -> {
                shareManager.copyToClipboard(event.text.asString(context))
            }

            is ScanResultEvent.ShareTo -> {
                shareManager.shareTo(event.text.asString(context))
            }

            is ScanResultEvent.ShowSystemMessage -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    ScanResultScreen(
        onAction = viewModel::onAction,
        state = state,
        titleState = viewModel.titleState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScanResultScreen(
    onAction: (ScanResultAction) -> Unit = {},
    state: ScanResultState,
    titleState: TextFieldState = TextFieldState()
) {
    val isInPreviewMode = LocalInspectionMode.current
    val focusManager = LocalFocusManager.current

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
                            focusManager.clearFocus()
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
        },
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
    ) { innerPadding ->
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .wrapContentSize()
            )
        } else {
            val qrSize = 160.dp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth()
                    .width(480.dp)
                    .padding(innerPadding)
                    .padding(top = 48.dp)
                    .padding(horizontal = 16.dp)
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
                        EditableTitle(
                            state = titleState,
                            placeholder = {
                                Text(state.qr.toTitle())
                            }
                        )

                        when (state.qr) {
                            is QRCode.Contact,
                            is QRCode.Geolocation,
                            is QRCode.PhoneNumber,
                            is QRCode.Wifi -> {
                                Text(
                                    text = state.qr.toFormattedUiText().asString(),
                                    textAlign = TextAlign.Center
                                )
                            }

                            is QRCode.Link -> {
                                val url = buildAnnotatedString {
                                    withLink(
                                        link = LinkAnnotation.Url(
                                            url = state.qr.url,
                                            styles = TextLinkStyles(
                                                style = SpanStyle(
                                                    background = MaterialTheme.colorScheme.linkBg,
                                                    color = MaterialTheme.colorScheme.link
                                                )
                                            )
                                        )
                                    ) {
                                        append(state.qr.url)
                                    }
                                }

                                Text(url)
                            }

                            is QRCode.Text -> {
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
                                onClick = {
                                    onAction(ScanResultAction.OnShareClick)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceHigher,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Icon(
                                    imageVector = QRCraftIcons.Share,
                                    contentDescription = stringResource(R.string.share)
                                )

                                Text(
                                    text = stringResource(R.string.share)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            QRCraftButton(
                                onClick = {
                                    onAction(ScanResultAction.OnCopyClick)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceHigher,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Icon(
                                    imageVector = QRCraftIcons.Copy,
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
}

@Preview
@Composable
private fun ScanResultScreenPreview() {
    QRCraftTheme {
        ScanResultScreen(
            state = ScanResultState(
                qr = PreviewQr.link,
                isLoading = false
            )
        )
    }
}