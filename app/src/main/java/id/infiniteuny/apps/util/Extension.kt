package id.infiniteuny.apps.util

import android.util.Log

inline fun <reified T> T.logD(msg: String?) {
    Log.d(T::class.java.simpleName, msg)
}

inline fun <reified T> T.logE(msg: String?) {
    Log.e(T::class.java.simpleName, msg)
}