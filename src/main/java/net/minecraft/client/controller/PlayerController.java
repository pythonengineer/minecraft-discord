package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.StepSound;

public class PlayerController {
	protected final Minecraft mc;
	public boolean isInTestMode = false;

	public PlayerController(Minecraft mc) {
		this.mc = mc;
	}

    public void init() {
    }

    public void onWorldChange(World world) {
    }

	public void clickBlock(int x, int y, int z) {
		this.sendBlockRemoved(x, y, z);
	}

	public boolean sendBlockRemoved(int x, int y, int z) {
		this.mc.effectRenderer.addBlockDestroyEffects(x, y, z);
		World world4 = this.mc.theWorld;
		Block block5 = Block.blocksList[world4.getBlockId(x, y, z)];
		int i6 = world4.getBlockMetadata(x, y, z);
		boolean z7 = world4.setBlockWithNotify(x, y, z, 0);
		if(block5 != null && z7) {
            this.mc.sndManager.playSound(block5.stepSound.getBreakSound(), (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, (block5.stepSound.getVolume() + 1.0F) / 2.0F, block5.stepSound.getPitch() * 0.8F);
			block5.onBlockDestroyedByPlayer(world4, x, y, z, i6);
		}

		return z7;
	}

	public void sendBlockRemoving(int x, int y, int z, int blockID) {
	}

	public void resetBlockRemoving() {
	}

	public void setPartialTime(float partialTime) {
	}

	public float getBlockReachDistance() {
		return 5.0F;
	}

    public void flipPlayer(EntityPlayer playerEntity) {
    }

	public void onUpdate() {
	}

	public boolean shouldDrawHUD() {
		return true;
	}

	public void onRespawn(EntityPlayer playerEntity) {
	}

    public boolean onPlayerRightClick(EntityPlayer entityPlayer, World world, ItemStack itemStack, int x, int y, int z, int side) {
        return itemStack.useItem(entityPlayer, world, x, y, z, side);
    }
}
