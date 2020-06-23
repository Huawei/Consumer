package com.huawei.codelabs.hihealth.happysport.utils

import android.app.Application
import android.content.Context
import com.huawei.codelabs.hihealth.happysport.data.AppDatabase
import com.huawei.codelabs.hihealth.happysport.data.WalkingRepository
import com.huawei.codelabs.hihealth.happysport.viewmodels.MainViewModelFactory
import com.huawei.codelabs.hihealth.happysport.viewmodels.HistoryViewModelFactory

object InjectorUtils {
    fun provideMainViewModelFactory(app: Application): MainViewModelFactory {
        val repository = WalkingRepository.getInstance(
            AppDatabase.getInstance(app.applicationContext).sportDao(),
            AppDatabase.getInstance(app.applicationContext).timeSeqDataDao()
        )
        return MainViewModelFactory(repository, app)
    }

    fun provideHistoryViewModelFactory(context: Context): HistoryViewModelFactory{
        val repository = WalkingRepository.getInstance(
            AppDatabase.getInstance(context).sportDao(),
            AppDatabase.getInstance(context).timeSeqDataDao()
        )
        return HistoryViewModelFactory(repository)
    }
}