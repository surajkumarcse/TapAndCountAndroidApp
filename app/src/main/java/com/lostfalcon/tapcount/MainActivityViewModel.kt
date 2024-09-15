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
import androidx.lifecycle.viewModelScope
import com.lostfalcon.tapcount.Notification.NotificationController
import com.lostfalcon.tapcount.SessionInfo.CentralCountInfo
import com.lostfalcon.tapcount.SessionInfo.HistoryInfoUnit
import com.lostfalcon.tapcount.SessionInfo.SessionInfoSerializer
import com.lostfalcon.tapcount.SessionInfo.SessionInfoSerializer.Companion.clearSharedPrefs
import com.lostfalcon.tapcount.SessionInfo.SessionInfoSerializerHelper
import com.lostfalcon.tapcount.db.AppDatabase
import com.lostfalcon.tapcount.db.SessionInfo
import com.lostfalcon.tapcount.db.SessionInfoHelper
import com.lostfalcon.tapcount.db.SessionInfoHelper.Companion.convertSessionInfoToDbCompatible
import com.lostfalcon.tapcount.db.SessionInfoHelper.Companion.getSavedSessionsFromDb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val _previousSessions = MutableStateFlow<List<HistoryInfoUnit>>(emptyList())
    val previousSessions: StateFlow<List<HistoryInfoUnit>> = _previousSessions.asStateFlow()


    var sessionInfo: SessionInfo = SessionInfo(
        sessionId = sessionId.toString(),
        dateTime = Date().toString(),
        countValue = 0
    )

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

    fun onHistoryClicked(context: Context) {
        fetchSavedSessionInfo(context)
    }

    fun saveCurrentSessionInfoToDisk(context: Context) {
        SessionInfoHelper.saveSessionInDb(
            context,
            sessionInfo
        )
    }

    fun fetchSavedSessionInfo(context: Context) {
        viewModelScope.launch {
            val historyInfoUnits = mutableListOf<HistoryInfoUnit>()

            val previousSessions = getSavedSessionsFromDb(
                context
            )

            Log.d(LOG_TAG, "size from previousSessions: ${previousSessions.size}")

            previousSessions.forEach {
                val value = it.countValue
                val date = SessionInfoSerializerHelper.getDate(it.dateTime)
                Log.d(LOG_TAG, date.toString())
                historyInfoUnits.add(HistoryInfoUnit(value, date))
            }

            _previousSessions.value = historyInfoUnits.reversed()
        }
    }

    fun doNotify(context: Context) {
        NotificationController.notify(context)
    }

    /**
     * While creating this app, I tried to put all the session history to the shared pref.
     * When user clicked history, then these session history will get retrieved and shown to the user.
     * Moving forward, it better to put it in DB
     *
     * This function will try to migrate sessionInfos from SharedPref to Db
     * we are not maintaining flag to wait for entire migration happen and then only we will insert new one.
     * reason: As this is very small app and only 10 downloads till this date. very less user will get impacted.
     */
    fun migrateDataFromSharedPrefToDb(context: Context) {
        val db = AppDatabase.getDatabase(context)
        val sessionDao = db.sessionDao()

        val sessionInfos = SessionInfoSerializer.getSessionsFromSharedPref(
            context
        )

        if(sessionInfos.isEmpty()) {
            Log.i(LOG_TAG, "SharedPref session list is empty")
            return
        }

        val dbSessionInfos = convertSessionInfoToDbCompatible(sessionInfos)

        viewModelScope.launch {
            try {
                Log.i(LOG_TAG, "inserting dbSessionInfo...")
                // push it to the db
                for(dbSessionInfo in dbSessionInfos) {
                    Log.i(LOG_TAG, "inserting dbSessionInfo...${dbSessionInfo.sessionId} ${dbSessionInfo.countValue}")
                    sessionDao.insert(dbSessionInfo)
                }
                clearSharedPrefs(context)
            } catch (exception: Exception) {
                exception.printStackTrace()
                Log.e(LOG_TAG, "exception : ${exception.message}")
            }
        }
    }

    companion object {
        const val LOG_TAG = "MainActivityViewModel"
    }
}