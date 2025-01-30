package com.mojang.minecraft.gui;

public final class ErrorScreen extends Screen {
    private String title;
    private String desc;

    public ErrorScreen(String string1, String string2) {
        this.title = string1;
        this.desc = string2;
    }

    public final void init() {
    }

    public final void render(int i1, int i2) {
        fillGradient(0, 0, this.y, this.w, -12574688, -11530224);
        this.drawCenteredString(this.title, this.y / 2, 90, 0xFFFFFF);
        this.drawCenteredString(this.desc, this.y / 2, 110, 0xFFFFFF);
        super.render(i1, i2);
    }

    protected final void keyPressed(char c1, int i2) {
    }
}
