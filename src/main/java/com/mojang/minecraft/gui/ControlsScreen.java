package com.mojang.minecraft.gui;

import com.mojang.minecraft.Options;

public final class ControlsScreen extends Screen {
    private Screen parent;
    private String title = "Controls";
    private Options options;
    private int selectedKey = -1;

    public ControlsScreen(Screen screen1, Options options2) {
        this.parent = screen1;
        this.options = options2;
    }

    public final void init() {
        for(int i1 = 0; i1 < this.options.keyBindings.length; ++i1) {
            this.buttons.add(new KeyBindingButton(i1, this.width / 2 - 155 + i1 % 2 * 160, this.height / 6 + 24 * (i1 >> 1), this.options.getKeyBinding(i1)));
        }

        this.buttons.add(new Button(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
    }

    protected final void buttonClicked(Button button1) {
        for(int i2 = 0; i2 < this.options.keyBindings.length; ++i2) {
            ((Button)this.buttons.get(i2)).msg = this.options.getKeyBinding(i2);
        }

        if(button1.id == 200) {
            this.minecraft.setScreen(this.parent);
        } else {
            this.selectedKey = button1.id;
            button1.msg = "> " + this.options.getKeyBinding(button1.id) + " <";
        }
    }

    protected final void keyPressed(char c1, int i2) {
        if(this.selectedKey >= 0) {
            this.options.setKeyBinding(this.selectedKey, i2);
            ((Button)this.buttons.get(this.selectedKey)).msg = this.options.getKeyBinding(this.selectedKey);
            this.selectedKey = -1;
        } else {
            super.keyPressed(c1, i2);
        }
    }

    public final void render(int i1, int i2) {
        fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
        drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215);
        super.render(i1, i2);
    }
}
