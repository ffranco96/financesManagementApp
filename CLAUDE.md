# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

Single-module Android app (`:app`) for personal finance tracking: record income/expenses,
categorize them, import/export CSV, view balance and category charts, and poll crypto prices.
Jetpack Compose UI, Hilt DI, Room persistence, DataStore for config. Kotlin 2.0, `minSdk` 26,
`compileSdk`/`targetSdk` 35, JVM target 11.

UI copy, log messages, and git commit messages are in Spanish; code identifiers are English.
The default (and only) account is `accountId = 0` (`Record.DEFAULT_ACCOUNT_ID`).

## Commands

Use the Gradle wrapper (`gradlew.bat` on this Windows machine). The Gradle daemon is disabled
(`org.gradle.daemon=false`), so builds spawn a fresh JVM each time.

| Task | Command |
| --- | --- |
| Build debug APK | `./gradlew assembleDebug` |
| All JVM unit tests | `./gradlew testDebugUnitTest` |
| Single test class | `./gradlew testDebugUnitTest --tests "com.example.financesmanagementapp.data.local.ParseCsvUseCaseTest"` |
| Single test method | `./gradlew testDebugUnitTest --tests "*.ParseCsvUseCaseTest.parses valid line"` |
| Instrumented tests (needs device/emulator) | `./gradlew connectedDebugAndroidTest` |
| Android Lint | `./gradlew lintDebug` |
| Build release APK | `./gradlew assembleRelease` |

`NullSafeMutableLiveData` is disabled in `app/build.gradle.kts` (`android { lint { ... } }`):
AGP/lint's `NonNullableMutableLiveDataDetector` hits an `IncompatibleClassChangeError` against the
Kotlin Analysis API on this toolchain (unrelated to project code) and otherwise crashes
`lintVitalAnalyzeRelease`, which runs automatically before any release build/bundle and would
block it.

There is no ktlint/detekt/spotless config — no separate format step.

Unit tests use JUnit4 + MockK + `kotlinx-coroutines-test`. Tests that touch Android framework
classes run under Robolectric (`@RunWith(RobolectricTestRunner::class)`) and live in
`src/test/`. `src/androidTest/` currently holds one instrumented copy of the CSV-writer test.

## Architecture

Layering is `ui/` (Compose screen + ViewModel) → `domain/` use cases → `data/` (Room + DataStore + network).
Use cases are single-responsibility classes with `operator fun invoke(...)`, `@Inject`-constructed.

### DI (Hilt)

- App-wide modules in `di/`: `DatabaseModule` (Room `AppDatabase` + migrations), `RepositoryModule`
  and `ConfigModule` (`@Binds` impls; `ConfigModule` also provides a singleton `Gson`), `CsvModule`.
- Feature-scoped module `ui/home/di/NetworkModule` for Retrofit/Binance.
- `ui/home/di/ServiceLocator` is a deliberate escape hatch: `UpdateCryptoactivesWorker` is a plain
  `CoroutineWorker` (not `@HiltWorker`), so `HomeViewModel.init` stashes `GetCryptoPriceByTickerUseCase`
  into `ServiceLocator` for the worker to pick up. The worker writes the BTC price into
  `SharedPreferences` (`btc_price_pref`).
- `AppDatabase.getInstance(context)` (manual singleton in the companion) is legacy; Hilt injection
  via `DatabaseModule` is the real path. Keep migrations registered in **both** places if you touch them.

### Navigation

`navigation/AppNavigation.kt` is the single `NavHost`; routes are the `AppScreens` sealed hierarchy.
Start destination is chosen at runtime from `SharedPreferences("financesMgmtAppPrefs").isLoggedIn`
(login is a stub — no real auth). ViewModels are obtained with `hiltViewModel()` per composable.

### ViewModels

MVVM with `StateFlow` for state (cold repo `Flow` → `stateIn(WhileSubscribed(5000))`) and
`MutableSharedFlow` for one-shot UI events (e.g. `HomeUiEvent.ShowToast`). Note the file
`ui/home/ui/HomeStartViewModel.kt` declares class `HomeViewModel` (marked `open` so tests can
subclass and override the flows); the screen is `HomeStartScreen`.

## Domain invariants — read before touching records or categories

- **`amount` is signed.** Negative = expense, positive = income. Balances are plain `SUM(amount)`.
  There is no `isIncome` column (dropped in `MIGRATION_3_4`, which folded the old sign into `amount`).
  `Category.isIncome` still exists as taxonomy metadata but the record's sign is the source of truth.

