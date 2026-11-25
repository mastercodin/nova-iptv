# Feature Parity Matrix (Stage 1 baseline)

Status vocabulary used below: `Not researched`, `Researched`, `Designed`, `In development`, `Implemented`, `Tested`, `Verified`, `Platform limited`, `Provider dependent`.

This is an honest baseline of the current open-source project, not a claim of completion. "Tested" means a unit test or emulator check exists; "Verified" is reserved for the final acceptance stage.

Each row records: feature, observed competitor evidence, user workflow/screens/settings, required data/dependencies, limitations/premium/errors, our status, and our improved implementation intent.

## A. Provider engine

| ID | Feature | Competitor evidence / workflow / screens / settings | Data, dependencies, limits, errors | Our status | Our improved version |
|---|---|---|---|---|---|
| A1 | Multiple provider accounts | Settings > Provider; several active at once (Premium); quick switch | Credentials local; provider order; `max_conn` | Researched — competitor requires Premium | Free multi-provider, local credentials, priority engine |
| A2 | M3U / M3U8 | Default format; full tag support; link preferred over file | `#EXTM3U`, `#EXTINF`, XMLTV, headers, DRM | **Tested** — `M3uParserTest` | Strict + tolerant parser, diagnostics report |
| A3 | Xtream Codes API | Server URL, login, password, v1/v2; live+VOD+EPG | API endpoints, token, connection count | Researched | Independent REST client, no credential upload |
| A4 | Stalker / MAC portal | Portal URL, MAC, optional login, device_id/sn/stb_type | Handshake, device profile | Researched | Pluggable portal adapter |
| A5 | OTC JSON | `.otc`/`otc.json`, base64, localized fields | JSON schema documented | Researched | Independent OTC reader |
| A6 | XSPF | `.xspf` or `<trackList>` | XML | Researched | Format detector + parser |
| A7 | Enigma2 `.tv` | `#SERVICE` lines | Bouquet text | Researched | Format detector + parser |
| A8 | XML/TXT formats | WebOS XML, Tizen XML, plain TXT list, XML list-of-playlists | Format sniffing | Researched | Auto-detect pipeline |
| A9 | Provider-specific configuration | Provider properties page: codec, categories, archive, network, EPG | Per-provider settings store | **In development** — one playlist URL saved | Full provider entity with properties screen |
| A10 | Playlist refresh | `refresh`, `refresh_at`, manual reload, intervals, saved copy | WorkManager, cache | Researched | Smart refresh with ETag/304 and health status |
| A11 | Provider status | Reload report, channel counts, errors, expiry/billed message | Status model | Researched | Dashboard with health, latency, last-good copy |
| A12 | Connection limits | 1–20 per provider; PiP/Studio/recording accounting | Counter + provider plan | Designed | Global connection broker |
| A13 | Custom headers | `#EXTHTTP`, VLC opts, Kodi stream_headers | Secure header store | Researched | Per-provider + per-channel header layers |
| A14 | User-Agent / Referer | App/provider/channel precedence | Header resolver | Researched | Transparent precedence viewer |
| A15 | Provider prioritization | Rename with number / Change priority | Order field | Designed | Drag ordering on TV |
| A16 | Duplicate detection | Fold by name/category/tvg-id, suffix preference | Fingerprinting | Designed | Smart duplicate scoring, one-tap merge |
| A17 | Category management | Merge/prefix/suffix provider categories; hide/rename | Category entity | Tested (basic group filter) | Rich category editor |
| A18 | Channel tagging | Tags as virtual categories; mass tagging | Tag join table | Researched | Tags + automation rules |
| A19 | Custom logos | URL zip pack or per-channel logo | Images, cache | Researched | Local cache + fallback initials |

## B. Live TV engine

