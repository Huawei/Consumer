package com.huawei.codelabs.hihealth.happysport.data

import androidx.room.Embedded
import androidx.room.Relation

class SportAndTimeSeqData {
    @Embedded
    lateinit  var sport: Sport


    @Relation(parentColumn = "id", entityColumn = "sportId")
    var timeSeqDatas: List<TimeSeqData> = arrayListOf()
}
