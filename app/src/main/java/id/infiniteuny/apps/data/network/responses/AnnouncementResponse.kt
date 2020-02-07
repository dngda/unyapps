package id.infiniteuny.apps.data.network.responses

import id.infiniteuny.apps.data.db.entities.Announcement

data class AnnouncementResponse(
    val status: String,
    val results: List<Announcement>,
    val total_page: Int,
    val per_page: Int,
    val first_index: Int
)