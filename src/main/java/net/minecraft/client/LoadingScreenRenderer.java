package net.minecraft.client;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.lwjgl.opengl.Display;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.render.Tessellator;

public class LoadingScreenRenderer implements IProgressUpdate {
    private String currentlyDisplayedProgress = "";
    private Minecraft mc;
    private String currentlyDisplayedText = "";
    private long systemTime = EagRuntime.currentTimeMillis();
    private boolean printText = false;

    public LoadingScreenRenderer(Minecraft minecraft) {
        this.mc = minecraft;
    }

    public void printText(String string1) {
        this.printText = false;
        this.drawScreen(string1);
    }

    public void displayProgressMessage(String string1) {
        this.printText = true;
        this.drawScreen(this.currentlyDisplayedText);
    }

    private void drawScreen(String title) {
        if(this.mc.running) {
            this.currentlyDisplayedText = title;
            int i2 = this.mc.scaledResolution.getScaledWidth();
            int title2 = this.mc.scaledResolution.getScaledHeight();
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, (double)i2, (double)title2, 0.0D, 100.0D, 300.0D);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -200.0F);
        }
    }

    public void displayLoadingString(String loadingString) {
        if(this.mc.running) {
            this.systemTime = 0L;
            this.currentlyDisplayedProgress = loadingString;
            this.setLoadingProgress(-1);
            this.systemTime = 0L;
        }
    }

    public void setLoadingProgress(int loadingProgress) {
        if(this.mc.running) {
            long j2;
            if((j2 = EagRuntime.currentTimeMillis()) - this.systemTime >= 20L) {
                this.systemTime = j2;
                int i3 = this.mc.scaledResolution.getScaledWidth();
                int i9 = this.mc.scaledResolution.getScaledHeight();
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                GL11.glOrtho(0.0D, (double)i3, (double)i9, 0.0D, 100.0D, 300.0D);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
                GL11.glTranslatef(0.0F, 0.0F, -200.0F);
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                Tessellator tessellator4 = Tessellator.instance;
                int i5 = this.mc.renderEngine.getTexture("/dirt.png");
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, i5);
                tessellator4.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
                tessellator4.setColorOpaque_I(4210752);
                tessellator4.addVertexWithUV(0.0D, (double)i9, 0.0D, 0.0D, (double)((float)i9 / 32.0F));
                tessellator4.addVertexWithUV((double)i3, (double)i9, 0.0D, (double)((float)i3 / 32.0F), (double)((float)i9 / 32.0F));
                tessellator4.addVertexWithUV((double)i3, 0.0D, 0.0D, (double)((float)i3 / 32.0F), 0.0D);
                tessellator4.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
                tessellator4.draw();
                if(loadingProgress >= 0) {
                    i5 = i3 / 2 - 50;
                    int i6 = i9 / 2 + 16;
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator4.startDrawingQuads(DefaultVertexFormats.POSITION_COLOR);
                    tessellator4.setColorOpaque_I(8421504);
                    tessellator4.addVertex((double)i5, (double)i6, 0.0D);
                    tessellator4.addVertex((double)i5, (double)(i6 + 2), 0.0D);
                    tessellator4.addVertex((double)(i5 + 100), (double)(i6 + 2), 0.0D);
                    tessellator4.addVertex((double)(i5 + 100), (double)i6, 0.0D);
                    tessellator4.setColorOpaque_I(8454016);
                    tessellator4.addVertex((double)i5, (double)i6, 0.0D);
                    tessellator4.addVertex((double)i5, (double)(i6 + 2), 0.0D);
                    tessellator4.addVertex((double)(i5 + loadingProgress), (double)(i6 + 2), 0.0D);
                    tessellator4.addVertex((double)(i5 + loadingProgress), (double)i6, 0.0D);
                    tessellator4.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                }

                this.mc.fontRenderer.drawStringWithShadow(this.currentlyDisplayedText, (i3 - this.mc.fontRenderer.getStringWidth(this.currentlyDisplayedText)) / 2, i9 / 2 - 4 - 16, 0xFFFFFF);
                this.mc.fontRenderer.drawStringWithShadow(this.currentlyDisplayedProgress, (i3 - this.mc.fontRenderer.getStringWidth(this.currentlyDisplayedProgress)) / 2, i9 / 2 - 4 + 8, 0xFFFFFF);
                Display.update();

                try {
                    Thread.yield();
                } catch (Exception exception7) {
                }
            }
        }
    }
}
