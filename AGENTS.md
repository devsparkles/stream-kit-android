# StreamKit: AI Agent Guidance

## Project Overview

**StreamKit** is an Android study project (target SDK 36, min SDK 24) designed to implement ExoPlayer in a modular architecture. The goal is to support three streaming capabilities:
- Podcast playback in background (with lifecycle considerations)
- Video streaming
- Video with overlay rendering

Current state: Jetpack Compose UI skeleton with Material3 theming ready for media player integration.

## Architecture & Component Structure

### Build System: Gradle Kotlin DSL + Version Catalog

- **Root config** (`build.gradle.kts`): Applies Android application plugin and Kotlin Compose compiler
- **App config** (`app/build.gradle.kts`): Namespace `com.xaviermaximin.streamkit`, Java 11 compatibility
- **Dependency management** (`gradle/libs.versions.toml`): Single source of truth - ALL dependencies added here first, then aliased in build files
- **Key constraint**: AGP 9.1.0, Kotlin 2.2.10, Compose BOM 2024.09.00

When adding media dependencies (ExoPlayer), add version to `libs.versions.toml` under `[versions]`, then library entry under `[libraries]` with pattern:
```toml
exoPlayer = { group = "com.google.android.exoplayer", name = "exoplayer", version.ref = "exoPlayerVersion" }
```
Then reference in `app/build.gradle.kts` as `implementation(libs.exoPlayer)`

### Package Structure

```
com.xaviermaximin.streamkit/
├── MainActivity.kt          # Single activity, Compose-based entry point
└── ui/theme/                # Compose Material3 theme setup
    ├── Color.kt            # Custom color palette
    ├── Theme.kt            # StreamKitTheme composable
    └── Type.kt             # Typography definitions
```

**Future modules** (not yet created, but planned per README):
- `player/` - ExoPlayer wrapper with podcast/video/overlay modes
- `media/` - Media item models and data sources
- `background/` - Background playback service and lifecycle management

## Key Development Patterns

### Compose-First UI

- All UI in Jetpack Compose (no XML layouts)
- `MainActivity` uses `setContent {}` block, not `setContentView()`
- `enableEdgeToEdge()` called for immersive layout
- Material3 components via `androidx.compose.material3`
- Preview composables suffixed with `@Preview` for Android Studio preview

### Theme Application

- Centralized in `StreamKitTheme` composable - wrap all content
- Applied per screen in scaffold pattern (see `MainActivity`)
- Resources in `res/values/` (strings, colors) referenced via generated R class

### Testing Structure

- **Unit tests**: `src/test/java/` (JUnit 4)
- **Instrumented tests**: `src/androidTest/java/` (Espresso + Compose test APIs)
- Mock implementations expected in modular feature packages, not in main app

## Developer Workflows

### Build & Run

```bash
# From project root
./gradlew assembleDebug              # Build debug APK
./gradlew installDebug               # Install on connected device
./gradlew build                      # Full build with tests
./gradlew clean build                # Clean rebuild
```

### Preview & Test

```bash
# Unit tests (local)
./gradlew test

# Instrumented tests (device/emulator required)
./gradlew connectedAndroidTest

# Compose preview via IDE: @Preview-annotated functions in Android Studio
```

### ProGuard & Release

- ProGuard rules in `app/proguard-rules.pro` (currently empty)
- Release builds set `isMinifyEnabled = false` - enable when adding third-party libs
- Debug builds not minified (faster iteration)

## Integration Points & Dependencies

### Current Dependencies

**UI Framework**:
- `androidx.activity:activity-compose` (1.13.0) - Compose activity binding
- `androidx.compose.*` (Material3, UI, tooling) via BOM 2024.09.00
- `androidx.lifecycle:lifecycle-runtime-ktx` (2.10.0) - ViewModel/State lifecycle

**Testing**:
- JUnit 4 (local), Espresso (instrumented), Compose test APIs

### Critical Addition: ExoPlayer

When implementing the player module:
1. Add ExoPlayer dependency to `libs.versions.toml` (latest 2.x stable)
2. Create `player/MediaPlayerWrapper.kt` - abstraction over raw ExoPlayer
3. Handle lifecycle binding in `MainActivity` (pause on stop, release resources)
4. Background playback requires `Service` (separate from Compose UI thread) - use `MediaSessionCompat` for controls
5. Overlay rendering uses `Compose.Layer` or custom `Canvas` in compose

### Resource Organization

- Strings: `res/values/strings.xml`
- Colors: `res/values/colors.xml` + `ui/theme/Color.kt` (Material3 palette)
- Drawable assets: `res/drawable/` (currently icons only)
- Android XML resources must be accessed via generated R class in Kotlin code

## Project-Specific Conventions

1. **No XML layouts** - All UI in Compose
2. **Single-module app structure** (for now) - no multi-module until feature set stabilizes
3. **Namespace as package name**: Both tied to `com.xaviermaximin.streamkit`
4. **Kotlin 2.2 + Compose compiler** - Use `@Composable`, lambdas with trailing syntax
5. **Material3 design system** - No custom theming beyond provided in `ui/theme/`
6. **Java 11 baseline** - Use modern language features (records if targeting 24+, but Kotlin preferred)

## Critical Files to Reference

- **Architecture contracts**: `MainActivity.kt` (Compose entry), `ui/theme/Theme.kt` (design system)
- **Build truth**: `gradle/libs.versions.toml` (versions), `app/build.gradle.kts` (config)
- **Manifest contracts**: `AndroidManifest.xml` (permissions, activities) - `MainActivity` is single launcher
- **Project intent**: `README.md` (modular ExoPlayer streaming goal)

