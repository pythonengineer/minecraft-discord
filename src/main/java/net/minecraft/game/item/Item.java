package net.minecraft.game.item;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public class Item {
    protected static EaglercraftRandom rand = new EaglercraftRandom();
    public static Item[] itemsList = new Item[1024];
    public static Item shovel;
    public static Item pickaxeSteel;
    public static Item axeSteel;
    public static Item flintSteel;
    public static Item arrow;
    public static Item coal;
    public static Item diamond;
    public static Item ingotIron;
    public static Item ingotGold;
    public static Item swordSteel;
    public static Item swordWood;
    public static Item shovelWood;
    public static Item pickaxeWood;
    public static Item axeWood;
    public static Item swordStone;
    public static Item shovelStone;
    public static Item pickaxeStone;
    public static Item axeStone;
    public static Item swordDiamond;
    public static Item shovelDiamond;
    public static Item pickaxeDiamond;
    public static Item axeDiamond;
    public static Item stick;
    public final int shiftedIndex;
    protected int maxStackSize = 100;
    protected int iconIndex;

    protected Item(int var1) {
        this.shiftedIndex = var1 + 256;
        itemsList[var1 + 256] = this;
        System.out.println("Setting items[" + (var1 + 256) + "] to " + this);
    }

    public final int getIconIndex() {
        return this.iconIndex;
    }

    public void onItemUse(ItemStack var1, World var2, int var3, int var4, int var5, int var6) {
    }

    public float getStrVsBlock(Block var1) {
        return 1.0F;
    }

    public boolean onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
        return false;
    }

    public final int getItemStackLimit() {
        return this.maxStackSize;
    }

    public boolean onPlaced(World var1, float var2, float var3, float var4) {
        return false;
    }

    public boolean shouldUseOnTouchEagler(ItemStack itemStack) {
        return false;
    }

    static {
        ItemSpade var10000 = new ItemSpade(0);
        byte var1 = 82;
        ItemSpade var0 = var10000;
        var0.iconIndex = var1;
        shovel = var0;
        ItemPickaxe var9 = new ItemPickaxe(1);
        var1 = 98;
        ItemPickaxe var2 = var9;
        var2.iconIndex = var1;
        pickaxeSteel = var2;
        ItemAxe var10 = new ItemAxe(2);
        var1 = 114;
        ItemAxe var3 = var10;
        var3.iconIndex = var1;
        axeSteel = var3;
        ItemFlintAndSteel var11 = new ItemFlintAndSteel(3);
        var1 = 5;
        ItemFlintAndSteel var4 = var11;
        var4.iconIndex = var1;
        flintSteel = var4;
        ItemFood var12 = new ItemFood(4, 4);
        var1 = 4;
        ItemFood var5 = var12;
        var5.iconIndex = var1;
        ItemBow var13 = new ItemBow(5);
        var1 = 21;
        ItemBow var6 = var13;
        var6.iconIndex = var1;
        Item var14 = new Item(6);
        var1 = 37;
        Item var7 = var14;
        var7.iconIndex = var1;
        arrow = var7;
        var14 = new Item(7);
        var1 = 7;
        var7 = var14;
        var7.iconIndex = var1;
        coal = var7;
        var14 = new Item(8);
        var1 = 55;
        var7 = var14;
        var7.iconIndex = var1;
        diamond = var7;
        var14 = new Item(9);
        var1 = 23;
        var7 = var14;
        var7.iconIndex = var1;
        ingotIron = var7;
        var14 = new Item(10);
        var1 = 39;
        var7 = var14;
        var7.iconIndex = var1;
        ingotGold = var7;
        ItemSword var15 = new ItemSword(11);
        var1 = 66;
        ItemSword var8 = var15;
        var8.iconIndex = var1;
        swordSteel = var8;
        var15 = new ItemSword(12);
        var1 = 64;
        var8 = var15;
        var8.iconIndex = var1;
        swordWood = var8;
        var10000 = new ItemSpade(13);
        var1 = 80;
        var0 = var10000;
        var0.iconIndex = var1;
        shovelWood = var0;
        var9 = new ItemPickaxe(14);
        var1 = 96;
        var2 = var9;
        var2.iconIndex = var1;
        pickaxeWood = var2;
        var10 = new ItemAxe(15);
        var1 = 112;
        var3 = var10;
        var3.iconIndex = var1;
        axeWood = var3;
        var15 = new ItemSword(16);
        var1 = 65;
        var8 = var15;
        var8.iconIndex = var1;
        swordStone = var8;
        var10000 = new ItemSpade(17);
        var1 = 81;
        var0 = var10000;
        var0.iconIndex = var1;
        shovelStone = var0;
        var9 = new ItemPickaxe(18);
        var1 = 97;
        var2 = var9;
        var2.iconIndex = var1;
        pickaxeStone = var2;
        var10 = new ItemAxe(19);
        var1 = 113;
        var3 = var10;
        var3.iconIndex = var1;
        axeStone = var3;
        var15 = new ItemSword(20);
        var1 = 67;
        var8 = var15;
        var8.iconIndex = var1;
        swordDiamond = var8;
        var10000 = new ItemSpade(21);
        var1 = 83;
        var0 = var10000;
        var0.iconIndex = var1;
        shovelDiamond = var0;
        var9 = new ItemPickaxe(22);
        var1 = 99;
        var2 = var9;
        var2.iconIndex = var1;
        pickaxeDiamond = var2;
        var10 = new ItemAxe(23);
        var1 = 115;
        var3 = var10;
        var3.iconIndex = var1;
        axeDiamond = var3;
        var14 = new Item(24);
        var1 = 53;
        var7 = var14;
        var7.iconIndex = var1;
        stick = var7;
    }
}
