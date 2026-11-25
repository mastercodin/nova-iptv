# J. NOVA Development Roadmap — 12 milestones

Status: **Designed**. No milestone started; awaiting approval after these documents.

Prior Stage 3 approval is acknowledged. Because this NOVA directive requires planning first, execution now begins at Milestone 1 and proceeds milestone by milestone. Milestone 1 subsumes the prior Stage 3 provider-abstraction work; provider suite requirements are included in Milestones 1 and 10.

| Milestone | Name | Deliverables | Exit criteria | Status |
|---|---|---|---|---|
| 1 | Extension architecture | Module graph, capability interfaces, trust tiers, transport adapters scaffold | Interfaces compile; no circular deps; architecture doc updated | Not started |
| 2 | Add-on manifest & registry | Manifest schema, validator, repository registry, version pinning, signature checks | Validator test suite passes; bad manifests rejected | Not started |
| 3 | Add-on manager | Install/uninstall/enable/disable/configure/update/health/search/categories | Lifecycle + cleanup tests pass; TV D-pad flow works | Not started |
| 4 | Add-on marketplace | Marketplace screens, featured/trending/categories, details, settings, repo manager, update manager | Local repo fixture browsable; no invented ratings/badges | Not started |
| 5 | Metadata integrations | Metadata add-on interface, TMDB/TVDB/Fanart/links adapters, conflict resolution | Metadata merge/correction tests pass | Not started |
| 6 | Catalog integrations | Catalog/search/meta/stream/subtitles capabilities, Stremio-compatible adapter, provider extensions | Fake add-on + catalog contract tests pass | Not started |
| 7 | Global search | Unified search engine, filters, natural-language local parser | Cross-source search tests pass; privacy rules verified | Not started |
| 8 | Unified library | Unified movies/series/anime/docs/local, watchlists, history, continue watching, collections | State persistence and duplicate detection tests pass | Not started |
| 9 | Playback source manager | Source list, honest capability labels, user selection, fallback policy, stats | Honesty + recovery tests pass | Not started |
| 10 | Media server & provider suite | Jellyfin/Plex/Emby/Kodi/DLNA/SMB/WebDAV; Direct Stream, M3U, XTream, Stalker/MAC, local files, Cesbo Astra, backup loading, common templates | All provider templates import against mocks; credential redaction passes | Not started |
| 11 | Developer SDK | Manifest spec, interfaces, docs, sample extension, compatibility validator, test tools | Sample extension passes validator and contract tests | Not started |
| 12 | Security & compatibility testing | Sandbox, permissions, revocation, crash isolation, device matrix, performance, final QA | Full security suite + TV/phone matrix passes; no feature marked Verified without evidence | Not started |

## Milestone rules

1. Write technical design and tests before implementation.
2. Implement behind interfaces and feature flags.
3. Run unit/contract/integration/UI/security tests each milestone.
4. Update status docs honestly; no silent omissions.
5. Do not move to the next milestone with failing exit criteria.
6. Keep the previous NOVA IPTV requirements intact; this roadmap extends them.

## Approval gate

Start Milestone 1 only after approval of documents A–J plus the provider-suite supplement.
