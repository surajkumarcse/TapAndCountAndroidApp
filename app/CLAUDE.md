# app module

Main application module for TapAndCount.

## Key Files

| File | Purpose |
|------|---------|
| `MainActivity.kt` | Entry point; hosts all Compose UI, handles volume key events, notification intents |
| `MainActivityViewModel.kt` | MVVM ViewModel; manages count state, vibration, tone, DB operations |
| `Screen.kt` | Sealed class defining NavHost routes (`main_screen`, `app_info_screen`) |

## Permissions (AndroidManifest.xml)

- `POST_NOTIFICATIONS` — for persistent count notification
- `VIBRATE` — for haptic feedback on each tap

## Data Flow

```
User tap / volume key / notification button
        ↓
MainActivityViewModel.incrementCount() / decrementCount()
        ↓
CentralCountInfo.count (global mutable state)
        ↓
NotificationController.notify()   +   Vibrator   +   ToneGenerator
```

## ViewModel Responsibilities (`MainActivityViewModel`)

- `incrementCount(context)` — increment, vibrate, play tone, update notification
- `decrementCount(context)` — decrement if count > 0, vibrate error tone
- `onResetClicked()` — save session to Room DB, reset count to 0
- `onHistoryClicked()` — query DB, convert to `HistoryInfoUnit` list, emit via StateFlow
- `migrateDataFromSharedPrefToDb()` — one-time migration from legacy SharedPreferences to Room DB; called in `onStart()`
- `onPause()` — releases ToneGenerator to avoid resource leaks

## Navigation

Uses Jetpack Navigation Compose. Routes defined in `Screen.kt`:
- `MainScreen` → `TapAndCountHomeScreen`
- `AppInfoScreen` → delegates to `appInfo` module's `SettingsUI.AppInfoScreen()`

## Volume Key Support

`dispatchKeyEvent()` in `MainActivity` intercepts:
- `VOLUME_UP` → `incrementCount()`
- `VOLUME_DOWN` → `decrementCount()`
