package com.lostfalcon.tapcount.SessionInfo

import android.content.Context
import android.content.SharedPreferences
import com.lostfalcon.tapcount.Util.Constants.PREFERENCES_FILE_KEY
import com.lostfalcon.tapcount.Util.Constants.SESSIONS_KEY

class SessionInfoSerializer {
    companion object {
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

        fun getSessions(context: Context): List<SessionInfo> {
            val sharedPref: SharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
            val sessionsJson = sharedPref.getString(SESSIONS_KEY, "[]")
            return SessionInfoSerializerHelper.fromJsonList(sessionsJson ?: "[]")
        }
    }
}