package com.huawei.codelabs.hihealth.happysport.data

import androidx.room.TypeConverter
import java.util.*

/**
 * Type converters to allow Room to reference complex data types.
 */
class Converters {
    @TypeConverter
    fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun datestampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter
    fun timeSeqDataTagToString(tag: TimeSeqDataTag): String = tag.name

    @TypeConverter
    fun stringToTimeSeqDataTag(s: String): TimeSeqDataTag = TimeSeqDataTag.valueOf(s)

    @TypeConverter
    fun sportTypeToString(sportType: SportType): String = sportType.name

    @TypeConverter
    fun stringToSportType(s: String): SportType = SportType.valueOf(s)

    @TypeConverter
    fun sportStatusToString(sportStatus: SportStatus): String = sportStatus.name

    @TypeConverter
    fun stringToSportStatus(s: String): SportStatus = SportStatus.valueOf(s)
}