# Screen Inventory (Stage 2)

Status: **Designed**. "Current" describes what exists in the prototype today; nothing here is a claim of final verification.

Design rule: TV first — D-pad focus order, 10-foot typography, overscan-safe margins, no focus traps, deterministic back behaviour. Touch/mobile adapts the same state into vertical layouts and gestures.

| # | Screen | Purpose / key actions | TV-first requirements | Mobile adaptation | Data | Current status |
|---|---|---|---|---|---|---|
| 1 | Welcome & setup wizard | Choose language/theme, privacy choice, add first provider | Large buttons, single focus column, skip available | Vertical stepper | Settings | Designed |
| 2 | Add provider | Pick template, enter URL/credentials/MAC, connect test | Auto-advancing fields, QR code option | Form + scanner | Provider engine | Designed |
| 3 | Provider management | List providers, enable/disable, order, reload, status | Horizontal provider cards, priority controls | List + swipe | Provider entity | Designed |
| 4 | Provider properties | Codec, categories, archive, network, EPG sources, connections | Tabbed expert panel, focus memory | Scrollable sections | Provider config | Designed |
| 5 | Playlist import | URL or local file, format report, errors | File-picker fallback guidance | SAF picker | Parser | In development (local M3U import exists) |
| 6 | Live TV home | Categories, favorites, recent, continue watching | Grid rows, hero focus, quick provider switch | Vertical feed | Channel repo | Implemented (basic) |
| 7 | Channel categories | Category list with counts and filters | Left rail + content pane | Chips | Category repo | Implemented (basic chips) |
| 8 | Channel browser | Full channel grid/list, sort, search | Virtualized grid, channel numbers | Grid/list toggle | Channel repo | Tested |
| 9 | Full-screen player | Playback, HUD, menu, tracks, seek | OK opens menu, remote key mapping | Tap zones, gestures | PlayerEngine | Implemented (basic) |
| 10 | Player settings | Codec, audio/subtitle, aspect, AFR, buffer, sleep timer | Side panel, no nested deep stack | Bottom sheet | Player policy | Designed |
| 11 | EPG timeline | Horizontal timeline, now/next, day switch | D-pad grid, snapping to now | Vertical program list | EPG repo | Designed |
| 12 | Program details | Description, cast, rating, record/remind/catch-up | Modal with clear focus return | Full screen | Programme | Designed |
| 13 | Catch-up browser | Past programmes by channel/date | Date strip + program grid | List | EPG + archive | Designed |
| 14 | Movies | Movie rows/folders, filters, genres | Poster grid, remote-friendly paging | Grid | VOD repo | Designed |
| 15 | Movie details | Poster, metadata, play, watchlist, similar | Actions row, trailer | Full screen | VOD item | Designed |
| 16 | Series | Series rows/folders | Poster grid | Grid | VOD repo | Designed |
| 17 | Series details | Seasons, episodes, continue | Season tabs, episode grid | Detail screen | VOD item | Designed |
| 18 | Season & episode selection | Episode list, progress, parts | Focus memory per season | List | Season/Episode | Designed |
| 19 | Continue watching | Resume tiles with progress | Row on home + full screen | Row | History | Designed |
| 20 | Favorites | Favorite channels/VOD | Star toggle, list | List | Favorites | Tested (channels) |
| 21 | Watch history | Recently watched channels/VOD, clear | Row + manager | List | History | Implemented (recent chips) |
| 22 | Search | Global search channels/programs/VOD, voice, recent queries | Focus first result, category context | Search bar | Search engine | Tested (channels) |
| 23 | Studio mode | Multi-view grid, swap audio/main | Tile grid, per-tile focus | Grid/tablet | PlayerEngine | Designed |
| 24 | Picture-in-picture | In-app PiP and system PiP controls | Swap/size/close actions | Overlay | PlayerEngine | Designed |
| 25 | Recording manager | Scheduled/active/finished/failed, storage | Status tabs | List | Recording repo | Implemented (scheduling UI) |
| 26 | Radio | Station list, now playing, sleep timer, favorites | Full-screen ambient mode | Compact player | Radio source | Designed |
| 27 | Sports center | Games now/later/tomorrow, sport filters | Rows + live badges | List | EPG/event channels | Designed |
| 28 | User profiles | Add/switch/PIN profiles | Profile rail at start | Grid | Profile repo | Designed |
| 29 | Parental controls | Child mode, restrict code, adult code, locks | Expert mode with code challenge | Settings | Security | Designed |
| 30 | Remote-control settings | Key binding, long press, gestures | Visual remote diagram | List | Input config | Designed |
| 31 | Appearance settings | Themes, wallpapers, layouts, overscan | Live preview | Preview pane | Settings | Designed |
| 32 | Network settings | Proxy, DoH, VPN check, user-agent, SSL | Expert sections | Form | Network profile | Designed |
| 33 | Backup and restore | Code/account/file/self-hosted, encryption | Progress + confirm | Form | Backup service | Designed |
| 34 | Diagnostics | Provider health, stream stats, error history | Read-only dashboard | Detail list | Diagnostics | Designed |
| 35 | Application settings | General, player, provider data, EPG, about | Simple/normal/expert levels | Sections | Settings | Implemented (basic) |

## Navigation and accessibility rules

- Every screen exposes a primary focus target on entry.
- Back returns to the previous focus target, not the top of a list.
- No modal chain deeper than two without an explicit "home" action.
- Minimum focus scale and contrast are TV-safe; text has 10-foot sizing variant.
- Overscan margins are configurable and applied to all screens.
- Screen readers get labels for icons and state; captions/subtitles have accessible style controls.
