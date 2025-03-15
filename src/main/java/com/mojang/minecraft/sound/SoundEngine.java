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

public final class SoundEngine {
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

    public final Sound getAudioInfo(String soundName, float volume, float pitch) {
        List list4 = null;
        Map map5 = this.sounds;
        synchronized(this.sounds) {
            list4 = (List)this.sounds.get(soundName);
        }

        if(list4 == null) {
            return null;
        } else {
            Sound sound = (Sound)list4.get(this.random.nextInt(list4.size()));
            sound.pitch = pitch;
            sound.volume = volume;
            return sound;
        }
    }

    public void registerSound(String soundFile, String soundName) {
        try {
            for(soundName = soundName.substring(0, soundName.length() - 4).replaceAll("/", "."); Character.isDigit(soundName.charAt(soundName.length() - 1)); soundName = soundName.substring(0, soundName.length() - 1)) {
            }

            EagRuntime.getRequiredResourceBytes(soundFile);

            Sound sound = new Sound(null, soundFile);
            Map map3 = this.sounds;
            synchronized(this.sounds) {
                Object object4;
                if((object4 = (List)this.sounds.get(soundName)) == null) {
                    object4 = new ArrayList();
                    this.sounds.put(soundName, object4);
                }

                ((List)object4).add(sound);
            }
        } catch (Exception exception6) {
            exception6.printStackTrace();
        }

    }

    public final void registerMusic(String musicName, String musicFile) {
        Map map3 = this.music;
        synchronized(this.music) {
            for(musicName = musicName.substring(0, musicName.length() - 4).replaceAll("/", "."); Character.isDigit(musicName.charAt(musicName.length() - 1)); musicName = musicName.substring(0, musicName.length() - 1)) {
            }

            EagRuntime.getRequiredResourceBytes(musicFile);

            Object object4;
            if((object4 = (List)this.music.get(musicName)) == null) {
                object4 = new ArrayList();
                this.music.put(musicName, object4);
            }

            ((List)object4).add(musicFile);
        }
    }

    public boolean playMusic(SoundPlayer soundPlayer, String musicName) {
        if(!soundPlayer.options.music) {
            return false;
        }

        List list3 = null;
        Map map4 = this.music;
        synchronized(this.music) {
            list3 = (List)this.music.get(musicName);
        }

        if(list3 == null) {
            return false;
        } else {
            String file8 = (String)list3.get(this.random.nextInt(list3.size()));
            this.playingMusic = soundPlayer.play(new Sound(null, file8), null);
            return true;
        }
    }
}