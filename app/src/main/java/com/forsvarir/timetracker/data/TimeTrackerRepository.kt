package com.forsvarir.timetracker.data

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
    fun ready(): LiveData<Boolean>
    suspend fun save(activityInstance: ActivityInstance)
    suspend fun allPreviousActivities(): LiveData<List<ActivityInstance>>
}

class TimeTrackerRepositoryImpl(
    private val database: TimeTrackerDatabase,
    private val dataAccessScope: DataAccessScope
) : TimeTrackerRepository {
    private var mutableActivityTypes: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    private var activityTypes: LiveData<List<String>> = mutableActivityTypes
    private var mutablePreviousActivities: MutableLiveData<List<ActivityInstance>> =
        MutableLiveData(emptyList())
    private var previousActivities: LiveData<List<ActivityInstance>> = mutablePreviousActivities
    private val databaseReady: LiveData<Boolean> = database.isOpen

    init {
        loadActivities()
    }

    override fun ready(): LiveData<Boolean> = databaseReady

    override fun availableActivities(): LiveData<List<String>> {
        if (databaseReady.value!! && mutableActivityTypes.value?.size ?: 0 <= 0) {
            loadActivities()
        }
        return activityTypes
    }

    override suspend fun save(activityInstance: ActivityInstance) {
        dataAccessScope.launch {
            withContext(Dispatchers.IO) {
                if (activityInstance.activityInstanceId == 0L) {
                    val savedId = database.timeTrackerDao.insertActivityInstance(activityInstance)
                    activityInstance.activityInstanceId = savedId
                } else {
                    database.timeTrackerDao.updateActivityInstance(activityInstance)
                }
            }
        }
    }

    override suspend fun allPreviousActivities(): LiveData<List<ActivityInstance>> {
        dataAccessScope.launch {
            withContext(Dispatchers.IO) {
                mutablePreviousActivities.postValue(database.timeTrackerDao.getPreviousActivityInstances())
            }
        }
        return previousActivities
    }


    private fun loadActivities() {
        dataAccessScope.launch {
            withContext(Dispatchers.IO) {
                val activities = database.timeTrackerDao.getActivityTypes().stream()
                    .map { activity -> activity.name }
                    .toList()

                mutableActivityTypes.postValue(activities)
            }
        }
    }
}

class DataAccessScope(override val coroutineContext: CoroutineContext = Dispatchers.IO) :
    CoroutineScope {

}
