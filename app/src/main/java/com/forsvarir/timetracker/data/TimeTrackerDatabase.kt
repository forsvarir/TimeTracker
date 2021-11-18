package com.forsvarir.timetracker.data

import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.forsvarir.timetracker.data.entities.ActivityType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(entities = [ActivityType::class], version = 1, exportSchema = false)
abstract class TimeTrackerDatabase : RoomDatabase() {
    val isOpen: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    abstract val timeTrackerDao: TimeTrackerDao
}

class TrackerDbOpen(
    private val scope: CoroutineScope,
    private val timeTrackerDatabase: () -> TimeTrackerDatabase
) : RoomDatabase.Callback() {

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)

        val dbs = timeTrackerDatabase()
        if (!dbs.isOpen.value!!) {
            createDatabase(dbs)
        }
    }

    private fun createDatabase(dbs: TimeTrackerDatabase) {
        scope.launch {
            withContext(Dispatchers.IO) {
                if (dbs.timeTrackerDao.countActivityTypes() == 0) {

                    dbs.timeTrackerDao.insertActivityType(ActivityType(name = "Travelling"))
                    dbs.timeTrackerDao.insertActivityType(ActivityType(name = "Working"))
                    dbs.timeTrackerDao.insertActivityType(ActivityType(name = "Sleeping"))
                    dbs.timeTrackerDao.insertActivityType(ActivityType(name = "Programming"))
                }
                dbs.isOpen.postValue(true)
            }
        }
    }
}
