package net.minecraft.client.sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.EaglercraftSoundManager;

public final class SoundPool {
    private EaglercraftRandom b = new EaglercraftRandom();
    private Map nameToSoundPoolEntriesMapping = new HashMap();
    public int numberOfSoundPoolEntries = 0;

    public final void getFolder(EaglercraftSoundManager mgr, String var1, String var2) {
        try {
            String var3 = var1;

            for(var1 = var1.substring(0, var1.indexOf(".")); Character.isDigit(var1.charAt(var1.length() - 1)); var1 = var1.substring(0, var1.length() - 1)) {
            }

            var1 = var1.replaceAll("/", ".");
            if(!this.nameToSoundPoolEntriesMapping.containsKey(var1)) {
                this.nameToSoundPoolEntriesMapping.put(var1, new ArrayList());
            }

            ((List)this.nameToSoundPoolEntriesMapping.get(var1)).add(new SoundPoolEntry(mgr, var3, var2));
            ++this.numberOfSoundPoolEntries;
        } catch (Exception var4) {
            var4.printStackTrace();
            throw new RuntimeException(var4);
        }
    }

    public final SoundPoolEntry getRandomSoundFromSoundPool(String var1) {
        List var2 = (List)this.nameToSoundPoolEntriesMapping.get(var1);
        return var2 == null ? null : (SoundPoolEntry)var2.get(this.b.nextInt(var2.size()));
    }
}
