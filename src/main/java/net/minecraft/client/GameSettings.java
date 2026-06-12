package net.minecraft.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglerInputStream;
import net.lax1dude.eaglercraft.EaglerOutputStream;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;

public class GameSettings {
    private static final String[] GUI_SCALES = new String[]{"AUTO", "SMALL",
            "NORMAL", "LARGE" };
    private static final String[] RENDER_DISTANCES = new String[]{"FAR", "NORMAL", "SHORT", "TINY"};
    private static final String[] DIFFICULTY_LEVELS = new String[]{"Peaceful", "Easy", "Normal", "Hard"};
    public boolean music = true;
    public boolean sound = true;
    public boolean invertMouse = false;
    public boolean showFPS = false;
    public int renderDistance = 0;
    public boolean viewBobbing = true;
    public boolean anaglyph = false;
    public boolean limitFramerate = false;
    public boolean fancyGraphics = true;
    public boolean touchscreen;
    public int guiScale = 3;
    public KeyBinding keyBindForward = new KeyBinding("Forward", Keyboard.KEY_W);
    public KeyBinding keyBindLeft = new KeyBinding("Left", Keyboard.KEY_A);
    public KeyBinding keyBindBack = new KeyBinding("Back", Keyboard.KEY_S);
    public KeyBinding keyBindRight = new KeyBinding("Right", Keyboard.KEY_D);
    public KeyBinding keyBindJump = new KeyBinding("Jump", Keyboard.KEY_SPACE);
    public KeyBinding keyBindInventory = new KeyBinding("Inventory", Keyboard.KEY_I);
    public KeyBinding keyBindDrop = new KeyBinding("Drop", Keyboard.KEY_Q);
    public KeyBinding keyBindChat = new KeyBinding("Chat", Keyboard.KEY_T);
    public KeyBinding keyBindToggleFog = new KeyBinding("Toggle fog", Keyboard.KEY_F);
    public KeyBinding[] keyBindings = new KeyBinding[]{this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindToggleFog};
    private Minecraft mc;
    public int numberOfOptions = 11;
    public int difficulty = 2;
    public boolean thirdPersonView = false;

    public GameSettings(Minecraft mc) {
        this.mc = mc;
        this.loadOptions();
    }

    public GameSettings() {
    }

    public String getKeyBindingDescription(int keyBindIndex) {
        return this.keyBindings[keyBindIndex].keyDescription + ": " + Keyboard.getKeyName(this.keyBindings[keyBindIndex].keyCode);
    }

    public void setKeyBinding(int keyBindIndex, int keyBinding) {
        this.keyBindings[keyBindIndex].keyCode = keyBinding;
        this.saveOptions();
    }

    public void setOptionValue(int keyBindIndex, int value) {
        if(keyBindIndex == 0) {
            this.music = !this.music;
            this.mc.sndManager.onSoundOptionsChanged();
        }

        if(keyBindIndex == 1) {
            this.sound = !this.sound;
            this.mc.sndManager.onSoundOptionsChanged();
        }

        if(keyBindIndex == 2) {
            this.invertMouse = !this.invertMouse;
        }

        if(keyBindIndex == 3) {
            this.showFPS = !this.showFPS;
        }

        if(keyBindIndex == 4) {
            this.renderDistance = this.renderDistance + value & 3;
        }

        if(keyBindIndex == 5) {
            this.viewBobbing = !this.viewBobbing;
        }

        if(keyBindIndex == 6) {
            this.anaglyph = !this.anaglyph;
            this.mc.renderEngine.refreshTextures();
        }

        if(keyBindIndex == 7) {
            this.limitFramerate = !this.limitFramerate;
        }

        if(keyBindIndex == 8) {
            this.guiScale = this.guiScale + value & 3;
        }

        if(keyBindIndex == 9) {
            this.difficulty = this.difficulty + value & 3;
        }

        if(keyBindIndex == 10) {
            this.fancyGraphics = !this.fancyGraphics;
            this.mc.renderGlobal.loadRenderers();
        }

        this.saveOptions();
    }

