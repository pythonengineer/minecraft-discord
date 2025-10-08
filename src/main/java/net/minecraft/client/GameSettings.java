package net.minecraft.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglerInputStream;
import net.lax1dude.eaglercraft.EaglerOutputStream;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;

public final class GameSettings {
    private static final String[] GUI_SCALES = new String[]{"AUTO", "SMALL",
            "NORMAL", "LARGE" };
    private static final String[] RENDER_DISTANCES = new String[]{"FAR", "NORMAL", "SHORT", "TINY"};
    private static final String[] DIFFICULTIES = new String[]{"Peaceful", "Easy", "Normal", "Hard"};
    public boolean music = true;
    public boolean sound = true;
    public boolean invertMouse = false;
    public boolean showFPS = false;
    public int renderDistance = 0;
    public boolean viewBobbing = true;
    public boolean anaglyph = false;
    public boolean limitFramerate = false;
    public boolean touchscreen;
    public int guiScale = 3;
    public KeyBinding keyBindForward = new KeyBinding("Forward", 17);
    public KeyBinding keyBindLeft = new KeyBinding("Left", 30);
    public KeyBinding keyBindBack = new KeyBinding("Back", 31);
    public KeyBinding keyBindRight = new KeyBinding("Right", 32);
    public KeyBinding keyBindJump = new KeyBinding("Jump", 57);
    public KeyBinding keyBindInventory = new KeyBinding("Inventory", 23);
    public KeyBinding keyBindDrop = new KeyBinding("Drop", 16);
    private KeyBinding keyBindChat = new KeyBinding("Chat", 20);
    public KeyBinding keyBindToggleFog = new KeyBinding("Toggle fog", 33);
    public KeyBinding keyBindSave = new KeyBinding("Save location", 28);
    public KeyBinding keyBindLoad = new KeyBinding("Load location", 19);
    public KeyBinding[] keyBindings = new KeyBinding[]{this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindToggleFog, this.keyBindSave, this.keyBindLoad};
    private Minecraft mc;
    public int numberOfOptions = 10;
    public int difficulty = 2;
    public boolean thirdPersonView = false;

    public GameSettings(Minecraft var1) {
        this.mc = var1;
        this.loadOptions();
    }

    public final String getOptionDisplayString(int var1) {
        return this.keyBindings[var1].keyDescription + ": " + Keyboard.getKeyName(this.keyBindings[var1].keyCode);
    }

    public final void setKeyBinding(int var1, int var2) {
        this.keyBindings[var1].keyCode = var2;
        this.saveOptions();
    }

    public final void setOptionValue(int var1, int var2) {
        if(var1 == 0) {
            this.music = !this.music;
            this.mc.sndManager.onSoundOptionsChanged();
        }

        if(var1 == 1) {
            this.sound = !this.sound;
            this.mc.sndManager.onSoundOptionsChanged();
        }

        if(var1 == 2) {
            this.invertMouse = !this.invertMouse;
        }

        if(var1 == 3) {
            this.showFPS = !this.showFPS;
        }

        if(var1 == 4) {
            this.renderDistance = this.renderDistance + var2 & 3;
        }

        if(var1 == 5) {
            this.viewBobbing = !this.viewBobbing;
        }

        if(var1 == 6) {
            this.anaglyph = !this.anaglyph;
            this.mc.renderEngine.refreshTextures();
        }

        if(var1 == 7) {
            this.limitFramerate = !this.limitFramerate;
        }

        if(var1 == 8) {
            this.guiScale = this.guiScale + var2 & 3;
        }

        if(var1 == 9) {
            this.difficulty = this.difficulty + var2 & 3;
        }

        this.saveOptions();
    }

