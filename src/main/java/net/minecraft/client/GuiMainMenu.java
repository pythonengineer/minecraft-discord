package net.minecraft.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.lwjgl.util.glu.GLU;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.world.block.Block;

public class GuiMainMenu extends GuiScreen {
    private static final EaglercraftRandom rand = new EaglercraftRandom();
    String[] logoBlockLayers = new String[]{" *   * * *   * *** *** *** *** *** ***", " ** ** * **  * *   *   * * * * *    * ", " * * * * * * * **  *   **  *** **   * ", " *   * * *  ** *   *   * * * * *    * ", " *   * * *   * *** *** * * * * *    * "};
    private LogoEffectRandomizer[][] logoEffects;
	private float updateCounter = 0.0F;
    private String splashString = "missingno";

    public GuiMainMenu() {
        try {
            ArrayList arrayList1 = new ArrayList();
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(EagRuntime.getResourceStream("/assets/title/splashes.txt")));
            String string3 = "";

            while((string3 = bufferedReader2.readLine()) != null) {
                string3 = string3.trim();
                if(string3.length() > 0) {
                    arrayList1.add(string3);
                }
            }

            this.splashString = (String)arrayList1.get(rand.nextInt(arrayList1.size()));
        } catch (Exception exception4) {
        }

    }

	public void updateScreen() {
        ++this.updateCounter;
        if(this.logoEffects != null) {
            for(int i1 = 0; i1 < this.logoEffects.length; ++i1) {
                for(int i2 = 0; i2 < this.logoEffects[i1].length; ++i2) {
                    this.logoEffects[i1][i2].updateLogoEffects();
                }
            }
        }

	}

	protected void keyTyped(char typedChar, int keyCode) {
	}

	public void initGui() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date());
        if(calendar1.get(2) + 1 == 11 && calendar1.get(5) == 9) {
            this.splashString = "Happy birthday, ez!";
        } else if(calendar1.get(2) + 1 == 6 && calendar1.get(5) == 1) {
            this.splashString = "Happy birthday, Notch!";
        } else if(calendar1.get(2) + 1 == 12 && calendar1.get(5) == 24) {
            this.splashString = "Merry Christmas!";
        } else if(calendar1.get(2) + 1 == 1 && calendar1.get(5) == 1) {
            this.splashString = "Happy new year!";
        }

		this.controlList.clear();
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48, "Singleplayer"));
        this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72, "Multiplayer"));
        this.controlList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 96, "Play tutorial level"));
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, "Options..."));
		((GuiButton)this.controlList.get(2)).enabled = false;
        if(this.mc.session == null) {
            ((GuiButton)this.controlList.get(1)).enabled = false;
        }

	}

    protected void actionPerformed(GuiButton button) {
        if(button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.options));
        }

        if(button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if(button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        Tessellator tessellator4 = Tessellator.instance;
        this.drawLogo(partialTicks);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/logo.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator4.setColorOpaque_I(0xFFFFFF);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
        GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
        float f15;
        GL11.glScalef(f15 = (1.8F - MathHelper.abs(MathHelper.sin((float)(EagRuntime.currentTimeMillis() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F)) * 100.0F / (float)(this.fontRenderer.getStringWidth(this.splashString) + 32), f15, f15);
        drawCenteredString(this.fontRenderer, this.splashString, 0, -8, 16776960);
        GL11.glPopMatrix();
        String string16 = "Copyright Mojang Specifications. Do not distribute.";
        drawString(this.fontRenderer, string16, this.width - this.fontRenderer.getStringWidth(string16) - 2, this.height - 10, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawLogo(float renderPartialTick) {
        int i3;
        if(this.logoEffects == null) {
            this.logoEffects = new LogoEffectRandomizer[this.logoBlockLayers[0].length()][this.logoBlockLayers.length];

            for(int i2 = 0; i2 < this.logoEffects.length; ++i2) {
                for(i3 = 0; i3 < this.logoEffects[i2].length; ++i3) {
                    this.logoEffects[i2][i3] = new LogoEffectRandomizer(this, i2, i3);
                }
            }
        }

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        ScaledResolution scaledResolution14 = new ScaledResolution(this.mc);
        i3 = 120 * scaledResolution14.getScaleFactor();
        GLU.gluPerspective(70.0F, (float)this.mc.displayWidth / (float)i3, 0.05F, 100.0F);
        GL11.glViewport(0, this.mc.displayHeight - i3, this.mc.displayWidth, i3);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDepthMask(true);

        for(int i4 = 0; i4 < 3; ++i4) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.4F, 0.6F, -12.0F);
            if(i4 == 0) {
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                GL11.glTranslatef(0.0F, -0.4F, 0.0F);
                GL11.glScalef(0.98F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            }

            if(i4 == 1) {
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            }

            if(i4 == 2) {
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            }

            GL11.glScalef(1.0F, -1.0F, 1.0F);
            GL11.glRotatef(15.0F, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(0.89F, 1.0F, 0.4F);
            GL11.glTranslatef((float)(-this.logoBlockLayers[0].length()) * 0.5F, (float)(-this.logoBlockLayers.length) * 0.5F, 0.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
            if(i4 == 0) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/title/black.png"));
            }

            RenderBlocks renderBlocks5 = new RenderBlocks();

            for(int i6 = 0; i6 < this.logoBlockLayers.length; ++i6) {
                for(int i7 = 0; i7 < this.logoBlockLayers[i6].length(); ++i7) {
                    char c8 = this.logoBlockLayers[i6].charAt(i7);
                    if(c8 != 32) {
                        GL11.glPushMatrix();
                        LogoEffectRandomizer logoEffectRandomizer9 = this.logoEffects[i7][i6];
                        float f10 = (float)(logoEffectRandomizer9.prevHeight + (logoEffectRandomizer9.height - logoEffectRandomizer9.prevHeight) * (double)renderPartialTick);
                        float f11 = 1.0F;
                        float f12 = 1.0F;
                        float f13 = 0.0F;
                        if(i4 == 0) {
                            f11 = f10 * 0.04F + 1.0F;
                            f12 = 1.0F / f11;
                            f10 = 0.0F;
                        }

                        GL11.glTranslatef((float)i7, (float)i6, f10);
                        GL11.glScalef(f11, f11, f11);
                        GL11.glRotatef(f13, 0.0F, 1.0F, 0.0F);
                        renderBlocks5.renderBlockAsItem(Block.stone, f12);
                        GL11.glPopMatrix();
                    }
                }
            }

            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    static EaglercraftRandom getRandom() {
        return rand;
    }
}
