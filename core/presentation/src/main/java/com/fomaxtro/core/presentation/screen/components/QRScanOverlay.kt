package com.fomaxtro.core.presentation.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.theme.OnOverlay
import com.fomaxtro.core.presentation.designsystem.theme.Overlay
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme

@Composable
fun QRScanOverlay(
    frameSize: Dp,
    cornerRadius: Dp,
    color: Color,
    strokeWidth: Dp,
    borderSize: Dp,
    placeHolder: String,
    modifier: Modifier = Modifier
) {
    val padding = 48.dp

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Overlay,
                shape = QRFrameShape(
                    frameSize = frameSize,
                    cornerRadius = cornerRadius
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        QRScanFrame(
            color = color,
            strokeWidth = strokeWidth,
            cornerRadius = cornerRadius,
            borderSize = borderSize,
            modifier = Modifier.size(frameSize)
        )

        Text(
            text = placeHolder,
            modifier = Modifier
                .offset(
                    y = -frameSize / 2 - padding
                ),
            style = MaterialTheme.typography.titleSmall,
            color = OnOverlay
        )
    }
}

private class QRFrameShape(
    private val frameSize: Dp,
    private val cornerRadius: Dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val frameSizePx = with(density) { frameSize.toPx() }
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }

        val outside = Path().apply {
            addRect(
                rect = Rect(
                    offset = Offset(0f, 0f),
                    size = size
                )
            )
        }

        val inside = Path().apply {
            val frameOffset = Offset(
                x = (size.width - frameSizePx) / 2,
                y = (size.height - frameSizePx) / 2
            )

            addRoundRect(
                RoundRect(
                    rect = Rect(
                        offset = frameOffset,
                        size = Size(frameSizePx, frameSizePx)
                    ),
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                )
            )
        }

        val result = Path().apply {
            op(outside, inside, PathOperation.Difference)
        }

        return Outline.Generic(result)
    }
}

@Preview(showBackground = true)
@Composable
private fun QRScanOverlayPreview() {
    QRCraftTheme {
        QRScanOverlay(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp,
            cornerRadius = 18.dp,
            borderSize = 16.dp,
            frameSize = 324.dp,
            placeHolder = "Scan QR code"
        )
    }
}