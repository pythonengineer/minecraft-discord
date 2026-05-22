package net.minecraft.game.world.block;

import java.util.ArrayList;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemBlock;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.tileentity.TileEntitySign;
import net.minecraft.game.world.material.Material;

public class Block {
    private static StepSound soundPowderFootstep = new StepSound("stone", 1.0F, 1.0F);
    private static StepSound soundWoodFootstep = new StepSound("wood", 1.0F, 1.0F);
    private static StepSound soundGravelFootstep = new StepSound("gravel", 1.0F, 1.0F);
    private static StepSound soundGrassFootstep = new StepSound("grass", 1.0F, 1.0F);
    private static StepSound soundStoneFootstep = new StepSound("stone", 1.0F, 1.0F);
    private static StepSound soundMetalFootstep = new StepSound("stone", 1.0F, 1.5F);
    private static StepSound soundGlassFootstep = new StepSoundGlass("stone", 1.0F, 1.0F);
    private static StepSound soundClothFootstep = new StepSound("cloth", 1.0F, 1.0F);
    private static StepSound soundSandFootstep = new StepSoundSand("sand", 1.0F, 1.0F);
    public static final Block[] blocksList = new Block[256];
    public static final boolean[] tickOnLoad = new boolean[256];
    public static final boolean[] opaqueCubeLookup = new boolean[256];
    public static final int[] lightOpacity = new int[256];
    private static boolean[] canBlockGrass = new boolean[256];
    public static final int[] lightValue = new int[256];
    public static final Block stone;
    public static final BlockGrass grass;
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
    public static final BlockLeaves leaves;
    public static final Block sponge;
    public static final Block glass;
    public static final Block cloth;
    public static final BlockFlower plantYellow;
    public static final BlockFlower plantRed;
    public static final BlockFlower mushroomBrown;
    public static final BlockFlower mushroomRed;
    public static final Block blockGold;
    public static final Block blockSteel;
    public static final Block stairDouble;
    public static final Block stairSingle;
    public static final Block brick;
    public static final Block tnt;
    public static final Block bookshelf;
    public static final Block cobblestoneMossy;
    public static final Block obsidian;
    public static final Block torch;
    public static final BlockFire fire;
    public static final Block mobSpawner;
    public static final Block stairCompactWood;
    public static final Block chest;
    public static final Block cog;
    public static final Block oreDiamond;
    public static final Block blockDiamond;
    public static final Block workbench;
    public static final Block crops;
    public static final Block tilledField;
    public static final Block stoneOvenIdle;
    public static final Block stoneOvenActive;
    public static final Block signStanding;
    public static final Block doorWood;
    public static final Block ladder;
    public static final Block minecartTrack;
    public static final Block stairCompactStone;
    public int blockIndexInTexture;
    public final int blockID;
    protected float blockHardness;
    protected float blockResistance;
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;
    public StepSound stepSound;
    public float blockParticleGravity;
    public final Material blockMaterial;

    protected Block(int blockID, Material material) {
        this.stepSound = soundPowderFootstep;
        this.blockParticleGravity = 1.0F;
        if(blocksList[blockID] != null) {
            throw new IllegalArgumentException("Slot " + blockID + " is already occupied by " + blocksList[blockID] + " when adding " + this);
        } else {
            this.blockMaterial = material;
            blocksList[blockID] = this;
            this.blockID = blockID;
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            opaqueCubeLookup[blockID] = this.isOpaqueCube();
            lightOpacity[blockID] = this.isOpaqueCube() ? 255 : 0;
            canBlockGrass[blockID] = false;
        }
    }

    protected Block(int blockID, int textureIndex, Material material) {
        this(blockID, material);
        this.blockIndexInTexture = textureIndex;
    }

    protected final Block setStepSound(StepSound soundName) {
        this.stepSound = soundName;
        return this;
    }

    protected final Block setLightOpacity(int opacity) {
        lightOpacity[this.blockID] = opacity;
        return this;
    }

    private Block setLightValue(float light) {
        lightValue[this.blockID] = (int)(15.0F * light);
        return this;
    }

    protected final Block setResistance(float resistance) {
        this.blockResistance = resistance * 3.0F;
        return this;
    }

    public boolean renderAsNormalBlock() {
        return true;
    }

    public int getRenderType() {
        return 0;
    }

