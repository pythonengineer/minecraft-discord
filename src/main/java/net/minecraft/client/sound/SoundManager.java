package net.minecraft.client.sound;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.EaglercraftSoundManager;
import net.lax1dude.eaglercraft.internal.EnumPlatformOS;
import net.minecraft.client.GameSettings;
import net.minecraft.game.entity.EntityLiving;

public class SoundManager {
    private static EaglercraftSoundManager sndManager;
    private SoundPool soundPoolSounds = new SoundPool();
    private SoundPool soundPoolStreaming = new SoundPool();
    private SoundPool soundPoolMusic = new SoundPool();
    private GameSettings options;
    private EaglercraftRandom rand = new EaglercraftRandom();
    private int ticksBeforeMusic = this.rand.nextInt(12000);
    private SoundPoolEntry playingMusic;
    private SoundPoolEntry playingStreaming;

    public void loadSoundSettings(GameSettings options) {
        this.soundPoolStreaming.isGetRandomSound = false;
        this.options = options;
        sndManager = new EaglercraftSoundManager();
    }

    public void registerSounds() {
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

        stream = EagRuntime.getResourceStream("/assets/streaming.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String file;
            while ((file = reader.readLine()) != null) {
                this.addStreaming(file, "/assets/streaming/" + file);
            }
        } catch (Exception e) {
        }
    }

    public void onSoundOptionsChanged() {
        if(!this.options.music) {
            sndManager.stopAllStatic();
        }

    }

    public void closeMinecraft() {
        sndManager.stopAllSounds();
    }

    public void addSound(String soundName, String soundFile) {
        EagRuntime.getRequiredResourceBytes(soundName);
        this.soundPoolSounds.addSound(sndManager, soundFile, soundName);
    }

    public void addStreaming(String streamingName, String streamingFile) {
        EagRuntime.getRequiredResourceBytes(streamingName);
        this.soundPoolStreaming.addSound(sndManager, streamingName, streamingFile);
    }

    public void addMusic(String musicName, String musicFile) {
        if (EagRuntime.getPlatformOS() != EnumPlatformOS.IPHONE && this.options.music) {
            EagRuntime.getRequiredResourceBytes(musicFile);
        }

        this.soundPoolMusic.addSound(sndManager, musicName, musicFile);
    }

    public void playRandomMusicIfReady() {
        if(this.options.music) {
            if(!sndManager.isSoundPlaying(this.playingMusic) && !sndManager.isSoundPlaying(this.playingStreaming) && (this.playingMusic == null || !this.playingMusic.queued)) {
                if(this.ticksBeforeMusic > 0) {
                    --this.ticksBeforeMusic;
                    return;
                }

                this.playingMusic = this.soundPoolMusic.getRandomSound();
                this.playingMusic.playStatic = true;
                if(this.playingMusic != null) {
                    this.ticksBeforeMusic = this.rand.nextInt(24000) + 24000;
                    this.play(this.playingMusic);
                }
            }

        }
    }

    public SoundPoolEntry play(SoundPoolEntry sound) {
        sndManager.playSound(sound);
        return sound;
    }

    public void setListener(EntityLiving livingEntity, float partialTicks) {
        if(this.options.sound) {
            sndManager.setListener(livingEntity, partialTicks);
        }
    }

    public void playStreaming(String soundName, float x, float y, float z, float volume, float pitch) {
        if(this.options.sound) {
            sndManager.stopSound(this.playingStreaming);
            if(soundName != null) {
                SoundPoolEntry entry = this.soundPoolStreaming.getRandomSoundFromSoundPool(soundName);
                if(entry != null && volume > 0.0F) {
                    sndManager.stopSound(this.playingMusic);
                    this.playingStreaming = new SoundPoolEntry(entry, x, y, z, 4.0F, 1.0F, 0.5F);
                    this.play(this.playingStreaming);
                }

            }
        }
    }

    public void playSound(String soundName, float x, float y, float z, float volume, float pitch) {
        if(this.options.sound) {
            SoundPoolEntry entry = this.soundPoolSounds.getRandomSoundFromSoundPool(soundName);
            if(entry != null && volume > 0.0F) {
                this.play(new SoundPoolEntry(entry, x, y, z, volume, pitch));
            }

        }
    }

    public void playSoundFX(String fxSoundName, float volume, float pitch) {
        if(this.options.sound) {
            SoundPoolEntry entry = this.soundPoolSounds.getRandomSoundFromSoundPool(fxSoundName);
            if(entry != null) {
                this.play(new SoundPoolEntry(entry, true, 1.0F, 0.25F));
            }

        }
    }
}
