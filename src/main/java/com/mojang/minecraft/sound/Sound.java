package com.mojang.minecraft.sound;

public final class Sound {
    public SoundPos pos;
    public String url;
    public float pitch = 1.0F;
    public float volume = 1.0F;

    public Sound(SoundPos soundPos2, String url, float pitch, float volume) {
        this.pos = soundPos2;
        this.url = url;
        this.pitch = pitch;
        this.volume = volume;
    }

    public Sound(SoundPos soundPos2, String url) {
        this.pos = soundPos2;
        this.url = url;
    }
}