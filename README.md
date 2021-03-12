# OneBar

A sleek HUD mod for Minecraft: Java Edition. 

Stable releases downloadable from [Curseforge](https://www.curseforge.com/minecraft/mc-mods/onebar) or [Modrinth](https://modrinth.com/mod/OneBar), automated builds available from [Actions tab](https://github.com/Madis0/OneBar/actions).

## Installation

1. Install Minecraft 1.16.x
2. Install [Fabric Loader](https://fabricmc.net/use/)
3. Download OneBar, [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api), [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu), [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config)
4. Place the JARs you downloaded to your Minecraft's mods folder
5. Run the game!

## How it works

OneBar is meant to be contextual, showing you the values (and bar overlays) only when you need them. The positive effects, including health itself, are displayed left-to-right, negative effects right-to-left. 

For example, if you get hungry, the hunger overlay is displayed over the health by its value. If you eat, you'll lose the overlay and the value, because you no longer need it. However, if you are hungry and underwater, the water bar is displayed on top of health _and_ hunger because it is the most important value in this case.

There are also settings for configuring the colors and visibility of various elements.

[Feature roadmap](https://github.com/Madis0/OneBar/projects)

### Bars and values

#### Positive (left-to-right)

* Health - red bar, **number**
* Armor - white bar on top, no number
* Absorption - no bar, **+number**
* Health boost - same as health, but changes bars widths to fit the max health
* Resistance - no bar, **+number%** where number is the [effect level multiplier](https://minecraft.gamepedia.com/Resistance#Effect)
* Regeneration - pink bar, **→number** where number is the resulting health
* Fire Resistance - same as fire, but crossed number
* Water Breathing - same as air, but crossed number

#### Negative (right-to-left)

* Hunger - brown bar, **-number**
* Air - blue bar, **-anumber**
* Fire - orange bar, **-fnumber×** where number is a rough damage multiplier (1× - burning, 2× - burning in fire, 4× - burning in lava)
* Hardcore mode - no bar, **!**

#### Others

* Mount health - orange bar above OneBar, similar style
* Horse jump - brown vertical bar below crosshair
* Experience points - level count and bar to the right of hotbar 

![](https://i.ibb.co/Jcs3ys8/2021-02-19-20-01-43.png)

### FAQ

**Q: Forge support?**
A: No. Feel free to fork according to the license or [check out my resource pack that inspired this](https://www.curseforge.com/minecraft/texture-packs/material-design-hud).

**Q: Backports?**
A: No, Cloth Config isn't cross-compatible and I don't suggest using old versions anyway.

**Q: Snapshots?**
A: Somewhat. It probably runs, but config will crash. You've been warned!
