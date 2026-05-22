package net.minecraft.game.item;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.world.block.Block;

public final class ItemStack {
	public int stackSize;
	public int animationsToGo;
	public int itemID;
	public int itemDmg;

	public ItemStack(Block block) {
		this((Block)block, 1);
	}

	public ItemStack(Block block, int amount) {
		this(block.blockID, amount);
	}

	public ItemStack(Item item) {
		this((Item)item, 1);
	}

	public ItemStack(Item item, int amount) {
		this(item.shiftedIndex, amount);
	}

	public ItemStack(int id) {
		this(id, 1);
	}

	public ItemStack(int itemID, int amount) {
		this.stackSize = 0;
		this.itemID = itemID;
		this.stackSize = amount;
	}

	public ItemStack(int itemID, int amount, int damage) {
		this.stackSize = 0;
		this.itemID = itemID;
		this.stackSize = amount;
		this.itemDmg = damage;
	}

	public ItemStack(NBTTagCompound compoundTag) {
		this.stackSize = 0;
		this.itemID = compoundTag.getShort("id");
		this.stackSize = compoundTag.getByte("Count");
		this.itemDmg = compoundTag.getShort("Damage");
	}

	public final ItemStack splitStack(int amount) {
		this.stackSize -= amount;
		return new ItemStack(this.itemID, amount, this.itemDmg);
	}

	public final Item getItem() {
		return Item.itemsList[this.itemID];
	}

	public final NBTTagCompound writeToNBT(NBTTagCompound compoundTag) {
		compoundTag.setShort("id", (short)this.itemID);
		compoundTag.setByte("Count", (byte)this.stackSize);
		compoundTag.setShort("Damage", (short)this.itemDmg);
		return compoundTag;
	}

	public final int getMaxDamage() {
		return Item.itemsList[this.itemID].getMaxDamage();
	}

	public final void damageItem(int damage) {
		this.itemDmg += damage;
		if(this.itemDmg > this.getMaxDamage()) {
			--this.stackSize;
			if(this.stackSize < 0) {
				this.stackSize = 0;
			}

			this.itemDmg = 0;
		}

	}

    public ItemStack copy() {
        ItemStack itemstack = new ItemStack(this.itemID, this.stackSize, this.itemDmg);
        return itemstack;
    }
}