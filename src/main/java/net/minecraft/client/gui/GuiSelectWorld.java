package net.minecraft.client.gui;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.world.World;

public class GuiSelectWorld extends GuiScreen {
	protected GuiScreen currentScreen;
	protected String screenTitle = "Select world";
	private boolean selected = false;

	public GuiSelectWorld(GuiScreen screen) {
		this.currentScreen = screen;
	}

	public final void initGui() {
		for(int i2 = 0; i2 < 5; ++i2) {
			NBTTagCompound nBTTagCompound3;
			if((nBTTagCompound3 = World.saveWorldFile("World" + (i2 + 1))) == null) {
				this.controlList.add(new GuiButton(i2, this.width / 2 - 100, this.height / 6 + i2 * 24, "- empty -"));
			} else {
				String string4 = "World " + (i2 + 1);
				long j5 = nBTTagCompound3.getLong("SizeOnDisk");
				string4 = string4 + " (" + (float)(j5 / 1024L * 100L / 1024L) / 100.0F + " MB)";
				this.controlList.add(new GuiButton(i2, this.width / 2 - 100, this.height / 6 + i2 * 24, string4));
			}
		}

		this.buttons();
	}

	protected static String getWorldName(int worldIndex) {
		return World.saveWorldFile("World" + worldIndex) != null ? "World" + worldIndex : null;
	}

	public void buttons() {
		this.controlList.add(new GuiButton(5, this.width / 2 - 100, this.height / 6 + 120 + 12, "Delete world..."));
		this.controlList.add(new GuiButton(6, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
	}

	protected final void actionPerformed(GuiButton button) {
		if(button.enabled) {
			if(button.id < 5) {
				this.actionWorld(button.id + 1);
			} else if(button.id == 5) {
				this.mc.setGuiScreen(new GuiDeleteWorld(this));
			} else {
				if(button.id == 6) {
					this.mc.setGuiScreen(this.currentScreen);
				}

			}
		}
	}

	public void actionWorld(int worldIndex) {
		this.mc.setGuiScreen((GuiScreen)null);
		if(!this.selected) {
			this.selected = true;
			this.mc.startWorld("World" + worldIndex);
			this.mc.setGuiScreen((GuiScreen)null);
		}
	}

	public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}