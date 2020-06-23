package com.huawei.codelabs.hihealth.happysport.data

import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

class WalkingRepository private constructor(
    private val sportDao: SportDao,
    private val timeSeqDataDao: TimeSeqDataDao
) {
    fun getLatestSport(): LiveData<WalkingSport?> {
        return sportDao.getLatestSportByType(SportType.WALKING).map {
            it?.let {
                WalkingSport().apply {
                    sport = it.sport
                    it.timeSeqDatas.forEach { seqData ->
                        when (seqData.tag) {
                            TimeSeqDataTag.STEP_DELTA -> stepDeltaSeq.add(seqData)
                            TimeSeqDataTag.STRIDE_FREQUENCY -> strideFrequencySeq.add(seqData)
                            else -> throw IllegalStateException()
                        }
                    }
                }
            }
        }
    }


    fun getSports(): LiveData<List<WalkingSport>> {
        return sportDao.getSports(SportType.WALKING).map {
            mutableListOf<WalkingSport>().apply {
                it.forEach {
                    this.add(WalkingSport().apply {
                        sport = it.sport
                        it.timeSeqDatas.forEach { seqData ->
                            when (seqData.tag) {
                                TimeSeqDataTag.STEP_DELTA -> stepDeltaSeq.add(seqData)
                                TimeSeqDataTag.STRIDE_FREQUENCY -> strideFrequencySeq.add(seqData)
                                else -> throw IllegalStateException()
                            }
                        }
                    })
                }
            }
        }
    }

    fun getSport(id: Long): LiveData<WalkingSport?> {
        return sportDao.getSport(id).map {
            it?.let {
                WalkingSport().apply {
                    sport = it.sport
                    it.timeSeqDatas.forEach { seqData ->
                        when (seqData.tag) {
                            TimeSeqDataTag.STEP_DELTA -> stepDeltaSeq.add(seqData)
                            TimeSeqDataTag.STRIDE_FREQUENCY -> strideFrequencySeq.add(seqData)
                            else -> throw IllegalStateException()
                        }
                    }
                }
            }
        }
    }

    suspend fun createWalkingSport(): WalkingSport {
        return withContext(IO) {
            val sport = Sport(
                sportType = SportType.WALKING,
                sportStatus = SportStatus.STARTED,
                startTime = Calendar.getInstance()
            )
            sport.id = sportDao.insertSport(sport)
            WalkingSport(sport = sport)
        }
    }

    inner class WalkingSport(
        var sport: Sport = Sport(),
        val stepDeltaSeq: MutableList<TimeSeqData> = mutableListOf(),
        val strideFrequencySeq: MutableList<TimeSeqData> = mutableListOf(),
        val mutex: Mutex = Mutex()
    ) {
        fun formatDuration(): String {
            return DateUtils.formatElapsedTime((sport.stopTime.timeInMillis - sport.startTime.timeInMillis) / 1000)
        }

        fun formatStartTime(): String {
            return SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.CHINA
            ).format(sport.startTime.time)
        }

        fun formatTotalSteps(): String {
            var total: Long = 0
            stepDeltaSeq.forEach {
                total += it.value
            }
            return total.toString()
        }

        fun formatCalorie(): String {
            // calorie(kcal) = weight(kg) * distance(km) * 1.036
            return String.format("%.2f", 60 * formatTotalSteps().toLong() * 0.4 * 0.001 * 1.036)
        }

        override fun equals(other: Any?): Boolean {
            other as WalkingSport
            return sport == other.sport &&
                    stepDeltaSeq == other.stepDeltaSeq
        }

        suspend fun feedStepDeltaData(stepDelta: Long, time: Long) {
            withContext(IO) {
                timeSeqDataDao.insertTimeSeqData(
                    TimeSeqData(
                        tag = TimeSeqDataTag.STEP_DELTA,
                        sportId = sport.id,
                        time = time,
                        value = stepDelta
                    )
                )
            }
        }

        suspend fun start() {
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
    }

    companion object {
        @Volatile
        private var instance: WalkingRepository? = null

        fun getInstance(sportDao: SportDao, timeSeqDataDao: TimeSeqDataDao) =
            instance ?: synchronized(this) {
                instance ?: WalkingRepository(sportDao, timeSeqDataDao).also { instance = it }
            }
    }
}