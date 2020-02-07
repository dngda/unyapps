package id.infiniteuny.apps.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Announcement(
    var title: String? = null,
    @PrimaryKey(autoGenerate = false)
    var link: String,
    var created_date: String? = null,
    var attachment: String? = null
)