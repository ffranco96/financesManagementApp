# financesManagementApp

Android (Kotlin/Compose/Material3) personal finance manager.

## Build & run

```
./gradlew assembleDebug
./gradlew test
./gradlew lint
```

APK output: `financesManagementApp_v{version}_{buildType}.apk`

Versions in `gradle.properties`: `APP_VERSION_NAME`, `APP_VERSION_CODE`.

## Architecture

- **Entry**: `FinancesManagementApp` (HiltApplication) → `MainActivity` (single Activity) → `AppNavigation` (NavHost, 4 screens)
- **DI**: Dagger Hilt (kapt). Modules: `DatabaseModule`, `ConfigModule`, `RepositoryModule`, `NetworkModule`
- **DB**: Room v2 (`finances_app_database`), entity `RecordEntity` (table `records`), migration 1→2 adds `accountId`
- **Config**: DataStore Preferences, serialized via Gson
- **Network**: Retrofit + Gson → Binance API (`data-api.binance.vision/api/v3/`)
- **Background**: WorkManager polls BTC price every 1 minute
- **Nav**: `AppScreens` sealed class. Start destination depends on `isLoggedIn` in SharedPreferences `financesMgmtAppPrefs`.
- **Theme**: Material3 with dynamic color on Android 12+

## Key quirks

- **WorkManager + Hilt gap**: Workers use a static `ServiceLocator` object (in `ui/home/di/`) because Hilt injection into Workers is not wired up
- **CSV delimiter**: semicolon `;`, NOT comma. Header: `amount; description; categoryName; date; currency`
- **Category mapping**: DB stores `categoryName` as String; runtime maps to full `Category` object via `Category.fromName()`
- **Login**: mock — writes `isLoggedIn = true` to SharedPreferences, no real auth
- **`hilt.enableAggregatingTask = false`** in app build.gradle
- **`HomeViewModel`** is `open` (not typical)
- **Package split**: `graphs` domain use case exists (`GetBalanceByAccountAndCategoryUseCase`) but no UI screen for it yet
- **Min SDK**: 26, Java 11 target
- **Tests**: only example stub tests exist (`ExampleUnitTest`, `ExampleInstrumentedTest`)
