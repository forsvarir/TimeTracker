package com.forsvarir.timetracker.control

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.forsvarir.timetracker.MainActivity
import com.forsvarir.timetracker.R
import com.forsvarir.timetracker.verification.CurrentActivityScreenVerifier

class CurrentActivityScreenController(private val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {
    fun navigateToActivityHistoryScreen() {
        rule.onNodeWithContentDescription(rule.activity.getString(R.string.navigate_to_previous_activities))
            .performClick()
    }

    fun navigateToCurrentActivityScreen() {
        rule.onNodeWithContentDescription(rule.activity.getString(R.string.navigate_to_current_activity))
            .performClick()
    }

    fun setCurrentActivity(newActivity: String) {
        rule
            .onNodeWithContentDescription("Current Activity")
            .performClick()
        rule
            .onNodeWithText(newActivity)
            .performClick()
    }

    infix fun verify(
        block: CurrentActivityScreenVerifier.() -> Unit
    ): CurrentActivityScreenVerifier {
        return CurrentActivityScreenVerifier(rule).apply(block)
    }
}
