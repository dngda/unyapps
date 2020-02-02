package id.infiniteuny.apps.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.infiniteuny.apps.data.db.entities.News
import id.infiniteuny.apps.data.repositories.NewsRepository
import id.infiniteuny.apps.util.lazyDeferred
import kotlinx.coroutines.Deferred

class HomeViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val lastNewsList: Deferred<LiveData<List<News>>> by lazyDeferred {
        newsRepository.getLastNews(0)
    }

}
