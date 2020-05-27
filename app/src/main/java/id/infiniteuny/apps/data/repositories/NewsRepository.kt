package id.infiniteuny.apps.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.infiniteuny.apps.data.db.AppDatabase
import id.infiniteuny.apps.data.db.entities.News
import id.infiniteuny.apps.data.db.entities.NewsContent
import id.infiniteuny.apps.data.network.NewsApi
import id.infiniteuny.apps.data.network.SafeApiRequest
import id.infiniteuny.apps.data.preferences.PreferenceProvider
import id.infiniteuny.apps.util.ApiException
import id.infiniteuny.apps.util.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository(
    private val api: NewsApi,
    private val db: AppDatabase,
    private val prefs: PreferenceProvider
) : SafeApiRequest() {

    companion object {
        private const val MINIMUM_INTERVAL = 6
    }

    private val newsList = MutableLiveData<List<News>>()
    private var newsContent: NewsContent? = null

    init {
        newsList.observeForever {
            saveNews(it)
        }
    }

    suspend fun getLastNews(page: Int): LiveData<List<News>> {
        return withContext(Dispatchers.IO) {
            fetchNews(page, false)
            db.getNewsDao().getLast3News()
        }
    }

    suspend fun getNewsList(page: Int, fetch: Boolean): LiveData<List<News>> {
        return withContext(Dispatchers.IO) {
            fetchNews(page, fetch)
            db.getNewsDao().getNewsList()
        }
    }

    suspend fun getNewsListDirect(page: Int, fetch: Boolean): LiveData<List<News>> {
        return withContext(Dispatchers.IO) {
            fetchNews(page, fetch)
            newsList
        }
    }

    suspend fun getNewsContent(link: String): NewsContent {
        return withContext(Dispatchers.IO) {
            fetchNewsContent(link)
            newsContent!!
        }
    }

    private suspend fun fetchNews(page: Int, fetch: Boolean) {
        val lastSavedAt = prefs.getNewsLastSavedAt()
        if (lastSavedAt.isNullOrEmpty() || isFetchNeeded(lastSavedAt) || fetch) {
            try {
                do {
                    val response = apiRequest {
                        api.getNews(page)
                    }
                    newsList.postValue(response.results)
                } while (response.status != "200")
            } catch (e: ApiException) {
                Log.d("FetchError", e.message!!)
                fetchNews(page, true)
            }
        }
    }

    private suspend fun fetchNewsContent(link: String) {
        try {
            do {
                val response = apiRequest {
                    api.getNewsContent(link)
                }
                newsContent = response.result
            } while (response.status != "200")
        } catch (e: ApiException) {
            Log.d("FetchError", e.message!!)
            fetchNewsContent(link)
        }
    }

    private fun isFetchNeeded(lastSavedAt: String): Boolean {
        return System.currentTimeMillis() - lastSavedAt.toLong() > 1000 * 3600 * MINIMUM_INTERVAL
    }

    private fun saveNews(newsList: List<News>) {
        Coroutines.io {
            prefs.saveNewsLastSavedAt(System.currentTimeMillis().toString())
            db.getNewsDao().saveAllNews(newsList)
        }
    }
}