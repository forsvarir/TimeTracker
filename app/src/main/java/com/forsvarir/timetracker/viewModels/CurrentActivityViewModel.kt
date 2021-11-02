package com.forsvarir.timetracker.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.forsvarir.timetracker.data.ActivityInstance
import com.forsvarir.timetracker.data.TimeTrackerRepository
import java.util.*

class CurrentActivityViewModel(timeTrackerRepository: TimeTrackerRepository) : ViewModel() {
    val activityHistory: LiveData<List<ActivityInstance>> = timeTrackerRepository.getPreviousActivities()
    val availableActivities : LiveData<List<String>> = timeTrackerRepository.availableActivities()

    private val mutablePreviousActivities = MutableLiveData<List<String>>(listOf())//LinkedList<String>>()
    val previousActivities: LiveData<List<String>> = mutablePreviousActivities

    private val mutableCurrentActivity = MutableLiveData<String>("Unknown")
    val currentActivity : LiveData<String> = mutableCurrentActivity

    fun startActivity(newActivity: String) {
        mutableCurrentActivity.value = availableActivities.value?.find { it == newActivity }
    }
}
