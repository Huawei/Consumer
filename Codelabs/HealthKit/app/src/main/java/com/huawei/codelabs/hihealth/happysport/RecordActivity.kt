package com.huawei.codelabs.hihealth.happysport

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.huawei.codelabs.hihealth.happysport.databinding.ActivityRecordBinding
import com.huawei.codelabs.hihealth.happysport.utils.InjectorUtils
import com.huawei.codelabs.hihealth.happysport.viewmodels.HistoryViewModel

class RecordActivity : AppCompatActivity() {
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
        val binding: ActivityRecordBinding = DataBindingUtil.setContentView(this, R.layout.activity_record)
        binding.lifecycleOwner = this

        val sportId = intent.getLongExtra("SPORT_ID", 0)
        val walkingSport = viewModel.getSport(sportId)
        walkingSport.observe(this, Observer { sport ->
            sport?.let {
                binding.walkingSport = sport

                val chart = binding.chart
                val entries = mutableListOf<Entry>()

                sport.stepDeltaSeq.forEach {
                    entries.add(
                        Entry(
                            it.time.toFloat() / 1000,
                            it.value.toFloat()
                        )
                    )
                }

                val dataSet = LineDataSet(entries, "heart rate")
                dataSet.color = getColor(R.color.colorChartLine)
                dataSet.setDrawCircleHole(false)
                dataSet.setDrawCircles(false)
                dataSet.setDrawValues(false)
                dataSet.setDrawFilled(true)
                dataSet.fillDrawable
                dataSet.lineWidth = 2.0f
                chart.data = LineData(dataSet)

                chart.axisLeft.axisMinimum = 0f
                chart.axisLeft.axisMaximum = 220f
                chart.axisRight.axisMinimum = 0f
                chart.axisRight.axisMaximum = 220f
                chart.extraLeftOffset = 12f
                chart.extraRightOffset = 12f
                chart.description = null

                chart.invalidate()
            }
        })
    }
}