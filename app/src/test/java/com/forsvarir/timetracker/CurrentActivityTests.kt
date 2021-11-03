package com.forsvarir.timetracker

import com.forsvarir.timetracker.data.ActivityConstants
import com.forsvarir.timetracker.data.TimeTrackerRepository
import com.forsvarir.timetracker.viewModels.CurrentActivityViewModel
import com.forsvarir.timetracker.viewModels.TimeFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(InstantTaskExecutorExtension::class)
class CurrentActivityTests {
    @Test
    fun activitiesAvailable() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")

        val viewModel = CurrentActivityViewModel(TimeTrackerRepository(possibleActivities))

        assertThat(viewModel.availableActivities.value).containsExactly(
            "Programming",
            "Walking",
            "Sleeping"
        )
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
        val clock = ProgrammableTimeFactory()
        val model = CurrentActivityViewModel(TimeTrackerRepository(possibleActivities), clock)

        model.startActivity("Programming")

        assertThat(model.currentActivity.value).isNotNull
        val currentActivity = model.currentActivity.value!!
        assertThat(currentActivity.name).isEqualTo("Programming")
        assertThat(currentActivity.startTime).isEqualTo(clock.now())
        assertThat(currentActivity.endTime).isNull()

        assertThat(model.previousActivities.value).isEmpty()
    }


    @Test
    fun runningActivity_newActivity() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")
        val initialActivityStartTime = LocalDateTime.of(2021, 1, 2, 5, 30)
        val secondActivityStartTime = initialActivityStartTime.plusHours(1)
        val clock = ProgrammableTimeFactory(initialActivityStartTime)
        val model = CurrentActivityViewModel(TimeTrackerRepository(possibleActivities), clock)

        model.startActivity("Programming")

        clock.setNow(secondActivityStartTime)
        model.startActivity("Walking")

        assertThat(model.currentActivity.value).isNotNull
        val currentActivity = model.currentActivity.value!!
        assertThat(model.previousActivities.value).hasSize(1)
        assertThat(model.previousActivities.value).isNotNull
        val secondActivity = model.previousActivities.value!!.first()

        assertThat(currentActivity.name).isEqualTo("Walking")
        assertThat(currentActivity.startTime).isEqualTo(secondActivityStartTime)
        assertThat(currentActivity.endTime).isNull()

        assertThat(secondActivity.name).isEqualTo("Programming")
        assertThat(secondActivity.startTime).isEqualTo(initialActivityStartTime)
        assertThat(secondActivity.endTime).isEqualTo(secondActivityStartTime)
    }

    @Test
    fun restartCurrentActivity() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")
        val initialActivityStartTime = LocalDateTime.of(2021, 1, 2, 5, 30)
        val secondActivityStartTime = initialActivityStartTime.plusHours(1)
        val clock = ProgrammableTimeFactory(initialActivityStartTime)
        val model = CurrentActivityViewModel(TimeTrackerRepository(possibleActivities), clock)

        model.startActivity("Programming")
        clock.setNow(secondActivityStartTime)
        model.startActivity("Programming")

        assertThat(model.currentActivity.value).isNotNull
        val currentActivity = model.currentActivity.value!!
        assertThat(currentActivity.name).isEqualTo("Programming")
        assertThat(currentActivity.startTime).isEqualTo(initialActivityStartTime)
        assertThat(currentActivity.endTime).isNull()

        assertThat(model.previousActivities.value).hasSize(0)
    }
}

class ProgrammableTimeFactory(initialTime: LocalDateTime = LocalDateTime.now()) :
    TimeFactory {
    private var currentTime: LocalDateTime = initialTime

    override fun now(): LocalDateTime {
        return currentTime
    }

    fun setNow(newNow: LocalDateTime) {
        currentTime = newNow
    }
}
