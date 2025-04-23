package net.minecraft.client.controller;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.EntityPlayer;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.MobSpawner;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class PlayerControllerSP extends PlayerController {
	private int curBlockX;
	private int curBlockY;
	private int curBlockZ;
	private int curBlockDamage;
	private int prevBlockDamage;
	private int blockHitWait;
	private MobSpawner mobSpawner;

	private PlayerControllerSP(Minecraft var1) {
		super(var1);
	}

	public final void preparePlayer(EntityPlayer var1) {
		var1.inventory.mainInventory[5] = Block.stairSingle.blockID;
		var1.inventory.stackSize[5] = 99;
		var1.inventory.mainInventory[6] = Block.stone.blockID;
		var1.inventory.stackSize[6] = 99;
		var1.inventory.mainInventory[7] = Block.waterMoving.blockID;
		var1.inventory.stackSize[7] = 99;
		var1.inventory.mainInventory[8] = Block.lavaMoving.blockID;
		var1.inventory.stackSize[8] = 99;
	}

	public final void sendBlockRemoved(int var1, int var2, int var3) {
		int var4 = this.mc.theWorld.getBlockId(var1, var2, var3);
		Block.blocksList[var4].dropBlockAsItem(this.mc.theWorld);
		super.sendBlockRemoved(var1, var2, var3);
	}

	public final boolean canPlace(int var1) {
		return this.mc.thePlayer.inventory.consumeInventoryItem(var1);
	}

	public final void clickBlock(int var1, int var2, int var3) {
		int var4 = this.mc.theWorld.getBlockId(var1, var2, var3);
		if(var4 > 0 && Block.blocksList[var4].blockStrength() == 0) {
			this.sendBlockRemoved(var1, var2, var3);
		}

	}

	public final void resetBlockRemoving() {
		this.curBlockDamage = 0;
		this.blockHitWait = 0;
	}

	public final void sendBlockRemoving(int var1, int var2, int var3) {
		if(this.blockHitWait > 0) {
			--this.blockHitWait;
		} else if(var1 == this.curBlockX && var2 == this.curBlockY && var3 == this.curBlockZ) {
			int var4 = this.mc.theWorld.getBlockId(var1, var2, var3);
			if(var4 != 0) {
				Block var5 = Block.blocksList[var4];
				this.prevBlockDamage = var5.blockStrength();
				++this.curBlockDamage;
				if(this.curBlockDamage == this.prevBlockDamage + 1) {
					this.sendBlockRemoved(var1, var2, var3);
					this.curBlockDamage = 0;
					this.blockHitWait = 5;
				}

			}
		} else {
			this.curBlockDamage = 0;
			this.curBlockX = var1;
			this.curBlockY = var2;
			this.curBlockZ = var3;
		}
	}

	public final void setPartialTime(float var1) {
		if(this.curBlockDamage <= 0) {
			this.mc.renderGlobal.damagePartialTime = 0.0F;
		} else {
			this.mc.renderGlobal.damagePartialTime = ((float)this.curBlockDamage + var1 - 1.0F) / (float)this.prevBlockDamage;
		}
	}

	public final float getBlockReachDistance() {
		return 4.0F;
	}

	public final boolean sendUseItem(EntityPlayer var1, int var2) {
		Block var3 = Block.blocksList[var2];
		if(var3 == Block.mushroomRed && this.mc.thePlayer.inventory.consumeInventoryItem(var2)) {
			var1.attackEntityFrom((Entity)null, 3);
			return true;
		} else if(var3 == Block.mushroomBrown && this.mc.thePlayer.inventory.consumeInventoryItem(var2)) {
			boolean var4 = false;
			if(var1.health > 0) {
				var1.health += 5;
				if(var1.health > 20) {
					var1.health = 20;
				}

				var1.scoreValue = var1.heartsHalvesLife / 2;
			}

			return true;
		} else {
			return false;
		}
	}

	public final void onWorldChange(World var1) {
		super.onWorldChange(var1);
		this.mobSpawner = new MobSpawner(var1);
		int var2 = var1.width * var1.length * var1.height / 64 / 64 / 8;

		for(int var3 = 0; var3 < var2; ++var3) {
			this.mobSpawner.performSpawning(var2, var1.playerEntity, (LoadingScreenRenderer)null);
		}

	}

	public final void onUpdate() {
		this.mobSpawner.performSpawning();
	}
}
