package net.minecraft.client;

import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.game.physics.Vec3D;

public class RenderHelper {
	private static FloatBuffer colorBuffer = GLAllocation.createFloatBuffer(16);

	public static void disableStandardItemLighting() {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LIGHT0);
		GL11.glDisable(GL11.GL_LIGHT1);
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
	}

	public static void enableStandardItemLighting() {
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
        Vec3D vec3D0 = (Vec3D.createVector((double)0.7F, 1.0D, -0.20000000298023224D)).normalize();
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, setColorBuffer(vec3D0.xCoord, vec3D0.yCoord, vec3D0.zCoord, 0.0D));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, setColorBuffer(0.6F, 0.6F, 0.6F, 1.0F));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        vec3D0 = (Vec3D.createVector(-0.699999988079071D, 1.0D, (double)0.2F)).normalize();
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, setColorBuffer(vec3D0.xCoord, vec3D0.yCoord, vec3D0.zCoord, 0.0D));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, setColorBuffer(0.6F, 0.6F, 0.6F, 1.0F));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, setColorBuffer(0.4F, 0.4F, 0.4F, 1.0F));
    }

	private static FloatBuffer setColorBuffer(double r, double g, double b, double a) {
		return setColorBuffer((float)r, (float)g, (float)b, (float)a);
	}

	private static FloatBuffer setColorBuffer(float r, float g, float b, float a) {
		colorBuffer.clear();
		colorBuffer.put(r).put(g).put(b).put(a);
		colorBuffer.flip();
		return colorBuffer;
	}
}
