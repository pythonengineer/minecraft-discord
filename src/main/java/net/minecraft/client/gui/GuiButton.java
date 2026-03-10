package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;

public class GuiButton extends Gui {
	private int width;
	private int height;
	private int xPosition;
	private int yPosition;
	public String displayString;
	public int id;
	public boolean enabled;
	private boolean visible;

	public GuiButton(int id, int x, int y, String displayString) {
		this(id, x, y, 200, 20, displayString);
	}

	protected GuiButton(int id, int x, int y, int width, int height, String displayString) {
		this.width = 200;
		this.height = 20;
		this.enabled = true;
		this.visible = true;
		this.id = id;
		this.xPosition = x;
		this.yPosition = y;
		this.width = width;
		this.height = 20;
		this.displayString = displayString;
	}

	public final void drawButton(Minecraft mc, int x, int y) {
		if(this.visible) {
			FontRenderer fontRenderer4 = mc.fontRenderer;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/gui.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			byte mc1 = 1;
			boolean x1 = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
			if(!this.enabled) {
				mc1 = 0;
			} else if(x1) {
				mc1 = 2;
			}

			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + mc1 * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + mc1 * 20, this.width / 2, this.height);
			if(!this.enabled) {
				drawCenteredString(fontRenderer4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, -6250336);
			} else if(x1) {
				drawCenteredString(fontRenderer4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 16777120);
			} else {
				drawCenteredString(fontRenderer4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 14737632);
			}
		}
	}

	public final boolean mousePressed(int x, int y) {
		return this.enabled && x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
	}
}