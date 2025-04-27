package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.MobSpawner;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;

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

	public final void openInventory() {
		this.mc.displayGuiScreen(new GuiInventory());
	}

	public final void flipPlayer(EntityPlayer var1) {
		var1.inventory.mainInventory[4] = new ItemStack(Block.clothWhite, 99);
		var1.inventory.mainInventory[5] = new ItemStack(Block.glass, 99);
		var1.inventory.mainInventory[6] = new ItemStack(Block.torch, 99);
		var1.inventory.mainInventory[7] = new ItemStack(Block.tnt, 99);
		var1.inventory.mainInventory[8] = new ItemStack(Block.bookShelf, 99);
		int var2 = 0;

		for(int var3 = 256; var3 < 1024 && var2 < 4; ++var3) {
			if(Item.itemsList[var3] != null) {
				var1.inventory.mainInventory[var2] = new ItemStack(var3);
				++var2;
			}
		}

		var1.inventory.mainInventory[9] = new ItemStack(Item.apple.shiftedIndex, 99);
	}

	public final boolean sendBlockRemoved(int var1, int var2, int var3) {
		int var4 = this.mc.theWorld.getBlockId(var1, var2, var3);
		boolean var5 = super.sendBlockRemoved(var1, var2, var3);
		if(var5) {
			Block.blocksList[var4].dropBlockAsItem(this.mc.theWorld, var1, var2, var3);
		}

		return var5;
	}

	public final void clickBlock(int var1, int var2, int var3) {
		int var4 = this.mc.theWorld.getBlockId(var1, var2, var3);
		if(var4 > 0 && Block.blocksList[var4].blockStrength(this.mc.thePlayer) == 0) {
			this.sendBlockRemoved(var1, var2, var3);
		}

	}

	public final void resetBlockRemoving() {
		this.curBlockDamage = 0;
		this.blockHitWait = 0;
	}

	public final void sendBlockRemoving(int var1, int var2, int var3, int var4) {
		if(this.blockHitWait > 0) {
			--this.blockHitWait;
		} else {
			super.sendBlockRemoving(var1, var2, var3, var4);
			if(var1 == this.curBlockX && var2 == this.curBlockY && var3 == this.curBlockZ) {
				var4 = this.mc.theWorld.getBlockId(var1, var2, var3);
				if(var4 != 0) {
					Block var6 = Block.blocksList[var4];
					this.prevBlockDamage = var6.blockStrength(this.mc.thePlayer);
					if(this.curBlockDamage % 4 == 0 && var6 != null) {
						SoundManager var10000 = this.mc.sndManager;
						String var10001 = "step." + var6.stepSound.soundDir;
						float var10002 = (float)var1 + 0.5F;
						float var10003 = (float)var2 + 0.5F;
						float var10004 = (float)var3 + 0.5F;
						StepSound var5 = var6.stepSound;
						float var10005 = (var5.soundVolume + 1.0F) / 8.0F;
						var5 = var6.stepSound;
						var10000.playSound(var10001, var10002, var10003, var10004, var10005, var5.soundPitch * 0.5F);
					}

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
		int var2 = var1.width * var1.length * var1.height / 64 / 64 / 64;

		for(int var3 = 0; var3 < var2; ++var3) {
			this.mobSpawner.performSpawning(var2, var1.playerEntity, null);
		}

	}

	public final void onUpdate() {
		this.mobSpawner.performSpawning();
	}
}
