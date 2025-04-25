package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;

public final class GuiNameLevel extends GuiScreen {
	private GuiScreen parent;
	private String title = "Enter level name:";
    private int slot;
	private String name;
	private int id = 0;

	public GuiNameLevel(GuiScreen var1, String var2, int var3) {
		this.parent = var1;
		this.name = var2;
        this.slot = var3;
		if(this.name.equals("-")) {
			this.name = "";
		}

	}

	public final void initGui() {
		this.controlList.clear();
		Keyboard.enableRepeatEvents(true);
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Save"));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 144, "Cancel"));
		((GuiButton)this.controlList.get(0)).enabled = this.name.trim().length() > 1;
	}

    public final void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public final void updateScreen() {
		++this.id;
	}

	protected final void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id == 0 && this.name.trim().length() > 1) {
				this.name.trim();
				this.mc.displayGuiScreen((GuiScreen)null);
				this.mc.setIngameFocus();
			}

			if(var1.id == 1) {
				this.mc.displayGuiScreen(this.parent);
			}

		}
	}

	protected final void keyTyped(char var1, int var2) {
		if(var2 == 14 && this.name.length() > 0) {
			this.name = this.name.substring(0, this.name.length() - 1);
		}

		if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_\'*!\"#%/()=+?[]{}<>".indexOf(var1) >= 0 && this.name.length() < 64) {
			this.name = this.name + var1;
		}

		((GuiButton)this.controlList.get(0)).enabled = this.name.trim().length() > 1;
	}

	public final void drawScreen(int var1, int var2) {
		drawGradientRect(0, 0, this.width, this.height, 1610941696, -1607454624);
		drawCenteredString(this.fontRenderer, this.title, this.width / 2, 40, 16777215);
		int var3 = this.width / 2 - 100;
		int var4 = this.height / 2 - 10;
		drawRect(var3 - 1, var4 - 1, var3 + 200 + 1, var4 + 20 + 1, -6250336);
		drawRect(var3, var4, var3 + 200, var4 + 20, -16777216);
		FontRenderer var10000 = this.fontRenderer;
		String var10001 = this.name + (this.id / 6 % 2 == 0 ? "_" : "");
		int var10002 = var3 + 4;
		int var10003 = var4 + 6;
		int var6 = 14737632;
		int var5 = var10003;
		var3 = var10002;
		String var7 = var10001;
		var10000.drawStringWithShadow(var7, var3, var5, var6);
		super.drawScreen(var1, var2);
	}
}
