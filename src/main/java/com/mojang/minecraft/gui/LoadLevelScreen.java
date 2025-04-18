package com.mojang.minecraft.gui;

public class LoadLevelScreen extends Screen implements Runnable {
    protected Screen parent;
    private boolean finished = false;
    private boolean loaded = false;
    private String[] levels = null;
    private String status = "";
    protected String title = "Load level";
    boolean fileLoaded = false;
    protected boolean isSaveScreen = false;

    public LoadLevelScreen(Screen screen) {
        this.parent = screen;
    }

    public void run() {
        this.status = "Failed to load levels";
        this.finished = true;
    }

    protected void setLevels(String[] levels) {
        for(int i2 = 0; i2 < 5; ++i2) {
            ((Button)this.buttons.get(i2)).enabled = !levels[i2].equals("-");
            ((Button)this.buttons.get(i2)).msg = levels[i2];
            ((Button)this.buttons.get(i2)).visible = true;
        }

    }

    public void init() {
        (new Thread(this)).start();

        for(int i1 = 0; i1 < 5; ++i1) {
            this.buttons.add(new Button(i1, this.width / 2 - 100, this.height / 6 + i1 * 24, "---"));
            ((Button)this.buttons.get(i1)).visible = false;
            ((Button)this.buttons.get(i1)).enabled = false;
        }

        this.buttons.add(new Button(5, this.width / 2 - 100, this.height / 6 + 120 + 12, "Load file..."));
        this.buttons.add(new Button(6, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
    }

    protected final void buttonClicked(Button button) {
        if(!this.fileLoaded) {
            if(button.enabled) {
                if(this.loaded && button.id < 5) {
                    this.loadLevel(button.id);
                }

                if(this.finished || this.loaded && button.id == 5) {
                    this.fileLoaded = true;
                }

                if(this.finished || this.loaded && button.id == 6) {
                    this.minecraft.setScreen(this.parent);
                }

            }
        }
    }

    protected void loadLevel(String file) {
        this.minecraft.setScreen(this.parent);
    }

    protected void loadLevel(int id) {
        this.minecraft.loadLevel(this.minecraft.user.name, id);
        this.minecraft.setScreen((Screen)null);
        this.minecraft.grabMouse();
    }

    public void render(int xMouse, int yMouse) {
        fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
        drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        if(this.fileLoaded) {
            drawCenteredString(this.font, "Selecting file..", this.width / 2, this.height / 2 - 4, 0xFFFFFF);

            try {
                Thread.sleep(20L);
            } catch (InterruptedException interruptedException3) {
                interruptedException3.printStackTrace();
            }
        } else {
            if(!this.loaded) {
                drawCenteredString(this.font, this.status, this.width / 2, this.height / 2 - 4, 0xFFFFFF);
            }

            super.render(xMouse, yMouse);
        }
    }

    public final void removed() {
        super.removed();

    }

    public final void tick() {
        super.tick();

    }
}