| ID | Feature | Evidence / workflow | Limits / errors | Status | Improved version |
|---|---|---|---|---|---|
| B1 | Channel browser | Main Live screen, list/grid/cards | Large lists need indexing | **Tested** | Virtualized, sectioned browser |
| B2 | Channel categories | Category rows, virtual categories | Provider data | **Tested** | Merge/auto-classify |
| B3 | Favorites | Channel menu / star | Per-profile | **Tested** | Syncable per profile |
| B4 | Watch history | Recent/f watched | Per-profile | **Tested** (recent chips) | Full history screen with progress |
| B5 | Recently watched | Recent category/player menu | 5s threshold in competitor | **Tested** | Configurable threshold |
| B6 | Quick switching | Player menu recent, number keys | Connection count | Implemented (prev/next) | Predictive quick zapping |
| B7 | Channel previews | Preview in lists, video/thumbnail | Premium, connection limit, disabled by provider | Researched | Silent preview with health guard |
| B8 | Channel sorting | Provider order/name/frequency + fallback | Per category | Researched | Saved sort profiles |
| B9 | Advanced filtering | Hide/restrict, category/provider filters | Premium for many | Researched | Facet filters |
| B10 | Custom ordering | Drag/manual channel numbers | Premium | Researched | TV-friendly reorder |
| B11 | Channel search | Global search, voice, recent queries | Large-list optimization | **Tested** (name filter) | Unified search engine |
| B12 | Multi-provider aggregation | Merged channel list, quick provider switch | Premium | Researched | Provider facets in browser |
| B13 | Automatic channel recovery | Retry, switch source/channel, skip broken | Provider errors | Designed | Health-scored auto-routing |

## C. EPG engine

| ID | Feature | Evidence / workflow | Limits / errors | Status | Improved version |
|---|---|---|---|---|---|
| C1 | XMLTV | Plain/gz/xz, header `url-tvg`, user sources | Matching errors | **Tested** (`XmlTvParserTest`) | Streaming parser for huge files |
| C2 | Multiple EPG sources | Up to 5/provider + global (Premium) | Premium | Researched | Free multi-source with conflict resolution |
| C3 | Compressed XMLTV | gzip/xz detected by content | Memory | Researched | Background decompression worker |
| C4 | Automatic matching | `tvg-id`, `tvg-name`, case rules | Wrong matches | In development | Confidence scoring + suggestions |
| C5 | Manual mapping | Channel menu > Map channel to EPG | Applies after reload | Researched | Bulk mapping UI |
| C6 | Program timeline | Timetable/tvguide view, now/next | Large data | Designed | Fast horizontal timeline |
| C7 | Current/next programmes | Channel list/description/player HUD | EPG required | Implemented (Guide list) | Timeline + now/next cards |
| C8 | Program descriptions | XMLTV title/sub-title/desc/cast/etc. | Language selection | **Implemented** | Rich details screen |
| C9 | Program search | Global search, future records | Premium for some | Researched | Search across channels + descriptions |
| C10 | Time-zone correction | Per channel/provider/app shift; internal/display clocks | Device clock | Researched | Automatic sanity detector |
| C11 | EPG synchronization | Reload intervals, on-demand XC/Stalker, cache-first | Provider throttling | **Implemented** (URL/local load) | Incremental sync |
| C12 | Reminders | Remind / auto-switch (Premium) | App must be open | Researched | Local notifications + rules |
| C13 | Catch-up integration | `catchup-id`, programme archive links | Provider support | Researched | Unified "watch earlier" action |
| C14 | EPG from full provider suite | Direct Stream, M3U, XC, MAC/Stalker, local files, Cesbo Astra, backup import, local templates | Provider-dependent | Researched | One EPG normalization pipeline for every provider type |
| C15 | Built-in default EPG sources | Three gzip XMLTV feeds supplied as user-replaceable defaults; independently fetchable | Third-party data, not warranted; disableable | **Implemented** | Clear source labels, per-source failure reporting, user override |

## D. Playback engine

