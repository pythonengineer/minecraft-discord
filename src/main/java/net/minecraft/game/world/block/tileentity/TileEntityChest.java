package net.minecraft.game.world.block.tileentity;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public class TileEntityChest extends TileEntity implements IInventory {
	private ItemStack[] chestContents = new ItemStack[36];

	public int getSizeInventory() {
		return 27;
	}

	public ItemStack getStackInSlot(int slot) {
		return this.chestContents[slot];
	}

	public ItemStack decrStackSize(int slot, int decrementAmount) {
		if(this.chestContents[slot] != null) {
			ItemStack decrementAmount1;
			if(this.chestContents[slot].stackSize <= decrementAmount) {
				decrementAmount1 = this.chestContents[slot];
				this.chestContents[slot] = null;
				return decrementAmount1;
			} else {
				decrementAmount1 = this.chestContents[slot].splitStack(decrementAmount);
				if(this.chestContents[slot].stackSize == 0) {
					this.chestContents[slot] = null;
				}

				return decrementAmount1;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.chestContents[slot] = stack;
		if(stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}

	}

	public String getInvName() {
		return "Chest";
	}

	public void readFromNBT(NBTTagCompound compoundTag) {
		super.readFromNBT(compoundTag);
		NBTTagList nBTTagList5 = compoundTag.getTagList("Items");
		this.chestContents = new ItemStack[27];

		for(int i2 = 0; i2 < nBTTagList5.tagCount(); ++i2) {
			NBTTagCompound nBTTagCompound3;
			int i4;
			if((i4 = (nBTTagCompound3 = (NBTTagCompound)nBTTagList5.tagAt(i2)).getByte("Slot") & 255) >= 0 && i4 < this.chestContents.length) {
				this.chestContents[i4] = new ItemStack(nBTTagCompound3);
			}
		}

	}

	public void writeToNBT(NBTTagCompound compoundTag) {
		super.writeToNBT(compoundTag);
		NBTTagList nBTTagList2 = new NBTTagList();

		for(int i3 = 0; i3 < this.chestContents.length; ++i3) {
			if(this.chestContents[i3] != null) {
				NBTTagCompound nBTTagCompound4;
				(nBTTagCompound4 = new NBTTagCompound()).setByte("Slot", (byte)i3);
				this.chestContents[i3].writeToNBT(nBTTagCompound4);
				nBTTagList2.setTag(nBTTagCompound4);
			}
		}

		compoundTag.setTag("Items", nBTTagList2);
	}

	public int getInventoryStackLimit() {
		return 64;
	}
}
