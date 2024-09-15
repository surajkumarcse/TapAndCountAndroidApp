package com.lostfalcon.tapcount.db

import android.content.Context
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SessionInfoHelper {
    companion object {
        fun convertSessionInfoToDbCompatible(sessionInfos: List<com.lostfalcon.tapcount.SessionInfo.SessionInfo>): List<SessionInfo> {
            val dbSessionInfos = mutableListOf<SessionInfo>()

            for (sessionInfo in sessionInfos) {
                dbSessionInfos.add(
                    SessionInfo(
                        sessionId = sessionInfo.sessionId,
                        countValue = sessionInfo.countValue,
                        dateTime = sessionInfo.dateTime
                    )
                )
            }

            return dbSessionInfos
        }

        fun saveSessionInDb(context: Context, sessionInfo: SessionInfo) {
            val db = AppDatabase.getDatabase(context)
            val sessionDao = db.sessionDao()

            GlobalScope.launch {
                sessionDao.insert(sessionInfo)
            }
        }

        suspend fun getSavedSessionsFromDb(context: Context): List<SessionInfo> {
            val db = AppDatabase.getDatabase(context)
            val sessionDao = db.sessionDao()

            val sessions = sessionDao.getAllSessions()

            return sessions
        }
    }
}