# H. NOVA UI Screen Inventory (add-on ecosystem)

Status: **Designed**. This extends the original 35-screen inventory; all new screens are TV-first then adapted to touch.

| # | Screen | Purpose | TV-first rules | Mobile adaptation | Status |
|---|---|---|---|---|---|
| N1 | Add-on home | Featured, trending, categories, installed | Rows with focus memory | Vertical feed | Designed |
| N2 | Featured add-ons | Editorial/verified featured list | Hero row | Cards | Designed |
| N3 | Trending add-ons | Publicly sourced trending only; no invented counts | Row + detail | List | Designed |
| N4 | IPTV add-ons | Provider-type add-ons | Category filter | List | Designed |
| N5 | Movies & series add-ons | Catalog add-ons | Poster/card rows | Grid | Designed |
| N6 | Anime add-ons | Anime catalogs | Genre chips | Grid | Designed |
| N7 | Metadata add-ons | TMDB/TVDB/Fanart/etc. | Toggle rows | List | Designed |
| N8 | Subtitle add-ons | OpenSubtitles/local subtitle providers | Language chips | List | Designed |
| N9 | Media server integrations | Jellyfin/Plex/Emby/Kodi/DLNA | Status cards | List | Designed |
| N10 | Installed add-ons | Enable/disable/configure/update/uninstall | Focus on primary action | List | Designed |
| N11 | Add-on details | Description, developer, version, platforms, capabilities, permissions, dependencies, docs | Read-only panels + install | Detail | Designed |
| N12 | Add-on settings | Per-add-on config, credentials, language/region | Form with expert level | Form | Designed |
| N13 | Repository management | Add/verify/sync/remove repos | URL entry + status | Form | Designed |
| N14 | Add-on update manager | Available updates, version pinning, compatibility | Table with focus | List | Designed |
| N15 | Universal home | Continue Watching, Live TV, Trending, Anime, New Releases, Recommendations, Recently Added, Favorites, Providers, Add-ons, Local Media, Connected Servers | Reorderable rows, no hidden focus traps | Dashboard editor | Designed |
| N16 | Universal library | Movies, Series, Anime, Documentaries, Personal videos, collections, watchlists | Sidebar categories | Tabs | Designed |
| N17 | Unified search | All sources, filters, natural-language input | Results grouped by type, first result focused | Search UI | Designed |
| N18 | Playback source manager | Authorized sources with provider/language/subtitle/status | Focusable source list, no false claims | Bottom sheet | Designed |
| N19 | Universal watchlist | Cross-provider watchlist | Row + manager | List | Designed |
| N20 | Universal watch history | Cross-provider history | Row + manager | List | Designed |
| N21 | Provider health dashboard | Provider/add-on health, latency, errors, last success | Read-only dashboard | Detail list | Designed |
| N22 | Add-on performance monitor | Response times, error budgets, circuit state | Dashboard | Detail list | Designed |
| N23 | Metadata correction | Manual match/correct metadata | Two-pane compare | Form | Designed |
| N24 | Custom collections editor | Name, artwork, filters, rules | Grid + rule builder | Form | Designed |
| N25 | AI assistant | Natural-language discovery with privacy notice | Text entry + suggestions | Chat-like panel | Designed |
| N26 | Profiles & permissions | Per-profile add-on access, parental controls | Profile rail | Grid | Designed |
| N27 | Developer tools | Manifest validator, compatibility validator, test harness | Expert form | Form | Designed |

## Navigation rules

- Add-on installation and permission screens must be reachable and operable with D-pad only.
- Permission changes require explicit confirmation; no silent grant.
- Every dashboard section can be hidden/reordered; focus order updates accordingly.
- Source manager must not imply unverified resolution/language/subtitle availability.
