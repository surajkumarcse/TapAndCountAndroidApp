# db — Database Layer

Room database implementation for persisting tap count sessions.

## Files

| File | Purpose |
|------|---------|
| `SessionInfo.kt` | `@Entity` — the database row model |
| `SessionDao.kt` | `@Dao` — SQL query interface |
| `AppDatabase.kt` | `@Database` — singleton Room database |
| `SessionInfoHelper.kt` | Helper utilities for saving/loading sessions |

## Entity: `SessionInfo`

Table: `session_table`

| Field | Type | Notes |
|-------|------|-------|
| `id` | Int | Primary key, auto-increment |
| `sessionId` | String | UUID identifying the session |
| `dateTime` | String | Timestamp string |
| `countValue` | Int | The final count value |
| `sessionName` | String | Defaults to `"UNTITLED"` |

> Note: There is also a legacy `SessionInfo` in the `SessionInfo/` package (now `@Deprecated`). Always use `db.SessionInfo` for new code.

## DAO: `SessionDao`

```kotlin
insert(sessionInfo: SessionInfo)       // insert a session record
getAllSessions(): List<SessionInfo>    // retrieve all sessions
```

## Database: `AppDatabase`

- Singleton via `AppDatabase.getDatabase(context)`
- Version: 1, `exportSchema = false`

## Helper: `SessionInfoHelper`

- `saveSessionInDb(context, sessionInfo)` — saves using `GlobalScope.launch` (fire-and-forget)
- `getSavedSessionsFromDb(context)` — suspend function, call from `viewModelScope`
- `convertSessionInfoToDbCompatible()` — converts legacy `SessionInfo/SessionInfo` to `db.SessionInfo` during migration

## Usage Pattern

Always access the DB through `SessionInfoHelper` from the ViewModel — never call `AppDatabase` or `SessionDao` directly from UI or other layers.
