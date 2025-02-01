package com.mojang.minecraft.gui;

public final class PauseScreen extends Screen {
    public final void init() {
        this.buttons.clear();
        this.buttons.add(new Button(0, this.y / 2 - 100, this.w / 3, 200, 20, "Generate new level..."));
        this.buttons.add(new Button(1, this.y / 2 - 100, this.w / 3 + 32, 200, 20, "Save level.."));
        this.buttons.add(new Button(2, this.y / 2 - 100, this.w / 3 + 64, 200, 20, "Load level.."));
        this.buttons.add(new Button(3, this.y / 2 - 100, this.w / 3 + 96, 200, 20, "Back to game"));
        if(this.minecraft.user == null) {
            //((Button)this.buttons.get(1)).enabled = false;
            //((Button)this.buttons.get(2)).enabled = false;
        }

        if(this.minecraft.connectionManager != null) {
            ((Button)this.buttons.get(0)).enabled = false;
            ((Button)this.buttons.get(1)).enabled = false;
            ((Button)this.buttons.get(2)).enabled = false;
        }

    }

    protected final void buttonClicked(Button button1) {
        if(button1.id == 0) {
            this.minecraft.setScreen(new NewLevelScreen(this));
        }

        //if(this.minecraft.user != null) {
            if(button1.id == 1) {
                this.minecraft.saveLevel();//this.minecraft.setScreen(new SaveLevelScreen(this));
            }

            if(button1.id == 2) {
                this.minecraft.loadLevel("", 0);//this.minecraft.setScreen(new LoadLevelScreen(this));
            }
        //}

        if(button1.id == 3) {
            this.minecraft.setScreen((Screen)null);
            this.minecraft.grabMouse();
        }

    }

    public final void render(int i1, int i2) {
        fillGradient(0, 0, this.y, this.w, 1610941696, -1607454624);
        this.drawCenteredString("Game menu", this.y / 2, 40, 0xFFFFFF);
        super.render(i1, i2);
    }
}
