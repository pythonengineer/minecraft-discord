package net.minecraft.client.gui;

public final class GuiSaveLevel extends GuiLoadLevel {
	public GuiSaveLevel(GuiScreen var1) {
		super(var1);
		this.title = "Save level";
	}

	public final void initGui() {
		super.initGui();
		((GuiButton)this.controlList.get(5)).displayString = "Save file...";
	}

	protected final void openLevel(String[] var1) {
		for(int var2 = 0; var2 < 5; ++var2) {
			((GuiButton)this.controlList.get(var2)).displayString = var1[var2];
			((GuiButton)this.controlList.get(var2)).visible = true;
		}

	}

	protected final void openLevel(int var1) {
		this.mc.displayGuiScreen(new GuiNameLevel(this, ((GuiButton)this.controlList.get(var1)).displayString, var1));
	}
}
