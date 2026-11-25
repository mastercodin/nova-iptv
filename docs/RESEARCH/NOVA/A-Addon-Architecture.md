# A. NOVA Add-on Architecture

Status: **Designed**. No implementation in this phase.

## 1. Goal

Turn NOVA into an independent universal entertainment app with an extensible add-on ecosystem, without coupling the core to any single provider.

## 2. Add-on definition

An add-on is a **capability vendor** with a declarative manifest. It may be:

1. A built-in Kotlin module (highest trust).
2. A signed local bundle with a restricted host API (verified community).
3. A remote HTTP endpoint implementing a documented protocol such as Stremio's public add-on protocol (untrusted by default).
4. A repository-declared provider adapter that is compiled/signed and installed through NOVA's manager.

The core never executes arbitrary downloaded scripts with unrestricted privileges. Remote add-ons are data endpoints; local extensions run behind a restricted interface in an isolated process/worker where the platform allows.

## 3. Core components

```
AddonManager
 ├── RepositoryRegistry
 ├── AddonRegistry (installed/enabled/version/pinning)
 ├── ManifestValidator
 ├── PermissionBroker
 ├── CapabilityRouter
 │    ├── CatalogCapability
 │    ├── MetadataCapability
 │    ├── StreamCapability
 │    ├── SubtitleCapability
 │    ├── ProviderCapability
 │    └── MediaServerCapability
 ├── TransportAdapters
 │    ├── StremioHttpAdapter
 │    ├── NovaLocalAdapter
 │    └── CloudstreamInspiredAdapter
 ├── HealthMonitor
 ├── UpdateManager
 └── MarketplaceClient
```

## 4. Lifecycle

`discover → validate manifest → permission review → install → configure → enable → health-check → use → update → disable/uninstall cleanup`.

Uninstall removes add-on config, cached credentials, catalog caches and registered capabilities. A revocation list can disable a version or repository.

## 5. Capability routing

Calls are resolved by `(capability, content type, id prefix, region/language, enabled add-ons)`. Multiple add-ons can answer; results are merged with source labels and conflict policies. No result is marked available unless the add-on produced verifiable data.

## 6. Trust tiers

| Tier | Example | Rights |
|---|---|---|
| Built-in | M3U parser, local library | Full internal APIs, signed with app |
| Verified signed | Community extension | Declared permissions only, isolated |
| Repository signed | Curated repo | Declared permissions, version pinned |
| Remote protocol | Stremio HTTP endpoint | Network-only, no local file access |
| Untrusted | User-added URL | Explicit consent, restricted, no private data |

## 7. Compatibility adapters

- **Stremio HTTP adapter**: maps `/manifest.json`, `/catalog`, `/meta`, `/stream`, `/subtitles` to NOVA capabilities. `player`/`library` events are only sent with explicit user opt-in because they reveal watch activity.
- **Cloudstream-inspired adapter**: reads documented JSON repository metadata and provider manifests; NOVA does not run Cloudstream extension bytecode.
- **Nuvio-style adapter**: uses documented public APIs for user data where the user opts in; Stremio-compatible add-ons are handled by the Stremio adapter.
- **Media server adapters**: Jellyfin/Plex/Emby REST with user tokens; Kodi JSON-RPC for libraries; DLNA/UPnP for local discovery; SMB/WebDAV for local/network files.

## 8. Failure isolation

Each add-on has timeouts, result size caps, error budgets and circuit breakers. A failing add-on is degraded, not fatal. Health state is visible to the user and exported in diagnostics.

## 9. Privacy

Add-ons receive only the fields their manifest declares and the user approves. Search queries, watch history and credentials are never sent by default. Events are opt-in.
