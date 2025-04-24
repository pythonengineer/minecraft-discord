package net.minecraft.client.controller;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.entity.player.ItemStack;
import net.minecraft.game.level.MobSpawner;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class PlayerControllerSP extends PlayerController {
	private int curBlockX = -1;
	private int curBlockY = -1;
	private int curBlockZ = -1;
	private int curBlockDamage = 0;
	private int prevBlockDamage = 0;
	private int blockHitWait = 0;
	private MobSpawner mobSpawner;

	public PlayerControllerSP(Minecraft var1) {
		super(var1);
	}

	public final void displayInventoryGUI() {
		this.mc.displayGuiScreen(new GuiInventory());
	}

	public final void preparePlayer(EntityPlayer var1) {
		var1.inventory.mainInventory[8] = new ItemStack(Block.bookShelf, 99);
		var1.inventory.mainInventory[7] = new ItemStack(Block.tnt, 99);

		for(int var4 = 0; var4 < 20; ++var4) {
			int var2 = var4 % 5;
			int var3 = var4 / 5;
			var1.inventory.mainInventory[var4 + 9] = new ItemStack(var2 + (var3 << 4));
		}

	}

	public final void sendBlockRemoved(int var1, int var2, int var3) {
		int var4 = this.mc.theWorld.getBlockId(var1, var2, var3);
		Block.blocksList[var4].dropBlockAsItem(this.mc.theWorld, var1, var2, var3);
		super.sendBlockRemoved(var1, var2, var3);
	}

	public final boolean canPlace(int var1) {
		InventoryPlayer var2 = this.mc.thePlayer.inventory;
		var1 = var2.getInventorySlotContainItem(var1);
		if(var1 < 0) {
			return false;
		} else {
			if(--var2.mainInventory[var1].stackSize <= 0) {
				var2.mainInventory[var1] = null;
			}

			return true;
		}
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
