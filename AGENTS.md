# Agent Guidelines: The Block

This document establishes the architectural standards, directory structure, coding conventions, and workflow rules for AI agents and developers working on **The Block** vehicle auction platform.

---

## 1. Project Overview & Tech Stack

**The Block** is a native Android buyer-side vehicle auction application built as part of the OPENLANE challenge. Buyers can browse and search inventory, inspect vehicle condition and details, and place live bids.

* **Language**: Kotlin 2.x
* **UI Toolkit**: Jetpack Compose with Material 3
* **Architecture**: Clean Architecture (Layered + Feature-driven)
* **Dependency Injection**: Dagger Hilt
* **Asynchronous / Reactive**: Kotlin Coroutines & `StateFlow` / `Flow`
* **Image Loading**: Coil 3 (`coil-compose`)
* **Target SDK**: 37 | **Min SDK**: 28 | **Java Source**: Version 25

---

## 2. Directory & Package Blueprint

All application source code resides under `app/src/main/java/com/anenha/block/`. The architecture strictly separates concerns across `domain`, `data`, `features`, `ui`, and `di`:

```
app/src/main/java/com/anenha/block/
├── domain/                      # DOMAIN LAYER (Pure Kotlin, zero Android UI dependencies)
│   ├── model/                   # Business models (Vehicle, VehicleLocation, AuctionDetails, Bid)
│   ├── repository/              # Repository contracts/interfaces (VehicleRepository)
│   └── usecase/                 # Single-responsibility use cases (GetVehiclesUseCase, SearchVehiclesUseCase, GetVehicleByIdUseCase, PlaceBidUseCase)
│
├── data/                        # DATA LAYER (Framework & external integrations)
│   ├── datasource/              # Local JSON asset parser (VehicleAssetDataSource)
│   ├── dto/                     # Data Transfer Objects matching data/vehicles.json (VehicleDto)
│   ├── mapper/                  # Mappers converting DTOs <-> Domain models (VehicleMapper)
│   └── repository/              # In-memory cached repository implementation (VehicleRepositoryImpl)
│
├── features/                    # FEATURE PACKAGES (Vertical feature slices)
│   ├── home/                    # Auction feed, real-time search, live bidding integration
│   │   ├── HomeScreen.kt
│   │   ├── HomeViewModel.kt
│   │   ├── HomeUiState.kt
│   │   └── HomeUiEvent.kt
│   └── detail/                  # Vehicle details, specifications, condition notes, gallery, live bidding
│       ├── VehicleDetailScreen.kt
│       ├── VehicleDetailViewModel.kt
│       └── VehicleDetailUiState.kt
│
├── ui/                          # SHARED PRESENTATION FOUNDATION
│   ├── theme/                   # Material 3 ColorScheme, Typography, Shapes, Spacing
│   ├── components/              # Reusable design atoms (VehicleCard, VehicleBadge, BidBottomSheet)
│   ├── navigation/              # Destinations, NavHost setup (BlockDestinations, BlockNavHost)
│   └── util/                    # Presentation formatters (FormatUtils)
│
└── di/                          # DEPENDENCY INJECTION (Dagger Hilt)
    ├── IoDispatcher.kt          # @IoDispatcher qualifier
    ├── DataModule.kt            # Coroutine dispatchers, JSON parser, data source bindings
    └── RepositoryModule.kt      # Hilt @Binds VehicleRepositoryImpl -> VehicleRepository
```

---

## 3. Architectural Rules & Dependency Boundaries

### The Clean Architecture Dependency Rule
Dependencies must **only point inward**:
1. **`domain` is independent**:
   * Must contain **pure Kotlin** code.
   * **Never** import Android framework classes (e.g., `android.content.*`, `androidx.compose.*`, `android.os.*`, `ViewModel`).
   * Has **no dependency** on `data`, `features`, or `ui`.
2. **`data` depends only on `domain`**:
   * Implements domain repository interfaces (`domain/repository/*`).
   * Handles raw serialization, JSON parsing, disk storage, and in-memory caches.
   * DTOs must never leak into `domain` or `features`.
