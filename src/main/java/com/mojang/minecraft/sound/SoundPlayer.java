package com.mojang.minecraft.sound;

import com.mojang.minecraft.Options;
import com.mojang.minecraft.player.Player;

import net.lax1dude.eaglercraft.EaglercraftSoundManager;

public final class SoundPlayer {
    private final EaglercraftSoundManager sndManager;
    public Options options;

    public SoundPlayer(Options options1) {
        this.options = options1;
        this.sndManager = new EaglercraftSoundManager();
    }

    public final void stop() {
        this.sndManager.stopAllSounds();
    }

    public final void stopNotMusic(Sound music) {
        this.sndManager.stopAllExcept(music);
    }

    public final void stopSound(Sound sound) {
        if (sound != null) {
            this.sndManager.stopSound(sound);
        }
    }

    public final Sound play(Sound audioInfo1, SoundPos soundPos2) {
        Sound sound = null;
        if (this.options.music || this.options.sound) {
            sound = new Sound(soundPos2, audioInfo1.url, audioInfo1.pitch, audioInfo1.volume);
            this.sndManager.playSound(sound);
        }

        return sound;
    }

    public void setListener(Player player, float parFloat1) {
        this.sndManager.setListener(player, parFloat1);
    }
}