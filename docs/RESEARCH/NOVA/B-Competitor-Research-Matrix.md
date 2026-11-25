# B. Competitor Research Matrix — NOVA add-on ecosystem

Status: **Researched**. Sources are public documentation only; no proprietary code, branding, protected assets or private APIs were used.

| Project | Publicly documented capability | Evidence/source | Relevant design lesson | NOVA status |
|---|---|---|---|---|
| Stremio | Add-on protocol over HTTP/IPFS/legacy; resources `catalog`, `meta`, `stream`, `subtitles`, `player`, `library`; manifest with `id`, `version`, `name`, `types`, `catalogs`, `resources`, `idPrefixes`; routes `/{resource}/{type}/{id}.json`; CORS required | stremio-addon-sdk docs and protocol.md | Small declarative capability surface; ID-prefix routing; event resources are privacy-sensitive | Designed — Stremio-compatible HTTP adapter |
| Stremio | Native EPG for live TV add-ons via `behaviorHints.epgProvider` + `tv` catalog with `date` extra and `metasDetailed` | protocol.md | Live TV can be an add-on, not only a built-in module | Designed |
| Cloudstream | Extension developer template, provider creation, JSON repositories, scraping guide | recloudstream.github.io/csdocs | Repository + independent provider modules; JSON repo metadata | Designed — inspired adapter, not bytecode compatible |
| Nuvio | Client-side playback interface integrating the Stremio add-on ecosystem; unified discovery | NuvioTV GitHub description | Add-on ecosystem and discovery can coexist with IPTV | Designed |
| NuvioPlusIPTV | Live TV guide, multiview up to 4, sports hub, calendar, catch-up, recordings, provider VOD, VPN, mobile/TV account sync | GitHub README/release notes | Multiview and sports aggregation are expected in universal apps | Researched |
| Nuvio | Public REST/RPC API for profiles, add-ons, library, watch data (`api.nuvio.tv`) | nuvio.tv/docs | Optional account sync should be a documented API, not embedded secret | Designed |
| Kodi | Add-on types: plugin sources, repositories, scrapers, scripts, subtitle scripts, services; Python libraries; JSON-RPC API | kodi.wiki Add-on development | Clear add-on taxonomy and repo model; avoid executing untrusted Python | Researched |
| Plex | Media server REST APIs with user tokens | Plex developer docs | Library/auth model for server integrations | Researched |
| Jellyfin | REST API, user tokens, library/continue-watching endpoints | Jellyfin API docs (site blocked in this environment; capability confirmed via Sparkle/Nuvio docs) | Open-source server integration target | Researched |
| Emby | REST API reference, multi-language SDK, plugin API | dev.emby.media | Mature SDK pattern; use documented REST only | Researched |
| VLC | Network streams, broad codec/subtitle support | VLC public documentation | Playback flexibility reference, not an add-on system | Researched |
| Sparkle TV | Multi-source PVR: m3u/xtream/xmltv, HDHomeRun, Enigma2, Jellyfin, Plex, Stalker, Tvheadend, TVMosaic, DVBLink/Viewer; EPG, AFR, subtitles, multiview, DVR, themes, parental, remote customization | Google Play / official site | Universal app must unify many PVR sources and keep TV-first control | Researched |
| the reference commercial IPTV player | Provider/EPG/playback/recording/VOD/profiles feature set (previous research) | faq.the vendor FAQ | Strong IPTV baseline already captured in prior docs | Researched |
| Cesbo Astra | HTTP Play M3U8/XSPF playlists, groups, logos, `url-tvg` XMLTV, token/IP/expire/conlimit auth, control API with roles | astra.cesbo.com public docs | Astra is a documented provider type; adapter can be built independently | Researched |

## No invented claims

Ratings, popularity, verification badges and compatibility are not invented. Where public docs were inaccessible (Jellyfin API site returned 403), capability is recorded as researched via other public sources and marked for confirmation during integration.

## Design conclusions

1. A small capability protocol (catalog/meta/stream/subtitles + provider/media-server variants) covers most ecosystem patterns.
2. ID-prefix routing enables cross-add-on metadata reuse without copying provider data.
3. Event resources (`player`, `library`) must be opt-in for privacy.
4. Repository metadata and signed manifests are enough for a safe marketplace; arbitrary scripts are not.
5. Media server integrations should be first-class capabilities, not afterthoughts.
