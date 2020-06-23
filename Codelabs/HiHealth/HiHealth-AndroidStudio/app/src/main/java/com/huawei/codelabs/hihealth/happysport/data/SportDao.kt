package com.huawei.codelabs.hihealth.happysport.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SportDao {
    @Transaction
    @Query("select * FROM sports WHERE sportType = :sportType ORDER BY id DESC LIMIT 1")
    fun getLatestSportByType(sportType: SportType): LiveData<SportAndTimeSeqData?>

    @Transaction
    @Query("select * FROM sports WHERE sportType = :sportType")
    fun getSports(sportType: SportType): LiveData<List<SportAndTimeSeqData>>


    @Transaction
    @Query("select * FROM sports WHERE id = :id")
    fun getSport(id: Long): LiveData<SportAndTimeSeqData?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSport(sport: Sport): Long

    @Update
    fun updateSport(sport: Sport)
}