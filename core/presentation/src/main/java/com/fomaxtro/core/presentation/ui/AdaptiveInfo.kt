package com.fomaxtro.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

@Composable
fun currentScreenType(): ScreenType {
    val windowInfo = LocalWindowInfo.current
    val widthDp = with(LocalDensity.current) { windowInfo.containerSize.width.toDp() }

    return if (widthDp > 600.dp) {
        ScreenType.WIDE
    } else {
        ScreenType.STANDARD
    }
}

enum class ScreenType {
    STANDARD,
    WIDE
}