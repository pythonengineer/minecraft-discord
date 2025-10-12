package net.minecraft.client;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.lwjgl.opengl.Display;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.render.Tessellator;

public final class LoadingScreenRenderer implements IProgressUpdate {
    private String text = "";
    private Minecraft mc;
    private String title = "";
    private long start = EagRuntime.currentTimeMillis();

	public LoadingScreenRenderer(Minecraft var1) {
		this.mc = var1;
	}

	public final void displayProgressMessage(String var1) {
		if(this.mc.running) {
			this.title = var1;
            int var3 = this.mc.scaledResolution.getScaledWidth();
            int var2 = this.mc.scaledResolution.getScaledHeight();
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, (double)var3, (double)var2, 0.0D, 100.0D, 300.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		}
	}

	public final void displayLoadingString(String var1) {
        if(this.mc.running) {
			this.start = 0L;
			this.text = var1;
            if(this.mc.running) {
                long var4 = EagRuntime.currentTimeMillis();
                if(var4 - this.start >= 20L) {
                    this.start = var4;
                    int var3 = this.mc.scaledResolution.getScaledWidth();
                    int var8 = this.mc.scaledResolution.getScaledHeight();
                    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                    GL11.glMatrixMode(GL11.GL_PROJECTION);
                    GL11.glLoadIdentity();
                    GL11.glOrtho(0.0D, (double)var3, (double)var8, 0.0D, 100.0D, 300.0D);
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glLoadIdentity();
                    GL11.glTranslatef(0.0F, 0.0F, -200.0F);
                    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
                    Tessellator var9 = Tessellator.instance;
                    int var5 = this.mc.renderEngine.getTexture("/dirt.png");
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5);
                    var9.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
                    var9.setColorOpaque_I(4210752);
                    var9.addVertexWithUV(0.0D, (double)var8, 0.0D, 0.0D, (double)((float)var8 / 32.0F));
                    var9.addVertexWithUV((double)var3, (double)var8, 0.0D, (double)((float)var3 / 32.0F), (double)((float)var8 / 32.0F));
                    var9.addVertexWithUV((double)var3, 0.0D, 0.0D, (double)((float)var3 / 32.0F), 0.0D);
                    var9.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
                    var9.draw();
                    this.mc.fontRenderer.drawStringWithShadow(this.title, (var3 - this.mc.fontRenderer.getStringWidth(this.title)) / 2, var8 / 2 - 4 - 16, 16777215);
                    this.mc.fontRenderer.drawStringWithShadow(this.text, (var3 - this.mc.fontRenderer.getStringWidth(this.text)) / 2, var8 / 2 - 4 + 8, 16777215);
                    Display.update();

                    try {
                        Thread.yield();
                    } catch (Exception var6) {
                    }
                }

                this.start = 0L;
            }
		}
	}
}
