package com.fomaxtro.core.domain

interface ShareManager {
    fun copyToClipboard(text: String)
    fun shareTo(text: String)
}