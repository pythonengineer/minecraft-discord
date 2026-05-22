package net.minecraft.game.world.block.tileentity;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

public class TileEntityFurnace extends TileEntity implements IInventory {
	private ItemStack[] furnaceItemStacks = new ItemStack[3];
	private int furnaceBurnTime = 0;
	private int currentItemBurnTime = 0;
	private int furnaceCookTime = 0;

	public final int getSizeInventory() {
		return this.furnaceItemStacks.length;
	}

	public final ItemStack getStackInSlot(int slot) {
		return this.furnaceItemStacks[slot];
	}

	public final ItemStack decrStackSize(int slot, int decrementAmount) {
		if(this.furnaceItemStacks[slot] != null) {
			ItemStack decrementAmount1;
			if(this.furnaceItemStacks[slot].stackSize <= decrementAmount) {
				decrementAmount1 = this.furnaceItemStacks[slot];
				this.furnaceItemStacks[slot] = null;
				return decrementAmount1;
			} else {
				decrementAmount1 = this.furnaceItemStacks[slot].splitStack(decrementAmount);
				if(this.furnaceItemStacks[slot].stackSize == 0) {
					this.furnaceItemStacks[slot] = null;
				}

				return decrementAmount1;
			}
		} else {
			return null;
		}
	}

	public final void setInventorySlotContents(int slot, ItemStack stack) {
		this.furnaceItemStacks[slot] = stack;
		if(stack != null && stack.stackSize > 64) {
			stack.stackSize = 64;
		}

	}

	public final String getInvName() {
		return "Chest";
	}

	public final void readFromNBT(NBTTagCompound compoundTag) {
		super.readFromNBT(compoundTag);
		NBTTagList nBTTagList2 = compoundTag.getTagList("Items");
		this.furnaceItemStacks = new ItemStack[this.furnaceItemStacks.length];

		for(int i3 = 0; i3 < nBTTagList2.tagCount(); ++i3) {
			NBTTagCompound nBTTagCompound4;
			byte b5;
			if((b5 = (nBTTagCompound4 = (NBTTagCompound)nBTTagList2.tagAt(i3)).getByte("Slot")) >= 0 && b5 < this.furnaceItemStacks.length) {
				this.furnaceItemStacks[b5] = new ItemStack(nBTTagCompound4);
			}
		}

		this.furnaceBurnTime = compoundTag.getShort("BurnTime");
		this.furnaceCookTime = compoundTag.getShort("CookTime");
		this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
	}

	public final void writeToNBT(NBTTagCompound compoundTag) {
		super.writeToNBT(compoundTag);
		compoundTag.setShort("BurnTime", (short)this.furnaceBurnTime);
		compoundTag.setShort("CookTime", (short)this.furnaceCookTime);
		NBTTagList nBTTagList2 = new NBTTagList();

		for(int i3 = 0; i3 < this.furnaceItemStacks.length; ++i3) {
			if(this.furnaceItemStacks[i3] != null) {
				NBTTagCompound nBTTagCompound4;
				(nBTTagCompound4 = new NBTTagCompound()).setByte("Slot", (byte)i3);
				this.furnaceItemStacks[i3].writeToNBT(nBTTagCompound4);
				nBTTagList2.setTag(nBTTagCompound4);
			}
		}

		compoundTag.setTag("Items", nBTTagList2);
	}

	public final int getInventoryStackLimit() {
		return 64;
	}

	public final int getCookProgressScaled(int scale) {
		return this.furnaceCookTime * scale / 200;
	}

	public final int getBurnTimeRemainingScaled(int scale) {
        if(this.currentItemBurnTime == 0) {
            this.currentItemBurnTime = 200;
        }

		return this.furnaceBurnTime * scale / this.currentItemBurnTime;
	}

	public final boolean isBurning() {
		return this.furnaceBurnTime > 0;
	}

