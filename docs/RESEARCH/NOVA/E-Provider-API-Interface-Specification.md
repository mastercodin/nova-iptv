# E. Provider API Interface Specification (draft v1)

Status: **Designed**. Kotlin-like conceptual interfaces; implementations will be original.

## 1. Provider capability

```kotlin
interface CatalogProvider {
    val manifest: AddonManifest
    suspend fun getCatalog(catalogId: String, page: PageRequest): Result<CatalogPage>
    suspend fun search(query: SearchQuery): Result<CatalogPage>
    suspend fun getMovieDetails(id: String): Result<MediaDetails>
    suspend fun getSeriesDetails(id: String): Result<SeriesDetails>
    suspend fun getEpisodes(id: String, season: Int?): Result<List<Episode>>
    suspend fun getAvailableSources(id: String, videoId: String?): Result<List<Source>>
    suspend fun getSubtitles(id: String, videoId: String?): Result<List<SubtitleTrack>>
}
```

## 2. Metadata capability

```kotlin
interface MetadataProvider {
    suspend fun getMeta(type: ContentType, id: String): Result<Metadata>
    suspend fun searchMeta(query: String, filters: MetaFilters): Result<List<Metadata>>
    suspend fun enrich(item: Metadata): Result<Metadata>
}
```

## 3. Subtitle capability

```kotlin
interface SubtitleProvider {
    suspend fun search(meta: SubtitleSearchRequest): Result<List<SubtitleTrack>>
    suspend fun download(trackId: String): Result<SubtitleFile>
    suspend fun shift(track: SubtitleFile, deltaMs: Long): Result<SubtitleFile>
}
```

## 4. Media server capability

```kotlin
interface MediaServerProvider {
    suspend fun authenticate(): Result<ServerSession>
    suspend fun listLibraries(): Result<List<MediaLibrary>>
    suspend fun listItems(libraryId: String, page: PageRequest): Result<MediaPage>
    suspend fun playbackUrl(itemId: String): Result<PlaybackSource>
    suspend fun reportProgress(itemId: String, positionMs: Long, durationMs: Long): Result<Unit>
}
```

## 5. Authentication capability

```kotlin
interface AuthProvider {
    suspend fun startLogin(): Result<AuthChallenge>
    suspend fun completeLogin(challenge: AuthChallenge, input: AuthInput): Result<AuthToken>
    suspend fun refresh(token: AuthToken): Result<AuthToken>
    suspend fun logout(): Result<Unit>
}
```

## 6. Result integrity rules

- `Source` includes `verifiedResolution`, `verifiedLanguage`, `verifiedSubtitles` booleans; UI shows "unknown" unless verified.
- Errors are typed: `Unsupported`, `Unauthorized`, `RateLimited`, `ProviderError`, `NetworkError`, `ParseError`, `PermissionDenied`.
- Every result carries `sourceAddonId` and `fetchedAt`.
- No capability may return arbitrary file paths or executable code.

## 7. Example source object

```json
{
  "id": "src-1",
  "provider": "org.example.provider",
  "url": "https://example.org/stream.m3u8",
  "headers": { "Referer": "https://example.org/" },
  "type": "hls",
  "claimedResolution": "1080p",
  "verifiedResolution": false,
  "language": "en",
  "verifiedLanguage": false,
  "subtitles": [],
  "status": "unknown"
}
```
