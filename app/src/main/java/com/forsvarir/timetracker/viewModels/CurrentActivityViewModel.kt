package com.forsvarir.timetracker.viewModels

import androidx.lifecycle.ViewModel
import com.forsvarir.timetracker.data.TimeTrackerRepository

class CurrentActivityViewModel(timeTrackerRepository: TimeTrackerRepository) : ViewModel() {

    val availableActivities = timeTrackerRepository.availableActivities()

}
