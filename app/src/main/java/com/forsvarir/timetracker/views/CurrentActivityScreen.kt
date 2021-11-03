package com.forsvarir.timetracker.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun CurrentActivityView() {
    var update by remember { mutableStateOf(false) }
    var currentItem by remember { mutableStateOf("Programming") }

    Column(Modifier.padding(all = 8.dp)) {
        Box(
            modifier = Modifier.wrapContentSize(Alignment.TopStart)
        ) {
            if (!update) {
                CurrentActivityStatus(currentItem) { update = true }
            } else {
                SelectCurrentActivity(currentItem) {
                    currentItem = it
                    update = false
                }
            }
        }
    }
}

@Composable
private fun CurrentActivityStatus(currentActivity: String, onUpdate: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = currentActivity,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.clickable(enabled = true, role = Role.Button) {
                onUpdate()
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "TickTickTick",
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
fun SelectCurrentActivity(currentItem: String, onSelected: (newItem: String) -> Unit) {
    val items = listOf("Programming", "Walking", "Sleeping", "Eating")

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { onSelected(currentItem) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
        ) {
            items.forEach { itemText ->
                DropdownMenuItem(onClick = {
                    onSelected(itemText)
                }) {
                    Text(itemText)
                }
            }
        }
    }
}
