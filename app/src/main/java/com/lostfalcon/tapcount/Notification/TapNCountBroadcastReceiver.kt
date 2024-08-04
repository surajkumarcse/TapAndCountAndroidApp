package com.lostfalcon.tapcount.Notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.lostfalcon.tapcount.SessionInfo.CentralCountInfo
import com.lostfalcon.tapcount.Util.Constants.NOTIFICATION_TAP_ACTION
import com.lostfalcon.tapcount.Util.Constants.NOTIFICATION_UNDO_ACTION

class TapNCountBroadcastReceiver: BroadcastReceiver() {
    val LOG_TAG = "TapNCountBroadcastReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action) {
            NOTIFICATION_UNDO_ACTION -> {
                CentralCountInfo.count.value--
            }

            NOTIFICATION_TAP_ACTION -> {
                CentralCountInfo.count.value++
            }

            else -> {
                Log.e(LOG_TAG, "unknown broadcast receiver received")
            }
        }
    }
}