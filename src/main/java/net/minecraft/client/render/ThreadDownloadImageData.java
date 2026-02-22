package net.minecraft.client.render;

import net.lax1dude.eaglercraft.opengl.ImageData;

public final class ThreadDownloadImageData {
    public ImageData image;
    public int referenceCount = 1;
    public int textureIntDownload = -1;
    public boolean textureSetupComplete = false;

    public ThreadDownloadImageData(String var1, ImageBufferDownload var2) {
        (new ThreadDownloadImage(this, var1, var2)).start();
    }
}
