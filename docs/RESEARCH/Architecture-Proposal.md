# Architecture Proposal (Stage 2)

Status: **Designed**.

## 1. Architecture principles

- Native-first: Kotlin + Jetpack Compose + Media3/ExoPlayer on Android; not an HTML video app.
- Local-first: playback and browsing work without any server. Credentials stay on-device.
- Module independence: parsers, provider clients, channel DB, EPG, player, VOD, recording, search, settings, sync and diagnostics are separate modules with explicit contracts.
- TV-first: every screen is designed for D-pad focus before touch adaptation.
- Replaceable engines: codec/provider/cast backends sit behind interfaces.
- Observability without surveillance: diagnostics are local, explicit, redacted and exportable only by user action.

## 2. Proposed module graph

```
app (Compose UI, navigation, theme)
 ├── feature-live        (channel browser, categories, zapping)
 ├── feature-epg         (timeline, program details, reminders)
 ├── feature-player      (Media3 wrapper, PiP, multi-view, stats)
 ├── feature-vod         (movies, series, seasons, episodes)
 ├── feature-recording   (scheduler, storage, library)
 ├── feature-provider    (add/edit providers, diagnostics UI)
 ├── feature-settings    (profiles, parental, appearance, network)
 ├── feature-search      (global search, local NL parsing)
 ├── core-provider       (templates, XC/Stalker/M3U/OTC adapters)
 ├── core-parser         (M3U, XMLTV, OTC, OTV, XSPF, Enigma2, TXT)
 ├── core-channel        (Room DB, channel model, dedupe/tags)
 ├── core-epg            (ingest, match, timeline model)
 ├── core-playback       (ExoPlayer abstraction, track policy)
 ├── core-vod            (repository + metadata)
 ├── core-recording      (WorkManager jobs, storage backends)
 ├── core-security       (Keystore, redaction, consent)
 ├── core-diagnostics    (health, logs, exports)
 └── core-sync (optional) (self-hosted backup/Sync API client)
```

Dependencies point inward: features depend on cores; cores never depend on UI. A `core-model` module holds shared IDs and value objects.

## 3. Technology choices

### Android client

| Concern | Choice | Rationale |
|---|---|---|
| Language | Kotlin | Native Android TV standard |
| UI | Jetpack Compose + Material 3 Expressive | Adaptive, declarative, TV focus APIs |
| Navigation | Navigation 3 / adaptive scaffolds | Multi-pane, state preservation |
| Playback | Media3 ExoPlayer (+HLS, DASH) | Native, DRM-capable, PiP, Cast |
| Database | Room | Channel/EPG/VOD/recording indexes |
| Preferences | DataStore | Settings/profiles; no SharedPreferences sprawl |
| Background work | WorkManager | Refresh, recording, reminders |
| Images | Coil | Async posters/logos with caching |
| DI | Manual composition root or Hilt | Explicit module boundaries |
| Testing | JUnit, Turbine, Compose tests, screenshot tests | Layered verification |

### Optional companion backend (only with user opt-in)

TypeScript/Node.js, PostgreSQL, Redis, secure REST APIs. Used only for cross-device sync of settings/profiles/backups. No playlist credentials are uploaded by default; the player never requires the backend. Self-hosting is supported.

## 4. Key contracts (interfaces)

```kotlin
interface PlaylistParser { fun supports(input: FormatHint): Boolean; suspend fun parse(source: Source): ParseResult }
interface ProviderClient { suspend fun loadChannels(): ProviderResult; suspend fun loadEpgrange(): EpgResult; val limits: ConnectionLimits }
interface ChannelRepository { fun observeChannels(): Flow<List<Channel>>; suspend fun apply(providerId: String, channels: List<Channel>) }
interface EpgRepository { suspend fun ingest(source: EpgSource): EpgIngestReport; fun observeProgrammes(): Flow<List<Programme>> }
interface PlayerEngine { fun prepare(item: Playable, policy: PlaybackPolicy); fun release(); fun observeState(): Flow<PlayerState> }
interface RecordingStorage { suspend fun write(rule: RecordingRule): RecordingHandle; fun freeSpace(): Long }
interface SyncService { suspend fun backup(payload: EncryptedBackup); suspend fun restore(id: String): EncryptedBackup }
```

## 5. Data flow

1. Provider import: Source → format sniff → parser → normalized `Channel`/`VodItem` list → dedupe/tag → Room.
2. EPG: Source → decompress → stream parse → match → Room → timeline/now-next.
3. Playback: Channel/VodItem → URL + headers resolver → PlayerEngine → Media3 source → renderer/tracks → stats.
4. Recovery: error classifier → policy (retry/source/next) → connection broker → diagnostics event.
5. Recording: rule → WorkManager → stream copy → storage backend → library index.

## 6. Security design

- Secrets: Android Keystore-backed encrypted DataStore for passwords/tokens/headers.
- Redaction: diagnostics and logs never contain credentials, full URLs with tokens, or DRM keys.
- TLS: validate by default; invalid-certificate override requires explicit per-provider consent and warning.
- Parsers: no external entities/DTD; size limits; timeouts; bounded nesting; no dynamic code.
- DRM: use platform APIs only; no key extraction or circumvention; unsupported DRM is reported clearly.
- Backups: encrypted by default; optional password; self-hosted endpoint is user-chosen.
- Profiles: PIN/biometric gating; Keystore-backed PIN verifier; no plaintext PIN.

## 7. Testing strategy

- Unit: parsers, matchers, URL template engine, dedupe, policy resolver.
- Integration: local fixture provider server (M3U/XMLTV/OTV/XC-like) with deterministic responses.
- UI: Compose previews + screenshot tests for TV and phone form factors.
- Playback: test HLS/DASH/TS streams; simulated HTTP failures; codec switching.
- Security: static checks for secrets in logs, parser fuzzing, TLS negative tests.
- Device: emulator baseline + physical TV/box in Stage 13.

## 8. Migration from current prototype

The current project already contains: M3U parser, XMLTV parser, DataStore settings, channel/favorite/history/recordings UI, Media3 playback, adaptive Compose navigation. These move into the module boundaries above; provider/EPG/VOD code is extracted from `MainViewModel`/`MainScreen` into cores and features. No code is copied from the competitor.
