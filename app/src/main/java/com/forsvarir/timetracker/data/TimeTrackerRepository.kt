package com.forsvarir.timetracker.data

class TimeTrackerRepository(private val possibleActivities: List<String>)  {
    fun availableActivities(): List<String> {
        return possibleActivities
    }
}
