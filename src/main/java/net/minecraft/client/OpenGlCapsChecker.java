package net.minecraft.client;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public class OpenGlCapsChecker {
    public boolean checkARBOcclusion() {
        return false & GL11.checkOcclusionQuerySupport();
    }
}
