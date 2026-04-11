# Notification — Persistent Notification System

Manages an ongoing notification that lets users increment/undo their count without opening the app.

## Files

| File | Purpose |
|------|---------|
| `NotificationController.kt` | Builds and posts the persistent notification |
| `TapNCountBroadcastReceiver.kt` | Handles button presses from the notification |

## How It Works

1. `NotificationController.notify(context)` — creates/updates the notification with the current count
2. The notification has two action buttons, each backed by a `PendingIntent` broadcast:
   - **Tap** (code `9001`) → broadcasts `NOTIFICATION_TAP_ACTION` → increments count
   - **Undo** (code `9010`) → broadcasts `NOTIFICATION_UNDO_ACTION` → decrements count
3. `TapNCountBroadcastReceiver.onReceive()` catches these broadcasts and mutates `CentralCountInfo.count`

## Notification Properties

- Channel ID: `AppStateBluePrint` (defined in `Util/Constants.kt`)
- `setOngoing(true)` — notification is persistent, cannot be swiped away
- Notification text shows the current count value

## Constants (from `Util/Constants.kt`)

```kotlin
NOTIFICATION_CHANNEL_ID_APP_STATE_BLUEPRINT = "AppStateBluePrint"
NOTIFICATION_INCREMENT_CODE = 9001
NOTIFICATION_UNDO_CODE = 9010
NOTIFICATION_UNDO_ACTION = "com.lostfalcon.tapcount.notification.undo"
NOTIFICATION_TAP_ACTION  = "com.lostfalcon.tapcount.notification.increment"
```

## When to Call `notify()`

`NotificationController.notify()` should be called after every count change (increment or decrement) so the notification stays in sync with `CentralCountInfo.count`. This is handled by `MainActivityViewModel`.
