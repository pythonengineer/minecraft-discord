package net.minecraft.client.render;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.game.entity.player.ItemStack;
import net.minecraft.game.level.block.Block;

public final class ItemRenderer {
    private Minecraft a;
    private ItemStack itemToRender = null;
    private float equippedProgress = 0.0F;
    private float prevEquippedProgress = 0.0F;
    private int swingProgress = 0;
    private boolean itemSwingState = false;
    private RenderBlocks renderBlocksInstance = new RenderBlocks(Tessellator.instance);

    public ItemRenderer(Minecraft var1) {
        this.a = var1;
    }

    public final void renderItemInFirstPerson(float var1) {
        float var2 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * var1;
        EntityPlayerSP var3 = this.a.thePlayer;
        GL11.glPushMatrix();
        GL11.glRotatef(var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * var1, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * var1, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        float var4;
        float var5;
        float var6;
        if(this.itemSwingState) {
            var4 = ((float)this.swingProgress + var1) / 8.0F;
            var5 = MathHelper.sin(var4 * (float)Math.PI);
            var6 = MathHelper.sin(MathHelper.sqrt_float(var4) * (float)Math.PI);
            GL11.glTranslatef(-var6 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var4) * (float)Math.PI * 2.0F) * 0.2F, -var5 * 0.2F);
        }

        GL11.glTranslatef(0.56F, -0.52F - (1.0F - var2) * 0.6F, -0.71999997F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_NORMALIZE);
        if(this.itemSwingState) {
            var4 = ((float)this.swingProgress + var1) / 8.0F;
            var5 = MathHelper.sin(var4 * var4 * (float)Math.PI);
            var6 = MathHelper.sin(MathHelper.sqrt_float(var4) * (float)Math.PI);
            GL11.glRotatef(var6 * 80.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-var5 * 20.0F, 1.0F, 0.0F, 0.0F);
        }

        var4 = this.a.theWorld.getBlockLightValue((int)var3.posX, (int)var3.posY, (int)var3.posZ);
        GL11.glColor4f(var4, var4, var4, 1.0F);
        if(this.itemToRender != null) {
            GL11.glScalef(0.4F, 0.4F, 0.4F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.a.renderEngine.getTexture("/terrain.png"));
            ItemStack var7 = this.itemToRender;
            if(var7.itemID > 0) {
                var7 = this.itemToRender;
                this.renderBlocksInstance.renderBlockOnInventory(Block.blocksList[var7.itemID]);
            } else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.a.renderEngine.getTexture("/gui/items.png"));
                GL11.glDisable(GL11.GL_LIGHTING);
                Tessellator var9 = Tessellator.instance;
                var1 = (float)(this.itemToRender.iconIndex % 16 << 4) / 256.0F;
                var2 = (float)((this.itemToRender.iconIndex % 16 << 4) + 16) / 256.0F;
                float var8 = (float)(this.itemToRender.iconIndex / 16 << 4) / 256.0F;
                var4 = (float)((this.itemToRender.iconIndex / 16 << 4) + 16) / 256.0F;
                var9.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
                var9.addVertexWithUV(-0.4F, -0.2F, -0.4F, var1, var4);
                var9.addVertexWithUV(0.29999998F, -0.2F, 0.29999998F, var2, var4);
                var9.addVertexWithUV(0.29999998F, 0.8F, 0.29999998F, var2, var8);
                var9.addVertexWithUV(-0.4F, 0.8F, -0.4F, var1, var8);
                var9.draw();
                GL11.glEnable(GL11.GL_LIGHTING);
            }
        } else {
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.0F, 0.2F, 0.0F);
            GL11.glRotatef(-120.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
        }

        GL11.glDisable(GL11.GL_NORMALIZE);
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
    }

    public final void updateEquippedItem() {
        this.prevEquippedProgress = this.equippedProgress;
        if(this.itemSwingState) {
            ++this.swingProgress;
            if(this.swingProgress == 8) {
                this.swingProgress = 0;
                this.itemSwingState = false;
            }
        }

        EntityPlayerSP var1 = this.a.thePlayer;
        ItemStack var3 = var1.inventory.getCurrentItem();
        float var2 = var3 == this.itemToRender ? 1.0F : 0.0F;
        var2 -= this.equippedProgress;
        if(var2 < -0.4F) {
            var2 = -0.4F;
        }

        if(var2 > 0.4F) {
            var2 = 0.4F;
        }

        this.equippedProgress += var2;
        if(this.equippedProgress < 0.1F) {
            this.itemToRender = var3;
        }

    }

    public final void equipAnimationSpeed() {
        this.equippedProgress = 0.0F;
    }

    public final void equippedItemRender() {
        this.swingProgress = -1;
        this.itemSwingState = true;
    }
}
