package net.minecraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.game.entity.player.ItemStack;

public final class ItemRenderer {
	public Minecraft mc;
	public ItemStack itemToRender = null;
	public float equippedProgress = 0.0F;
	public float prevEquippedProgress = 0.0F;
	public int swingProgress = 0;
	public boolean itemSwingState = false;
	RenderBlocks renderBlocksInstance = new RenderBlocks(Tessellator.instance);

	public ItemRenderer(Minecraft var1) {
		this.mc = var1;
	}
}
