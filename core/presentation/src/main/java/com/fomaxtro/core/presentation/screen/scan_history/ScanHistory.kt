package com.fomaxtro.core.presentation.screen.scan_history

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.scaffolds.QRCraftScaffold
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftIcons
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.onSurfaceAlt
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHigher
import com.fomaxtro.core.presentation.preview.PreviewModel
import com.fomaxtro.core.presentation.preview.PreviewQr
import com.fomaxtro.core.presentation.screen.scan_history.components.HistoryItem
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import com.fomaxtro.core.presentation.util.ShareManager
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.random.Random

@Composable
fun ScanHistoryRoot(
    viewModel: ScanHistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val shareManager = koinInject<ShareManager>()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ScanHistoryEvent.ShareTo -> {
                shareManager.shareTo(event.text.asString(context))
            }

            is ScanHistoryEvent.ShowSystemError -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    ScanHistoryScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScanHistoryScreen(
    onAction: (ScanHistoryAction) -> Unit = {},
    state: ScanHistoryState
) {
    val tabs = listOf(
        stringResource(R.string.scanned),
        stringResource(R.string.generated)
    )

    val lazyListState = rememberLazyListState()
    val bottomSheetState = rememberModalBottomSheetState()
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    QRCraftScaffold(
        title = stringResource(R.string.scan_history)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SecondaryTabRow(
                selectedTabIndex = state.selectedTabIndex,
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(
                                selectedTabIndex = state.selectedTabIndex,
                                matchContentSize = false
                            ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            ) {
                tabs.forEachIndexed { index, tabText ->
                    Tab(
                        selected = index == state.selectedTabIndex,
                        onClick = {
                            onAction(ScanHistoryAction.OnTabSelected(index))
                        },
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceAlt
                    ) {
                        Text(
                            text = tabText,
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 16.dp)
                        )
                    }
                }
            }

            if (state.history.isEmpty()) {
                Text(
                    text = stringResource(R.string.empty_history),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )
            } else {
                Box {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 12.dp)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        state = lazyListState
                    ) {
                        items(state.history) { history ->
                            HistoryItem(
                                item = history,
                                onLongClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onAction(ScanHistoryAction.OnHistoryLongClick(history))
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Crossfade(
                        targetState = lazyListState.canScrollForward,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) { isVisible ->
                        if (isVisible) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(128.dp)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                MaterialTheme.colorScheme.surface
                                            )
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        }

        if (state.isShareSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    onAction(ScanHistoryAction.OnBottomSheetDismiss)
                },
                scrimColor = Color.Transparent,
                containerColor = MaterialTheme.colorScheme.surfaceHigher,
                sheetState = bottomSheetState
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                onAction(ScanHistoryAction.OnShareClick)
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(
                            imageVector = QRCraftIcons.Share,
                            contentDescription = stringResource(R.string.share)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(stringResource(R.string.share))
                    }

                    TextButton(
                        onClick = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {

                            }
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            imageVector = QRCraftIcons.Trash,
                            contentDescription = stringResource(R.string.share)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(stringResource(R.string.delete))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ScanHistoryScreenPreview() {
    QRCraftTheme {
        ScanHistoryScreen(
            state = ScanHistoryState(
                history = (1..15).map { id ->
                    val qrCodeRand = listOf(
                        PreviewQr.link,
                        PreviewQr.wifi,
                        PreviewQr.text,
                        PreviewQr.contact,
                        PreviewQr.phoneNumber,
                        PreviewQr.phoneNumber
                    )

                    PreviewModel.createQRCodeUi(
                        qrCodeRand[Random.nextInt(0, qrCodeRand.size)]
                    )
                }
            )
        )
    }
}