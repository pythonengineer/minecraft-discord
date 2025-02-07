package com.mojang.minecraft.gui;

import com.mojang.minecraft.net.ConnectionManager;
import com.mojang.minecraft.net.Packet;

import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;

public final class ChatScreen extends Screen {
    private String typedMsg = "";
    private int counter = 0;

    public final void init() {
        Keyboard.enableRepeatEvents(true);
    }

    public final void closeScreen() {
        Keyboard.enableRepeatEvents(false);
    }

    public final void tick() {
        ++this.counter;
    }

    protected final void keyPressed(char c1, int i2) {
        if(i2 == 1) {
            this.minecraft.setScreen((Screen)null);
        } else if(i2 == 28) {
            ConnectionManager connectionManager10000 = this.minecraft.connectionManager;
            String string4 = this.typedMsg.trim();
            ConnectionManager connectionManager3 = connectionManager10000;
            if((string4 = string4.trim()).length() > 0) {
                connectionManager3.connection.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, string4});
            }

            this.minecraft.setScreen((Screen)null);
        } else {
            if(i2 == 14 && this.typedMsg.length() > 0) {
                this.typedMsg = this.typedMsg.substring(0, this.typedMsg.length() - 1);
            }

            if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_\'*!\\\"#%/()=+?[]{}<>@|$;".indexOf(c1) >= 0 && this.typedMsg.length() < 64 - (this.minecraft.user.name.length() + 2)) {
                this.typedMsg = this.typedMsg + c1;
            }

        }
    }

    public final void render(int i1, int i2) {
        fill(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        this.drawString("> " + this.typedMsg + (this.counter / 6 % 2 == 0 ? "_" : ""), 4, this.height - 12, 14737632);
    }
}
