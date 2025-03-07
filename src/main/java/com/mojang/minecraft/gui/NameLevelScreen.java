package com.mojang.minecraft.gui;

import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;

public final class NameLevelScreen extends Screen {
	private Screen parent;
	private String title = "Enter level name:";
	private int id;
	private String name;
	private int counter = 0;

	public NameLevelScreen(Screen screen1, String string2, int i3) {
		this.parent = screen1;
		this.id = i3;
		this.name = string2;
		if(this.name.equals("-")) {
			this.name = "";
		}

	}

	public final void init() {
		this.buttons.clear();
		Keyboard.enableRepeatEvents(true);
        this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4 + 120, "Save"));
        this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 4 + 144, "Cancel"));
        ((Button)this.buttons.get(0)).enabled = this.name.trim().length() > 1;
	}

	public final void closeScreen() {
		Keyboard.enableRepeatEvents(false);
	}

	public final void tick() {
		++this.counter;
	}

	protected final void buttonClicked(Button button1) {
		if(button1.enabled) {
			if(button1.id == 0 && this.name.trim().length() > 1) {
                int i10001 = this.id;
                String string4 = this.name.trim();
                int i3 = i10001;
                this.minecraft.levelIo.save(this.minecraft.level, this.minecraft.minecraftUri, this.minecraft.user.name, this.minecraft.user.sessionId, string4, i3);
				this.minecraft.setScreen((Screen)null);
				this.minecraft.grabMouse();
			}

			if(button1.id == 1) {
				this.minecraft.setScreen(this.parent);
			}

		}
	}

	protected final void keyPressed(char c1, int i2) {
		if(i2 == 14 && this.name.length() > 0) {
			this.name = this.name.substring(0, this.name.length() - 1);
		}

        if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_\'*!\"#%/()=+?[]{}<>".indexOf(c1) >= 0 && this.name.length() < 64) {
            this.name = this.name + c1;
        }

		((Button)this.buttons.get(0)).enabled = this.name.trim().length() > 1;
	}

	public final void render(int i1, int i2) {
        fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
        drawCenteredString(this.font, this.title, this.width / 2, 40, 16777215);
        int i3 = this.width / 2 - 100;
        int i4 = this.height / 2 - 10;
        fill(i3 - 1, i4 - 1, i3 + 200 + 1, i4 + 20 + 1, -6250336);
        fill(i3, i4, i3 + 200, i4 + 20, -16777216);
        drawString(this.font, this.name + (this.counter / 6 % 2 == 0 ? "_" : ""), i3 + 4, i4 + 6, 14737632);
        super.render(i1, i2);
	}
}
