# Product Requirements — Open IPTV (Stage 2)

Status: **Designed** (requirements baseline; not implementation claims).

## 1. Product definition

An independent, open-source IPTV/OTT player for Android TV / Google TV, Android boxes, phones and tablets. It consumes **user-supplied, authorized** playlists and provider APIs. It ships no channels, no subscriptions, no provider credentials, and no DRM circumvention.

Legal/ethical constraints:

- No copying proprietary code, assets, logos, UI layouts, or branding.
- No license-check bypass, premium activation bypass, or account forgery.
- No access to unlicensed content; the user is responsible for their provider and content rights.
- DRM support is limited to standards the device exposes legitimately (Widevine/PlayReady/ClearKey via platform APIs); no keys are shipped or extracted.

## 2. Target platforms

- Primary: Android TV / Google TV, API 26+ (TV-first remote navigation).
- Secondary: Android phones/tablets, API 24+.
- Optional companion backend only for user-opted sync; core playback works fully offline/local.
- Verification hardware: emulator `Small_Phone` (Android 16 API 36, x86_64), physical TV/box to be added in Stage 13.

## 3. Functional requirements by module

### 3.1 Provider engine (PR-A)

- PR-A1: CRUD providers with types: Playlist, Xtream Codes, Stalker/MAC, OTC, XSPF, Enigma2, XML, TXT, local file.
- PR-A2: Credentials and headers stored with Android Keystore-backed encryption; never uploaded by default.
- PR-A3: Per-provider configuration: codec preference, stream technology, categories include/exclude/rename, catch-up overrides, HTTP headers, API/content User-Agent, Referer, max connections, EPG sources, time shift, auto-refresh policy, enabled modules.
- PR-A4: Import pipeline sniffs format by extension and content, parses in a background worker, reports counts/warnings/errors, and supports nested playlists with loop protection.
- PR-A5: Duplicate detection with configurable match strategy and per-entry source list.
- PR-A6: Provider health: last success, latency, channel count delta, HTTP status, certificate errors, saved-copy fallback.
- PR-A7: Connection broker enforces per-provider limits across playback, previews, PiP, Studio and recording.
- Acceptance: importing a valid M3U with `#EXTINF`, catch-up, headers and BOM produces the expected channels; a 403 produces a structured diagnostic; credentials are not present in logs or backups unless explicitly included and encrypted.

### 3.2 Live TV engine (PR-B)

- PR-B1: Virtualized channel browser with categories, tags, favorites, recent, hidden/locked states.
- PR-B2: Sorting, filtering, provider facets, custom numbers, manual ordering, duplicate folding.
- PR-B3: Quick switching with recent history, number keys, channel up/down, source switching.
- PR-B4: Optional previews with connection-budget guard and provider disable flag.
- PR-B5: Automatic recovery: retry, source fallback, next/last channel, skip-broken window.
- Acceptance: 20,000-channel list scrolls without jank on emulator; favorites persist after restart; broken stream is retried and then routed according to user policy.

### 3.3 EPG engine (PR-C)

- PR-C1: XMLTV parser for plain/gzip/xz, streamed off the main thread.
- PR-C2: Multi-source ingestion with source labels and merge conflict policy.
- PR-C3: Matching by tvg-id, tvg-name/name, manual mapping, and match-mode configuration.
- PR-C4: Data model supports title, sub-title, description, category, actors, directors, rating, star rating, posters/icons, episode ids, catch-up id.
- PR-C5: Timeline view with current/next, program details, search, reminders, archive links.
- PR-C6: Time-shift at channel/provider/app level; timezone sanity checks from device clock.
- PR-C7: The EPG subsystem must accept programme data from the full provider suite: Direct Stream, M3U/M3U8, Xtream Codes, MAC/Stalker, local playlist files, Cesbo Astra, configuration-backup imports and common local templates.
- PR-C8: Optional built-in default EPG sources (user-disableable, replaceable):
  - `https://avkb.short.gy/epg.xml.gz` (ALL OTT EPG)
  - `https://avkb.short.gy/jioepg.xml.gz` (JioTV EPG)
  - `https://avkb.short.gy/tsepg.xml.gz` (TataPlay EPG)

  These are third-party sources selected by the user as convenient defaults; NOVA does not own or warrant them. The user can disable them and supply authorized XMLTV sources instead.
- Acceptance: valid XMLTV loads with correct program counts (plain/gzip); a deliberately shifted feed is corrected by configured offset; unmatched channels appear in a diagnostic list; each default source failure is reported independently without breaking the others.

### 3.4 Playback engine (PR-D)

