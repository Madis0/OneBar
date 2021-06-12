# OneBar

A sleek HUD mod for Minecraft: Java Edition using Fabric mod loader. 

[![Download from Curseforge](http://cf.way2muchnoise.eu/full_onebar_downloads%20on%20Curseforge.svg?badge_style=for_the_badge)](https://www.curseforge.com/minecraft/mc-mods/onebar)

Stable releases downloadable from [Curseforge](https://www.curseforge.com/minecraft/mc-mods/onebar) or [Modrinth](https://modrinth.com/mod/OneBar), automated builds available from [Actions tab](https://github.com/Madis0/OneBar/actions).

![](https://i.ibb.co/XtPJdcy/image.png)

## Installation

1. Install Minecraft 1.17 or later
2. Install [Fabric Loader](https://fabricmc.net/use/)
3. Download OneBar, [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api), [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu), [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config)
4. Place the JARs you downloaded to your Minecraft's mods folder
5. Run the game!

## How it works

OneBar is meant to be contextual, showing you the values (and bar overlays) only when you need them. The positive effects, including health itself, are displayed left-to-right, negative effects right-to-left. 

For example, if you get hungry, the hunger overlay is displayed over the health by its value. If you eat, you'll lose the overlay and the value, because you no longer need it. However, if you are hungry and underwater, the water bar is displayed on top of health _and_ hunger because it is the most important value in this case.

The mod only uses client-side data, so it is never needed on a server. There are also settings for configuring the colors and visibility of various elements.

### Bars and values

#### Positive (left-to-right)

* Health - red bar, **number**, where number is your health points
* Health boost - red bar, **number**, bar width adapts to the max health
* Natural regeneration - yellow bar, **→number** where number is the resulting health¹
* Regeneration - pink bar, **→number** where number is the resulting health¹
* Absorption - no bar, **+number**, where number is your absorption health points
* Resistance - no bar, **+⛨number%** or **+rnumber%** where number is the [effect level multiplier](https://minecraft.gamepedia.com/Resistance#Effect)²
* Fire Resistance - no bar, **-~🔥number×~** or **-~bnumber×~** where number is a rough damage multiplier (1× - burning, 2× - burning in fire, 4× - burning in lava)²
* Water Breathing/Conduit Power - blue bar, **-~⭘number~** or **-~anumber~**, number is usually zero and bar hidden unless you got water breathing _within_ water²

#### Negative (right-to-left)

* Hunger - brown bar, **-number**, where number is calculated as 20-food points (e.g. if you had 15 food points, it would show 5)
* Air - blue bar, **-⭘number** or **-anumber**, where number is the counter up to 20 until drowning (the equivalent of broken bubbles in vanilla)²
* Freezing - light gray bar, **-❄number** or **-fnumber**, where number is the counter up to 20 until freezing (the equivalent of frost fading in vanilla)²
* Burning - orange full bar, **-🔥number×** or **-bnumber×** where number is a rough damage multiplier (1× - burning, 2× - burning in fire, 4× - burning in lava)²
* Hunger effect - yellowish brown bar, **→number** where number is the resulting hunger¹
* Poison - yellowish green bar, **→number** where number is the resulting health¹
* Wither - dark gray bar, **→number** where number is the resulting health¹
* Hardcore mode - no bar, **☠** or **HC**²

#### Others

* Mount health - orange bar above OneBar, similar style
* Horse jump - brown vertical bar below crosshair
* Armor - white bar above OneBar, no number
* Held food restored hunger bar - orange (wasted) or green (exact/less) bar below OneBar, no number
* Experience points - level count and bar to the right of hotbar
* Vanilla-like fractionated counters - shows values in vanilla hearts, e.g. 9,5 instead of 19
* Zero saturation - no bar, **↓** - shows when saturation is 0 and hunger will decrease with activity

¹ Best-guess estimate based on what the client knows at the time, can change by some server-side values and natural occurrences. Can be disabled in options.

² The emoji are white and look a bit different in-game. Can be disabled in settings to show letters instead.

## Mods I recommend with OneBar

* [BerdinskiyBear's Armor HUD](https://www.curseforge.com/minecraft/mc-mods/berdinskiybears-armor-hud) - adds a customizable hotbar-like armor bar
* [Food Highlight](https://www.curseforge.com/minecraft/mc-mods/food-highlight) - shows orange (wasted) and green (exact/less) indicators on food in hotbar and inventories
* [Enhanced Attack Indicator](https://www.curseforge.com/minecraft/mc-mods/enhanced-attack-indicator) - adds more features to the attack cooldown, e.g drawing a bow
* [Till it Breaks](https://www.curseforge.com/minecraft/mc-mods/till-it-breaks) - adds numeric durability and arrow count (for bows and crossbows) to hotbar items

## FAQ

**Q: Why do I have to download [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config) separately?**

A: Because it's big, gets updated a lot and you probably have a mod or two that requires it anyway. If one of your mods already bundles it, you don't have to download it at all :)

**Q: How do I edit the colors?**

A: Click Esc -> Mods -> OneBar -> config button. Then, for each color field you can use [this tool](https://htmlcolorcodes.com/color-picker/) for choosing a color and [this list](https://gist.github.com/lopspower/03fb1cc0ac9f32ef38f4#all-hex-value-from-100-to-0-alpha) for choosing the transparency.

**Q: Forge support?**

A: No. Feel free to fork according to the license or [check out my resource pack that inspired this](https://www.curseforge.com/minecraft/texture-packs/material-design-hud).

**Q: Backports?**

A: No, Cloth Config isn't cross-compatible and I don't suggest using old versions anyway.

**Q: Snapshots?**

A: Somewhat. It probably runs, but config will crash. You've been warned!
