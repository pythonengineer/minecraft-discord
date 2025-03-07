package com.mojang.minecraft.gui;

public class Button extends Gui {
    int x;
    int y;
    public int w;
    public int h;
    public String msg;
    public int id;
    public boolean enabled;
    public boolean visible;

    public Button(int i1, int i2, int i3, String string4) {
        this(i1, i2, i3, 200, 20, string4);
    }

    protected Button(int i1, int i2, int i3, int i4, int i5, String string6) {
        this.x = 200;
        this.y = 20;
        this.enabled = true;
        this.visible = true;
        this.id = i1;
        this.w = i2;
        this.h = i3;
        this.x = i4;
        this.y = 20;
        this.msg = string6;
    }
}
