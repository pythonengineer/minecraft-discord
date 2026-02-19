package net.minecraft.client;

import com.mojang.nbt.NBTBase;
import com.mojang.nbt.NBTTagCompound;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglerZLIB;
import net.lax1dude.eaglercraft.lwjgl.opengl.Display;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.render.Tessellator;

public class LoadingScreenRenderer {
    private String text;
    private Minecraft mc;
    private String title;
    private long start;

    public LoadingScreenRenderer(Minecraft var1) {
        this.text = "";
        this.title = "";
        this.start = EagRuntime.currentTimeMillis();
        this.mc = var1;
    }

    public final void setTitle(String var1) {
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

    public final void setText(String var1) {
        if(this.mc.running) {
            this.start = 0L;
            this.text = var1;
            this.setProgress(-1);
            this.start = 0L;
        }
    }

    public final void setProgress(int var1) {
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
                if(var1 >= 0) {
                    var5 = var3 / 2 - 50;
                    int var6 = var8 / 2 + 16;
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    var9.startDrawingQuads(DefaultVertexFormats.POSITION_COLOR);
                    var9.setColorOpaque_I(8421504);
                    var9.addVertex((double)var5, (double)var6, 0.0D);
                    var9.addVertex((double)var5, (double)(var6 + 2), 0.0D);
                    var9.addVertex((double)(var5 + 100), (double)(var6 + 2), 0.0D);
                    var9.addVertex((double)(var5 + 100), (double)var6, 0.0D);
                    var9.setColorOpaque_I(8454016);
                    var9.addVertex((double)var5, (double)var6, 0.0D);
                    var9.addVertex((double)var5, (double)(var6 + 2), 0.0D);
                    var9.addVertex((double)(var5 + var1), (double)(var6 + 2), 0.0D);
                    var9.addVertex((double)(var5 + var1), (double)var6, 0.0D);
                    var9.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                }

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

    public LoadingScreenRenderer() {
    }

    public static NBTTagCompound read(InputStream var0) throws IOException {
        DataInputStream var4 = new DataInputStream(new BufferedInputStream(EaglerZLIB.newGZIPInputStream(var0)));

        NBTTagCompound var5;
        byte b0 = var4.readByte();
        try {
            NBTBase var1 = NBTBase.read(var4, b0);
            if(!(var1 instanceof NBTTagCompound)) {
                throw new IOException("Root tag must be a named compound tag");
            }

            var5 = (NBTTagCompound)var1;
        } finally {
            var4.close();
        }

        return var5;
    }

    public static void write(NBTTagCompound var0, OutputStream var1) throws IOException {
        DataOutputStream var5 = new DataOutputStream(new BufferedOutputStream(EaglerZLIB.newGZIPOutputStream(var1)));

        try {
            NBTBase.writeNamedTag(var0, var5);
        } finally {
            var5.close();
        }

    }
}
