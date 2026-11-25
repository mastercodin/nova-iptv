# F. Database Schema Changes (NOVA extension)

Status: **Designed**. Additive to the existing NOVA Room schema; no migration executed in this phase.

## 1. Add-on tables

- `addons`: id, manifest_id, name, version, developer, source_type (builtin/signed/remote/repo), endpoint, entrypoint, enabled, pinned_version, health_state, installed_at, updated_at, signature_digest.
- `addon_capabilities`: addon_id, capability_name, content_types_json, id_prefixes_json, extras_json.
- `addon_permissions`: addon_id, permission, granted, granted_at.
- `addon_settings`: addon_id, key, encrypted_value, updated_at.
- `repositories`: id, url, name, version, verified, last_sync, signature_key.
- `addon_updates`: addon_id, available_version, discovered_at, compatibility_state.

## 2. Catalog and metadata tables

- `catalog_items`: id (namespace-prefixed), source_addon_id, catalog_id, content_type, title, original_title, poster, backdrop, release_year, rating, genres_json, languages_json, countries_json, added_at, expires_at.
- `media_details`: item_id, plot, cast_json, directors_json, runtime, tmdb_id, imdb_id, tvdb_id, metadata_source, refreshed_at.
- `series_seasons`: series_id, season_number, name, poster, details_json.
- `series_episodes`: series_id, season_number, episode_number, part, title, plot, runtime, air_date, source_ids_json.
- `sources`: id, item_id, video_id, addon_id, url_encrypted, headers_json_encrypted, type, claimed_resolution, verified_resolution, language, verified_language, subtitles_json, status, last_checked.
- `subtitle_tracks`: id, item_id, addon_id, url, language, label, format, verified.

## 3. Unified state tables

- `unified_watch_state`: profile_id, item_id, position_ms, duration_ms, completed, updated_at, source_id.
- `unified_watchlist`: profile_id, item_id, added_at, source_addon_id.
- `unified_favorites`: profile_id, item_id, kind (channel/vod/radio), added_at.
- `content_matches`: canonical_id, item_id, match_type, confidence, verified.
- `provider_health`: provider_id, addon_id, status, latency_ms, last_success, last_error, channel_count, error_code.

## 4. Home and customization

- `home_sections`: profile_id, section_id, order_index, visible, style_json.
- `custom_collections`: profile_id, name, artwork, filter_json.
- `profiles`: existing profile table extended with `pin_verifier`, `biometric_enabled`, `avatar`, `language_preferences_json`.

## 5. Migration principles

- All new tables are additive; existing provider/channel/EPG data remains valid.
- Versioned Room migration with fixture-based migration tests.
- Encrypted fields use Keystore-backed keys; never store plaintext tokens/headers.
- Source claims and verified facts are stored separately.
- No destructive migration; failed migration rolls back.
