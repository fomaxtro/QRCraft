package com.fomaxtro.core.presentation.screen.scan.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.onOverlay
import com.fomaxtro.core.presentation.designsystem.theme.overlay
import com.fomaxtro.core.presentation.ui.rememberScreenOrientationState

@Composable
fun QRScanOverlay(
    frameSize: Dp,
    color: Color,
    placeHolder: String,
    modifier: Modifier = Modifier
) {
    val padding = 32.dp
    val cornerRadius = 18.dp
    val screenOrientation = rememberScreenOrientationState()
    val frameRotation by animateFloatAsState(
        targetValue = screenOrientation.rotationDegrees.toFloat()
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.overlay,
                shape = QRFrameShape(
                    frameSize = frameSize,
                    cornerRadius = cornerRadius
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .rotate(-frameRotation),
            contentAlignment = Alignment.Center
        ) {
            QRScanFrame(
                color = color,
                strokeWidth = 4.dp,
                cornerRadius = cornerRadius,
                borderSize = 16.dp,
                modifier = Modifier
                    .size(frameSize)
            )

            Text(
                text = placeHolder,
                modifier = Modifier
                    .offset(
                        y = -frameSize / 2 - padding
                    ),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onOverlay
            )
        }
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
            frameSize = 324.dp,
            placeHolder = "Scan QR code"
        )
    }
}