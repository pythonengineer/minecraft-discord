package net.minecraft.client;

public final class ChatLine {
    public String message;
    public int updateCounter;

    public ChatLine(String message) {
        this.message = message;
        this.updateCounter = 0;
    }
}