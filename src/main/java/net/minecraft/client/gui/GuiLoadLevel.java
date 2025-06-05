package net.minecraft.client.gui;

public class GuiLoadLevel extends GuiScreen implements Runnable {
	private GuiScreen parent;
	private boolean finished = false;
	private boolean loaded = false;
	private String[] levels = null;
	private String status = "";
	protected String title = "Load level";
    private boolean frozen = false;

	public GuiLoadLevel(GuiScreen var1) {
		this.parent = var1;
	}

	public void run() {
		this.status = "Failed to load levels";
		this.finished = true;
	}

	protected void openLevel(String[] var1) {
		for(int var2 = 0; var2 < 5; ++var2) {
			((GuiButton)this.controlList.get(var2)).enabled = !var1[var2].equals("-");
			((GuiButton)this.controlList.get(var2)).displayString = var1[var2];
			((GuiButton)this.controlList.get(var2)).visible = true;
		}

        ((GuiButton)this.controlList.get(5)).visible = true;
	}

	public void initGui() {
		for(int var1 = 0; var1 < 5; ++var1) {
			this.controlList.add(new GuiButton(var1, this.width / 2 - 100, this.height / 6 + var1 * 24, "---"));
			((GuiButton)this.controlList.get(var1)).visible = false;
		}

		this.controlList.add(new GuiButton(5, this.width / 2 - 100, this.height / 6 + 120 + 12, "Load file..."));
		this.controlList.add(new GuiButton(6, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
		((GuiButton)this.controlList.get(5)).visible = false;
	}

    protected final void actionPerformed(GuiButton var1) {
        if(!this.frozen) {
            if(var1.enabled) {
                if(this.loaded && var1.id < 5) {
                    this.openLevel(var1.id);
                }

                if(this.finished || this.loaded && var1.id == 6) {
                    this.mc.displayGuiScreen(this.parent);
                }

            }
        }
    }

	protected void openLevel(int var1) {
		this.mc.displayGuiScreen((GuiScreen)null);
		this.mc.setIngameFocus();
	}

	public final void drawScreen(int var1, int var2) {
		drawGradientRect(0, 0, this.width, this.height, 1610941696, -1607454624);
		drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
		if(!this.loaded) {
			drawCenteredString(this.fontRenderer, this.status, this.width / 2, this.height / 2 - 4, 16777215);
		}

		super.drawScreen(var1, var2);
	}
}
