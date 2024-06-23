package com.lostfalcon.tapcount.SessionInfo

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SessionInfoSerializerHelper {
    companion object {
        private val DEFAULT_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy"
        private val json = Json { encodeDefaults = true }

        fun toJson(session: SessionInfo): String = json.encodeToString(session)

        fun fromJson(jsonString: String): SessionInfo = json.decodeFromString(jsonString)

        fun toJsonList(sessions: List<SessionInfo>): String = json.encodeToString(sessions)

        fun fromJsonList(jsonString: String): List<SessionInfo> = json.decodeFromString<List<SessionInfo>>(jsonString)

        // it might throw exception, handle it
        fun getDate(sessionInfo: SessionInfo) : String {
            val dateTime = sessionInfo.dateTime

            // Define the input date format
            val inputDateFormat = SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.US)

            // Parse the date string to a Date object
            val date: Date = inputDateFormat.parse(dateTime)

            // Define the desired output date format
            val outputDateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.US)

            // Format the Date object to a human-readable string
            return outputDateFormat.format(date)
        }
    }
}