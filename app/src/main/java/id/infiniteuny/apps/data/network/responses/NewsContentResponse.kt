package id.infiniteuny.apps.data.network.responses

import id.infiniteuny.apps.data.db.entities.NewsContent

data class NewsContentResponse(
    val status: String,
    val result: NewsContent
)