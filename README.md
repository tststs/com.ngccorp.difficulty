# NGC Corp. Difficulty

A [Hytale](https://hytale.com) server plugin that scales damage across four independent axes: **mob damage**, **environment damage** (fire, void, drowning, suffocation, etc.), **fall damage**, and **player damage** (damage players deal to mobs). PvP damage is always left unmodified.

Settings persist across restarts and can be changed live — without reloading the server.

## Features

- Four named presets: Normal, Medium, Hard, Nightmare
- Fine-grained sliders (0.1× – 10×) via an in-game admin UI
- Independent mob, environment, fall, and player damage multipliers
- Player damage multiplier is custom-only — presets always leave it at ×1.0
- Zero overhead when all multipliers are 1.0 (vanilla)
- Config persisted to disk automatically

## Presets

| Preset    | Mob damage | Environment damage | Fall damage | Player damage |
|-----------|:----------:|:------------------:|:-----------:|:-------------:|
| Normal    | ×1.0       | ×1.0               | ×1.0        | ×1.0          |
| Medium    | ×1.5       | ×1.25              | ×1.25       | ×1.0          |
| Hard      | ×2.0       | ×1.5               | ×1.75       | ×1.0          |
| Nightmare | ×3.0       | ×2.5               | ×2.0        | ×1.0          |

The player damage multiplier is intentionally not part of any preset — it is only adjustable via the admin UI slider or by editing the config directly. This keeps preset behaviour predictable while still allowing server-specific tuning.

## Commands

| Command                        | Description                             |
|--------------------------------|-----------------------------------------|
| `/difficulty`                  | Show the current multipliers            |
| `/difficulty normal`           | Apply the Normal preset                 |
| `/difficulty medium`           | Apply the Medium preset                 |
| `/difficulty hard`             | Apply the Hard preset                   |
| `/difficulty nightmare`        | Apply the Nightmare preset              |
| `/difficulty ui`               | Open the admin UI with sliders          |

## Dependencies

- **Required:** `Ellie:HyUI` — used for the in-game admin UI
- **Optional:** `Buuz135:MultipleHUD` — additional HUD integration

## Installation

1. Drop `com.ngccorp.difficulty.jar` into your server's plugins folder.
2. Start or reload the server.
3. The plugin loads with vanilla defaults (all multipliers at ×1.0).

## Building from source

```bash
./gradlew build
# Output: build/libs/com.ngccorp.difficulty.jar
```

On Windows use `.\gradlew.bat`.

## License

MIT
