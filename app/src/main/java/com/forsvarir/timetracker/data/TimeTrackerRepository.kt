package com.forsvarir.timetracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.streams.toList

interface TimeTrackerRepository {
    fun availableActivities(): LiveData<List<String>>
    fun ready(): LiveData<Boolean>
}

class TimeTrackerRepositoryImpl(
    private val database: TimeTrackerDatabase,
    private val dataAccessScope: DataAccessScope
) : TimeTrackerRepository {
    private var mutableActivities: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    private var activities: LiveData<List<String>> = mutableActivities
    private val databaseReady: LiveData<Boolean> = database.isOpen

    init {
        loadActivities()
    }

    override fun ready(): LiveData<Boolean> = databaseReady

    override fun availableActivities(): LiveData<List<String>> {
        if (databaseReady.value!! && mutableActivities.value?.size ?: 0 <= 0) {
            loadActivities()
        }
        return activities
    }

    private fun loadActivities() {
        dataAccessScope.launch {
            withContext(Dispatchers.IO) {
                val activities = database.timeTrackerDao.getActivityTypes().stream()
                    .map { activity -> activity.name }
                    .toList()

                mutableActivities.postValue(activities)
            }
        }
    }
}

class DataAccessScope(override val coroutineContext: CoroutineContext = Dispatchers.IO) :
    CoroutineScope {

}
