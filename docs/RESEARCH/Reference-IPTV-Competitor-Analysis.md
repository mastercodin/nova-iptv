# the reference commercial IPTV player — Competitor Analysis (Stage 1)

Status: **Researched**. No application code was written in this stage.

## 1. Research method and sources

Primary sources actually opened:

- Official FAQ index: `https://faq.the vendor FAQ/`
- Official changelog: `https://faq.the vendor FAQ/changelog`
- 80+ individual FAQ articles under `/basics`, `/providers`, `/connection`, `/playback`, `/features`, `/recording`, `/premium`, `/business`, `/restrictions`, `/formats`, `/about`
- Google Play listing supplied in the directive: `https://play.google.com/store/apps/details?id=com.vendor accountigator.iptvnavigator`

Notes:

- The FAQ site publishes `robots.txt` with `use=reference, ai-train=no`. This document uses the pages strictly as a human-readable reference for feature inventory, not as training data and not as source code.
- The Play listing resolves to an app named **the reference commercial IPTV player IPTV** with package `com.vendor accountigator.iptvnavigator`; the FAQ download page describes additional builds (standalone, Aptoide, Google Play TV/phone, Huawei, VIVO). The standalone package observed in the local APK is `studio.scillarium.vendor accountigator`. The docs and listing are treated as the public feature description; the exact app-store package naming is noted as an ambiguity rather than assumed.
- The complete changelog was parsed from 1.7.6.1 (2026-Nov-8) back to 1.5.7.1 (2020-01-06): **86 dated releases**, plus major-version groupings. Every dated entry was inspected for features not in the main description.
- No proprietary source code, APK code, assets, logos, or UI screenshots were copied into the project.

Extracted article texts are kept outside version control at `/tmp/opencode/faq/*.txt`; the parsed changelog is at `/tmp/opencode/changelog.txt`.

## 2. Product overview

the reference commercial IPTV player is an IPTV **player**, not a content provider. The user supplies a playlist, provider account, or portal. It supports Android TV / Google TV, Fire TV, Android boxes, phones and tablets; no native WebOS/Tizen app, and no desktop app (desktop use is via emulator/Waydroid). Minimum Android varies by build: standalone 5.0+, Play TV 8.0+, Play phone 9.0+; some newer features require 6.0+.

Core promise: bring your own authorized playlist/provider; the app supplies navigation, EPG, playback, organization and optional advanced features.

## 3. Observed feature domains

### 3.1 Provider engine

- Provider templates: **Playlist**, **Xtream Codes**, **MAC Portal**, **Stalker Portal**. Xtream accepts server URL + username/password or a full `get.php` link. MAC/Stalker accept portal URL, MAC, optional login/password and device attributes (`device_id`, `sn`, `device_id2`, `stb_type`, `hw_version`, `signature`, app/portal versions, User-Agent).
- Playlist formats: M3U/M3U8 (richest support), OTC JSON (`.otc`/`otc.json`), XSPF, LG WebOS webtv XML, Samsung Tizen XML, Enigma2 `.tv`, plain `.txt`, XML list-of-playlists, and generic fallback to M3U.
- M3U coverage: `#EXTM3U` header attributes `url-tvg`, `url-logo`, `catchup*`, `refresh`, `refresh_at`, `max-conn`, `billed-till`, `billed-msg`, `vod_library`; `#EXTINF` attributes `tvg-id`, `tvg-name`, `tvg-logo`, `group-title`, `ch-number`, `type`, `adult`, `parent-code`, `tvg-shift`, `codec`, `audio-track`, `video-track`, blacklists, `seek_preview`, event times; `#EXTGRP`, `#EXTHTTP`, `#EXTVLCOPT`, `#KODIPROP`, `#EXTATTRFROMURL`, nested playlists.
- Provider properties: per-provider codec, stream technology (HLS/MPEG-TS), expiration display, manual-only updates, reload interval, enabled modules, category filtering/renaming, catch-up overrides, API/user-agent/content user-agent, Referer, custom headers, max connections (1–20), preview disabling, plain list/alphabetical order, up to 5 EPG sources, EPG time shift.
- Multiple providers: several active at once (Premium), provider priority via name/number, merge/split/prefix/suffix categories, quick provider switch, duplicate folding with source auto-fallback, preferred suffix lists, show provider name, allow-access per profile, non-editable providers.
- Organization: tags as virtual categories, custom logos via URL zip pack or per-channel pick, mass tagging/renaming/hiding/moving, channel numbers, sort modes, duplicate detection, recently-added/new categories.
- Refresh: playlist-declared `refresh`/`refresh_at`, app-wide list update intervals, manual reload, saved-copy mode, cache reuse when provider is down, EPG reload on new channels.

