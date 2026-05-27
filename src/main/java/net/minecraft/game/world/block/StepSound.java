package net.minecraft.game.world.block;

public class StepSound {
    public final String stepSoundName;
    public final float stepSoundVolume;
    public final float stepSoundPitch;

    public StepSound(String soundName, float volume, float pitch) {
        this.stepSoundName = soundName;
        this.stepSoundVolume = volume;
        this.stepSoundPitch = pitch;
    }

    public float getVolume() {
        return this.stepSoundVolume;
    }

    public float getPitch() {
        return this.stepSoundPitch;
    }

    public String getBreakSound() {
        return "step." + this.stepSoundName;
    }

    public String getStepSound() {
        return "step." + this.stepSoundName;
    }
}
