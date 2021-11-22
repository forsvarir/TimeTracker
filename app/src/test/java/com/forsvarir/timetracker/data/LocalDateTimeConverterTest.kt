package com.forsvarir.timetracker.data

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class LocalDateTimeConverterTest {
    val sut = LocalDateTimeConverter()

    @Test
    fun stringToDate_NullToNull() {
        assertThat(sut.toDate(null)).isNull()
    }

    @Test
    fun stringToDate_DateStringToDate() {
        assertThat(sut.toDate("2021-10-30T20:21:33")).isEqualTo(
            LocalDateTime.of(
                2021,
                10,
                30,
                20,
                21,
                33
            )
        )
    }

    @Test
    fun dateToString_NullToNull() {
        assertThat(sut.toDateString(null)).isNull()
    }

    @Test
    fun dateToString_LocalDateToString() {
        assertThat(
            sut.toDateString(
                LocalDateTime.of(
                    2020,
                    12,
                    31,
                    9,
                    2,
                    4
                )
            )
        ).isEqualTo("2020-12-31T09:02:04")
    }
}