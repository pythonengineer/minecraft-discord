package net.minecraft.client.render.tileentity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.SignModel;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.block.tileentity.TileEntitySign;

public class TileEntitySignRenderer extends TileEntitySpecialRenderer {
    private SignModel modelSign = new SignModel();

    public void renderTileEntityMobSpawner(TileEntitySign sign, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y + 0.75F, (float)z + 0.5F);
        GL11.glRotatef(-((float)(sign.getBlockMetadata() * 360) / 16.0F), 0.0F, 1.0F, 0.0F);
        this.bindTextureByName("/item/sign.png");
        GL11.glPushMatrix();
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        this.modelSign.renderSign();
        GL11.glPopMatrix();
        GL11.glTranslatef(0.0F, 0.5F, 0.09F);
        GL11.glScalef(0.016666668F, -0.016666668F, 0.016666668F);
        GL11.glNormal3f(0.0F, 0.0F, -0.016666668F);
        FontRenderer fontRenderer17 = this.getFontRenderer();

        for(int i19 = 0; i19 < sign.signText.length; ++i19) {
            String string20 = sign.signText[i19];
            if(i19 == sign.lineBeingEdited) {
                string20 = "> " + string20 + " <";
                fontRenderer17.drawString(string20, -fontRenderer17.getStringWidth(string20) / 2, i19 * 10 - sign.signText.length * 5, 0);
            } else {
                fontRenderer17.drawString(string20, -fontRenderer17.getStringWidth(string20) / 2, i19 * 10 - sign.signText.length * 5, 0);
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public void renderTileEntityMobSpawner(TileEntity tileEntity1, double d2, double d4, double d6, float f8) {
        this.renderTileEntityMobSpawner((TileEntitySign)tileEntity1, d2, d4, d6, f8);
    }
}
