package net.minecraft.client.gui.container;

import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.game.world.block.tileentity.TileEntitySign;

public final class GuiEditSign extends GuiScreen {
    private String screenTitle = "Edit sign message:";
    private TileEntitySign entitySign;
    private int updateCounter;
    private int editLine = 0;

    public GuiEditSign(TileEntitySign signTileEntity) {
        this.entitySign = signTileEntity;
    }

    public final void initGui() {
        this.controlList.clear();
        Keyboard.enableRepeatEvents(true);
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Done"));
    }

    public final void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public final void updateScreen() {
        ++this.updateCounter;
    }

    protected final void actionPerformed(GuiButton button) {
        if(button.enabled) {
            if(button.id == 0) {
                this.entitySign.onInventoryChanged();
                this.mc.setGuiScreen((GuiScreen)null);
            }

        }
    }

    protected final void keyTyped(char typedChar, int keyCode) {
        if(keyCode == 200) {
            this.editLine = this.editLine - 1 & 3;
        }

        if(keyCode == 208 || keyCode == 28) {
            this.editLine = this.editLine + 1 & 3;
        }

        if(keyCode == 14 && this.entitySign.signText[this.editLine].length() > 0) {
            this.entitySign.signText[this.editLine] = this.entitySign.signText[this.editLine].substring(0, this.entitySign.signText[this.editLine].length() - 1);
        }

        if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_\'*!\"#%/()=+?[]{}<>".indexOf(typedChar) >= 0 && this.entitySign.signText[this.editLine].length() < 15) {
            this.entitySign.signText[this.editLine] = this.entitySign.signText[this.editLine] + typedChar;
        }

    }

    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 40, 0xFFFFFF);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(this.width / 2), (float)(this.height / 2), 50.0F);
        GL11.glScalef(-60.0F, -60.0F, -60.0F);
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        if(this.updateCounter / 6 % 2 == 0) {
            this.entitySign.lineBeingEdited = this.editLine;
        }

        GL11.glRotatef((float)(this.entitySign.getBlockMetadata() * 360) / 16.0F, 0.0F, 1.0F, 0.0F);
        TileEntityRenderer.instance.renderTileEntityAt(this.entitySign, -0.5D, -0.75D, -0.5D, 0.0F);
        this.entitySign.lineBeingEdited = -1;
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}