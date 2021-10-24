package com.forsvarir.timetracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TimeTrackerRepository(possibleActivities: List<String>) {
    private var availableActivities = MutableLiveData(possibleActivities)

    fun availableActivities(): LiveData<List<String>> {
        return availableActivities
    }
}
