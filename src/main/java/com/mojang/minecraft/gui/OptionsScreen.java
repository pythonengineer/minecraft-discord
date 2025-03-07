package com.mojang.minecraft.gui;

import com.mojang.minecraft.Options;

public final class OptionsScreen extends Screen {
    private Screen parent;
    private String title = "Options";
    private Options options;

    public OptionsScreen(Screen screen1, Options options2) {
        this.parent = screen1;
        this.options = options2;
    }

    public final void init() {
        for(int i1 = 0; i1 < 5; ++i1) {
            this.buttons.add(new Button(i1, this.width / 2 - 100, this.height / 6 + i1 * 24, this.options.getOption(i1)));
        }

        this.buttons.add(new Button(10, this.width / 2 - 100, this.height / 6 + 120 + 12, "Controls..."));
        this.buttons.add(new Button(20, this.width / 2 - 100, this.height / 6 + 168, "Done"));
    }

    protected final void buttonClicked(Button button1) {
        if(button1.enabled) {
            if(button1.id < 5) {
                this.options.setOption(button1.id, 1);
                button1.msg = this.options.getOption(button1.id);
            }

            if(button1.id == 10) {
                this.minecraft.setScreen(new ControlsScreen(this, this.options));
            }

            if(button1.id == 20) {
                this.minecraft.setScreen(this.parent);
            }

        }
    }

    public final void render(int i1, int i2) {
        fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
        drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215);
        super.render(i1, i2);
    }
}
