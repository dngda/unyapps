package id.infiniteuny.apps.ui.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.infiniteuny.apps.data.db.entities.Announcement
import id.infiniteuny.apps.data.db.entities.News
import id.infiniteuny.apps.data.repositories.AnnouncementRepository
import id.infiniteuny.apps.data.repositories.NewsRepository
import id.infiniteuny.apps.util.lazyDeferred
import kotlinx.coroutines.Deferred

class HomeViewModel(
    private val newsRepository: NewsRepository,
    private val announcementRepository: AnnouncementRepository
) : ViewModel() {

    val lastNewsList: Deferred<LiveData<List<News>>> by lazyDeferred {
        newsRepository.getLastNews(0)
    }

    val lastAnnouncementList: Deferred<LiveData<List<Announcement>>> by lazyDeferred {
        announcementRepository.getLastAnnouncement(0)
    }

    fun onBeritaMoreClick(view: View) {

    }

}
