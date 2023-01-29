![comparison of no zoom, 3x zoom, and 50x zoom](https://user-images.githubusercontent.com/10100202/67816432-973d2400-fab2-11e9-8699-e05eb5ba6551.jpg)

# WI Zoom (Wurst-Imperium Zoom)

The zoom from the [Wurst Client](https://www.wurstclient.net/) as a standalone mod. The WI Zoom Minecraft mod lets you zoom in and out with the mouse wheel, providing up to 50x magnification without the need for a spyglass. This mod is fully compatible with OptiFine and Sodium, but unlike OptiFine's zoom, WI Zoom lets the camera move in a logical way, without making it wobble around. WI Zoom supports all of the latest Minecraft versions, including snapshots.

![Requires Fabric API](https://user-images.githubusercontent.com/10100202/93722968-0aec9180-fb9b-11ea-9983-bc0fc51b47ab.png)

## Downloads (for users)

[![Download WI Zoom](https://user-images.githubusercontent.com/10100202/214881367-956f0bc9-4dbe-43cb-850a-04d73e00b344.png)](https://www.wurstimperium.net/wi-zoom/download/?utm_source=GitHub&utm_medium=WI-Zoom&utm_campaign=README.md&utm_content=Download+WI+Zoom)

## Setup (for developers)

(This assumes that you are using Windows with [Eclipse](https://www.eclipse.org/downloads/) and [Java Development Kit 17](https://adoptium.net/?variant=openjdk17&jvmVariant=hotspot) already installed.)

1. Clone / download the repository.

2. Run these two commands in PowerShell:

   ```powershell
   ./gradlew.bat genSources
   ./gradlew.bat eclipse
   ```

3. In Eclipse, go to `Import...` > `Existing Projects into Workspace` and select this project.

## Features

- Up to 50x zoom!
- No <a href="https://minecraft.fandom.com/wiki/Spyglass" target="_blank">spyglass</a> needed!
- Zoom in and out with the mouse wheel!
- No wobbly camera nonsense!
- Dynamic mouse sensitivity! (since v1.1)
- Fully compatible with <a href="https://optifine.net/home" target="_blank">OptiFine</a> / <a href="https://github.com/CaffeineMC/sodium-fabric" target="_blank">Sodium</a>!

## How To Zoom

Hold down the <kbd>V</kbd> key to activate the zoom (this keybind can be changed).  
While zooming, you can use the <kbd>mouse wheel</kbd> to zoom in further.

## Changing The Zoom Keybind

<details>
  <summary>How to change the keybind (click to expand)</summary>

  In the pause menu, click on "Options...".

  <img src="https://user-images.githubusercontent.com/10100202/67876632-e0d45000-fb40-11e9-88a5-6d5d22cdb33a.png" alt="screenshot of the Game Menu with the Options button highlighted" width="1113" height="832" />

  In the Options menu, click on "Controls...".

  <img src="https://user-images.githubusercontent.com/10100202/67876634-e0d45000-fb40-11e9-8e81-ef677755e1c3.png" alt="screenshot of the Options menu with the Controls button highlighted" width="1113" height="779" />
  
  In the Controls menu, scroll down to the "WI Zoom" section. If you don't have any other mods installed, you will find this section at the very bottom.

  <img src="https://user-images.githubusercontent.com/10100202/67876636-e16ce680-fb40-11e9-8934-ad65580dc91a.png" alt="screenshot of the Controls menu with the WI Zoom keybind highlighted at the bottom" width="1113" height="599" />
</details>

## Supported Languages
- Chinese (Simplified/Mainland) (since v1.4)
- Chinese (Traditional/Taiwan) (since v1.4)
- Cantonese (Hong Kong) (since v1.4)
- Classical Chinese (since v1.4)
- English (US)
- Estonian (Estonia) (since v1.4)
- French (France) (since v1.4)
- German (Germany)
- Russian (Russia) (since v1.3)

## Will you add Forge support?

Aside from the 1.12.2 Forge version that already exists, no. A modern Forge version is not being considered due to <a href="https://forums.minecraftforge.net/topic/58706-regarding-minecraft-112-and-policy-changes/" target="_blank" rel="noopener noreferrer">Forge's new CoreMod policy</a> (effective since 1.13) as well as issues with the <a href="https://web.archive.org/web/20201125032822/https://gist.github.com/jellysquid3/629eb84a74ab326046faf971150dc6c3" target="_blank" rel="noopener noreferrer">Forge community's treatment of modders</a>.

However, the experimental&nbsp;<a href="https://patchworkmc.net/">Patchwork</a> project can sometimes make Forge mods and Fabric mods work together. Depending on which other mods you use, this might work with WI Zoom.

Also, keep in mind that many former Forge mods now have Fabric versions (or have switched to Fabric entirely due to the above-mentioned issues).
