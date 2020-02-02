package id.infiniteuny.apps.data.network.responses

import id.infiniteuny.apps.data.db.entities.News

data class NewsResponse(
    var status: String,
    val results: List<News>
)