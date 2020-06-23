package com.huawei.codelabs.hihealth.happysport.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "sports")
data class Sport(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val sportType: SportType = SportType.INDOOR_RUNNING,
    var sportStatus: SportStatus = SportStatus.STARTED,
    val startTime: Calendar = Calendar.getInstance(),
    var stopTime: Calendar = Calendar.getInstance(),
    var duration: Long = 0
)

enum class SportType {
    INDOOR_RUNNING,
    WALKING
}

enum class SportStatus {
    STARTED,
    RUNNING,
    PAUSED,
    RESUMED,
    STOPPED,
    FEED
}