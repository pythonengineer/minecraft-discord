package com.mojang.minecraft.sound;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.internal.PlatformAudio;

public final class SoundManager {
    public Map sounds = new HashMap();
    private Map music = new HashMap();
    public EaglercraftRandom random = new EaglercraftRandom();
    public long lastMusic = System.currentTimeMillis() + 60000L;
    public Sound playingMusic;

    public final void registerSounds() {
        InputStream stream = EagRuntime.getResourceStream("/assets/sounds.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String file;
            while ((file = reader.readLine()) != null) {
                this.registerSound("/assets/sound/" + file, file);
            }
        } catch (Exception e) {
        }

        stream = EagRuntime.getResourceStream("/assets/music.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String file;
            while ((file = reader.readLine()) != null) {
                this.registerMusic(file, "/assets/music/" + file);
            }
        } catch (Exception e) {
        }
    }

    public final Sound getAudioInfo(String string1, float f2, float f3) {
        List list4 = null;
        Map map5 = this.sounds;
        synchronized(this.sounds) {
            list4 = (List)this.sounds.get(string1);
        }

        if(list4 == null) {
            return null;
        } else {
            Sound sound = (Sound)list4.get(this.random.nextInt(list4.size()));
            sound.pitch = f3;
            sound.volume = f2;
            return sound;
        }
    }

    public void registerSound(String file1, String string2) {
        try {
            for(string2 = string2.substring(0, string2.length() - 4).replaceAll("/", "."); Character.isDigit(string2.charAt(string2.length() - 1)); string2 = string2.substring(0, string2.length() - 1)) {
            }

            EagRuntime.getRequiredResourceBytes(file1);

            Sound sound = new Sound(null, file1);
            Map map3 = this.sounds;
            synchronized(this.sounds) {
                Object object4;
                if((object4 = (List)this.sounds.get(string2)) == null) {
                    object4 = new ArrayList();
                    this.sounds.put(string2, object4);
                }

                ((List)object4).add(sound);
            }
        } catch (Exception exception6) {
            exception6.printStackTrace();
        }

    }

    public final void registerMusic(String string1, String file2) {
        Map map3 = this.music;
        synchronized(this.music) {
            for(string1 = string1.substring(0, string1.length() - 4).replaceAll("/", "."); Character.isDigit(string1.charAt(string1.length() - 1)); string1 = string1.substring(0, string1.length() - 1)) {
            }

            EagRuntime.getRequiredResourceBytes(file2);
            if (EagRuntime.getPlatformType() != EnumPlatformType.DESKTOP) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PlatformAudio.loadAudioDataNew(file2, true,
                                EagRuntime.browserResourcePackLoader);
                    }
                }).start();
            }

            Object object4;
            if((object4 = (List)this.music.get(string1)) == null) {
                object4 = new ArrayList();
                this.music.put(string1, object4);
            }

            ((List)object4).add(file2);
        }
    }

    public boolean playMusic(SoundPlayer soundPlayer1, String string2) {
        if(!soundPlayer1.options.music) {
            return false;
        }

        List list3 = null;
        Map map4 = this.music;
        synchronized(this.music) {
            list3 = (List)this.music.get(string2);
        }

        if(list3 == null) {
            return false;
        } else {
            String file8 = (String)list3.get(this.random.nextInt(list3.size()));
            this.playingMusic = soundPlayer1.play(new Sound(null, file8), null);
            return true;
        }
    }
}