package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public class Block {
    private static StepSound soundPowderFootstep = new StepSound("stone", 1.0F, 1.0F);
    private static StepSound soundWoodFootstep = new StepSound("wood", 1.0F, 1.0F);
    private static StepSound soundGravelFootstep = new StepSound("gravel", 1.0F, 1.0F);
    private static StepSound soundGrassFootstep = new StepSound("grass", 1.0F, 1.0F);
    private static StepSound soundStoneFootstep = new StepSound("stone", 1.0F, 1.0F);
    private static StepSound soundMetalFootstep = new StepSound("stone", 1.0F, 1.5F);
    public static final Block[] blocksList = new Block[256];
    public static final boolean[] tickOnLoad = new boolean[256];
    public static final boolean[] opaqueCubeLookup = new boolean[256];
    public static final int[] lightOpacity = new int[256];
    private static boolean[] canBlockGrass = new boolean[256];
    public static final boolean[] isBlockFluid = new boolean[256];
    public static final int[] lightValue = new int[256];
    public static final Block stone;
    public static final Block grass;
    public static final Block dirt;
    public static final Block cobblestone;
    public static final Block planks;
    public static final Block sapling;
    public static final Block bedrock;
    public static final Block waterMoving;
    public static final Block waterStill;
    public static final Block lavaMoving;
    public static final Block lavaStill;
    public static final Block sand;
    public static final Block gravel;
    public static final Block oreGold;
    public static final Block oreIron;
    public static final Block oreCoal;
    public static final Block wood;
    public static final Block leaves;
    public static final Block sponge;
    public static final Block glass;
    public static final Block clothRed;
    public static final Block clothOrange;
    public static final Block clothYellow;
    public static final Block clothChartreuse;
    public static final Block clothGreen;
    public static final Block clothSpringGreen;
    public static final Block clothCyan;
    public static final Block clothCapri;
    public static final Block clothUltramarine;
    public static final Block clothViolet;
    public static final Block clothPurple;
    public static final Block clothMagenta;
    public static final Block clothRose;
    public static final Block clothDarkGray;
    public static final Block clothGray;
    public static final Block clothWhite;
    public static final Block plantYellow;
    public static final Block plantRed;
    public static final Block mushroomBrown;
    public static final Block mushroomRed;
    public static final Block blockGold;
    public static final Block blockSteel;
    public static final Block stairDouble;
    public static final Block stairSingle;
    public static final Block brick;
    public static final Block tnt;
    public static final Block bookShelf;
    public static final Block cobblestoneMossy;
    public static final Block obsidian;
    public static final Block torch;
    public static final BlockFire fire;
    public int blockIndexInTexture;
    public final int blockID;
    protected float hardness;
    protected boolean canExplode;
    public float minX;
    public float minY;
    public float minZ;
    public float maxX;
    public float maxY;
    public float maxZ;
    public StepSound stepSound;
    public float blockParticleGravity;

    protected Block(int var1) {
        this.canExplode = true;
        this.stepSound = soundPowderFootstep;
        this.blockParticleGravity = 1.0F;
        blocksList[var1] = this;
        this.blockID = var1;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        opaqueCubeLookup[var1] = this.isOpaqueCube();
        lightOpacity[var1] = this.isOpaqueCube() ? 255 : 0;
        canBlockGrass[var1] = this.renderAsNormalBlock();
        isBlockFluid[var1] = false;
    }

    protected Block setLightOpacity(int var1) {
        lightOpacity[this.blockID] = var1;
        return this;
    }

    protected Block setLightValue(float var1) {
        lightValue[this.blockID] = (int)(15.0F * var1);
        return this;
    }

    public boolean renderAsNormalBlock() {
        return true;
    }

    public int getRenderType() {
        return 0;
    }

    protected final void setTickOnLoad(boolean var1) {
        tickOnLoad[this.blockID] = var1;
    }

    protected final void setBlockBounds(float var1, float var2, float var3, float var4, float var5, float var6) {
        this.minX = var1;
        this.minY = var2;
        this.minZ = var3;
        this.maxX = var4;
        this.maxY = var5;
        this.maxZ = var6;
    }

    protected Block(int var1, int var2) {
        this(var1);
        this.blockIndexInTexture = var2;
    }

    public float getBlockBrightness(World var1, int var2, int var3, int var4) {
        return var1.getBlockLightValue(var2, var3, var4);
    }

    public boolean shouldSideBeRendered(World var1, int var2, int var3, int var4, int var5) {
        return !var1.isBlockNormalCube(var2, var3, var4);
    }

    public int getBlockTexture(int var1) {
        return this.blockIndexInTexture;
    }

    public final AxisAlignedBB getSelectedBoundingBoxFromPool(int var1, int var2, int var3) {
        return new AxisAlignedBB((float)var1 + this.minX, (float)var2 + this.minY, (float)var3 + this.minZ, (float)var1 + this.maxX, (float)var2 + this.maxY, (float)var3 + this.maxZ);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
        return new AxisAlignedBB((float)var1 + this.minX, (float)var2 + this.minY, (float)var3 + this.minZ, (float)var1 + this.maxX, (float)var2 + this.maxY, (float)var3 + this.maxZ);
    }

    public boolean isOpaqueCube() {
        return true;
    }

    public boolean isCollidable() {
        return true;
    }

    public void updateTick(World var1, int var2, int var3, int var4, EaglercraftRandom var5) {
    }

    public void onBlockDestroyedByPlayer(World var1, int var2, int var3, int var4) {
    }

    public Material getBlockMaterial() {
        return Material.air;
    }

    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
    }

    public int tickRate() {
        return 5;
    }

    public void onBlockAdded(World var1, int var2, int var3, int var4) {
    }

    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
    }

    public int quantityDropped(EaglercraftRandom var1) {
        return 1;
    }

    public int idDropped() {
        return this.blockID;
    }

    public final int blockStrength(EntityPlayer var1) {
        return (int)(this.hardness / var1.canHarvestBlock(this) * 30.0F);
    }

    public void dropBlockAsItem(World var1, int var2, int var3, int var4) {
        this.dropBlockAsItemWithChance(var1, var2, var3, var4, 1.0F);
    }

    public void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, float var5) {
        int var6 = this.quantityDropped(var1.random);

        for(int var7 = 0; var7 < var6; ++var7) {
            if(var1.random.nextFloat() <= var5) {
                float var8 = var1.random.nextFloat() * 0.7F + 0.15F;
                float var9 = var1.random.nextFloat() * 0.7F + 0.15F;
                float var10 = var1.random.nextFloat() * 0.7F + 0.15F;
                EntityItem var11 = new EntityItem(var1, (float)var2 + var8, (float)var3 + var9, (float)var4 + var10, new ItemStack(this.idDropped()));
                var11.delayBeforeCanPickup = 20;
                var1.releaseEntitySkin(var11);
            }
        }

    }

    public final boolean getCanExplode() {
        return this.canExplode;
    }

    public final MovingObjectPosition collisionRayTrace(int var1, int var2, int var3, Vec3D var4, Vec3D var5) {
        var4 = var4.addVector((float)(-var1), (float)(-var2), (float)(-var3));
        var5 = var5.addVector((float)(-var1), (float)(-var2), (float)(-var3));
        Vec3D var6 = var4.getIntermediateWithXValue(var5, this.minX);
        Vec3D var7 = var4.getIntermediateWithXValue(var5, this.maxX);
        Vec3D var8 = var4.getIntermediateWithYValue(var5, this.minY);
        Vec3D var9 = var4.getIntermediateWithYValue(var5, this.maxY);
        Vec3D var10 = var4.getIntermediateWithZValue(var5, this.minZ);
        var5 = var4.getIntermediateWithZValue(var5, this.maxZ);
        if(!this.isVecInsideYZBounds(var6)) {
            var6 = null;
        }

        if(!this.isVecInsideYZBounds(var7)) {
            var7 = null;
        }

        if(!this.isVecInsideXZBounds(var8)) {
            var8 = null;
        }

        if(!this.isVecInsideXZBounds(var9)) {
            var9 = null;
        }

        if(!this.isVecInsideXYBounds(var10)) {
            var10 = null;
        }

        if(!this.isVecInsideXYBounds(var5)) {
            var5 = null;
        }

        Vec3D var11 = null;
        if(var6 != null) {
            var11 = var6;
        }

        if(var7 != null && (var11 == null || var4.distanceTo(var7) < var4.distanceTo(var11))) {
            var11 = var7;
        }

        if(var8 != null && (var11 == null || var4.distanceTo(var8) < var4.distanceTo(var11))) {
            var11 = var8;
        }

        if(var9 != null && (var11 == null || var4.distanceTo(var9) < var4.distanceTo(var11))) {
            var11 = var9;
        }

        if(var10 != null && (var11 == null || var4.distanceTo(var10) < var4.distanceTo(var11))) {
            var11 = var10;
        }

        if(var5 != null && (var11 == null || var4.distanceTo(var5) < var4.distanceTo(var11))) {
            var11 = var5;
        }

        if(var11 == null) {
            return null;
        } else {
            byte var12 = -1;
            if(var11 == var6) {
                var12 = 4;
            }

            if(var11 == var7) {
                var12 = 5;
            }

            if(var11 == var8) {
                var12 = 0;
            }

            if(var11 == var9) {
                var12 = 1;
            }

            if(var11 == var10) {
                var12 = 2;
            }

            if(var11 == var5) {
                var12 = 3;
            }

            return new MovingObjectPosition(var1, var2, var3, var12, var11.addVector((float)var1, (float)var2, (float)var3));
        }
    }

    private boolean isVecInsideYZBounds(Vec3D var1) {
        return var1 == null ? false : var1.yCoord >= this.minY && var1.yCoord <= this.maxY && var1.zCoord >= this.minZ && var1.zCoord <= this.maxZ;
    }

    private boolean isVecInsideXZBounds(Vec3D var1) {
        return var1 == null ? false : var1.xCoord >= this.minX && var1.xCoord <= this.maxX && var1.zCoord >= this.minZ && var1.zCoord <= this.maxZ;
    }

    private boolean isVecInsideXYBounds(Vec3D var1) {
        return var1 == null ? false : var1.xCoord >= this.minX && var1.xCoord <= this.maxX && var1.yCoord >= this.minY && var1.yCoord <= this.maxY;
    }

    public void onBlockDestroyedByExplosion(World var1, int var2, int var3, int var4) {
    }

    public int getRenderBlockPass() {
        return 0;
    }

    static {
        BlockStone var10000 = new BlockStone(1, 1);
        float var1 = 1.0F;
        BlockStone var0 = var10000;
        var0.hardness = var1;
        boolean var5 = false;
        var0.canExplode = false;
        StepSound var6 = soundStoneFootstep;
        var0.stepSound = var6;
        stone = var0;
        BlockGrass var22 = new BlockGrass(2);
        var1 = 0.6F;
        BlockGrass var2 = var22;
        var2.hardness = var1;
        var6 = soundGrassFootstep;
        var2.stepSound = var6;
        grass = var2;
        BlockDirt var24 = new BlockDirt(3, 2);
        var1 = 0.5F;
        BlockDirt var3 = var24;
        var3.hardness = var1;
        var6 = soundGravelFootstep;
        var3.stepSound = var6;
        dirt = var3;
        Block var25 = new Block(4, 16);
        var1 = 1.5F;
        Block var4 = var25;
        var4.hardness = var1;
        var5 = false;
        var4.canExplode = false;
        var6 = soundStoneFootstep;
        var4.stepSound = var6;
        cobblestone = var4;
        var25 = new Block(5, 4);
        var1 = 1.5F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundWoodFootstep;
        var4.stepSound = var6;
        planks = var4;
        BlockSapling var27 = new BlockSapling(6, 15);
        var1 = 0.0F;
        BlockSapling var7 = var27;
        var7.hardness = var1;
        var6 = soundGrassFootstep;
        var7.stepSound = var6;
        sapling = var7;
        var25 = new Block(7, 17);
        var1 = 999.0F;
        var4 = var25;
        var4.hardness = var1;
        var5 = false;
        var4.canExplode = false;
        var6 = soundStoneFootstep;
        var4.stepSound = var6;
        bedrock = var4;
        BlockFlowing var28 = new BlockFlowing(8, Material.water);
        var1 = 100.0F;
        BlockFlowing var8 = var28;
        var8.hardness = var1;
        waterMoving = var8.setLightOpacity(2);
        BlockStationary var29 = new BlockStationary(9, Material.water);
        var1 = 100.0F;
        BlockStationary var9 = var29;
        var9.hardness = var1;
        waterStill = var9.setLightOpacity(2);
        var28 = new BlockFlowing(10, Material.lava);
        var1 = 0.0F;
        var8 = var28;
        var8.hardness = var1;
        lavaMoving = var8.setLightValue(1.0F).setLightOpacity(255);
        var29 = new BlockStationary(11, Material.lava);
        var1 = 100.0F;
        var9 = var29;
        var9.hardness = var1;
        lavaStill = var9.setLightValue(1.0F).setLightOpacity(255);
        BlockSand var30 = new BlockSand(12, 18);
        var1 = 0.5F;
        BlockSand var10 = var30;
        var10.hardness = var1;
        var6 = soundGravelFootstep;
        var10.stepSound = var6;
        sand = var10;
        var30 = new BlockSand(13, 19);
        var1 = 0.6F;
        var10 = var30;
        var10.hardness = var1;
        var6 = soundGravelFootstep;
        var10.stepSound = var6;
        gravel = var10;
        BlockOre var31 = new BlockOre(14, 32);
        var1 = 3.0F;
        BlockOre var11 = var31;
        var11.hardness = var1;
        var5 = false;
        var11.canExplode = false;
        var6 = soundStoneFootstep;
        var11.stepSound = var6;
        oreGold = var11;
        var31 = new BlockOre(15, 33);
        var1 = 3.0F;
        var11 = var31;
        var11.hardness = var1;
        var5 = false;
        var11.canExplode = false;
        var6 = soundStoneFootstep;
        var11.stepSound = var6;
        oreIron = var11;
        var31 = new BlockOre(16, 34);
        var1 = 3.0F;
        var11 = var31;
        var11.hardness = var1;
        var5 = false;
        var11.canExplode = false;
        var6 = soundStoneFootstep;
        var11.stepSound = var6;
        oreCoal = var11;
        BlockLog var32 = new BlockLog(17);
        var1 = 2.5F;
        BlockLog var12 = var32;
        var12.hardness = var1;
        var6 = soundWoodFootstep;
        var12.stepSound = var6;
        wood = var12;
        BlockLeaves var33 = new BlockLeaves(18, 22);
        var1 = 0.2F;
        BlockLeaves var13 = var33;
        var13.hardness = var1;
        var25 = var13.setLightOpacity(1);
        var6 = soundGrassFootstep;
        var4 = var25;
        var4.stepSound = var6;
        leaves = var4;
        BlockSponge var34 = new BlockSponge(19);
        var1 = 0.6F;
        BlockSponge var14 = var34;
        var14.hardness = var1;
        var6 = soundGrassFootstep;
        var14.stepSound = var6;
        sponge = var14;
        BlockGlass var35 = new BlockGlass(20, 49, false);
        var1 = 0.3F;
        BlockGlass var15 = var35;
        var15.hardness = var1;
        var6 = soundMetalFootstep;
        var15.stepSound = var6;
        glass = var15;
        var25 = new Block(21, 64);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothRed = var4;
        var25 = new Block(22, 65);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothOrange = var4;
        var25 = new Block(23, 66);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothYellow = var4;
        var25 = new Block(24, 67);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothChartreuse = var4;
        var25 = new Block(25, 68);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothGreen = var4;
        var25 = new Block(26, 69);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothSpringGreen = var4;
        var25 = new Block(27, 70);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothCyan = var4;
        var25 = new Block(28, 71);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothCapri = var4;
        var25 = new Block(29, 72);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothUltramarine = var4;
        var25 = new Block(30, 73);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothViolet = var4;
        var25 = new Block(31, 74);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothPurple = var4;
        var25 = new Block(32, 75);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothMagenta = var4;
        var25 = new Block(33, 76);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothRose = var4;
        var25 = new Block(34, 77);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothDarkGray = var4;
        var25 = new Block(35, 78);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothGray = var4;
        var25 = new Block(36, 79);
        var1 = 0.8F;
        var4 = var25;
        var4.hardness = var1;
        var6 = soundGrassFootstep;
        var4.stepSound = var6;
        clothWhite = var4;
        BlockFlower var36 = new BlockFlower(37, 13);
        var1 = 0.0F;
        BlockFlower var16 = var36;
        var16.hardness = var1;
        var6 = soundGrassFootstep;
        var16.stepSound = var6;
        plantYellow = var16;
        var36 = new BlockFlower(38, 12);
        var1 = 0.0F;
        var16 = var36;
        var16.hardness = var1;
        var6 = soundGrassFootstep;
        var16.stepSound = var6;
        plantRed = var16;
        BlockMushroom var37 = new BlockMushroom(39, 29);
        var1 = 0.0F;
        BlockMushroom var17 = var37;
        var17.hardness = var1;
        var6 = soundGrassFootstep;
        var17.stepSound = var6;
        mushroomBrown = var17;
        var37 = new BlockMushroom(40, 28);
        var1 = 0.0F;
        var17 = var37;
        var17.hardness = var1;
        var6 = soundGrassFootstep;
        var17.stepSound = var6;
        mushroomRed = var17;
        BlockOreBlock var38 = new BlockOreBlock(41, 40);
        var1 = 3.0F;
        BlockOreBlock var18 = var38;
        var18.hardness = var1;
        var5 = false;
        var18.canExplode = false;
        var6 = soundMetalFootstep;
        var18.stepSound = var6;
        blockGold = var18;
        var38 = new BlockOreBlock(42, 39);
        var1 = 5.0F;
        var18 = var38;
        var18.hardness = var1;
        var5 = false;
        var18.canExplode = false;
        var6 = soundMetalFootstep;
        var18.stepSound = var6;
        blockSteel = var18;
        BlockStep var39 = new BlockStep(43, true);
        var1 = 2.0F;
        BlockStep var19 = var39;
        var19.hardness = var1;
        var5 = false;
        var19.canExplode = false;
        var6 = soundStoneFootstep;
        var19.stepSound = var6;
        stairDouble = var19;
        var39 = new BlockStep(44, false);
        var1 = 2.0F;
        var19 = var39;
        var19.hardness = var1;
        var5 = false;
        var19.canExplode = false;
        var6 = soundStoneFootstep;
        var19.stepSound = var6;
        stairSingle = var19;
        var25 = new Block(45, 7);
        var1 = 2.0F;
        var4 = var25;
        var4.hardness = var1;
        var5 = false;
        var4.canExplode = false;
        var6 = soundStoneFootstep;
        var4.stepSound = var6;
        brick = var4;
        BlockTNT var40 = new BlockTNT(46, 8);
        var1 = 0.0F;
        BlockTNT var20 = var40;
        var20.hardness = var1;
        var6 = soundGrassFootstep;
        var20.stepSound = var6;
        tnt = var20;
        BlockBookshelf var41 = new BlockBookshelf(47, 35);
        var1 = 1.5F;
        BlockBookshelf var21 = var41;
        var21.hardness = var1;
        var6 = soundWoodFootstep;
        var21.stepSound = var6;
        bookShelf = var21;
        var25 = new Block(48, 36);
        var1 = 1.0F;
        var4 = var25;
        var4.hardness = var1;
        var5 = false;
        var4.canExplode = false;
        var6 = soundStoneFootstep;
        var4.stepSound = var6;
        cobblestoneMossy = var4;
        var10000 = new BlockStone(49, 37);
        var1 = 10.0F;
        var0 = var10000;
        var0.hardness = var1;
        var5 = false;
        var0.canExplode = false;
        var6 = soundStoneFootstep;
        var0.stepSound = var6;
        obsidian = var0;
        BlockTorch var42 = new BlockTorch(50, 80);
        var1 = 0.0F;
        BlockTorch var23 = var42;
        var23.hardness = var1;
        var25 = var23.setLightValue(0.9F);
        var6 = soundWoodFootstep;
        var4 = var25;
        var4.stepSound = var6;
        torch = var4;
        BlockFire var43 = new BlockFire(51, 31);
        var1 = 0.0F;
        BlockFire var26 = var43;
        var26.hardness = var1;
        var25 = var26.setLightValue(1.0F);
        var6 = soundWoodFootstep;
        var4 = var25;
        var4.stepSound = var6;
        fire = (BlockFire)var4;
    }
}
