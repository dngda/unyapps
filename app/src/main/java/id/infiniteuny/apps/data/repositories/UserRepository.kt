package id.infiniteuny.apps.data.repositories

import id.infiniteuny.apps.data.db.AppDatabase
import id.infiniteuny.apps.data.db.entities.User

class UserRepository(
    private val db: AppDatabase
) {

    suspend fun saveUser(user: User) = db.getUserDao().insert(user)

    fun getUser() = db.getUserDao().getUser()
}