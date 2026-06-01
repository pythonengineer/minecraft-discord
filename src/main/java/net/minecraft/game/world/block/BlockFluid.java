package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public abstract class BlockFluid extends Block {
    protected int fluidType = 1;

    protected BlockFluid(int i1, Material material2) {
        super(i1, (material2 == Material.lava ? 14 : 12) * 16 + 13, material2);
        float f3 = 0.0F;
        float f4 = 0.0F;
        if(material2 == Material.lava) {
            this.fluidType = 2;
        }

        this.setBlockBounds(0.0F + f4, 0.0F + f3, 0.0F + f4, 1.0F + f4, 1.0F + f3, 1.0F + f4);
        this.setTickOnLoad(true);
    }

    public static float getFluidHeightPercent(int fluidHeight) {
        if(fluidHeight >= 8) {
            fluidHeight = 0;
        }

        return (float)(fluidHeight + 1) / 9.0F;
    }

    public int getBlockTextureFromSide(int side) {
        return side != 0 && side != 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
    }

    protected int getFlowDecay(World world, int x, int y, int z) {
        return world.getBlockMaterial(x, y, z) != this.material ? -1 : world.getBlockMetadata(x, y, z);
    }

    protected int getEffectiveFlowDecay(IBlockAccess iBlockAccess, int x, int y, int z) {
        if(iBlockAccess.getBlockMaterial(x, y, z) != this.material) {
            return -1;
        } else {
            int world1;
            if((world1 = iBlockAccess.getBlockMetadata(x, y, z)) >= 8) {
                world1 = 0;
            }

            return world1;
        }
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean canCollideCheck(int metadata, boolean flag) {
        return flag && metadata == 0;
    }

    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, int x, int y, int z, int metadata) {
        return iBlockAccess.getBlockMaterial(x, y, z) == this.material ? false : (metadata == 1 ? true : super.shouldSideBeRendered(iBlockAccess, x, y, z, metadata));
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    public int getRenderType() {
        return 4;
    }

    public int idDropped(int metadata, EaglercraftRandom rand) {
        return 0;
    }

    public int quantityDropped(EaglercraftRandom rand) {
        return 0;
    }

    private Vec3D getFlowVector(IBlockAccess iBlockAccess, int x, int y, int z) {
        Vec3D vec3D5 = Vec3D.createVector(0.0D, 0.0D, 0.0D);
        int i6 = this.getEffectiveFlowDecay(iBlockAccess, x, y, z);

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

            if(i7 == 3) {
                ++i9;
            }

            int i10;
            if((i10 = this.getEffectiveFlowDecay(iBlockAccess, i8, y, i9)) < 0) {
                if((i10 = this.getEffectiveFlowDecay(iBlockAccess, i8, y - 1, i9)) >= 0) {
                    i10 -= i6 - 8;
                    vec3D5 = vec3D5.addVector((double)((i8 - x) * i10), (double)(i10 * 0), (double)((i9 - z) * i10));
                }
            } else if(i10 >= 0) {
                i10 -= i6;
                vec3D5 = vec3D5.addVector((double)((i8 - x) * i10), (double)(i10 * 0), (double)((i9 - z) * i10));
            }
        }

        if(iBlockAccess.getBlockMetadata(x, y, z) >= 8) {
            boolean z11 = false;
            if(this.shouldSideBeRendered(iBlockAccess, x, y, z - 1, 2)) {
                z11 = true;
            }

            if(z11 || this.shouldSideBeRendered(iBlockAccess, x, y, z + 1, 3)) {
                z11 = true;
            }

            if(z11 || this.shouldSideBeRendered(iBlockAccess, x - 1, y, z, 4)) {
                z11 = true;
            }

            if(z11 || this.shouldSideBeRendered(iBlockAccess, x + 1, y, z, 5)) {
                z11 = true;
            }

            if(z11 || this.shouldSideBeRendered(iBlockAccess, x, y + 1, z - 1, 2)) {
                z11 = true;
            }

            if(z11 || this.shouldSideBeRendered(iBlockAccess, x, y + 1, z + 1, 3)) {
                z11 = true;
            }

            if(z11 || this.shouldSideBeRendered(iBlockAccess, x - 1, y + 1, z, 4)) {
                z11 = true;
            }

            if(z11 || this.shouldSideBeRendered(iBlockAccess, x + 1, y + 1, z, 5)) {
                z11 = true;
            }

            if(z11) {
                vec3D5 = vec3D5.normalize().addVector(0.0D, -6.0D, 0.0D);
            }
        }

        return vec3D5.normalize();
    }

    public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3D velocityVector) {
        Vec3D world1 = this.getFlowVector(world, x, y, z);
        velocityVector.xCoord += world1.xCoord;
        velocityVector.yCoord += world1.yCoord;
        velocityVector.zCoord += world1.zCoord;
    }

    public int tickRate() {
        return this.material == Material.water ? 5 : (this.material == Material.lava ? 30 : 0);
    }

    public float getBlockBrightness(IBlockAccess iBlockAccess, int x, int y, int z) {
        float f5 = iBlockAccess.getBrightness(x, y, z);
        float world1 = iBlockAccess.getBrightness(x, y + 1, z);
        return f5 > world1 ? f5 : world1;
    }

    public void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
        super.updateTick(world, x, y, z, rand);
    }

    public int getRenderBlockPass() {
        return this.material == Material.water ? 1 : 0;
    }

    public void randomDisplayTick(World world, int x, int y, int z, EaglercraftRandom rand) {
        int i6;
        if(this.material == Material.water && rand.nextInt(64) == 0 && (i6 = world.getBlockMetadata(x, y, z)) > 0 && i6 < 8) {
            world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "liquid.water", rand.nextFloat() * 0.25F + 0.75F, rand.nextFloat() + 0.5F);
        }

        if(this.material == Material.lava && world.getBlockMaterial(x, y + 1, z) == Material.air && !world.isBlockNormalCube(x, y + 1, z) && rand.nextInt(100) == 0) {
            double d12 = (double)((float)x + rand.nextFloat());
            double d8 = (double)y + this.maxY;
            double d10 = (double)((float)z + rand.nextFloat());
            world.spawnParticle("lava", d12, d8, d10, 0.0D, 0.0D, 0.0D);
        }

    }

    public static double getFlowDirection(IBlockAccess iBlockAccess, int x, int y, int z, Material material) {
        Vec3D vec3D5 = null;
        if(material == Material.water) {
            vec3D5 = ((BlockFluid)Block.waterMoving).getFlowVector(iBlockAccess, x, y, z);
        }

        if(material == Material.lava) {
            vec3D5 = ((BlockFluid)Block.lavaMoving).getFlowVector(iBlockAccess, x, y, z);
        }

        return vec3D5.xCoord == 0.0D && vec3D5.zCoord == 0.0D ? -1000.0D : Math.atan2(vec3D5.zCoord, vec3D5.xCoord) - Math.PI / 2D;
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        this.checkForHarden(world, x, y, z);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
        this.checkForHarden(world, x, y, z);
    }

    private void checkForHarden(World world, int x, int y, int z) {
        if(world.getBlockId(x, y, z) == this.blockID) {
            if(this.material == Material.lava) {
                boolean z5 = false;
                if(z5 || world.getBlockMaterial(x, y, z - 1) == Material.water) {
                    z5 = true;
                }

                if(z5 || world.getBlockMaterial(x, y, z + 1) == Material.water) {
                    z5 = true;
                }

                if(z5 || world.getBlockMaterial(x - 1, y, z) == Material.water) {
                    z5 = true;
                }

                if(z5 || world.getBlockMaterial(x + 1, y, z) == Material.water) {
                    z5 = true;
                }

                if(z5 || world.getBlockMaterial(x, y + 1, z) == Material.water) {
                    z5 = true;
                }

                if(z5) {
                    int i6;
                    if((i6 = world.getBlockMetadata(x, y, z)) == 0) {
                        world.setBlockWithNotify(x, y, z, Block.obsidian.blockID);
                    } else if(i6 <= 4) {
                        world.setBlockWithNotify(x, y, z, Block.cobblestone.blockID);
                    }

                    this.triggerLavaMixEffects(world, x, y, z);
                }
            }

        }
    }

    protected void triggerLavaMixEffects(World world, int x, int y, int z) {
        world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

        for(int i4 = 0; i4 < 8; ++i4) {
            world.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + 1.2D, (double)z + Math.random(), 0.0D, 0.0D, 0.0D);
        }

    }
}
