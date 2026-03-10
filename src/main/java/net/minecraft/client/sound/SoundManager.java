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
    private EaglercraftSoundManager sndManager;
    private SoundPool soundPoolSounds = new SoundPool();
    private SoundPool soundPoolMusic = new SoundPool();
    private GameSettings options;

    public final void loadSoundSettings(GameSettings options) {
        this.options = options;
        this.sndManager = new EaglercraftSoundManager();
    }

    public final void registerSounds() {
        InputStream stream = EagRuntime.getResourceStream("/assets/sounds.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String file;
            while ((file = reader.readLine()) != null) {
                this.addSound("/assets/sound/" + file, file);
            }
        } catch (Exception e) {
        }

        stream = EagRuntime.getResourceStream("/assets/music.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String file;
            while ((file = reader.readLine()) != null) {
                this.addMusic(file, "/assets/music/" + file);
            }
        } catch (Exception e) {
        }
    }

    public final void onSoundOptionsChanged() {
        if(!this.options.music) {
            this.sndManager.stopAllStatic();
        }

    }

    public final void closeMinecraft() {
        this.sndManager.stopAllSounds();
    }

    public final void addSound(String soundName, String soundFile) {
        EagRuntime.getRequiredResourceBytes(soundName);
        this.soundPoolSounds.addSound(this.sndManager, soundFile, soundName);
    }

    public final void addMusic(String musicName, String musicFile) {
        if (EagRuntime.getPlatformOS() != EnumPlatformOS.IPHONE && this.options.music) {
            EagRuntime.getRequiredResourceBytes(musicFile);
        }

        this.soundPoolMusic.addSound(this.sndManager, musicName, musicFile);
    }

    public final SoundPoolEntry play(SoundPoolEntry sound) {
        this.sndManager.playSound(sound);
        return sound;
    }

    public final void setListener(EntityLiving livingEntity, float partialTicks) {
        if(this.options.sound) {
            this.sndManager.setListener(livingEntity, partialTicks);
        }
    }

    public final void playSound(String soundName, float x, float y, float z, float volume, float pitch) {
        if(this.options.sound) {
            SoundPoolEntry entry = this.soundPoolSounds.getRandomSoundFromSoundPool(soundName);
            if(entry != null && volume > 0.0F) {
                this.play(new SoundPoolEntry(entry, x, y, z, volume, pitch));
            }

        }
    }

    public final void playSoundFX(String fxSoundName, float volume, float pitch) {
        if(this.options.sound) {
            SoundPoolEntry entry = this.soundPoolSounds.getRandomSoundFromSoundPool(fxSoundName);
            if(entry != null) {
                this.play(new SoundPoolEntry(entry, true, 1.0F, 0.25F));
            }

        }
    }
}
