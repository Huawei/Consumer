package com.huawei.codelabs.hihealth.happysport.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.huawei.codelabs.hihealth.happysport.data.WalkingRepository

class HistoryViewModelFactory(
    private val repository: WalkingRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryViewModel(repository) as T
    }
}