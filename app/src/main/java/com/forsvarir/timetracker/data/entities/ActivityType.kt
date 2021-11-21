package com.forsvarir.timetracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_type")
data class ActivityType(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "activity_type_id")
    var activityTypeId: Long = 0,
    @ColumnInfo(name = "name")
    var name: String
)
