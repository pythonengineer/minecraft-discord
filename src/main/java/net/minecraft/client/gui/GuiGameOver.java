package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public final class GuiGameOver extends GuiScreen {
	public final void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 72, "Generate new level..."));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 96, "Load level.."));
        ((GuiButton)this.controlList.get(2)).enabled = false;
		//if(this.mc.session == null) {
		//	((GuiButton)this.controlList.get(2)).enabled = false;
		//}

	}

	protected final void actionPerformed(GuiButton var1) {
		if(var1.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.options));
		}

		if(var1.id == 1) {
			this.mc.displayGuiScreen(new GuiNewLevel(this));
		}

		if(var1.id == 2) {// && this.mc.session != null) {
		    //this.mc.displayGuiScreen(new GuiLoadLevel(this));
		}

	}

	public final void drawScreen(int var1, int var2) {
		drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		drawCenteredString(this.fontRenderer, "Game over!", this.width / 2 / 2, 30, 16777215);
		GL11.glPopMatrix();
		drawCenteredString(this.fontRenderer, "Score: &e" + this.mc.thePlayer.getScore(), this.width / 2, 100, 16777215);
		super.drawScreen(var1, var2);
	}
}
