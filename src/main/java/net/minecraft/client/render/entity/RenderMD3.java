package net.minecraft.client.render.entity;

import java.io.IOException;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.model.md3.MD3Loader;
import net.minecraft.client.model.md3.MD3Model;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;

public final class RenderMD3 extends Render {
	private MD3Model[] model = new MD3Model[1];

	public RenderMD3() {
		this.shadowSize = 0.5F;

		try {
			this.model[0] = new MD3Model((new MD3Loader()).loadModel("/test2.md3"));
		} catch (IOException var2) {
			var2.printStackTrace();
		}
	}

	public final void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6) {
		EntityLiving var10001 = (EntityLiving)var1;
		var6 = var6;
		var5 = var4;
		var4 = var3;
		var3 = var2;
		EntityLiving var9 = var10001;
		RenderMD3 var8 = this;
		GL11.glPushMatrix();

		try {
			var2 = var9.prevRenderYawOffset + (var9.renderYawOffset - var9.prevRenderYawOffset) * var6;
			GL11.glTranslatef(var3, var4, var5);
			var8.loadTexture("/cube-nes.png");
			GL11.glRotatef(-var2 + 180.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glScalef(0.02F, -0.02F, 0.02F);
			GL11.glEnable(GL11.GL_NORMALIZE);
			var8.model[0].renderModelVertices(0, 0, 0.0F);
			GL11.glDisable(GL11.GL_NORMALIZE);
		} catch (Exception var7) {
			var7.printStackTrace();
		}

		GL11.glPopMatrix();
	}
}
