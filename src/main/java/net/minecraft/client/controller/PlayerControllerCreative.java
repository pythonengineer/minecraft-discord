package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Session;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class PlayerControllerCreative extends PlayerController {
	public PlayerControllerCreative(Minecraft minecraft1) {
		super(minecraft1);
		this.isInTestMode = true;
	}

    public void init() {
    }

	public void onRespawn(EntityPlayer playerEntity) {
		for(int i2 = 0; i2 < 9; ++i2) {
			if(playerEntity.inventory.mainInventory[i2] == null) {
				this.mc.thePlayer.inventory.mainInventory[i2] = new ItemStack(((Block)Session.registeredBlocksList.get(i2)).blockID);
			} else {
				this.mc.thePlayer.inventory.mainInventory[i2].stackSize = 1;
			}
		}

	}

	public boolean shouldDrawHUD() {
		return false;
	}

    public void onWorldChange(World world) {
        super.onWorldChange(world);
    }

	public void onUpdate() {
	}
}
