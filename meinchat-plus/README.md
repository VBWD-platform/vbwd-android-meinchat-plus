# vbwd-android-meinchat-plus

A feature **plugin** for the [vbwd-android](https://github.com/vbwd-platform/vbwd-android-core)
plugin-host platform — the Kotlin · Jetpack Compose · Hilt port of the vbwd-ios
SDK. Plugin id: `meinchat-plus` · version `0.2.0`.

End-to-end encrypted messaging — a Signal double-ratchet layer over meinchat (the canonical declared plugin→plugin dependency).

## What it registers

Through the `PlatformSdk` facade (the single extension seam) this plugin contributes:
a MeinChatSecureMessaging implementation registered into meinchat's seam (device-registry + prekey wire services usable today; the Signal session is a fail-closed stub until a Kotlin/JVM Signal library is vendored).

It touches **no core internals** — it depends on the public `:core` module only
(Open/Closed). Depends on **`:core`** and, as a **declared peer**, **`meinchat`** (`PluginMetadata.dependencies = ["meinchat"]`) — the only plugin→plugin edge the dependency-boundary check permits.

## Consume it

As a standalone module the plugin is published to GitHub Packages and consumed by
Maven coordinate:

```kotlin
// settings.gradle.kts — add the GitHub Packages repo (PAT with read:packages)
dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/vbwd-platform/vbwd-android-meinchat-plus")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

// build.gradle.kts
dependencies {
    // declared peer dependency (mirrors PluginMetadata.dependencies)
    implementation("com.vbwd:vbwd-android-meinchat-plus:0.2.0")
    implementation("com.vbwd:vbwd-android-meinchat:1.1.0")
}
```

Then add it to the host's `provideAvailablePlugins` list and to
`app/src/main/assets/plugins.json` (the enable/disable manifest).

## Build & test

```bash
./gradlew check        # ktlint + detekt + unit tests
```

## Docs

- [`docs/architecture.md`](docs/architecture.md) — how this plugin is wired.
- Original sprint report: `docs/dev_log/20260619/reports/12-A10-meinchat-plus-plugin.md` in the umbrella repo.

## License

BSL 1.1 (Business Source License). Part of the **vbwd-platform** SDK.
