package com.forsvarir.timetracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.forsvarir.timetracker.viewModels.LocalTimeFactory
import com.forsvarir.timetracker.viewModels.TimeFactory
import java.time.Duration
import java.time.LocalDateTime

class TimeTrackerRepository(possibleActivities: List<String> = listOf("Programming", "Walking", "Sleeping")) {
    private var availableActivities = MutableLiveData(possibleActivities)
    val previousActivities = MutableLiveData(listOf<ActivityInstance>())

    fun availableActivities(): LiveData<List<String>> {
        return availableActivities
    }


    fun getPreviousActivities() : LiveData<List<ActivityInstance>> {
        return previousActivities
    }
}

data class ActivityInstance (
    var name : String,
    private val clock : TimeFactory = LocalTimeFactory(),
    var startTime : LocalDateTime = clock.now(),
    var endTime : LocalDateTime? = null
) {
    val duration by lazy {
        val end = endTime ?: clock.now()
        val difference = Duration.between(startTime, end).toMillis()
        String.format("%02d:%02d.%02d",
            java.util.concurrent.TimeUnit.MILLISECONDS.toHours(difference),
            java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(difference) -
                    java.util.concurrent.TimeUnit.HOURS.toMinutes(java.util.concurrent.TimeUnit.MILLISECONDS.toHours(difference)),
            java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(difference) -
                    java.util.concurrent.TimeUnit.MINUTES.toSeconds(java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(difference)))
    }
}

class ActivityConstants {
    companion object {
        val Unknown = ActivityInstance("Unknown" )
    }
}