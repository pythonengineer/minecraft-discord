package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.GuiMainMenu;
import net.minecraft.game.world.World;

public class GuiIngameMenu extends GuiScreen {
    private int updateCounter2 = 0;
    private int updateCounter = 0;

    public void initGui() {
        this.updateCounter2 = 0;
		this.controlList.clear();
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48, "Save and quit to title.."));
        this.controlList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24, "Back to game"));
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96, "Options..."));
	}

	protected void actionPerformed(GuiButton button) {
		if(button.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

		if(button.id == 1) {
            this.mc.changeWorld1((World)null);
			this.mc.displayGuiScreen(new GuiMainMenu());
		}

		if(button.id == 4) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		}

	}

    public void updateScreen() {
        super.updateScreen();
        ++this.updateCounter;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        if(!this.mc.theWorld.saveWorld(this.updateCounter2++) || this.updateCounter < 20) {
            float f4 = MathHelper.sin(((float)(this.updateCounter % 10) + partialTicks) / 10.0F * (float)Math.PI * 2.0F) * 0.2F + 0.8F;
            int i5 = (int)(255.0F * f4);
            drawString(this.fontRenderer, "Saving level..", 8, this.height - 16, i5 << 16 | i5 << 8 | i5);
        }

        drawCenteredString(this.fontRenderer, "Game menu", this.width / 2, 40, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
