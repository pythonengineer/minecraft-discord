package net.minecraft.client;

import java.io.IOException;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglerInputStream;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLoadLevel;
import net.minecraft.client.gui.GuiNewLevel;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.World;

public final class GuiMainMenu extends GuiScreen {
	private float updateCounter = 0.0F;
	private String[] splashes = new String[]{"Pre-beta!", "Now on Discord!", "Uses Eaglercraft!", "Music by C418!", "Christ is King", "As seen on TV!", "Awesome!", "100% pure!", "May contain nuts!", "Better than Prey!", "More polygons!", "Sexy!", "Limited edition!", "Flashing letters!", "Made by Notch!", "Coming soon!", "Best in class!", "When it\'s finished!", "Absolutely dragon free!", "Excitement!", "More than 4000 sold!", "One of a kind!", "700+ hits on YouTube!", "Indev!", "Spiders everywhere!", "Check it out!", "Holy cow, man!", "It\'s a game!", "Made in Sweden!", "Reticulating splines!", "Minecraft!", "Yaaay!", "Alpha version!", "Singleplayer!", "Keyboard compatible!", "Undocumented!", "Ingots!", "Exploding creepers!", "That\'s not a moon!", "l33t!", "Create!", "Survive!", "Dungeon!", "Exclusive!", "The bee\'s knees!", "Down with O.P.P.!", "Closed source!", "Classy!", "Wow!", "Not on steam!", "9.95 euro!", "Half price!", "Oh man!", "Check it out!", "Awesome community!", "Pixels!", "Teetsuuuuoooo!", "Kaaneeeedaaaa!", "Now with difficulty!", "Enhanced!", "90% bug free!", "Pretty!", "12 herbs and spices!", "Fat free!", "Absolutely no memes!", "Free dental!", "Ask your doctor!", "Minors welcome!", "Cloud computing!", "Legal in Finland!", "Hard to label!", "Technically good!"};
	private String currentSplash = this.splashes[(int)(Math.random() * (double)this.splashes.length)];

	public final void updateScreen() {
		this.updateCounter += 0.01F;
	}

	protected final void keyTyped(char var1, int var2) {
	}

	public final void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48, "Generate new level..."));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72, "Load level.."));
		this.controlList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 96, "Play tutorial level"));
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, "Options..."));
		((GuiButton)this.controlList.get(2)).enabled = false;
		//if(this.mc.session == null) {
		//	((GuiButton)this.controlList.get(1)).enabled = false;
		//}

	}

	protected final void actionPerformed(GuiButton var1) {
		if(var1.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.options));
		}

		if(var1.id == 1) {
			this.mc.displayGuiScreen(new GuiNewLevel(this));
		}

        if(var1.id == 2) {//if(this.mc.session != null && var1.id == 2) {
            //this.mc.displayGuiScreen(new GuiLoadLevel(this));
            try {
                byte[] level = EagRuntime.getStorage("level.mclevel");
                if(level != null) {
                    EaglerInputStream var4 = new EaglerInputStream(level);
                    World var2 = (new PlayerLoader(this.mc, this.mc.loadingScreen)).load(var4);
                    var4.close();
                    this.mc.setLevel(var2);
                    this.mc.displayGuiScreen((GuiScreen)null);
                    this.mc.setIngameFocus();
                }
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }

	}

	public final void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		Tessellator var4 = Tessellator.instance;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/logo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var4.setColorOpaque_I(16777215);
		this.drawTexturedModalRect((this.width - 256) / 2, 30, 0, 0, 256, 49);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
		GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
        float var15 = 1.8F - MathHelper.abs(MathHelper.sin((float)(EagRuntime.currentTimeMillis() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
        var15 = var15 * 100.0F / (float)(this.fontRenderer.getStringWidth(this.currentSplash) + 32);
        GL11.glScalef(var15, var15, var15);
        drawCenteredString(this.fontRenderer, this.currentSplash, 0, -8, 16776960);
        GL11.glPopMatrix();
        String var16 = "Copyright Mojang Specifications. Do not distribute.";
        drawString(this.fontRenderer, var16, this.width - this.fontRenderer.getStringWidth(var16) - 2, this.height - 10, 16777215);
		super.drawScreen(var1, var2, var3);
	}
}
