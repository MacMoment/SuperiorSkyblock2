# SuperiorSkyblock2 - Genphoria Rework

## Build Instructions

This is a Gradle-based Java plugin for Bukkit/Spigot Minecraft servers.

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Gradle 8.x (included via Gradle Wrapper)
- Internet connection for downloading dependencies

### Building the Plugin

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-repo/SuperiorSkyblock2.git
   cd SuperiorSkyblock2
   ```

2. **Make the Gradle wrapper executable (Linux/macOS):**
   ```bash
   chmod +x gradlew
   ```

3. **Build the plugin:**
   
   **Linux/macOS:**
   ```bash
   ./gradlew build
   ```
   
   **Windows:**
   ```cmd
   gradlew.bat build
   ```

4. **Find the output:**
   The compiled JAR file will be located in:
   ```
   target/SuperiorSkyblock2-<version>.jar
   ```

### Clean Build

To perform a clean build (removes previous build artifacts):
```bash
./gradlew clean build
```

### Development Build

For development/debug builds:
```bash
./gradlew shadowJar
```

## Genphoria Customizations

This version includes the following customizations for the Genphoria server:

### Command Aliases
- `/team`, `/island`, `/clan`, `/is`, `/p`, `/plot` - All work as the main island command

### Prefix Format
All messages use the Genphoria rainbow prefix:
`<#EC8C8C>&lG<#FFC58B>&lE<#FFE295>&lN<#FFFF9E>&lP<#9AFF9A>&lH<#97CFF5>&lO<#9DB5FA>&lR<#A39AFF>&lI<#EEA0FF>&lA &8»&7`

### Color Scheme
- Primary: `<#97CFF5>` (Light blue)
- Secondary: `&7` (Gray for regular text)
- Actions: `&8(( &f<action> &7<description> &8))`

### Team Chat Format
`<#FFC58B>&lT<#FFFF9E>&lE<#9AFF9A>&lA<#EEA0FF>&lM &f{PLAYER}: &7{MESSAGE}`

### Upgrade System
The following team upgrades are available:
- **Token Multiplier** - 100 levels, +0.02x per level, 1 Team Point each
- **Money Multiplier** - 100 levels, +0.02x per level, 1 Team Point each
- **EXP Multiplier** - 100 levels, +0.02x per level, 1 Team Point each
- **Gendrops Speedboost** - Multiple levels, 2 Team Points each
- **Island Size** - 5 levels (125x125 to 250x250), 5+ Team Points each
- **Hopper Limit** - 3 levels (50 to 150 per player), 5 Team Points each

### Toggle Menu Command
Use `/is togglemenu` or `/is toggle menu` to toggle whether `/is` opens the panel menu or teleports you directly to your island.

### Team Points System

Team Points are a currency used for purchasing upgrades. Admins can give team points using:

```
/is admin giveteampoints <player/island> <amount>
/is admin setteampoints <player/island> <amount>
```

### Player Stats Tracking

The plugin tracks player statistics for the leaderboard system:
- **Farming** - Total crops farmed
- **Fishing** - Total fish caught
- **Money** - Player's money contribution
- **Tokens** - Player's token contribution

Admin commands for managing stats:
```
/is admin setplayerstats <player> <farming/fishing/money/tokens> <amount>
```

### Leaderboard Points System

Teams earn leaderboard points based on member contributions:
- **Farming:** 1 point per 10,000 crops farmed
- **Fishing:** 1 point per 100 fish caught
- **Money:** 1 point per $10,000,000
- **Tokens:** 1 point per 10,000 tokens

### Team Info Command

View detailed team statistics and member contributions:
```
/is teaminfo [player/island-name]
```

This shows:
- Team name and owner
- Total team points
- Team statistics (farming, fishing, money, tokens)
- Points breakdown for each stat
- Member contributions ranked by points

### Configuration Files
- `config.yml` - Main configuration with Genphoria defaults
- `lang/en-US.yml` - English language file with Genphoria prefix
- `menus/*.yml` - All menus updated with Genphoria styling
- `menus/team-leaderboard.yml` - Team leaderboard menu configuration

### New Admin Commands
| Command | Description |
|---------|-------------|
| `/is admin giveteampoints <player/island> <amount>` | Give team points to an island |
| `/is admin setteampoints <player/island> <amount>` | Set team points for an island |
| `/is admin setplayerstats <player> <stat> <amount>` | Set a player's stat value |

### New Player Commands
| Command | Description |
|---------|-------------|
| `/is teaminfo [player/island]` | View detailed team statistics |
| `/is togglemenu` | Toggle menu on/off for /is command |
| `/is toggle menu` | Same as togglemenu |

## Support

For issues or questions, please open an issue on the GitHub repository.
