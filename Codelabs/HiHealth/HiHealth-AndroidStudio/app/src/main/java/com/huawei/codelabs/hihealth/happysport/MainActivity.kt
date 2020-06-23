package com.huawei.codelabs.hihealth.happysport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.huawei.codelabs.hihealth.happysport.data.TimeSeqData
import com.huawei.codelabs.hihealth.happysport.databinding.ActivityMainBinding
import com.huawei.codelabs.hihealth.happysport.utils.InjectorUtils
import com.huawei.codelabs.hihealth.happysport.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            InjectorUtils.provideMainViewModelFactory(applicationContext)
        ).get(
            MainViewModel::class.java
        )
    }

    private val Tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(Tag, "create activity")
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.heartRateSequence.observe(this, Observer<List<TimeSeqData>> { heartRateSequence ->
            val chart = binding.chart
            val entries = mutableListOf<Entry>()

            heartRateSequence.forEach {
                entries.add(Entry(it.time.toFloat() / 1000, it.value.toFloat()))
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
        })

        val intent = Intent(this, HiHealthKitService::class.java)
        val btn: Button = findViewById(R.id.start_button)
        btn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                Log.i(Tag, "start onClick")
                startService(intent)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_history -> {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onStartStopBtnClick(view: View) {
    }

    @Suppress("UNUSED_PARAMETER")
    fun onTest(view: View) {
    }
}
