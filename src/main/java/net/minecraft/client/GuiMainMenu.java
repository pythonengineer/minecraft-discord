package net.minecraft.client;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.lwjgl.util.glu.GLU;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.gui.GuiButton;
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
	private String[] splashes = new String[]{"Pre-beta!", "Now on Discord!", "Built on Eaglercraft!", "Jesus Christ is God", "As seen on TV!", "Awesome!", "100% pure!", "May contain nuts!", "Better than Prey!", "More polygons!", "Sexy!", "Limited edition!", "Flashing letters!", "Made by Notch!", "Coming soon!", "Best in class!", "When it\'s finished!", "Absolutely dragon free!", "Excitement!", "More than 25000 sold!", "One of a kind!", "Heaps of hits on YouTube!", "Indev!", "Spiders everywhere!", "Check it out!", "Holy cow, man!", "It\'s a game!", "Made in Sweden!", "Reticulating splines!", "Minecraft!", "Yaaay!", "Alpha version!", "Singleplayer!", "Keyboard compatible!", "Undocumented!", "Ingots!", "Exploding creepers!", "That\'s not a moon!", "l33t!", "Create!", "Survive!", "Dungeon!", "Exclusive!", "The bee\'s knees!", "Down with O.P.P.!", "Closed source!", "Classy!", "Wow!", "Not on steam!", "9.95 euro!", "Half price!", "Oh man!", "Check it out!", "Awesome community!", "Pixels!", "Teetsuuuuoooo!", "Kaaneeeedaaaa!", "Now with difficulty!", "Enhanced!", "90% bug free!", "Pretty!", "12 herbs and spices!", "Fat free!", "Absolutely no memes!", "Free dental!", "Ask your doctor!", "Minors welcome!", "Cloud computing!", "Legal in Finland!", "Hard to label!", "Technically good!", "Bringing home the bacon!", "Indie!", "GOTY!", "Ceci n\'est pas une title screen!", "Euclidian!", "Now in 3D!", "Inspirational!", "Herregud!", "Complex cellular automata!", "Yes, sir!", "Played by cowboys!", "WebGL2!", "Thousands of colors!", "Try it!", "Age of Wonders is better!", "Try the mushroom stew!", "Sensational!", "Hot tamale, hot hot tamale!", "Play him off, keyboard cat!", "Guaranteed!", "Macroscopic!", "Bring it on!", "Random splash!", "Call your mother!", "Monster infighting!", "Loved by millions!", "Ultimate edition!", "Freaky!", "You\'ve got a brand new key!", "Water proof!", "Uninflammable!", "Whoa, dude!", "All inclusive!", "Tell your friends!", "NP is not in P!", "Notch <3 Ez!", "Music by C418!", "Livestreamed!", "Haunted!", "Polynomial!", "Terrestrial!", "All is full of love!", "Full of stars!", "Scientific!", "Cooler than Spock!", "Collaborate and listen!", "Never dig down!", "Take frequent breaks!", "Not linear!", "Han shot first!", "Nice to meet you!", "Buckets of lava!", "Ride the pig!", "Larger than Earth!", "sqrt(-1) love you!", "Phobos anomaly!", "Punching wood!", "Falling off cliffs!", "0% sugar!", "150% hyperbole!", "Synecdoche!", "Let\'s danec!", "Seecret Friday update!", "Reference implementation!", "Rude with two dudes with food!", "Kiss the sky!", "20 GOTO 10!", "Verlet intregration!", "Peter Griffin!", "Do not distribute!", "Cogito ergo sum!", "4815162342 lines of code!", "A skeleton popped out!", "The Work of Notch!", "The sum of its parts!", "BTAF used to be good!", "I miss ADOM!", "umop-apisdn!", "OICU812!", "Bring me Ray Cokes!", "Finger-licking!", "Thematic!", "Pneumatic!", "Sublime!", "Octagonal!", "Une baguette!", "Gargamel plays it!", "Rita is the new top dog!", "SWM forever!", "Representing Edsbyn!"};
	private String splashString = this.splashes[(int)(Math.random() * (double)this.splashes.length)];

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
		this.controlList.clear();
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48, "Single player"));
        this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72, "Multi player"));
        this.controlList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 96, "Play tutorial level"));
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, "Options..."));
        ((GuiButton)this.controlList.get(1)).enabled = false;
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

    private void drawLogo(float partialTicks) {
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
                        float f10 = (float)(logoEffectRandomizer9.prevHeight + (logoEffectRandomizer9.height - logoEffectRandomizer9.prevHeight) * (double)partialTicks);
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
