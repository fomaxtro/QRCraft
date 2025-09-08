package com.fomaxtro.core.presentation.screen.scan_history

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.scaffolds.QRCraftScaffold
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.onSurfaceAlt
import com.fomaxtro.core.presentation.preview.PreviewModel
import com.fomaxtro.core.presentation.preview.PreviewQr
import com.fomaxtro.core.presentation.screen.scan_history.components.HistoryItem
import kotlin.random.Random

@Composable
fun ScanHistoryRoot(
    viewModel: ScanHistoryViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScanHistoryScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

data class ScanHistoryTab(
    val text: String,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScanHistoryScreen(
    onAction: (ScanHistoryAction) -> Unit = {},
    state: ScanHistoryState
) {
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val scannedTabText = stringResource(R.string.scanned)
    val generatedTabText = stringResource(R.string.generated)
    val tabs = remember {
        listOf(
            ScanHistoryTab(
                text = scannedTabText,
                onClick = {}
            ),
            ScanHistoryTab(
                text = generatedTabText,
                onClick = {}
            )
        )
    }

    val lazyListState = rememberLazyListState()

    QRCraftScaffold(
        title = stringResource(R.string.scan_history)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SecondaryTabRow(
                    selectedTabIndex = selectedTabIndex,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier
                                .tabIndicatorOffset(
                                    selectedTabIndex = selectedTabIndex,
                                    matchContentSize = false
                                ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = index == selectedTabIndex,
                            onClick = {
                                selectedTabIndex = index

                                tab.onClick()
                            },
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceAlt
                        ) {
                            Text(
                                text = tab.text,
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
                                onClick = {},
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
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