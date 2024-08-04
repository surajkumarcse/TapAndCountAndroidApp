package com.lostfalcon.tapcount.Util

object Constants {
    val PREFERENCES_FILE_KEY = "com.lostfalcon.tapcount.PREFERENCE_FILE_KEY"
    val SESSIONS_KEY = "SESSIONS_KEY"

    // once the app opens it will show in the notification tray
    val NOTIFICATION_CHANNEL_ID_APP_STATE_BLUEPRINT = "AppStateBluePrint"
    val NOTIIFICATION_INCREMENT_CODE = 9001
    val NOTIFICATION_UNDO_CODE = 9010
    val NOTIFICATION_UNDO_ACTION = "com.lostfalcon.tapcount.notification.undo"
    val NOTIFICATION_TAP_ACTION = "com.lostfalcon.tapcount.notification.increment"
    val NOTIFICATION_TYPE = "ACTION_TYPE"
}