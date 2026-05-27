package net.minecraft.client;

public class KeyBinding {
    public String keyDescription;
    public int keyCode;

    public KeyBinding(String desc, int keyCode) {
        this.keyDescription = desc;
        this.keyCode = keyCode;
    }
}
