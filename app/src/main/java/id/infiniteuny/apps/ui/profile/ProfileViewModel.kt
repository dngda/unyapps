package id.infiniteuny.apps.ui.profile

import androidx.lifecycle.ViewModel
import id.infiniteuny.apps.data.repositories.UserRepository

class ProfileViewModel(
    repository: UserRepository
) : ViewModel() {
    val user = repository.getUser()
}
