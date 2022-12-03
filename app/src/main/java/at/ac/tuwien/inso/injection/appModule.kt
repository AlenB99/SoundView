package at.ac.tuwien.inso.injection

import androidx.room.Room
import at.ac.tuwien.inso.App
import at.ac.tuwien.inso.persistence.database.AppDatabase
import at.ac.tuwien.inso.repository.FriendRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * The Koin App Module for general injections.
 *
 * This module should be split up in the near future:
 * 1. appModule
 * 2. persistenceModule
 * 3. viewModelModule
 */
val appModule = module {

    // Provide the Application as a Singleton
    single { androidApplication() as App }

    // Provide the Room Database as a Singleton
    single {
        Room.databaseBuilder(
            get<App>(),
            AppDatabase::class.java, "my_database"
        ).build()
    }

    // Provide the Room Friend DAO as a Singleton
    single { get<AppDatabase>().friendDao() }

    // Provide the Friend Repository as a Singleton
    single { FriendRepository(get()) }

    // Provide the MainViewModel
    //viewModel { FriendViewModel(get()) }
}
