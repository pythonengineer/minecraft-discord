package net.minecraft.client.controller;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Session;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.ItemStack;
import net.minecraft.game.level.MobSpawner;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class PlayerControllerCreative extends PlayerController {
	private MobSpawner mobSpawner;

    private PlayerControllerCreative(Minecraft var1) {
        super(var1);
    }

	public final void displayInventoryGUI() {
        this.mc.displayGuiScreen(new GuiInventory());
	}

	public final void flipPlayer(EntityPlayer var1) {
		for(int var2 = 0; var2 < 9; ++var2) {
            if(var1.inventory.mainInventory[var2] == null) {
                this.mc.thePlayer.inventory.mainInventory[var2] = new ItemStack(Block.blocksList[((Block)Session.allowedBlocks.get(var2)).blockID]);
            } else {
                this.mc.thePlayer.inventory.mainInventory[var2].stackSize = 1;
            }
		}

	}

	public final boolean shouldDrawHUD() {
		return false;
	}

	public final void onWorldChange(World var1) {
		super.onWorldChange(var1);
		var1.survivalWorld = false;
		this.mobSpawner = new MobSpawner(var1);
        int var2 = var1.width * var1.length * var1.height / 64 / 64 / 64;

		for(int var3 = 0; var3 < var2; ++var3) {
			this.mobSpawner.performSpawning(var2, var1.playerEntity, (LoadingScreenRenderer)null);
		}

	}

	public final void onUpdate() {
		this.mobSpawner.performSpawning();
	}
}
