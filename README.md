## Minecraft on Discord

![Minecraft](/screenshot.png?raw=true)

_**Minecraft on Discord**_ is a project that brings every version of Minecraft to Discord's activities on web, mobile, and desktop by converting
its Java engine to modern WebGL-compatible JavaScript and providing a web proxy that your embedded Discord application can use.

This version is **Beta 1.7.3**, a *Beta* version of the game which was released on _**July 8, 2011**_. This is the final version without creative mode or the hunger bar.

The modern OpenGL pipeline and the TeaVM application that allows Minecraft to be run in the web was created for *Eaglercraft* by *lax1dude* and *ayunami2000*,
along with the touch support for mobile users. Much of the credit for this project therefore belongs to them.

You can learn more about this particular version [on the Minecraft wiki](https://minecraft.wiki/w/Java_Edition_Beta_1.7.3).

### Setup

To create your own Minecraft activity on Discord, first [follow Discord's instructions on setting up an application](https://discord.com/developers/docs/activities/building-an-activity#step-2-creating-an-app).

You will need to create a `.env` file as shown in the instructions and place it in the "discord" folder.

The "discord/client" folder contains the embedded web application. Once you have ran `CompileJS` which compiles the JavaScript classes and moves them there, you can run Vite
and configure your Discord app to point to the domain of your public endpoint as demonstrated in Discord's example.

The "discord/server" folder contains the websockify proxy that transits traffic to and from the regular Java Minecraft server.
It also authenticates clients running your activity with Discord's API.
For multiplayer to work, you will need to configure a `/minecraft` URL mapping in Discord that points to an HTTPS domain representing the proxy.

Run `npm install` in the "discord/client" or "discord/server" projects to install dependencies and `npm run build` to build the app (must be ran whenever changes are made).
You can run `npm run preview` to start a web server, or move the contents of the built "discord/client/dist" folder to your own web server.

As all endpoints must be HTTPS secure, a good way to set up public endpoints for both the embedded application web server and the multiplayer proxy server
is to use something like *cloudflared* or *ngrok* and have a domain pointed at port 4173 for the embedded app and port 3000 for the websocket proxy if you don't have your own domains.

The Minecraft server (Bukkit) itself is in the "server" folder of the repository. Clients need not connect directly to it thanks to the websocket proxy.
If the Minecraft server you want to connect to isn't available at `127.0.0.1:25565` for the websocket, you will need to edit `discord/server/websockify.js`.
Make sure `online-mode` is set to false in the server configuration.

Uncommenting the Eruda script in `discord/index.html` will enable a web debugging console that works sometimes on mobile.
You can also set `openDebugConsoleOnLaunch` to 'true' in `window.minecraftOpts` in the same file or use Discord's development console.

Player skins will load, but due to the CSP requirements on some browsers you will have to also add a `/skin` URL mapping and a `/skindb` mapping to the Discord app.
The target for `/skin` should be `textures.minecraft.net` and your `/skindb` target should be `playerdb.co`.

Skins are found using the authenticated Discord username,
however you may add aliases for Discord usernames so they log-in with custom player names in `discord/client/public/aliases.txt`.

### Notes

The modern inventory which features item dragging, shift-click, and double-click has been backported to this version for touchscreen convenience.

There may be an exiting issue on Discord iOS related to memory consumption. If encountered, try turning off music and it might be resolved.

All textures and sound files are in the `discord/client/public/assets` folder, all created either by Mojang Specifications or Eaglercraft.

The Minecraft source code for this version was obtained using [RetroMCP-Java](https://github.com/MCPHackers/RetroMCP-Java).

Replacement rain sounds sourced from [here.](https://www.curseforge.com/minecraft/texture-packs/gentler-rain-sounds)

Credit for Minecraft should go towards its creator Notch.
