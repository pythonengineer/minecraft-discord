package net.minecraft.game.world.block.tileentity;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

public final class TileEntityFurnace extends TileEntity implements IInventory {
	private ItemStack[] furnaceItemStacks = new ItemStack[3];
	private int furnaceBurnTime = 0;
	private int currentItemBurnTime = 0;
	private int furnaceCookTime = 0;

	public final int getSizeInventory() {
		return this.furnaceItemStacks.length;
	}

	public final ItemStack getStackInSlot(int var1) {
		return this.furnaceItemStacks[var1];
	}

	public final ItemStack decrStackSize(int var1, int var2) {
		if(this.furnaceItemStacks[var1] != null) {
			ItemStack var3;
			if(this.furnaceItemStacks[var1].stackSize <= var2) {
				var3 = this.furnaceItemStacks[var1];
				this.furnaceItemStacks[var1] = null;
				return var3;
			} else {
				var3 = this.furnaceItemStacks[var1].splitStack(var2);
				if(this.furnaceItemStacks[var1].stackSize == 0) {
					this.furnaceItemStacks[var1] = null;
				}

				return var3;
			}
		} else {
			return null;
		}
	}

	public final void setInventorySlotContents(int var1, ItemStack var2) {
		this.furnaceItemStacks[var1] = var2;
		if(var2 != null && var2.stackSize > 64) {
			var2.stackSize = 64;
		}

	}

	public final String getInvName() {
		return "Chest";
	}

	public final int getInventoryStackLimit() {
		return 64;
	}

    public final int getCookProgressScaled(int var1) {
        return this.furnaceCookTime * 24 / 200;
    }

    public final int getBurnTimeRemainingScaled(int var1) {
        return this.furnaceBurnTime * 12 / this.currentItemBurnTime;
    }

    public final boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    public final void updateEntity() {
        boolean var1 = this.furnaceBurnTime > 0;
        if(this.furnaceBurnTime > 0) {
            --this.furnaceBurnTime;
        }

        int var4;
        if(this.furnaceBurnTime == 0 && this.canSmelt()) {
            this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
            if(this.furnaceBurnTime > 0 && this.furnaceItemStacks[1] != null) {
                --this.furnaceItemStacks[1].stackSize;
                if(this.furnaceItemStacks[1].stackSize == 0) {
                    this.furnaceItemStacks[1] = null;
                }
            }
        }

        if(this.isBurning() && this.canSmelt()) {
            ++this.furnaceCookTime;
            if(this.furnaceCookTime == 200) {
                this.furnaceCookTime = 0;
                if(this.canSmelt()) {
                    int var3 = smeltItem(this.furnaceItemStacks[0].getItem().shiftedIndex);
                    if(this.furnaceItemStacks[2] == null) {
                        this.furnaceItemStacks[2] = new ItemStack(var3, 1);
                    } else if(this.furnaceItemStacks[2].itemID == var3) {
                        ++this.furnaceItemStacks[2].stackSize;
                    }

                    --this.furnaceItemStacks[0].stackSize;
                    if(this.furnaceItemStacks[0].stackSize <= 0) {
                        this.furnaceItemStacks[0] = null;
                    }
                }
            }
        } else {
            this.furnaceCookTime = 0;
        }

        if(var1 != this.furnaceBurnTime > 0) {
            boolean var10000 = this.furnaceBurnTime > 0;
            int var5 = this.zCoord;
            int var8 = this.xCoord;
            var4 = this.yCoord;
            World var10 = this.worldObj;
            boolean var9 = var10000;
            int var6 = var10.getBlockMetadata(var4, var8, var5);
            TileEntity var7 = var10.getBlockTileEntity(var4, var8, var5);
            if(var9) {
                var10.setBlockWithNotify(var4, var8, var5, Block.stoneOvenActive.blockID);
            } else {
                var10.setBlockWithNotify(var4, var8, var5, Block.stoneOvenIdle.blockID);
            }

            var10.setBlockMetadataWithNotify(var4, var8, var5, var6);
            var10.setBlockTileEntity(var4, var8, var5, var7);
        }

    }

    private boolean canSmelt() {
        if(this.furnaceItemStacks[0] == null) {
            return false;
        } else {
            int var1 = smeltItem(this.furnaceItemStacks[0].getItem().shiftedIndex);
            if(var1 < 0) {
                return false;
            } else if(this.furnaceItemStacks[2] == null) {
                return true;
            } else if(this.furnaceItemStacks[2].itemID != var1) {
                return false;
            } else {
                if(this.furnaceItemStacks[2].stackSize < 64) {
                    ItemStack var2 = this.furnaceItemStacks[2];
                    if(this.furnaceItemStacks[2].stackSize < var2.getItem().getItemStackLimit()) {
                        return true;
                    }
                }

                return this.furnaceItemStacks[2].stackSize < Item.itemsList[var1].getItemStackLimit();
            }
        }
    }

    public static int smeltItem(int var0) {
        return var0 == Block.oreIron.blockID ? Item.ingotIron.shiftedIndex : (var0 == Block.oreGold.blockID ? Item.ingotGold.shiftedIndex : (var0 == Block.oreDiamond.blockID ? Item.diamond.shiftedIndex : (var0 == Block.sand.blockID ? Block.glass.blockID : (var0 == Item.porkRaw.shiftedIndex ? Item.porkCooked.shiftedIndex : (var0 == Block.cobblestone.blockID ? Block.stone.blockID : -1)))));
    }

    private static int getItemBurnTime(ItemStack var0) {
        if(var0 == null) {
            return 0;
        } else {
            int var1 = var0.getItem().shiftedIndex;
            return var1 < 256 && Block.blocksList[var1].material == Material.wood ? 300 : (var1 == Item.stick.shiftedIndex ? 100 : (var1 == Item.coal.shiftedIndex ? 1600 : 0));
        }
    }

    public static boolean isItemFuel(ItemStack parItemStack) {
        return getItemBurnTime(parItemStack) > 0;
    }
}
