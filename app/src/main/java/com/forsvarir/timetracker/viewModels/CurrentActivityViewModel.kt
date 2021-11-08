package com.forsvarir.timetracker.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forsvarir.timetracker.data.ActivityConstants
import com.forsvarir.timetracker.data.ActivityInstance
import com.forsvarir.timetracker.data.TimeTrackerDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.util.*
import kotlin.streams.toList

class CurrentActivityViewModel(
    private val clock: TimeFactory = LocalTimeFactory(),
    private val timeTrackerRepository: TimeTrackerDatabase
) : ViewModel() {

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val activities = timeTrackerRepository.timeTrackerDao.getActivityTypes()
                availableActivities = activities.stream().map { s -> s.name }.toList()
            }
        }
    }

    var availableActivities: List<String> = emptyList()

    private val mutablePreviousActivities =
        MutableLiveData<MutableList<ActivityInstance>>(LinkedList())
    val previousActivities: LiveData<MutableList<ActivityInstance>> = mutablePreviousActivities

    private val mutableCurrentActivity =
        MutableLiveData(ActivityConstants.Unknown)
    val currentActivity: LiveData<ActivityInstance> = mutableCurrentActivity

    fun startActivity(newActivity: String) {
        val activityChangeTime = clock.now()
        if (newActivity == currentActivity.value?.name) {
            return
        }

        if (currentActivity.value?.name != ActivityConstants.Unknown.name) {
            currentActivity.value?.endTime = activityChangeTime
            previousActivities.value?.add(currentActivity.value!!)
        }

        mutableCurrentActivity.value = ActivityInstance(
            availableActivities.find { it == newActivity }!!,
            clock,
            activityChangeTime
        )
    }
}

interface TimeFactory {
    fun now(): LocalDateTime
}

class LocalTimeFactory : TimeFactory {
    override fun now(): LocalDateTime {
        return LocalDateTime.now()
    }
}