# OneBar

A sleek HUD mod for Minecraft: Java Edition using Fabric mod loader. 

[![Download from Curseforge](https://cf.way2muchnoise.eu/full_onebar_downloads%20on%20Curseforge.svg?badge_style=for_the_badge)](https://www.curseforge.com/minecraft/mc-mods/onebar) [![Modrinth](https://img.shields.io/modrinth/dt/onebar?color=4&label=Download%20from%20Modrinth&style=for-the-badge)](https://modrinth.com/mod/onebar) 


<a href="https://www.curseforge.com/minecraft/mc-mods/fabric-api"><img src="https://camo.githubusercontent.com/9296b230044bb4ef07851ff9baa2d04aeb210baee3467abafff1380fa081f08b/68747470733a2f2f692e696d6775722e636f6d2f4f6c31546366382e706e67" alt="drawing" width="200"/></a> <a href="https://www.curseforge.com/minecraft/mc-mods/cloth-config"><img src="https://raw.githubusercontent.com/Jab125/Jab125/main/imgs/requiredClothConfig.png" alt="drawing" width="200"/></a> <a href="https://www.curseforge.com/minecraft/mc-mods/modmenu"><img src="https://dl.isxander.dev/badges/suggests-mod-menu.png" alt="drawing" width="200"/></a> 

Automated dev builds available from [Actions tab](https://github.com/Madis0/OneBar/actions).

![](https://i.ibb.co/XtPJdcy/image.png)

Reviews by Niche Duck: [main mod](https://www.youtube.com/watch?v=-Exd6HXWSpc) (v1.2.2), [config](https://www.youtube.com/watch?v=fJbe21IGc7U) (v1.2.2). 
[Mob arena gameplay with customized OneBar](https://www.youtube.com/watch?v=2wvhI5AhvE0) (v2.3.1) by agata_m

## Installation

1. Install Minecraft 1.16 or later
2. Install [Fabric Loader](https://fabricmc.net/use/)
3. Download OneBar, [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api), [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu), [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config)
4. Place the JARs you downloaded to your Minecraft's mods folder
5. Run the game!

Currently also works on [Quilt](https://quiltmc.org), though no effort has yet been made to explicitly support it. See [#21](https://github.com/Madis0/OneBar/issues/21).

## How it works

OneBar is meant to be contextual, showing the values and bars only when you need them. Tips:

* The positive effects, including health itself, are displayed left-to-right; negative effects are displayed right-to-left. 
* If you get hungry, the hunger overlay is displayed over the health by its value. 
  * If you eat, you'll lose the overlay and the value, because you no longer need it. 
  * If you are also underwater, the water bar is displayed on top of health _and_ hunger because it is the most important value in this case. 
* Health is _always_ the first number and hunger is _almost always_ the last number (except when you have the hunger effect).
  * Everything in between is an estimate, additional health or damage source (can be disabled).
* Remember [old Minecraft without hunger and experience](https://minecraft.fandom.com/wiki/File:Beta_1.7.png)? This mod gives you a visually similar experience without losing modern functionality.
 
The mod only uses client-side data, so it is never needed on a server. There are also a lot of settings for configuring the colors and visibility of various elements.

### Bars and values

* **1** represents any number. 
* All emoji are white and look a bit different in-game. Text is shown when emoji are disabled (recommended for unicode or custom fonts).
* Estimates are a best guess based on what the client knows at the time, can change by server-side changes or natural occurences, displayed _in italic_. Can be disabled in options.

#### Positive (left-to-right)

| Effect | Color | Bar | Emoji | Text | Description/comment |
|-|-|-|-|-|-|
| Health | Red | █ | 1 | 1 | Number is your health points |
| Health boost | Red | █ | 1 | 1 | Bar width adapts to max health |
| Natural regeneration | Yellow | █ | _→1_ | _→1_ | Number is the resulting health |
| Regeneration | Pink | █ | _→1_ | _→1_ | Number is the resulting health |
| Absorption |  |  | +1 | +1 | Number is your absorption health points |
| Resistance |  |  | +⛨1% | +r1% | Number is [the effect level × 20%](https://minecraft.gamepedia.com/Resistance#Effect) |
| Fire resistance |  |  | -~🔥1×~ | -~b1×~ | Number is a rough damage multiplier (1× - burning, 2× - in fire, 3× - in soul fire, 4× - in lava), only shown if you are on fire |
| Water Breathing, Conduit Power | Blue | █ | -~⭘1~ | -~a1~ | The bar is only shown if you got the effect after losing air, text is only shown if you are in water |
| Held food restored hunger: exact/less | Green | █ | _→1_ | _→1_ | Overlaid on hunger bar; number is the resulting hunger (like AppleSkin) | 
| Held food restored hunger: wasted | Orange | █ | _→-1_ | _→-1_ | Overlaid on hunger bar; number is the waste of hunger (how much more than needed) | 

#### Negative (right-to-left)

| Effect | Color | Bar | Emoji | Text | Description/comment |
|-|-|-|-|-|-|
| Hunger | Brown | █ | -1 | -1 | Number is 20 minus food points (e.g. if you had 15 food points, it would show 5) |
| Hunger effect | Yellowish brown | █ | _→1_ | _→1_ | Number is the resulting hunger |
| Getting hungrier |  |  | ↓ | ↓ | Opt-in, shown when saturation is zero |
| Air | Blue | █ | -⭘1 | -a1 | Number is the equivalent of the _lack of_ bubbles in vanilla |
| Freezing | Light gray | █ | -❄1 | -f1 | Number is the equivalent of frost fading in vanilla |
| Burning | Orange | █ | -🔥1× | -b1× | Number is a rough damage multiplier (1× - burning, 2× - in fire, 3× - in soul fire, 4× - in lava) |
| Poison | Yellowish green | █ | _→1_ | _→1_ | Number is the resulting health |
| Wither | Dark gray | █ | _→1_ | _→1_ | Number is the resulting health |
| Warden anger | Dark teal | █ | -🔔1 | -s1 | The closest warden's anger level, influenced also by other entities
| Bad Omen |  |  | -🪓1× | -BO1× | Number is [the effect level](https://minecraft.fandom.com/wiki/Bad_Omen#Effect) |
| Impending death |  |  | _→0_ | _→0_ | Drowning, freezing damage, burning in fire/lava, starvation, suffocation. Zero is the resulting health (death) |
| Hardcore mode |  |  | ☠ | HC | The vanilla one, not the UHC mode of OneBar which has no indicators |

#### Mounts

| Effect | Color | Bar | Text | Where | Description/comment |
|-|-|-|-|-|-|
| Mount health | Orange | █ | 1 | Above OneBar | Similar to OneBar itself |
| Horse jump | Brown | ▄▄ | 1.0 | Below crosshair | Vertical bar, number shows approximate height of blocks the horse can jump | 

#### Others

| Effect | Color | Bar | Text | Where | Description/comment |
|-|-|-|-|-|-|
| Experience bar | Green | ▁ | 1 | Next to hotbar | Shown only when you have any XP (configurable) |
| Lapis Lazuli counter | Blue |  | 1 or 1× | Next to experience bar | Opt-in, shows either the raw count or times enchantable maxed out (XP level 30+) |
| Armor | White | ▔ |  | Above OneBar, above mount health | Equivalent of vanilla's armor indicator; also shown for horses if applicable |
| Armor durability | Light blue | ▔ |  | Overlaid on armor bar | Opt-in, width adapts to armor bar |
| Elytra durability | Purple | ▔ |  | Overlaid on armor bar | Shown only when flying |
| Saturation | Orange | ▁ |  | Below OneBar | Opt-in |

## Mods I recommend with OneBar

* [BerdinskiyBear's Armor HUD](https://www.curseforge.com/minecraft/mc-mods/berdinskiybears-armor-hud) - adds a customizable hotbar-like armor bar
* [Food Highlight](https://www.curseforge.com/minecraft/mc-mods/food-highlight) - shows orange (wasted) and green (exact/less) indicators on food in hotbar and inventories
* [Enhanced Attack Indicator](https://www.curseforge.com/minecraft/mc-mods/enhanced-attack-indicator) - adds more features to the attack cooldown, e.g drawing a bow
* [Till it Breaks](https://www.curseforge.com/minecraft/mc-mods/till-it-breaks) - adds numeric durability and arrow count (for bows and crossbows) to hotbar items

## FAQ

**Q: Why do I have to download [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config) separately?**

A: Because it's big, gets updated a lot and you probably have a mod or two that requires it anyway. If one of your mods already bundles it, you don't have to download it at all :)

**Q: How do I edit the colors?**

A: Click `Esc` → `Mods` → `OneBar` → `config button`. Colors are formatted as opacity + RGB in HEX, so choose an opacity from [this list](https://gist.github.com/lopspower/03fb1cc0ac9f32ef38f4#all-hex-value-from-100-to-0-alpha) and use [any HEX color picker](https://rgbacolorpicker.com/hex-color-picker) for choosing a color. For example if I want 80% opacity (`CC`) on blue (`0000FF`), I will enter `#CC0000FF`.

**Q: Can I use this with minigames?**

A: Yes! There are options to disable hunger display or enable UHC mode, plus you can toggle those or entire OneBar with a keybind (if you set one).

**Q: Can I use this in my favorite server?**

A: Yes. If things like AppleSkin and effect HUDs are allowed, you can use the full featureset of OneBar. If they are not, just disable effect estimates to use it for essentially only health and hunger.

**Q: Forge support?**

A: No. Feel free to fork according to the license or [check out my resource pack that inspired this](https://www.curseforge.com/minecraft/texture-packs/material-design-hud).

**Q: Backports?**

A: No. There are old versions available down to 1.16.5 though.

**Q: Snapshots?**

A: Maybe. If I have time, I might do targeted updates, but otherwise as long as you have the latest Cloth Config and Fabric API, it probably runs anyway.
