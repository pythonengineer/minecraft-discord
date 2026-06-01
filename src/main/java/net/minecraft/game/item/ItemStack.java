package net.minecraft.game.item;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class ItemStack {
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
		this.readFromNBT(compoundTag);
	}

	public ItemStack splitStack(int amount) {
		this.stackSize -= amount;
		return new ItemStack(this.itemID, amount, this.itemDmg);
	}

	public Item getItem() {
		return Item.itemsList[this.itemID];
	}

	public int getIconIndex() {
		return this.getItem().getIconIndex(this);
	}

	public boolean useItem(EntityPlayer entityPlayer, World world, int x, int y, int z, int side) {
		return this.getItem().onItemUse(this, entityPlayer, world, x, y, z, side);
	}

	public float getStrVsBlock(Block block) {
		return this.getItem().getStrVsBlock(this, block);
	}

	public ItemStack useItemRightClick(World world, EntityPlayer entityPlayer) {
		return this.getItem().onItemRightClick(this, world, entityPlayer);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compoundTag) {
		compoundTag.setShort("id", (short)this.itemID);
		compoundTag.setByte("Count", (byte)this.stackSize);
		compoundTag.setShort("Damage", (short)this.itemDmg);
		return compoundTag;
	}

	public void readFromNBT(NBTTagCompound compoundTag) {
		this.itemID = compoundTag.getShort("id");
		this.stackSize = compoundTag.getByte("Count");
		this.itemDmg = compoundTag.getShort("Damage");
	}

	public int getMaxStackSize() {
		return this.getItem().getItemStackLimit();
	}

	public int getMaxDamage() {
		return Item.itemsList[this.itemID].getMaxDamage();
	}

	public void damageItem(int damage) {
		this.itemDmg += damage;
		if(this.itemDmg > this.getMaxDamage()) {
			--this.stackSize;
			if(this.stackSize < 0) {
				this.stackSize = 0;
			}

			this.itemDmg = 0;
		}

	}

	public void hitEntity(EntityLiving entityLiving) {
		Item.itemsList[this.itemID].hitEntity(this, entityLiving);
	}

	public void onDestroyBlock(int i1, int i2, int i3, int i4) {
		Item.itemsList[this.itemID].onBlockDestroyed(this, i1, i2, i3, i4);
	}

	public int getDamageVsEntity(Entity entity) {
		return Item.itemsList[this.itemID].getDamageVsEntity(entity);
	}

	public boolean canHarvestBlock(Block block) {
		return Item.itemsList[this.itemID].canHarvestBlock(block);
	}

	public void onItemDestroyedByUse(EntityPlayer entityPlayer) {
	}

	public void useItemOnEntity(EntityLiving entityLiving) {
		Item.itemsList[this.itemID].saddleEntity(this, entityLiving);
	}

    public ItemStack copy() {
        ItemStack itemstack = new ItemStack(this.itemID, this.stackSize, this.itemDmg);
        return itemstack;
    }
}
