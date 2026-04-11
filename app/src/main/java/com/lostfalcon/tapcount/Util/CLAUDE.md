# Util — App-wide Constants

Single file (`Constants.kt`) holding all global constant values used across the app.

## Constants Reference

| Constant | Value | Used For |
|----------|-------|---------|
| `PREFERENCES_FILE_KEY` | `"com.lostfalcon.tapcount.PREFERENCE_FILE_KEY"` | Legacy SharedPreferences file name |
| `SESSIONS_KEY` | `"SESSIONS_KEY"` | Legacy SharedPreferences key for session list |
| `NOTIFICATION_CHANNEL_ID_APP_STATE_BLUEPRINT` | `"AppStateBluePrint"` | Notification channel ID |
| `NOTIIFICATION_INCREMENT_CODE` | `9001` | PendingIntent request code for tap action |
| `NOTIFICATION_UNDO_CODE` | `9010` | PendingIntent request code for undo action |
| `NOTIFICATION_UNDO_ACTION` | `"com.lostfalcon.tapcount.notification.undo"` | Broadcast action string for undo |
| `NOTIFICATION_TAP_ACTION` | `"com.lostfalcon.tapcount.notification.increment"` | Broadcast action string for tap |
| `NOTIFICATION_TYPE` | `"ACTION_TYPE"` | Intent extra key to identify notification action type |

> Note: `NOTIIFICATION_INCREMENT_CODE` has a typo (double `I`) — do not fix without updating all usages.

## Guidelines

- Add new constants here rather than hardcoding strings/ints elsewhere.
- `PREFERENCES_FILE_KEY` and `SESSIONS_KEY` are only needed for the legacy migration path — do not use them for new features.
