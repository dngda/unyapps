package id.infiniteuny.apps.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val KEY_SAVED_AT = "saved_at"
private const val KEY_CONTENT_SAVED_AT = "content_saved_at"

class PreferenceProvider(
    context: Context
) {

    private val appContext = context.applicationContext
    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun saveLastSavedAt(savedAt: String) {
        preference.edit().putString(KEY_SAVED_AT, savedAt).apply()
    }

    fun saveContentLastSavedAt(savedAt: String) {
        preference.edit().putString(KEY_CONTENT_SAVED_AT, savedAt).apply()
    }

    fun getLastSavedAt(): String? {
        return preference.getString(KEY_SAVED_AT, null)
    }

    fun getContentLastSavedAt(): String? {
        return preference.getString(KEY_CONTENT_SAVED_AT, null)
    }
}