package com.forsvarir.timetracker.app

import android.app.Application
import com.forsvarir.timetracker.data.TimeTrackerRepository
import com.forsvarir.timetracker.viewModels.CurrentActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class TimeTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(applicationModule)
        }
    }
}

val applicationModule = module {
    factory { TimeTrackerRepository() }

    viewModel {
        CurrentActivityViewModel(
            timeTrackerRepository = get()
        )
    }
}