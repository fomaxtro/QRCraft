package com.fomaxtro.core.data

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.fomaxtro.core.domain.PermissionChecker

class AndroidPermissionChecker(
    private val context: Context
) : PermissionChecker {
    override fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}