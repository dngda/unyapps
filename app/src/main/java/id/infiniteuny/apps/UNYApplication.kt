package id.infiniteuny.apps

import android.app.Application
import id.infiniteuny.apps.data.db.AppDatabase
import id.infiniteuny.apps.data.repositories.UserRepository
import id.infiniteuny.apps.ui.auth.AuthViewModel
import id.infiniteuny.apps.ui.auth.adapter.LoginSliderAdapter
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class UNYApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // start Koin!
        startKoin {
            androidContext(this@UNYApplication)
            modules(module {
                viewModel { AuthViewModel(get()) }
                single { LoginSliderAdapter(get()) }
                single { AppDatabase(get()) }
                single { UserRepository(get()) }
            })
        }
    }
}