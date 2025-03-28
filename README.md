# Granter v1.0

A **powerful and flexible granting system** for **Minecraft servers** that integrates with **LuckPerms** and **PlaceholderAPI**. This plugin allows players with specific permission to grant ranks to other players.

---

## Features

- **Dynamic Rank Limits**: The plugin allows you to set limits for how many times a player can grant specific ranks, such as `limit1`, `limit2`, etc.
- **Integration with LuckPerms**: Automatically checks for the required permissions to grant ranks.
- **Customizable Rank Prefixes**: Set custom rank prefixes and messages in `config.yml`.
- **Colored Rank Notifications**: Grant notifications and messages are color-coded and fully customizable.
- **Grant Usage Logging**: Track who granted what rank to which player and how many times.

---

## Optional Plugins (Dependencies)

1. **[PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)**  
   - **Required for dynamic placeholders in messages**. PlaceholderAPI allows the plugin to dynamically insert player data (e.g., player name, rank) into messages.


## Required Plugins (Dependencies)

1. **[LuckPerms](https://www.spigotmc.org/resources/luckperms.28140/)**  
   - **Required for rank management**. This plugin handles permissions and rank assignments. Without LuckPerms, the plugin won't be able to manage ranks or check for permissions.

---

## Commands

1. ```/grant <player_name> <rank_name>```
    - **Description:** Grants the mentioned player the rank.
    - **Permission:** ```granter.grant```, ```granter.grant.limit<limit>```

2. ```/grantreload```
    - **Description:** Reloads the plugin configuration (data.yml, config.yml).
    - **Permission:** ```granter.grant.reload```

3. ```/grantinfo```
   - **Description:** Shows a player information regarding his grants (available and used).
   - **Permission:** ```granter.grant.info```


5. ```/grantreset```
   - **Description:** Resets a players grants essentially letting him grant again.
   - **Permission:** ```granter.grant.reset```


---

## Configuration

### `config.yml`

The `config.yml` file allows you to configure rank prefixes, limits, and other plugin settings:

```yaml
# ===================================================================
# Granter Plugin Configuration, Made by Flubel
# ===================================================================
# This configuration file allows you to set up ranks, their prefixes, 
# limits for granting, and messages that appear when a rank is granted.
# ===================================================================

ranks:
  # Default demo ranks, you can add your own ranks, prefixes, and limits.
  # Each rank has a unique prefix and granting limits.
  # Limits work with the permission node: granter.grant.limit<limit_number> (limit1, limit2)
  # Example: For limit1, the permission would be granter.grant.limit1

  flux:
    prefix: '&b[FLUX]'  # Prefix that appears before player names
    limits:
      limit1: 4       # Max number of times this rank can grant permission level 1
      limit2: 2       # Max number of times this rank can grant permission level 2
      limit3: 1       # Max number of times this rank can grant permission level 3

  flux+:
    prefix: '&d[FLUX+]'
    limits:
      limit1: 4
      limit2: 2
      limit3: 1

  vortex:
    prefix: '&e[VORTEX]'
    limits:
      limit1: 2
      limit2: 1
      limit3: 0  # This rank cannot grant level 3 permissions

# ===================================================================
# Grant Messages
# ===================================================================
# These messages are broadcasted to the server when a rank is granted.
# Use placeholders to dynamically insert player names and ranks.
# - {granter_player} = Player granting the rank
# - {granted_player} = Player receiving the rank
# - {rank} = Rank being granted
messages:
  - " "  # Optional Space
  - "&ePlayer &6&l{granter_player}&e granted {rank} &eto &6&l{granted_player}"
  - " "  # Optional Space

# ===================================================================
# Message Decoration
# ===================================================================
# This decoration appears above and below the broadcasted messages. e.g. @, =, ~, +. *, $
# It can be adjusted to fit different symbols and widths.
decor: "@"

# Number of decoration characters to appear in a single row.
# Recommended values: 45 for '@', 46 for '=' (adjust as needed).
decor_length: 45
```

---

## License
This plugin is distributed unded the [FGPL](https://flubel.com/license) license.
