package com.forsvarir.timetracker.utils

import java.time.Duration

fun formatDuration(duration: Duration): String {
    val millsPerSecond = 1000
    val secondsPerMinute = 60
    val minutesPerHour = 60

    var timeNotAllocated = duration.toMillis() / millsPerSecond
    val seconds = timeNotAllocated % secondsPerMinute
    timeNotAllocated /= secondsPerMinute
    val minutes = timeNotAllocated % minutesPerHour
    val hours = timeNotAllocated / minutesPerHour

    return String.format("%02d:%02d.%02d", hours, minutes, seconds)
}