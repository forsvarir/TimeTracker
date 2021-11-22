package com.forsvarir.timetracker.utils

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration

internal class DurationFormatterTest {
    @Test
    fun durationToString() {
        val duration = Duration.ofHours(2).plusMinutes(33).plusSeconds(10)
        assertThat(formatDuration(duration)).isEqualTo("02:33.10")
    }

    @Test
    fun durationWithDaysToStringConvertsDaysToHours() {
        val duration = Duration.ofDays(2).plusHours(2).plusMinutes(33).plusSeconds(10)
        assertThat(formatDuration(duration)).isEqualTo("50:33.10")
    }
}