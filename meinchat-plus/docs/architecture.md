# vbwd-android-meinchat-plus — architecture

Port of the iOS `meinchat plus` plugin. Plugin id `meinchat-plus`,
version `0.2.0`.

## Boundary

This module depends on **`:core` only** plus the declared peer `meinchat`. The
build's `dependencyBoundaryCheck` task enforces that boundary; an undeclared
edge to another plugin (or to `:app`) fails the build. The plugin is a thin
**composition root**: `Plugin.install(sdk)` wires the domain (services/stores),
the Compose views, and the menu items — each in its own file (Single
Responsibility).

## Extension seams used

End-to-end encrypted messaging — a Signal double-ratchet layer over meinchat (the canonical declared plugin→plugin dependency).

Concretely it registers: a MeinChatSecureMessaging implementation registered into meinchat's seam (device-registry + prekey wire services usable today; the Signal session is a fail-closed stub until a Kotlin/JVM Signal library is vendored) — all via the `PlatformSdk` facade, never by
reaching into a registry or the host's composition root (Interface Segregation /
Dependency Inversion).

## Lifecycle

`install` (register seams) → `activate` (mark live) → `deactivate` / `uninstall`
(tear down — unsubscribe events, release stores). A failure in any hook is
**isolated** by the host's `PluginRegistry`: this plugin becomes
`PluginStatus.Error` without aborting its peers.

## See also

- The plugin contract: `Plugin` / `PlatformSdk` / `PluginHost` in `vbwd-android-core`.
- The full sprint write-up: `docs/dev_log/20260619/reports/12-A10-meinchat-plus-plugin.md` (umbrella repo).