- PR-D1: Media3/ExoPlayer-based player abstraction; HLS, DASH, progressive TS/MP4; optional RTSP/VLC module.
- PR-D2: Track selection for audio/subtitles/video with language preferences and per-channel memory.
- PR-D3: External subtitle formats SRT/WebVTT/ASS with local file picker and optional OpenSubtitles integration using user account.
- PR-D4: Aspect ratio modes, zoom, deinterlacing, AFR with safe-mode list, audio shift.
- PR-D5: Buffer profiles (none/15/30/60/240s) and low-memory profile.
- PR-D6: retry/backoff, source fallback, broken-channel cooldown, structured error mapping (401/403/404/5xx/SSL/ENOENT/DRM).
- PR-D7: DVR window detection for HLS/DASH when no provider archive exists.
- PR-D8: MediaSession notification, screen-off audio, system PiP.
- PR-D9: Player stats overlay: resolution, fps, bitrate estimate, buffer health, dropped frames, codec, ABR level.
- Acceptance: HLS/DASH test streams play; codec/track choice survives channel switch; simulated 403 triggers retry then user-visible diagnostic; state persists across rotation.

### 3.5 Movies and series (PR-E)

- PR-E1: VOD repository from XC/Stalker-like APIs, M3U `vod_library`, OTV JSON, user links, DLNA/SMB/WebDAV.
- PR-E2: Movies/series/TV shows, categories, seasons, episodes, parts, multiple videos, subtitles files.
- PR-E3: Metadata model with poster/backdrop/plot/cast/director/genre/rating/year/duration/TMDB/IMDb.
- PR-E4: Continue watching, resume, watchlist, favorites, search, filters and sorting.
- PR-E5: Optional user-provided TMDB API key for enrichment.
- Acceptance: sample OTV file produces movies, series, seasons, episodes and posters; resume position survives restart; missing poster uses generated placeholder.

### 3.6 Advanced viewing (PR-F)

- PR-F1: In-app multi-view/Studio grid up to device capability; per-tile audio focus and swap.
- PR-F2: System PiP and in-app PiP with connection guard.
- PR-F3: Chromecast via Media3 Cast and DLNA/UPnP casting from phone/tablet with header preservation where the protocol allows.
- PR-F4: Android TV home recommendations via MediaSession/TvProvider where permitted.
- Acceptance: two concurrent streams respect provider limit; cast attempts show clear unsupported/DRM errors; TV remote can focus every tile.

### 3.7 Recording (PR-G)

- PR-G1: Record current channel, scheduled time, EPG programme, archive save.
- PR-G2: Storage backends: internal, SAF folder/USB/SD, SMB, WebDAV.
- PR-G3: Chunked writes <4GB, retry uploads, free-space guard, old-recording cleanup policy.
- PR-G4: Recording states, notifications, error recovery, playback of in-progress recordings.
- Acceptance: a scheduled recording starts via WorkManager; simulated storage failure produces a retryable status; recordings survive app restart.

### 3.8 Other features (PR-H)

- PR-H1: Radio page with metadata and background playback.
- PR-H2: Sports center derived from EPG and event channels; no scores.
- PR-H3: Reminders with notifications and optional auto-switch.
- PR-H4: Profiles with per-profile favorites, history, VOD state and settings; PIN/biometric.
- PR-H5: Parental controls: child mode, restrict code, adult code, locked channels.
- PR-H6: Customization: themes, wallpapers, layouts, remote keys, gestures, overscan.
- PR-H7: Backup/restore: encrypted local, optional self-hosted endpoint, selective restore.
- PR-H8: Boot autostart with platform-permission guidance.
- PR-H9: Network settings: proxy, DoH, VPN check, wait-for-network, SSL policy with explicit user consent.
- Acceptance: two profiles have isolated favorites/history; PIN blocks settings; backup round-trips with password; network failure states are understandable.

## 4. Non-functional requirements

- Performance: cold start <3s on reference TV emulator; channel list first paint <500ms for 20k items; no frame drops during EPG scroll on low-memory profile.
- Memory: low-memory profile disables previews/thumbnails and reduces buffers; no unbounded caches.
- Privacy: no analytics by default; no credential upload; diagnostics export is explicit and redacts credentials.
- Security: Keystore for secrets, TLS validation by default, explicit opt-in for invalid certificates, input validation on all parsers, XXE disabled, no dynamic code execution from playlists.
- Offline: core features require no backend; account sync is optional and self-hostable.
- Testability: each module has unit tests; UI has Compose previews/screenshot tests; integration tests use local fixtures.
- Documentation: every feature record includes limitations and evidence.

## 5. Data model overview

`Provider`, `Playlist`, `Channel`, `Category`, `Tag`, `EpgSource`, `Programme`, `VodItem`, `Season`, `Episode`, `VideoSource`, `SubtitleTrack`, `RecordingRule`, `Recording`, `Profile`, `Favorite`, `HistoryEntry`, `Settings`, `NetworkProfile`, `DiagnosticEvent`.

## 6. Acceptance traceability

Each requirement ID maps to a row in `Feature-Parity-Matrix.md` and a screen in `Screen-Inventory.md`. A requirement is "Verified" only when implementation, tests, device check, and security review are recorded.
