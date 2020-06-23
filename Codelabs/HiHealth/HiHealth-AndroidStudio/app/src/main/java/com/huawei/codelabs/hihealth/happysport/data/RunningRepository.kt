package com.huawei.codelabs.hihealth.happysport.data

import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.huawei.codelabs.hihealth.happysport.utils.mToKm
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class RunningRepository private constructor(
    private val sportDao: SportDao,
    private val timeSeqDataDao: TimeSeqDataDao
) {
    private val linkStatus = MutableLiveData("DISCONNECTED")

    fun getLatestSport(): LiveData<RunningSport?> {
        return sportDao.getLatestSportByType(SportType.INDOOR_RUNNING).map {
            it?.let {
                RunningSport().apply {
                    sport = it.sport
                    it.timeSeqDatas.forEach { seqData ->
                        when (seqData.tag) {
                            TimeSeqDataTag.HEART_RATE -> heartRateSeq.add(seqData)
                            TimeSeqDataTag.VELOCITY -> velocitySeq.add(seqData)
                            TimeSeqDataTag.DISTANCE -> distanceSeq.add(seqData)
                        }
                    }
                }
            }
        }
    }


    fun getSports(): LiveData<List<RunningSport>> {
        return sportDao.getSports(SportType.INDOOR_RUNNING).map {
            mutableListOf<RunningSport>().apply {
                it.forEach {
                    this.add(RunningSport().apply {
                        sport = it.sport
                        it.timeSeqDatas.forEach { seqData ->
                            when (seqData.tag) {
                                TimeSeqDataTag.HEART_RATE -> heartRateSeq.add(seqData)
                                TimeSeqDataTag.VELOCITY -> velocitySeq.add(seqData)
                                TimeSeqDataTag.DISTANCE -> distanceSeq.add(seqData)
                            }
                        }
                    })
                }
            }
        }
    }

    fun getSport(id: Long): LiveData<RunningSport?> {
        return sportDao.getSport(id).map {
            it?.let {
                RunningSport().apply {
                    sport = it.sport
                    it.timeSeqDatas.forEach { seqData ->
                        when (seqData.tag) {
                            TimeSeqDataTag.HEART_RATE -> heartRateSeq.add(seqData)
                            TimeSeqDataTag.VELOCITY -> velocitySeq.add(seqData)
                            TimeSeqDataTag.DISTANCE -> distanceSeq.add(seqData)
                        }
                    }
                }
            }
        }
    }

    suspend fun createRunningSport(): RunningSport {
        return withContext(IO) {
            val sport = Sport(
                sportType = SportType.INDOOR_RUNNING,
                sportStatus = SportStatus.STARTED,
                startTime = Calendar.getInstance()
            )
            sport.id = sportDao.insertSport(sport)
            RunningSport(sport = sport)
        }
    }

    fun setLinkStatus(status: String) {
        linkStatus.postValue(status)
    }

    fun getLinkStatus(): LiveData<String> {
        return linkStatus
    }

    inner class RunningSport(
        var sport: Sport = Sport(),
        val heartRateSeq: MutableList<TimeSeqData> = mutableListOf(),
        val velocitySeq: MutableList<TimeSeqData> = mutableListOf(),
        val distanceSeq: MutableList<TimeSeqData> = mutableListOf(),
        val mutex: Mutex = Mutex()
    ) {
        fun formatDuration(): String {
            return DateUtils.formatElapsedTime(sport.duration)
        }

        fun formatDistance(): String {
            if (distanceSeq.isEmpty()) {
                return 0.toLong().mToKm()
            }

            return (distanceSeq.last().value - distanceSeq.first().value).mToKm()
        }

        fun formatStartTime(): String {
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(sport.startTime.time)
        }

        override fun equals(other: Any?): Boolean {
            other as RunningSport
            return sport == other.sport &&
                    heartRateSeq == other.heartRateSeq &&
                    velocitySeq == other.velocitySeq &&
                    distanceSeq == other.distanceSeq
        }


        suspend fun feedHeartRateData(heartRate: Long, time: Long) {
            withContext(IO) {
                timeSeqDataDao.insertTimeSeqData(
                    TimeSeqData(
                        tag = TimeSeqDataTag.HEART_RATE,
                        sportId = sport.id,
                        time = time,
                        value = heartRate
                    )
                )
            }
        }

        suspend fun feedVelocityData(velocity: Long, time: Long) {
            withContext(IO) {
                timeSeqDataDao.insertTimeSeqData(
                    TimeSeqData(
                        tag = TimeSeqDataTag.VELOCITY,
                        sportId = sport.id,
                        time = time,
                        value = velocity
                    )
                )
            }
        }

        suspend fun feedDistanceData(distance: Long, time: Long) {
            withContext(IO) {
                timeSeqDataDao.insertTimeSeqData(
                    TimeSeqData(
                        tag = TimeSeqDataTag.DISTANCE,
                        sportId = sport.id,
                        time = time,
                        value = distance
                    )
                )
            }
        }

        suspend fun feedDurationData(duration: Long) {
            withContext(IO) {
                sport.duration = duration
                sportDao.updateSport(sport)
            }
        }

        suspend fun running() {
            withContext(IO) {
                if (sport.sportStatus != SportStatus.RUNNING) {
                    sport.sportStatus = SportStatus.RUNNING
                    sportDao.updateSport(sport)
                }
            }
        }

        suspend fun stop() {
            withContext(IO) {
                mutex.withLock {
                    sport.stopTime = Calendar.getInstance()
                    sport.sportStatus = SportStatus.STOPPED
                    sportDao.updateSport(sport)
                }
            }
        }

        suspend fun pause() {
            withContext(IO) {
                mutex.withLock {
                    sport.sportStatus = SportStatus.PAUSED
                    sportDao.updateSport(sport)
                }
            }
        }

        suspend fun resume() {
            withContext(IO) {
                mutex.withLock {
                    sport.sportStatus = SportStatus.RESUMED
                    sportDao.updateSport(sport)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var instance: RunningRepository? = null

        fun getInstance(sportDao: SportDao, timeSeqDataDao: TimeSeqDataDao) =
            instance ?: synchronized(this) {
                instance ?: RunningRepository(sportDao, timeSeqDataDao).also { instance = it }
            }
    }
}