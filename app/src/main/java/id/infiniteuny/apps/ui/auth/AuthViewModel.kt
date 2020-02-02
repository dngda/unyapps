package id.infiniteuny.apps.ui.auth

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import id.infiniteuny.apps.data.db.entities.User
import id.infiniteuny.apps.data.repositories.UserRepository
import id.infiniteuny.apps.ui.MainActivity
import id.infiniteuny.apps.util.Coroutines
import id.infiniteuny.apps.util.snackBar

class AuthViewModel(
    private val repository: UserRepository
) : ViewModel() {

    fun getLoggedInUser() = repository.getUser()

    fun onLoginButtonClick(view: View) {
        view.snackBar("Coming soon!")
    }

    fun onGuestButtonClick(view: View) {

        val guest = User(0, "Guest", "guest@useremail.com")
        Coroutines.main {
            repository.saveUser(guest)
        }

        Intent(view.context, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            view.context.startActivity(it)
        }
    }

}
