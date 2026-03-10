package net.minecraft.client.render;

import java.io.ByteArrayInputStream;

import org.json.JSONObject;

import net.ellerton.japng.Png;
import net.ellerton.japng.argb8888.Argb8888Bitmap;
import net.ellerton.japng.error.PngException;
import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.opengl.ImageData;

final class ThreadDownloadImage extends Thread {
    private String username;
    private ImageBufferDownload buffer;
    private ThreadDownloadImageData imageData;

    ThreadDownloadImage(ThreadDownloadImageData downloadImageDataThread, String username, ImageBufferDownload imageBufferDownloader) {
        this.imageData = downloadImageDataThread;
        this.username = username;
        this.buffer = imageBufferDownloader;
    }

    public final void run() {
        byte[] data = EagRuntime.downloadSkinDbURL(this.username);
        if (data == null) {
            return;
        }

        JSONObject d = new JSONObject(new String(data));
        String textureUrl = d.getJSONObject("data").getJSONObject("player").getString("skin_texture");
        if (textureUrl == null || textureUrl.isEmpty()) {
            return;
        }

        textureUrl = textureUrl.replace("http:", "https:");
        try {
            Thread.sleep(5L);
        } catch (InterruptedException e1) {
        }

        data = EagRuntime.downloadSkinURL(textureUrl);
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        Argb8888Bitmap png = null;
        try {
            png = Png.readArgb8888Bitmap(is);
        } catch (PngException e) {
            e.printStackTrace();
            return;
        }

        if (png == null) {
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
        if(this.buffer == null) {
            this.imageData.image = tex;
        } else {
            this.imageData.image = this.buffer.parseUserSkin(tex);
        }
    }
}