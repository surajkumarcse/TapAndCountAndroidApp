package com.lostfalcon.tapcount.SessionInfo

import android.content.Context
import android.content.SharedPreferences
import com.lostfalcon.tapcount.Util.Constants.PREFERENCES_FILE_KEY
import com.lostfalcon.tapcount.Util.Constants.SESSIONS_KEY

class SessionInfoSerializer {
    companion object {
        @Deprecated("try to save in DB")
        fun saveSession(context: Context, sessionInfo: SessionInfo) {
            val sharedPref: SharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
            val existingSessionsJson = sharedPref.getString(SESSIONS_KEY, "[]")
            val existingSessions = SessionInfoSerializerHelper.fromJsonList(existingSessionsJson ?: "[]").toMutableList()
            existingSessions.add(sessionInfo)
            android.util.Log.e("surakuma", "size: ${existingSessions.size} ${existingSessions.toString()}")
            with(sharedPref.edit()) {
                putString(SESSIONS_KEY, SessionInfoSerializerHelper.toJsonList(existingSessions))
                apply()
            }
        }

        @Deprecated("try to get the Sessions from DB")
        fun getSessionsFromSharedPref(context: Context): List<SessionInfo> {
            val sharedPref: SharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
            val sessionsJson = sharedPref.getString(SESSIONS_KEY, "[]")
            return SessionInfoSerializerHelper.fromJsonList(sessionsJson ?: "[]")
        }

        fun clearSharedPrefs(context: Context) {
            val sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
        }
    }
}