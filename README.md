## Minecraft on Discord

![Minecraft](/screenshot.png?raw=true)

_**Minecraft on Discord**_ is a project that brings every version of Minecraft to Discord activities by converting the Java engine to modern WebGL-compatible JavaScript
and providing a web server using *Vite* that can be linked to your Discord application.

This version is **rd-161348**, the fourth version of the game which was released on _**May 16, 2009**_.

The modern OpenGL pipeline and the TeaVM application that allows Minecraft to be run in the web was created for *Eaglercraft* by *lax1dude* and *ayunami2000*,
along with the touch support for mobile users. Much of the credit for this project therefore belongs to them.

You can learn about the version itself [on the Minecraft wiki](https://minecraft.wiki/w/Java_Edition_pre-Classic_rd-161348).

### Usage

To create a Minecraft activity on Discord, first [follow Discord's instructions on setting up an application](https://discord.com/developers/docs/activities/building-an-activity#step-2-creating-an-app).

The "discord" folder contains the web application. Once you have ran `CompileJS` which compiles the JavaScript classes and moves them there, you can run Vite
and configure your Discord app to point to the URL of your public endpoint as demonstrated in Discord's example.

If you make any source code modifications, remember that caching exists and you may need to increment the `classes.js?v` parameter in `discord/index.html` for the changes to take effect on Discord.

Uncommenting the Eruda script in `discord/index.html` will enable a web debugging console that works on Discord (and sometimes on mobile).
You can also set `openDebugConsoleOnLaunch` to 'true' in `window.minecraftOpts` in the same file.

### Gameplay changes (non-mobile)

You can delete the saved level file by pressing backspace.

### Gameplay changes (mobile)

You can delete the saved level file by tapping the button second from right at the top.

The button to the right of the delete level file saves the level. The leftmost button resets your position and the button to the right of that spawns a human.

Tap the block on the top left to switch blocks. The most bottom right button is jump and the one above it toggles block picking.

### Notes

All textures are in the `discord/assets` folder, all created either by Mojang Specifications or Eaglercraft.

The Minecraft source code for this version was obtained using [RetroMCP-Java](https://github.com/MCPHackers/RetroMCP-Java).

Credit for Minecraft should go towards its creator Notch.
