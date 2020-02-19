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
import com.huawei.codelabs.hihealth.happysport.utils.HiHealthSetup
import com.huawei.codelabs.hihealth.happysport.utils.InjectorUtils
import com.huawei.codelabs.hihealth.happysport.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.qualifiedName
    }

    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            InjectorUtils.provideMainViewModelFactory(application)
        ).get(
            MainViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "create activity")

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.stepDeltaSequence.observe(this, Observer<List<TimeSeqData>> { stepDeltaSequence ->
            val chart = binding.chart
            val entries = mutableListOf<Entry>()

            stepDeltaSequence.forEach {
                entries.add(Entry(it.time.toFloat() / 1000, it.value.toFloat()))
            }

            val dataSet = LineDataSet(entries, "step delta")
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

        // login with huawei id.
        HiHealthSetup.login(this, viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        HiHealthSetup.onActivityResult(requestCode, resultCode, data)
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
        val btn: Button = findViewById(view.id)

        if (viewModel.isRunning()) {
            viewModel.stop()
            btn.text = resources.getString(R.string.app_start)
        } else {
            viewModel.start()
            btn.text = resources.getString(R.string.app_stop)
        }
    }

}
