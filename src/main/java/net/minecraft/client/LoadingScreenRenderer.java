package net.minecraft.client;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.lwjgl.opengl.Display;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.render.Tessellator;

public final class LoadingScreenRenderer {
	private String text = "";
	public Minecraft minecraft;
	public String title = "";
	private long start = EagRuntime.currentTimeMillis();

	public LoadingScreenRenderer(Minecraft var1) {
		this.minecraft = var1;
	}

	public final void displayProgressMessage(String var1) {
		if(!this.minecraft.running) {
			throw new MinecraftError();
		} else {
			this.text = var1;
			this.setLoadingProgress(-1);
		}
	}

	public final void setLoadingProgress(int var1) {
		if(!this.minecraft.running) {
			throw new MinecraftError();
		} else {
			long var2 = EagRuntime.currentTimeMillis();
			if(var2 - this.start >= 20L) {
				this.start = var2;
				int var8 = this.minecraft.scaledResolution.getScaledWidth();
				int var3 = this.minecraft.scaledResolution.getScaledHeight();
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
				Tessellator var4 = Tessellator.instance;
				int var5 = this.minecraft.renderEngine.getTexture("/dirt.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5);
				float var9 = 32.0F;
				var4.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
				var4.setColorOpaque_I(4210752);
				var4.addVertexWithUV(0.0F, (float)var3, 0.0F, 0.0F, (float)var3 / var9);
				var4.addVertexWithUV((float)var8, (float)var3, 0.0F, (float)var8 / var9, (float)var3 / var9);
				var4.addVertexWithUV((float)var8, 0.0F, 0.0F, (float)var8 / var9, 0.0F);
				var4.addVertexWithUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
				var4.draw();
				if(var1 >= 0) {
					var5 = var8 / 2 - 50;
					int var6 = var3 / 2 + 16;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					var4.startDrawingQuads(DefaultVertexFormats.POSITION_COLOR);
					var4.setColorOpaque_I(8421504);
					var4.addVertex((float)var5, (float)var6, 0.0F);
					var4.addVertex((float)var5, (float)(var6 + 2), 0.0F);
					var4.addVertex((float)(var5 + 100), (float)(var6 + 2), 0.0F);
					var4.addVertex((float)(var5 + 100), (float)var6, 0.0F);
					var4.setColorOpaque_I(8454016);
					var4.addVertex((float)var5, (float)var6, 0.0F);
					var4.addVertex((float)var5, (float)(var6 + 2), 0.0F);
					var4.addVertex((float)(var5 + var1), (float)(var6 + 2), 0.0F);
					var4.addVertex((float)(var5 + var1), (float)var6, 0.0F);
					var4.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				this.minecraft.fontRenderer.drawStringWithShadow(this.title, (var8 - this.minecraft.fontRenderer.getWidth(this.title)) / 2, var3 / 2 - 4 - 16, 16777215);
				this.minecraft.fontRenderer.drawStringWithShadow(this.text, (var8 - this.minecraft.fontRenderer.getWidth(this.text)) / 2, var3 / 2 - 4 + 8, 16777215);
				Display.update();

				try {
					Thread.yield();
				} catch (Exception var7) {
				}
			}
		}
	}
}
