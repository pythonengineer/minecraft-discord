package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.player.ItemStack;
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
	public static final Block log;
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
	public static final Block goldBlock;
	public static final Block ironBlock;
	public static final Block stairDouble;
	public static final Block stairSingle;
	public static final Block brick;
	public static final Block tnt;
	public static final Block bookShelf;
	public static final Block cobblestoneMossy;
	public static final Block obsidian;
	public static final Block torch;
	public int blockIndexInTexture;
	public final int blockID;
	private int hardness;
	private boolean blockIsDropped;
	public float minX;
	public float minY;
	public float minZ;
	public float maxX;
	public float maxY;
	public float maxZ;
	public float blockParticleGravity;

	protected Block(int var1) {
		this.blockIsDropped = true;
		this.blockParticleGravity = 1.0F;
		blocksList[var1] = this;
		this.blockID = var1;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		opaqueCubeLookup[var1] = this.isOpaqueCube();
		lightOpacity[var1] = this.isOpaqueCube() ? 255 : 0;
		canBlockGrass[var1] = this.renderAsNormalBlock();
		isBlockFluid[var1] = false;
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

	protected final Block setResistance(float var1) {
		this.hardness = (int)(var1 * 20.0F);
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

	public int idDropped() {
		return this.blockID;
	}

	public final int blockStrength() {
		return this.hardness;
	}

	public void dropBlockAsItem(World var1, int var2, int var3, int var4) {
		this.dropBlockAsItemWithChance(var1, var2, var3, var4, 1.0F);
	}

	public void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, float var5) {
		int var6 = this.quantityDropped(var1.random);

		for(int var7 = 0; var7 < var6; ++var7) {
			if(var1.random.nextFloat() <= var5) {
				float var8 = 0.7F;
				float var9 = var1.random.nextFloat() * var8 + (1.0F - var8) * 0.5F;
				float var10 = var1.random.nextFloat() * var8 + (1.0F - var8) * 0.5F;
				var8 = var1.random.nextFloat() * var8 + (1.0F - var8) * 0.5F;
				var1.spawnEntityInWorld(new EntityItem(var1, (float)var2 + var9, (float)var3 + var10, (float)var4 + var8, new ItemStack(blocksList[this.idDropped()], 1)));
			}
		}

	}

	public final boolean canDrop() {
		return this.blockIsDropped;
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
		Block var10000 = (new BlockStone(1, 1)).setResistance(1.0F);
		boolean var0 = false;
		Block var1 = var10000;
		var1.blockIsDropped = false;
		stone = var1;
		grass = (new BlockGrass()).setResistance(0.6F);
		dirt = (new BlockDirt()).setResistance(0.5F);
		var10000 = (new Block(4, 16)).setResistance(1.5F);
		var0 = false;
		var1 = var10000;
		var1.blockIsDropped = false;
		cobblestone = var1;
		planks = (new Block(5, 4)).setResistance(1.5F);
		sapling = (new BlockSapling(6)).setResistance(0.0F);
		var10000 = (new Block(7, 17)).setResistance(999.0F);
		var0 = false;
		var1 = var10000;
		var1.blockIsDropped = false;
		bedrock = var1;
		waterMoving = (new BlockFluid(8, Material.water)).setResistance(100.0F).setLightOpacity(2);
		waterStill = (new BlockStationary(9, Material.water)).setResistance(100.0F).setLightOpacity(2);
		lavaMoving = (new BlockFluid(10, Material.lava)).setResistance(0.0F).setLightValue(0.8F).setLightOpacity(255);
		lavaStill = (new BlockStationary(11, Material.lava)).setResistance(100.0F).setLightValue(0.8F).setLightOpacity(255);
		sand = (new BlockSand(12, 18)).setResistance(0.5F);
		gravel = (new BlockSand(13, 19)).setResistance(0.6F);
		var10000 = (new BlockOre(14, 32)).setResistance(3.0F);
		var0 = false;
		var1 = var10000;
		var1.blockIsDropped = false;
		oreGold = var1;
		var10000 = (new BlockOre(15, 33)).setResistance(3.0F);
		var0 = false;
		var1 = var10000;
		var1.blockIsDropped = false;
		oreIron = var1;
		var10000 = (new BlockOre(16, 34)).setResistance(3.0F);
		var0 = false;
		var1 = var10000;
		var1.blockIsDropped = false;
		oreCoal = var1;
		log = (new BlockLog()).setResistance(2.5F);
		leaves = (new BlockLeaves()).setResistance(0.2F).setLightOpacity(1);
		sponge = (new BlockSponge()).setResistance(0.6F);
		glass = (new BlockGlass()).setResistance(0.3F);
		clothRed = (new Block(21, 64)).setResistance(0.8F);
		clothOrange = (new Block(22, 65)).setResistance(0.8F);
		clothYellow = (new Block(23, 66)).setResistance(0.8F);
		clothChartreuse = (new Block(24, 67)).setResistance(0.8F);
		clothGreen = (new Block(25, 68)).setResistance(0.8F);
		clothSpringGreen = (new Block(26, 69)).setResistance(0.8F);
		clothCyan = (new Block(27, 70)).setResistance(0.8F);
		clothCapri = (new Block(28, 71)).setResistance(0.8F);
		clothUltramarine = (new Block(29, 72)).setResistance(0.8F);
		clothViolet = (new Block(30, 73)).setResistance(0.8F);
		clothPurple = (new Block(31, 74)).setResistance(0.8F);
		clothMagenta = (new Block(32, 75)).setResistance(0.8F);
		clothRose = (new Block(33, 76)).setResistance(0.8F);
		clothDarkGray = (new Block(34, 77)).setResistance(0.8F);
		clothGray = (new Block(35, 78)).setResistance(0.8F);
		clothWhite = (new Block(36, 79)).setResistance(0.8F);
		plantYellow = (new BlockFlower(37, 13)).setResistance(0.0F);
		plantRed = (new BlockFlower(38, 12)).setResistance(0.0F);
		mushroomBrown = (new BlockMushroom(39, 29)).setResistance(0.0F);
		mushroomRed = (new BlockMushroom(40, 28)).setResistance(0.0F);
		var10000 = (new BlockOreBlock(41, 40)).setResistance(3.0F);
		var0 = false;
		var1 = var10000;
		var1.blockIsDropped = false;
		goldBlock = var1;
		var10000 = (new BlockOreBlock(42, 39)).setResistance(5.0F);
		var0 = false;
		var1 = var10000;
		var1.blockIsDropped = false;
		ironBlock = var1;
		var10000 = (new BlockStep(43, true)).setResistance(2.0F);
		var0 = false;
		var1 = var10000;
		var1.blockIsDropped = false;
		stairDouble = var1;
		var10000 = (new BlockStep(44, false)).setResistance(2.0F);
		var0 = false;
		var1 = var10000;
		var1.blockIsDropped = false;
		stairSingle = var1;
		var10000 = (new Block(45, 7)).setResistance(2.0F);
		var0 = false;
		var1 = var10000;
		var1.blockIsDropped = false;
		brick = var1;
		tnt = (new BlockTNT()).setResistance(0.0F);
		bookShelf = (new BlockBookshelf()).setResistance(1.5F);
		var10000 = (new Block(48, 36)).setResistance(1.0F);
		var0 = false;
		var1 = var10000;
		var1.blockIsDropped = false;
		cobblestoneMossy = var1;
		var10000 = (new BlockStone(49, 37)).setResistance(10.0F);
		var0 = false;
		var1 = var10000;
		var1.blockIsDropped = false;
		obsidian = var1;
		torch = (new BlockTorch()).setResistance(0.0F).setLightValue(1.0F);
	}
}
