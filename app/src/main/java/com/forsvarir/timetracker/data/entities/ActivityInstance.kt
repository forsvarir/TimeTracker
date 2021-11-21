package com.forsvarir.timetracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.forsvarir.timetracker.viewModels.LocalTimeFactory
import com.forsvarir.timetracker.viewModels.TimeFactory
import java.time.Duration
import java.time.LocalDateTime

@Entity(tableName = "activity_type")
data class ActivityInstance(
    private val clock: TimeFactory = LocalTimeFactory(),
    @PrimaryKey(autoGenerate = true)
    var activityInstanceId: Long = 0,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "start_time")
    var startTime: LocalDateTime = clock.now(),
    @ColumnInfo(name = "end_time")
    var endTime: LocalDateTime? = null
) {
    fun getDuration(): String {
        val end = endTime ?: clock.now()
        val difference = Duration.between(startTime, end).toMillis()
        return String.format(
            "%02d:%02d.%02d",
            java.util.concurrent.TimeUnit.MILLISECONDS.toHours(difference),
            java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(difference) -
                    java.util.concurrent.TimeUnit.HOURS.toMinutes(
                        java.util.concurrent.TimeUnit.MILLISECONDS.toHours(
                            difference
                        )
                    ),
            java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(difference) -
                    java.util.concurrent.TimeUnit.MINUTES.toSeconds(
                        java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(
                            difference
                        )
                    )
        )
    }
}
