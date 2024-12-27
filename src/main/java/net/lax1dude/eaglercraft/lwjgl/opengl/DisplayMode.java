package net.lax1dude.eaglercraft.lwjgl.opengl;

import net.lax1dude.eaglercraft.internal.PlatformInput;

public class DisplayMode {
    private int width;
    private int height;

    public DisplayMode(int width, int height) {
        this.width = width;
        this.height = height;
        PlatformInput.setSize(this.width, this.height);
    }

    public int getWidth() {
        return PlatformInput.getWindowWidth();
    }

    public int getHeight() {
        return PlatformInput.getWindowHeight();
    }
}
