package com.mojang.minecraft;

import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglerInputStream;
import net.lax1dude.eaglercraft.EaglerOutputStream;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;
import net.lax1dude.eaglercraft.opengl.ImageData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;

public final class Options {
	private static final String[] RENDER_DISTANCES = new String[]{"FAR", "NORMAL", "SHORT", "TINY"};
	public boolean music = true;
	public boolean sound = true;
	public boolean invertYMouse = false;
	public boolean showFramerate = false;
	public int viewDistance = 0;
	public boolean bobView = true;
	public boolean anaglyph3d = false;
	public boolean limitFramerate = false;
	public KeyMapping forward = new KeyMapping("Forward", 17);
	public KeyMapping left = new KeyMapping("Left", 30);
	public KeyMapping back = new KeyMapping("Back", 31);
	public KeyMapping right = new KeyMapping("Right", 32);
	public KeyMapping jump = new KeyMapping("Jump", 57);
	public KeyMapping build = new KeyMapping("Build", 48);
	public KeyMapping chat = new KeyMapping("Chat", 20);
	public KeyMapping toggleFog = new KeyMapping("Toggle fog", 33);
	private KeyMapping save = new KeyMapping("Save location", 28);
	private KeyMapping load = new KeyMapping("Load location", 19);
	public KeyMapping[] keys = new KeyMapping[]{this.forward, this.left, this.back, this.right, this.jump, this.build, this.chat, this.toggleFog, this.save, this.load};
	private Minecraft minecraft;

    public Options(Minecraft minecraft) {
        this.minecraft = minecraft;
        this.load();
    }

    public final String getKeyMessage(int key) {
        return this.keys[key].name + ": " + Keyboard.getKeyName(this.keys[key].key);
    }

    public final void setKey(int keyNum, int key) {
        this.keys[keyNum].key = key;
        this.save();
    }

    public final void setOption(int id1, int id2) {
        if(id1 == 0) {
            this.music = !this.music;
            if (!this.music) {
                this.minecraft.soundPlayer.stopSound(this.minecraft.soundEngine.playingMusic);
            }
        }

        if(id1 == 1) {
            this.sound = !this.sound;
            if (!this.sound) {
                this.minecraft.soundPlayer.stopNotMusic(this.minecraft.soundEngine.playingMusic);
            }
        }

        if(id1 == 2) {
            this.invertYMouse = !this.invertYMouse;
        }

        if(id1 == 3) {
            this.showFramerate = !this.showFramerate;
        }

        if(id1 == 4) {
            this.viewDistance = this.viewDistance + id2 & 3;
        }

        if(id1 == 5) {
            this.bobView = !this.bobView;
        }

        if(id1 == 6) {
            this.anaglyph3d = !this.anaglyph3d;
            Textures id11 = this.minecraft.textures;
            Iterator id21 = this.minecraft.textures.pixelsMap.keySet().iterator();

            int i3;
            ImageData bufferedImage4;
            while(id21.hasNext()) {
                i3 = ((Integer)id21.next()).intValue();
                bufferedImage4 = (ImageData)id11.pixelsMap.get(i3);
                id11.addTexture(bufferedImage4, i3);
            }

            id21 = id11.idMap.keySet().iterator();

            while(id21.hasNext()) {
                String string8 = (String)id21.next();

                try {
                    if(string8.startsWith("##")) {
                        bufferedImage4 = ImageData.loadImageFile("/assets" + string8.substring(2));
                    } else {
                        bufferedImage4 = ImageData.loadImageFile("/assets" + string8);
                    }

                    i3 = ((Integer)id11.idMap.get(string8)).intValue();
                    id11.addTexture(bufferedImage4, i3);
                } catch (Exception exception5) {
                    exception5.printStackTrace();
                }
            }
        }

        this.save();
    }

    public final String getMessage(int option) {
        return option == 0 ? "Music: " + (this.music ? "ON" : "OFF") : (option == 1 ? "Sound: " + (this.sound ? "ON" : "OFF") : (option == 2 ? "Invert mouse: " + (this.invertYMouse ? "ON" : "OFF") : (option == 3 ? "Show FPS: " + (this.showFramerate ? "ON" : "OFF") : (option == 4 ? "Render distance: " + RENDER_DISTANCES[this.viewDistance] : (option == 5 ? "View bobbing: " + (this.bobView ? "ON" : "OFF") : (option == 6 ? "3d anaglyph: " + (this.anaglyph3d ? "ON" : "OFF") : ""))))));
    }

    private void load() {
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
                        this.invertYMouse = string5[1].equals("true");
                    }

                    if(string5[0].equals("showFrameRate")) {
                        this.showFramerate = string5[1].equals("true");
                    }

                    if(string5[0].equals("viewDistance")) {
                        this.viewDistance = Integer.parseInt(string5[1]);
                    }

                    if(string5[0].equals("bobView")) {
                        this.bobView = string5[1].equals("true");
                    }

                    if(string5[0].equals("anaglyph3d")) {
                        this.anaglyph3d = string5[1].equals("true");
                    }

                    for(int i3 = 0; i3 < this.keys.length; ++i3) {
                        if(string5[0].equals("key_" + this.keys[i3].name)) {
                            this.keys[i3].key = Integer.parseInt(string5[1]);
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

    private void save() {
        try {
            EaglerOutputStream bao = new EaglerOutputStream();
            PrintWriter printWriter1 = new PrintWriter(new OutputStreamWriter(bao));
            printWriter1.println("music:" + this.music);
            printWriter1.println("sound:" + this.sound);
            printWriter1.println("invertYMouse:" + this.invertYMouse);
            printWriter1.println("showFrameRate:" + this.showFramerate);
            printWriter1.println("viewDistance:" + this.viewDistance);
            printWriter1.println("bobView:" + this.bobView);
            printWriter1.println("anaglyph3d:" + this.anaglyph3d);

            for(int i2 = 0; i2 < this.keys.length; ++i2) {
                printWriter1.println("key_" + this.keys[i2].name + ":" + this.keys[i2].key);
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
