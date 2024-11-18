package com.note.core.ui

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openLink(url: String) {
    Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        startActivity(this)
    }
}