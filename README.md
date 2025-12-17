# WI Zoom (Wurst-Imperium Zoom)

WI Zoom is the zoom from the [Wurst Client](https://go.wimods.net/from/github.com/Wurst-Imperium/WI-Zoom?to=https://www.wurstclient.net/) as a standalone Minecraft mod, letting you zoom in and out with the mouse wheel and providing up to 50x magnification without the need for a spyglass. This mod is fully compatible with OptiFine and Sodium, but unlike OptiFine's zoom, WI Zoom lets the camera move in a logical way, without making it wobble around. WI Zoom supports all of the latest Minecraft versions, including snapshots.

![comparison of no zoom, 3x zoom, and 50x zoom](https://img.wimods.net/github.com/Wurst-Imperium/WI-Zoom?to=https://user-images.githubusercontent.com/10100202/67816432-973d2400-fab2-11e9-8699-e05eb5ba6551.jpg)

## Downloads

[![Download WI Zoom](https://user-images.githubusercontent.com/10100202/214881367-956f0bc9-4dbe-43cb-850a-04d73e00b344.png)](https://go.wimods.net/from/github.com/Wurst-Imperium/WI-Zoom?to=https://www.wimods.net/wi-zoom/download/?utm_source=GitHub&utm_medium=WI+Zoom&utm_campaign=README.md&utm_content=WI+Zoom+GitHub+repo+download+button)

## Installation

> [!IMPORTANT]
> Always make sure that your modloader and all of your mods are made for the same Minecraft version. Your game will crash if you mix different versions.

### Installation using Fabric

1. Install [Fabric Loader](https://go.wimods.net/from/github.com/Wurst-Imperium/WI-Zoom?to=https://fabricmc.net/use/installer/).
2. Add [Fabric API](https://go.wimods.net/from/github.com/Wurst-Imperium/WI-Zoom?to=https://modrinth.com/mod/fabric-api) to your mods folder.
3. Add WI Zoom to your mods folder.

### Installation using NeoForge

1. Install [NeoForge](https://go.wimods.net/from/github.com/Wurst-Imperium/WI-Zoom?to=https://neoforged.net/).
2. Add WI Zoom to your mods folder.

## Features

- Up to 50x zoom!
- No <a href="https://go.wimods.net/from/github.com/Wurst-Imperium/WI-Zoom?to=https://minecraft.wiki/w/Spyglass" target="_blank">spyglass</a> needed!
- Zoom in and out with the mouse wheel!
- No wobbly camera nonsense!
- Dynamic mouse sensitivity! (since v1.1)
- Fully compatible with <a href="https://go.wimods.net/from/github.com/Wurst-Imperium/WI-Zoom?to=https://optifine.net/home" target="_blank">OptiFine</a> / <a href="https://go.wimods.net/from/github.com/Wurst-Imperium/WI-Zoom?to=https://github.com/CaffeineMC/sodium-fabric" target="_blank">Sodium</a>!

## How to zoom

Hold down the <kbd>V</kbd> key to activate the zoom (this keybind can be changed).  
While zooming, you can use the <kbd>mouse wheel</kbd> to zoom in further.

## Changing the zoom keybind

<details>
  <summary>How to change the keybind (click to expand)</summary>

  In the pause menu, click on "Options...".

  ![screenshot of the Game Menu with the Options button highlighted](https://user-images.githubusercontent.com/10100202/67876632-e0d45000-fb40-11e9-88a5-6d5d22cdb33a.png)

  In the Options menu, click on "Controls...".

  ![screenshot of the Options menu with the Controls button highlighted](https://user-images.githubusercontent.com/10100202/67876634-e0d45000-fb40-11e9-8e81-ef677755e1c3.png)
  
  In the Controls menu, scroll down to the "WI Zoom" section. If you don't have any other mods installed, you will find this section at the very bottom.

  ![screenshot of the Controls menu with the WI Zoom keybind highlighted at the bottom](https://user-images.githubusercontent.com/10100202/67876636-e16ce680-fb40-11e9-8934-ad65580dc91a.png)
</details>

## Supported languages

- Azerbaijani (Azerbaijan) (since v1.5)
- Chinese (Simplified/Mainland) (since v1.4)
- Chinese (Traditional/Taiwan) (since v1.4)
- Cantonese (Hong Kong) (since v1.4)
- Classical Chinese (since v1.4)
- Dutch (Netherlands) (since v1.5)
- English (US)
- Estonian (Estonia) (since v1.4)
- French (France) (since v1.4)
- German (Germany)
- Kurdish (since v1.5)
- Portuguese (Brazil) (since v1.5)
- Russian (Russia) (since v1.3)
- Turkish (Turkey) (since v1.5)
- Ukrainian (Ukraine) (since v1.5)

## Development Setup

> [!IMPORTANT]
> Make sure you have [Java Development Kit 25](https://adoptium.net/temurin/releases?version=25&os=any&arch=any) installed. It won't work with other versions.

### Development using Eclipse

1. Clone the repository:

   ```pwsh
   git clone https://github.com/Wurst-Imperium/WI-Zoom.git
   cd WI-Zoom
   ```

2. Generate the sources:

   In Fabric versions:
   ```pwsh
   ./gradlew genSources eclipse
   ```

   In NeoForge versions:
   ```pwsh
   ./gradlew eclipse
   ```

3. In Eclipse, go to `Import...` > `Existing Projects into Workspace` and select this project.

4. **Optional:** Right-click on the project and select `Properties` > `Java Code Style`. Then under `Clean Up`, `Code Templates`, `Formatter`, import the respective files in the `codestyle` folder.

### Development using VSCode / Cursor

> [!TIP]
> You'll probably want to install the [Extension Pack for Java](https://go.wimods.net/from/github.com/Wurst-Imperium/WI-Zoom?to=https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) to make development easier.

1. Clone the repository:

   ```pwsh
   git clone https://github.com/Wurst-Imperium/WI-Zoom.git
   cd WI-Zoom
   ```

2. Generate the sources:

   In Fabric versions:
   ```pwsh
   ./gradlew genSources vscode
   ```

   In NeoForge versions:
   ```pwsh
   ./gradlew eclipse
   ```
   (That's not a typo. NeoForge doesn't have `vscode`, but `eclipse` works fine.)

3. Open the `WI-Zoom` folder in VSCode / Cursor.

4. **Optional:** In the VSCode settings, set `java.format.settings.url` to `https://raw.githubusercontent.com/Wurst-Imperium/WI-Zoom/master/codestyle/formatter.xml` and `java.format.settings.profile` to `Wurst-Imperium`.

### Development using IntelliJ IDEA

I don't use or recommend IntelliJ, but the commands to run would be:

```pwsh
git clone https://github.com/Wurst-Imperium/WI-Zoom.git
cd WI-Zoom
./gradlew genSources idea --no-configuration-cache
```

**Note:** IntelliJ IDEA is [not yet compatible](https://github.com/FabricMC/fabric-loom/issues/1349) with Gradle's configuration cache. You will run into issues.

Possible workarounds:
- Turn off args files ([this setting](https://i.imgur.com/zHqIOYg.png)). Won't work for some users because of a command length limit.
- Add `--no-configuration-cache` to all of your Gradle commands.
