package com.forsvarir.timetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TimeTrackerDao {
    @Insert
    suspend fun insertActivityType(night: ActivityType)

    @Query("SELECT * FROM activity_type order by activityTypeId")
    fun getActivityTypes(): List<ActivityType>

    @Query("SELECT COUNT(1) FROM activity_type")
    fun countActivities(): Int
}