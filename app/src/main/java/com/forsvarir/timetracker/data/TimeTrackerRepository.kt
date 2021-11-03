package com.forsvarir.timetracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDateTime

class TimeTrackerRepository(possibleActivities: List<String> = listOf("Programming", "Walking", "Sleeping")) {
    private var availableActivities = MutableLiveData(possibleActivities)

    fun availableActivities(): LiveData<List<String>> {
        return availableActivities
    }

    fun getPreviousActivities() : LiveData<List<ActivityInstance>> {
      return MutableLiveData(listOf(
          ActivityInstance("Programming", "11:00"),
          ActivityInstance("Walking", "10:10"),
          ActivityInstance("Programming", "10:01"),
          ActivityInstance("Walking", "11:11")

      ))
    }
}

data class ActivityInstance (
    var name : String,
    var duration : String, // TODO: remove and derive this from start/end time
    var startTime : LocalDateTime = LocalDateTime.now(),
    var endTime : LocalDateTime? = null
 )

class ActivityConstants {
    companion object {
        val Unknown = ActivityInstance("Unknown", "")
    }
}