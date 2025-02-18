package net.lax1dude.eaglercraft;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.sound.Sound;

import net.lax1dude.eaglercraft.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.internal.IAudioCacheLoader;
import net.lax1dude.eaglercraft.internal.IAudioHandle;
import net.lax1dude.eaglercraft.internal.IAudioResource;
import net.lax1dude.eaglercraft.internal.PlatformAudio;
import net.lax1dude.eaglercraft.log4j.LogManager;
import net.lax1dude.eaglercraft.log4j.Logger;
import net.lax1dude.eaglercraft.util.MathHelper;

/**
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class EaglercraftSoundManager {

    protected class ActiveSoundEvent {

        protected final EaglercraftSoundManager manager;

        protected final Sound soundInstance;
        protected IAudioHandle soundHandle;

        protected float activeX;
        protected float activeY;
        protected float activeZ;

        protected float activePitch;
        protected float activeGain;

        protected int repeatCounter = 0;
        protected boolean paused = false;

        protected ActiveSoundEvent(EaglercraftSoundManager manager, Sound soundInstance,
                IAudioHandle soundHandle) {
            this.manager = manager;
            this.soundInstance = soundInstance;
            this.soundHandle = soundHandle;
            if (soundInstance.pos != null) {
                this.activeX = soundInstance.pos.x;
                this.activeY = soundInstance.pos.y;
                this.activeZ = soundInstance.pos.z;
            }
            this.activePitch = soundInstance.pitch;
            this.activeGain = soundInstance.volume;
        }

        protected void updateLocation() {
            if (soundInstance.pos == null) {
                return;
            }
            float x = soundInstance.pos.x;
            float y = soundInstance.pos.y;
            float z = soundInstance.pos.z;
            float pitch = soundInstance.pitch;
            float gain = soundInstance.volume;
            if (x != activeX || y != activeY || z != activeZ) {
                soundHandle.move(x, y, z);
                activeX = x;
                activeY = y;
                activeZ = z;
            }
            if (pitch != activePitch) {
                soundHandle.pitch(EaglercraftSoundManager.this.getNormalizedPitch(soundInstance));
                activePitch = pitch;
            }
            if (gain != activeGain) {
                soundHandle.gain(
                        EaglercraftSoundManager.this.getNormalizedVolume(soundInstance));
                activeGain = gain;
            }
        }

    }

    protected static class WaitingSoundEvent {

        protected final Sound playSound;
        protected int playTicks;
        protected boolean paused = false;

        private WaitingSoundEvent(Sound playSound, int playTicks) {
            this.playSound = playSound;
            this.playTicks = playTicks;
        }

    }

    private static final Logger logger = LogManager.getLogger("SoundManager");

    private final List<ActiveSoundEvent> activeSounds;
    private final List<WaitingSoundEvent> queuedSounds;

    public EaglercraftSoundManager() {
        activeSounds = new LinkedList<>();
        queuedSounds = new LinkedList<>();
    }

    public void unloadSoundSystem() {
        // handled by PlatformApplication
    }

    public void reloadSoundSystem() {
        PlatformAudio.flushAudioCache();
    }

    public void stopAllSounds() {
        Iterator<ActiveSoundEvent> soundItr = activeSounds.iterator();
        while (soundItr.hasNext()) {
            ActiveSoundEvent evt = soundItr.next();
            if (!evt.soundHandle.shouldFree()) {
                evt.soundHandle.end();
            }
        }
        activeSounds.clear();
    }

    public void pauseAllSounds() {
        Iterator<ActiveSoundEvent> soundItr = activeSounds.iterator();
        while (soundItr.hasNext()) {
            ActiveSoundEvent evt = soundItr.next();
            if (!evt.soundHandle.shouldFree()) {
                evt.soundHandle.pause(true);
                evt.paused = true;
            }
        }
        Iterator<WaitingSoundEvent> soundItr2 = queuedSounds.iterator();
        while (soundItr2.hasNext()) {
            soundItr2.next().paused = true;
        }
    }

    public void resumeAllSounds() {
        Iterator<ActiveSoundEvent> soundItr = activeSounds.iterator();
        while (soundItr.hasNext()) {
            ActiveSoundEvent evt = soundItr.next();
            if (!evt.soundHandle.shouldFree()) {
                evt.soundHandle.pause(false);
                evt.paused = false;
            }
        }
        Iterator<WaitingSoundEvent> soundItr2 = queuedSounds.iterator();
        while (soundItr2.hasNext()) {
            soundItr2.next().paused = false;
        }
    }

    public void updateAllSounds() {
        Iterator<ActiveSoundEvent> soundItr = activeSounds.iterator();
        while (soundItr.hasNext()) {
            ActiveSoundEvent evt = soundItr.next();
            boolean persist = false;
            if (evt.soundHandle.shouldFree()) {
                if (!persist) {
                    soundItr.remove();
                }
            } else {
                evt.updateLocation();
            }
        }
        Iterator<WaitingSoundEvent> soundItr2 = queuedSounds.iterator();
        while (soundItr2.hasNext()) {
            WaitingSoundEvent evt = soundItr2.next();
            if (!evt.paused && --evt.playTicks <= 0) {
                soundItr2.remove();
                playSound(evt.playSound);
            }
        }
        PlatformAudio.clearAudioCache();
    }

    public boolean isSoundPlaying(Sound sound) {
        Iterator<ActiveSoundEvent> soundItr = activeSounds.iterator();
        while (soundItr.hasNext()) {
            ActiveSoundEvent evt = soundItr.next();
            if (evt.soundInstance == sound) {
                return !evt.soundHandle.shouldFree();
            }
        }
        return false;
    }

    public void stopSound(Sound sound) {
        Iterator<ActiveSoundEvent> soundItr = activeSounds.iterator();
        while (soundItr.hasNext()) {
            ActiveSoundEvent evt = soundItr.next();
            if (evt.soundInstance == sound) {
                if (!evt.soundHandle.shouldFree()) {
                    evt.soundHandle.end();
                    soundItr.remove();
                    return;
                }
            }
        }
        Iterator<WaitingSoundEvent> soundItr2 = queuedSounds.iterator();
        while (soundItr2.hasNext()) {
            if (soundItr2.next().playSound == sound) {
                soundItr2.remove();
            }
        }
    }

    public void playSound(Sound sound) {
        if (!PlatformAudio.available()) {
            return;
        }
        if (sound != null) {
            IAudioResource trk;
            if (EagRuntime.getPlatformType() != EnumPlatformType.DESKTOP) {
                trk = PlatformAudio.loadAudioDataNew(sound.url, sound.pos != null,
                        EagRuntime.browserResourcePackLoader);
            } else {
                trk = PlatformAudio.loadAudioData(sound.url, sound.pos != null);
            }
            if (trk == null) {
                logger.warn("Unable to play unknown soundEvent(3): {}", sound.url);
            } else {

                ActiveSoundEvent newSound = new ActiveSoundEvent(this, sound,
                        null);

                float pitch = getNormalizedPitch(sound);
                float attenuatedGain = getNormalizedVolume(sound);
                boolean repeat = false;

                if (sound.pos != null) {
                    newSound.soundHandle = PlatformAudio.beginPlayback(trk, newSound.activeX, newSound.activeY,
                            newSound.activeZ, attenuatedGain, pitch, repeat);
                } else {
                    newSound.soundHandle = PlatformAudio.beginPlaybackStatic(trk, attenuatedGain, pitch,
                            repeat);
                }

                if (newSound.soundHandle == null) {
                    logger.error("Unable to play soundEvent(4): {}", sound.url);
                } else {
                    activeSounds.add(newSound);
                }
            }
        }
    }

    public void playDelayedSound(Sound sound, int delay) {
        queuedSounds.add(new WaitingSoundEvent(sound, delay));
    }

    private float getNormalizedVolume(Sound sound) {
        return (float)MathHelper.clamp_double((double)sound.volume, 0.0D, 1.0D);
    }

    private float getNormalizedPitch(Sound sound) {
        return MathHelper.clamp_float(sound.pitch, 0.5f, 2.0f);
    }

    public void setListener(Player player, float partialTicks) {
        if (!PlatformAudio.available()) {
            return;
        }
        if (player != null) {
            try {
                float f = player.xRotO + (player.xRot - player.xRotO) * partialTicks;
                float f1 = player.yRotO + (player.yRot - player.yRotO) * partialTicks;
                double d0 = player.xo + (player.x - player.xo) * (double)partialTicks;
                double d1 = player.yo + (player.y - player.yo) * (double)partialTicks
                        + (double)player.heightOffset;
                double d2 = player.zo + (player.z - player.zo) * (double)partialTicks;
                PlatformAudio.setListener((float)d0, (float)d1, (float)d2, f, f1);
            } catch (Throwable t) {
                // eaglercraft 1.5.2 had Infinity/NaN crashes for this function which
                // couldn't be resolved via if statement checks in the above variables
            }
        }
    }

}
