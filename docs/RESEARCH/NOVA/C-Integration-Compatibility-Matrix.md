# C. Supported Integration Compatibility Matrix

Status: **Designed**. "Planned" means intended adapter; no integration is implemented in this phase.

| Integration | Protocol / source | Auth | Capabilities mapped | Compatibility approach | Limitations | Status |
|---|---|---|---|---|---|---|
| Stremio add-on | HTTP `/manifest.json`, `/catalog`, `/meta`, `/stream`, `/subtitles`, optional EPG | Usually none or add-on-specific config/token in URL | Catalog, Metadata, Stream, Subtitles, optional Live EPG | Direct protocol adapter; manifest translated to NOVA manifest | CORS, add-on availability, content legality, no guarantee every add-on works on TV | Planned |
| Cloudstream extension | JSON repository + provider model | Provider-specific | Provider catalog/search/details/sources | Read repository manifest; implement NOVA provider interface; do not run Cloudstream bytecode | Not binary compatible; scraping may break | Planned |
| Nuvio API | Public REST/RPC for profiles/addons/library/watch | User token | Sync, add-on list, library, watch state | Optional sync service with explicit consent | Requires user account; privacy review | Planned |
| Jellyfin | REST API, user token/API key | User credentials/token | MediaServer library, streams, progress | Native integration adapter | Server version differences, local network | Planned |
| Plex | REST API, user token | User token | MediaServer library, streams, progress | Native integration adapter | Token handling, server discovery | Planned |
| Emby | REST API, SDK patterns | User token | MediaServer library, streams, progress | Native integration adapter | API versioning | Planned |
| Kodi | JSON-RPC for library | User-configured | MediaServer library/playback | Read-only library adapter by default | No Python add-on execution | Planned |
| DLNA / UPnP | Device discovery, content directory | None/local | Local media browsing/cast | Local network adapter | TV codec support, network isolation | Planned |
| SMB / WebDAV | Network folder | User credentials | Local media, recordings | Storage backend + media scanner | Secure storage of credentials, latency | Planned |
| OpenSubtitles | Authorized API | User account/key | Subtitle search/download | Subtitle add-on | Rate limits, terms of use, user consent | Planned |
| TMDB | Authorized API key | User-provided key | Metadata enrichment | Metadata add-on | Attribution, offline fallback | Planned |
| TVDB | Authorized API where permitted | User-provided key | Metadata enrichment | Metadata add-on | License/terms | Planned |
| Fanart | Public artwork API where permitted | None/key | Artwork | Metadata add-on | Attribution, caching policy | Planned |
| IMDb metadata links | Links only where permitted | None | Metadata links, not scraping | Link resolver | No unauthorized scraping | Planned |
| M3U/Xtream/Stalker/XMLTV | Existing NOVA provider parsers | Provider credentials | IPTV provider capability | Built-in provider modules | Provider variability, connection limits | Partially implemented |
| Local media | SAF / MediaStore | OS permissions | Local library | Built-in scanner | Storage permission changes | Planned |

## Provider-suite addendum (explicit requirement)

| Provider option | Protocol / input | Capabilities | Compatibility approach | Limitations | Status |
|---|---|---|---|---|---|
| Direct Stream (universal) | Single http/https/udp/rtp/rtsp/rtmp URL + headers | One channel/stream, codec preference, EPG id | Universal provider template | Only authorized streams; device codec limits | Designed |
| M3U / M3U8 | URL or local file | Channels, EPG, catch-up, VOD link | Built-in parser | Provider tag variants | Partially implemented |
| Xtream Codes API | Server + login/password, v1/v2 | Live, VOD, series, EPG, archive, expiry | Native adapter | Provider API variants | Researched |
| MAC Portal | Portal URL + MAC | Live/VOD via portal | Portal adapter | Device/MAC registration | Researched |
| Stalker Portal | Portal URL + MAC + login/password + device fields | Live/VOD/EPG | Portal adapter | Device fingerprinting | Researched |
| Local playlist files | SAF/file picker | One-time channels | Local file adapter | Android TV file access limits | Implemented (basic) |
| Cesbo Astra | HTTP Play `playlist.m3u8`/XSPF, tokens, XMLTV, control API | Channels/logos/groups/EPG/status | Documented Astra adapter | Token/auth rules, port isolation | Researched |
| Configuration backup loading | NOVA encrypted file/code/user URL | Restore providers/settings/state | Backup service | No claimed proprietary-format support | Designed |
| Common local templates | Declarative JSON presets | Provider setup shortcuts | Template registry | No secrets in presets | Designed |

## Rules

- Never mark resolution, audio language or subtitle availability as verified unless the add-on/source reported it or playback observed it.
- No DRM bypass, authentication circumvention or protected-stream extraction.
- Every integration declares permissions and is revocable.
