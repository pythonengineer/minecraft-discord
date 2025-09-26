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
    private SoundPoolEntry currentMusic;

    public final void loadSoundSettings(GameSettings var1) {
        this.options = var1;
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

    public final void addSound(String var1, String var2) {
        EagRuntime.getRequiredResourceBytes(var1);
        this.soundPoolSounds.addSound(this.sndManager, var2, var1);
    }

    public final void addMusic(String var1, String var2) {
        if (EagRuntime.getPlatformOS() != EnumPlatformOS.IPHONE && this.options.music) {
            EagRuntime.getRequiredResourceBytes(var2);
        }

        this.soundPoolMusic.addSound(this.sndManager, var1, var2);
    }

    public final void playRandomMusicIfReady(float var1, float var2, float var3) {
        if(this.options.music) {
            if (this.currentMusic == null || (!this.sndManager.isSoundPlaying(this.currentMusic) && !this.currentMusic.queued)) {
                this.currentMusic = this.soundPoolMusic.getRandomSoundFromSoundPool("calm");
                this.currentMusic.playStatic = true;
                this.play(this.currentMusic);
            }
        }

    }

    public final SoundPoolEntry play(SoundPoolEntry sound) {
        this.sndManager.playSound(sound);
        return sound;
    }

    public final void setListener(EntityLiving var1, float var2) {
        if(this.options.sound) {
            this.sndManager.setListener(var1, var2);
        }
    }

    public final void playSound(String var1, float var2, float var3, float var4, float var5, float var6) {
        if(this.options.sound) {
            SoundPoolEntry var8 = this.soundPoolSounds.getRandomSoundFromSoundPool(var1);
            if(var8 != null && var5 > 0.0F) {
                this.play(new SoundPoolEntry(var8, var2, var3, var4, var5, var6));
            }

        }
    }

    public final void playSoundFX(String var1, float var2, float var3) {
        if(this.options.sound) {
            SoundPoolEntry var4 = this.soundPoolSounds.getRandomSoundFromSoundPool(var1);
            if(var4 != null) {
                this.play(new SoundPoolEntry(var4, true, 1.0F, 0.25F));
            }

        }
    }
}
