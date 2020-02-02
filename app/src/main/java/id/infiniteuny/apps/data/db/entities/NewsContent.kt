package id.infiniteuny.apps.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsContent(
    var title: String? = null,
    var summary: String? = null,
    @PrimaryKey(autoGenerate = false)
    var link: String,
    var content: String? = null,
    var thumbnail: String? = null,
    var created_date: String? = null,
    val featured_image: String? = null
)