### 3.2 EPG engine

- Sources: provider XMLTV (plain/gzip/xz detected by content), Xtream Codes API, Stalker API, user global XMLTV links, built-in fallback source, up to 5 per-provider sources (Premium), several global sources (Premium).
- Matching: `tvg-id` exact/case-insensitive fallback for provider sources; `tvg-name`/name for all sources; manual mapping with source selection and best matches; match-mode configuration (`tvg-id OR tvg-name`, AND, etc.).
- Fields read: `display-name`, `icon`, `title`, `sub-title`, `desc`, `category`, `actor`, `director`, `date`, multi-language picks, `episode-num` (TMDB/IMDb ids), `rating`, `star-rating`, `catchup-id` for archive lookup.
- Views/settings: now/next, timeline/timetable, current/next in lists, reminders, auto-switch at start (Premium), time-zone correction per channel/provider/app, internal vs displayed clock, max past days, cache-first download, on-demand per-channel EPG for XC/Stalker, EPG module toggle, clear data, reload report with per-source status and "guide until".
- Errors: no source, no channel matched, outdated data, no programmes covering now, server error, wrong time.

### 3.3 Playback engine

- Decoders: hardware ExoPlayer (default), system VideoView, software VLC (3.7.7 in 1.7.6.1), legacy ExoPlayer v1, external players (MX/VLC/Vimu/Nova/manual). Per-channel, per-provider, and app defaults; user choice wins; optional "do not remember last codec".
- Formats: HLS, DASH (incl. multi-period/server-side ads), MPEG-TS, Smooth Streaming (ISM), RTSP, RTP/UDP, some Acestream, DRM (Widevine/PlayReady/ClearKey, cbcs/cenc, multi-key, `license_data`, `#KODIPROP`).
- Tracks: audio/subtitle selection, preferred language lists, subtitle style, software audio decoder for 5.1 volume, audio shift ±5s hardware (Premium), track blacklists, bitrate preference, auto video selection by resolution, tunneling.
- Picture: fit/fill-width/fill-screen/remove-bars/stretch/16:9, zoom 50–150%, separate X/Y scaling (Premium), deinterlacing via VLC, AFR with mode exclusions and 59.94≈60 handling (Premium), fixed screen mode, night mode, overscan margins.
- Recovery: configurable retries 1–10/always/manual/instant switch, long-buffering-as-error, retry delay, skip broken channels in quick switch for N minutes, source auto-switch, switch codec, external player fallback.
- Buffer: default ~15s, 30/60/~240s, no-buffer low-memory/proxy mode, separate VOD buffer.
- Player UI: OK/tap menu, customizable action rows/buttons, recent channels (5s threshold), seek bar with preview thumbnails, sleep timer, brightness/volume, channel/programme management, bindable remote/touch actions, HUD variants, screen-off audio (Premium), system PiP (Premium, Android 8+).

### 3.4 Catch-up, DVR and recording

- Catch-up types: `default`, `append`, `shift`, `archive`, `timeshift`, Flussonic HLS/DASH/TS, XC timeshift API; template variables with offsets; `catchup-id` from EPG.
- DVR window: rewind live HLS/DASH with ≥5-minute sliding window when no provider archive; toggleable module (Premium).
- Recording (Premium): record current channel until programme end or fixed duration, scheduled time up to 7 days ahead, EPG-based with start-earlier/end-later, save past show from archive. Storage: internal, USB/SD, chosen folder, SMB, WebDAV; <4GB parts for FAT32; free-space guard and old-recording deletion; segmented upload with retry; recording statuses (Recording/Missed/Interrupted/Partly recorded), connection accounting and errors.

### 3.5 VOD / media library

- Sources: provider movies/series, XC/Stalker, `vod_library` in M3U/OTC, user-provided JSON/M3U/OTV/XML, DLNA/UPnP scanning, SMB/WebDAV folders (Premium), local single stream links.
- OTV format: movies/series/TV shows, categories with icon/background, seasons/episodes/parts, multiple videos per title, subtitles files, posters/backdrops/plot/cast/director/country/genre/rating/year/duration/added/TTL/adult/trailer/TMDB/IMDb, base64 and localized fields, runtime attribute URLs, DRM/headers.
- UI: Home with Continue watching/favorites/latest/top rated/recommendations/recordings/new episodes, Movies, Series, All, From TV, Recordings, Favorites, Folders; lazy loading for large categories; search/filter/sort; watched positions; next-episode handling; user TMDB key for metadata.

