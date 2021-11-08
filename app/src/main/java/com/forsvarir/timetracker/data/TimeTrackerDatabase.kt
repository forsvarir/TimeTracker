package com.forsvarir.timetracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(entities = [ActivityType::class], version = 1, exportSchema = false)
abstract class TimeTrackerDatabase : RoomDatabase() {
    abstract val timeTrackerDao: TimeTrackerDao
}

class TrackerDbOpen(
    private val scope: CoroutineScope,
    private val timeTrackerDatabase: () -> TimeTrackerDatabase
) :
    RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        val dbs = timeTrackerDatabase()
        scope.launch {
            withContext(Dispatchers.IO) {
                dbs.timeTrackerDao.insertActivityType(ActivityType(name = "Travelling"))
                dbs.timeTrackerDao.insertActivityType(ActivityType(name = "Working"))
                dbs.timeTrackerDao.insertActivityType(ActivityType(name = "Sleeping"))
                dbs.timeTrackerDao.insertActivityType(ActivityType(name = "Programming"))
            }
        }
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
    }
}