- **Category identity is the enum `name`, not the label.** `CategoryName` / `SubcategoryName`
  entries have a stable key (`CATEGORY_VEHICLE`, `SUBCATEGORY_FUEL`) used for DB columns and CSV;
  `.label` is the Spanish UI text and can change freely. `RecordEntity.category` / `.subcategory`
  store `enum.toString()`. Parse persisted values only via `CategoryName.fromRaw` /
  `SubcategoryName.fromRaw` (they fall back to `WITHOUT_CATEGORY` / `SUBCATEGORY_NONE` for stale keys).

- **Icons and colors are never persisted.** `RecordEntity` carries only the two enum keys.
  The full `Category` (with `iconRsc`, `colorCategory`, `displayName`) is resolved at read time by
  `Category.fromCategoryAndSubcategory(...)` against `Category.ALL_CATEGORIES`. This is what keeps
  stored records rendering with current build resources — always go through it when mapping
  entity → domain (see `GetAllRecordsFlowUseCase`, `GetCategoryTotalUseCase`).

- **Changing the taxonomy requires bumping `Category.CATEGORIES_VERSION`.** Categories are seeded
  into DataStore by `InitializeConfigUseCase` (run from `FinancesManagementApp.onCreate`). It
  reseeds only when the stored list is empty **or** the stored version is lower than
  `CATEGORIES_VERSION`. Add/rename/remove a `Category` in `ALL_CATEGORIES` → bump the constant, or
  existing installs keep the old list. `iconRsc` / `colorCategory` reference `R.drawable.ic_*` /
  `R.color.categ_color_*` — verify those resources exist (list them fresh and compile; resources
  are edited concurrently).

- **Dates are strings.** ISO-8601 `yyyy-MM-dd'T'HH:mm:ss` for new records, bare `yyyy-MM-dd` for
  legacy. `Record` / `RecordEntity` `compareTo` and every date filter try `LocalDateTime` first,
  then `LocalDate`, then string compare. Preserve that fallback chain.

### Room migrations

`AppDatabase` is at version 4 with `MIGRATION_1_2`, `MIGRATION_2_3`, `MIGRATION_3_4`. No
`fallbackToDestructiveMigration`. Any schema change to `RecordEntity` needs a new numbered
migration added to both `DatabaseModule.provideAppDatabase` and `AppDatabase.getInstance`.

### CSV

Semicolon-delimited (`;`), not comma. Two independent paths:
- **Export**: `ExportCsvUseCase` builds the string → `CsvFileWriter` (impl `RecordsCsvFileWriter`
  writes to `context.filesDir`, throws `FileAlreadyExistsException` on collision).
  Header: `amount; description; category; subcategory; date; currency`.
- **Import**: `ReadCsvUseCase` (URI → `InputStream`) → `ParseCsvUseCase` (header-driven column
  lookup, returns `CsvParseResult(records, errors)` with typed `ParseError`s; unknown category
  keys degrade to `WITHOUT_CATEGORY` rather than failing the row).

Header/value escaping for `;` inside fields is a known gap — do not assume fields are quoted.

## Firebase / Crashlytics

- Crash reporting goes through the `CrashReporter` interface (`domain/crash/`), implemented by
  `FirebaseCrashReporter` (`data/crash/`) and bound in `di/CrashModule`. Inject `CrashReporter`
  rather than calling `FirebaseCrashlytics.getInstance()` directly; use `recordException(e, msg)`
  in `catch` blocks that would otherwise swallow the error (already wired in `ConfigRepositoryImpl`,
  `ExportCsvUseCase`, `ReadCsvUseCase`). Tests pass `mockk(relaxed = true)`.
- Crashlytics + Analytics auto-initialize via their manifest-merged ContentProviders — no code in
  `Application.onCreate`. Both `firebase_crashlytics_collection_enabled` and
  `firebase_analytics_collection_enabled` manifest meta-data are driven by the same
  `manifestPlaceholders["crashlyticsCollectionEnabled"]` (default `true` in `defaultConfig`,
  overridden to `false` in `debug`): **all Firebase telemetry off on `debug`, on for `release`**.
  `mappingFileUploadEnabled` is off for `debug`.
- `app/google-services.json` is the real config for Firebase project `financesapp-967a1`, app
  package `com.example.financesmanagementapp`. It's committed — the Android API key in it is
  restricted by package + signing cert, not a secret. Re-download from the Firebase console
  (console.firebase.google.com) after adding build variants, SHA-1 certs, or new Firebase
  products. A `release` build uploads the Crashlytics mapping file and needs network.
- Versions live in `gradle/libs.versions.toml` (`firebaseBom`, `googleServices`,
  `firebaseCrashlyticsPlugin`); Firebase artifacts are unversioned (BoM-managed).

## Git / workflow

Work happens on `develop`; PRs target `master`. This checkout is a git worktree under
`.claude/worktrees/` — run everything from here, don't `cd` to the main checkout. The stash stack
is shared across worktrees (see the environment note; prefer a WIP commit over bare `git stash`).
