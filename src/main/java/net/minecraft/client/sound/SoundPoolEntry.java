package net.minecraft.client.sound;

import net.lax1dude.eaglercraft.EaglercraftSoundManager;
import net.lax1dude.eaglercraft.internal.IAudioResource;

public final class SoundPoolEntry {
    private EaglercraftSoundManager mgr;
    public String soundName;
    public String soundUrl;
    public boolean playStatic = false;
    public float pitch = 1.0F;
    public float volume = 1.0F;
    public float x;
    public float y;
    public float z;

    public SoundPoolEntry(EaglercraftSoundManager mgr, String var1, String var2) {
        this.mgr = mgr;
        this.soundName = var1;
        this.soundUrl = var2;
    }

    public SoundPoolEntry(SoundPoolEntry sound, float x, float y, float z, float volume, float pitch) {
        this(sound.mgr, sound.soundName, sound.soundUrl);
        this.x = x;
        this.y = y;
        this.z = z;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundPoolEntry(SoundPoolEntry sound, boolean playStatic) {
        this(sound.mgr, sound.soundName, sound.soundUrl);
        this.playStatic = playStatic;
    }

    public void finish(IAudioResource buffer) {
        this.mgr.playSoundLoaded(this, buffer);
    }
}
