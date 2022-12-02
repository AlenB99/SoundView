package at.ac.tuwien.inso

import android.app.Application
import at.ac.tuwien.inso.injection.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}
