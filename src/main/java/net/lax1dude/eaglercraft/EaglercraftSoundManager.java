package net.lax1dude.eaglercraft;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.internal.IAudioHandle;
import net.lax1dude.eaglercraft.internal.IAudioResource;
import net.lax1dude.eaglercraft.internal.PlatformAudio;
import net.lax1dude.eaglercraft.log4j.LogManager;
import net.lax1dude.eaglercraft.log4j.Logger;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.sound.SoundPoolEntry;
import net.minecraft.game.entity.EntityLiving;

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

        protected final SoundPoolEntry soundInstance;
        protected IAudioHandle soundHandle;

        protected float activeX;
        protected float activeY;
        protected float activeZ;

        protected float activePitch;
        protected float activeGain;

        protected int repeatCounter = 0;
        protected boolean paused = false;

        protected ActiveSoundEvent(EaglercraftSoundManager manager, SoundPoolEntry soundInstance,
                IAudioHandle soundHandle) {
            this.manager = manager;
            this.soundInstance = soundInstance;
            this.soundHandle = soundHandle;
            if (!soundInstance.playStatic) {
                this.activeX = soundInstance.x;
                this.activeY = soundInstance.y;
                this.activeZ = soundInstance.z;
            }
            this.activePitch = soundInstance.pitch;
            this.activeGain = soundInstance.volume;
        }

        protected void updateLocation() {
            if (soundInstance.playStatic) {
                return;
            }
            float x = soundInstance.x;
            float y = soundInstance.y;
            float z = soundInstance.z;
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

        protected final SoundPoolEntry playSound;
        protected int playTicks;
        protected boolean paused = false;

        private WaitingSoundEvent(SoundPoolEntry playSound, int playTicks) {
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

    public void stopAllStatic() {
        Iterator<ActiveSoundEvent> soundItr = activeSounds.iterator();
        while (soundItr.hasNext()) {
            ActiveSoundEvent evt = soundItr.next();
            if (evt.soundInstance.playStatic && !evt.soundHandle.shouldFree()) {
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

    public boolean isSoundPlaying(SoundPoolEntry sound) {
        Iterator<ActiveSoundEvent> soundItr = activeSounds.iterator();
        while (soundItr.hasNext()) {
            ActiveSoundEvent evt = soundItr.next();
            if (evt.soundInstance == sound) {
                return !evt.soundHandle.shouldFree();
            }
        }
        return false;
    }

    public void stopSound(SoundPoolEntry sound) {
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

    public void playSound(SoundPoolEntry sound) {
        if (!PlatformAudio.available()) {
            return;
        }
        if (sound != null) {
            sound.queued = true;
            IAudioResource trk = null;
            if (EagRuntime.getPlatformType() != EnumPlatformType.DESKTOP) {
                PlatformAudio.loadAudioDataNew(sound, !sound.playStatic,
                  EagRuntime.browserResourcePackLoader);
            } else {
                trk = PlatformAudio.loadAudioData(sound.soundUrl, !sound.playStatic);
            }
            if (!PlatformAudio.isAsyncSupported()) {
                this.playSoundLoaded(sound, trk);
            }
        }
    }

    public void playSoundLoaded(SoundPoolEntry sound, IAudioResource trk) {
        if (trk == null) {
            logger.warn("Unable to play unknown soundEvent(3): {}", sound.soundUrl);
        } else {

            ActiveSoundEvent newSound = new ActiveSoundEvent(this, sound,
                    null);

            float pitch = getNormalizedPitch(sound);
            float attenuatedGain = getNormalizedVolume(sound);
            boolean repeat = false;

            if (!sound.playStatic) {
                newSound.soundHandle = PlatformAudio.beginPlayback(trk, newSound.activeX, newSound.activeY,
                        newSound.activeZ, attenuatedGain, pitch, repeat);
            } else {
                newSound.soundHandle = PlatformAudio.beginPlaybackStatic(trk, attenuatedGain, pitch,
                        repeat);
            }

            if (newSound.soundHandle == null) {
                logger.error("Unable to play soundEvent(4): {}", sound.soundUrl);
            } else {
                activeSounds.add(newSound);
                sound.queued = false;
            }
        }
    }

    public void playDelayedSound(SoundPoolEntry sound, int delay) {
        queuedSounds.add(new WaitingSoundEvent(sound, delay));
    }

    private float getNormalizedVolume(SoundPoolEntry sound) {
        return (float)MathHelper.clamp_double((double)sound.volume, 0.0D, 1.0D);
    }

    private float getNormalizedPitch(SoundPoolEntry sound) {
        return MathHelper.clamp_float(sound.pitch, 0.5f, 2.0f);
    }

    public void setListener(EntityLiving listener, float partialTicks) {
        if (!PlatformAudio.available()) {
            return;
        }
        if (listener != null) {
            try {
                float f = listener.prevRotationPitch + (listener.rotationPitch - listener.prevRotationPitch) * partialTicks;
                float f1 = listener.prevRotationYaw + (listener.rotationYaw - listener.prevRotationYaw) * partialTicks;
                double d0 = listener.prevPosX + (listener.posX - listener.prevPosX) * (double)partialTicks;
                double d1 = listener.prevPosY + (listener.posY - listener.prevPosY) * (double)partialTicks
                    + (double)listener.yOffset;
                double d2 = listener.prevPosZ + (listener.posZ - listener.prevPosZ) * (double)partialTicks;
                PlatformAudio.setListener((float)d0, (float)d1, (float)d2, f, f1);
            } catch (Throwable t) {
                // eaglercraft 1.5.2 had Infinity/NaN crashes for this function which
                // couldn't be resolved via if statement checks in the above variables
            }
        }
    }

}