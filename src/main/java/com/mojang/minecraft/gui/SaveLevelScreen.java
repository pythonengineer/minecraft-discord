package com.mojang.minecraft.gui;

public final class SaveLevelScreen extends LoadLevelScreen {
    public SaveLevelScreen(Screen screen1) {
        super(screen1);
        this.title = "Save level";
    }

    protected final void setLevels(String[] levelNames) {
        for(int i2 = 0; i2 < 5; ++i2) {
            ((Button)this.buttons.get(i2)).msg = levelNames[i2];
            ((Button)this.buttons.get(i2)).visible = true;
        }

    }

    protected final void loadLevel(int id) {
        this.minecraft.setScreen(new NameLevelScreen(this, ((Button)this.buttons.get(id)).msg, id));
    }
}