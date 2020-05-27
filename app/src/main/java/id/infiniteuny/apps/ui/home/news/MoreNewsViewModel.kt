package id.infiniteuny.apps.ui.home.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.infiniteuny.apps.data.db.entities.News
import id.infiniteuny.apps.data.repositories.NewsRepository
import id.infiniteuny.apps.util.lazyDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class MoreNewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {
    val newsList: Deferred<LiveData<List<News>>> by lazyDeferred {
        newsRepository.getNewsList(0, false)
    }

    var page: Int = 0
    fun getNewsListByPageAsync(page: Int): Deferred<LiveData<List<News>>> {
        return viewModelScope.async {
            newsRepository.getNewsListDirect(page, true)
        }
    }
}
