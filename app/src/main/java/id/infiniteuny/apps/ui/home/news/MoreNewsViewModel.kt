package id.infiniteuny.apps.ui.home.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.infiniteuny.apps.data.db.entities.News
import id.infiniteuny.apps.data.repositories.NewsRepository
import id.infiniteuny.apps.util.lazyDeferred
import kotlinx.coroutines.Deferred

class MoreNewsViewModel(
    newsRepository: NewsRepository
) : ViewModel() {
    val newsList: Deferred<LiveData<List<News>>> by lazyDeferred {
        newsRepository.getNewsList(0, false)
    }
}
