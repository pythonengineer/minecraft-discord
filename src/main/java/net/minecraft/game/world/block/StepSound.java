package net.minecraft.game.world.block;

public class StepSound {
    private String stepSoundName;
    public final float stepSoundVolume;
    public final float stepSoundPitch;

    public StepSound(String soundName, float volume, float pitch) {
        this.stepSoundName = soundName;
        this.stepSoundVolume = volume;
        this.stepSoundPitch = pitch;
    }

    public String getBreakSound() {
        return "step." + this.stepSoundName;
    }

    public final String getStepSound() {
        return "step." + this.stepSoundName;
    }
}