package id.infiniteuny.apps.ui.home.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.infiniteuny.apps.data.db.entities.Announcement
import id.infiniteuny.apps.data.repositories.AnnouncementRepository
import id.infiniteuny.apps.util.lazyDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class MoreAnnouncementViewModel(
    private val
    announcementRepository: AnnouncementRepository
) : ViewModel() {

    val announcementList: Deferred<LiveData<List<Announcement>>> by lazyDeferred {
        announcementRepository.getAnnouncementList(0, false)
    }

    var page: Int = 0
    fun getAnnouncementListByPageAsync(page: Int): Deferred<LiveData<List<Announcement>>> {
        return viewModelScope.async {
            announcementRepository.getAnnouncementListDirect(page, true)
        }
    }
}
