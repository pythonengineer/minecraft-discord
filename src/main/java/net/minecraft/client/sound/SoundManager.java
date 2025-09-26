package net.minecraft.client.sound;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftSoundManager;
import net.lax1dude.eaglercraft.internal.EnumPlatformOS;
import net.minecraft.client.GameSettings;
import net.minecraft.game.entity.EntityLiving;

public final class SoundManager {
    private SoundPool soundPoolSounds = new SoundPool();
    private SoundPool soundPoolMusic = new SoundPool();
    private GameSettings options;
    private SoundPoolEntry currentMusic;

    public final void loadSoundSettings(GameSettings var1) {
        this.options = var1;
    }

    public final void registerSounds() {
    }

    public final void onSoundOptionsChanged() {
    }

    public final void closeMinecraft() {
    }

    public final void addSound(String var1, String var2) {
        EagRuntime.getRequiredResourceBytes(var1);
    }

    public final void addMusic(String var1, String var2) {
        if (EagRuntime.getPlatformOS() != EnumPlatformOS.IPHONE && this.options.music) {
            EagRuntime.getRequiredResourceBytes(var2);
        }
    }
}
