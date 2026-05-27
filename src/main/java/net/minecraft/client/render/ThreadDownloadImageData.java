package net.minecraft.client.render;

import net.lax1dude.eaglercraft.opengl.ImageData;

public class ThreadDownloadImageData {
    public ImageData image;
    public int referenceCount = 1;
    public int textureName = -1;
    public boolean textureSetupComplete = false;

    public ThreadDownloadImageData(String username, ImageBuffer imageBuffer) {
        (new ThreadDownloadImage(this, username, imageBuffer)).start();
    }
}
