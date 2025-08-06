package com.fomaxtro.core.presentation.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.theme.OnOverlay
import com.fomaxtro.core.presentation.designsystem.theme.Overlay
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme

@Composable
fun OverlayLoading() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Overlay
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = OnOverlay
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.loading),
            color = OnOverlay
        )
    }
}

@Preview
@Composable
private fun OverlayLoadingPreview() {
    QRCraftTheme {
        OverlayLoading()
    }
}