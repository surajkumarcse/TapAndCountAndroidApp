package com.lostfalcon.tapcount.SessionInfo

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import java.util.Date
import java.util.UUID

@Keep
@Serializable
@Deprecated("please use SessionInfo from db module")
data class SessionInfo(val sessionId: String, var dateTime: String, var countValue: Int)
