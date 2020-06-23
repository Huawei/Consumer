package com.huawei.codelabs.hihealth.happysport.viewmodels

import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.*
import com.huawei.codelabs.hihealth.happysport.data.*
import com.huawei.codelabs.hihealth.happysport.utils.mToKm
import com.huawei.codelabs.hihealth.happysport.utils.mpsToKph

class MainViewModel(runningRepository: RunningRepository) : ViewModel() {
    private val Tag: String = "MainViewModel"

    init {

        Log.d(Tag, "init main view model")
    }

    val linkStatus : LiveData<String> = runningRepository.getLinkStatus()

    var sportStatus: LiveData<String> = runningRepository.getLatestSport().map {
        it?.sport?.sportStatus.toString()
    }

    val distance: LiveData<String> = runningRepository.getLatestSport().map {
        if (it == null || it.distanceSeq.isEmpty()) {
            return@map 0.toLong().mToKm()
        }

        //(it.distanceSeq.last().value - it.distanceSeq.first().value).mToKm()
        it.distanceSeq.last().value.mToKm()
    }

    val velocity: LiveData<String> = runningRepository.getLatestSport().map {
        if (it == null || it.velocitySeq.isEmpty()) {
            return@map 0.toLong().mpsToKph()
        }

        it.velocitySeq.last().value.mpsToKph()
    }

    val time: LiveData<String> = runningRepository.getLatestSport().map {
        if (it == null) {
            DateUtils.formatElapsedTime(0).toString()
        } else {
            DateUtils.formatElapsedTime(it.sport.duration).toString()
        }
    }

    val heartRateSequence: LiveData<List<TimeSeqData>> = runningRepository.getLatestSport().map {
        Log.d(Tag, "heart rate updated")
        it?.heartRateSeq ?: listOf<TimeSeqData>()
    }
}