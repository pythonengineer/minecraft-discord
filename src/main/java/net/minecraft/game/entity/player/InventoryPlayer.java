package net.minecraft.game.entity.player;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;

import net.minecraft.game.IInventory;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

public class InventoryPlayer implements IInventory {
    public ItemStack[] mainInventory = new ItemStack[37];
	public ItemStack[] armorInventory = new ItemStack[4];
    public ItemStack[] craftingInventory = new ItemStack[4];
	public int currentItem = 0;
	private EntityPlayer player;
    public ItemStack draggedItemStack;
    public boolean inventoryChanged = false;

	public InventoryPlayer(EntityPlayer playerEntity) {
		this.player = playerEntity;
	}

	public ItemStack getCurrentItem() {
		return this.mainInventory[this.currentItem];
	}

	private int getInventorySlotContainItem(int itemID) {
		for(int i = 0; i < this.mainInventory.length; ++i) {
			if(this.mainInventory[i] != null && this.mainInventory[i].itemID == itemID) {
				return i;
			}
		}

		return -1;
	}

	private int storeItemStack(int itemID) {
		for(int i = 0; i < this.mainInventory.length; ++i) {
			if(this.mainInventory[i] != null && this.mainInventory[i].itemID == itemID && this.mainInventory[i].stackSize < this.mainInventory[i].getItem().getItemStackLimit() && this.mainInventory[i].stackSize < this.getInventoryStackLimit()) {
				return i;
			}
		}

		return -1;
	}

	public int getFirstEmptyStack() {
		for(int i = 0; i < this.mainInventory.length; ++i) {
			if(this.mainInventory[i] == null) {
				return i;
			}
		}

		return -1;
	}

	public void setCurrentItem(int itemID, boolean flag) {
		int slot = this.getInventorySlotContainItem(itemID);
		if(slot >= 0 && slot < 9) {
			this.currentItem = slot;
		}
	}

	public void changeCurrentItem(int direction) {
		if(direction > 0) {
			direction = 1;
		}

		if(direction < 0) {
			direction = -1;
		}

		for(this.currentItem -= direction; this.currentItem < 0; this.currentItem += 9) {
		}

		while(this.currentItem >= 9) {
			this.currentItem -= 9;
		}

	}

	private int storePartialItemStack(int itemID, int amount) {
		int slot = this.storeItemStack(itemID);
		if(slot < 0) {
			slot = this.getFirstEmptyStack();
		}

		if(slot < 0) {
			return amount;
		} else {
			if(this.mainInventory[slot] == null) {
				this.mainInventory[slot] = new ItemStack(itemID, 0);
			}

			int toStore = amount;
			if(amount > this.mainInventory[slot].getMaxStackSize() - this.mainInventory[slot].stackSize) {
				toStore = this.mainInventory[slot].getMaxStackSize() - this.mainInventory[slot].stackSize;
			}

			if(toStore > this.getInventoryStackLimit() - this.mainInventory[slot].stackSize) {
				toStore = this.getInventoryStackLimit() - this.mainInventory[slot].stackSize;
			}

			if(toStore == 0) {
				return amount;
			} else {
				amount -= toStore;
				this.mainInventory[slot].stackSize += toStore;
				this.mainInventory[slot].animationsToGo = 5;
				return amount;
			}
		}
	}

	public void decrementAnimations() {
		for(int i = 0; i < this.mainInventory.length; ++i) {
			if(this.mainInventory[i] != null && this.mainInventory[i].animationsToGo > 0) {
				--this.mainInventory[i].animationsToGo;
			}
		}

	}

	public boolean consumeInventoryItem(int itemID) {
		int slot = this.getInventorySlotContainItem(itemID);
		if(slot < 0) {
			return false;
		} else {
			if(--this.mainInventory[slot].stackSize <= 0) {
				this.mainInventory[slot] = null;
			}

			return true;
		}
	}

	public boolean addItemStackToInventory(ItemStack stack) {
		if(stack.itemDmg == 0) {
			stack.stackSize = this.storePartialItemStack(stack.itemID, stack.stackSize);
			if(stack.stackSize == 0) {
				return true;
			}
		}

		int slot = this.getFirstEmptyStack();
		if(slot >= 0) {
			this.mainInventory[slot] = stack;
			this.mainInventory[slot].animationsToGo = 5;
			return true;
		} else {
			return false;
		}
	}

