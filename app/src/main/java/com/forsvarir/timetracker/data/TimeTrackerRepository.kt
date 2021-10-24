package com.forsvarir.timetracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

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

data class ActivityInstance (var name : String, var duration : String)
