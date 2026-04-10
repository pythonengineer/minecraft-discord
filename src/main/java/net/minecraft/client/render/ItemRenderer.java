package net.minecraft.client.render;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.client.render.entity.RenderPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

public final class ItemRenderer {
	private Minecraft mc;
	private ItemStack itemToRender = null;
	private float equippedProgress = 0.0F;
	private float prevEquippedProgress = 0.0F;
	private int swingProgress = 0;
	private boolean itemSwingState = false;
	private RenderBlocks renderBlocksInstance = new RenderBlocks();

	public ItemRenderer(Minecraft mc) {
		this.mc = mc;
	}

    public final void renderItemInFirstPerson(float partialTicks) {
        float f2 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks;
        GL11.glPushMatrix();
        GL11.glRotatef(this.mc.thePlayer.prevRotationPitch + (this.mc.thePlayer.rotationPitch - this.mc.thePlayer.prevRotationPitch) * partialTicks, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(this.mc.thePlayer.prevRotationYaw + (this.mc.thePlayer.rotationYaw - this.mc.thePlayer.prevRotationYaw) * partialTicks, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        float f9 = this.mc.theWorld.getBrightness(MathHelper.floor_double(this.mc.thePlayer.posX), MathHelper.floor_double(this.mc.thePlayer.posY), MathHelper.floor_double(this.mc.thePlayer.posZ));
        GL11.glColor4f(f9, f9, f9, 1.0F);
        float f4;
        float f5;
        if(this.itemToRender != null) {
            GL11.glPushMatrix();
            if(this.itemSwingState) {
                f9 = ((float)this.swingProgress + partialTicks) / 8.0F;
                f4 = MathHelper.sin(f9 * (float)Math.PI);
                f5 = MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI);
                GL11.glTranslatef(-f5 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI * 2.0F) * 0.2F, -f4 * 0.2F);
            }

            GL11.glTranslatef(0.56F, -0.52F - (1.0F - f2) * 0.6F, -0.71999997F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(GL11.GL_RESCALE_NORMAL);
            if(this.itemSwingState) {
                f9 = ((float)this.swingProgress + partialTicks) / 8.0F;
                f4 = MathHelper.sin(f9 * f9 * (float)Math.PI);
                f5 = MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI);
                GL11.glRotatef(-f4 * 20.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-f5 * 20.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-f5 * 80.0F, 1.0F, 0.0F, 0.0F);
            }

            GL11.glScalef(0.4F, 0.4F, 0.4F);
            if(this.itemToRender.itemID < 256 && Block.blocksList[this.itemToRender.itemID].getRenderType() == 0) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
                this.renderBlocksInstance.renderBlockOnInventory(Block.blocksList[this.itemToRender.itemID]);
            } else {
                if(this.itemToRender.itemID < 256) {
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
                } else {
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/items.png"));
                }

                Tessellator t = Tessellator.instance;
                ItemStack itemStack = this.itemToRender;
                f5 = (float)(itemStack.getItem().getIcon() % 16 << 4) / 256.0F;
                itemStack = this.itemToRender;
                partialTicks = (float)((itemStack.getItem().getIcon() % 16 << 4) + 16) / 256.0F;
                itemStack = this.itemToRender;
                f2 = (float)(itemStack.getItem().getIcon() / 16 << 4) / 256.0F;
                itemStack = this.itemToRender;
                f9 = (float)((itemStack.getItem().getIcon() / 16 << 4) + 16) / 256.0F;
                GL11.glEnable(GL11.GL_RESCALE_NORMAL);
                GL11.glTranslatef(0.0F, -0.3F, 0.0F);
                GL11.glScalef(1.5F, 1.5F, 1.5F);
                GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(-(15.0F / 16.0F), -(1.0F / 16.0F), 0.0F);
                t.setNormal(0.0F, 0.0F, 1.0F);
                t.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
                t.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)partialTicks, (double)f9);
                t.addVertexWithUV(1.0D, 0.0D, 0.0D, (double)f5, (double)f9);
                t.addVertexWithUV(1.0D, 1.0D, 0.0D, (double)f5, (double)f2);
                t.addVertexWithUV(0.0D, 1.0D, 0.0D, (double)partialTicks, (double)f2);
                t.draw();
                t.setNormal(0.0F, 0.0F, -1.0F);
                t.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
                t.addVertexWithUV(0.0D, 1.0D, -0.0625D, (double)partialTicks, (double)f2);
                t.addVertexWithUV(1.0D, 1.0D, -0.0625D, (double)f5, (double)f2);
                t.addVertexWithUV(1.0D, 0.0D, -0.0625D, (double)f5, (double)f9);
                t.addVertexWithUV(0.0D, 0.0D, -0.0625D, (double)partialTicks, (double)f9);
                t.draw();
                t.setNormal(-1.0F, 0.0F, 0.0F);
                t.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);

                int i6;
                float f7;
                float f8;
                for(i6 = 0; i6 < 16; ++i6) {
                    f7 = (float)i6 / 16.0F;
                    f8 = partialTicks + (f5 - partialTicks) * f7 - 0.001953125F;
                    f7 *= 1.0F;
                    t.addVertexWithUV((double)f7, 0.0D, -0.0625D, (double)f8, (double)f9);
                    t.addVertexWithUV((double)f7, 0.0D, 0.0D, (double)f8, (double)f9);
                    t.addVertexWithUV((double)f7, 1.0D, 0.0D, (double)f8, (double)f2);
                    t.addVertexWithUV((double)f7, 1.0D, -0.0625D, (double)f8, (double)f2);
                }

                t.draw();
                t.setNormal(1.0F, 0.0F, 0.0F);
                t.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);

                for(i6 = 0; i6 < 16; ++i6) {
                    f7 = (float)i6 / 16.0F;
                    f8 = partialTicks + (f5 - partialTicks) * f7 - 0.001953125F;
                    f7 = f7 * 1.0F + 1.0F / 16.0F;
                    t.addVertexWithUV((double)f7, 1.0D, -0.0625D, (double)f8, (double)f2);
                    t.addVertexWithUV((double)f7, 1.0D, 0.0D, (double)f8, (double)f2);
                    t.addVertexWithUV((double)f7, 0.0D, 0.0D, (double)f8, (double)f9);
                    t.addVertexWithUV((double)f7, 0.0D, -0.0625D, (double)f8, (double)f9);
                }

                t.draw();
                t.setNormal(0.0F, 1.0F, 0.0F);
                t.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);

                for(i6 = 0; i6 < 16; ++i6) {
                    f7 = (float)i6 / 16.0F;
                    f8 = f9 + (f2 - f9) * f7 - 0.001953125F;
                    f7 = f7 * 1.0F + 1.0F / 16.0F;
                    t.addVertexWithUV(0.0D, (double)f7, 0.0D, (double)partialTicks, (double)f8);
                    t.addVertexWithUV(1.0D, (double)f7, 0.0D, (double)f5, (double)f8);
                    t.addVertexWithUV(1.0D, (double)f7, -0.0625D, (double)f5, (double)f8);
                    t.addVertexWithUV(0.0D, (double)f7, -0.0625D, (double)partialTicks, (double)f8);
                }

                t.draw();
                t.setNormal(0.0F, -1.0F, 0.0F);
                t.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);

                for(i6 = 0; i6 < 16; ++i6) {
                    f7 = (float)i6 / 16.0F;
                    f8 = f9 + (f2 - f9) * f7 - 0.001953125F;
                    f7 *= 1.0F;
                    t.addVertexWithUV(1.0D, (double)f7, 0.0D, (double)f5, (double)f8);
                    t.addVertexWithUV(0.0D, (double)f7, 0.0D, (double)partialTicks, (double)f8);
                    t.addVertexWithUV(0.0D, (double)f7, -0.0625D, (double)partialTicks, (double)f8);
                    t.addVertexWithUV(1.0D, (double)f7, -0.0625D, (double)f5, (double)f8);
                }

                t.draw();
                GL11.glDisable(GL11.GL_RESCALE_NORMAL);
            }

            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            if(this.itemSwingState) {
                f9 = ((float)this.swingProgress + partialTicks) / 8.0F;
                f4 = MathHelper.sin(f9 * (float)Math.PI);
                f5 = MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI);
                GL11.glTranslatef(-f5 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI * 2.0F) * 0.4F, -f4 * 0.4F);
            }

            GL11.glTranslatef(0.64000005F, -0.6F - (1.0F - f2) * 0.6F, -0.71999997F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(GL11.GL_RESCALE_NORMAL);
            if(this.itemSwingState) {
                f9 = ((float)this.swingProgress + partialTicks) / 8.0F;
                f4 = MathHelper.sin(f9 * f9 * (float)Math.PI);
                f5 = MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI);
                GL11.glRotatef(f5 * 70.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-f4 * 20.0F, 0.0F, 0.0F, 1.0F);
            }

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.getTexture()));
            GL11.glTranslatef(-0.2F, -0.3F, 0.1F);
            GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glTranslatef(6.0F, 0.0F, 0.0F);
            ((RenderPlayer)RenderManager.instance.getEntityRenderObject(this.mc.thePlayer)).drawFirstPersonHand();
            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
    }

    public final void renderOverlays(float partialTicks) {
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        int i2;
        Tessellator t;
        float f7;
        float f9;
        if(this.mc.thePlayer.fire > 0) {
            i2 = this.mc.renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, i2);
            t = Tessellator.instance;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            for(i2 = 0; i2 < 2; ++i2) {
                GL11.glPushMatrix();
                int i4 = Block.fire.blockIndexInTexture + (i2 << 4);
                int i5 = (i4 & 15) << 4;
                i4 &= 240;
                float f6 = (float)i5 / 256.0F;
                float f10 = ((float)i5 + 15.99F) / 256.0F;
                f7 = (float)i4 / 256.0F;
                f9 = ((float)i4 + 15.99F) / 256.0F;
                GL11.glTranslatef((float)(-((i2 << 1) - 1)) * 0.24F, -0.3F, 0.0F);
                GL11.glRotatef((float)((i2 << 1) - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
                t.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
                t.addVertexWithUV(-0.5D, -0.5D, -0.5D, (double)f10, (double)f9);
                t.addVertexWithUV(0.5D, -0.5D, -0.5D, (double)f6, (double)f9);
                t.addVertexWithUV(0.5D, 0.5D, -0.5D, (double)f6, (double)f7);
                t.addVertexWithUV(-0.5D, 0.5D, -0.5D, (double)f10, (double)f7);
                t.draw();
                GL11.glPopMatrix();
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
        }

        if(this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
            i2 = this.mc.renderEngine.getTexture("/water.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, i2);
            t = Tessellator.instance;
            float f8 = this.mc.thePlayer.getBrightness(partialTicks);
            GL11.glColor4f(f8, f8, f8, 0.5F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glPushMatrix();
            f7 = -this.mc.thePlayer.rotationYaw / 64.0F;
            f9 = this.mc.thePlayer.rotationPitch / 64.0F;
            t.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
            t.addVertexWithUV(-1.0D, -1.0D, -0.5D, (double)(f7 + 4.0F), (double)(f9 + 4.0F));
            t.addVertexWithUV(1.0D, -1.0D, -0.5D, (double)(f7 + 0.0F), (double)(f9 + 4.0F));
            t.addVertexWithUV(1.0D, 1.0D, -0.5D, (double)(f7 + 0.0F), (double)(f9 + 0.0F));
            t.addVertexWithUV(-1.0D, 1.0D, -0.5D, (double)(f7 + 4.0F), (double)(f9 + 0.0F));
            t.draw();
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
        }

        GL11.glEnable(GL11.GL_ALPHA_TEST);
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

	public final void resetEquippedProgress() {
		this.equippedProgress = 0.0F;
	}

	public final void swing() {
		this.swingProgress = -1;
		this.itemSwingState = true;
	}
}
