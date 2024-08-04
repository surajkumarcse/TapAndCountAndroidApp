package com.lostfalcon.tapcount

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lostfalcon.tapcount.Notification.NotificationController
import com.lostfalcon.tapcount.SessionInfo.CentralCountInfo
import com.lostfalcon.tapcount.SessionInfo.HistoryInfoUnit
import com.lostfalcon.tapcount.SessionInfo.SessionInfo
import com.lostfalcon.tapcount.SessionInfo.SessionInfoSerializer
import com.lostfalcon.tapcount.SessionInfo.SessionInfoSerializerHelper
import java.util.Date
import java.util.UUID

class MainActivityViewModel : ViewModel() {
    var count = CentralCountInfo.count
    lateinit var vibrator: Vibrator
    private val VIBRATION_DURATION = 1000L
    val ANIMATION_DELAY = 20L
    val TONE_DURATION = 200L
    var toneGenerator: ToneGenerator? = null
    var isDialogShown: Boolean = false
    var isBackPressed = mutableStateOf(false)
    var sessionId = UUID.randomUUID()
    var sessionInfo: SessionInfo = SessionInfo(sessionId.toString(), Date().toString(), 0)

    fun playSound() {

    }

    fun doVibrate(context: Context, vibrationEffect: Int) {
        // Get the Vibrator service
        val vibrator = context.getSystemService(Vibrator::class.java)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createPredefined(vibrationEffect))
        } else {
            vibrator.vibrate(VIBRATION_DURATION)
        }
    }

    fun playTone(tone: Int = VibrationEffect.EFFECT_TICK, duration: Int = 20) {
        if(toneGenerator == null) {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 60)
        }
        toneGenerator?.startTone(tone, duration)
    }

    fun decrementCount(context: Context) {
        if(count.value > 0) {
            count.value--
            doVibrate(context, VibrationEffect.EFFECT_HEAVY_CLICK)
            playTone(ToneGenerator.TONE_SUP_ERROR)
            doNotify(context)
        }
    }

    fun incrementCount(context: Context) {
        count.value++
        doVibrate(context, VibrationEffect.EFFECT_TICK)
        playTone()
        doNotify(context)
    }

    fun onPause() {
        toneGenerator?.release()
        toneGenerator = null
    }

    fun onResetClicked(context: Context) {
        if(count.value > 0) {
            sessionInfo.dateTime = Date().toString()
            sessionInfo.countValue = count.value
            saveCurrentSessionInfoToDisk(context)
        }
        Log.i(LOG_TAG, "Saving session to Disk")
        count.value = 0
    }

    fun onHistoryClicked(context: Context): List<HistoryInfoUnit> {
        val historyInfoUnits = retrieveSavedSessionInfo(context)
        Log.e(LOG_TAG, historyInfoUnits.size.toString())
        return historyInfoUnits
    }

    fun saveCurrentSessionInfoToDisk(context: Context) {
        SessionInfoSerializer.saveSession(
            context,
            sessionInfo
        )
    }

    fun retrieveSavedSessionInfo(context: Context):List<HistoryInfoUnit> {
        val historyInfoUnits = mutableListOf<HistoryInfoUnit>()

        val previousSessions = SessionInfoSerializer.getSessions(
            context
        )

        Log.d(LOG_TAG, "size from previousSessions: ${previousSessions.size}")

        previousSessions.forEach {
            val value = it.countValue
            val date = SessionInfoSerializerHelper.getDate(it)
            Log.d(LOG_TAG, date.toString())
            historyInfoUnits.add(HistoryInfoUnit(value, date))
        }


        return historyInfoUnits.reversed()
    }

    fun doNotify(context: Context) {
        NotificationController.notify(context)
    }

    companion object {
        const val LOG_TAG = "MainActivityViewModel"
    }
}