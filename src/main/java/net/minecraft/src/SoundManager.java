package net.minecraft.src;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.EaglercraftSoundManager;
import net.lax1dude.eaglercraft.internal.EnumPlatformOS;

public class SoundManager {
    private static EaglercraftSoundManager sndManager;
	private SoundPool soundPoolSounds = new SoundPool();
	private SoundPool soundPoolStreaming = new SoundPool();
	private SoundPool soundPoolMusic = new SoundPool();
	private GameSettings options;
	private EaglercraftRandom rand = new EaglercraftRandom();
	private int field_583_i = this.rand.nextInt(12000);
    private SoundPoolEntry playingMusic;
    private SoundPoolEntry playingStreaming;

	public void loadSoundSettings(GameSettings var1) {
		this.soundPoolStreaming.field_1657_b = false;
		this.options = var1;
        sndManager = new EaglercraftSoundManager();
	}

    public void registerSounds() {
        InputStream stream = EagRuntime.getResourceStream("/assets/sounds.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String file;
            while ((file = reader.readLine()) != null) {
                this.func_6372_a(file, "/assets/sound/" + file);
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
        if(this.options.musicVolume == 0.0F) {
            sndManager.stopAllStatic();
        } else {
            if(this.playingMusic != null) {
                this.playingMusic.volume = this.options.musicVolume;
                sndManager.updateAllSounds();
            }
		}
	}

	public void closeMinecraft() {
        sndManager.stopAllSounds();
	}

	public void func_6372_a(String var1, String var2) {
        EagRuntime.getRequiredResourceBytes(var2);
		this.soundPoolSounds.addSound(sndManager, var1, var2);
	}

	public void addStreaming(String var1, String var2) {
        EagRuntime.getRequiredResourceBytes(var1);
		this.soundPoolStreaming.addSound(sndManager, var1, var2);
	}

	public void addMusic(String var1, String var2) {
        if (EagRuntime.getPlatformOS() != EnumPlatformOS.IPHONE && this.options.musicVolume != 0.0F) {
            EagRuntime.getRequiredResourceBytes(var2);
        }

		this.soundPoolMusic.addSound(sndManager, var1, var2);
	}

	public void func_4033_c() {
		if(this.options.musicVolume != 0.0F) {
            if(!sndManager.isSoundPlaying(this.playingMusic) && !sndManager.isSoundPlaying(this.playingStreaming) && (this.playingMusic == null || !this.playingMusic.queued)) {
				if(this.field_583_i > 0) {
					--this.field_583_i;
					return;
				}

                this.playingMusic = this.soundPoolMusic.getRandomSound();
                if(this.playingMusic != null) {
					this.field_583_i = this.rand.nextInt(12000) + 12000;
					this.playingMusic.volume = this.options.musicVolume;
	                this.playingMusic.playStatic = true;
                    this.play(this.playingMusic);
				}
			}

		}
	}

    public SoundPoolEntry play(SoundPoolEntry sound) {
        sndManager.playSound(sound);
        return sound;
    }

	public void func_338_a(EntityLiving var1, float var2) {
		if(this.options.soundVolume != 0.0F) {
			if(var1 != null) {
	            sndManager.setListener(var1, var2);
			}
		}
	}

	public void func_331_a(String var1, float var2, float var3, float var4, float var5, float var6) {
		if(this.options.soundVolume != 0.0F) {
            sndManager.stopSound(this.playingStreaming);
			if(var1 != null) {
				SoundPoolEntry var8 = this.soundPoolStreaming.getRandomSoundFromSoundPool(var1);
				if(var8 != null && var5 > 0.0F) {
                    sndManager.stopSound(this.playingMusic);
                    this.playingStreaming = new SoundPoolEntry(var8, var2, var3, var4, 4.0F, 1.0F, 0.5F * this.options.soundVolume);
                    this.play(this.playingStreaming);
				}

			}
		}
	}

	public void playSound(String var1, float var2, float var3, float var4, float var5, float var6) {
		if(this.options.soundVolume != 0.0F) {
			SoundPoolEntry var7 = this.soundPoolSounds.getRandomSoundFromSoundPool(var1);
			if(var7 != null && var5 > 0.0F) {
                this.play(new SoundPoolEntry(var7, var2, var3, var4, var5 * this.options.soundVolume, var6));
			}

		}
	}

	public void func_337_a(String var1, float var2, float var3) {
		if(this.options.soundVolume != 0.0F) {
			SoundPoolEntry var4 = this.soundPoolSounds.getRandomSoundFromSoundPool(var1);
			if(var4 != null) {
                this.play(new SoundPoolEntry(var4, true, 1.0F, 0.25F * this.options.soundVolume));
			}

		}
	}
}