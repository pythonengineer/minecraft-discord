package net.minecraft.client.sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.EaglercraftSoundManager;

public class SoundPool {
    private EaglercraftRandom rand = new EaglercraftRandom();
    private Map nameToSoundPoolEntriesMapping = new HashMap();
    private List allSoundPoolEntries = new ArrayList();
    public int numberOfSoundPoolEntries = 0;
    public boolean isGetRandomSound = true;

    public SoundPoolEntry addSound(EaglercraftSoundManager mgr, String soundName, String soundFile) {
        try {
            String string3 = soundName;
            soundName = soundName.substring(0, soundName.indexOf("."));
            if(this.isGetRandomSound) {
                while(Character.isDigit(soundName.charAt(soundName.length() - 1))) {
                    soundName = soundName.substring(0, soundName.length() - 1);
                }
            }

            soundName = soundName.replaceAll("/", ".");
            if(!this.nameToSoundPoolEntriesMapping.containsKey(soundName)) {
                this.nameToSoundPoolEntriesMapping.put(soundName, new ArrayList());
            }

            SoundPoolEntry entry = new SoundPoolEntry(mgr, string3, soundFile);
            ((List)this.nameToSoundPoolEntriesMapping.get(soundName)).add(entry);
            this.allSoundPoolEntries.add(entry);
            ++this.numberOfSoundPoolEntries;
            return entry;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public SoundPoolEntry getRandomSoundFromSoundPool(String soundName) {
        List list2 = (List)this.nameToSoundPoolEntriesMapping.get(soundName);
        return list2 == null ? null : (SoundPoolEntry)list2.get(this.rand.nextInt(list2.size()));
    }

    public SoundPoolEntry getRandomSound() {
        return this.allSoundPoolEntries.size() == 0 ? null : (SoundPoolEntry)this.allSoundPoolEntries.get(this.rand.nextInt(this.allSoundPoolEntries.size()));
    }
}
