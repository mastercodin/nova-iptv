# D. NOVA Add-on Manifest Specification (draft v1)

Status: **Designed**.

## 1. Format

JSON UTF-8, served as `manifest.json` for remote add-ons or embedded in a signed local bundle. Unknown fields are ignored with a warning.

## 2. Required fields

| Field | Type | Meaning |
|---|---|---|
| `id` | string | Reverse-DNS unique id, e.g. `org.example.catalog` |
| `version` | string | Semantic version `MAJOR.MINOR.PATCH` |
| `name` | string | Display name |
| `description` | string | Short description |
| `capabilities` | array | One or more capability declarations |
| `minAppVersion` | string | Minimum NOVA version |
| `permissions` | array | Permissions requested |

## 3. Optional fields

`developer`, `homepage`, `documentation`, `repository`, `languages`, `regions`, `platforms`, `maxAppVersion`, `dependencies`, `settingsSchema`, `healthUrl`, `signature`, `checksum`, `translations`, `categories`, `tags`, `entrypoint`, `endpoint`, `behaviorHints`.

## 4. Capability object

```json
{
  "name": "catalog",
  "types": ["movie", "series", "anime", "channel", "radio"],
  "idPrefixes": ["tt", "tmdb:", "nov:"],
  "catalogs": [
    { "id": "trending", "name": "Trending", "type": "movie", "extra": ["genre", "year"] }
  ]
}
```

Supported capability names: `catalog`, `meta`, `stream`, `subtitles`, `provider`, `mediaServer`, `epg`, `metadata`, `discovery`.

## 5. Permission vocabulary

`network:<host-pattern>`, `storage:read`, `storage:write`, `credentials:<provider-id>`, `watchHistory:read`, `watchHistory:write`, `preferences:read`, `notifications`, `mediaServer:<type>`, `localNetwork`, `background`.

No `all` or wildcard permission. Remote add-ons may not request `storage:*` or local-network permissions.

## 6. Stremio compatibility mapping

| Stremio manifest field | NOVA mapping |
|---|---|
| `id`, `version`, `name`, `description` | same |
| `resources` (strings/objects) | `capabilities` |
| `types` | capability `types` |
| `idPrefixes` | capability `idPrefixes` |
| `catalogs` | capability `catalogs` |
| `behaviorHints.epgProvider` | `behaviorHints.novaEpgProvider = true` + `epg` capability |
| `player`/`library` resources | opt-in event permissions, never automatic |

## 7. Example

```json
{
  "id": "org.example.sample",
  "version": "1.0.0",
  "name": "Sample Catalog",
  "description": "Public test catalog",
  "developer": "Example Developer",
  "documentation": "https://example.org/docs",
  "capabilities": [
    {
      "name": "catalog",
      "types": ["movie"],
      "catalogs": [{ "id": "main", "type": "movie", "name": "Movies" }]
    },
    {
      "name": "meta",
      "types": ["movie"],
      "idPrefixes": ["tt"]
    }
  ],
  "minAppVersion": "1.0.0",
  "permissions": ["network:example.org"],
  "platforms": ["android-tv", "android-mobile"],
  "languages": ["en"]
}
```

## 8. Validation rules

- Semantic version must parse.
- At least one capability.
- No unknown permission strings accepted.
- Every capability must declare `types`.
- `idPrefixes` optional for catalog-only add-ons.
- Signature/checksum verified before install when present.
- Dependency graph must be acyclic and version-compatible.
