# Provider Suite Supplement (addendum to B/C/F)

Status: **Designed**. This addendum makes the provider requirement explicit; it does not replace earlier documents.

The user requirement: NOVA must support the full suite of provider options found in the reference commercial IPTV player:

- Direct Stream Support (universal)
- M3U / M3U8 playlists
- Xtream Codes API
- MAC Portals / Stalker Portals
- Local playlist files
- Cesbo Astra
- Configuration backup loading
- Common local templates and more

## 1. Template registry

`ProviderTemplateRegistry` ships built-in templates and allows user-defined local templates (declarative JSON only):

| Template | Input fields | Capabilities | Evidence |
|---|---|---|---|
| Direct Stream | URL, name, headers, User-Agent, Referer, codec, optional EPG id | Universal single-channel/playback | the reference commercial IPTV player M3U accepts direct http/https/udp/rtp/rtsp/rtmp URLs |
| Playlist (M3U/M3U8) | URL or file, refresh, headers | Channels, EPG link, catch-up, VOD link | the reference commercial IPTV player format docs |
| Xtream Codes | Server URL, login, password, API v1/v2 | Live, VOD, series, EPG, archive, expiry | the reference commercial IPTV player provider docs |
| MAC Portal | Portal URL, MAC | Live/VOD via portal | the reference commercial IPTV player provider docs |
| Stalker Portal | Portal URL, MAC, login/password, device fields | Live/VOD/EPG | the reference commercial IPTV player provider docs |
| Local playlist file | SAF/file picker | One-time local channels | the reference commercial IPTV player notes file limitations |
| Cesbo Astra | Base URL/port, HTTPS, playlist path, token, groups, EPG | M3U8/XSPF channels, XMLTV, HTTP TS/HLS | Cesbo Astra public docs |
| Backup import | Backup code/file/URL | Restore providers/settings/channel state | the reference commercial IPTV player backup docs; NOVA uses its own encrypted format |

Template detection mirrors the reference commercial IPTV player's helpful behaviour: `get.php?username=...&password=...` suggests Xtream; `/c/00:1A:79:...` suggests MAC; `#EXTM3U` suggests Playlist.

## 2. Direct Stream Support (universal)

A universal "Direct Stream" provider accepts any authorized single stream URL and stores: display name, URL, optional category, logo, EPG id, HTTP headers, User-Agent, Referer, preferred codec, and catch-up/DVR hints. It is the escape hatch for arbitrary authorized HTTP(S)/UDP/RTP/RTSP/RTMP sources and for streams resolved by add-ons.

## 3. Cesbo Astra integration

Public documentation observed:

- HTTP Play is disabled by default; enable `Allow HTTP access` and/or `Allow HLS access` in Astra settings.
- Default playlist: `http://example.com:8000/playlist.m3u8`; path/port/name are configurable; token can be appended (`?token=xxx`).
- Playlist formats: M3U8 and XSPF; groups via `Settings → Groups` and `Playlist Arrange`.
- Channel logos served from configured path; `url-tvg` can be placed in the M3U header for XMLTV; channels linked by XMLTV Channel ID.
- Channel stream URLs are HTTP MPEG-TS (`/play/{id}`) or HLS (`.../index.m3u8`).
- Authorization: token query parameter, IP, expire, conlimit, packages; securetoken algorithm uses SHA1 over channel id, client IP, start/end times, securetoken and salt.
- Control API: `POST /control/` with `cmd` JSON or path entry `/api/...`; Basic/Bearer/cookie auth; roles 1 control, 2 observer, 3 streaming-only; `GET /api/config`, `stream-info`, `stream-status`, categories.

NOVA's Astra adapter will use only documented endpoints, keep credentials local, prefer observer/read-only calls for status, and never implement token generation beyond documented user-provided tokens/securetoken inputs.

## 4. Configuration backup loading

- the reference commercial IPTV player documents backup by code, account, own server (`POST` plain body; `GET` restore) and local file, with optional encryption and selective restore.
- NOVA supports its own encrypted backup format: file, code, or user-chosen URL; selective restore (providers/settings/channel state/media library); merge-providers mode; placeholder `?` credentials.
- **Compatibility statement:** we do not claim to parse proprietary/undocumented backup formats. Interoperability requires a published format; otherwise users migrate by re-adding providers or using NOVA exports.

## 5. Common local templates

Templates are declarative JSON presets covering common provider shapes, stored locally and editable by advanced users. They map input fields to API calls/headers/parsers. No template may contain secrets by default.

## 6. Database impact

- `provider_templates`: id, name, kind, schema_json, builtin, enabled.
- `provider_configs`: provider_id, template_id, encrypted_config_json.
- `backup_imports`: id, source_type, digest, restored_at, status.

## 7. EPG integration with the provider suite

The EPG subsystem must consume guide data from every provider type above:

- Direct Stream: optional `epg_id` on the universal stream entry.
- M3U/M3U8: `url-tvg` header, `tvg-id`, `tvg-name`, `tvg-shift`.
- Xtream Codes: provider EPG API and per-channel EPG.
- MAC/Stalker: portal EPG, device-id-sensitive.
- Local files: local XMLTV alongside local playlists.
- Cesbo Astra: `url-tvg` in HTTP Play playlist, XMLTV Channel ID on Astra channels.
- Configuration backup: restored provider EPG settings.
- Local templates: preset EPG source patterns.

### User-selectable default EPG sources

NOVA ships three optional, user-disableable default XMLTV sources:

| Name | URL | Notes |
|---|---|---|
| ALL OTT EPG | `https://avkb.short.gy/epg.xml.gz` | Third-party; not owned/warranted by NOVA |
| JioTV EPG | `https://avkb.short.gy/jioepg.xml.gz` | Third-party; not owned/warranted by NOVA |
| TataPlay EPG | `https://avkb.short.gy/tsepg.xml.gz` | Third-party; not owned/warranted by NOVA |

All three were verified as reachable gzip XMLTV responses at planning time. Users may disable these defaults and provide authorized sources instead. Failures are reported per source.

## 8. Acceptance criteria

- Each listed provider template loads a synthetic fixture.
- Astra playlist with groups/logos/url-tvg produces channels and EPG link.
- Direct stream URL with custom headers plays and redacts secrets in logs.
- Local file import reports Android TV limitations gracefully.
- Backup import round-trips settings and provider metadata without leaking credentials.
