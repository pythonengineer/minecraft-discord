"use strict";

import { DiscordSDK } from "@discord/embedded-app-sdk";

let auth;

const discordSdk = new DiscordSDK(import.meta.env.VITE_DISCORD_CLIENT_ID);

function startGame() {
  setupDiscordSdk().then(() => {
    console.log("Discord SDK is authenticated");
    const server =
        location.host === "ws://localhost" ? `ws://localhost` : `wss://${location.host}/.proxy/minecraft`;
    window.minecraftOpts = {
        container: "game_frame",
        crashOnUncaughtExceptions: true,
        assetUrlPrefix: ".proxy/",
        username: auth.user.username.slice(0, 16),
        server: server,
        mpPass: auth.access_token
    };
    main();
  });
}

function isAndroid() {
  return /Android/i.test(navigator.userAgent);
}

if (isAndroid()) {
  document.addEventListener('click', function() {
    document.getElementById('android-message').style.display = 'none';
    startGame();
  });
} else {
  document.getElementById('android-message').style.display = 'none';
  startGame();
}

async function setupDiscordSdk() {
  await discordSdk.ready();
  console.log("Discord SDK is ready");

  const { code } = await discordSdk.commands.authorize({
    client_id: import.meta.env.VITE_DISCORD_CLIENT_ID,
    response_type: "code",
    state: "",
    prompt: "none",
    scope: [
      "identify",
      "guilds",
      "applications.commands"
    ],
  });

  // Retrieve an access_token from the activity's server
  // Note: We need to prefix our backend `/api/token` route with `/.proxy` to stay compliant with the CSP.
  // Read more about constructing a full URL and using external resources at
  // https://discord.com/developers/docs/activities/development-guides#construct-a-full-url
  const response = await fetch("/.proxy/api/token", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      code,
    }),
  });
  const { access_token } = await response.json();

  auth = await discordSdk.commands.authenticate({
    access_token,
  });

  if (auth == null) {
    throw new Error("Authenticate command failed");
  }
}
