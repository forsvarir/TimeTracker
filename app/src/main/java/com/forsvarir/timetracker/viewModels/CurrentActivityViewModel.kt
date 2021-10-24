package com.forsvarir.timetracker.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.forsvarir.timetracker.data.TimeTrackerRepository

class CurrentActivityViewModel(timeTrackerRepository: TimeTrackerRepository) : ViewModel() {

    val availableActivities : LiveData<List<String>> = timeTrackerRepository.availableActivities()

}
