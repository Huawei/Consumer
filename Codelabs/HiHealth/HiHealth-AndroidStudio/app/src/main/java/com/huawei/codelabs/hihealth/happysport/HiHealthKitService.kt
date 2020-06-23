package com.huawei.codelabs.hihealth.happysport

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Button
import com.huawei.codelabs.hihealth.happysport.data.AppDatabase
import com.huawei.codelabs.hihealth.happysport.data.RunningRepository
import com.huawei.codelabs.hihealth.happysport.data.SportStatus
import com.huawei.codelabs.hihealth.happysport.data.SportType
import com.huawei.codelabs.hihealth.happysport.viewmodels.HiHealthKitAdapter.BaseSportData
import com.huawei.codelabs.hihealth.happysport.viewmodels.HiHealthKitAdapter.HiHealthKitAdapter
import com.huawei.codelabs.hihealth.happysport.viewmodels.HiHealthKitAdapter.ISportListener
import com.huawei.codelabs.hihealth.happysport.viewmodels.HiHealthKitAdapter.RunningSportData
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class HiHealthKitService : Service() {
    private var runningSport: RunningRepository.RunningSport? = null
    private val kitAdapter: HiHealthKitAdapter = HiHealthKitAdapter(this)
    private val Tag: String = "HiHealthKitService"
    private val sportEventChan = Channel<SportContext>(128)
    private val runningRepository by lazy {
        RunningRepository.getInstance(
            AppDatabase.getInstance(this).sportDao(),
            AppDatabase.getInstance(this).timeSeqDataDao()
        )
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(Tag, "on create")
        handleSportEvent()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(Tag, "on destroy")
        kitAdapter.unregisterSportListener()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(Tag, "on startCommand")
        kitAdapter.registerSportListener(KitListener())
        return super.onStartCommand(intent, flags, startId)
    }

    private fun handleSportEvent() {
        GlobalScope.launch {
            var active = false
            val checkRecover = suspend {
                if (!active) {
                    val rs = runningRepository.createRunningSport()
                    runningSport = rs
                    active = true
                    Log.d(Tag, "recovered")
                }
            }
            for (ctx in sportEventChan) {
                when (ctx.sportStatus) {
                    SportStatus.STARTED -> {
                        val rs = runningRepository.createRunningSport()
                        runningSport = rs
                        active = true
                    }
                    SportStatus.RUNNING -> {
                        checkRecover()
                        runningSport?.running()
                    }
                    SportStatus.PAUSED -> {
                        checkRecover()
                        runningSport?.pause()
                    }
                    SportStatus.RESUMED -> {
                        checkRecover()
                        runningSport?.resume()
                    }
                    SportStatus.STOPPED -> {
                        runningSport?.stop()
                        active = false
                        Log.d(Tag, "stopped")
                    }
                    SportStatus.FEED -> {
                        runningSport?.let {
                            ctx.data?.let { data ->
                                val t = data.duration.toLong()
                                it.feedDurationData(t)
                                it.feedDistanceData(data.distance.toLong(), t * 1000)
                                it.feedHeartRateData(data.heartRate.toLong(), t * 1000)
                                it.feedVelocityData(data.velocity.toLong(), t * 1000)
                            }
                        }
                    }
                }
            }
        }
    }

    fun startSport() {
        sportEventChan.offer(SportContext(SportStatus.STARTED, null))
    }

    fun pauseSport() {
        sportEventChan.offer(SportContext(SportStatus.PAUSED, null))
    }

    fun resumeSport() {
        sportEventChan.offer(SportContext(SportStatus.RESUMED, null))
    }

    fun stopSport() {
        sportEventChan.offer(SportContext(SportStatus.STOPPED, null))
    }

    fun runningSport() {
        sportEventChan.offer(SportContext(SportStatus.RUNNING, null))
    }

    fun feedData(data: RunningSportData) {
        sportEventChan.offer(SportContext(SportStatus.FEED, data))
    }


    data class SportContext(val sportStatus: SportStatus, val data: RunningSportData?)


    inner class KitListener : ISportListener {
        override fun onConnect() {
            runningRepository.setLinkStatus("CONNECTED")
        }

        override fun onDisconnect() {
            runningRepository.setLinkStatus("DISCONNECTED")
        }

        override fun onStart() {
            startSport()
        }

        override fun onPause() {
            pauseSport()
        }

        override fun onResume() {
            resumeSport()
        }

        override fun onRunning() {
            runningSport()
        }

        override fun onStop() {
            stopSport()
        }

        override fun onRecvData(data: BaseSportData) {
            if (data is RunningSportData) {
                feedData(data)
            }
        }

        override fun getSportType(): String {
            return SportType.INDOOR_RUNNING.toString()
        }
    }
}