package com.mojang.minecraft.sound;

import net.lax1dude.eaglercraft.EaglercraftSoundManager;
import net.lax1dude.eaglercraft.internal.IAudioResource;

public final class Sound {
    private EaglercraftSoundManager mgr;
    public SoundPos pos;
    public String url;
    public float pitch = 1.0F;
    public float volume = 1.0F;

    public Sound(EaglercraftSoundManager mgr, SoundPos soundPos, String url, float pitch, float volume) {
        this.mgr = mgr;
        this.pos = soundPos;
        this.url = url;
        this.pitch = pitch;
        this.volume = volume;
    }

    public Sound(SoundPos soundPos, String url) {
        this.pos = soundPos;
        this.url = url;
    }

    public void finish(IAudioResource buffer) {
        this.mgr.playSoundLoaded(this, buffer);
    }
}