package com.forsvarir.timetracker.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forsvarir.timetracker.data.ActivityInstance
import com.forsvarir.timetracker.ui.theme.TimeTrackerTheme

@Preview(widthDp = 480, heightDp = 800)
@Composable
private fun PreviewActivityHistoryView() {
    TimeTrackerTheme {
        ActivityHistoryView(events = stubbedEvents())
    }
}

@Composable
fun ActivityHistoryView(events: List<ActivityInstance>) {
    Column(Modifier.padding(all = 8.dp)) {
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
        events.forEach {
            EventRow(it.name, it.duration)
        }
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

private fun stubbedEvents(): List<ActivityInstance> {
    return listOf(
        ActivityInstance("Programming"),
        ActivityInstance("Walking"),
        ActivityInstance("Programming"),
        ActivityInstance("Eating")
    )
}
