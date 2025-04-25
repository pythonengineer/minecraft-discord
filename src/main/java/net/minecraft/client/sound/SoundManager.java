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

    public final void loadSoundSettings() {
        try {
            this.sndManager = new EaglercraftSoundManager();
        } catch (Exception var1) {
            System.err.println("error linking with the LibraryJavaSound plug-in");
        }
    }

    public final void registerSounds(GameSettings options) {
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
                this.addMusic(file, "/assets/music/" + file, options);
            }
        } catch (Exception e) {
        }
    }

    public final void closeMinecraft() {
        this.sndManager.stopAllSounds();
    }

    public final void addSound(String var1, String var2) {
        EagRuntime.getRequiredResourceBytes(var1);
        this.soundPoolSounds.getFolder(this.sndManager, var2, var1);
    }

    public final void addMusic(String var1, String var2, GameSettings options) {
        if (EagRuntime.getPlatformOS() != EnumPlatformOS.IPHONE || options.music) {
            EagRuntime.getRequiredResourceBytes(var2);
        }

        this.soundPoolMusic.getFolder(this.sndManager, var1, var2);
        if(this.soundPoolMusic.numberOfSoundPoolEntries == 3 && options.music) {
            SoundPoolEntry var3 = this.soundPoolMusic.getRandomSoundFromSoundPool("calm");
            var3.playStatic = true;
            this.play(var3);
        }

    }

    public final SoundPoolEntry play(SoundPoolEntry sound) {
        this.sndManager.playSound(sound);
        return sound;
    }

    public final void setListener(EntityLiving var1, float var2) {
        this.sndManager.setListener(var1, var2);
    }

    public final void a(String var1, float var2, float var3, float var4, float var5, float var6) {
        SoundPoolEntry var8 = this.soundPoolSounds.getRandomSoundFromSoundPool(var1);
        if(var8 != null) {
            this.play(new SoundPoolEntry(var8, var2, var3, var4, var5, var6));
        }

    }

    public final void playSound(String var1, float var2, float var3) {
        SoundPoolEntry var4 = this.soundPoolSounds.getRandomSoundFromSoundPool(var1);
        if(var4 != null) {
            this.play(new SoundPoolEntry(var4, true));
        }

    }
}
