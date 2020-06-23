package com.huawei.codelabs.hihealth.happysport.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TimeSeqDataDao {
    @Query("SELECT * FROM time_seq_datas")
    fun getTimeSeqDatas(): LiveData<List<TimeSeqData>>

    @Query("SELECT * FROM time_seq_datas WHERE id = :id")
    fun getTimeSeqData(id: Long): LiveData<TimeSeqData>

    @Query("SELECT * FROM time_seq_datas WHERE sportId = :sportId")
    fun getTimeSeqDataForSport(sportId: Long): LiveData<List<TimeSeqData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTimeSeqData(data: TimeSeqData): Long
}
