package com.huawei.codelabs.hihealth.happysport.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.huawei.codelabs.hihealth.happysport.data.RunningRepository

class HistoryViewModel(val runningRepository: RunningRepository) : ViewModel() {
    val isHistoryEmpty: LiveData<Boolean> = runningRepository.getSports().map{
        it.isEmpty()
    }

    val allSports: LiveData<List<RunningRepository.RunningSport>> = runningRepository.getSports()

    fun getSport(id: Long): LiveData<RunningRepository.RunningSport?> {
        return runningRepository.getSport(id)
    }
}