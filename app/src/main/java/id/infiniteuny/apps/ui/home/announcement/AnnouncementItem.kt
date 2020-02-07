package id.infiniteuny.apps.ui.home.announcement

import com.xwray.groupie.databinding.BindableItem
import id.infiniteuny.apps.R
import id.infiniteuny.apps.data.db.entities.Announcement
import id.infiniteuny.apps.databinding.ItemAnnouncementBinding

class AnnouncementItem(
    val announcement: Announcement
) : BindableItem<ItemAnnouncementBinding>() {
    override fun getLayout(): Int = R.layout.item_announcement

    override fun bind(viewBinding: ItemAnnouncementBinding, position: Int) {
        viewBinding.announcement = announcement
    }

}