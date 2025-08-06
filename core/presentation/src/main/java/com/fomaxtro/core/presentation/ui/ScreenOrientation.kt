package com.fomaxtro.core.presentation.ui

import android.content.res.Configuration
import android.hardware.SensorManager
import android.view.OrientationEventListener
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

interface ScreenOrientationState {
    val currentOrientation: ScreenOrientation
    val rotationDegrees: Int
}

@Stable
private class ScreenOrientationStateImpl(
    currentOrientation: ScreenOrientation,
    previousOrientation: ScreenOrientation = currentOrientation,
    rotationDegrees: Int = 0
) : ScreenOrientationState {
    override var currentOrientation by mutableStateOf(currentOrientation)
    var previousOrientation by mutableStateOf(previousOrientation)
    override var rotationDegrees by mutableIntStateOf(rotationDegrees)

    fun updateOrientation(degrees: Int) {
        val orientation = when (degrees) {
            in 315..360, in 0..45 -> ScreenOrientation.PORTRAIT
            in 225..315 -> ScreenOrientation.LANDSCAPE_LEFT
            in 135..225 -> ScreenOrientation.PORTRAIT_UPSIDE_DOWN
            in 45..135 -> ScreenOrientation.LANDSCAPE_RIGHT
            else -> ScreenOrientation.PORTRAIT
        }

        currentOrientation = orientation

        val compensationValue = when {
            previousOrientation == ScreenOrientation.PORTRAIT
                    && orientation == ScreenOrientation.LANDSCAPE_RIGHT -> +90

            previousOrientation == ScreenOrientation.PORTRAIT
                    && orientation == ScreenOrientation.LANDSCAPE_LEFT -> -90

            previousOrientation == ScreenOrientation.LANDSCAPE_RIGHT
                    && orientation == ScreenOrientation.PORTRAIT_UPSIDE_DOWN -> +90

            previousOrientation == ScreenOrientation.LANDSCAPE_RIGHT
                    && orientation == ScreenOrientation.PORTRAIT -> -90

            previousOrientation == ScreenOrientation.PORTRAIT_UPSIDE_DOWN
                    && orientation == ScreenOrientation.LANDSCAPE_LEFT -> +90

            previousOrientation == ScreenOrientation.PORTRAIT_UPSIDE_DOWN
                    && orientation == ScreenOrientation.LANDSCAPE_RIGHT -> -90

            previousOrientation == ScreenOrientation.LANDSCAPE_LEFT
                    && orientation == ScreenOrientation.PORTRAIT -> +90

            previousOrientation == ScreenOrientation.LANDSCAPE_LEFT
                    && orientation == ScreenOrientation.PORTRAIT_UPSIDE_DOWN -> -90

            previousOrientation == ScreenOrientation.PORTRAIT
                    && orientation == ScreenOrientation.PORTRAIT_UPSIDE_DOWN -> +180

            // Edge cases
            previousOrientation == ScreenOrientation.PORTRAIT_UPSIDE_DOWN
                    && orientation == ScreenOrientation.PORTRAIT -> -180

            previousOrientation == ScreenOrientation.LANDSCAPE_RIGHT
                    && orientation == ScreenOrientation.LANDSCAPE_LEFT -> +180

            previousOrientation == ScreenOrientation.LANDSCAPE_LEFT
                    && orientation == ScreenOrientation.LANDSCAPE_RIGHT -> -180

            else -> 0
        }

        rotationDegrees += compensationValue
        previousOrientation = orientation
    }

    object Saver : androidx.compose.runtime.saveable.Saver<ScreenOrientationStateImpl, Any> {
        override fun SaverScope.save(value: ScreenOrientationStateImpl): Any? {
            return listOf(
                value.currentOrientation,
                value.previousOrientation,
                value.rotationDegrees
            )
        }

        override fun restore(value: Any): ScreenOrientationStateImpl? {
            val (currentOrientation, previousOrientation, rotationDegrees) = value as List<*>

            return ScreenOrientationStateImpl(
                currentOrientation = currentOrientation as ScreenOrientation,
                previousOrientation = previousOrientation as ScreenOrientation,
                rotationDegrees = rotationDegrees as Int
            )
        }
    }
}

enum class ScreenOrientation {
    PORTRAIT,
    LANDSCAPE_RIGHT,
    LANDSCAPE_LEFT,
    PORTRAIT_UPSIDE_DOWN
}

@Composable
fun rememberScreenOrientationState(): ScreenOrientationState {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    TextFieldState.Saver

    val screenOrientationState = rememberSaveable(
        saver = ScreenOrientationStateImpl.Saver
    ) {
        val screenOrientation =
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                ScreenOrientation.PORTRAIT
            } else {
                ScreenOrientation.LANDSCAPE_RIGHT
            }

        ScreenOrientationStateImpl(screenOrientation)
    }

    DisposableEffect(context) {
        val orientationListener = object : OrientationEventListener(
            context,
            SensorManager.SENSOR_DELAY_NORMAL
        ) {
            override fun onOrientationChanged(degrees: Int) {
                if (degrees == ORIENTATION_UNKNOWN) return

                screenOrientationState.updateOrientation(degrees)
            }
        }

        orientationListener.enable()

        onDispose {
            orientationListener.disable()
        }
    }

    return screenOrientationState
}