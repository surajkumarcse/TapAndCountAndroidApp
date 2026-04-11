# ui/theme — Compose Theme

Material3 theming for TapAndCount: colors, typography, and the root theme composable.

## Files

| File | Purpose |
|------|---------|
| `Color.kt` | Color constant definitions for dark and light themes |
| `Theme.kt` | `TapCountTheme()` — root theme composable applied in `MainActivity` |
| `Type.kt` | Typography scale (currently only `bodyLarge` customized) |

## Color Palette

| Token | Usage |
|-------|-------|
| `Grey80 / Grey40` | Primary color (dark / light) |
| `GreyGrey80 / GreyGrey40` | Secondary color |
| `LightGrey80 / LightGrey40` | Tertiary color — used for text on primary backgrounds |

## TapCountTheme

- Selects dark/light `ColorScheme` based on `isSystemInDarkTheme()`
- Supports **dynamic color** on Android 12+ (`dynamicDarkColorScheme` / `dynamicLightColorScheme`)
- Wraps `MaterialTheme` with the chosen scheme and `Typography`

## Usage

```kotlin
// Applied once at the top level in MainActivity.onCreate()
TapCountTheme {
    MyApp()
}
```

Do not apply `TapCountTheme` inside individual composables — it is a root-level wrapper only.
