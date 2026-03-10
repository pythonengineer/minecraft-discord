package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.world.World;

public final class ItemBow extends Item {
	public ItemBow(int i1) {
		super(5);
		this.maxStackSize = 1;
	}

	public final ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer playerEntity) {
		if(playerEntity.inventory.consumeInventoryItem(Item.arrow.shiftedIndex)) {
			world.playSoundAtEntity(playerEntity, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
			world.entityJoinedWorld(new EntityArrow(world, playerEntity));
		}

		return stack;
	}

    public boolean shouldUseOnTouchEagler(ItemStack itemStack) {
        return true;
    }
}