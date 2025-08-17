package com.fomaxtro.core.presentation.designsystem.appbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.buttons.QRCraftFilledIconButton
import com.fomaxtro.core.presentation.designsystem.buttons.QRCraftSelectableIconButton
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftIcons
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHigher

enum class NavDestination {
    HISTORY,
    SCAN,
    CREATE_QR
}

@Composable
fun QRCraftBottomAppBar(
    currentDestination: NavDestination,
    onClick: (NavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .padding(vertical = 6.dp),
            shape = RoundedCornerShape(100.dp),
            color = MaterialTheme.colorScheme.surfaceHigher
        ) {
            Row(
                modifier = Modifier
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                QRCraftSelectableIconButton(
                    selected = currentDestination == NavDestination.HISTORY,
                    onClick = {
                        onClick(NavDestination.HISTORY)
                    }
                ) {
                    Icon(
                        imageVector = QRCraftIcons.History,
                        contentDescription = stringResource(R.string.history),
                        modifier = Modifier
                            .size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(72.dp))

                QRCraftSelectableIconButton(
                    selected = currentDestination == NavDestination.CREATE_QR,
                    onClick = {
                        onClick(NavDestination.CREATE_QR)
                    }
                ) {
                    Icon(
                        imageVector = QRCraftIcons.Create,
                        contentDescription = stringResource(R.string.create_qr),
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            }
        }

        QRCraftFilledIconButton(
            onClick = {
                onClick(NavDestination.SCAN)
            },
            modifier = Modifier
                .size(64.dp),
        ) {
            Icon(
                imageVector = QRCraftIcons.Scan,
                contentDescription = stringResource(R.string.scan_qr),
                modifier = Modifier
                    .size(28.dp)
            )
        }
    }
}

@Preview
@Composable
private fun QRCraftBottomAppBarPreview() {
    QRCraftTheme {
        QRCraftBottomAppBar(
            onClick = {},
            currentDestination = NavDestination.CREATE_QR
        )
    }
}