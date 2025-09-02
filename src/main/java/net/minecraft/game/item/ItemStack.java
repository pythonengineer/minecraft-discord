package net.minecraft.game.item;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.game.level.block.Block;

public final class ItemStack {
    public int stackSize;
    public int animationsToGo;
    public int itemID;

    public ItemStack(Block var1) {
        this((Block)var1, 1);
    }

    public ItemStack(Block var1, int var2) {
        this(var1.blockID, var2);
    }

    public ItemStack(Item var1) {
        this((Item)var1, 1);
    }

    public ItemStack(Item var1, int var2) {
        this(var1.shiftedIndex, var2);
    }

    public ItemStack(int var1) {
        this(var1, 1);
    }

    public ItemStack(int var1, int var2) {
        this.stackSize = 0;
        this.itemID = var1;
        this.stackSize = var2;
    }

    public ItemStack(NBTTagCompound var1) {
        this.stackSize = 0;
        this.itemID = var1.getShort("id");
        this.stackSize = var1.getByte("Count");
    }

    public final ItemStack splitStack(int var1) {
        this.stackSize -= var1;
        return new ItemStack(this.itemID, var1);
    }

    public final Item getItem() {
        return Item.itemsList[this.itemID];
    }

    public final NBTTagCompound writeToNBT(NBTTagCompound var1) {
        var1.setShort("id", (short)this.itemID);
        var1.setByte("Count", (byte)this.stackSize);
        return var1;
    }
}
