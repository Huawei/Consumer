package com.huawei.codelabs.hihealth.happysport

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.huawei.codelabs.hihealth.happysport.adapters.WalkingSportAdapter
import com.huawei.codelabs.hihealth.happysport.databinding.ActivityHistoryBinding
import com.huawei.codelabs.hihealth.happysport.utils.InjectorUtils
import com.huawei.codelabs.hihealth.happysport.viewmodels.HistoryViewModel

class HistoryActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            InjectorUtils.provideHistoryViewModelFactory(applicationContext)
        ).get(
            HistoryViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityHistoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_history)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = WalkingSportAdapter(applicationContext)
        binding.runningSportList.adapter = adapter
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: WalkingSportAdapter) {
        viewModel.allSports.observe(this) { sports ->
            sports.forEach {
                Log.d("HistoryActivity", "dump the sport: ${it.sport}")
            }
            adapter.submitList(sports)
        }
    }
}