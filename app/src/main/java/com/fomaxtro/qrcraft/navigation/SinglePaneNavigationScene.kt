package com.fomaxtro.qrcraft.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.Scene
import androidx.navigation3.ui.SceneStrategy
import com.fomaxtro.core.presentation.designsystem.appbars.NavDestination
import com.fomaxtro.core.presentation.designsystem.appbars.QRCraftBottomAppBar

class SinglePaneNavigationScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val onNavigate: (NavDestination) -> Unit,
    val entry: NavEntry<T>
) : Scene<T> {
    override val entries: List<NavEntry<T>> = listOf(entry)
    override val content: @Composable (() -> Unit) = {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            entry.Content()

            QRCraftBottomAppBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding(),
                onClick = onNavigate
            )
        }
    }

    companion object {
        internal const val WITH_NAVIGATION_KEY = "WithNavigation"

        fun withNavigation() = mapOf(WITH_NAVIGATION_KEY to true)
    }
}

class SinglePaneNavigationSceneStrategy<T : Any>(
    private val onNavigate: (NavDestination) -> Unit
) : SceneStrategy<T> {
    @Composable
    override fun calculateScene(entries: List<NavEntry<T>>, onBack: (Int) -> Unit): Scene<T>? {
        val entry = entries.last()

        return if (entry.metadata.containsKey(SinglePaneNavigationScene.WITH_NAVIGATION_KEY)) {
            SinglePaneNavigationScene(
                key = entries.last().contentKey,
                entry = entry,
                previousEntries = entries.dropLast(1),
                onNavigate = onNavigate
            )
        } else null
    }
}