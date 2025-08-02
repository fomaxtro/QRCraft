package com.fomaxtro.core.domain

interface PermissionChecker {
    fun hasPermission(permission: String): Boolean
}