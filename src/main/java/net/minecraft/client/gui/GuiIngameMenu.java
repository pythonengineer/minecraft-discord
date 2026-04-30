package net.minecraft.client.gui;

import net.minecraft.client.GuiMainMenu;
import net.minecraft.game.world.World;

public final class GuiIngameMenu extends GuiScreen {
	public final void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4, "Options..."));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 24, "Save and quit to title.."));
		this.controlList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 120, "Back to game"));
	}

	protected final void actionPerformed(GuiButton button) {
		if(button.id == 0) {
			this.mc.setGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

		if(button.id == 1) {
            this.mc.changeWorld1((World)null);
			this.mc.setGuiScreen(new GuiMainMenu());
		}

		if(button.id == 4) {
			this.mc.setGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		}

	}

    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        drawCenteredString(this.fontRenderer, "Game menu", this.width / 2, 40, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
