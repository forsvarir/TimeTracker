package com.forsvarir.timetracker.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.forsvarir.timetracker.data.TimeTrackerRepository
import com.forsvarir.timetracker.data.entities.ActivityInstance
import java.time.LocalDateTime

class CurrentActivityViewModel(
    private val timeTrackerRepository: TimeTrackerRepository,
    private val clock: TimeFactory,
    private val idleActivityName: String
) : ViewModel() {

    fun availableActivities() = timeTrackerRepository.availableActivities()

    val currentActivity: LiveData<ActivityInstance> = timeTrackerRepository.currentActivity()

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
            timeTrackerRepository.save(currentActivity.value!!)
        }

        timeTrackerRepository.save(
            ActivityInstance(
                name = newActivity,
                clock = clock,
                startTime = activityChangeTime
            )
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