### 3.6 Advanced viewing

- Studio mode: up to 20 tiles, layouts, maximize sizes, memory slots, archives/audio switching; PiP: channel-in-channel, swap main/PiP, swap audio, system PiP; both Premium and connection-limited.
- Play on TV (Premium, since 1.7.5.2): phone/tablet casts live/archive/VOD to Chromecast or DLNA/UPnP TV; phone proxies the stream so provider headers/tokens are preserved; no DRM; TV must support codec; notification controls; TV follows channel/episode changes.
- Android TV home integration: Play Next/Continue watching, favorite/recent rows, published items; DLNA/UPnP also used for media library.
- Radio page (1.7.6.1): provider radio or `type=radio`/Radio category; prev/next/play/favorite/sleep timer/what-played/volume, background continuation (Premium), song art via MusicBrainz/Cover Art Archive, find song via YouTube Music.
- Sports center (1.7.6.1, Premium): games now/later/today/tomorrow from EPG and event-channel names, filter by sport, LIVE/+2/repeat channels, reminders/recording, multiview; no scores.
- Event channels: `event-start`/`event-end`/`expires` make PPV/event slots appear as programmes and disappear after the event.

### 3.7 Other features

- Multiple profiles (Premium): per-profile favorites, channel state, history, searches, VOD state, studio layouts, partial settings, provider access, PIN, profile selection at start.
- Parental/restrictions: child mode with allowlisted channels, 4-digit restrict code for settings/editing/app start, adult content code separate, per-channel lock, hidden provider details behind service code, unlock duration.
- Customization: 10+ themes (3 variants each), accent/text colors, wallpapers (built-in/random/URL), overscan margins, launcher buttons, home columns, list styles (compact/side-by-side/cards/tiny), custom categories, remote key binding, touch zones, gestures, player menu styles.
- Backup/restore: backup code (≥7 days), optional AES password, one-time code, account backups per device, own HTTP server API, local file (standalone/expert), selective providers/settings/metadata, merge providers, placeholder `?` credentials.
- Network: app-wide/provider/channel User-Agent and Referer, custom headers, HTTP basic auth in URLs, SSL-invalid toggle, in-app HTTP/SOCKS5 proxy, DNS-over-HTTPS, VPN check, wait-for-network, billed messages/expiry display, provider diagnostics/status, IP display.
- Autostart on boot/wake (Premium, TV/box/tablet), auto-open last channel/live/radio (Premium), launcher mode.

### 3.8 Premium restrictions (competitor)

Premium gates: recording and Recordings section; Play on TV; PiP/Studio; AFR; several EPG sources and global sources; several providers active; duplicate folding/list cleanups; channel/category management; tags; profiles and parental locks; custom remote/touch actions; themes/logo packs/URL wallpaper; proxy/DoH; TMDB key/OpenSubtitles; boot autostart; VOD playback speed/sleep/night mode; video preview; track-by-bitrate/resolution; certain player menu styles. Free: single provider live channels, EPG, archive, media library browsing.

**Our open-source strategy:** implement generic capabilities without a paywall, license checks, or account activation. No premium bypass in the competitor's product is attempted.

## 4. Changelog-derived features not obvious from the main description

Inspected all 86 dated entries. High-signal additions by era:

