## Minecraft on Discord

![Minecraft](/screenshot.png?raw=true)

_**Minecraft on Discord**_ is a project that brings every version of Minecraft to Discord's activities on web, mobile, and desktop by converting
its Java engine to modern WebGL-compatible JavaScript and providing a web proxy that your embedded Discord application can use.

This version is **0.0.16a_02**, a *Multiplayer Classic* version of the game which was released on _**June 7, 2009**_.

The modern OpenGL pipeline and the TeaVM application that allows Minecraft to be run in the web was created for *Eaglercraft* by *lax1dude* and *ayunami2000*,
along with the touch support for mobile users. Much of the credit for this project therefore belongs to them.

You can learn about the version itself [on the Minecraft wiki](https://minecraft.wiki/w/Java_Edition_Classic_0.0.16a_02).

### Usage

To create a Minecraft activity on Discord, first [follow Discord's instructions on setting up an application](https://discord.com/developers/docs/activities/building-an-activity#step-2-creating-an-app).

You will need to create a `.env` file as shown in the instructions and place it in the "discord" folder.

The "discord/client" folder contains the embedded web application. Once you have ran `CompileJS` which compiles the JavaScript classes and moves them there, you can run Vite
and configure your Discord app to point to the domain of your public endpoint as demonstrated in Discord's example.

The "discord/server" folder contains the *websockify* proxy that transits traffic to and from the regular Java Minecraft server.
It also authenticates clients running your activity with Discord's API. For multiplayer to work, you will need to configure a `/minecraft` URL mapping in Discord that points to the proxy endpoint's domain.

Run `npm install` in the "discord/client" or "discord/server" projects to install dependencies and `npm run dev` to run them.

As all endpoints must be HTTPS secure, a good way to set up public endpoints for both the embedded application server
and the multiplayer proxy server is to use something like *cloudflared* or *ngrok* and have a domain pointed at port 5173 for the embedded app and one at port 3000 for the websocket proxy.

The Minecraft server itself is in the "server" folder of the repository. Clients need not connect directly to it thanks to the websocket proxy.
If the Minecraft server you want to connect to isn't available at `127.0.0.1:25565` for the websocket, you will have to edit `discord/server/websockify.js`.

If you make any code modifications to the game, remember that caching exists and you may need to increment the `classes.js?v` parameter in `discord/index.html` for the changes to take effect on Discord.

Uncommenting the Eruda script in `discord/index.html` will enable a web debugging console that works sometimes on mobile.
You can also set `openDebugConsoleOnLaunch` to 'true' in `window.minecraftOpts` in the same file or use Discord's development console.

### Gameplay on mobile

From left to right at the top are the buttons to teleport the player to spawn, pause the game, chat, and save the spawn position.

Tap the block on the top left to switch blocks. The most bottom right button is jump and the one above it toggles block picking mode.

### Notes

All textures are in the `discord/assets` folder, all created either by Mojang Specifications or Eaglercraft.

The Minecraft source code for this version was obtained using [RetroMCP-Java](https://github.com/MCPHackers/RetroMCP-Java).

Credit for Minecraft should go towards its creator Notch.
