package com.huawei.codelabs.hihealth.happysport.utils

import android.content.Context
import com.huawei.codelabs.hihealth.happysport.data.AppDatabase
import com.huawei.codelabs.hihealth.happysport.data.RunningRepository
import com.huawei.codelabs.hihealth.happysport.viewmodels.MainViewModelFactory
import com.huawei.codelabs.hihealth.happysport.viewmodels.HistoryViewModelFactory

object InjectorUtils {
    fun provideMainViewModelFactory(context: Context): MainViewModelFactory {
        val repository = RunningRepository.getInstance(
            AppDatabase.getInstance(context).sportDao(),
            AppDatabase.getInstance(context).timeSeqDataDao()
        )
        return MainViewModelFactory(repository, context)
    }

    fun provideHistoryViewModelFactory(context: Context): HistoryViewModelFactory{
        val repository = RunningRepository.getInstance(
            AppDatabase.getInstance(context).sportDao(),
            AppDatabase.getInstance(context).timeSeqDataDao()
        )
        return HistoryViewModelFactory(repository)
    }
}