package com.fomaxtro.core.data

import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import com.fomaxtro.core.domain.ShareManager

class AndroidShareManager(
    private val context: Context
) : ShareManager {
    private val clipboard = context.getSystemService(Service.CLIPBOARD_SERVICE) as ClipboardManager

    override fun copyToClipboard(text: String) {
        val clipData = ClipData.newPlainText("QR", text)

        clipboard.setPrimaryClip(clipData)
    }

    override fun shareTo(text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"

            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooserIntent = Intent.createChooser(sendIntent, null).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        context.startActivity(chooserIntent)
    }
}