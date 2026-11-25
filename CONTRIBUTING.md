# Contributing

Thanks for helping build Open IPTV.

## Ground rules

- Only contribute **original** code and assets you have the right to license.
- Do not copy code, resources, logos, strings or UI designs from proprietary
  apps, including any APK placed in this workspace.
- Do not add license-check bypasses, credential theft, or piracy-enabling code.

## Development

- Use JDK 21 and the Gradle wrapper in the repository root.
- Run `./gradlew assembleDebug` and `./gradlew testDebugUnitTest` before opening
  a pull request.
- Keep changes focused; add or update tests for parsing and use-case changes.
- Follow Kotlin official style conventions. A formatter/linter can be added in
  a later phase.

## Pull requests

1. Fork or create a branch from `main`.
2. Describe the problem and the approach.
3. List any new dependencies and why they are needed.
4. Confirm that the contribution is original and licensed under the project's
   MIT license.

## Reporting issues

Include device type (phone/tablet/TV), Android version, the exact steps to
reproduce, and the expected vs. observed behavior. Never paste private
playlist credentials or subscription tokens into an issue.