	public ItemStack decrStackSize(int slot, int decrementAmount) {
		ItemStack[] itemStack3 = this.mainInventory;
		if(slot >= this.mainInventory.length) {
			itemStack3 = this.armorInventory;
			slot -= this.mainInventory.length;
		}

		if(itemStack3[slot] != null) {
			ItemStack decrementAmount1;
			if(itemStack3[slot].stackSize <= decrementAmount) {
				decrementAmount1 = itemStack3[slot];
				itemStack3[slot] = null;
				return decrementAmount1;
			} else {
				decrementAmount1 = itemStack3[slot].splitStack(decrementAmount);
				if(itemStack3[slot].stackSize == 0) {
					itemStack3[slot] = null;
				}

				return decrementAmount1;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int slot, ItemStack stack) {
		ItemStack[] itemStack3 = this.mainInventory;
        if(slot >= itemStack3.length) {
            slot -= itemStack3.length;
            itemStack3 = this.armorInventory;
        }

        if(slot >= itemStack3.length) {
            slot -= itemStack3.length;
            itemStack3 = this.craftingInventory;
        }

		itemStack3[slot] = stack;
	}

	public float getStrVsBlock(Block block) {
		float strength = 1.0F;
		if(this.mainInventory[this.currentItem] != null) {
			strength *= this.mainInventory[this.currentItem].getStrVsBlock(block);
		}

		return strength;
	}

	public NBTTagList writeToNBT(NBTTagList tagList) {
		int i;
		NBTTagCompound compound;
		for(i = 0; i < this.mainInventory.length; ++i) {
			if(this.mainInventory[i] != null) {
				compound = new NBTTagCompound();
				compound.setByte("Slot", (byte)i);
				this.mainInventory[i].writeToNBT(compound);
				tagList.setTag(compound);
			}
		}

		for(i = 0; i < this.armorInventory.length; ++i) {
			if(this.armorInventory[i] != null) {
				compound = new NBTTagCompound();
				compound.setByte("Slot", (byte)(i + 100));
				this.armorInventory[i].writeToNBT(compound);
				tagList.setTag(compound);
			}
		}

        for(i = 0; i < this.craftingInventory.length; ++i) {
            if(this.craftingInventory[i] != null) {
                compound = new NBTTagCompound();
                compound.setByte("Slot", (byte)(i + 80));
                this.craftingInventory[i].writeToNBT(compound);
                tagList.setTag(compound);
            }
        }

		return tagList;
	}

	public void readFromNBT(NBTTagList tagList) {
		this.mainInventory = new ItemStack[36];
		this.armorInventory = new ItemStack[4];
        this.craftingInventory = new ItemStack[4];

		for(int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound compound = (NBTTagCompound)tagList.tagAt(i);
			int slot = compound.getByte("Slot") & 255;
			if(slot >= 0 && slot < this.mainInventory.length) {
				this.mainInventory[slot] = new ItemStack(compound);
			}

            if(slot >= 80 && slot < this.craftingInventory.length + 80) {
                this.craftingInventory[slot - 80] = new ItemStack(compound);
            }

			if(slot >= 100 && slot < this.armorInventory.length + 100) {
				this.armorInventory[slot - 100] = new ItemStack(compound);
			}
		}

	}

	public int getSizeInventory() {
		return this.mainInventory.length + 4;
	}

	public ItemStack getStackInSlot(int slot) {
		ItemStack[] itemStack2 = this.mainInventory;
        if(slot >= itemStack2.length) {
            slot -= itemStack2.length;
            itemStack2 = this.armorInventory;
        }

        if(slot >= itemStack2.length) {
            slot -= itemStack2.length;
            itemStack2 = this.craftingInventory;
        }

		return itemStack2[slot];
	}

	public String getInvName() {
		return "Inventory";
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public int getDamageVsEntity(Entity entity) {
		ItemStack itemStack = this.getStackInSlot(this.currentItem);
		return itemStack != null ? itemStack.getItem().getDamageVsEntity(entity) : 1;
	}

	public boolean canHarvestBlock(Block block) {
        if(block.material != Material.rock && block.material != Material.iron && block.material != Material.craftedSnow && block.material != Material.snow) {
            return true;
		} else {
			ItemStack itemStack = this.getStackInSlot(this.currentItem);
			return itemStack != null ? itemStack.getItem().canHarvestBlock(block) : false;
		}
	}

	public ItemStack armorItemInSlot(int slot) {
		return this.armorInventory[slot];
	}

	public int getTotalArmorValue() {
		int i1 = 0;
		int i2 = 0;
		int i3 = 0;

		for(int i4 = 0; i4 < this.armorInventory.length; ++i4) {
			if(this.armorInventory[i4] != null && this.armorInventory[i4].getItem() instanceof ItemArmor) {
				int i5 = this.armorInventory[i4].getMaxDamage();
				int i6 = this.armorInventory[i4].itemDmg;
				int i7 = i5 - i6;
				i2 += i7;
				i3 += i5;
				int i8 = ((ItemArmor)this.armorInventory[i4].getItem()).damageReduceAmount;
				i1 += i8;
			}
		}

		if(i3 == 0) {
			return 0;
		} else {
			return (i1 - 1) * i2 / i3 + 1;
		}
	}

	public void damageArmor(int damage) {
		for(int i = 0; i < this.armorInventory.length; ++i) {
			if(this.armorInventory[i] != null && this.armorInventory[i].getItem() instanceof ItemArmor) {
				this.armorInventory[i].damageItem(damage);
				if(this.armorInventory[i].stackSize == 0) {
                    this.armorInventory[i].onItemDestroyedByUse(this.player);
					this.armorInventory[i] = null;
				}
			}
		}

	}

	public void dropAllItems() {
		int i;
		for(i = 0; i < this.mainInventory.length; ++i) {
			if(this.mainInventory[i] != null) {
				this.player.dropPlayerItemWithRandomChoice(this.mainInventory[i], true);
				this.mainInventory[i] = null;
			}
		}

		for(i = 0; i < this.armorInventory.length; ++i) {
			if(this.armorInventory[i] != null) {
				this.player.dropPlayerItemWithRandomChoice(this.armorInventory[i], true);
				this.armorInventory[i] = null;
			}
		}

	}

    public void onInventoryChanged() {
        this.inventoryChanged = true;
    }

    public boolean getInventoryEqual(InventoryPlayer inventoryPlayer1) {
        int i2;
        for(i2 = 0; i2 < this.mainInventory.length; ++i2) {
            if(!this.getItemStacksEqual(inventoryPlayer1.mainInventory[i2], this.mainInventory[i2])) {
                return false;
            }
        }

        for(i2 = 0; i2 < this.armorInventory.length; ++i2) {
            if(!this.getItemStacksEqual(inventoryPlayer1.armorInventory[i2], this.armorInventory[i2])) {
                return false;
            }
        }

        for(i2 = 0; i2 < this.craftingInventory.length; ++i2) {
            if(!this.getItemStacksEqual(inventoryPlayer1.craftingInventory[i2], this.craftingInventory[i2])) {
                return false;
            }
        }

        return true;
    }

    private boolean getItemStacksEqual(ItemStack itemStack1, ItemStack itemStack2) {
        return itemStack1 == null && itemStack2 == null ? true : (itemStack1 != null && itemStack2 != null ? itemStack1.itemID == itemStack2.itemID && itemStack1.stackSize == itemStack2.stackSize && itemStack1.itemDmg == itemStack2.itemDmg : false);
    }

    public InventoryPlayer copyInventory() {
        InventoryPlayer inventoryPlayer1 = new InventoryPlayer((EntityPlayer)null);

        int i2;
        for(i2 = 0; i2 < this.mainInventory.length; ++i2) {
            inventoryPlayer1.mainInventory[i2] = this.mainInventory[i2] != null ? this.mainInventory[i2].copy() : null;
        }

        for(i2 = 0; i2 < this.armorInventory.length; ++i2) {
            inventoryPlayer1.armorInventory[i2] = this.armorInventory[i2] != null ? this.armorInventory[i2].copy() : null;
        }

        for(i2 = 0; i2 < this.craftingInventory.length; ++i2) {
            inventoryPlayer1.craftingInventory[i2] = this.craftingInventory[i2] != null ? this.craftingInventory[i2].copy() : null;
        }

        return inventoryPlayer1;
    }
}
