package id.infiniteuny.apps.ui.home.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.infiniteuny.apps.data.db.entities.Announcement
import id.infiniteuny.apps.data.repositories.AnnouncementRepository
import id.infiniteuny.apps.util.lazyDeferred
import kotlinx.coroutines.Deferred

class MoreAnnouncementViewModel(
    announcementRepository: AnnouncementRepository
) : ViewModel() {
    val announcementList: Deferred<LiveData<List<Announcement>>> by lazyDeferred {
        announcementRepository.getAnnouncementList(0, false)
    }
}
