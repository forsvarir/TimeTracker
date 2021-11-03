package com.forsvarir.timetracker.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.forsvarir.timetracker.data.ActivityConstants
import com.forsvarir.timetracker.data.ActivityInstance
import com.forsvarir.timetracker.data.TimeTrackerRepository
import java.util.*

class CurrentActivityViewModel(timeTrackerRepository: TimeTrackerRepository) : ViewModel() {
    val activityHistory: LiveData<List<ActivityInstance>> = timeTrackerRepository.getPreviousActivities()
    val availableActivities : LiveData<List<String>> = timeTrackerRepository.availableActivities()

    private val mutablePreviousActivities = MutableLiveData<MutableList<ActivityInstance>>(LinkedList())
    val previousActivities: LiveData<MutableList<ActivityInstance>> = mutablePreviousActivities

    private val mutableCurrentActivity = MutableLiveData<ActivityInstance>(ActivityConstants.Unknown)
    val currentActivity : LiveData<ActivityInstance> = mutableCurrentActivity

    fun startActivity(newActivity: String) {
        if(newActivity == currentActivity.value?.name) {
            return
        }
        if(currentActivity.value?.name != ActivityConstants.Unknown.name) {
            previousActivities.value?.add(currentActivity.value!!)
        }
        mutableCurrentActivity.value =
            ActivityInstance(availableActivities.value?.find { it == newActivity }!!,
                "")
    }
}
