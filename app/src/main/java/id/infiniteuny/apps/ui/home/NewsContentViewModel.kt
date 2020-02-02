package id.infiniteuny.apps.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.infiniteuny.apps.data.db.entities.NewsContent
import id.infiniteuny.apps.data.repositories.NewsRepository
import id.infiniteuny.apps.util.ApiException
import id.infiniteuny.apps.util.Coroutines

class NewsContentViewModel(private val repository: NewsRepository) : ViewModel() {

    var newsContent = MutableLiveData<NewsContent>()

    fun getNewsLink(link: String) {
        try {
            Coroutines.main {
                val respon = repository.getNewsContent(link)
                Log.d("ContentHasil", respon.toString())
                newsContent.postValue(respon)
            }
        } catch (e: ApiException) {
            Log.d("API", e.message!!)
            getNewsLink(link)
        }
    }

}