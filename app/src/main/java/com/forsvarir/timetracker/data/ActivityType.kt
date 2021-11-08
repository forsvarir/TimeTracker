package com.forsvarir.timetracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_type")
data class ActivityType(
    @PrimaryKey(autoGenerate = true)
    var activityTypeId: Long = 0,
    @ColumnInfo(name = "name")
    var name: String
)
