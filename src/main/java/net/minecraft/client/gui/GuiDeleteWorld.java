package net.minecraft.client.gui;

import net.minecraft.game.world.World;

public final class GuiDeleteWorld extends GuiSelectWorld {
    public GuiDeleteWorld(GuiScreen guiScreen1) {
        super(guiScreen1);
        this.screenTitle = "Delete world";
    }

    public final void initButtons() {
        this.controlList.add(new GuiButton(6, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
    }

    public final void actionWorld(int worldIndex) {
        String string2;
        if((string2 = getSaveFileName(worldIndex)) != null) {
            this.mc.displayGuiScreen(new GuiYesNo(this, "Are you sure you want to delete this world?", "\'" + string2 + "\' will be lost forever!", worldIndex));
        }

    }

    public final void deleteWorld(boolean shouldDelete, int worldIndex) {
        if(shouldDelete) {
            World.deleteWorld(this.getSaveFileName(worldIndex));
        }

        this.mc.displayGuiScreen(this.currentScreen);
    }
}