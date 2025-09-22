package com.fomaxtro.core.presentation.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.fomaxtro.core.presentation.R

object QRCraftIcons {
    object Default {
        val Alert: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.alert_triangle)
        val History: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.clock_refresh)
        val Copy: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.copy)
        val Link: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.link)
        val Geolocation: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.marker_pin)
        val Phone: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.phone)
        val Create: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.plus_circle)
        val Scan: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.scan)
        val Share: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.share)
        val Text: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.type)
        val Contact: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.user)
        val Wifi: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.wifi)
        val Trash: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.trash)
        val Zap: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.zap)
        val ZapOff: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.zap_off)
        val Image: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.image)
    }

    object Outlined {
        val Star: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.star_outlined)
    }

    object Filled {
        val Star: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.star_filled)
    }
}