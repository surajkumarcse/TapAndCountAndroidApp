# SessionInfo — Session Models & Serialization

Contains session data models, global count state, and legacy SharedPreferences serialization.

## Files

| File | Purpose |
|------|---------|
| `CentralCountInfo.kt` | Global singleton holding the live tap count |
| `SessionInfo.kt` | Legacy `@Serializable` session model (deprecated) |
| `HistoryInfoUnit.kt` | Lightweight UI model for displaying history |
| `SessionInfoSerializer.kt` | Legacy SharedPreferences read/write (deprecated) |
| `SessionInfoSerializerHelper.kt` | JSON serialization utilities + date formatting |

## CentralCountInfo (Important)

```kotlin
object CentralCountInfo {
    var count = mutableStateOf(0)
}
```

This is the **single source of truth** for the current tap count. Both the ViewModel and the Notification system read/write this directly. Do not introduce a second count state elsewhere.

## HistoryInfoUnit

```kotlin
data class HistoryInfoUnit(val value: Int, val date: String)
```

Used only in the UI layer (history dialog). Converted from `db.SessionInfo` inside the ViewModel.

## Deprecated Classes

`SessionInfo.kt` and `SessionInfoSerializer.kt` are `@Deprecated`. They exist solely to support the one-time migration from SharedPreferences to Room DB (`migrateDataFromSharedPrefToDb()` in `MainActivityViewModel`). Do not use them for new features.

## Date Formatting

`SessionInfoSerializerHelper.getDate()` converts raw date strings:
- Input format: `"EEE MMM dd HH:mm:ss zzz yyyy"`
- Output format: `"MMMM d, yyyy"` (e.g. "April 11, 2026")
