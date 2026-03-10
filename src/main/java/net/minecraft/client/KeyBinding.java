package net.minecraft.client;

public final class KeyBinding {
    public String keyDescription;
    public int keyCode;

    public KeyBinding(String desc, int keyCode) {
        this.keyDescription = desc;
        this.keyCode = keyCode;
    }
}