# OneBar

A HUD mod for Minecraft 1.16.x using Fabric mod loader. 

Work in progress, Curseforge and Modrinth releases will come when stable enough. Right now the JARs can be found from Actions tab.

## How it works

OneBar is meant to be contextual, showing you the values (and bar overlays) only when you need them. The positive effects, including health itself, are displayed left-to-right, negative effects right-to-left. 

For example, if you get hungry, the hunger overlay is displayed over the health by its value. If you eat, you'll lose the overlay and the value, because you no longer need it. However, if you are hungry and underwater, the water bar is displayed on top of health _and_ hunger because it is the most important value in this case.

### Bars and values

#### Positive (left-to-right)

* Health - red bar, **number**
* Armor - white bar on top, no number
* Absorption - no bar, **+number**
* Health boost - same as health, but changes bars widths to fit the max health

#### Negative (right-to-left)

* Hunger - brown bar, **-number**
* Air - blue bar, **-anumber**
* Hardcore mode - no bar, **!**

![](https://i.ibb.co/z2RP4gw/2021-01-31-21-42-18.png)
