package com.lostfalcon.tapcount.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SessionDao {
    @Insert
    suspend fun insert(sessionInfo: SessionInfo)

    @Query("SELECT * FROM session_table")
    suspend fun getAllSessions(): List<SessionInfo>
}