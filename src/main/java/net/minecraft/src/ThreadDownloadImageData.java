package net.minecraft.src;

import net.lax1dude.eaglercraft.opengl.ImageData;

public class ThreadDownloadImageData {
	public ImageData image;
	public int referenceCount = 1;
	public int textureName = -1;
	public boolean textureSetupComplete = false;

	public ThreadDownloadImageData(String var1, ImageBuffer var2) {
		(new ThreadDownloadImage(this, var1, var2)).start();
	}
}