3. **`features` depend on `domain` and `ui`**:
   * Feature ViewModels inject domain UseCases (or Repositories where simple pass-through is appropriate, though UseCases are preferred).
   * Feature screens compose reusable widgets from `ui/components/` and design tokens from `ui/theme/`.
4. **`ui` is the presentation foundation**:
   * Contains app-wide theme, design tokens, shared widgets, utility formatters, and top-level navigation.
   * Keeps reusable UI elements generic and decoupled from specific feature business logic.

---

## 4. Layer Responsibilities & Implementation Standards

### 4.1 Domain Layer (`app/domain`)
* **Models (`domain/model/`)**: Immutable Kotlin data classes representing real business concepts (`Vehicle`, `VehicleLocation`, `AuctionDetails`, `Bid`).
* **Repository Interfaces (`domain/repository/`)**: Define high-level data contracts returning Kotlin `Flow<T>` or direct types.
* **Use Cases (`domain/usecase/`)**:
  * Name format: `<Verb><Noun>UseCase` (e.g., `GetVehiclesUseCase`, `GetVehicleByIdUseCase`, `SearchVehiclesUseCase`, `PlaceBidUseCase`).
  * Single responsibility: contains business rules, validation, sorting, or calculation logic.
  * Overload `operator fun invoke(...)`:
    ```kotlin
    class PlaceBidUseCase @Inject constructor(
        private val repository: VehicleRepository
    ) {
        suspend operator fun invoke(vehicleId: String, amount: Double): Result<Vehicle> {
            return repository.placeBid(vehicleId, amount)
        }
    }
    ```

### 4.2 Data Layer (`app/data`)
* **DTOs (`data/dto/`)**: Mirror the raw schema from `data/vehicles.json`.
* **Mappers (`data/mapper/`)**: Extension functions converting DTOs to clean domain entities:
  ```kotlin
  fun VehicleDto.toDomain(): Vehicle = Vehicle(...)
  ```
* **Data Sources (`data/datasource/`)**:
  * Local asset parser for `vehicles.json` (`VehicleAssetDataSource`).
* **Repository Implementations (`data/repository/`)**:
  * Mediate between data sources and expose clean domain models to domain repository interfaces.
  * Maintain thread-safe in-memory caching via `Mutex` and `MutableStateFlow<Map<String, Vehicle>?>`.

### 4.3 Feature Layer (`app/features`)
* Each feature slice contains its screen, ViewModel, UI state, and (optionally) UI events:
  * `features/home`: Top-level vehicle auction feed, instant search filtering, and live bid triggers.
  * `features/detail`: Detailed vehicle telemetry, full specs, certified inspection, damage history, and sticky bidding bar.
* **ViewModel**:
  * Annotated with `@HiltViewModel`.
  * Exposes state via `val uiState: StateFlow<FeatureUiState>`.
  * Accepts intents via a public `onEvent(event: FeatureUiEvent)` method or explicit intent functions.
* **Composables**:
  * Screens should be split into a stateful route (collects `uiState`, passes lambdas) and a stateless screen (accepts data and callbacks only, facilitating `@Preview` testing).

### 4.4 Shared UI Layer (`app/ui`)
* **Theme**: Define clear color palettes, typography, spacing, and dark/light mode tokens in `ui/theme/`.
* **Components**: Shared, reusable UI widgets (`VehicleCard`, `VehicleBadge`, `BidBottomSheet`) live under `ui/components/`.
* **Navigation**: Centralized routes and NavHost configuration live under `ui/navigation/`.
* **Utilities**: Currency, number, and auction date/schedule formatters live under `ui/util/`.

---

## 5. State Management & Unidirectional Data Flow (UDF)

* Follow strict **Unidirectional Data Flow**:
  1. The **ViewModel** emits immutable `UiState` via `StateFlow`.
  2. The **UI** renders state and forwards user actions as `UiEvent`s or callbacks to the ViewModel.
  3. The **ViewModel** executes UseCases and updates `UiState`.
