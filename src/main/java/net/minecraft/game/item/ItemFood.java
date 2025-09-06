package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;

public class ItemFood extends Item {
	private int blocksEffectiveAgainst;

	public ItemFood(int var1, int var2) {
		super(var1);
		this.blocksEffectiveAgainst = var2;
		this.maxStackSize = 1;
	}

	public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
		--var1.stackSize;
		EntityPlayer var10000 = var3;
		int var5 = this.blocksEffectiveAgainst;
		EntityPlayer var4 = var10000;
		if(var4.health > 0) {
			var4.health += var5;
			if(var4.health > 20) {
				var4.health = 20;
			}

			var4.heartsLife = var4.heartsHalvesLife / 2;
		}

		return var1;
	}

    public boolean shouldUseOnTouchEagler(ItemStack itemStack) {
        return true;
    }
}
