# appInfo module

Reusable Android library module providing an App Info / Settings screen.
Designed to be dropped into any app without modification.

## Files

| File | Purpose |
|------|---------|
| `SettingsUI.kt` | Composable `AppInfoScreen()` — the full settings screen UI |
| `SettingsUIUtil.kt` | Intent helpers to open Gmail, Play Store, and GitHub |

## What the Screen Shows

- App info text (from `strings.xml`)
- **Send Feedback** button → opens Gmail with pre-filled subject ("Feedback Tap & Count") to `lostfalconofficial@gmail.com`
- **Rate Application** button → opens app's Play Store listing (falls back to browser if Play Store not installed)
- **Source Code** button → opens GitHub repo in browser

## How to Use in a Host App

```kotlin
// In your NavHost
composable(Screen.AppInfoScreen.route) {
    SettingsUI.AppInfoScreen(navController = navController)
}
```

## Reusability Notes

- `SettingsUIUtil.openGooglePlayStore()` uses `context.packageName` dynamically — works for any app
- Feedback email and GitHub URL are hardcoded in `SettingsUIUtil.kt` — update these when reusing in a different project
- Strings are in `appInfo/src/main/res/values/strings.xml`
