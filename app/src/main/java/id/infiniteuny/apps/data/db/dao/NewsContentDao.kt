package id.infiniteuny.apps.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.infiniteuny.apps.data.db.entities.NewsContent

@Dao
interface NewsContentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveContent(news: NewsContent)

    @Query("SELECT * FROM News WHERE link = :link ")
    fun getNewsContent(link: String): NewsContent
}