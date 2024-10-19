# OneBar

A sleek HUD mod for Minecraft: Java Edition using Fabric mod loader. 

[![Download from Curseforge](https://cf.way2muchnoise.eu/full_onebar_downloads%20on%20Curseforge.svg?badge_style=for_the_badge)](https://www.curseforge.com/minecraft/mc-mods/onebar) [![Download from Modrinth](https://img.shields.io/modrinth/dt/onebar?color=4&label=Download%20from%20Modrinth&style=for-the-badge)](https://modrinth.com/mod/onebar) 


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

## Mod compatibility

- [Raised](https://www.curseforge.com/minecraft/mc-mods/raised) - moves OneBar and hotbar upwards, height of both can be adjusted 
- [Auto HUD](https://www.curseforge.com/minecraft/mc-mods/auto-hud) - automatically shows and hides certain elements of OneBar as needed
- [Exordium](https://www.curseforge.com/minecraft/mc-mods/exordium) (beta) - improve HUD performance
- [OneBar + MCC Island Compat](https://modrinth.com/mod/onebarmcci) (unofficial) - disables OneBar when you have no health bar on MCC Island
- [Flour's Various Tweaks](https://modrinth.com/mod/fvt) and [Head-down display](https://www.curseforge.com/minecraft/mc-mods/headdowndisplay) - partial compatibility for the auto-hide feature, does not hide OneBar elements

## How it works

OneBar is meant to be contextual, showing the values and bars only when you need them. Tips:

* The positive effects, including health itself, are displayed left-to-right; negative effects are displayed right-to-left. 
* If you get hungry, the hunger overlay is displayed over the health by its value. 
  * If you eat, you'll lose the overlay and the value, because you no longer need it. 
  * If you are also underwater, the water bar is displayed on top of health _and_ hunger because it is the most important value in this case. 
* Health is _always_ the first number and hunger is _almost always_ the last number (except when you have the hunger effect).
  * Everything in between is an estimate, additional health or damage source (can be disabled).
  * You can also enable "Show additional symbols" in options to get icons for those
* Remember [old Minecraft without hunger and experience](https://minecraft.wiki/w/File:Beta_1.7.png)? This mod gives you a visually similar experience without losing modern functionality.
 
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
| Health/health boost (extra symbols) | Red | █ | ❤1 | he1 | When enabling "Show additional symbols" in Text options |
| Natural regeneration | Yellow | █ | _→1_ | _→1_ | Number is the resulting health |
| Regeneration | Pink | █ | _→1_ | _→1_ | Number is the resulting health |
| Absorption |  |  | +1 | +1 | Number is your absorption health points |
| Absorption (extra symbols) |  |  | +💟1 | +ab1 | When enabling "Show additional symbols" in Text options; in-game the emoji looks like a heart in picture frame |
| Resistance |  |  | +⛨1% | +re1% | Number is [the effect level × 20%](https://minecraft.wiki/Resistance#Effect) |
| Invisibility |  |  | +🫥 | +in | Crossed out when wearing armor/offhand item, have glowing effect or arrow(s) stuck in you. |
| Totem of Undying |  |  | +ቶ1 | +tu1 | Opt-in, shows the amount of Totem of Undyings in your inventory; crossed out when not held in main/offhand. |
| Fire resistance |  |  | -~🔥1×~ | -~bu1×~ | Number is a rough damage multiplier (1× - burning, 2× - in fire, 3× - in soul fire, 4× - in lava), only shown if you are on fire |
| Water Breathing, Conduit Power | Blue | █ | -~⭘1~ | -~ai1~ | The bar is only shown if you got the effect after losing air, text is only shown if you are in water |
| Held food restored hunger: exact/less | Green | █ | _→1_ | _→1_ | Overlaid on hunger bar; number is the resulting hunger (like AppleSkin) | 
| Held food restored hunger: wasted | Orange | █ | _→-1_ | _→-1_ | Overlaid on hunger bar; number is the waste of hunger (how much more than needed) | 

#### Negative (right-to-left)

| Effect | Color | Bar | Emoji | Text | Description/comment |
|-|-|-|-|-|-|
| Hunger | Brown | █ | -1 | -1 | Number is 20 minus food points (e.g. if you had 15 food points, it would show 5) |
| Hunger (extra symbols) | Brown | █ | -🍖1 | -hu1 | When enabling "Show additional symbols" in Text options |
| Hunger effect | Yellowish brown | █ | _→1_ | _→1_ | Number is the resulting hunger |
| Getting hungrier |  |  | ↓ | ↓ | Opt-in, shown when saturation is zero |
| Air | Blue | █ | -⭘1 | -ai1 | Number is the equivalent of the _lack of_ bubbles in vanilla |
| Freezing | Light gray | █ | -❄1 | -fr1 | Number is the equivalent of frost fading in vanilla |
| Burning | Orange | █ | -🔥1× | -bu1× | Number is a rough damage multiplier (1× - burning, 2× - in fire, 3× - in soul fire, 4× - in lava) |
| Poison | Yellowish green | █ | _→1_ | _→1_ | Number is the resulting health |
| Wither | Dark gray | █ | _→1_ | _→1_ | Number is the resulting health |
| Warden anger | Dark teal | █ | -💢1 | -wa1 | The closest (100 blocks) warden's anger level towards the player. [20-11 - angry, chasing; 10-6 - agitated, 5-0 - calm, can despawn.](https://minecraft.wiki/w/Warden#Anger)
| Levitation | Dark purple | █ | -⏏1 | -le1 | Number is the effect time, crossed out while in water |
| Fall height (experimental) |  |  | -⊻1 | -fa1 | Opt-in, indicates the amount of blocks you're about to fall in-air/with levitation effect/while sneaking on an edge. Currently does not consider target block type, your effects and enchants. |
| Glowing |  |  | -☀ | -gl | Usually given when shot by a spectral arrow. Emoji chosen to remind of the effect icon. |
| Infested |  |  | -💔🐛 | -Hin | Having infested effect may summon silverfish upon getting hit. Given in ominous trials. |
| Weaving |  |  | -😵🕸 | -Dwe | Having weaving effect may summon cobwebs upon death. Given in ominous trials. |
| Oozing |  |  | -😵▪▪ | -Doo | Having oozing effect may summon two slimes upon death. Given in ominous trials. |
| Wind Charged |  |  | -😵💨 | -Dwi | Having wind charged effect may summon a wind charge upon death. Given in ominous trials. |
| Bad Omen |  |  | -👹?1× | -bo1× | Number is the effect level; preserved when converting into Raid or Trial Omen |
| Raid Omen |  |  | -👹🪓1× | -ro1× | Number is [the effect level](https://minecraft.wiki/w/Raid#Java_Edition) |
| Trial Omen |  |  | -👹🗝1× | -to1× | Number is [the duration multiplier](https://minecraft.wiki/w/Trial_Omen#Causes), e.g. level 2 means 2×15=30 minutes of ominous trial |
| Impending death |  |  | _→0_ | _→0_ | Drowning, freezing damage, burning in fire/lava, starvation, suffocation, angry warden. Zero is the resulting health (death) |
| Hardcore mode |  |  | -☠ | -HC | The vanilla one, not the UHC mode of OneBar which has no indicators |

#### Mounts

| Effect | Color | Bar | Emoji | Text | Where | Description/comment |
|-|-|-|-|-|-|-|
| Mount health | Orange | █ | 1 | 1 | Above OneBar | Similar to OneBar itself |
| Mount health (extra symbols) | Orange | █ | 💝1 | mh1 | Above OneBar | When enabling "Show additional symbols" in Text options |
| Mount jump | Brown | ▄▄ or █ | 1.0 | 1.0 | Below crosshair | Vertical (horse and others) or horizontal (camel) bar, number shows approximate height of blocks the mount can jump (no est. for camels yet) | 
| Camel cooldown | Orange-red | ▄▄ | -1 | -1 | Below crosshair | Decreasing horizontal bar, number shows time in seconds before the camel can jump or move again | 

#### Armor

| Effect | Color | Bar | Emoji | Text | Where | Description/comment |
|-|-|-|-|-|-|-|
| Armor | White | ▔ |  |  | Above OneBar, above mount health | Equivalent of vanilla's armor indicator; also shown for horses if applicable |
| Armor durability | Light blue | ▔ |  |  | Overlaid on armor bar | Opt-in, width adapts to armor bar or armor bar segment |
| Segmented armor | White | ▔ |  |  | Above OneBar | Opt-in, similar to normal bar but each armor piece is a separate bar |
| Elytra durability | Purple | ▔ |  |  | Armor bar area | Normal armor bar: displayed as a full-width overlay only when flying; segmented armor bar: displayed constantly as a diamond-width bar at chest segment. |
| Mob head indicator |  |  | +a☻ | +aH | OneBar | Opt-in, "a" represents s̲keleton, c̲reeper, z̲ombie head; e̲nderman (carved pumpkin), p̲iglin (any golden armor) |

#### Others

| Effect | Color | Bar | Text | Where | Description/comment |
|-|-|-|-|-|-|
| Experience bar | Green | ▁ | 1 | Next to hotbar | Shown only when you have any XP (configurable) |
| Lapis Lazuli counter | Blue |  | 1 or 1× | Next to experience bar | Opt-in, shows either the raw count or times enchantable maxed out (XP level 30+) |
| Saturation | Orange | ▁ |  | Below OneBar | Opt-in |

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
