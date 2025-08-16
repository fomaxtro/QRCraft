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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.buttons.QRCraftFilledIconButton
import com.fomaxtro.core.presentation.designsystem.buttons.QRCraftSelectableIconButton
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme

enum class NavDestination {
    HISTORY,
    SCAN,
    CREATE_QR
}

@Composable
fun QRCraftBottomAppBar(
    onClick: (NavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    var navDestination by rememberSaveable {
        mutableStateOf(NavDestination.SCAN)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .padding(vertical = 6.dp),
            shape = RoundedCornerShape(100.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                QRCraftSelectableIconButton(
                    selected = navDestination == NavDestination.HISTORY,
                    onClick = {
                        navDestination = NavDestination.HISTORY
                        onClick(NavDestination.HISTORY)
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.clock_refresh),
                        contentDescription = stringResource(R.string.history),
                        modifier = Modifier
                            .size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(72.dp))

                QRCraftSelectableIconButton(
                    selected = navDestination == NavDestination.CREATE_QR,
                    onClick = {
                        navDestination = NavDestination.CREATE_QR
                        onClick(NavDestination.CREATE_QR)
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.plus_circle),
                        contentDescription = stringResource(R.string.create_qr),
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            }
        }

        QRCraftFilledIconButton(
            onClick = {
                navDestination = NavDestination.SCAN
                onClick(NavDestination.SCAN)
            },
            modifier = Modifier
                .size(64.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.scan),
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
            onClick = {}
        )
    }
}