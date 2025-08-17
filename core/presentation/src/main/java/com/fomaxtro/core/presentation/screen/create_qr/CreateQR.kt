package com.fomaxtro.core.presentation.screen.create_qr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.contact
import com.fomaxtro.core.presentation.designsystem.theme.contactBg
import com.fomaxtro.core.presentation.designsystem.theme.geo
import com.fomaxtro.core.presentation.designsystem.theme.geoBg
import com.fomaxtro.core.presentation.designsystem.theme.link
import com.fomaxtro.core.presentation.designsystem.theme.linkBg
import com.fomaxtro.core.presentation.designsystem.theme.phone
import com.fomaxtro.core.presentation.designsystem.theme.phoneBg
import com.fomaxtro.core.presentation.designsystem.theme.text
import com.fomaxtro.core.presentation.designsystem.theme.textBg
import com.fomaxtro.core.presentation.designsystem.theme.wifi
import com.fomaxtro.core.presentation.designsystem.theme.wifiBg
import com.fomaxtro.core.presentation.screen.create_qr.components.ClickableCard

@Composable
fun CreateQRRoot(
    viewModel: CreateQRViewModel = viewModel<CreateQRViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CreateQRScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateQRScreen(
    onAction: (CreateQRAction) -> Unit = {},
    state: CreateQRState
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.create_qr),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                horizontal = 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ClickableCard(
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.type),
                            contentDescription = stringResource(R.string.text)
                        )
                    },
                    iconContainerColor = MaterialTheme.colorScheme.textBg,
                    iconContentColor = MaterialTheme.colorScheme.text,
                    text = stringResource(R.string.text)
                )
            }

            item {
                ClickableCard(
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.link),
                            contentDescription = stringResource(R.string.link)
                        )
                    },
                    iconContainerColor = MaterialTheme.colorScheme.linkBg,
                    iconContentColor = MaterialTheme.colorScheme.link,
                    text = stringResource(R.string.link)
                )
            }

            item {
                ClickableCard(
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.user),
                            contentDescription = stringResource(R.string.contact)
                        )
                    },
                    iconContainerColor = MaterialTheme.colorScheme.contactBg,
                    iconContentColor = MaterialTheme.colorScheme.contact,
                    text = stringResource(R.string.contact),
                )
            }

            item {
                ClickableCard(
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.phone),
                            contentDescription = stringResource(R.string.phone_number)
                        )
                    },
                    iconContainerColor = MaterialTheme.colorScheme.phoneBg,
                    iconContentColor = MaterialTheme.colorScheme.phone,
                    text = stringResource(R.string.phone_number),
                )
            }

            item {
                ClickableCard(
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.marker_pin),
                            contentDescription = stringResource(R.string.geolocation)
                        )
                    },
                    iconContainerColor = MaterialTheme.colorScheme.geoBg,
                    iconContentColor = MaterialTheme.colorScheme.geo,
                    text = stringResource(R.string.geolocation),
                )
            }

            item {
                ClickableCard(
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.wifi),
                            contentDescription = stringResource(R.string.wifi)
                        )
                    },
                    iconContainerColor = MaterialTheme.colorScheme.wifiBg,
                    iconContentColor = MaterialTheme.colorScheme.wifi,
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
        CreateQRScreen(
            state = CreateQRState()
        )
    }
}