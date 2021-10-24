package com.forsvarir.timetracker.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.forsvarir.timetracker.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ActivityHistoryView(navTitle: (String) -> Unit = {}) {
    Column(Modifier.padding(all = 8.dp)) {
        navTitle(stringResource(R.string.previous_activities))
        Row {
            Text(
                modifier = Modifier.fillMaxWidth(0.5f),
                text = "Event",
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                modifier = Modifier.fillMaxWidth(0.5f),
                text = "Duration",
                style = MaterialTheme.typography.subtitle1
            )
        }
        EventRow("Programming", "00.11")
        EventRow("Walking", "01.11")
        EventRow("Sleeping", "08.01")
        EventRow("Eating", "02.11")
    }
}

@Composable
private fun EventRow(activity: String, duration: String) {
    Row {
        Text(
            modifier = Modifier.fillMaxWidth(0.5f),
            text = activity,
            style = MaterialTheme.typography.subtitle2
        )
        Text(
            modifier = Modifier.fillMaxWidth(0.5f),
            text = duration,
            style = MaterialTheme.typography.subtitle2
        )
    }
}
