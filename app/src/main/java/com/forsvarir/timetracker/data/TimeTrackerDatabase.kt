package com.forsvarir.timetracker.data

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.forsvarir.timetracker.R
import com.forsvarir.timetracker.data.entities.ActivityInstance
import com.forsvarir.timetracker.data.entities.ActivityType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(
    entities = [ActivityType::class, ActivityInstance::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class TimeTrackerDatabase : RoomDatabase() {
    val isOpen: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    abstract val timeTrackerDao: TimeTrackerDao
}

class TrackerDbOpen(
    private val scope: CoroutineScope,
    private val context: Context,
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
                    dbs.timeTrackerDao.insertActivityType(ActivityType(name = context.getString(R.string.ActivityIdle)))
                    dbs.timeTrackerDao.insertActivityType(ActivityType(name = context.getString(R.string.ActivityTravelling)))
                    dbs.timeTrackerDao.insertActivityType(ActivityType(name = context.getString(R.string.ActivitySleeping)))
                    dbs.timeTrackerDao.insertActivityType(ActivityType(name = "Programming"))
                    dbs.timeTrackerDao.insertActivityType(ActivityType(name = "Project"))
                    dbs.timeTrackerDao.insertActivityType(ActivityType(name = "Chores"))
                }
                dbs.isOpen.postValue(true)
            }
        }
    }
}
