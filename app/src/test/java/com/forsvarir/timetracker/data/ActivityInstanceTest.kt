package com.forsvarir.timetracker.data

import com.forsvarir.timetracker.CurrentActivityTests
import com.forsvarir.timetracker.data.entities.ActivityInstance
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ActivityInstanceTest {
    @Test
    fun duration_noEndTime() {
        val clock = CurrentActivityTests.ProgrammableTimeFactory()
        val instance = ActivityInstance(name = "ignored", clock = clock)

        clock.setNow(
            clock.now()
                .plusHours(12)
                .plusMinutes(34)
                .plusSeconds(52)
        )

        assertThat(instance.getDuration()).isEqualTo("12:34.52")
    }

    @Test
    fun duration_withEndTime() {
        val clock = CurrentActivityTests.ProgrammableTimeFactory()
        val instance = ActivityInstance(name = "ignored", clock = clock)

        instance.endTime =
            clock.now()
                .plusHours(23)
                .plusMinutes(24)
                .plusSeconds(25)


        assertThat(instance.getDuration()).isEqualTo("23:24.25")
    }
}