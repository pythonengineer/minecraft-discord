package net.minecraft.client;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSaveLevel;
import net.minecraft.client.gui.GuiScreen;

public final class s extends GuiScreen {
    public final void initGui() {
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96, "Save level.."));
    }

    protected final void keyTyped(char var1, int var2) {
    }

    protected final void actionPerformed(GuiButton var1) {
        if(this.mc.session != null && var1.id == 0) {
            this.mc.displayGuiScreen(new GuiSaveLevel(this));
        }

    }

    public final void drawScreen(int var1, int var2, float var3) {
        drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        drawCenteredString(this.fontRenderer, "Session conflict!", this.width / 2 / 2, 30, 16777215);
        GL11.glPopMatrix();
        drawCenteredString(this.fontRenderer, "Did you log in from another computer?", this.width / 2, 90, 16777215);
        super.drawScreen(var1, var2, var3);
    }

    public final boolean doesGuiPauseGame() {
        return false;
    }
}
