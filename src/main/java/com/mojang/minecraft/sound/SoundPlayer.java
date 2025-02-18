package com.mojang.minecraft.sound;

import com.mojang.minecraft.player.Player;

import net.lax1dude.eaglercraft.EaglercraftSoundManager;

public final class SoundPlayer {
    private final EaglercraftSoundManager sndManager;
    public boolean enabled = true;

    public SoundPlayer() {
        this.sndManager = new EaglercraftSoundManager();
    }

    public final void stop() {
        this.sndManager.stopAllSounds();
    }

    public final void play(Sound audioInfo1, SoundPos soundPos2) {
        if (this.enabled) {
            this.sndManager.playSound(new Sound(soundPos2, audioInfo1.url, audioInfo1.pitch, audioInfo1.volume));
        }
    }

    public void setListener(Player player, float parFloat1) {
        this.sndManager.setListener(player, parFloat1);
    }
}