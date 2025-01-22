package com.mojang.minecraft.gui;

public final class Button {
    public int x;
    public int y;
    public int w;
    public int h;
    public String msg;
    public int id;
    public boolean enabled = true;
    public boolean visible = true;

    public Button(int i1, int i2, int i3, int i4, int i5, String string6) {
        this.id = i1;
        this.x = i2;
        this.y = i3;
        this.w = 200;
        this.h = 20;
        this.msg = string6;
    }
}
