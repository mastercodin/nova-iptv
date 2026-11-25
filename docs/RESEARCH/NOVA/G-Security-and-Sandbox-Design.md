# G. Security and Sandbox Design

Status: **Designed**.

## 1. Threat model

Third-party add-ons are untrusted. Threats: malicious manifests, credential theft, tracking/watch-history leakage, arbitrary network calls, denial of service, supply-chain compromise, path traversal, fake availability claims, crash propagation.

## 2. Trust tiers and enforcement

| Tier | Mechanism | Restrictions |
|---|---|---|
| Built-in | Compiled into app | Full internal APIs |
| Signed local extension | Signature + manifest | Declared permissions only, isolated worker |
| Repository signed | Repo key pinning | Version pinning, declared permissions |
| Remote HTTP add-on | Documented protocol | Network-only, no local FS, no private data unless granted |
| User-added URL | Explicit consent | No credentials, no history, rate-limited |

## 3. Manifest verification

- Semantic version and manifest schema validation.
- Signature/checksum verification when provided; repository key pinning.
- Dependency graph acyclic; version constraints validated.
- Permission strings allowlisted; unknown permission rejects install.
- Capability declarations matched to implementation endpoints during health check.

## 4. Runtime isolation

- Remote add-ons: HTTP client with host allowlist from `network:<host>` permission, timeouts, response-size caps, TLS validation.
- Local signed extensions: isolated process/worker with a narrow binder/API surface; no arbitrary file access; no reflection into app internals.
- Crash isolation: add-on crashes are contained, logged locally, circuit-broken; core remains usable.
- Rate limiting per add-on; output budget per call.

## 5. Credential protection

- Android Keystore-backed encrypted DataStore for tokens/passwords/headers.
- Credentials are scoped to one add-on/provider; add-ons cannot enumerate other credentials.
- `credentials:<id>` permission plus explicit user grant.
- Logs and diagnostics redact headers, tokens, query strings with secrets and DRM keys.

## 6. Watch privacy

- `watchHistory:read/write` and `player`/`library` event permissions require explicit opt-in.
- Search queries are not broadcast to add-ons by default; per-query user action required.
- Optional local-only search mode.

## 7. Supply chain

- Repository verification, signed manifests, version pinning, dependency validation.
- Revocation list for malicious add-on ids/versions/repo keys.
- Update manager shows changelog/signature status; automatic updates only for trusted tiers.
- Uninstall cleanup removes config, caches, credentials and capability registrations.

## 8. Verification evidence

- No arbitrary script execution from downloaded content.
- No wildcard permissions.
- Security tests: manifest fuzzing, permission escalation attempts, redaction tests, TLS negative tests, crash containment tests.
