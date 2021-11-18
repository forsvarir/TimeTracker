package com.forsvarir.timetracker.verification

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.forsvarir.timetracker.MainActivity
import com.forsvarir.timetracker.R

class CurrentActivityScreenVerifier(private val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {
    fun currentActivityScreenIsOpen() {
        val currentActivity = rule.activity.getString(R.string.TitleCurrentActivityScreen)
        rule.onNodeWithText(currentActivity)
            .assertIsDisplayed()
    }

    fun activityHistoryScreenIsOpen() {
        val previousActivities = rule.activity.getString(R.string.TitlePreviousActivitiesScreen)
        rule.onNodeWithText(previousActivities)
            .assertIsDisplayed()
    }

    fun currentActivityIs(activityName: String) {
        rule.onNodeWithContentDescription(rule.activity.getString(R.string.CurrentRunningActivity))
            .assertTextContains(activityName)
    }

    fun currentRunningActivityIs(activityName: String, timeElapsedSinceStart: String) {
        rule.onNodeWithContentDescription(rule.activity.getString(R.string.RunningActivityProgress))
            .assertTextContains("$activityName $timeElapsedSinceStart")
    }

    fun noCurrentRunningActivityDisplayed() {
        rule.onAllNodesWithContentDescription(rule.activity.getString(R.string.RunningActivityProgress))
            .assertCountEquals(0)
    }
}
