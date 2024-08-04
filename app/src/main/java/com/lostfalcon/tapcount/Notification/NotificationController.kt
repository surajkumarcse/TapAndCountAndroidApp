package com.lostfalcon.tapcount.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.lostfalcon.tapcount.MainActivity
import com.lostfalcon.tapcount.R
import com.lostfalcon.tapcount.SessionInfo.CentralCountInfo
import com.lostfalcon.tapcount.Util.Constants
import com.lostfalcon.tapcount.Util.Constants.NOTIFICATION_CHANNEL_ID_APP_STATE_BLUEPRINT
import com.lostfalcon.tapcount.Util.Constants.NOTIFICATION_UNDO_CODE
import com.lostfalcon.tapcount.Util.Constants.NOTIIFICATION_INCREMENT_CODE


object NotificationController {

    lateinit var notificationManager : NotificationManager
    val PERSISTENT_NOTIFICATION_AFTER_APP_STARTS_CODE = 1000

    fun notify(context: Context) {
        notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID_APP_STATE_BLUEPRINT,
                "Show once you open the app",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val incrementIntent = Intent(
            context,
            MainActivity::class.java
        )
        incrementIntent.putExtra(
            Constants.NOTIFICATION_TYPE,
            Constants.NOTIFICATION_TAP_ACTION
        )
        incrementIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIncrementIntent =
            PendingIntent.getActivity(context, NOTIIFICATION_INCREMENT_CODE, incrementIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val undoIntent = Intent(
            context,
            MainActivity::class.java
        )
        undoIntent.putExtra(
            Constants.NOTIFICATION_TYPE,
            Constants.NOTIFICATION_UNDO_ACTION
        )
        undoIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        val pendingUndoIntent =
            PendingIntent.getActivity(context, NOTIFICATION_UNDO_CODE, undoIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID_APP_STATE_BLUEPRINT)
                .setSmallIcon(R.mipmap.ic_touch_tap)
                .setContentTitle(context.resources.getString(R.string.app_name))
                .setContentText(context.resources.getString(R.string.notification_text_holder) + CentralCountInfo.count.value)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true) // Makes the notification persistent
                .addAction(android.R.drawable.btn_plus, context.resources.getString(R.string.tap_button_text), pendingIncrementIntent)
                .addAction(android.R.drawable.btn_minus, context.resources.getString(R.string.undo_button_text), pendingUndoIntent)

        notificationManager?.notify(PERSISTENT_NOTIFICATION_AFTER_APP_STARTS_CODE, builder.build())
    }
}