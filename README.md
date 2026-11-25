# NOVA IPTV

An original, clean-room, open-source IPTV/OTT player for Android TV and phone/tablet.

> **Not affiliated with, derived from, or a copy of the reference commercial IPTV player.** No code,
> resources, logos, or reverse-engineered implementation details from any
> proprietary APK are used in this project.

## Status

Early scaffold. The module structure, documentation, license and CI are in
place; application features are implemented incrementally. See
[docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) for the intended design.

## Planned features

- Import user-supplied M3U playlists (local file or URL) and XMLTV EPG data
- Three user-disableable built-in gzip XMLTV sources (ALL OTT, JioTV, TataPlay)
- Channel list, favorites, watch history, search and a program Guide
- Recordings scheduling and playback-source navigation
- Stream playback via AndroidX Media3 / ExoPlayer with HLS and DASH support
- Adaptive Material 3 Expressive UI for phone/tablet and Android TV
- Clean-room add-on ecosystem planning in `docs/RESEARCH/NOVA/`

## Non-goals

- Bundling copyrighted streams or playlists; built-in EPG links are third-party, user-disableable, and not warranted by this project
- Provider credentials, subscription handling, or license/payment bypasses
- Copying another app's code, assets, branding or user interface

## Build

Requirements:

- JDK 21
- Android SDK platform 37 and build-tools 36.1.0 or newer
- Android Studio (latest stable) or the included Gradle wrapper

```sh
./gradlew assembleDebug
./gradlew testDebugUnitTest
```

The app module is `app`, package `com.open.iptv`, `minSdk 24`,
`targetSdk 37`.

## Project layout

```
app/src/main/java/com/open/iptv/
  data/     # playlist/EPG parsing, network, repositories, storage
  domain/   # models and use cases
  player/   # Media3/ExoPlayer playback wrapper
  ui/       # shared Compose components
  mobile/   # phone/tablet screens
  tv/       # Android TV / remote-friendly screens
docs/       # architecture and notes
.github/    # CI workflow
```

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## License

[MIT](LICENSE). Contributors retain copyright to their original contributions.
