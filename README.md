# Slimefun 4 — Fork for Paper 26.1.2

This is an **unofficial fork of Slimefun 4**, ported to Paper 26.1.2 / Java 25.

> This fork is not an official Slimefun release, is not supported by the Slimefun team, and **does not accept bug reports upstream**. All issues should be reported in this repository only.

## What is this

Slimefun is a plugin that turns a vanilla Minecraft server into a modpack-like experience without installing any mods. Over **500 new items and recipes**: from backpacks and jetpacks to nuclear reactors, magic altars, power grids and item transport systems.

This fork exists to make the plugin work on the current version of Minecraft.

## Target version

| | This fork | Original Slimefun |
| --- | --- | --- |
| **Minecraft** | 26.1.2 | 1.16 – 1.21 |
| **Server** | Paper 26.1.2 | Paper / Spigot |
| **Java** | **Java 25** | Java 16+ |

> Minecraft 26.1 is the first version using Mojang's new `YY.D.H` versioning scheme and the first to require **Java 25**. Running on Java 21 or lower is not possible.

## Installation

1. Install Paper 26.1.2 and make sure the server runs on Java 25.
2. Download the `.jar` from [Releases](../../releases) or build it manually (see below).
3. Place the `.jar` in the server's `plugins/` folder.
4. Start the server. Config files will be created in `plugins/Slimefun/`.

## Building from source

Requirements:
- JDK 25
- Maven 3.9+

```bash
git clone <this-repo-url>
cd Enigma-Slimefun4-new
mvn clean package
```

The built `.jar` will be at `target/Slimefun v4.9-UNOFFICIAL.jar`.

## Differences from upstream

- Compatibility with Paper 26.1.2 (Mojang mappings, new `api-version` format).
- Java 25 as compilation target (was Java 16/17).
- Updated dependencies for the new API.
- Code fixes for breaking changes in Paper API between 1.21 and 26.1.

No changes to gameplay, recipes or balance — this is a purely technical port.

## Changes from original

- **Grappling Hook** no longer drops on the ground after use — the item is returned directly to the player's inventory.

## License

Slimefun 4 is licensed under [GNU GPLv3](LICENSE). This fork inherits the same license.

Original project: <https://github.com/Slimefun/Slimefun4>

---

*Slimefun is not affiliated with Mojang Studios or Minecraft.*
