# TapAndCount Android App

## Project Overview

TapAndCount is an Android app that lets users count things by tapping the screen — designed for use cases like:
- Counting mantras/matras chanted during prayer
- Counting workout reps
- Counting people entering a venue (e.g. a mall)

It is built to replace traditional physical tally counters with a simple, intuitive mobile experience.

## Architecture

- **Pattern**: MVVM (Model-View-ViewModel)
- **UI**: Jetpack Compose with Material3
- **Database**: Room (for persistent count storage)
- **Async**: Kotlin Coroutines
- **Navigation**: Jetpack Navigation Compose

## Modules

- `app` — main application module (UI, ViewModel, DB, Notifications)
- `appInfo` — SettingsUI that can be used in other app as well (Sending feedback, open the source code, open the play store for rating)

## Key Source Directories (`app/src/main/java/com/lostfalcon/tapcount/`)

| Directory | Purpose |
|-----------|---------|
| `db/` | Room database, DAOs, entities |
| `SessionInfo/` | Session tracking logic |
| `Notification/` | Notification handling |
| `ui/theme/` | Compose theme and styling |
| `Util/` | Utility/helper classes |

## Build Configuration

- **Language**: Kotlin 2.0.0
- **compileSdk / targetSdk**: 34
- **minSdk**: 24
- **JVM target**: 11
- **Build system**: Gradle with Kotlin DSL (`build.gradle.kts`)
- **Version catalog**: `gradle/libs.versions.toml`

## Common Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Install debug build on connected device
./gradlew installDebug

# Clean build
./gradlew clean
```

## Testing

- **Unit tests**: `app/src/test/` — JUnit 4/5, Mockito, Robolectric
- **Instrumented tests**: `app/src/androidTest/` — Espresso, Compose UI tests
- Use Robolectric for Android framework mocking in unit tests
- Use Mockito (`mockito-kotlin`) for dependency mocking

## Code Conventions

- Follow MVVM strictly: ViewModels hold state and business logic, Composables are stateless where possible
- Use `StateFlow` / `collectAsState()` for UI state observation
- Room queries should go through the DAO — never access the DB directly from UI or ViewModel
- Prefer Kotlin idioms (extension functions, data classes, sealed classes for state)