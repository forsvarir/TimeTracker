package com.forsvarir.timetracker.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.forsvarir.timetracker.data.entities.ActivityInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.streams.toList

interface TimeTrackerRepository {
    fun availableActivities(): LiveData<List<String>>
    fun currentActivity(): LiveData<ActivityInstance>
    fun ready(): LiveData<Boolean>
    fun save(activityInstance: ActivityInstance)
    fun allPreviousActivities(): LiveData<List<ActivityInstance>>
}

class TimeTrackerRepositoryImpl(
    private val database: TimeTrackerDatabase,
    private val dataAccessScope: DataAccessScope,
    private val idleActivityName: String
) : TimeTrackerRepository {
    private var mutableActivityTypes: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    private var activityTypes: LiveData<List<String>> = mutableActivityTypes
    private var mutablePreviousActivities: MutableLiveData<List<ActivityInstance>> =
        MutableLiveData(emptyList())
    private var previousActivities: LiveData<List<ActivityInstance>> = mutablePreviousActivities
    private val databaseReady: LiveData<Boolean> = database.isOpen
    private var mutableCurrentActivity = MutableLiveData(ActivityInstance(name = idleActivityName))
    private var currentActivity: LiveData<ActivityInstance> = mutableCurrentActivity

    init {
        loadActivityTypes()
        loadCurrentActivity()
        loadPreviousActivities()
    }

    override fun ready(): LiveData<Boolean> = databaseReady

    override fun currentActivity(): LiveData<ActivityInstance> {
        return currentActivity
    }

    override fun availableActivities(): LiveData<List<String>> {
        if (databaseReady.value!! && mutableActivityTypes.value?.size ?: 0 <= 0) {
            loadActivityTypes()
        }
        return activityTypes
    }

    override fun save(activityInstance: ActivityInstance) {
        Log.println(Log.INFO, "DB", "Save activity: $activityInstance")
        if (activityInstance.name == idleActivityName) {
            Log.println(Log.INFO, "DB", "Save activity ignored for: $activityInstance")
            mutableCurrentActivity.postValue(activityInstance)
            return
        }
        dataAccessScope.launch {
            withContext(Dispatchers.IO) {
                if (activityInstance.activityInstanceId == 0L) {
                    val savedId = database.timeTrackerDao.insertActivityInstance(activityInstance)
                    activityInstance.activityInstanceId = savedId
                    mutableCurrentActivity.postValue(activityInstance)
                } else {
                    database.timeTrackerDao.updateActivityInstance(activityInstance)
                    loadPreviousActivities()
                }
            }
        }
    }

    override fun allPreviousActivities(): LiveData<List<ActivityInstance>> {
        return previousActivities
    }

    private fun loadPreviousActivities() {
        dataAccessScope.launch {
            withContext(Dispatchers.IO) {
                mutablePreviousActivities.postValue(database.timeTrackerDao.getPreviousActivityInstances())
            }
        }
    }

    private fun loadCurrentActivity() {
        dataAccessScope.launch {
            withContext(Dispatchers.IO) {
                val currentActivity = database.timeTrackerDao.getCurrentActivityInstance()
                    ?: ActivityInstance(name = idleActivityName)

                Log.println(Log.INFO, "DB", "Loaded current activity: $currentActivity")

                mutableCurrentActivity.postValue(currentActivity)
            }
        }
    }

    private fun loadActivityTypes() {
        dataAccessScope.launch {
            withContext(Dispatchers.IO) {
                val activities = database.timeTrackerDao.getActivityTypes().stream()
                    .map { activity -> activity.name }
                    .toList()

                Log.println(Log.INFO, "DB", "Loaded ActivityTypes (${activities.size})")

                mutableActivityTypes.postValue(activities)
            }
        }
    }
}

class DataAccessScope(override val coroutineContext: CoroutineContext = Dispatchers.IO) :
    CoroutineScope {

}
