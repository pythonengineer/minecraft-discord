package com.mojang.minecraft.gui;

public class LoadLevelScreen extends Screen implements Runnable {
    private Screen parent;
    private boolean finished = false;
    private boolean loaded = false;
    private String[] levels = null;
    private String status = "";
    protected String title = "Load level";
    private boolean unused = false;

    public LoadLevelScreen(Screen screen) {
        this.parent = screen;
    }

    public void run() {
        this.status = "Failed to load levels";
        this.finished = true;

    }

    protected void setLevels(String[] levelNames) {
        for(int i2 = 0; i2 < 5; ++i2) {
            ((Button)this.buttons.get(i2)).enabled = !levelNames[i2].equals("-");
            ((Button)this.buttons.get(i2)).msg = levelNames[i2];
            ((Button)this.buttons.get(i2)).visible = true;
        }

    }

    public void init() {
        (new Thread(this)).start();

        for(int i1 = 0; i1 < 5; ++i1) {
            this.buttons.add(new Button(i1, this.width / 2 - 100, this.height / 6 + i1 * 24, "---"));
            ((Button)this.buttons.get(i1)).visible = false;
        }

        this.buttons.add(new Button(5, this.width / 2 - 100, this.height / 6 + 120 + 12, "Load file..."));
        this.buttons.add(new Button(6, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
        ((Button)this.buttons.get(5)).visible = false;
    }

    protected final void buttonClicked(Button button) {
        if(button.enabled) {
            if(this.loaded && button.id < 5) {
                this.loadLevel(button.id);
            }

            if(this.finished || this.loaded && button.id == 6) {
                this.minecraft.setScreen(this.parent);
            }

        }
    }

    protected void loadLevel(int id) {
        this.minecraft.loadLevel(this.minecraft.user.name, id);
        this.minecraft.setScreen((Screen)null);
        this.minecraft.grabMouse();
    }

    public final void render(int xMouse, int yMouse) {
        fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
        drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        if(!this.loaded) {
            drawCenteredString(this.font, this.status, this.width / 2, this.height / 2 - 4, 0xFFFFFF);
        }

        super.render(xMouse, yMouse);
    }
}
