package com.forsvarir.timetracker

import com.forsvarir.timetracker.data.ActivityConstants
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

        assertThat(model.currentActivity.value).isEqualTo(ActivityConstants.Unknown)
        assertThat(model.previousActivities.value).isEmpty()
    }

    @Test
    fun noCurrentActivity_newActivity() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")
        val model = CurrentActivityViewModel(TimeTrackerRepository(possibleActivities))

        model.startActivity("Programming")

        assertThat(model.currentActivity.value?.name).isEqualTo("Programming")
        assertThat(model.previousActivities.value).isEmpty()
    }

    @Test
    fun runningActivity_newActivity() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")
        val model = CurrentActivityViewModel(TimeTrackerRepository(possibleActivities))

        model.startActivity("Programming")
        model.startActivity("Walking")

        assertThat(model.currentActivity.value?.name).isEqualTo("Walking")
        assertThat(model.previousActivities.value).hasSize(1)
        assertThat(model.previousActivities.value!!.first().name).isEqualTo("Programming")
    }

    @Test
    fun restartCurrentActivity() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")
        val model = CurrentActivityViewModel(TimeTrackerRepository(possibleActivities))

        model.startActivity("Programming")
        model.startActivity("Programming")

        assertThat(model.currentActivity.value?.name).isEqualTo("Programming")
        assertThat(model.previousActivities.value).hasSize(0)
    }
}