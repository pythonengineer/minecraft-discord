## Minecraft on Discord

![Minecraft](/screenshot.png?raw=true)

_**Minecraft on Discord**_ is a project that brings every version of Minecraft to Discord's activities on web, mobile, and desktop by converting
its Java engine to modern WebGL-compatible JavaScript and providing a web proxy that your embedded Discord application can use.

This version is **0.24_SURVIVAL_TEST_03**, a *Survival Test Classic* version of the game which was released on _**September 1, 2009**_.

The modern OpenGL pipeline and the TeaVM application that allows Minecraft to be run in the web was created for *Eaglercraft* by *lax1dude* and *ayunami2000*,
along with the touch support for mobile users. Much of the credit for this project therefore belongs to them.

You can learn about the version itself [on the Minecraft wiki](https://minecraft.wiki/w/Java_Edition_Classic_0.24_SURVIVAL_TEST_03).

### Usage

To create your own Minecraft activity on Discord, first [follow Discord's instructions on setting up an application](https://discord.com/developers/docs/activities/building-an-activity#step-2-creating-an-app).

You will need to create a `.env` file as shown in the instructions and place it in the "discord" folder.

The "discord/client" folder contains the embedded web application. Once you have ran `CompileJS` which compiles the JavaScript classes and moves them there, you can run Vite
and configure your Discord app to point to the domain of your public endpoint as demonstrated in Discord's example.

Run `npm install` in the "discord/client" project to install dependencies and `npm run build` to build the app (must be ran whenever changes are made).
You can run `npm run preview` to start a web server, or move the contents of the built "discord/client/dist" folder to your own web server.

As all endpoints must be HTTPS secure, a good way to set up public endpoints for the embedded application server
is to use something like *cloudflared* or *ngrok* and have a domain pointed at port 4173 for the embedded app if you're not running your own web server and domain.

If you make any code modifications to the game, remember that caching exists and you may need to increment the `classes.js?v` parameter in `discord/index.html` for the changes to take effect on Discord.

Uncommenting the Eruda script in `discord/index.html` will enable a web debugging console that works sometimes on mobile.
You can also set `openDebugConsoleOnLaunch` to 'true' in `window.minecraftOpts` in the same file or use Discord's development console.

### Notes

All textures and sound files are in the `discord/client/public/assets` folder, all created either by Mojang Specifications or Eaglercraft.

The Minecraft source code for this version was obtained using [RetroMCP-Java](https://github.com/MCPHackers/RetroMCP-Java).

Credit for Minecraft should go towards its creator Notch.