| ID | Feature | Evidence / workflow | Limits / errors | Status | Improved version |
|---|---|---|---|---|---|
| D1 | Media3 / ExoPlayer | Default hardware codec; Media3 on Play listing | Device codecs | **Implemented / Tested** | Single abstraction, per-channel choice |
| D2 | HLS | Adaptive, DVR window, multi-period | Manifest bugs | Implemented (module added) | Manifest health diagnostics |
| D3 | DASH | Multi-period/ads, DRM | Device DRM | Implemented (module added) | Period timeline UI |
| D4 | MPEG-TS | Often better for XC; direct streams | Feeds with broken PMT | Implemented (ExoPlayer progressive) | TS repair hints |
| D5 | Hardware decoding | Default, quality modes | Firmware bugs | **Implemented** | Codec profile per device |
| D6 | Software decoding | VLC fallback | CPU load, Android 6+ | Researched | Optional bundled fallback |
| D7 | Audio selection | Preferred languages, track memory | HLS packed tracks | Researched | Track picker + preferences |
| D8 | Subtitle selection | Track list, styles, shift | Codec-dependent | Researched | Track picker |
| D9 | External subtitles | OpenSubtitles, SRT/WebVTT/ASS, OTV subtitles | User account / Premium | Researched | Local subtitle file picker |
| D10 | Aspect ratios | Six modes, per-channel | Zoom/overscan | Researched | Gesture + TV remote cycling |
| D11 | Zoom | 50–150%, X/Y scaling (Premium) | Premium | Researched | Free zoom controls |
| D12 | Audio sync | ±5s hardware, per-channel | Premium for hardware | Researched | Visual lip-sync assistant |
| D13 | Adaptive bitrate | Auto/lowest/highest/resolution cap | Premium for caps | Implemented (Media3 default) | Network-aware profiles |
| D14 | Frame-rate matching | AFR 25/30/50/60/24, exclusions | TV/box only, Premium | Researched | Free AFR with safe modes |
| D15 | Buffer optimization | 15/30/60/240s/no-buffer, VOD buffer | Low memory | Researched | Adaptive buffer by device class |
| D16 | Automatic retry | 1–10/always/manual/instant, buffering-as-error | Provider limits | Designed | Policy with backoff + source routing |
| D17 | Stream diagnostics | Error text, bitrate estimate, stats | Provider-specific | Designed | Stats overlay + diagnostics export |
| D18 | DVR / timeshift | Live HLS/DASH sliding window ≥5 min | Premium toggle | Researched | Free DVR window where supported |
| D19 | Background playback | Screen-off sound (Premium), MediaSession | Phone/tablet, Premium | Researched | Free background audio service |
| D20 | PiP | In-app and system PiP, swap audio | Premium, Android 8+, connections | Researched | Free PiP with connection broker |

## E. Movies and series

| ID | Feature | Evidence / workflow | Limits / errors | Status | Improved version |
|---|---|---|---|---|---|
| E1 | VOD library | Provider, user sources, DLNA, SMB/WebDAV | Module toggle | Researched | Unified library index |
| E2 | Movie categories | Folders, filters, genres | Provider data | Researched | Smart collections |
| E3 | Series categories | Series/TV shows split | OTV `tv_show` | Researched | Same |
| E4 | Seasons | OTV seasons, XC seasons | Provider data | Researched | Season grid |
| E5 | Episodes | Episodes/parts, next episode | Metadata | Researched | Episode screen |
| E6 | Posters | Poster/bg/backdrop, colored fallback | Images | Researched | Coil caching |
| E7 | Metadata | Plot/cast/director/country/year/genre | Provider/TMDB | Researched | Metadata merge |
| E8 | Ratings | 0–10, star-rating in XMLTV | Provider data | Researched | Ratings UI |
| E9 | Genres | Genre filter, hidden categories | One-word genres | Researched | Genre facets |
| E10 | Search | Global/local VOD search, deep search | Premium for deep | Researched | Unified search |
| E11 | Continue watching | Home row, VOD progress | Per profile | Researched | Free continue watching |
| E12 | Playback progress | Save/restore position | Provider support | Researched | Local + optional sync |
| E13 | Resume playback | Ask/offer resume | Premium for some | Researched | Smart resume |
| E14 | Watchlist | VOD favorites/followed shows | Per profile | Researched | Watchlist screen |
| E15 | TMDB integration | User API key, metadata enrichment | User key, Premium in competitor | Researched | Optional user-provided key |
| E16 | Custom media libraries | User JSON/M3U/OTV/XML, network folders | Premium for network folders | Researched | Free user libraries |

## F. Advanced viewing

| ID | Feature | Evidence / workflow | Limits | Status | Improved version |
|---|---|---|---|---|---|
| F1 | Studio mode | Up to 20 screens, layouts, slots | Premium, connections, device decode | Researched | Free multi-view with capability guard |
| F2 | Multi-screen viewing | Side-by-side, huge lists | Device memory | Researched | Adaptive grid |
| F3 | Picture-in-picture | In-app PiP + system PiP | Premium, Android 8+ | Researched | Free PiP |
| F4 | Stream swapping | Swap main/PiP | Premium | Researched | Free swap |
| F5 | Audio switching between screens | Swap sound main/PiP | Premium | Researched | Free audio focus picker |
| F6 | Chromecast | Play on TV, Google Cast module | Phone only, Play services, Premium | Researched | Media3 Cast, no paywall |
| F7 | DLNA / UPnP | TV discovery, media library | TV codecs, same Wi-Fi | Researched | Free cast/file bridge |
| F8 | Android TV integration | Play Next, favorite/recent rows, banners | TV device | Researched | TV-first Compose UI |

