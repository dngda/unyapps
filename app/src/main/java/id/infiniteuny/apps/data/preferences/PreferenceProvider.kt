package id.infiniteuny.apps.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val KEY_NEWS_SAVED_AT = "news_list"
private const val KEY_ANNOUNCEMENT_SAVED_AT = "announcement_list"

class PreferenceProvider(
    context: Context
) {

    private val appContext = context.applicationContext
    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun saveNewsLastSavedAt(savedAt: String) {
        preference.edit().putString(KEY_NEWS_SAVED_AT, savedAt).apply()
    }

    fun getNewsLastSavedAt(): String? {
        return preference.getString(KEY_NEWS_SAVED_AT, null)
    }

    fun saveAnnouncementLastSavedAt(savedAt: String) {
        preference.edit().putString(KEY_ANNOUNCEMENT_SAVED_AT, savedAt).apply()
    }

    fun getAnnouncementLastSavedAt(): String? {
        return preference.getString(KEY_ANNOUNCEMENT_SAVED_AT, null)
    }

}