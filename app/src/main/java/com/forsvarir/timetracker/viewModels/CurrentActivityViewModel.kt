package com.forsvarir.timetracker.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.forsvarir.timetracker.data.ActivityConstants
import com.forsvarir.timetracker.data.ActivityInstance
import com.forsvarir.timetracker.data.TimeTrackerRepository
import java.time.LocalDateTime
import java.util.*

class CurrentActivityViewModel(
    timeTrackerRepository: TimeTrackerRepository,
    private val clock: TimeFactory = LocalTimeFactory()
) : ViewModel() {

    var availableActivities: LiveData<List<String>> = timeTrackerRepository.availableActivities()

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
            availableActivities.value?.find { it == newActivity }!!,
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