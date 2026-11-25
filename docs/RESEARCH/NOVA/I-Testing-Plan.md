# I. Testing Plan — NOVA add-on ecosystem

Status: **Designed**. No tests written in this phase.

## 1. Test principles

- Every capability has contract tests against a local fake implementation.
- No test depends on third-party services or real provider credentials.
- Untrusted add-on behavior is simulated with malicious manifests/responses.
- A feature is not `Tested` until its tests pass in CI; not `Verified` until device/QA sign-off.

## 2. Test layers

### Unit tests

- Manifest validation: required fields, semantic versions, unknown capability/permission rejection.
- Capability routing by type/idPrefix.
- Permission resolution and denial.
- Search query normalization and natural-language parser (local, deterministic).
- Metadata conflict resolution, duplicate-content scoring.
- URL template substitution (`${token}`, `${login}`, catchup variables) with redaction.

### Contract tests

- Stremio HTTP adapter: `/manifest.json`, `catalog`, `meta`, `stream`, `subtitles`, `player`/`library` event opt-in, Native EPG `metasDetailed`.
- NOVA provider interface: `getCatalog`, `search`, `getMovieDetails`, `getSeriesDetails`, `getEpisodes`, `getAvailableSources`, `getSubtitles`.
- Media server interface: auth, libraries, items, playback URL, progress.
- Repository manifest format and signed bundle verification.

### Integration tests (local fixtures)

- Fake Stremio add-on served over localhost with CORS.
- Fake JSON repository with valid/invalid manifests.
- M3U, XMLTV (gz/xz), OTC, OTV fixtures from the existing NOVA test data.
- Xtream-like, Stalker-like, and Astra-like mock endpoints for provider suite.
- Media server mocks returning library and playback sources.

### UI tests

- Compose previews for TV and mobile for all screens in `H-UI-Screen-Inventory.md`.
- Screenshot tests for marketplace, source manager, universal home, add-on details.
- D-pad focus traversal tests: no focus traps, every primary action reachable.
- Permission dialogs require explicit confirm.

### Security tests

- Manifest fuzzing; path traversal in local bundles.
- Credential redaction in logs/diagnostics.
- Network allowlist enforcement; blocked host attempts.
- Crash containment for local extension worker.
- Revocation/uninstall cleanup verification.
- Fake source claims: unresolved resolution/language/subtitle must display "unknown".

### Performance tests

- 20k-channel browser; 100k-item EPG; large catalog pagination.
- Add-on timeout/circuit-breaker behavior.
- Low-memory TV profile with previews/thumbnails disabled.

## 3. Milestone exit tests

| Milestone | Exit tests |
|---|---|
| 1 Extension architecture | Core interfaces compile; no circular module deps |
| 2 Manifest & registry | Validator rejects bad manifests; version pinning works |
| 3 Add-on manager | Install/enable/disable/uninstall/cleanup tests |
| 4 Marketplace | Local repo fixture browsable; no invented ratings/badges |
| 5 Metadata | Meta merge and manual correction tests |
| 6 Catalog | Catalog/search/details tests with fake add-on |
| 7 Global search | Cross-source search + filters + NL parser tests |
| 8 Unified library | Watch state, watchlist, duplicate detection tests |
| 9 Source manager | Source claim honesty + fallback policy tests |
| 10 Media servers | Mock Jellyfin/Plex/Emby/Kodi/DLNA contract tests |
| 11 Developer SDK | Sample extension passes compatibility validator |
| 12 Security & compatibility | Full security suite + TV/phone device matrix passes |

## 4. Device matrix

- Emulator `Small_Phone` (Android 16 API 36, x86_64) — baseline.
- Phone/tablet emulator profile for vertical layouts.
- Physical Android TV / Google TV box — Stage 12/13.
- Fire TV / low-memory box — optional but recommended.

## 5. Test data ownership

Fixtures are original/synthetic. No copyrighted posters, logos or streams are bundled.
