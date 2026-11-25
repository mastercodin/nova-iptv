# Architecture

## Principles

- **Clean-room:** implementation is written from scratch; no proprietary code
  or assets are copied.
- **Layered:** UI depends on domain, domain depends on data abstractions;
  player code is isolated so the playback engine can be swapped.
- **Testable:** playlist/EPG parsing and use cases are pure Kotlin where
  possible and covered by unit tests.
- **Dual-form-factor:** shared domain/data/player layers with separate
  phone/tablet and TV UI layers.

## Intended packages

| Package | Responsibility |
| --- | --- |
| `com.open.iptv.data` | M3U/XMLTV parsers, HTTP clients, repository implementations, local storage |
| `com.open.iptv.domain` | Models, repository interfaces, use cases |
| `com.open.iptv.player` | Media3/ExoPlayer wrapper, playback state and controls |
| `com.open.iptv.ui` | Shared Compose components and theming |
| `com.open.iptv.mobile` | Touch-oriented screens for phone and tablet |
| `com.open.iptv.tv` | Remote-friendly screens and focus handling for Android TV |

Dependency direction:

```
mobile / tv  ->  ui  ->  domain  ->  data
                     \->  player
```

The `player` layer should not depend on UI. Data sources should be injected
into repositories so tests can use fakes.

## Planned technical choices

- Kotlin, Coroutines and Flow
- AndroidX Media3 / ExoPlayer for playback
- OkHttp for playlist/EPG fetching
- Room for favorites and cached guide data (optional)
- Jetpack Compose for both UI variants, with TV-specific focus handling
- Dependency injection: to be decided (Hilt vs. manual composition root)

## Open decisions

1. Playback engine: Media3 only, with optional libVLC fallback for codecs
   Media3 cannot handle.
2. TV UI toolkit: Compose for TV vs. Leanback Compose integration.
3. EPG scope: full XMLTV import vs. per-channel program windows.
4. Persistence: Room vs. DataStore for settings and favorites.

## Verification

- Unit tests for parsers and use cases.
- Instrumented tests for the player screen on at least one phone and one TV
  emulator profile.
- CI runs `assembleDebug` and `testDebugUnitTest` on every push/PR.
