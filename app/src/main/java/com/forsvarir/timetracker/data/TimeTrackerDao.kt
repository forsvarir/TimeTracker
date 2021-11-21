package com.forsvarir.timetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.forsvarir.timetracker.data.entities.ActivityInstance
import com.forsvarir.timetracker.data.entities.ActivityType

@Dao
interface TimeTrackerDao {
    @Insert
    suspend fun insertActivityType(night: ActivityType)

    @Query("SELECT * FROM activity_type ORDER BY activity_type_id")
    fun getActivityTypes(): List<ActivityType>

    @Query("SELECT COUNT(1) FROM activity_type")
    fun countActivityTypes(): Int

    @Query("SELECT * FROM activity_instance ORDER BY activity_instance_id DESC")
    suspend fun getPreviousActivityInstances(): List<ActivityInstance>

    @Insert
    suspend fun insertActivityInstance(instance: ActivityInstance): Long

    @Update
    suspend fun updateActivityInstance(instance: ActivityInstance)
}