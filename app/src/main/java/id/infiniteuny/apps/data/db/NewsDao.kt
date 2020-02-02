package id.infiniteuny.apps.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.infiniteuny.apps.data.db.entities.News

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllNews(news: List<News>)

    @Query("SELECT * FROM News")
    fun getNews(): LiveData<List<News>>

    @Query("SELECT * FROM News LIMIT 3")
    fun getLast3News(): LiveData<List<News>>

}