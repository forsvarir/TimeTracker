package com.forsvarir.timetracker.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.forsvarir.timetracker.data.ActivityInstance
import com.forsvarir.timetracker.data.TimeTrackerRepository

class CurrentActivityViewModel(timeTrackerRepository: TimeTrackerRepository) : ViewModel() {

    val activityHistory: LiveData<List<ActivityInstance>> = timeTrackerRepository.getPreviousActivities()
    val availableActivities : LiveData<List<String>> = timeTrackerRepository.availableActivities()

}
