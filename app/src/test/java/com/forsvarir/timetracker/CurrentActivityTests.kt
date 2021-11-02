package com.forsvarir.timetracker

import com.forsvarir.timetracker.data.TimeTrackerRepository
import com.forsvarir.timetracker.viewModels.CurrentActivityViewModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class CurrentActivityTests {

    @Test
    fun activitiesAvailable() {
            val possibleActivities = listOf("Programming", "Walking", "Sleeping")

        val viewModel = CurrentActivityViewModel(TimeTrackerRepository(possibleActivities))

        assertThat(viewModel.availableActivities.value).containsExactly("Programming", "Walking", "Sleeping")
    }

    @Test
    fun initialState() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")
        val model = CurrentActivityViewModel(TimeTrackerRepository(possibleActivities))

        assertThat(model.currentActivity.value).isEqualTo("Unknown")
        assertThat(model.previousActivities.value).isEmpty()
    }

    @Test
    fun noCurrentActivity_newActivity() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")
        val model = CurrentActivityViewModel(TimeTrackerRepository(possibleActivities))

        model.startActivity("Programming")

        assertThat(model.currentActivity.value).isEqualTo("Programming")
        assertThat(model.previousActivities.value).isEmpty()
    }
}