package com.huawei.codelabs.hihealth.happysport.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.huawei.codelabs.hihealth.happysport.R
import com.huawei.codelabs.hihealth.happysport.data.*
import com.huawei.codelabs.hihealth.happysport.viewmodels.HiHealthBaseAdapter.BaseSportData
import com.huawei.codelabs.hihealth.happysport.viewmodels.HiHealthBaseAdapter.HiHealthBaseAdapter
import com.huawei.codelabs.hihealth.happysport.viewmodels.HiHealthBaseAdapter.ISportListener
import com.huawei.codelabs.hihealth.happysport.viewmodels.HiHealthBaseAdapter.WalkingSportData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(
    private val walkingRepository: WalkingRepository,
    private val app: Application
) : AndroidViewModel(app),
    ISportListener {
    companion object {
        private val TAG = MainViewModel::class.qualifiedName
    }

    init {
        Log.d(TAG, "init main view model")
    }

    var currentWalk: WalkingRepository.WalkingSport? = null

    val hiHealthBaseAdapter: HiHealthBaseAdapter = HiHealthBaseAdapter(app.applicationContext, this)

    val linkStatus = MutableLiveData(app.resources.getString(R.string.app_link_disconnected))

    val totalSteps: LiveData<String?> = walkingRepository.getLatestSport().map {
        it?.let {
            it.formatTotalSteps()
        }
    }

    val strideFrequency: LiveData<String?> = walkingRepository.getLatestSport().map {
        it?.let {
            var stride: Long = 0
            val lastStepDelta = it.stepDeltaSeq.lastOrNull()
            if (lastStepDelta != null) {
                stride = lastStepDelta.value * (60*1000/HiHealthBaseAdapter.SAMPLE_INTERVAL)
            }
            stride.toString()
        }
    }

    val calorie: LiveData<String?> = walkingRepository.getLatestSport().map {
        it?.let {
            it.formatCalorie()
        }
    }

    val stepDeltaSequence: LiveData<List<TimeSeqData>> = walkingRepository.getLatestSport().map {
        Log.d(TAG, "step delta updated")
        it?.stepDeltaSeq ?: mutableListOf()
    }

    fun start() {
        GlobalScope.launch {
            currentWalk = walkingRepository.createWalkingSport()
        }
        hiHealthBaseAdapter.start(this)
    }

    fun stop() {
        GlobalScope.launch {
            currentWalk?.stop()
            hiHealthBaseAdapter.stop()
            currentWalk = null
        }
    }

    fun isRunning(): Boolean {
        return currentWalk != null
    }

    override fun onConnect() {
        linkStatus.postValue(app.resources.getString(R.string.app_link_connected))
    }

    override fun onResume() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDisconnect() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSportType(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRecvData(data: BaseSportData) {
        if (data is WalkingSportData) {
            GlobalScope.launch {
                currentWalk?.feedStepDeltaData(data.stepDelta, data.timestamp)
            }
        }
    }

    override fun onRunning() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}