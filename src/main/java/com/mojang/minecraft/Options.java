package com.mojang.minecraft;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglerInputStream;
import net.lax1dude.eaglercraft.EaglerOutputStream;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;

public final class Options {
    private static final String[] RENDER_DISTANCES = new String[]{"FAR", "NORMAL", "SHORT", "TINY"};
    public boolean music = true;
    public boolean sound = true;
    public boolean invertMouse = false;
    public boolean showFPS = false;
    public int renderDistance = 0;
    public KeyBinding forward = new KeyBinding("Forward", 17);
    public KeyBinding left = new KeyBinding("Left", 30);
    public KeyBinding back = new KeyBinding("Back", 31);
    public KeyBinding right = new KeyBinding("Right", 32);
    public KeyBinding jump = new KeyBinding("Jump", 57);
    public KeyBinding build = new KeyBinding("Build", 48);
    public KeyBinding chat = new KeyBinding("Chat", 20);
    public KeyBinding toggleFog = new KeyBinding("Toggle fog", 33);
    public KeyBinding save = new KeyBinding("Save location", 28);
    public KeyBinding load = new KeyBinding("Load location", 19);
    public KeyBinding[] keyBindings = new KeyBinding[]{this.forward, this.left, this.back, this.right, this.jump, this.build, this.chat, this.toggleFog, this.save, this.load};

    public Options(Minecraft minecraft1) {
        this.loadOptions();
    }

    public final String getKeyBinding(int i1) {
        return this.keyBindings[i1].name + ": " + Keyboard.getKeyName(this.keyBindings[i1].key);
    }

    public final void setKeyBinding(int i1, int i2) {
        this.keyBindings[i1].key = i2;
        this.saveOptions();
    }

    public final void setOption(int i1, int i2) {
        if(i1 == 0) {
            this.music = !this.music;
            if (!this.music) {
                Minecraft.minecraft.soundPlayer.stopSound(Minecraft.minecraft.soundManager.playingMusic);
            }
        }

        if(i1 == 1) {
            this.sound = !this.sound;
            if (!this.sound) {
                Minecraft.minecraft.soundPlayer.stopNotMusic(Minecraft.minecraft.soundManager.playingMusic);
            }
        }

        if(i1 == 2) {
            this.invertMouse = !this.invertMouse;
        }

        if(i1 == 3) {
            this.showFPS = !this.showFPS;
        }

        if(i1 == 4) {
            this.renderDistance = this.renderDistance + i2 & 3;
        }

        this.saveOptions();
    }

    public final String getOption(int i1) {
        return i1 == 0 ? "Music: " + (this.music ? "ON" : "OFF") : (i1 == 1 ? "Sound: " + (this.sound ? "ON" : "OFF") : (i1 == 2 ? "Invert mouse: " + (this.invertMouse ? "ON" : "OFF") : (i1 == 3 ? "Show FPS: " + (this.showFPS ? "ON" : "OFF") : (i1 == 4 ? "Render distance: " + RENDER_DISTANCES[this.renderDistance] : ""))));
    }

    private void loadOptions() {
        try {
            byte[] options = EagRuntime.getStorage("options.txt");
            if(options != null) {
                BufferedReader bufferedReader1 = new BufferedReader(
                        new InputStreamReader(new EaglerInputStream(options)));
                String string2 = null;

                while((string2 = bufferedReader1.readLine()) != null) {
                    String[] string5;
                    if((string5 = string2.split(":"))[0].equals("music")) {
                        this.music = string5[1].equals("true");
                    }

                    if(string5[0].equals("sound")) {
                        this.sound = string5[1].equals("true");
                    }

                    if(string5[0].equals("invertYMouse")) {
                        this.invertMouse = string5[1].equals("true");
                    }

                    if(string5[0].equals("showFrameRate")) {
                        this.showFPS = string5[1].equals("true");
                    }

                    if(string5[0].equals("viewDistance")) {
                        this.renderDistance = Integer.parseInt(string5[1]);
                    }

                    for(int i3 = 0; i3 < this.keyBindings.length; ++i3) {
                        if(string5[0].equals("key_" + this.keyBindings[i3].name)) {
                            this.keyBindings[i3].key = Integer.parseInt(string5[1]);
                        }
                    }
                }

                bufferedReader1.close();
            }
        } catch (Exception exception4) {
            System.out.println("Failed to load options");
            exception4.printStackTrace();
        }
    }

    private void saveOptions() {
        try {
            EaglerOutputStream bao = new EaglerOutputStream();
            PrintWriter printWriter1 = new PrintWriter(new OutputStreamWriter(bao));
            printWriter1.println("music:" + this.music);
            printWriter1.println("sound:" + this.sound);
            printWriter1.println("invertYMouse:" + this.invertMouse);
            printWriter1.println("showFrameRate:" + this.showFPS);
            printWriter1.println("viewDistance:" + this.renderDistance);

            for(int i2 = 0; i2 < this.keyBindings.length; ++i2) {
                printWriter1.println("key_" + this.keyBindings[i2].name + ":" + this.keyBindings[i2].key);
            }

            printWriter1.close();
            byte[] options = bao.toByteArray();
            if (options != null) {
                EagRuntime.setStorage("options.txt", options);
            }
        } catch (Exception exception3) {
            System.out.println("Failed to save options");
            exception3.printStackTrace();
        }
    }
}
