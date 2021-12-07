package com.forsvarir.timetracker.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.forsvarir.timetracker.data.TimeTrackerRepository
import com.forsvarir.timetracker.data.entities.ActivityInstance

class ActivityHistoryViewModel(
    val timeTrackerRepository: TimeTrackerRepository
) : ViewModel() {

    val previousActivities: LiveData<List<ActivityInstance>> =
        timeTrackerRepository.allPreviousActivities()
}