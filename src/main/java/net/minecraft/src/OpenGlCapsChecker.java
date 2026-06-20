package net.minecraft.src;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public class OpenGlCapsChecker {
	private static boolean tryCheckOcclusionCapable = false;

	public boolean checkARBOcclusion() {
		return tryCheckOcclusionCapable && GL11.checkOcclusionQuerySupport();
	}
}
