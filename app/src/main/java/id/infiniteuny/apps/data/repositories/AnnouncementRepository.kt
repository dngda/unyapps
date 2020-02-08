package id.infiniteuny.apps.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.infiniteuny.apps.data.db.AppDatabase
import id.infiniteuny.apps.data.db.entities.Announcement
import id.infiniteuny.apps.data.network.NewsApi
import id.infiniteuny.apps.data.network.SafeApiRequest
import id.infiniteuny.apps.data.preferences.PreferenceProvider
import id.infiniteuny.apps.util.ApiException
import id.infiniteuny.apps.util.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnnouncementRepository(
    private val api: NewsApi,
    private val db: AppDatabase,
    private val prefs: PreferenceProvider
) : SafeApiRequest() {

    companion object {
        private const val MINIMUM_INTERVAL = 6
    }

    private val announcementList = MutableLiveData<List<Announcement>>()
//    private var newsContent: NewsContent? = null

    init {
        announcementList.observeForever {
            saveAnnouncement(it)
        }
    }

    suspend fun getLastAnnouncement(page: Int): LiveData<List<Announcement>> {
        return withContext(Dispatchers.IO) {
            fetchAnnouncement(page, false)
            db.getAnnouncementDao().getLast3Announcement()
        }
    }

    suspend fun getAnnouncementList(page: Int, forceFetch: Boolean): LiveData<List<Announcement>> {
        return withContext(Dispatchers.IO) {
            fetchAnnouncement(page, forceFetch)
            db.getAnnouncementDao().getAnnouncementList()
        }
    }

    private suspend fun fetchAnnouncement(page: Int, forceFetch: Boolean) {
        val lastSavedAt = prefs.getAnnouncementLastSavedAt()
        if (lastSavedAt.isNullOrEmpty() || isFetchNeeded(lastSavedAt) || forceFetch) {
            try {
                var response = apiRequest {
                    api.getAnnouncement(page)
                }
                while (response.status != "200") {
                    response = apiRequest {
                        api.getAnnouncement(page)
                    }
                }
                announcementList.postValue(response.results)
            } catch (e: ApiException) {
                Log.d("FetchError", e.message!!)
                fetchAnnouncement(page, true)
            }
        }
    }

    private fun isFetchNeeded(lastSavedAt: String): Boolean {
        return System.currentTimeMillis() - lastSavedAt.toLong() > 1000 * 3600 * MINIMUM_INTERVAL
    }

    private fun saveAnnouncement(announcementList: List<Announcement>) {
        Coroutines.io {
            prefs.saveAnnouncementLastSavedAt(System.currentTimeMillis().toString())
            db.getAnnouncementDao().saveAllAnnouncement(announcementList)
        }
    }
}