package net.minecraft.client.gui;

public class GuiButton extends Gui {
	int width;
	int height;
	public int x;
	public int y;
	public String displayString;
	public int id;
	public boolean enabled;
	public boolean visible;

	public GuiButton(int var1, int var2, int var3, String var4) {
		this(var1, var2, var3, 200, var4);
	}

	protected GuiButton(int var1, int var2, int var3, int var4, String var5) {
		this.width = 200;
		this.height = 20;
		this.enabled = true;
		this.visible = true;
		this.id = var1;
		this.x = var2;
		this.y = var3;
		this.width = var4;
		this.height = 20;
		this.displayString = var5;
	}
}
