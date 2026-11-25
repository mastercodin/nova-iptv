# Development Roadmap (Stage 1 plan, awaiting approval)

Status of this document: **Designed**. No further implementation is authorized until the user approves the start of implementation.

## Stage status legend

`Not started` / `In progress` / `Done` / `Blocked`. Stage 1 is done; Stage 2 is done; earlier prototype work exists but is not considered formal execution of later stages until approved.

| Stage | Name | Deliverables | Exit criteria | Status |
|---|---|---|---|---|
| 1 | Internet research & competitor matrix | Competitor analysis, source list, 86-entry changelog review, feature matrix | Every Part 2 feature has a row; no invented claims | **Done** |
| 2 | PRD & architecture | Product requirements, architecture, screen inventory, this roadmap | Module boundaries defined; legal constraints recorded | **Done** |
| 3 | Database schema & provider abstraction | Room entities/DAOs, Provider interface, format detector, Keystore store | Schema migration test; parser/provider contracts unit-tested | Not started |
| 4 | Playlist import & provider management | M3U/OTC/XSPF/Enigma2/TXT adapters; XC/Stalker clients; add/edit/reload screens; diagnostics | Valid fixtures import; bad fixtures produce errors; credentials encrypted | Not started (prototype has basic M3U/settings) |
| 5 | Channel browsing & organization | Virtualized browser, categories, tags, favorites, history, dedupe, sort/filter, quick switch | 20k-channel test; persistence after restart; TV focus map | Not started (prototype subset exists) |
| 6 | Native playback engine | Media3 abstraction, HLS/DASH/TS, tracks, subtitles, aspect/zoom, buffer profiles, retry, stats overlay | Test streams play; error policy test; state survives rotation | Not started (prototype basic player) |
| 7 | EPG engine | Streaming XMLTV ingest, multi-source matching, manual mapping, timeline, now/next, search, reminders | Fixture EPG loads; matching report; timezone correction | Not started (prototype parser only) |
| 8 | Movies, series & VOD | OTV/XC/Stalker/user sources, posters, seasons/episodes, continue watching, watchlist, TMDB option | OTV fixture renders; resume works | Not started |
| 9 | Advanced player features | PiP, Studio/multi-view, audio swap, AFR, external subtitles, OpenSubtitles, screen-off audio | Connection guard tested; PiP on emulator; AFR on TV hardware | Not started |
| 10 | Recording, PiP & multi-screen | Scheduler, storage backends, statuses, recovery, in-progress playback | WorkManager job fires; storage failure recovery; status persistence | Not started (prototype scheduling UI) |
| 11 | Profiles, parental & customization | Profiles/PIN/biometric, child mode, themes/wallpapers/layouts, remote keys/gestures, backup/restore | Isolated profiles; PIN blocks; backup round-trip | Not started |
| 12 | AI & smart features | Local NL search parsing, duplicate scoring, EPG match suggestions, smart categorization, health dashboard, automation rules | Deterministic local tests; no cloud dependency by default | Not started |
| 13 | QA, performance & security | Unit/integration/UI tests, screenshot tests, parser fuzzing, low-memory profile, security review, physical TV/box test, docs | All `Tested` features become `Verified`; acceptance criteria met | Not started |

## Proposed execution order within each stage

1. Write/update technical design and test plan.
2. Implement behind interfaces with feature toggles.
3. Add unit tests for pure logic.
4. Add integration tests with local fixtures.
5. Add Compose previews/screenshot tests for TV + phone.
6. Run `assembleDebug`, `testDebugUnitTest`, lint/static checks.
7. Install on emulator via REA MCP and capture screenshots.
8. Update `Feature-Parity-Matrix.md` statuses honestly.
9. Document limitations and unknowns.

## Risks and mitigations

| Risk | Mitigation |
|---|---|
| Provider APIs vary widely | Adapter interfaces + fixture-driven tests; mark provider-dependent features honestly |
| TV focus traps | Dedicated TV navigation tests and focus maps per screen |
| Large playlists/EPG on low-memory TVs | Streaming parsers, pagination, low-memory profile, bounded caches |
| DRM/device limitations | Report unsupported clearly; never circumvent; device capability probe |
| Credential leakage | Keystore, redaction tests, no logging of secrets, explicit backup consent |
| Premium/paywall temptation | No license checks, no account activation code; all features open-source |

## Definition of done for a feature

- Code merged into the independent module.
- Unit/integration/UI tests present and passing.
- TV and phone screens documented.
- Error states implemented and tested.
- Security/privacy review passed.
- Limitations recorded.
- Status updated from `Implemented`/`Tested` to `Verified` only after device check.

## Approval gate

Implementation of Stages 3–13 starts only after explicit user approval of these documents.
