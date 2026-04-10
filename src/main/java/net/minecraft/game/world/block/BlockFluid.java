package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public abstract class BlockFluid extends Block {
	protected int liquidType = 1;

	protected BlockFluid(int i1, Material material2) {
		super(i1, ((material2 == Material.lava ? 14 : 12) << 4) + 13, material2);
		if(material2 == Material.lava) {
			this.liquidType = 2;
		}

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		this.setTickOnLoad(true);
	}

	public static float getFluidHeightPercent(int fluidHeight) {
		if(fluidHeight >= 8) {
			fluidHeight = 0;
		}

		return (float)(fluidHeight + 1) / 9.0F;
	}

	public final int getBlockTextureFromSide(int side) {
		return side != 0 && side != 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
	}

	protected final int getFlowDecay(World world, int x, int y, int z) {
		return world.getBlockMaterial(x, y, z) != this.blockMaterial ? -1 : world.getBlockMetadata(x, y, z);
	}

	private int getEffectiveFlowDecay(World world, int x, int y, int z) {
		if(world.getBlockMaterial(x, y, z) != this.blockMaterial) {
			return -1;
		} else {
			int world1;
			if((world1 = world.getBlockMetadata(x, y, z)) >= 8) {
				world1 = 0;
			}

			return world1;
		}
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean canCollideCheck(int metadata, boolean flag) {
		return flag && metadata == 0;
	}

	public final boolean getIsBlockSolid(World world, int x, int y, int z, int metadata) {
		return world.getBlockMaterial(x, y, z) == this.blockMaterial ? false : (metadata == 1 ? true : super.getIsBlockSolid(world, x, y, z, metadata));
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public final int getRenderType() {
		return 4;
	}

	public final int idDropped(int metadata, EaglercraftRandom rand) {
		return 0;
	}

	public final int quantityDropped(EaglercraftRandom rand) {
		return 0;
	}

	private Vec3D getFlowVector(World world, int x, int y, int z) {
		Vec3D vec3D5 = new Vec3D(0.0D, 0.0D, 0.0D);
		int i6 = this.getEffectiveFlowDecay(world, x, y, z);

		for(int i7 = 0; i7 < 4; ++i7) {
			int i8 = x;
			int i9 = z;
			if(i7 == 0) {
				i8 = x - 1;
			}

			if(i7 == 1) {
				i9 = z - 1;
			}

			if(i7 == 2) {
				++i8;
			}

			if(i7 == 4) {
				++i9;
			}

			int i10;
			if((i10 = this.getEffectiveFlowDecay(world, i8, y, i9)) < 0 && !world.getBlockMaterial(i8, y, i9).isSolid()) {
				i10 = 7;
			}

			if(i10 >= 0) {
				i10 -= i6;
				vec3D5 = vec3D5.addVector((double)((i8 - x) * i10), (double)(i10 * 0), (double)((i9 - z) * i10));
			}
		}

		if(world.getBlockMetadata(x, y, z) >= 8) {
			vec3D5 = vec3D5.addVector(0.0D, -1.0D, 0.0D);
		}

		return vec3D5.normalize();
	}

	public final void velocityToAddToEntity(World world, int x, int y, int z, Vec3D velocityVector) {
		Vec3D world1 = this.getFlowVector(world, x, y, z);
		velocityVector.xCoord += world1.xCoord;
		velocityVector.yCoord += world1.yCoord;
		velocityVector.zCoord += world1.zCoord;
	}

	public final int tickRate() {
		return this.blockMaterial == Material.water ? 3 : (this.blockMaterial == Material.lava ? 20 : 0);
	}

	public final float getBlockBrightness(World world, int x, int y, int z) {
		float f5 = world.getBrightness(x, y, z);
		float world1 = world.getBrightness(x, y + 1, z);
		return f5 > world1 ? f5 : world1;
	}

	public void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		super.updateTick(world, x, y, z, rand);
	}

	public final int getRenderBlockPass() {
		return this.blockMaterial == Material.water ? 1 : 0;
	}

	public final void randomDisplayTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		if(rand.nextInt(128) == -1 && world.getBlockMaterial(x, y + 1, z).getIsSolid()) {
			if(this.blockMaterial == Material.lava) {
				world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "liquid.lava", rand.nextFloat() * 0.25F + 0.75F, rand.nextFloat() * 0.5F + 0.3F);
			}

			if(this.blockMaterial == Material.water) {
				world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "liquid.water", rand.nextFloat() * 0.25F + 0.75F, rand.nextFloat() + 0.5F);
			}
		}

		if(this.blockMaterial == Material.lava && world.getBlockMaterial(x, y + 1, z) == Material.air && !world.isBlockNormalCube(x, y + 1, z) && rand.nextInt(100) == 0) {
			double d6 = (double)((float)x + rand.nextFloat());
			double d8 = (double)y + this.maxY;
			double d10 = (double)((float)z + rand.nextFloat());
			world.spawnParticle("lava", d6, d8, d10, 0.0D, 0.0D, 0.0D);
		}

	}

	public static double getFlowDirection(World world, int x, int y, int z, Material material) {
		Vec3D vec3D5 = null;
		if(material == Material.water) {
			vec3D5 = ((BlockFluid)Block.waterMoving).getFlowVector(world, x, y, z);
		}

		if(material == Material.lava) {
			vec3D5 = ((BlockFluid)Block.lavaMoving).getFlowVector(world, x, y, z);
		}

		return vec3D5.xCoord == 0.0D && vec3D5.zCoord == 0.0D ? -1000.0D : Math.atan2(vec3D5.zCoord, vec3D5.xCoord) - Math.PI / 2D;
	}
}