    protected final Block setHardness(float hardness) {
        this.blockHardness = hardness;
        if(this.blockResistance < hardness * 5.0F) {
            this.blockResistance = hardness * 5.0F;
        }

        return this;
    }

    protected final void setTickOnLoad(boolean ticksOnLoad) {
        tickOnLoad[this.blockID] = ticksOnLoad;
    }

    public final void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = (double)minX;
        this.minY = (double)minY;
        this.minZ = (double)minZ;
        this.maxX = (double)maxX;
        this.maxY = (double)maxY;
        this.maxZ = (double)maxZ;
    }

    public float getBlockBrightness(IBlockAccess blockAccess, int x, int y, int z) {
        return blockAccess.getBrightness(x, y, z);
    }

    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int metadata) {
        return metadata == 0 && this.minY > 0.0D ? true : (metadata == 1 && this.maxY < 1.0D ? true : (metadata == 2 && this.minZ > 0.0D ? true : (metadata == 3 && this.maxZ < 1.0D ? true : (metadata == 4 && this.minX > 0.0D ? true : (metadata == 5 && this.maxX < 1.0D ? true : !blockAccess.isBlockNormalCube(x, y, z))))));
    }

    public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return this.getBlockTextureFromSideAndMetadata(side, blockAccess.getBlockMetadata(x, y, z));
    }

    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        return this.getBlockTextureFromSide(side);
    }

    public int getBlockTextureFromSide(int side) {
        return this.blockIndexInTexture;
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBoxFromPool((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ);
    }

    public void getCollidingBoundingBoxes(World world, int x, int y, int z, AxisAlignedBB aabb, ArrayList arrayList) {
        AxisAlignedBB world1;
        if((world1 = this.getCollisionBoundingBoxFromPool(world, x, y, z)) != null && aabb.intersectsWith(world1)) {
            arrayList.add(world1);
        }

    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBoxFromPool((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ);
    }

    public boolean isOpaqueCube() {
        return true;
    }

    public boolean canCollideCheck(int metadata, boolean flag) {
        return this.isCollidable();
    }

    public boolean isCollidable() {
        return true;
    }

    public void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
    }

    public void randomDisplayTick(World world, int x, int y, int z, EaglercraftRandom rand) {
    }

    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
    }

    public int tickRate() {
        return 10;
    }

    public void onBlockAdded(World world, int x, int y, int z) {
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
    }

    public int quantityDropped(EaglercraftRandom rand) {
        return 1;
    }

    public int idDropped(int metadata, EaglercraftRandom rand) {
        return this.blockID;
    }

    public final float blockStrength(EntityPlayer playerEntity) {
        if(this.blockHardness < 0.0F) {
            return 0.0F;
        } else if(!playerEntity.canHarvestBlock(this)) {
            return 1.0F / this.blockHardness / 100.0F;
        } else {
            InventoryPlayer inventoryPlayer2 = (playerEntity = playerEntity).inventory;
            float f4 = 1.0F;
            if(inventoryPlayer2.mainInventory[inventoryPlayer2.currentItem] != null) {
                f4 = 1.0F * inventoryPlayer2.mainInventory[inventoryPlayer2.currentItem].getItem().getStrVsBlock(this);
            }

            float f5 = f4;
            if(playerEntity.isInsideOfMaterial(Material.water)) {
                f5 = f4 / 5.0F;
            }

            if(!playerEntity.onGround) {
                f5 /= 5.0F;
            }

            return f5 / this.blockHardness / 30.0F;
        }
    }

    public void dropBlockAsItem(World world, int x, int y, int z, int metadata) {
        this.dropBlockAsItemWithChance(world, x, y, z, metadata, 1.0F);
    }

    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float chance) {
        int i7 = this.quantityDropped(world.rand);

        for(int i8 = 0; i8 < i7; ++i8) {
            int i9;
            if(world.rand.nextFloat() <= chance && (i9 = this.idDropped(metadata, world.rand)) > 0) {
                double d10 = (double)(world.rand.nextFloat() * 0.7F) + (double)0.15F;
                double d12 = (double)(world.rand.nextFloat() * 0.7F) + (double)0.15F;
                double d14 = (double)(world.rand.nextFloat() * 0.7F) + (double)0.15F;
                EntityItem entityItem16;
                (entityItem16 = new EntityItem(world, (double)x + d10, (double)y + d12, (double)z + d14, new ItemStack(i9))).delayBeforeCanPickup = 10;
                world.spawnEntityInWorld(entityItem16);
            }
        }

    }

    public float getExplosionResistance(Entity entity) {
        return this.blockResistance / 5.0F;
    }

    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3D vector1, Vec3D vector2) {
        vector1 = vector1.addVector((double)(-x), (double)(-y), (double)(-z));
        vector2 = vector2.addVector((double)(-x), (double)(-y), (double)(-z));
        Vec3D world1 = vector1.getIntermediateWithXValue(vector2, this.minX);
        Vec3D vec3D7 = vector1.getIntermediateWithXValue(vector2, this.maxX);
        Vec3D vec3D8 = vector1.getIntermediateWithYValue(vector2, this.minY);
        Vec3D vec3D9 = vector1.getIntermediateWithYValue(vector2, this.maxY);
        Vec3D vec3D10 = vector1.getIntermediateWithZValue(vector2, this.minZ);
        vector2 = vector1.getIntermediateWithZValue(vector2, this.maxZ);
        if(!this.isVecInsideYZBounds(world1)) {
            world1 = null;
        }

        if(!this.isVecInsideYZBounds(vec3D7)) {
            vec3D7 = null;
        }

        if(!this.isVecInsideXZBounds(vec3D8)) {
            vec3D8 = null;
        }

        if(!this.isVecInsideXZBounds(vec3D9)) {
            vec3D9 = null;
        }

        if(!this.isVecInsideXYBounds(vec3D10)) {
            vec3D10 = null;
        }

        if(!this.isVecInsideXYBounds(vector2)) {
            vector2 = null;
        }

        Vec3D vec3D11 = null;
        if(world1 != null) {
            vec3D11 = world1;
        }

        if(vec3D7 != null && (vec3D11 == null || vector1.distanceTo(vec3D7) < vector1.distanceTo(vec3D11))) {
            vec3D11 = vec3D7;
        }

        if(vec3D8 != null && (vec3D11 == null || vector1.distanceTo(vec3D8) < vector1.distanceTo(vec3D11))) {
            vec3D11 = vec3D8;
        }

        if(vec3D9 != null && (vec3D11 == null || vector1.distanceTo(vec3D9) < vector1.distanceTo(vec3D11))) {
            vec3D11 = vec3D9;
        }

        if(vec3D10 != null && (vec3D11 == null || vector1.distanceTo(vec3D10) < vector1.distanceTo(vec3D11))) {
            vec3D11 = vec3D10;
        }

        if(vector2 != null && (vec3D11 == null || vector1.distanceTo(vector2) < vector1.distanceTo(vec3D11))) {
            vec3D11 = vector2;
        }

        if(vec3D11 == null) {
            return null;
        } else {
            byte vector11 = -1;
            if(vec3D11 == world1) {
                vector11 = 4;
            }

            if(vec3D11 == vec3D7) {
                vector11 = 5;
            }

            if(vec3D11 == vec3D8) {
                vector11 = 0;
            }

            if(vec3D11 == vec3D9) {
                vector11 = 1;
            }

            if(vec3D11 == vec3D10) {
                vector11 = 2;
            }

            if(vec3D11 == vector2) {
                vector11 = 3;
            }

            return new MovingObjectPosition(x, y, z, vector11, vec3D11.addVector((double)x, (double)y, (double)z));
        }
    }

    private boolean isVecInsideYZBounds(Vec3D vector) {
        return vector == null ? false : vector.yCoord >= this.minY && vector.yCoord <= this.maxY && vector.zCoord >= this.minZ && vector.zCoord <= this.maxZ;
    }

    private boolean isVecInsideXZBounds(Vec3D vector) {
        return vector == null ? false : vector.xCoord >= this.minX && vector.xCoord <= this.maxX && vector.zCoord >= this.minZ && vector.zCoord <= this.maxZ;
    }

    private boolean isVecInsideXYBounds(Vec3D vector) {
        return vector == null ? false : vector.xCoord >= this.minX && vector.xCoord <= this.maxX && vector.yCoord >= this.minY && vector.yCoord <= this.maxY;
    }

    public void onBlockDestroyedByExplosion(World world, int x, int y, int z) {
    }

    public int getRenderBlockPass() {
        return 0;
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return true;
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer playerEntity) {
        return false;
    }

    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
    }

    public void onBlockPlaced(World world, int x, int y, int z, int side) {
    }

    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer playerEntity) {
    }

    public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3D velocityVector) {
    }

    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, int x, int y, int z) {
    }

    static {
        Block block10000 = (new BlockStone(1, 1)).setHardness(1.5F).setResistance(10.0F);
        StepSound stepSound1 = soundStoneFootstep;
        Block block0 = block10000;
        block10000.stepSound = stepSound1;
        stone = block0;
        block10000 = (new BlockGrass(2)).setHardness(0.6F);
        stepSound1 = soundGrassFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        grass = (BlockGrass)block0;
        block10000 = (new BlockDirt(3, 2)).setHardness(0.5F);
        stepSound1 = soundGravelFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        dirt = block0;
        block10000 = (new Block(4, 16, Material.rock)).setHardness(2.0F).setResistance(10.0F);
        stepSound1 = soundStoneFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        cobblestone = block0;
        block10000 = (new Block(5, 4, Material.wood)).setHardness(2.0F).setResistance(5.0F);
        stepSound1 = soundWoodFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        planks = block0;
        block10000 = (new BlockSapling(6, 15)).setHardness(0.0F);
        stepSound1 = soundGrassFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        sapling = block0;
        block10000 = (new Block(7, 17, Material.rock)).setHardness(-1.0F).setResistance(6000000.0F);
        stepSound1 = soundStoneFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        bedrock = block0;
        waterMoving = (new BlockFlowing(8, Material.water)).setHardness(100.0F).setLightOpacity(3);
        waterStill = (new BlockStationary(9, Material.water)).setHardness(100.0F).setLightOpacity(3);
        lavaMoving = (new BlockFlowing(10, Material.lava)).setHardness(0.0F).setLightValue(1.0F).setLightOpacity(255);
        lavaStill = (new BlockStationary(11, Material.lava)).setHardness(100.0F).setLightValue(1.0F).setLightOpacity(255);
        block10000 = (new BlockSand(12, 18)).setHardness(0.5F);
        stepSound1 = soundSandFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        sand = block0;
        block10000 = (new BlockGravel(13, 19)).setHardness(0.6F);
        stepSound1 = soundGravelFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        gravel = block0;
        block10000 = (new BlockOre(14, 32)).setHardness(3.0F).setResistance(5.0F);
        stepSound1 = soundStoneFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        oreGold = block0;
        block10000 = (new BlockOre(15, 33)).setHardness(3.0F).setResistance(5.0F);
        stepSound1 = soundStoneFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        oreIron = block0;
        block10000 = (new BlockOre(16, 34)).setHardness(3.0F).setResistance(5.0F);
        stepSound1 = soundStoneFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        oreCoal = block0;
        block10000 = (new BlockLog(17)).setHardness(2.0F);
        stepSound1 = soundWoodFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        wood = block0;
        block10000 = (new BlockLeaves(18, 52)).setHardness(0.2F).setLightOpacity(1);
        stepSound1 = soundGrassFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        leaves = (BlockLeaves)block0;
        block10000 = (new BlockSponge(19)).setHardness(0.6F);
        stepSound1 = soundGrassFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        sponge = block0;
        block10000 = (new BlockGlass(20, 49, Material.glass, false)).setHardness(0.3F);
        stepSound1 = soundGlassFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        glass = block0;
        block10000 = (new Block(35, 64, Material.cloth)).setHardness(0.8F);
        stepSound1 = soundClothFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        cloth = block0;
        block10000 = (new BlockFlower(37, 13)).setHardness(0.0F);
        stepSound1 = soundGrassFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        plantYellow = (BlockFlower)block0;
        block10000 = (new BlockFlower(38, 12)).setHardness(0.0F);
        stepSound1 = soundGrassFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        plantRed = (BlockFlower)block0;
        block10000 = (new BlockMushroom(39, 29)).setHardness(0.0F);
        stepSound1 = soundGrassFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        mushroomBrown = (BlockFlower)block0.setLightValue(0.125F);
        block10000 = (new BlockMushroom(40, 28)).setHardness(0.0F);
        stepSound1 = soundGrassFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        mushroomRed = (BlockFlower)block0;
        block10000 = (new BlockOreBlock(41, 39)).setHardness(3.0F).setResistance(10.0F);
        stepSound1 = soundMetalFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        blockGold = block0;
        block10000 = (new BlockOreBlock(42, 38)).setHardness(5.0F).setResistance(10.0F);
        stepSound1 = soundMetalFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        blockSteel = block0;
        block10000 = (new BlockStep(43, true)).setHardness(2.0F).setResistance(10.0F);
        stepSound1 = soundStoneFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        stairDouble = block0;
        block10000 = (new BlockStep(44, false)).setHardness(2.0F).setResistance(10.0F);
        stepSound1 = soundStoneFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        stairSingle = block0;
        block10000 = (new Block(45, 7, Material.rock)).setHardness(2.0F).setResistance(10.0F);
        stepSound1 = soundStoneFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        brick = block0;
        block10000 = (new BlockTNT(46, 8)).setHardness(0.0F);
        stepSound1 = soundGrassFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        tnt = block0;
        block10000 = (new BlockBookshelf(47, 35)).setHardness(1.5F);
        stepSound1 = soundWoodFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        bookshelf = block0;
        block10000 = (new Block(48, 36, Material.rock)).setHardness(2.0F).setResistance(10.0F);
        stepSound1 = soundStoneFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        cobblestoneMossy = block0;
        block10000 = (new BlockObsidian(49, 37)).setHardness(10.0F).setResistance(20.0F);
        stepSound1 = soundStoneFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        obsidian = block0;
        block10000 = (new BlockTorch(50, 80)).setHardness(0.0F).setLightValue(0.9375F);
        stepSound1 = soundWoodFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        torch = block0;
        block10000 = (new BlockFire(51, 31)).setHardness(0.0F).setLightValue(1.0F);
        stepSound1 = soundWoodFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        fire = (BlockFire)block0;
        block10000 = (new BlockMobSpawner(52, 65)).setHardness(5.0F);
        stepSound1 = soundMetalFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        mobSpawner = block0;
        stairCompactWood = new BlockStairs(53, planks);
        block10000 = (new BlockChest(54)).setHardness(2.5F);
        stepSound1 = soundWoodFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        chest = block0;
        block10000 = (new BlockGears(55, 62)).setHardness(0.5F);
        stepSound1 = soundMetalFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        cog = block0;
        block10000 = (new BlockOre(56, 50)).setHardness(3.0F).setResistance(5.0F);
        stepSound1 = soundStoneFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        oreDiamond = block0;
        block10000 = (new BlockOreBlock(57, 40)).setHardness(5.0F).setResistance(10.0F);
        stepSound1 = soundMetalFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        blockDiamond = block0;
        block10000 = (new BlockWorkbench(58)).setHardness(2.5F);
        stepSound1 = soundWoodFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        workbench = block0;
        block10000 = (new BlockCrops(59, 88)).setHardness(0.0F);
        stepSound1 = soundGrassFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        crops = block0;
        block10000 = (new BlockFarmland(60)).setHardness(0.6F);
        stepSound1 = soundGravelFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        tilledField = block0;
        block10000 = (new BlockFurnace(61, false)).setHardness(3.5F);
        stepSound1 = soundStoneFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        stoneOvenIdle = block0;
        block10000 = (new BlockFurnace(62, true)).setHardness(3.5F);
        stepSound1 = soundStoneFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        stoneOvenActive = block0.setLightValue(0.875F);
        block10000 = (new BlockSign(63, TileEntitySign.class, Item.sign.shiftedIndex)).setHardness(1.0F);
        stepSound1 = soundWoodFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        signStanding = block0;
        block10000 = (new BlockDoor(64)).setHardness(3.0F);
        stepSound1 = soundWoodFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        doorWood = block0;
        block10000 = (new BlockLadder(65, 83)).setHardness(0.4F);
        stepSound1 = soundWoodFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        ladder = block0;
        block10000 = (new BlockMinecartTrack(66, 128)).setHardness(1.0F);
        stepSound1 = soundMetalFootstep;
        block0 = block10000;
        block10000.stepSound = stepSound1;
        minecartTrack = block0;
        stairCompactStone = new BlockStairs(67, cobblestone);

        for(int i2 = 0; i2 < 256; ++i2) {
            if(blocksList[i2] != null) {
                Item.itemsList[i2] = new ItemBlock(i2 - 256);
            }
        }

    }
}
