package com.mojang.minecraft.net;

import java.io.ByteArrayInputStream;

import org.json.JSONObject;

import net.ellerton.japng.Png;
import net.ellerton.japng.argb8888.Argb8888Bitmap;
import net.ellerton.japng.error.PngException;
import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.opengl.ImageData;

final class NetworkPlayerTextureLoader extends Thread {
    private NetworkPlayer player;

    NetworkPlayerTextureLoader(NetworkPlayer player) {
        this.player = player;
    }

    public final void run() {
        byte[] data = EagRuntime.downloadRemoteURL("https://playerdb.co/api/player/minecraft/" + this.player.name);
        if (data == null) {
            System.out.println("Failed to load texture for " + this.player.name);
            return;
        }

        JSONObject d = new JSONObject(new String(data));
        String textureUrl = d.getJSONObject("data").getJSONObject("player").getString("skin_texture");
        if (textureUrl == null || textureUrl.isEmpty()) {
            System.out.println("Failed to load texture for " + this.player.name);
            return;
        }

        textureUrl = textureUrl.replace("http:", "https:");
        try {
            Thread.sleep(5L);
        } catch (InterruptedException e1) {
        }

        data = EagRuntime.downloadRemoteURL(textureUrl);
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        Argb8888Bitmap png = null;
        try {
            png = Png.readArgb8888Bitmap(is);
        } catch (PngException e) {
            e.printStackTrace();
            System.out.println("Failed to load texture for " + this.player.name);
            return;
        }

        if (png == null) {
            System.out.println("Failed to load texture for " + this.player.name);
            return;
        }

        int len = png.getWidth() * png.getHeight();
        int[] skin = new int[len];
        for (int i = 0; i < skin.length; ++i)
        {
            int color = png.array[i];
            skin[i] = (color & 0xFF00FF00) | ((color & 0x00FF0000) >>> 16) | ((color & 0x000000FF) << 16);
        }

        ImageData tex = new ImageData(64, 32, skin, true).getSubImage(0, 0, 64, 32);

        System.out.println("Loading texture for " + this.player.name);
        this.player.newTexture = tex;
    }
}