* **UI State Guidelines**:
  * Represent states as sealed interfaces or data classes with loading, success, and error semantics.
  * Always provide fallback / empty states for searches with zero results.

---

## 6. Coding & Styling Conventions

* **Immutability**: Prefer `val` over `var`, and immutable collections (`List`, `Set`) over mutable collections.
* **Coroutines**: Use `viewModelScope` in ViewModels. Never use `GlobalScope`.
* **String Resources**: Strings must never be hardcoded in UI composables or feature code. All strings must be extracted to `res/values/strings.xml`. The only exception is preview composables (`@Preview`), where mock strings are allowed.
* **Theme & Design Tokens**: Strictly consume existing tokens (colors, typography, shapes, spacing) defined in `ui/theme/` via `MaterialTheme`. Do NOT add new colors, shapes, or theme items—always reuse the existing design tokens.
* **Previews**: Every screen and component composable should have a corresponding `@Preview` showing standard and edge-case states (empty, error, loading).
* **Documentation (KDoc)**: Whenever modifying or refactoring a function that has KDoc, always update the documentation to accurately reflect signature, parameter, return value, behavior, or exception changes.
* **Dependency Injection**: Use Hilt constructor injection (`@Inject constructor`) for UseCases, Repositories, and DataSources.

---

## 7. Testing Strategy

* **Domain Tests (`app/src/test/.../domain/usecase`)**:
  * Unit test all UseCases with JUnit and Turbine:
    * `GetVehiclesUseCaseTest`: Emits complete vehicle list and handles empty feeds.
    * `GetVehicleByIdUseCaseTest`: Emits matching vehicle or null when ID is absent.
    * `SearchVehiclesUseCaseTest`: Validates filtered vehicle emissions and zero-match cases.
    * `PlaceBidUseCaseTest`: Validates repository delegation and Result handling.
* **Data Layer Tests (`app/src/test/.../data`)**:
  * `VehicleRepositoryImplTest`: Validates read-once caching, Mutex thread-safety, bid validation (`amount > currentBid`), and reactive Flow emission.
  * `VehicleMapperTest`: Validates DTO-to-Domain mapping accuracy, null handling, and domain computed properties (`title`, `isReserveMet`, etc.).
  * `VehicleJsonParsingTest`: Validates deserialization accuracy against raw `vehicles.json` schema.
* **ViewModel Tests (`app/src/test/.../features`)**:
  * `HomeViewModelTest`: Verifies feed loading, search filtering, and state transitions.
  * `VehicleDetailViewModelTest`: Verifies ID retrieval, vehicle loading, and bid updates using `Turbine` and `kotlinx-coroutines-test`.
* **Presentation Tests (`app/src/test/.../ui`)**:
  * Unit tests for shared atoms (`VehicleCardTest`), theme tokens (`ThemeTest`), and formatters (`FormatUtilsTest`).

---

## 8. Agent Execution Guidelines

When working on tasks in this repository, agents must:
1. **Preserve Boundaries**: Never add presentation dependencies to `domain` or leak DTOs outside `data`.
2. **Consult Schema**: Reference `data/vehicles.json` when writing data models and DTOs.
3. **No Hardcoded Strings**: Never hardcode strings in production UI or business logic. Add them to `res/values/strings.xml` and access them via `stringResource(...)`. The sole exception is mock preview data in `@Preview` composables.
4. **Strict Theme Adherence**: Exclusively use the colors, shapes, typography, and spacing already defined in `ui/theme/`. Do not create or add new items to the theme palette or custom design tokens.
5. **Verify Build**: Run Gradle build verification (`./gradlew test` or `./gradlew assembleDebug`) before marking tasks complete.
6. **Follow Hilt Conventions**: Add appropriate `@Provides` or `@Binds` in `di/` modules whenever creating new repository or data source interfaces.
7. **Keep KDoc Synchronized**: If a modified function has existing KDoc documentation, always update the KDoc to keep it aligned with code changes.
