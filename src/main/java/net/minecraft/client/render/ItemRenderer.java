package net.minecraft.client.render;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;

public final class ItemRenderer {
	private Minecraft mc;
	private ItemStack itemToRender = null;
	private float equippedProgress = 0.0F;
	private float prevEquippedProgress = 0.0F;
	private int swingProgress = 0;
	private boolean itemSwingState = false;
	private RenderBlocks renderBlocksInstance = new RenderBlocks(Tessellator.instance);

	public ItemRenderer(Minecraft var1) {
		this.mc = var1;
	}

	public final void renderItemInFirstPerson(float var1) {
		float var2 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * var1;
		EntityPlayerSP var3 = this.mc.thePlayer;
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

		var4 = this.mc.theWorld.getBlockLightValue((int)var3.posX, (int)var3.posY, (int)var3.posZ);
		GL11.glColor4f(var4, var4, var4, 1.0F);
		float var9;
		if(this.itemToRender != null) {
			GL11.glScalef(0.4F, 0.4F, 0.4F);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			if(this.itemToRender.itemID < 256) {
				this.renderBlocksInstance.renderBlockOnInventory(Block.blocksList[this.itemToRender.itemID]);
			} else {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/items.png"));
				GL11.glDisable(GL11.GL_LIGHTING);
				Tessellator var13 = Tessellator.instance;
				ItemStack var11 = this.itemToRender;
				var1 = (float)(var11.getItem().getIconIndex() % 16 << 4) / 256.0F;
				var11 = this.itemToRender;
				var2 = (float)((var11.getItem().getIconIndex() % 16 << 4) + 16) / 256.0F;
				var11 = this.itemToRender;
				var9 = (float)(var11.getItem().getIconIndex() / 16 << 4) / 256.0F;
				var11 = this.itemToRender;
				var4 = (float)((var11.getItem().getIconIndex() / 16 << 4) + 16) / 256.0F;
				var13.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
				var13.addVertexWithUV(-0.4F, -0.2F, -0.4F, var1, var4);
				var13.addVertexWithUV(0.29999998F, -0.2F, 0.29999998F, var2, var4);
				var13.addVertexWithUV(0.29999998F, 0.8F, 0.29999998F, var2, var9);
				var13.addVertexWithUV(-0.4F, 0.8F, -0.4F, var1, var9);
				var13.draw();
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
		if(this.mc.thePlayer.fire > 0) {
			int var12 = this.mc.renderEngine.getTexture("/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var12);
			Tessellator var7 = Tessellator.instance;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			for(int var8 = 0; var8 < 2; ++var8) {
				GL11.glPushMatrix();
				int var10 = Block.fire.blockIndexInTexture + (var8 << 4);
				int var14 = (var10 & 15) << 4;
				var10 &= 240;
				var5 = (float)var14 / 256.0F;
				var4 = ((float)var14 + 15.99F) / 256.0F;
				var6 = (float)var10 / 256.0F;
				var9 = ((float)var10 + 15.99F) / 256.0F;
				GL11.glTranslatef((float)(-((var8 << 1) - 1)) * 0.24F, -0.3F, 0.0F);
				GL11.glRotatef((float)((var8 << 1) - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
				var7.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
				var7.addVertexWithUV(-0.5F, -0.5F, -0.5F, var4, var9);
				var7.addVertexWithUV(0.5F, -0.5F, -0.5F, var5, var9);
				var7.addVertexWithUV(0.5F, 0.5F, -0.5F, var5, var6);
				var7.addVertexWithUV(-0.5F, 0.5F, -0.5F, var4, var6);
				var7.draw();
				GL11.glPopMatrix();
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
		}

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

		EntityPlayerSP var1 = this.mc.thePlayer;
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

	public final void resetEquippedProgress() {
		this.equippedProgress = 0.0F;
	}

	public final void swingItem() {
		this.swingProgress = -1;
		this.itemSwingState = true;
	}

	public final void resetEquippedProgress2() {
		this.equippedProgress = 0.0F;
	}
}
