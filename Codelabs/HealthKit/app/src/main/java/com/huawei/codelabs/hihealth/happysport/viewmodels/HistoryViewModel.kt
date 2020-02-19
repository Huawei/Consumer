package com.huawei.codelabs.hihealth.happysport.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.huawei.codelabs.hihealth.happysport.data.WalkingRepository

class HistoryViewModel(val walkingRepository: WalkingRepository) : ViewModel() {
    val isHistoryEmpty: LiveData<Boolean> = walkingRepository.getSports().map{
        it.isEmpty()
    }

    val allSports: LiveData<List<WalkingRepository.WalkingSport>> = walkingRepository.getSports()

    fun getSport(id: Long): LiveData<WalkingRepository.WalkingSport?> {
        return walkingRepository.getSport(id)
    }
}