    public final String getOptionString(int var1) {
        return var1 == 0 ? "Music: " + (this.music ? "ON" : "OFF") : (var1 == 1 ? "Sound: " + (this.sound ? "ON" : "OFF") : (var1 == 2 ? "Invert mouse: " + (this.invertMouse ? "ON" : "OFF") : (var1 == 3 ? "Show FPS: " + (this.showFPS ? "ON" : "OFF") : (var1 == 4 ? "Render distance: " + RENDER_DISTANCES[this.renderDistance] : (var1 == 5 ? "View bobbing: " + (this.viewBobbing ? "ON" : "OFF") : (var1 == 6 ? "3d anaglyph: " + (this.anaglyph ? "ON" : "OFF") : (var1 == 7 ? "Limit framerate: " + (this.limitFramerate ? "ON" : "OFF") : (var1 == 8 ? "GUI Scale: " + GUI_SCALES[this.guiScale] : (var1 == 9 ? "Difficulty: " + DIFFICULTIES[this.difficulty] : "")))))))));
    }

    private void loadOptions() {
        try {
            byte[] options = EagRuntime.getStorage("g");
            if(options != null) {
                BufferedReader var1 = new BufferedReader(
                        new InputStreamReader(new EaglerInputStream(options)));

                while(true) {
                    String var2 = var1.readLine();
                    if(var2 == null) {
                        var1.close();
                        return;
                    }

                    String[] var5 = var2.split(":");
                    if(var5[0].equals("music")) {
                        this.music = var5[1].equals("true");
                    }

                    if(var5[0].equals("sound")) {
                        this.sound = var5[1].equals("true");
                    }

                    if(var5[0].equals("invertYMouse")) {
                        this.invertMouse = var5[1].equals("true");
                    }

                    if(var5[0].equals("showFrameRate")) {
                        this.showFPS = var5[1].equals("true");
                    }

                    if(var5[0].equals("viewDistance")) {
                        this.renderDistance = Integer.parseInt(var5[1]);
                    }

                    if(var5[0].equals("guiScale")) {
                        this.guiScale = Integer.parseInt(var5[1]);
                    }

                    if(var5[0].equals("bobView")) {
                        this.viewBobbing = var5[1].equals("true");
                    }

                    if(var5[0].equals("anaglyph3d")) {
                        this.anaglyph = var5[1].equals("true");
                    }

                    if(var5[0].equals("limitFramerate")) {
                        this.limitFramerate = var5[1].equals("true");
                    }

                    if(var5[0].equals("difficulty")) {
                        this.difficulty = Integer.parseInt(var5[1]);
                    }

                    for(int var3 = 0; var3 < this.keyBindings.length; ++var3) {
                        if(var5[0].equals("key_" + this.keyBindings[var3].keyDescription)) {
                            this.keyBindings[var3].keyCode = Integer.parseInt(var5[1]);
                        }
                    }
                }
            }
        } catch (Exception var4) {
            System.out.println("Failed to load options");
            var4.printStackTrace();
        }
    }

    public final void saveOptions() {
        try {
            EaglerOutputStream bao = new EaglerOutputStream();
            PrintWriter var1 = new PrintWriter(new OutputStreamWriter(bao));
            var1.println("music:" + this.music);
            var1.println("sound:" + this.sound);
            var1.println("invertYMouse:" + this.invertMouse);
            var1.println("showFrameRate:" + this.showFPS);
            var1.println("viewDistance:" + this.renderDistance);
            var1.println("guiScale:" + this.guiScale);
            var1.println("bobView:" + this.viewBobbing);
            var1.println("anaglyph3d:" + this.anaglyph);
            var1.println("limitFramerate:" + this.limitFramerate);
            var1.println("difficulty:" + this.difficulty);

            for(int var2 = 0; var2 < this.keyBindings.length; ++var2) {
                var1.println("key_" + this.keyBindings[var2].keyDescription + ":" + this.keyBindings[var2].keyCode);
            }

            var1.close();
            byte[] options = bao.toByteArray();
            if (options != null) {
                EagRuntime.setStorage("g", options);
            }
        } catch (Exception var3) {
            System.out.println("Failed to save options");
            var3.printStackTrace();
        }
    }
}
