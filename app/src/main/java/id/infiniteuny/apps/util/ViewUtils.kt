package id.infiniteuny.apps.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.google.android.material.snackbar.Snackbar

fun Window.applyTransparentStatusBar() {
    apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        statusBarColor = Color.TRANSPARENT
    }
}

@SuppressLint("DefaultLocale")
fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { it.toLowerCase().capitalize() }

fun View.snackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).also { snackBar ->
        snackBar.setAction("Oke") {
            snackBar.dismiss()
        }
    }.show()
}