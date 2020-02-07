package id.infiniteuny.apps.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.infiniteuny.apps.data.db.dao.AnnouncementDao
import id.infiniteuny.apps.data.db.dao.NewsContentDao
import id.infiniteuny.apps.data.db.dao.NewsDao
import id.infiniteuny.apps.data.db.dao.UserDao
import id.infiniteuny.apps.data.db.entities.Announcement
import id.infiniteuny.apps.data.db.entities.News
import id.infiniteuny.apps.data.db.entities.NewsContent
import id.infiniteuny.apps.data.db.entities.User

@Database(
    entities = [User::class, News::class, NewsContent::class, Announcement::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun getNewsDao(): NewsDao
    abstract fun getNewsContentDao(): NewsContentDao
    abstract fun getAnnouncementDao(): AnnouncementDao


    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "My.db"
            ).build()
    }
}