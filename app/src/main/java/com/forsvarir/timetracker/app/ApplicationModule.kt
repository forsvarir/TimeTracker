package com.forsvarir.timetracker.app

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.forsvarir.timetracker.data.*
import com.forsvarir.timetracker.viewModels.CurrentActivityViewModel
import kotlinx.coroutines.MainScope
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class TimeTrackerApplication : Application() {

    companion object {
        var instance: TimeTrackerApplication? = null
        fun getContext(): Context {
            return instance!!
        }
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
        startKoin {
            modules(applicationModule)
        }
    }
}

val applicationModule = module {
    single {
        Room.databaseBuilder(
            (TimeTrackerApplication.getContext()).applicationContext,
            TimeTrackerDatabase::class.java,
            "time_tracker_database_v01"
        ).addCallback(
            TrackerDbOpen(
                MainScope()
            ) { get() }
        ).fallbackToDestructiveMigration()
            .build()
    }
    single { DataAccessScope() }
    factory<TimeTrackerRepository> {
        TimeTrackerRepositoryImpl(
            database = get(),
            dataAccessScope = get()
        )
    }

    viewModel {
        CurrentActivityViewModel(
            timeTrackerRepository = get()
        )
    }
}
