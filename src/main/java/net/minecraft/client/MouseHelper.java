package net.minecraft.client;

import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.lwjgl.input.Mouse;

public class MouseHelper {
    public int deltaX;
    public int deltaY;

    public MouseHelper() {
    }

    public void grabMouseCursor() {
        Mouse.setGrabbed(true);
        this.deltaX = 0;
        this.deltaY = 0;
    }

    public void ungrabMouseCursor() {
        Mouse.setGrabbed(false);
    }

    public void mouseXYChange() {
        this.deltaX = PointerInputAbstraction.getDX();
        this.deltaY = PointerInputAbstraction.getDY();
    }
}
