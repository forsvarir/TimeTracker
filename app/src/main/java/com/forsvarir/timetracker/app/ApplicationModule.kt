package com.forsvarir.timetracker.app

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.forsvarir.timetracker.R
import com.forsvarir.timetracker.data.*
import com.forsvarir.timetracker.viewModels.CurrentActivityViewModel
import com.forsvarir.timetracker.viewModels.LocalTimeFactory
import com.forsvarir.timetracker.viewModels.TimeFactory
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
    single { TimeTrackerApplication.getContext().applicationContext }
    single {
        Room.databaseBuilder(
            get(),
            TimeTrackerDatabase::class.java,
            "time_tracker_database"
        ).addCallback(
            TrackerDbOpen(
                scope = MainScope(),
                context = get(),
                timeTrackerDatabase = { get() }
            )
        ).fallbackToDestructiveMigration()
            .build()
    }
    single { DataAccessScope() }
    single<TimeFactory> { LocalTimeFactory() }
    factory<TimeTrackerRepository> {
        TimeTrackerRepositoryImpl(
            database = get(),
            dataAccessScope = get(),
            idleActivityName = get<Context>().getString(R.string.ActivityIdle)
        )
    }

    viewModel {
        CurrentActivityViewModel(
            timeTrackerRepository = get(),
            clock = get(),
            idleActivityName = get<Context>().getString(R.string.ActivityIdle)
        )
    }
}
