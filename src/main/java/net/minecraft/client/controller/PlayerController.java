package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.StepSound;

public class PlayerController {
	protected final Minecraft mc;
	public boolean isInTestMode = false;

	public PlayerController(Minecraft mc) {
		this.mc = mc;
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
			SoundManager soundManager10000 = this.mc.sndManager;
			String string10001 = block5.stepSound.getBreakSound();
			float f10002 = (float)x + 0.5F;
			float f10003 = (float)y + 0.5F;
			float f10004 = (float)z + 0.5F;
			StepSound stepSound8 = block5.stepSound;
			float f10005 = (block5.stepSound.stepSoundVolume + 1.0F) / 2.0F;
			stepSound8 = block5.stepSound;
			soundManager10000.playSound(string10001, f10002, f10003, f10004, f10005, block5.stepSound.stepSoundPitch * 0.8F);
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

	public void updateController() {
	}

	public boolean shouldDrawHUD() {
		return true;
	}

	public void onRespawn(EntityPlayer playerEntity) {
	}
}