	public final void updateEntity() {
		boolean z1 = this.furnaceBurnTime > 0;
        boolean z2 = false;
		if(this.furnaceBurnTime > 0) {
			--this.furnaceBurnTime;
            z2 = true;
		}

		if(this.furnaceBurnTime == 0 && this.canSmelt()) {
			this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
            if(this.furnaceBurnTime > 0) {
                z2 = true;
                if(this.furnaceItemStacks[1] != null) {
                    --this.furnaceItemStacks[1].stackSize;
                    if(this.furnaceItemStacks[1].stackSize == 0) {
                        this.furnaceItemStacks[1] = null;
                    }
                }
            }
		}

		if(this.isBurning() && this.canSmelt()) {
			++this.furnaceCookTime;
			if(this.furnaceCookTime == 200) {
				this.furnaceCookTime = 0;
				if(this.canSmelt()) {
                    int i4 = getSmeltingResult(this.furnaceItemStacks[0].getItem().shiftedIndex);
                    if(this.furnaceItemStacks[2] == null) {
                        this.furnaceItemStacks[2] = new ItemStack(i4, 1);
                    } else if(this.furnaceItemStacks[2].itemID == i4) {
						++this.furnaceItemStacks[2].stackSize;
					}

					--this.furnaceItemStacks[0].stackSize;
					if(this.furnaceItemStacks[0].stackSize <= 0) {
						this.furnaceItemStacks[0] = null;
					}
				}

                z2 = true;
			}
		} else {
			this.furnaceCookTime = 0;
		}

        if(z1 != this.furnaceBurnTime > 0) {
            z2 = true;
            boolean z10000 = this.furnaceBurnTime > 0;
            int i6 = this.zCoord;
            int i5 = this.yCoord;
            int i9 = this.xCoord;
            World world10 = this.worldObj;
            boolean z3 = z10000;
            int i7 = world10.getBlockMetadata(i9, i5, i6);
            TileEntity tileEntity8 = world10.getBlockTileEntity(i9, i5, i6);
            if(z3) {
                world10.setBlockWithNotify(i9, i5, i6, Block.stoneOvenActive.blockID);
            } else {
                world10.setBlockWithNotify(i9, i5, i6, Block.stoneOvenIdle.blockID);
            }

            world10.setBlockMetadata(i9, i5, i6, i7);
            world10.setBlockTileEntity(i9, i5, i6, tileEntity8);
        }

        if(z2) {
            this.worldObj.updateTileEntityChunkAndDoNothing(this.xCoord, this.yCoord, this.zCoord);
        }

	}

	private boolean canSmelt() {
		int i1;
		ItemStack itemStack2;
		return this.furnaceItemStacks[0] == null ? false : ((i1 = getSmeltingResult(this.furnaceItemStacks[0].getItem().shiftedIndex)) < 0 ? false : (this.furnaceItemStacks[2] == null ? true : (this.furnaceItemStacks[2].itemID != i1 ? false : (this.furnaceItemStacks[2].stackSize < 64 && this.furnaceItemStacks[2].stackSize < (itemStack2 = this.furnaceItemStacks[2]).getItem().getItemStackLimit() ? true : this.furnaceItemStacks[2].stackSize < Item.itemsList[i1].getItemStackLimit()))));
	}

	public static int getSmeltingResult(int shiftedIndex) {
		return shiftedIndex == Block.oreIron.blockID ? Item.ingotIron.shiftedIndex : (shiftedIndex == Block.oreGold.blockID ? Item.ingotGold.shiftedIndex : (shiftedIndex == Block.oreDiamond.blockID ? Item.diamond.shiftedIndex : (shiftedIndex == Block.sand.blockID ? Block.glass.blockID : (shiftedIndex == Item.porkRaw.shiftedIndex ? Item.porkCooked.shiftedIndex : (shiftedIndex == Block.cobblestone.blockID ? Block.stone.blockID : -1)))));
	}

	private static int getItemBurnTime(ItemStack stack) {
		int stack1;
		return stack == null ? 0 : ((stack1 = stack.getItem().shiftedIndex) < 256 && Block.blocksList[stack1].blockMaterial == Material.wood ? 300 : (stack1 == Item.stick.shiftedIndex ? 100 : (stack1 == Item.coal.shiftedIndex ? 1600 : 0)));
	}

	public final void onInventoryChanged() {
		this.worldObj.updateTileEntityChunkAndDoNothing(this.xCoord, this.yCoord, this.zCoord);
	}

    public static boolean isItemFuel(ItemStack parItemStack) {
        return getItemBurnTime(parItemStack) > 0;
    }
}