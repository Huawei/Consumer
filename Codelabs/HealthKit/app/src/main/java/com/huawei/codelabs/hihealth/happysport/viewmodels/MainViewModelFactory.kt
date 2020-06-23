package com.huawei.codelabs.hihealth.happysport.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.huawei.codelabs.hihealth.happysport.data.WalkingRepository

class MainViewModelFactory(
    private val repository: WalkingRepository,
    private val app: Application
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository, app) as T
    }
}