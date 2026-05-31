# destination-line

Minecraft Paper/Spigot 1.20+ plugin that shows a packet-only waypoint trail per player.

## Features

- Auto-shows destination `1` when a player joins.
- Operator/permission command to show destinations for a specific player:
  - `/showdestination <player> <destinationNumber>`
- Optional management commands:
  - `/hidedestination <player>`
  - `/listdestinations`
- Real-time actionbar distance and smooth particle trail.
- Trail is **packet-only per targeted player** (`Player#spawnParticle`), so other players do not see it.
- Configurable destination list in `config.yml`.

## Build

```bash
mvn -B package
```

Built jar will be in `target/`.

## Install

1. Build the plugin.
2. Copy the jar from `target/` into your server's `plugins/` folder.
3. Start/restart the server.
4. Edit generated `plugins/DestinationLine/config.yml` as needed.
5. Run `/reload confirm` or restart after config changes.

## Commands

- `/showdestination <player> <destinationNumber>` (permission: `destination.show`, default: op)
- `/hidedestination <player>` (permission: `destination.hide`, default: op)
- `/listdestinations` (permission: `destination.list`)

## Add more destinations

Open `config.yml` and add another numeric key under `destinations`:

```yml
destinations:
  "3":
    name: Castle Gate
    world: world
    x: 240.0
    y: 72.0
    z: 19.0
    displayText: "Castle Gate"
    particle: REDSTONE
    color:
      red: 255
      green: 80
      blue: 80
    size: 1.2
```

Fields:

- `name` (required)
- `world` (required)
- `x`, `y`, `z` (required)
- `displayText` (optional, defaults to `name`)
- `particle` (optional, defaults to `VILLAGER_HAPPY`)
- `color` + `size` (optional, used by `REDSTONE` particle)
