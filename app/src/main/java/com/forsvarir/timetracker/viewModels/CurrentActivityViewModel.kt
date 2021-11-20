package com.forsvarir.timetracker.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.forsvarir.timetracker.data.ActivityInstance
import com.forsvarir.timetracker.data.TimeTrackerRepository
import java.time.LocalDateTime
import java.util.*

class CurrentActivityViewModel(
    private val timeTrackerRepository: TimeTrackerRepository,
    private val clock: TimeFactory,
    private val idleActivityName: String
) : ViewModel() {

    fun availableActivities() = timeTrackerRepository.availableActivities()

    private val mutablePreviousActivities =
        MutableLiveData<MutableList<ActivityInstance>>(LinkedList())
    val previousActivities: LiveData<MutableList<ActivityInstance>> = mutablePreviousActivities

    private val mutableCurrentActivity =
        MutableLiveData(ActivityInstance(idleActivityName))
    val currentActivity: LiveData<ActivityInstance> = mutableCurrentActivity

    private val mutableTick = MutableLiveData(0)
    val tick = mutableTick

    fun incrementTick() {
        mutableTick.postValue(mutableTick.value!! + 1)
    }

    fun startActivity(newActivity: String) {
        val activityChangeTime = clock.now()
        if (newActivity == currentActivity.value?.name) {
            return
        }

        if (currentActivity.value?.name != idleActivityName) {
            currentActivity.value?.endTime = activityChangeTime
            previousActivities.value?.add(currentActivity.value!!)
        }

        mutableCurrentActivity.value = ActivityInstance(
            newActivity,
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