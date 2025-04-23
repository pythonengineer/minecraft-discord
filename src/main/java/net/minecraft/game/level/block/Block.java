package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public class Block {
	public static final Block[] blocksList = new Block[256];
	public static final boolean[] tickOnLoad = new boolean[256];
	private static boolean[] opaqueCubeLookup = new boolean[256];
	public static final int[] lightOpacity = new int[256];
	private static boolean[] canBlockGrass = new boolean[256];
	public static final boolean[] isBlockContainer = new boolean[256];
	public static final int[] lightValue = new int[256];
	public static final Block stone = (new BlockStone(1, 1)).setHardness(1.0F);
	public static final Block grass = (new BlockGrass()).setHardness(0.6F);
	public static final Block dirt = (new BlockDirt()).setHardness(0.5F);
	public static final Block cobblestone = (new Block(4, 16)).setHardness(1.5F);
	public static final Block planks = (new Block(5, 4)).setHardness(1.5F);
	public static final Block sapling = (new BlockSapling(6)).setHardness(0.0F);
	public static final Block bedrock = (new Block(7, 17)).setHardness(999.0F);
	public static final Block waterMoving = (new BlockFluid(8, Material.water)).setHardness(100.0F).setLightOpacity(2);
	public static final Block waterStill = (new BlockStationary(9, Material.water)).setHardness(100.0F).setLightOpacity(2);
	public static final Block lavaMoving = (new BlockFluid(10, Material.lava)).setHardness(0.0F).setLightValue(0.8F);
	public static final Block lavaStill = (new BlockStationary(11, Material.lava)).setHardness(100.0F).setLightValue(0.8F);
	public static final Block sand = (new BlockSand(12, 18)).setHardness(0.5F);
	public static final Block gravel = (new BlockSand(13, 19)).setHardness(0.6F);
	public static final Block oreGold = (new BlockOre(14, 32)).setHardness(3.0F);
	public static final Block oreIron = (new BlockOre(15, 33)).setHardness(3.0F);
	public static final Block oreCoal = (new BlockOre(16, 34)).setHardness(3.0F);
	public static final Block log = (new BlockLog()).setHardness(2.5F);
	public static final Block leaves = (new BlockLeaves()).setHardness(0.2F).setLightOpacity(1);
	public static final Block sponge = (new BlockSponge()).setHardness(0.6F);
	public static final Block glass = (new BlockGlass()).setHardness(0.3F);
	public static final Block clothRed = (new Block(21, 64)).setHardness(0.8F);
	public static final Block clothOrange = (new Block(22, 65)).setHardness(0.8F);
	public static final Block clothYellow = (new Block(23, 66)).setHardness(0.8F);
	public static final Block clothChartreuse = (new Block(24, 67)).setHardness(0.8F);
	public static final Block clothGreen = (new Block(25, 68)).setHardness(0.8F);
	public static final Block clothSpringGreen = (new Block(26, 69)).setHardness(0.8F);
	public static final Block clothCyan = (new Block(27, 70)).setHardness(0.8F);
	public static final Block clothCapri = (new Block(28, 71)).setHardness(0.8F);
	public static final Block clothUltramarine = (new Block(29, 72)).setHardness(0.8F);
	public static final Block clothViolet = (new Block(30, 73)).setHardness(0.8F);
	public static final Block clothPurple = (new Block(31, 74)).setHardness(0.8F);
	public static final Block clothMagenta = (new Block(32, 75)).setHardness(0.8F);
	public static final Block clothRose = (new Block(33, 76)).setHardness(0.8F);
	public static final Block clothDarkGray = (new Block(34, 77)).setHardness(0.8F);
	public static final Block clothGray = (new Block(35, 78)).setHardness(0.8F);
	public static final Block clothWhite = (new Block(36, 79)).setHardness(0.8F);
	public static final Block plantYellow = (new BlockFlower(37, 13)).setHardness(0.0F);
	public static final Block plantRed = (new BlockFlower(38, 12)).setHardness(0.0F);
	public static final Block mushroomBrown = (new BlockMushroom(39, 29)).setHardness(0.0F);
	public static final Block mushroomRed = (new BlockMushroom(40, 28)).setHardness(0.0F);
	public static final Block goldBlock = (new BlockOreBlock(41, 40)).setHardness(3.0F);
	public static final Block ironBlock = (new BlockOreBlock(42, 39)).setHardness(5.0F);
	public static final Block stairDouble = (new BlockStep(43, true)).setHardness(2.0F);
	public static final Block stairSingle = (new BlockStep(44, false)).setHardness(2.0F);
	public static final Block brick = (new Block(45, 7)).setHardness(2.0F);
	public static final Block tnt = (new BlockTNT()).setHardness(0.0F);
	public static final Block bookShelf = (new BlockBookshelf()).setHardness(1.5F);
	public static final Block cobblestoneMossy = (new Block(48, 36)).setHardness(1.0F);
	public static final Block obsidian = (new BlockStone(49, 37)).setHardness(10.0F);
	public static final Block torch = (new BlockTorch()).setHardness(0.0F).setLightValue(1.0F);
	public int blockIndexInTexture;
	public final int blockID;
	private int blockHardness;
	public float minX;
	public float minY;
	public float minZ;
	public float maxX;
	public float maxY;
	public float maxZ;
	public float blockParticleGravity;

	protected Block(int var1) {
		this.blockParticleGravity = 1.0F;
		blocksList[var1] = this;
		this.blockID = var1;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		opaqueCubeLookup[var1] = this.isOpaqueCube();
		lightOpacity[var1] = this.isOpaqueCube() ? 255 : 0;
		canBlockGrass[var1] = this.renderAsNormalBlock();
		isBlockContainer[var1] = false;
	}

	private Block setLightOpacity(int var1) {
		lightOpacity[this.blockID] = var1;
		return this;
	}

	private Block setLightValue(float var1) {
		lightValue[this.blockID] = (int)(8.0F * var1);
		return this;
	}

	public boolean renderAsNormalBlock() {
		return true;
	}

	public int getRenderType() {
		return 0;
	}

	protected final Block setHardness(float var1) {
		this.blockHardness = (int)(var1 * 20.0F);
		return this;
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

	public void updateTick(World var1, int var2, int var3, int var4, EaglercraftRandom var5) {
	}

	public void onBlockDestroyedByPlayer(World var1, int var2, int var3, int var4) {
	}

	public Material getMaterial() {
		return Material.air;
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
	}

	public void onBlockPlaced(World var1, int var2, int var3, int var4) {
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

	public final int blockStrength() {
		return this.blockHardness;
	}

	public void dropBlockAsItem(World var1) {
		this.dropBlockAsItemWithChance(var1, 1.0F);
	}

	public void dropBlockAsItemWithChance(World var1, float var2) {
		int var3 = this.quantityDropped(var1.random);

		for(int var4 = 0; var4 < var3; ++var4) {
			if(var1.random.nextFloat() <= 1.0F) {
				var1.random.nextFloat();
				var1.random.nextFloat();
				var1.random.nextFloat();
			}
		}

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

	public int getRenderBlockPass() {
		return 0;
	}
}
