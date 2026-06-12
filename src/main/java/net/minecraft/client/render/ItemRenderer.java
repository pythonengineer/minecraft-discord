package net.minecraft.client.render;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.client.render.entity.RenderPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

public class ItemRenderer {
	private Minecraft mc;
	private ItemStack itemToRender = null;
	private float equippedProgress = 0.0F;
	private float prevEquippedProgress = 0.0F;
	private RenderBlocks renderBlocksInstance = new RenderBlocks();

	public ItemRenderer(Minecraft mc) {
		this.mc = mc;
	}

    public void renderItem(ItemStack itemStack) {
        GL11.glPushMatrix();
        if(itemStack.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemStack.itemID].getRenderType())) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
            this.renderBlocksInstance.renderBlockOnInventory(Block.blocksList[itemStack.itemID]);
        } else {
            if(itemStack.itemID < 256) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
            } else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/items.png"));
            }

            Tessellator tessellator2 = Tessellator.instance;
            float f3 = (float)(itemStack.getIconIndex() % 16 * 16 + 0) / 256.0F;
            float f4 = (float)(itemStack.getIconIndex() % 16 * 16 + 16) / 256.0F;
            float f5 = (float)(itemStack.getIconIndex() / 16 * 16 + 0) / 256.0F;
            float f6 = (float)(itemStack.getIconIndex() / 16 * 16 + 16) / 256.0F;
            float f7 = 1.0F;
            float f8 = 0.0F;
            float f9 = 0.3F;
            GL11.glEnable(GL11.GL_RESCALE_NORMAL);
            GL11.glTranslatef(-f8, -f9, 0.0F);
            float f10 = 1.5F;
            GL11.glScalef(f10, f10, f10);
            GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
            float f11 = 0.0625F;
            tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
            tessellator2.setNormal(0.0F, 0.0F, 1.0F);
            tessellator2.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)f4, (double)f6);
            tessellator2.addVertexWithUV((double)f7, 0.0D, 0.0D, (double)f3, (double)f6);
            tessellator2.addVertexWithUV((double)f7, 1.0D, 0.0D, (double)f3, (double)f5);
            tessellator2.addVertexWithUV(0.0D, 1.0D, 0.0D, (double)f4, (double)f5);
            tessellator2.draw();
            tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
            tessellator2.setNormal(0.0F, 0.0F, -1.0F);
            tessellator2.addVertexWithUV(0.0D, 1.0D, (double)(0.0F - f11), (double)f4, (double)f5);
            tessellator2.addVertexWithUV((double)f7, 1.0D, (double)(0.0F - f11), (double)f3, (double)f5);
            tessellator2.addVertexWithUV((double)f7, 0.0D, (double)(0.0F - f11), (double)f3, (double)f6);
            tessellator2.addVertexWithUV(0.0D, 0.0D, (double)(0.0F - f11), (double)f4, (double)f6);
            tessellator2.draw();
            tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
            tessellator2.setNormal(-1.0F, 0.0F, 0.0F);

            int i12;
            float f13;
            float f14;
            float f15;
            for(i12 = 0; i12 < 16; ++i12) {
                f13 = (float)i12 / 16.0F;
                f14 = f4 + (f3 - f4) * f13 - 0.001953125F;
                f15 = f7 * f13;
                tessellator2.addVertexWithUV((double)f15, 0.0D, (double)(0.0F - f11), (double)f14, (double)f6);
                tessellator2.addVertexWithUV((double)f15, 0.0D, 0.0D, (double)f14, (double)f6);
                tessellator2.addVertexWithUV((double)f15, 1.0D, 0.0D, (double)f14, (double)f5);
                tessellator2.addVertexWithUV((double)f15, 1.0D, (double)(0.0F - f11), (double)f14, (double)f5);
            }

            tessellator2.draw();
            tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
            tessellator2.setNormal(1.0F, 0.0F, 0.0F);

            for(i12 = 0; i12 < 16; ++i12) {
                f13 = (float)i12 / 16.0F;
                f14 = f4 + (f3 - f4) * f13 - 0.001953125F;
                f15 = f7 * f13 + 0.0625F;
                tessellator2.addVertexWithUV((double)f15, 1.0D, (double)(0.0F - f11), (double)f14, (double)f5);
                tessellator2.addVertexWithUV((double)f15, 1.0D, 0.0D, (double)f14, (double)f5);
                tessellator2.addVertexWithUV((double)f15, 0.0D, 0.0D, (double)f14, (double)f6);
                tessellator2.addVertexWithUV((double)f15, 0.0D, (double)(0.0F - f11), (double)f14, (double)f6);
            }

            tessellator2.draw();
            tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
            tessellator2.setNormal(0.0F, 1.0F, 0.0F);

            for(i12 = 0; i12 < 16; ++i12) {
                f13 = (float)i12 / 16.0F;
                f14 = f6 + (f5 - f6) * f13 - 0.001953125F;
                f15 = f7 * f13 + 0.0625F;
                tessellator2.addVertexWithUV(0.0D, (double)f15, 0.0D, (double)f4, (double)f14);
                tessellator2.addVertexWithUV((double)f7, (double)f15, 0.0D, (double)f3, (double)f14);
                tessellator2.addVertexWithUV((double)f7, (double)f15, (double)(0.0F - f11), (double)f3, (double)f14);
                tessellator2.addVertexWithUV(0.0D, (double)f15, (double)(0.0F - f11), (double)f4, (double)f14);
            }

            tessellator2.draw();
            tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
            tessellator2.setNormal(0.0F, -1.0F, 0.0F);

            for(i12 = 0; i12 < 16; ++i12) {
                f13 = (float)i12 / 16.0F;
                f14 = f6 + (f5 - f6) * f13 - 0.001953125F;
                f15 = f7 * f13;
                tessellator2.addVertexWithUV((double)f7, (double)f15, 0.0D, (double)f3, (double)f14);
                tessellator2.addVertexWithUV(0.0D, (double)f15, 0.0D, (double)f4, (double)f14);
                tessellator2.addVertexWithUV(0.0D, (double)f15, (double)(0.0F - f11), (double)f4, (double)f14);
                tessellator2.addVertexWithUV((double)f7, (double)f15, (double)(0.0F - f11), (double)f3, (double)f14);
            }

            tessellator2.draw();
            GL11.glDisable(GL11.GL_RESCALE_NORMAL);
        }

        GL11.glPopMatrix();
    }

    public void renderItemInFirstPerson(float renderPartialTick) {
        float f2 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * renderPartialTick;
        GL11.glPushMatrix();
        GL11.glRotatef(this.mc.thePlayer.prevRotationPitch + (this.mc.thePlayer.rotationPitch - this.mc.thePlayer.prevRotationPitch) * renderPartialTick, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(this.mc.thePlayer.prevRotationYaw + (this.mc.thePlayer.rotationYaw - this.mc.thePlayer.prevRotationYaw) * renderPartialTick, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        float f9 = this.mc.theWorld.getBrightness(MathHelper.floor_double(this.mc.thePlayer.posX), MathHelper.floor_double(this.mc.thePlayer.posY), MathHelper.floor_double(this.mc.thePlayer.posZ));
        GL11.glColor4f(f9, f9, f9, 1.0F);
        float f4;
        float f5;
        if(this.itemToRender != null) {
            GL11.glPushMatrix();
            f9 = this.mc.thePlayer.getSwingProgress(renderPartialTick);
            f4 = MathHelper.sin(f9 * (float)Math.PI);
            f5 = MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI);
            GL11.glTranslatef(-f5 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI * 2.0F) * 0.2F, -f4 * 0.2F);
            GL11.glTranslatef(0.56F, -0.52F - (1.0F - f2) * 0.6F, -0.71999997F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(GL11.GL_RESCALE_NORMAL);
            f9 = this.mc.thePlayer.getSwingProgress(renderPartialTick);
            f4 = MathHelper.sin(f9 * f9 * (float)Math.PI);
            f5 = MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI);
            GL11.glRotatef(-f4 * 20.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f5 * 20.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f5 * 80.0F, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(0.4F, 0.4F, 0.4F);
            this.renderItem(this.itemToRender);
            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            f9 = this.mc.thePlayer.getSwingProgress(renderPartialTick);
            f4 = MathHelper.sin(f9 * (float)Math.PI);
            f5 = MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI);
            GL11.glTranslatef(-f5 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI * 2.0F) * 0.4F, -f4 * 0.4F);
            GL11.glTranslatef(0.64000005F, -0.6F - (1.0F - f2) * 0.6F, -0.71999997F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(GL11.GL_RESCALE_NORMAL);
            f9 = this.mc.thePlayer.getSwingProgress(renderPartialTick);
            f4 = MathHelper.sin(f9 * f9 * (float)Math.PI);
            f5 = MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI);
            GL11.glRotatef(f5 * 70.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f4 * 20.0F, 0.0F, 0.0F, 1.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.getTexture()));
            GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
            GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glTranslatef(5.6F, 0.0F, 0.0F);
            RenderPlayer renderPlayer12 = (RenderPlayer)RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            renderPlayer12.drawFirstPersonHand();
            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
    }

    public void renderOverlays(float partialTicks) {
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        int i2;
        int i3;
        int i5;
        if(this.mc.thePlayer.fire > 0) {
            i2 = this.mc.renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, i2);
            this.renderFireInFirstPerson(partialTicks);
        }

        if(this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
            i2 = MathHelper.floor_double(this.mc.thePlayer.posX);
            i3 = MathHelper.floor_double(this.mc.thePlayer.posY);
            int i10 = MathHelper.floor_double(this.mc.thePlayer.posZ);
            i5 = this.mc.renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, i5);
            i2 = this.mc.theWorld.getBlockId(i2, i3, i10);
            if(Block.blocksList[i2] != null) {
                this.renderInsideOfBlock(partialTicks, Block.blocksList[i2].getBlockTextureFromSide(2));
            }
        }

        if(this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
            i2 = this.mc.renderEngine.getTexture("/water.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, i2);
            this.renderWarpedTextureOverlay(partialTicks);
        }

        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    private void renderInsideOfBlock(float partialTicks, int i2) {
        Tessellator tessellator13 = Tessellator.instance;
        this.mc.thePlayer.getBrightness(partialTicks);
        GL11.glColor4f(0.1F, 0.1F, 0.1F, 0.5F);
        GL11.glPushMatrix();
        float f9 = (float)(i2 % 16) / 256.0F - 0.0078125F;
        float f8 = ((float)(i2 % 16) + 15.99F) / 256.0F + 0.0078125F;
        float f6 = (float)(i2 / 16) / 256.0F - 0.0078125F;
        float f11 = ((float)(i2 / 16) + 15.99F) / 256.0F + 0.0078125F;
        tessellator13.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
        tessellator13.addVertexWithUV(-1.0D, -1.0D, -0.5D, (double)f8, (double)f11);
        tessellator13.addVertexWithUV(1.0D, -1.0D, -0.5D, (double)f9, (double)f11);
        tessellator13.addVertexWithUV(1.0D, 1.0D, -0.5D, (double)f9, (double)f6);
        tessellator13.addVertexWithUV(-1.0D, 1.0D, -0.5D, (double)f8, (double)f6);
        tessellator13.draw();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderWarpedTextureOverlay(float partialTicks) {
        Tessellator tessellator4 = Tessellator.instance;
        float f12;
        GL11.glColor4f(f12 = this.mc.thePlayer.getBrightness(partialTicks), f12, f12, 0.5F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glPushMatrix();
        float f7 = -this.mc.thePlayer.rotationYaw / 64.0F;
        float f9 = this.mc.thePlayer.rotationPitch / 64.0F;
        tessellator4.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
        tessellator4.addVertexWithUV(-1.0D, -1.0D, -0.5D, (double)(f7 + 4.0F), (double)(f9 + 4.0F));
        tessellator4.addVertexWithUV(1.0D, -1.0D, -0.5D, (double)(f7 + 0.0F), (double)(f9 + 4.0F));
        tessellator4.addVertexWithUV(1.0D, 1.0D, -0.5D, (double)(f7 + 0.0F), (double)(f9 + 0.0F));
        tessellator4.addVertexWithUV(-1.0D, 1.0D, -0.5D, (double)(f7 + 4.0F), (double)(f9 + 0.0F));
        tessellator4.draw();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void renderFireInFirstPerson(float partialTicks) {
        Tessellator tessellator4 = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        for(int i2 = 0; i2 < 2; ++i2) {
            GL11.glPushMatrix();
            int i3;
            int i5 = ((i3 = Block.fire.blockIndexInTexture + (i2 << 4)) & 15) << 4;
            i3 &= 240;
            float f6 = (float)i5 / 256.0F;
            float f12 = ((float)i5 + 15.99F) / 256.0F;
            float f7 = (float)i3 / 256.0F;
            float f9 = ((float)i3 + 15.99F) / 256.0F;
            GL11.glTranslatef((float)(-((i2 << 1) - 1)) * 0.24F, -0.3F, 0.0F);
            GL11.glRotatef((float)((i2 << 1) - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
            tessellator4.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
            tessellator4.addVertexWithUV(-0.5D, -0.5D, -0.5D, (double)f12, (double)f9);
            tessellator4.addVertexWithUV(0.5D, -0.5D, -0.5D, (double)f6, (double)f9);
            tessellator4.addVertexWithUV(0.5D, 0.5D, -0.5D, (double)f6, (double)f7);
            tessellator4.addVertexWithUV(-0.5D, 0.5D, -0.5D, (double)f12, (double)f7);
            tessellator4.draw();
            GL11.glPopMatrix();
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }

	public void updateEquippedItem() {
		this.prevEquippedProgress = this.equippedProgress;
		ItemStack itemStack = this.mc.thePlayer.inventory.getCurrentItem();
		float f2 = itemStack == this.itemToRender ? 1.0F : 0.0F;
		f2 -= this.equippedProgress;
		if(f2 < -0.4F) {
			f2 = -0.4F;
		}

		if(f2 > 0.4F) {
			f2 = 0.4F;
		}

		this.equippedProgress += f2;
		if(this.equippedProgress < 0.1F) {
			this.itemToRender = itemStack;
		}

	}

	public void resetEquippedProgress() {
		this.equippedProgress = 0.0F;
	}
}
