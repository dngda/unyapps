package id.infiniteuny.apps.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.infiniteuny.apps.data.db.entities.Announcement

@Dao
interface AnnouncementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllAnnouncement(announcement: List<Announcement>)

    @Query("SELECT * FROM Announcement")
    fun getAnnouncementList(): LiveData<List<Announcement>>

    @Query("SELECT * FROM Announcement LIMIT 3")
    fun getLast3Announcement(): LiveData<List<Announcement>>

}