## G. Recording

| ID | Feature | Evidence / workflow | Limits | Status | Improved version |
|---|---|---|---|---|---|
| G1 | Live recording | Player/channel menu Record | Premium, storage | Researched | Free recording with consent guard |
| G2 | Scheduled recording | Up to 7 days, time + duration | Premium | Researched | WorkManager scheduler |
| G3 | EPG-based recording | Programme menu, start/end padding | EPG | Researched | Rule-based scheduling |
| G4 | Archive saving | Save finished programme from archive | Provider support | Researched | Archive downloader |
| G5 | Recording library | Recordings tab, states | Premium | Researched | Library screen |
| G6 | USB storage | App folder on USB | Uninstall deletes | Researched | SAF picker |
| G7 | SD storage | App folder on SD | Device support | Researched | SAF picker |
| G8 | Internal storage | App-private folder | Space | Researched | Quota manager |
| G9 | SMB | `smb://` share + credentials | Network, Premium | Researched | Optional SMB module |
| G10 | WebDAV | HTTPS WebDAV share | No free-space report | Researched | Optional WebDAV module |
| G11 | Recording status | Recording/Missed/Interrupted/Partly | Notifications | Researched | Status history |
| G12 | Error recovery | Retry uploads, space guard, delete old | Storage failure | Researched | Transparent recovery log |

## H. Other features

| ID | Feature | Evidence / workflow | Limits | Status | Improved version |
|---|---|---|---|---|---|
| H1 | Radio | Radio page, controls, covers | Premium extras | Researched | Free radio page |
| H2 | Sports center | EPG/event-channel detection | Premium, EPG | Researched | Free sports hub |
| H3 | Program reminders | Remind/auto-switch | App open | Researched | Local notifications |
| H4 | Multiple profiles | Separate favorites/history/settings | Premium | Researched | Free profiles + PIN |
| H5 | Parental controls | Restrict code, adult code, settings lock | Premium for locks | Researched | Free, platform Keystore-backed |
| H6 | Child mode | Allowlisted channels, exit code | Premium for marking | Researched | Free child mode |
| H7 | PIN protection | 4-digit profile/restrict/service codes | Codes in app data | Researched | Biometric + Keystore |
| H8 | Custom wallpapers | Built-in/random/URL | Premium for URL | Researched | Local + URL |
| H9 | Themes | 10 themes × 3 variants, accents | Premium for some | Researched | M3 Expressive dynamic themes |
| H10 | Custom layouts | Home columns, list styles, launcher buttons | Premium for some | Researched | Dashboard editor |
| H11 | Remote configuration | Bind keys, long press, color keys | Premium | Researched | Free key mapping |
| H12 | Gesture configuration | Touch zones, swipes, two-finger | Premium | Researched | Free gestures |
| H13 | Backup/restore | Code/account/server/file, AES password | Expiry, Premium account | Researched | Encrypted local + self-hosted |
| H14 | Startup on boot | Boot/wake autostart | Premium, Android limits | Researched | Optional, permission-safe |
| H15 | Advanced network | Proxy, DoH, VPN check, wait-for-network | Premium for proxy/DoH | Researched | Network diagnostics dashboard |
| H16 | Provider diagnostics | Reload reports, HTTP errors, status | Provider-dependent | Researched | Health scores and exports |

## Screens required by the directive (status summary)

All 35 screens from Part 5 are inventoried in `Screen-Inventory.md`. Status summary: Welcome/setup, Add provider, Provider management/properties, Playlist import, Live TV home, Channel categories/browser, Full-screen player, EPG timeline, Program details, Movies/movie details, Series/season/episode, Continue watching, Favorites, History, Search, Recording manager, Radio, Sports center, Profiles, Parental controls, Remote settings, Appearance, Network, Backup, Diagnostics, Settings are **Designed** or **In development** based on the current app; only Channel browser, Favorites, Guide list, Search, Settings dialog, Player overlay, Recordings list, and main navigation have concrete implementations today.

## Acceptance checklist policy

A feature moves to `Verified` only after: implementation exists, automated tests cover the primary path, emulator/TV-device check is recorded, privacy/security review is complete, and limitations are documented. Nothing in this matrix is marked `Verified` at Stage 1.
