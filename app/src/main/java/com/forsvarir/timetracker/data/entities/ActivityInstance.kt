package com.forsvarir.timetracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.forsvarir.timetracker.utils.formatDuration
import com.forsvarir.timetracker.viewModels.LocalTimeFactory
import com.forsvarir.timetracker.viewModels.TimeFactory
import java.time.Duration
import java.time.LocalDateTime

@Entity(tableName = "activity_instance")
data class ActivityInstance(
    @Ignore
    private val clock: TimeFactory = LocalTimeFactory(),

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "activity_instance_id")
    var activityInstanceId: Long = 0,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "start_time")
    var startTime: LocalDateTime = clock.now(),

    @ColumnInfo(name = "end_time")
    var endTime: LocalDateTime? = null
) {
    constructor(
        activityInstanceId: Long = 0,
        name: String,
        startTime: LocalDateTime,
        endTime: LocalDateTime? = null
    ) : this(LocalTimeFactory(), activityInstanceId, name, startTime, endTime)


    @Ignore
    fun getDuration(): String {
        val end = endTime ?: clock.now()
        return formatDuration(Duration.between(startTime, end))
    }
}