- 1.7.6.x: radio page, sports center, screen-off sound, Android TV home items, event channels (`event-start/end/expires`), VOD subtitle files, per-provider manual playlist copy, pinch-to-fill, 59.94≈60 AFR, subtitle shift, VLC 3.7.7, multi-period DASH, Huawei account.
- 1.7.5.x: full recording subsystem with SMB/WebDAV/SD/USB, DoH, proxy, logo packs, DVR toggle, ±5s audio delay, seek previews, ClearKey/cbcs, Stalker device selection, tablet split-screen, archive merged into media library, on-demand XC/Stalker EPG, cache-first EPG.
- 1.7.4.x: mass tagging, subtitle toggle action, archive gaps, audio shift on hardware, user TMDB key, `tvg-serie`/`serie-title`, CENC/DRM, basic live DVR window, ad-break streams, Adult code separation, track blacklists, precise catchup template offsets, expanded TXT format, embedded EPG icons, Stalker batch EPG.
- 1.7.3.x: auto-retry switch-to-next, Google Assistant global search, Stalker radio/series search, EPG module toggle, overscan in percent, OpenSubtitles, episode switching, country detection, media-library module on phones, VLC sync mode, seek preview, bitrate estimation.
- 1.7.2.x: top-rated/random VOD views, vertical phone player, `tvg-season/episode`, XC archive API variants, CC style, home launcher style, deep-search toggle, swipe directions, focused Studio size.
- 1.7.1.x: preferred media language, cache speed-up, grouping countries, duplicate folding scope change, HTTP-auth stream links, precise archive template shifts, recent-channel clearing, fallback sort mode, VOD continue watching, provider order for user libraries.
- 1.6.9.x–1.6.6.x: profiles, RTSP, OTC/OTV base64/localization, SVG icons, `#EXTATTRFROMURL`, up to 3/5 EPG sources, mass channel actions, track black/whitelists, Studio up to 20 screens, backup encryption, DLNA improvements, search revamp, AFR, player bindable actions, wallpapers/themes, channel numbers to 10000.
- 1.6.5.x–1.5.7.x: side-by-side lists, provider-expiry display, quick provider switch, auto-number favorites, channel numbers, multiple themes, search optimization, Android TV rows/previews, DASH/Flussonic archive, time-warp mode, XC v1 API, sleep timers, remote key configuration, duplicate folding.

## 5. Error states and diagnostics observed

`403/401` subscription/key/IP/connection-limit/header problems; `404/405/410/500/502/503` stream gone/server down; `cannot connect`; ENOENT; SSL hostname/chain/anchor errors; no matching EPG; outdated EPG; no programmes now; playlist failed/no channels; provider expired; not enough connections for PiP/Studio/recording; recording Missed/Interrupted/Partly recorded; DRM unsupported by device; no space on recording storage; network/VPN not ready; provider refuses too many connections; wrong device time; overscan cut-off; unsupported TV codec/cast format.

## 6. Test-case seeds (from documented workflows)

1. Add XC provider with URL/user/pass and verify channel/VOD counts.
2. Add MAC portal with registered MAC and custom `device_id`.
3. Import M3U with BOM, nested playlist, catchup, `#EXTHTTP`, DRM keys.
4. Load gzip/xz XMLTV, verify source report and per-channel mapping.
5. Shift EPG per channel/provider/app and verify now/next.
6. Fold duplicate channels with suffix preference and force source fallback.
7. Play HLS/DASH/TS/RTSP; switch codec; select audio/subtitle; set zoom/AFR.
8. Trigger 403 and verify retry/fallback/skip-broken behaviour.
9. Schedule EPG recording with start/end padding; store to SMB/WebDAV; verify statuses.
10. Create two profiles with PIN and separate favorites/history.
11. Backup with password, restore with placeholder credentials.
12. Cast live/VOD from phone to DLNA/Chromecast and verify header preservation.
13. Verify Studio/PiP connection-limit guard and audio swap.
14. Verify radio page, sleep timer, and screen-off audio.
15. Verify sports center detection from EPG and event-channel names.

## 7. Our improved version (design intent)

- Same generic capabilities, original implementation, no copied code/assets/branding, no premium/licensing gate.
- Local-first: playlist credentials never leave the device unless the user explicitly configures a self-hosted backup endpoint.
- Unified provider abstraction with health scores, automatic dead-stream detection, clear diagnostics dashboard.
- Faster TV navigation: cached indexes, deterministic focus, no deep modal stacks for zapping.
- Better EPG: real timeline, smart matching suggestions, timezone sanity checks, program-to-archive linkage.
- Better playback: adaptive recovery policy, per-channel codec memory, stream statistics overlay, AFR/buffer profiles.
- Powerful global search plus optional local natural-language parsing (on-device, no cloud dependency by default).
- Profiles, parental controls and customized dashboards implemented from day one, not as premium extras.
- Accessibility and low-memory TV profiles as first-class settings.

## 8. Sources

- `https://faq.the vendor FAQ/` and all individual article URLs listed in `/tmp/opencode/fetch_faq.py`
- `https://faq.the vendor FAQ/changelog`
- `https://play.google.com/store/apps/details?id=com.vendor accountigator.iptvnavigator`
- `https://faq.the vendor FAQ/about/downloads`
- `https://faq.the vendor FAQ/premium/what-premium-unlocks`
