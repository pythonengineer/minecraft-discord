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
    public static Item striker;
    public static Item bow;
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
    public static Item bowlEmpty;
    public static Item bowlSoup;
    public static Item swordGold;
    public static Item shovelGold;
    public static Item pickaxeGold;
    public static Item axeGold;
    public static Item silk;
    public static Item feather;
    public static Item gunpowder;
    public static Item hoeWood;
    public static Item hoeStone;
    public static Item hoeSteel;
    public static Item hoeDiamond;
    public static Item hoeGold;
    public static Item seeds;
    public static Item wheat;
    public static Item bread;
    public final int shiftedIndex;
    protected int maxStackSize = 64;
    protected int maxDamage = 32;
    protected int iconIndex;

    protected Item(int var1) {
        this.shiftedIndex = var1 + 256;
        itemsList[var1 + 256] = this;
    }

    public final Item setIconIndex(int var1) {
        this.iconIndex = var1;
        return this;
    }

    public final int getIconIndex() {
        return this.iconIndex;
    }

    public boolean onItemUse(ItemStack var1, World var2, int var3, int var4, int var5, int var6) {
        return false;
    }

    public float getStrVsBlock(Block var1) {
        return 1.0F;
    }

    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
        return var1;
    }

    public final int getItemStackLimit() {
        return this.maxStackSize;
    }

    public boolean onPlaced(World var1, float var2, float var3, float var4) {
        return false;
    }

    public final int getMaxDamage() {
        return this.maxDamage;
    }

    public void hitEntity(ItemStack var1) {
    }

    public void onBlockDestroyed(ItemStack var1) {
    }

    public int getDamageVsEntity() {
        return 1;
    }

    public boolean canHarvestBlock(Block var1) {
        return false;
    }

    public boolean shouldUseOnTouchEagler(ItemStack itemStack) {
        return false;
    }

    static {
        ItemSpade var10000 = new ItemSpade(0, 2);
        byte var1 = 82;
        ItemSpade var0 = var10000;
        var0.iconIndex = var1;
        shovel = var0;
        ItemPickaxe var14 = new ItemPickaxe(1, 2);
        var1 = 98;
        ItemPickaxe var2 = var14;
        var2.iconIndex = var1;
        pickaxeSteel = var2;
        ItemAxe var15 = new ItemAxe(2, 2);
        var1 = 114;
        ItemAxe var3 = var15;
        var3.iconIndex = var1;
        axeSteel = var3;
        ItemFlintAndSteel var16 = new ItemFlintAndSteel(3);
        var1 = 5;
        ItemFlintAndSteel var4 = var16;
        var4.iconIndex = var1;
        striker = var4;
        ItemFood var17 = new ItemFood(4, 4);
        var1 = 4;
        ItemFood var5 = var17;
        var5.iconIndex = var1;
        ItemBow var18 = new ItemBow(5);
        var1 = 21;
        ItemBow var6 = var18;
        var6.iconIndex = var1;
        bow = var6;
        Item var19 = new Item(6);
        var1 = 37;
        Item var7 = var19;
        var7.iconIndex = var1;
        arrow = var7;
        var19 = new Item(7);
        var1 = 7;
        var7 = var19;
        var7.iconIndex = var1;
        coal = var7;
        var19 = new Item(8);
        var1 = 55;
        var7 = var19;
        var7.iconIndex = var1;
        diamond = var7;
        var19 = new Item(9);
        var1 = 23;
        var7 = var19;
        var7.iconIndex = var1;
        ingotIron = var7;
        var19 = new Item(10);
        var1 = 39;
        var7 = var19;
        var7.iconIndex = var1;
        ingotGold = var7;
        ItemSword var20 = new ItemSword(11, 2);
        var1 = 66;
        ItemSword var8 = var20;
        var8.iconIndex = var1;
        swordSteel = var8;
        var20 = new ItemSword(12, 0);
        var1 = 64;
        var8 = var20;
        var8.iconIndex = var1;
        swordWood = var8;
        var10000 = new ItemSpade(13, 0);
        var1 = 80;
        var0 = var10000;
        var0.iconIndex = var1;
        shovelWood = var0;
        var14 = new ItemPickaxe(14, 0);
        var1 = 96;
        var2 = var14;
        var2.iconIndex = var1;
        pickaxeWood = var2;
        var15 = new ItemAxe(15, 0);
        var1 = 112;
        var3 = var15;
        var3.iconIndex = var1;
        axeWood = var3;
        var20 = new ItemSword(16, 1);
        var1 = 65;
        var8 = var20;
        var8.iconIndex = var1;
        swordStone = var8;
        var10000 = new ItemSpade(17, 1);
        var1 = 81;
        var0 = var10000;
        var0.iconIndex = var1;
        shovelStone = var0;
        var14 = new ItemPickaxe(18, 1);
        var1 = 97;
        var2 = var14;
        var2.iconIndex = var1;
        pickaxeStone = var2;
        var15 = new ItemAxe(19, 1);
        var1 = 113;
        var3 = var15;
        var3.iconIndex = var1;
        axeStone = var3;
        var20 = new ItemSword(20, 3);
        var1 = 67;
        var8 = var20;
        var8.iconIndex = var1;
        swordDiamond = var8;
        var10000 = new ItemSpade(21, 3);
        var1 = 83;
        var0 = var10000;
        var0.iconIndex = var1;
        shovelDiamond = var0;
        var14 = new ItemPickaxe(22, 3);
        var1 = 99;
        var2 = var14;
        var2.iconIndex = var1;
        pickaxeDiamond = var2;
        var15 = new ItemAxe(23, 3);
        var1 = 115;
        var3 = var15;
        var3.iconIndex = var1;
        axeDiamond = var3;
        var19 = new Item(24);
        var1 = 53;
        var7 = var19;
        var7.iconIndex = var1;
        stick = var7;
        var19 = new Item(25);
        var1 = 71;
        var7 = var19;
        var7.iconIndex = var1;
        bowlEmpty = var7;
        ItemSoup var21 = new ItemSoup(26, 10);
        var1 = 72;
        ItemSoup var9 = var21;
        var9.iconIndex = var1;
        bowlSoup = var9;
        var20 = new ItemSword(27, 0);
        var1 = 68;
        var8 = var20;
        var8.iconIndex = var1;
        swordGold = var8;
        var10000 = new ItemSpade(28, 0);
        var1 = 84;
        var0 = var10000;
        var0.iconIndex = var1;
        shovelGold = var0;
        var14 = new ItemPickaxe(29, 0);
        var1 = 100;
        var2 = var14;
        var2.iconIndex = var1;
        pickaxeGold = var2;
        var15 = new ItemAxe(30, 0);
        var1 = 116;
        var3 = var15;
        var3.iconIndex = var1;
        axeGold = var3;
        var19 = new Item(31);
        var1 = 8;
        var7 = var19;
        var7.iconIndex = var1;
        silk = var7;
        var19 = new Item(32);
        var1 = 24;
        var7 = var19;
        var7.iconIndex = var1;
        feather = var7;
        var19 = new Item(33);
        var1 = 40;
        var7 = var19;
        var7.iconIndex = var1;
        gunpowder = var7;
        ItemHoe var22 = new ItemHoe(34, 0);
        short var11 = 128;
        ItemHoe var10 = var22;
        var10.iconIndex = var11;
        hoeWood = var10;
        var22 = new ItemHoe(35, 1);
        var11 = 129;
        var10 = var22;
        var10.iconIndex = var11;
        hoeStone = var10;
        var22 = new ItemHoe(36, 2);
        var11 = 130;
        var10 = var22;
        var10.iconIndex = var11;
        hoeSteel = var10;
        var22 = new ItemHoe(37, 3);
        var11 = 131;
        var10 = var22;
        var10.iconIndex = var11;
        hoeDiamond = var10;
        var22 = new ItemHoe(38, 4);
        var11 = 132;
        var10 = var22;
        var10.iconIndex = var11;
        hoeGold = var10;
        ItemSeeds var23 = new ItemSeeds(39, Block.crops.blockID);
        var1 = 9;
        ItemSeeds var12 = var23;
        var12.iconIndex = var1;
        seeds = var12;
        var19 = new Item(40);
        var1 = 25;
        var7 = var19;
        var7.iconIndex = var1;
        wheat = var7;
        var17 = new ItemFood(41, 5);
        var1 = 41;
        var5 = var17;
        var5.iconIndex = var1;
        bread = var5;
    }
}
