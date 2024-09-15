package com.lostfalcon.tapcount.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_table")
data class SessionInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: String,
    var dateTime: String,
    var countValue: Int,
    var sessionName: String = "UNTITLED"
)

