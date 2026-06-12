package net.minecraft.client.sound;

import net.lax1dude.eaglercraft.EaglercraftSoundManager;
import net.lax1dude.eaglercraft.internal.IAudioResource;

public class SoundPoolEntry {
    private EaglercraftSoundManager mgr;
    public String soundName;
    public String soundUrl;
    public boolean playStatic = false;
    public boolean queued = false;
    public float pitch = 1.0F;
    public float volume = 1.0F;
    public float finalVolume = -1.0F;
    public float x;
    public float y;
    public float z;

    public SoundPoolEntry(EaglercraftSoundManager mgr, String soundName, String soundUrl) {
        this.mgr = mgr;
        this.soundName = soundName;
        this.soundUrl = soundUrl;
    }

    public SoundPoolEntry(SoundPoolEntry sound, float x, float y, float z, float volume, float pitch, float finalVolume) {
        this(sound.mgr, sound.soundName, sound.soundUrl);
        this.x = x;
        this.y = y;
        this.z = z;
        this.volume = volume;
        this.pitch = pitch;
        this.finalVolume = finalVolume;
    }

    public SoundPoolEntry(SoundPoolEntry sound, float x, float y, float z, float volume, float pitch) {
        this(sound.mgr, sound.soundName, sound.soundUrl);
        this.x = x;
        this.y = y;
        this.z = z;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundPoolEntry(SoundPoolEntry sound, boolean playStatic, float pitch, float volume) {
        this(sound.mgr, sound.soundName, sound.soundUrl);
        this.playStatic = playStatic;
        this.pitch = pitch;
        this.volume = volume;
    }

    public void finish(IAudioResource buffer) {
        this.mgr.playSoundLoaded(this, buffer);
    }
}
