package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.effect.EntityDiggingFX;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public class PlayerController {
	protected final Minecraft mc;

	public PlayerController(Minecraft var1) {
		this.mc = var1;
	}

	public void onWorldChange(World var1) {
		var1.multiplayerWorld = true;
	}

	public void displayInventoryGUI() {
	}

	public void clickBlock(int var1, int var2, int var3) {
		this.sendBlockRemoved(var1, var2, var3);
	}

	public boolean canPlace(int var1) {
		return true;
	}

	public void sendBlockRemoved(int var1, int var2, int var3) {
		int var7 = var3;
		int var6 = var2;
		int var5 = var1;
		EffectRenderer var4 = this.mc.effectRenderer;
		int var8 = var4.worldObj.getBlockId(var1, var2, var3);
		if(var8 != 0) {
			Block var18 = Block.blocksList[var8];

			for(int var9 = 0; var9 < 4; ++var9) {
				for(int var10 = 0; var10 < 4; ++var10) {
					for(int var11 = 0; var11 < 4; ++var11) {
						float var12 = (float)var5 + ((float)var9 + 0.5F) / (float)4;
						float var13 = (float)var6 + ((float)var10 + 0.5F) / (float)4;
						float var14 = (float)var7 + ((float)var11 + 0.5F) / (float)4;
						var4.addEffect(new EntityDiggingFX(var4.worldObj, var12, var13, var14, var12 - (float)var5 - 0.5F, var13 - (float)var6 - 0.5F, var14 - (float)var7 - 0.5F, var18));
					}
				}
			}
		}

		World var15 = this.mc.theWorld;
		Block var16 = Block.blocksList[var15.getBlockId(var1, var2, var3)];
		boolean var17 = var15.setBlockWithNotify(var1, var2, var3, 0);
		if(var16 != null && var17) {
			var16.onBlockDestroyedByPlayer(var15, var1, var2, var3);
		}

	}

	public void sendBlockRemoving(int var1, int var2, int var3) {
	}

	public void resetBlockRemoving() {
	}

	public void setPartialTime(float var1) {
	}

	public float getBlockReachDistance() {
		return 5.0F;
	}

	public void preparePlayer(EntityPlayer var1) {
	}

	public void onUpdate() {
	}

	public boolean shouldDrawHUD() {
		return true;
	}

	public void flipPlayer(EntityPlayer var1) {
	}
}
