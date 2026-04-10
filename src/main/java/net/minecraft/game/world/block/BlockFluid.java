package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public abstract class BlockFluid extends Block {
	protected BlockFluid(int i1, Material material2) {
		super(i1, material2 == Material.lava ? 30 : 14, material2);
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
		return this.blockMaterial != Material.lava && side != 0 && side != 1 ? this.blockIndexInTexture + 32 : this.blockIndexInTexture;
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

	public final boolean canCollideCheck(boolean flag) {
		return flag;
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

	public final void velocityToAddToEntity(World world, int x, int y, int z, Vec3D velocityVector) {
		Vec3D vec3D6 = new Vec3D(0.0D, 0.0D, 0.0D);
		int i7 = this.getEffectiveFlowDecay(world, x, y, z);

		for(int i8 = 0; i8 < 4; ++i8) {
			int i9 = x;
			int i10 = z;
			if(i8 == 0) {
				i9 = x - 1;
			}

			if(i8 == 1) {
				i10 = z - 1;
			}

			if(i8 == 2) {
				++i9;
			}

			if(i8 == 4) {
				++i10;
			}

			int i11;
			if((i11 = this.getEffectiveFlowDecay(world, i9, y, i10)) > 0 && i11 < 8) {
				i11 -= i7;
				vec3D6 = vec3D6.addVector((double)((i9 - x) * i11), (double)(i11 * 0), (double)((i10 - z) * i11));
			}
		}

		vec3D6 = vec3D6.normalize();
		velocityVector.xCoord += vec3D6.xCoord;
		velocityVector.yCoord += vec3D6.yCoord;
		velocityVector.zCoord += vec3D6.zCoord;
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
}