    public String getOptionDisplayString(int keyBindIndex) {
        return keyBindIndex == 0 ? "Music: " + (this.music ? "ON" : "OFF") : (keyBindIndex == 1 ? "Sound: " + (this.sound ? "ON" : "OFF") : (keyBindIndex == 2 ? "Invert mouse: " + (this.invertMouse ? "ON" : "OFF") : (keyBindIndex == 3 ? "Show FPS: " + (this.showFPS ? "ON" : "OFF") : (keyBindIndex == 4 ? "Render distance: " + RENDER_DISTANCES[this.renderDistance] : (keyBindIndex == 5 ? "View bobbing: " + (this.viewBobbing ? "ON" : "OFF") : (keyBindIndex == 6 ? "3d anaglyph: " + (this.anaglyph ? "ON" : "OFF") : (keyBindIndex == 7 ? "Limit framerate: " + (this.limitFramerate ? "ON" : "OFF") : (keyBindIndex == 8 ? "GUI Scale: " + GUI_SCALES[this.guiScale] : (keyBindIndex == 9 ? "Difficulty: " + DIFFICULTY_LEVELS[this.difficulty] : (keyBindIndex == 10 ? "Graphics: " + (this.fancyGraphics ? "FANCY" : "FAST") : ""))))))))));
    }

    private void loadOptions() {
        try {
            byte[] options = EagRuntime.getStorage("g");
            if(options != null) {
                BufferedReader bufferedReader1 = new BufferedReader(
                        new InputStreamReader(new EaglerInputStream(options)));

                String string2;
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

                    if(string5[0].equals("guiScale")) {
                        this.guiScale = Integer.parseInt(string5[1]);
                    }

                    if(string5[0].equals("bobView")) {
                        this.viewBobbing = string5[1].equals("true");
                    }

                    if(string5[0].equals("anaglyph3d")) {
                        this.anaglyph = string5[1].equals("true");
                    }

                    if(string5[0].equals("limitFramerate")) {
                        this.limitFramerate = string5[1].equals("true");
                    }

                    if(string5[0].equals("difficulty")) {
                        this.difficulty = Integer.parseInt(string5[1]);
                    }

                    if(string5[0].equals("fancyGraphics")) {
                        this.fancyGraphics = string5[1].equals("true");
                    }

                    for(int i3 = 0; i3 < this.keyBindings.length; ++i3) {
                        if(string5[0].equals("key_" + this.keyBindings[i3].keyDescription)) {
                            this.keyBindings[i3].keyCode = Integer.parseInt(string5[1]);
                        }
                    }
                }

                bufferedReader1.close();
            }
        } catch (Exception var4) {
            System.out.println("Failed to load options");
            var4.printStackTrace();
        }
    }

    public void saveOptions() {
        try {
            EaglerOutputStream bao = new EaglerOutputStream();
            PrintWriter printWriter1 = new PrintWriter(new OutputStreamWriter(bao));
            printWriter1.println("music:" + this.music);
            printWriter1.println("sound:" + this.sound);
            printWriter1.println("invertYMouse:" + this.invertMouse);
            printWriter1.println("showFrameRate:" + this.showFPS);
            printWriter1.println("viewDistance:" + this.renderDistance);
            printWriter1.println("guiScale:" + this.guiScale);
            printWriter1.println("bobView:" + this.viewBobbing);
            printWriter1.println("anaglyph3d:" + this.anaglyph);
            printWriter1.println("limitFramerate:" + this.limitFramerate);
            printWriter1.println("difficulty:" + this.difficulty);
            printWriter1.println("fancyGraphics:" + this.fancyGraphics);

            for(int i2 = 0; i2 < this.keyBindings.length; ++i2) {
                printWriter1.println("key_" + this.keyBindings[i2].keyDescription + ":" + this.keyBindings[i2].keyCode);
            }

            printWriter1.close();
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
