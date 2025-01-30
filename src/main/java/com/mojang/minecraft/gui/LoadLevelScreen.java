package com.mojang.minecraft.gui;

public class LoadLevelScreen extends Screen implements Runnable {
    private Screen parent;
    private boolean finished = false;
    private boolean loaded = false;
    private String[] levels = null;
    private String status = "";
    protected String title = "Load level";

    public LoadLevelScreen(Screen screen1) {
        this.parent = screen1;
    }

    public void run() {
        this.status = "Failed to load levels";
        this.finished = true;

    }

    protected void setLevels(String[] string1) {
        for(int i2 = 0; i2 < 5; ++i2) {
            ((Button)this.buttons.get(i2)).enabled = !string1[i2].equals("-");
            ((Button)this.buttons.get(i2)).msg = string1[i2];
            ((Button)this.buttons.get(i2)).visible = true;
        }

    }

    public final void init() {
        (new Thread(this)).start();

        for(int i1 = 0; i1 < 5; ++i1) {
            this.buttons.add(new Button(i1, this.y / 2 - 100, this.w / 4 + i1 * 24, 200, 20, "---"));
            ((Button)this.buttons.get(i1)).visible = false;
        }

        this.buttons.add(new Button(5, this.y / 2 - 100, this.w / 4 + 144, 200, 20, "Cancel"));
    }

    protected final void buttonClicked(Button button1) {
        if(button1.enabled) {
            if(this.loaded && button1.id < 5) {
                this.loadLevel(button1.id);
            }

            if(this.finished || this.loaded && button1.id == 5) {
                this.minecraft.setScreen(this.parent);
            }

        }
    }

    protected void loadLevel(int i1) {
        this.minecraft.loadLevel(this.minecraft.user.name, i1);
        this.minecraft.setScreen((Screen)null);
        this.minecraft.grabMouse();
    }

    public final void render(int i1, int i2) {
        fillGradient(0, 0, this.y, this.w, 1610941696, -1607454624);
        this.drawCenteredString(this.title, this.y / 2, 40, 0xFFFFFF);
        if(!this.loaded) {
            this.drawCenteredString(this.status, this.y / 2, this.w / 2 - 4, 0xFFFFFF);
        }

        super.render(i1, i2);
    }
}
