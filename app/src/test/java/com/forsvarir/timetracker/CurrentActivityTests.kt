package com.forsvarir.timetracker

import com.forsvarir.timetracker.data.TimeTrackerRepository
import com.forsvarir.timetracker.viewModels.CurrentActivityViewModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CurrentActivityTests {

    @Test
    fun activitiesAvailable() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")

        val viewModel = CurrentActivityViewModel(TimeTrackerRepository(possibleActivities))

        assertThat(viewModel.availableActivities).containsExactly("Programming", "Walking", "Sleeping")
    }
}