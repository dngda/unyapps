package id.infiniteuny.apps

import android.app.Application
import id.infiniteuny.apps.data.db.AppDatabase
import id.infiniteuny.apps.data.network.NetworkConnectionInterceptor
import id.infiniteuny.apps.data.network.NewsApi
import id.infiniteuny.apps.data.preferences.PreferenceProvider
import id.infiniteuny.apps.data.repositories.AnnouncementRepository
import id.infiniteuny.apps.data.repositories.NewsRepository
import id.infiniteuny.apps.data.repositories.UserRepository
import id.infiniteuny.apps.ui.auth.AuthViewModel
import id.infiniteuny.apps.ui.auth.adapter.LoginSliderAdapter
import id.infiniteuny.apps.ui.home.HomeViewModel
import id.infiniteuny.apps.ui.home.news.MoreNewsViewModel
import id.infiniteuny.apps.ui.home.news.NewsContentViewModel
import id.infiniteuny.apps.ui.profile.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class UNYApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // start Koin!
        startKoin {
            androidContext(this@UNYApplication)
            androidLogger()
            modules(module {
                single { LoginSliderAdapter(get()) }
                single { NetworkConnectionInterceptor(get()) }
                single { NewsApi(get()) }
                single { AppDatabase(get()) }
                single { PreferenceProvider(get()) }
                single { UserRepository(get()) }
                single { NewsRepository(get(), get(), get()) }
                single { AnnouncementRepository(get(), get(), get()) }

                viewModel { AuthViewModel(get()) }
                viewModel { HomeViewModel(get(), get()) }
                viewModel { NewsContentViewModel(get()) }
                viewModel { ProfileViewModel(get()) }
                viewModel { MoreNewsViewModel() }
            })
        }
    }
}