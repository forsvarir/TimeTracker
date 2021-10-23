package com.forsvarir.timetracker

import com.forsvarir.timetracker.viewModels.CurrentActivityViewModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CurrentActivityTests {
    private val possibleActivities = listOf("Programming", "Walking", "Sleeping")
    private val viewModel = CurrentActivityViewModel(possibleActivities)

    @Test
    fun activitiesAvailable() {
        assertThat(viewModel.availableActivities).isNotEmpty
    }
}