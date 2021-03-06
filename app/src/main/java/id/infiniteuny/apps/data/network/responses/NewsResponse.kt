package id.infiniteuny.apps.data.network.responses

import id.infiniteuny.apps.data.db.entities.News

data class NewsResponse(
    val status: String,
    val results: List<News>,
    val total_page: Int,
    val per_page: Int,
    val first_index: Int
)