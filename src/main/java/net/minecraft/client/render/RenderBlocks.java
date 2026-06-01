package net.minecraft.client.render;

import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.BlockDoor;
import net.minecraft.game.world.block.BlockFluid;
import net.minecraft.game.world.material.Material;

public class RenderBlocks {
    private IBlockAccess blockAccess;
	private int overrideBlockTexture = -1;
    private boolean flipTexture = false;
	private boolean renderAllFaces = false;

    public RenderBlocks(IBlockAccess iBlockAccess) {
        this.blockAccess = iBlockAccess;
    }

	public RenderBlocks() {
	}

	public void renderBlockUsingTexture(Block block, int x, int y, int z, int overrideBlockTextureIndex) {
		this.overrideBlockTexture = overrideBlockTextureIndex;
		this.renderBlockByRenderType(block, x, y, z);
		this.overrideBlockTexture = -1;
	}

    public boolean renderBlockByRenderType(Block block, int x, int y, int z) {
        int i5 = block.getRenderType();
        block.setBlockBoundsBasedOnState(this.blockAccess, x, y, z);
        return i5 == 0 ? this.renderStandardBlock(block, x, y, z) : (i5 == 4 ? this.renderBlockFluids(block, x, y, z) : (i5 == 1 ? this.renderBlockReed(block, x, y, z) : (i5 == 6 ? this.renderBlockCrops(block, x, y, z) : (i5 == 2 ? this.renderBlockTorch(block, x, y, z) : (i5 == 3 ? this.renderBlockFire(block, x, y, z) : (i5 == 5 ? this.renderBlockRedstoneWire(block, x, y, z) : (i5 == 8 ? this.renderBlockLadder(block, x, y, z) : (i5 == 7 ? this.renderBlockDoor(block, x, y, z) : (i5 == 9 ? this.renderBlockMinecartTrack(block, x, y, z) : (i5 == 10 ? this.renderBlockStairs(block, x, y, z) : (i5 == 11 ? this.renderBlockFence(block, x, y, z) : (i5 == 12 ? this.renderBlockLever(block, x, y, z) : false))))))))))));
    }

    public boolean renderBlockTorch(Block block, int x, int y, int z) {
        int i5 = this.blockAccess.getBlockMetadata(x, y, z);
        Tessellator tessellator6 = Tessellator.instance;
        float f7 = block.getBlockBrightness(this.blockAccess, x, y, z);
        if(Block.lightValue[block.blockID] > 0) {
            f7 = 1.0F;
        }

        tessellator6.setColorOpaque_F(f7, f7, f7);
        double d8 = (double)0.4F;
        double d10 = 0.5D - d8;
        double d12 = (double)0.2F;
        if(i5 == 1) {
            this.renderTorchAtAngle(block, (double)x - d10, (double)y + d12, (double)z, -d8, 0.0D);
        } else if(i5 == 2) {
            this.renderTorchAtAngle(block, (double)x + d10, (double)y + d12, (double)z, d8, 0.0D);
        } else if(i5 == 3) {
            this.renderTorchAtAngle(block, (double)x, (double)y + d12, (double)z - d10, 0.0D, -d8);
        } else if(i5 == 4) {
            this.renderTorchAtAngle(block, (double)x, (double)y + d12, (double)z + d10, 0.0D, d8);
        } else {
            this.renderTorchAtAngle(block, (double)x, (double)y, (double)z, 0.0D, 0.0D);
        }

        return true;
    }

    public boolean renderBlockLever(Block block, int x, int y, int z) {
        int i5 = this.blockAccess.getBlockMetadata(x, y, z);
        int i6 = i5 & 7;
        boolean z7 = (i5 & 8) > 0;
        Tessellator tessellator8 = Tessellator.instance;
        boolean z9 = this.overrideBlockTexture >= 0;
        if(!z9) {
            this.overrideBlockTexture = Block.cobblestone.blockIndexInTexture;
        }

        float f10 = 0.25F;
        float f11 = 0.1875F;
        float f12 = 0.1875F;
        if(i6 == 5) {
            block.setBlockBounds(0.5F - f11, 0.0F, 0.5F - f10, 0.5F + f11, f12, 0.5F + f10);
        } else if(i6 == 6) {
            block.setBlockBounds(0.5F - f10, 0.0F, 0.5F - f11, 0.5F + f10, f12, 0.5F + f11);
        } else if(i6 == 4) {
            block.setBlockBounds(0.5F - f11, 0.5F - f10, 1.0F - f12, 0.5F + f11, 0.5F + f10, 1.0F);
        } else if(i6 == 3) {
            block.setBlockBounds(0.5F - f11, 0.5F - f10, 0.0F, 0.5F + f11, 0.5F + f10, f12);
        } else if(i6 == 2) {
            block.setBlockBounds(1.0F - f12, 0.5F - f10, 0.5F - f11, 1.0F, 0.5F + f10, 0.5F + f11);
        } else if(i6 == 1) {
            block.setBlockBounds(0.0F, 0.5F - f10, 0.5F - f11, f12, 0.5F + f10, 0.5F + f11);
        }

        this.renderStandardBlock(block, x, y, z);
        if(!z9) {
            this.overrideBlockTexture = -1;
        }

        float f13 = block.getBlockBrightness(this.blockAccess, x, y, z);
        if(Block.lightValue[block.blockID] > 0) {
            f13 = 1.0F;
        }

        tessellator8.setColorOpaque_F(f13, f13, f13);
        int i14 = block.getBlockTextureFromSide(0);
        if(this.overrideBlockTexture >= 0) {
            i14 = this.overrideBlockTexture;
        }

        int i15 = (i14 & 15) << 4;
        int i16 = i14 & 240;
        float f17 = (float)i15 / 256.0F;
        float f18 = ((float)i15 + 15.99F) / 256.0F;
        float f19 = (float)i16 / 256.0F;
        float f20 = ((float)i16 + 15.99F) / 256.0F;
        Vec3D[] vec3D21 = new Vec3D[8];
        float f22 = 0.0625F;
        float f23 = 0.0625F;
        float f24 = 0.625F;
        vec3D21[0] = Vec3D.createVector((double)(-f22), 0.0D, (double)(-f23));
        vec3D21[1] = Vec3D.createVector((double)f22, 0.0D, (double)(-f23));
        vec3D21[2] = Vec3D.createVector((double)f22, 0.0D, (double)f23);
        vec3D21[3] = Vec3D.createVector((double)(-f22), 0.0D, (double)f23);
        vec3D21[4] = Vec3D.createVector((double)(-f22), (double)f24, (double)(-f23));
        vec3D21[5] = Vec3D.createVector((double)f22, (double)f24, (double)(-f23));
        vec3D21[6] = Vec3D.createVector((double)f22, (double)f24, (double)f23);
        vec3D21[7] = Vec3D.createVector((double)(-f22), (double)f24, (double)f23);

        for(int i25 = 0; i25 < 8; ++i25) {
            if(z7) {
                vec3D21[i25].zCoord -= 0.0625D;
                vec3D21[i25].rotateAroundX((float)Math.PI / 4.5F);
            } else {
                vec3D21[i25].zCoord += 0.0625D;
                vec3D21[i25].rotateAroundX(-0.69813174F);
            }

            if(i6 == 6) {
                vec3D21[i25].rotateAroundY((float)Math.PI / 2F);
            }

            if(i6 < 5) {
                vec3D21[i25].yCoord -= 0.375D;
                vec3D21[i25].rotateAroundX((float)Math.PI / 2F);
                if(i6 == 4) {
                    vec3D21[i25].rotateAroundY(0.0F);
                }

                if(i6 == 3) {
                    vec3D21[i25].rotateAroundY((float)Math.PI);
                }

                if(i6 == 2) {
                    vec3D21[i25].rotateAroundY((float)Math.PI / 2F);
                }

                if(i6 == 1) {
                    vec3D21[i25].rotateAroundY(-1.5707964F);
                }

                vec3D21[i25].xCoord += (double)x + 0.5D;
                vec3D21[i25].yCoord += (double)((float)y + 0.5F);
                vec3D21[i25].zCoord += (double)z + 0.5D;
            } else {
                vec3D21[i25].xCoord += (double)x + 0.5D;
                vec3D21[i25].yCoord += (double)((float)y + 0.125F);
                vec3D21[i25].zCoord += (double)z + 0.5D;
            }
        }

        Vec3D vec3D30 = null;
        Vec3D vec3D26 = null;
        Vec3D vec3D27 = null;
        Vec3D vec3D28 = null;

        for(int i29 = 0; i29 < 6; ++i29) {
            if(i29 == 0) {
                f17 = (float)(i15 + 7) / 256.0F;
                f18 = ((float)(i15 + 9) - 0.01F) / 256.0F;
                f19 = (float)(i16 + 6) / 256.0F;
                f20 = ((float)(i16 + 8) - 0.01F) / 256.0F;
            } else if(i29 == 2) {
                f17 = (float)(i15 + 7) / 256.0F;
                f18 = ((float)(i15 + 9) - 0.01F) / 256.0F;
                f19 = (float)(i16 + 6) / 256.0F;
                f20 = ((float)(i16 + 16) - 0.01F) / 256.0F;
            }

            if(i29 == 0) {
                vec3D30 = vec3D21[0];
                vec3D26 = vec3D21[1];
                vec3D27 = vec3D21[2];
                vec3D28 = vec3D21[3];
            } else if(i29 == 1) {
                vec3D30 = vec3D21[7];
                vec3D26 = vec3D21[6];
                vec3D27 = vec3D21[5];
                vec3D28 = vec3D21[4];
            } else if(i29 == 2) {
                vec3D30 = vec3D21[1];
                vec3D26 = vec3D21[0];
                vec3D27 = vec3D21[4];
                vec3D28 = vec3D21[5];
            } else if(i29 == 3) {
                vec3D30 = vec3D21[2];
                vec3D26 = vec3D21[1];
                vec3D27 = vec3D21[5];
                vec3D28 = vec3D21[6];
            } else if(i29 == 4) {
                vec3D30 = vec3D21[3];
                vec3D26 = vec3D21[2];
                vec3D27 = vec3D21[6];
                vec3D28 = vec3D21[7];
            } else if(i29 == 5) {
                vec3D30 = vec3D21[0];
                vec3D26 = vec3D21[3];
                vec3D27 = vec3D21[7];
                vec3D28 = vec3D21[4];
            }

            tessellator8.addVertexWithUV(vec3D30.xCoord, vec3D30.yCoord, vec3D30.zCoord, (double)f17, (double)f20);
            tessellator8.addVertexWithUV(vec3D26.xCoord, vec3D26.yCoord, vec3D26.zCoord, (double)f18, (double)f20);
            tessellator8.addVertexWithUV(vec3D27.xCoord, vec3D27.yCoord, vec3D27.zCoord, (double)f18, (double)f19);
            tessellator8.addVertexWithUV(vec3D28.xCoord, vec3D28.yCoord, vec3D28.zCoord, (double)f17, (double)f19);
        }

        return true;
    }

    public boolean renderBlockFire(Block block, int x, int y, int z) {
        Tessellator tessellator5 = Tessellator.instance;
        int i6 = block.getBlockTextureFromSide(0);
        if(this.overrideBlockTexture >= 0) {
            i6 = this.overrideBlockTexture;
        }

        float f7 = block.getBlockBrightness(this.blockAccess, x, y, z);
        tessellator5.setColorOpaque_F(f7, f7, f7);
        int i8 = (i6 & 15) << 4;
        int i9 = i6 & 240;
        double d10 = (double)((float)i8 / 256.0F);
        double d12 = (double)(((float)i8 + 15.99F) / 256.0F);
        double d14 = (double)((float)i9 / 256.0F);
        double d16 = (double)(((float)i9 + 15.99F) / 256.0F);
        float f18 = 1.4F;
        double d21;
        double d23;
        double d25;
        double d27;
        double d29;
        double d31;
        double d33;
        if(!this.blockAccess.isBlockNormalCube(x, y - 1, z) && !Block.fire.canBlockCatchFire(this.blockAccess, x, y - 1, z)) {
            float f37 = 0.2F;
            float f20 = 0.0625F;
            if((x + y + z & 1) == 1) {
                d10 = (double)((float)i8 / 256.0F);
                d12 = (double)(((float)i8 + 15.99F) / 256.0F);
                d14 = (double)((float)(i9 + 16) / 256.0F);
                d16 = (double)(((float)i9 + 15.99F + 16.0F) / 256.0F);
            }

            if((x / 2 + y / 2 + z / 2 & 1) == 1) {
                d21 = d12;
                d12 = d10;
                d10 = d21;
            }

            if(Block.fire.canBlockCatchFire(this.blockAccess, x - 1, y, z)) {
                tessellator5.addVertexWithUV((double)((float)x + f37), (double)((float)y + f18 + f20), (double)(z + 1), d12, d14);
                tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f20), (double)(z + 1), d12, d16);
                tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f20), (double)(z + 0), d10, d16);
                tessellator5.addVertexWithUV((double)((float)x + f37), (double)((float)y + f18 + f20), (double)(z + 0), d10, d14);
                tessellator5.addVertexWithUV((double)((float)x + f37), (double)((float)y + f18 + f20), (double)(z + 0), d10, d14);
                tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f20), (double)(z + 0), d10, d16);
                tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f20), (double)(z + 1), d12, d16);
                tessellator5.addVertexWithUV((double)((float)x + f37), (double)((float)y + f18 + f20), (double)(z + 1), d12, d14);
            }

            if(Block.fire.canBlockCatchFire(this.blockAccess, x + 1, y, z)) {
                tessellator5.addVertexWithUV((double)((float)(x + 1) - f37), (double)((float)y + f18 + f20), (double)(z + 0), d10, d14);
                tessellator5.addVertexWithUV((double)(x + 1 - 0), (double)((float)(y + 0) + f20), (double)(z + 0), d10, d16);
                tessellator5.addVertexWithUV((double)(x + 1 - 0), (double)((float)(y + 0) + f20), (double)(z + 1), d12, d16);
                tessellator5.addVertexWithUV((double)((float)(x + 1) - f37), (double)((float)y + f18 + f20), (double)(z + 1), d12, d14);
                tessellator5.addVertexWithUV((double)((float)(x + 1) - f37), (double)((float)y + f18 + f20), (double)(z + 1), d12, d14);
                tessellator5.addVertexWithUV((double)(x + 1 - 0), (double)((float)(y + 0) + f20), (double)(z + 1), d12, d16);
                tessellator5.addVertexWithUV((double)(x + 1 - 0), (double)((float)(y + 0) + f20), (double)(z + 0), d10, d16);
                tessellator5.addVertexWithUV((double)((float)(x + 1) - f37), (double)((float)y + f18 + f20), (double)(z + 0), d10, d14);
            }

            if(Block.fire.canBlockCatchFire(this.blockAccess, x, y, z - 1)) {
                tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f18 + f20), (double)((float)z + f37), d12, d14);
                tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f20), (double)(z + 0), d12, d16);
                tessellator5.addVertexWithUV((double)(x + 1), (double)((float)(y + 0) + f20), (double)(z + 0), d10, d16);
                tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f18 + f20), (double)((float)z + f37), d10, d14);
                tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f18 + f20), (double)((float)z + f37), d10, d14);
                tessellator5.addVertexWithUV((double)(x + 1), (double)((float)(y + 0) + f20), (double)(z + 0), d10, d16);
                tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f20), (double)(z + 0), d12, d16);
                tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f18 + f20), (double)((float)z + f37), d12, d14);
            }

            if(Block.fire.canBlockCatchFire(this.blockAccess, x, y, z + 1)) {
                tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f18 + f20), (double)((float)(z + 1) - f37), d10, d14);
                tessellator5.addVertexWithUV((double)(x + 1), (double)((float)(y + 0) + f20), (double)(z + 1 - 0), d10, d16);
                tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f20), (double)(z + 1 - 0), d12, d16);
                tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f18 + f20), (double)((float)(z + 1) - f37), d12, d14);
                tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f18 + f20), (double)((float)(z + 1) - f37), d12, d14);
                tessellator5.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f20), (double)(z + 1 - 0), d12, d16);
                tessellator5.addVertexWithUV((double)(x + 1), (double)((float)(y + 0) + f20), (double)(z + 1 - 0), d10, d16);
                tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f18 + f20), (double)((float)(z + 1) - f37), d10, d14);
            }

            if(Block.fire.canBlockCatchFire(this.blockAccess, x, y + 1, z)) {
                d21 = (double)x + 0.5D + 0.5D;
                d23 = (double)x + 0.5D - 0.5D;
                d25 = (double)z + 0.5D + 0.5D;
                d27 = (double)z + 0.5D - 0.5D;
                d29 = (double)x + 0.5D - 0.5D;
                d31 = (double)x + 0.5D + 0.5D;
                d33 = (double)z + 0.5D - 0.5D;
                double d35 = (double)z + 0.5D + 0.5D;
                d10 = (double)((float)i8 / 256.0F);
                d12 = (double)(((float)i8 + 15.99F) / 256.0F);
                d14 = (double)((float)i9 / 256.0F);
                d16 = (double)(((float)i9 + 15.99F) / 256.0F);
                ++y;
                f18 = -0.2F;
                if((x + y + z & 1) == 0) {
                    tessellator5.addVertexWithUV(d29, (double)((float)y + f18), (double)(z + 0), d12, d14);
                    tessellator5.addVertexWithUV(d21, (double)(y + 0), (double)(z + 0), d12, d16);
                    tessellator5.addVertexWithUV(d21, (double)(y + 0), (double)(z + 1), d10, d16);
                    tessellator5.addVertexWithUV(d29, (double)((float)y + f18), (double)(z + 1), d10, d14);
                    d10 = (double)((float)i8 / 256.0F);
                    d12 = (double)(((float)i8 + 15.99F) / 256.0F);
                    d14 = (double)((float)(i9 + 16) / 256.0F);
                    d16 = (double)(((float)i9 + 15.99F + 16.0F) / 256.0F);
                    tessellator5.addVertexWithUV(d31, (double)((float)y + f18), (double)(z + 1), d12, d14);
                    tessellator5.addVertexWithUV(d23, (double)(y + 0), (double)(z + 1), d12, d16);
                    tessellator5.addVertexWithUV(d23, (double)(y + 0), (double)(z + 0), d10, d16);
                    tessellator5.addVertexWithUV(d31, (double)((float)y + f18), (double)(z + 0), d10, d14);
                } else {
                    tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f18), d35, d12, d14);
                    tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), d27, d12, d16);
                    tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), d27, d10, d16);
                    tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f18), d35, d10, d14);
                    d10 = (double)((float)i8 / 256.0F);
                    d12 = (double)(((float)i8 + 15.99F) / 256.0F);
                    d14 = (double)((float)(i9 + 16) / 256.0F);
                    d16 = (double)(((float)i9 + 15.99F + 16.0F) / 256.0F);
                    tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f18), d33, d12, d14);
                    tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), d25, d12, d16);
                    tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), d25, d10, d16);
                    tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f18), d33, d10, d14);
                }
            }
        } else {
            double d19 = (double)x + 0.5D + 0.2D;
            d21 = (double)x + 0.5D - 0.2D;
            d23 = (double)z + 0.5D + 0.2D;
            d25 = (double)z + 0.5D - 0.2D;
            d27 = (double)x + 0.5D - 0.3D;
            d29 = (double)x + 0.5D + 0.3D;
            d31 = (double)z + 0.5D - 0.3D;
            d33 = (double)z + 0.5D + 0.3D;
            tessellator5.addVertexWithUV(d27, (double)((float)y + f18), (double)(z + 1), d12, d14);
            tessellator5.addVertexWithUV(d19, (double)(y + 0), (double)(z + 1), d12, d16);
            tessellator5.addVertexWithUV(d19, (double)(y + 0), (double)(z + 0), d10, d16);
            tessellator5.addVertexWithUV(d27, (double)((float)y + f18), (double)(z + 0), d10, d14);
            tessellator5.addVertexWithUV(d29, (double)((float)y + f18), (double)(z + 0), d12, d14);
            tessellator5.addVertexWithUV(d21, (double)(y + 0), (double)(z + 0), d12, d16);
            tessellator5.addVertexWithUV(d21, (double)(y + 0), (double)(z + 1), d10, d16);
            tessellator5.addVertexWithUV(d29, (double)((float)y + f18), (double)(z + 1), d10, d14);
            d10 = (double)((float)i8 / 256.0F);
            d12 = (double)(((float)i8 + 15.99F) / 256.0F);
            d14 = (double)((float)(i9 + 16) / 256.0F);
            d16 = (double)(((float)i9 + 15.99F + 16.0F) / 256.0F);
            tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f18), d33, d12, d14);
            tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), d25, d12, d16);
            tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), d25, d10, d16);
            tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f18), d33, d10, d14);
            tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f18), d31, d12, d14);
            tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), d23, d12, d16);
            tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), d23, d10, d16);
            tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f18), d31, d10, d14);
            d19 = (double)x + 0.5D - 0.5D;
            d21 = (double)x + 0.5D + 0.5D;
            d23 = (double)z + 0.5D - 0.5D;
            d25 = (double)z + 0.5D + 0.5D;
            d27 = (double)x + 0.5D - 0.4D;
            d29 = (double)x + 0.5D + 0.4D;
            d31 = (double)z + 0.5D - 0.4D;
            d33 = (double)z + 0.5D + 0.4D;
            tessellator5.addVertexWithUV(d27, (double)((float)y + f18), (double)(z + 0), d10, d14);
            tessellator5.addVertexWithUV(d19, (double)(y + 0), (double)(z + 0), d10, d16);
            tessellator5.addVertexWithUV(d19, (double)(y + 0), (double)(z + 1), d12, d16);
            tessellator5.addVertexWithUV(d27, (double)((float)y + f18), (double)(z + 1), d12, d14);
            tessellator5.addVertexWithUV(d29, (double)((float)y + f18), (double)(z + 1), d10, d14);
            tessellator5.addVertexWithUV(d21, (double)(y + 0), (double)(z + 1), d10, d16);
            tessellator5.addVertexWithUV(d21, (double)(y + 0), (double)(z + 0), d12, d16);
            tessellator5.addVertexWithUV(d29, (double)((float)y + f18), (double)(z + 0), d12, d14);
            d10 = (double)((float)i8 / 256.0F);
            d12 = (double)(((float)i8 + 15.99F) / 256.0F);
            d14 = (double)((float)i9 / 256.0F);
            d16 = (double)(((float)i9 + 15.99F) / 256.0F);
            tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f18), d33, d10, d14);
            tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), d25, d10, d16);
            tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), d25, d12, d16);
            tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f18), d33, d12, d14);
            tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f18), d31, d10, d14);
            tessellator5.addVertexWithUV((double)(x + 1), (double)(y + 0), d23, d10, d16);
            tessellator5.addVertexWithUV((double)(x + 0), (double)(y + 0), d23, d12, d16);
            tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f18), d31, d12, d14);
        }

        return true;
    }

    public boolean renderBlockRedstoneWire(Block block, int x, int y, int z) {
        Tessellator tessellator5 = Tessellator.instance;
        int i6 = block.getBlockTextureFromSideAndMetadata(0, this.blockAccess.getBlockMetadata(x, y, z));
        if(this.overrideBlockTexture >= 0) {
            i6 = this.overrideBlockTexture;
        }

        float f7 = block.getBlockBrightness(this.blockAccess, x, y, z);
        tessellator5.setColorOpaque_F(f7, f7, f7);
        int i8 = (i6 & 15) << 4;
        int i9 = i6 & 240;
        double d10 = (double)((float)i8 / 256.0F);
        double d12 = (double)(((float)i8 + 15.99F) / 256.0F);
        double d14 = (double)((float)i9 / 256.0F);
        double d16 = (double)(((float)i9 + 15.99F) / 256.0F);
        float f18 = 0.0F;
        float f19 = 0.03125F;
        boolean z20 = this.blockAccess.getBlockId(x - 1, y, z) == Block.redstoneWire.blockID || !this.blockAccess.isBlockNormalCube(x - 1, y, z) && this.blockAccess.getBlockId(x - 1, y - 1, z) == Block.redstoneWire.blockID;
        boolean z21 = this.blockAccess.getBlockId(x + 1, y, z) == Block.redstoneWire.blockID || !this.blockAccess.isBlockNormalCube(x + 1, y, z) && this.blockAccess.getBlockId(x + 1, y - 1, z) == Block.redstoneWire.blockID;
        boolean z22 = this.blockAccess.getBlockId(x, y, z - 1) == Block.redstoneWire.blockID || !this.blockAccess.isBlockNormalCube(x, y, z - 1) && this.blockAccess.getBlockId(x, y - 1, z - 1) == Block.redstoneWire.blockID;
        boolean z23 = this.blockAccess.getBlockId(x, y, z + 1) == Block.redstoneWire.blockID || !this.blockAccess.isBlockNormalCube(x, y, z + 1) && this.blockAccess.getBlockId(x, y - 1, z + 1) == Block.redstoneWire.blockID;
        if(this.blockAccess.isBlockNormalCube(x - 1, y, z) && this.blockAccess.getBlockId(x - 1, y + 1, z) == Block.redstoneWire.blockID) {
            z20 = true;
        }

        if(this.blockAccess.isBlockNormalCube(x + 1, y, z) && this.blockAccess.getBlockId(x + 1, y + 1, z) == Block.redstoneWire.blockID) {
            z21 = true;
        }

        if(this.blockAccess.isBlockNormalCube(x, y, z - 1) && this.blockAccess.getBlockId(x, y + 1, z - 1) == Block.redstoneWire.blockID) {
            z22 = true;
        }

        if(this.blockAccess.isBlockNormalCube(x, y, z + 1) && this.blockAccess.getBlockId(x, y + 1, z + 1) == Block.redstoneWire.blockID) {
            z23 = true;
        }

        float f24 = 0.3125F;
        float f25 = (float)(x + 0);
        float f26 = (float)(x + 1);
        float f27 = (float)(z + 0);
        float f28 = (float)(z + 1);
        byte b29 = 0;
        if((z20 || z21) && !z22 && !z23) {
            b29 = 1;
        }

        if((z22 || z23) && !z21 && !z20) {
            b29 = 2;
        }

        if(b29 != 0) {
            d10 = (double)((float)(i8 + 16) / 256.0F);
            d12 = (double)(((float)(i8 + 16) + 15.99F) / 256.0F);
            d14 = (double)((float)i9 / 256.0F);
            d16 = (double)(((float)i9 + 15.99F) / 256.0F);
        }

        if(b29 == 0) {
            if(z21 || z22 || z23 || z20) {
                if(!z20) {
                    f25 += f24;
                }

                if(!z20) {
                    d10 += (double)(f24 / 16.0F);
                }

                if(!z21) {
                    f26 -= f24;
                }

                if(!z21) {
                    d12 -= (double)(f24 / 16.0F);
                }

                if(!z22) {
                    f27 += f24;
                }

                if(!z22) {
                    d14 += (double)(f24 / 16.0F);
                }

                if(!z23) {
                    f28 -= f24;
                }

                if(!z23) {
                    d16 -= (double)(f24 / 16.0F);
                }
            }

            tessellator5.addVertexWithUV((double)(f26 + f18), (double)((float)y + f19), (double)(f28 + f18), d12, d16);
            tessellator5.addVertexWithUV((double)(f26 + f18), (double)((float)y + f19), (double)(f27 - f18), d12, d14);
            tessellator5.addVertexWithUV((double)(f25 - f18), (double)((float)y + f19), (double)(f27 - f18), d10, d14);
            tessellator5.addVertexWithUV((double)(f25 - f18), (double)((float)y + f19), (double)(f28 + f18), d10, d16);
        }

        if(b29 == 1) {
            tessellator5.addVertexWithUV((double)(f26 + f18), (double)((float)y + f19), (double)(f28 + f18), d12, d16);
            tessellator5.addVertexWithUV((double)(f26 + f18), (double)((float)y + f19), (double)(f27 - f18), d12, d14);
            tessellator5.addVertexWithUV((double)(f25 - f18), (double)((float)y + f19), (double)(f27 - f18), d10, d14);
            tessellator5.addVertexWithUV((double)(f25 - f18), (double)((float)y + f19), (double)(f28 + f18), d10, d16);
        }

        if(b29 == 2) {
            tessellator5.addVertexWithUV((double)(f26 + f18), (double)((float)y + f19), (double)(f28 + f18), d12, d16);
            tessellator5.addVertexWithUV((double)(f26 + f18), (double)((float)y + f19), (double)(f27 - f18), d10, d16);
            tessellator5.addVertexWithUV((double)(f25 - f18), (double)((float)y + f19), (double)(f27 - f18), d10, d14);
            tessellator5.addVertexWithUV((double)(f25 - f18), (double)((float)y + f19), (double)(f28 + f18), d12, d14);
        }

        d10 = (double)((float)(i8 + 16) / 256.0F);
        d12 = (double)(((float)(i8 + 16) + 15.99F) / 256.0F);
        d14 = (double)((float)i9 / 256.0F);
        d16 = (double)(((float)i9 + 15.99F) / 256.0F);
        if(this.blockAccess.isBlockNormalCube(x - 1, y, z) && this.blockAccess.getBlockId(x - 1, y + 1, z) == Block.redstoneWire.blockID) {
            tessellator5.addVertexWithUV((double)((float)x + f19), (double)((float)(y + 1) + f18), (double)((float)(z + 1) + f18), d12, d14);
            tessellator5.addVertexWithUV((double)((float)x + f19), (double)((float)(y + 0) - f18), (double)((float)(z + 1) + f18), d10, d14);
            tessellator5.addVertexWithUV((double)((float)x + f19), (double)((float)(y + 0) - f18), (double)((float)(z + 0) - f18), d10, d16);
            tessellator5.addVertexWithUV((double)((float)x + f19), (double)((float)(y + 1) + f18), (double)((float)(z + 0) - f18), d12, d16);
        }

        if(this.blockAccess.isBlockNormalCube(x + 1, y, z) && this.blockAccess.getBlockId(x + 1, y + 1, z) == Block.redstoneWire.blockID) {
            tessellator5.addVertexWithUV((double)((float)(x + 1) - f19), (double)((float)(y + 0) - f18), (double)((float)(z + 1) + f18), d10, d16);
            tessellator5.addVertexWithUV((double)((float)(x + 1) - f19), (double)((float)(y + 1) + f18), (double)((float)(z + 1) + f18), d12, d16);
            tessellator5.addVertexWithUV((double)((float)(x + 1) - f19), (double)((float)(y + 1) + f18), (double)((float)(z + 0) - f18), d12, d14);
            tessellator5.addVertexWithUV((double)((float)(x + 1) - f19), (double)((float)(y + 0) - f18), (double)((float)(z + 0) - f18), d10, d14);
        }

        if(this.blockAccess.isBlockNormalCube(x, y, z - 1) && this.blockAccess.getBlockId(x, y + 1, z - 1) == Block.redstoneWire.blockID) {
            tessellator5.addVertexWithUV((double)((float)(x + 1) + f18), (double)((float)(y + 0) - f18), (double)((float)z + f19), d10, d16);
            tessellator5.addVertexWithUV((double)((float)(x + 1) + f18), (double)((float)(y + 1) + f18), (double)((float)z + f19), d12, d16);
            tessellator5.addVertexWithUV((double)((float)(x + 0) - f18), (double)((float)(y + 1) + f18), (double)((float)z + f19), d12, d14);
            tessellator5.addVertexWithUV((double)((float)(x + 0) - f18), (double)((float)(y + 0) - f18), (double)((float)z + f19), d10, d14);
        }

        if(this.blockAccess.isBlockNormalCube(x, y, z + 1) && this.blockAccess.getBlockId(x, y + 1, z + 1) == Block.redstoneWire.blockID) {
            tessellator5.addVertexWithUV((double)((float)(x + 1) + f18), (double)((float)(y + 1) + f18), (double)((float)(z + 1) - f19), d12, d14);
            tessellator5.addVertexWithUV((double)((float)(x + 1) + f18), (double)((float)(y + 0) - f18), (double)((float)(z + 1) - f19), d10, d14);
            tessellator5.addVertexWithUV((double)((float)(x + 0) - f18), (double)((float)(y + 0) - f18), (double)((float)(z + 1) - f19), d10, d16);
            tessellator5.addVertexWithUV((double)((float)(x + 0) - f18), (double)((float)(y + 1) + f18), (double)((float)(z + 1) - f19), d12, d16);
        }

        return true;
    }

    public boolean renderBlockMinecartTrack(Block block, int x, int y, int z) {
        Tessellator tessellator5 = Tessellator.instance;
        int i6 = this.blockAccess.getBlockMetadata(x, y, z);
        int i7 = block.getBlockTextureFromSideAndMetadata(0, i6);
        if(this.overrideBlockTexture >= 0) {
            i7 = this.overrideBlockTexture;
        }

        float f8 = block.getBlockBrightness(this.blockAccess, x, y, z);
        tessellator5.setColorOpaque_F(f8, f8, f8);
        int i9 = (i7 & 15) << 4;
        int i10 = i7 & 240;
        double d11 = (double)((float)i9 / 256.0F);
        double d13 = (double)(((float)i9 + 15.99F) / 256.0F);
        double d15 = (double)((float)i10 / 256.0F);
        double d17 = (double)(((float)i10 + 15.99F) / 256.0F);
        float f19 = 0.0625F;
        float f20 = (float)(x + 1);
        float f21 = (float)(x + 1);
        float f22 = (float)(x + 0);
        float f23 = (float)(x + 0);
        float f24 = (float)(z + 0);
        float f25 = (float)(z + 1);
        float f26 = (float)(z + 1);
        float f27 = (float)(z + 0);
        float f28 = (float)y + f19;
        float f29 = (float)y + f19;
        float f30 = (float)y + f19;
        float f31 = (float)y + f19;
        if(i6 != 1 && i6 != 2 && i6 != 3 && i6 != 7) {
            if(i6 == 8) {
                f20 = f21 = (float)(x + 0);
                f22 = f23 = (float)(x + 1);
                f24 = f27 = (float)(z + 1);
                f25 = f26 = (float)(z + 0);
            } else if(i6 == 9) {
                f20 = f23 = (float)(x + 0);
                f21 = f22 = (float)(x + 1);
                f24 = f25 = (float)(z + 0);
                f26 = f27 = (float)(z + 1);
            }
        } else {
            f20 = f23 = (float)(x + 1);
            f21 = f22 = (float)(x + 0);
            f24 = f25 = (float)(z + 1);
            f26 = f27 = (float)(z + 0);
        }

        if(i6 != 2 && i6 != 4) {
            if(i6 == 3 || i6 == 5) {
                ++f29;
                ++f30;
            }
        } else {
            ++f28;
            ++f31;
        }

        tessellator5.addVertexWithUV((double)f20, (double)f28, (double)f24, d13, d15);
        tessellator5.addVertexWithUV((double)f21, (double)f29, (double)f25, d13, d17);
        tessellator5.addVertexWithUV((double)f22, (double)f30, (double)f26, d11, d17);
        tessellator5.addVertexWithUV((double)f23, (double)f31, (double)f27, d11, d15);
        tessellator5.addVertexWithUV((double)f23, (double)f31, (double)f27, d11, d15);
        tessellator5.addVertexWithUV((double)f22, (double)f30, (double)f26, d11, d17);
        tessellator5.addVertexWithUV((double)f21, (double)f29, (double)f25, d13, d17);
        tessellator5.addVertexWithUV((double)f20, (double)f28, (double)f24, d13, d15);
        return true;
    }

    public boolean renderBlockLadder(Block block, int x, int y, int z) {
        Tessellator tessellator5 = Tessellator.instance;
        int i6 = block.getBlockTextureFromSide(0);
        if(this.overrideBlockTexture >= 0) {
            i6 = this.overrideBlockTexture;
        }

        float f7 = block.getBlockBrightness(this.blockAccess, x, y, z);
        tessellator5.setColorOpaque_F(f7, f7, f7);
        int i8 = (i6 & 15) << 4;
        int i9 = i6 & 240;
        double d10 = (double)((float)i8 / 256.0F);
        double d12 = (double)(((float)i8 + 15.99F) / 256.0F);
        double d14 = (double)((float)i9 / 256.0F);
        double d16 = (double)(((float)i9 + 15.99F) / 256.0F);
        int i18 = this.blockAccess.getBlockMetadata(x, y, z);
        float f19 = 0.0F;
        float f20 = 0.05F;
        if(i18 == 5) {
            tessellator5.addVertexWithUV((double)((float)x + f20), (double)((float)(y + 1) + f19), (double)((float)(z + 1) + f19), d10, d14);
            tessellator5.addVertexWithUV((double)((float)x + f20), (double)((float)(y + 0) - f19), (double)((float)(z + 1) + f19), d10, d16);
            tessellator5.addVertexWithUV((double)((float)x + f20), (double)((float)(y + 0) - f19), (double)((float)(z + 0) - f19), d12, d16);
            tessellator5.addVertexWithUV((double)((float)x + f20), (double)((float)(y + 1) + f19), (double)((float)(z + 0) - f19), d12, d14);
        }

        if(i18 == 4) {
            tessellator5.addVertexWithUV((double)((float)(x + 1) - f20), (double)((float)(y + 0) - f19), (double)((float)(z + 1) + f19), d12, d16);
            tessellator5.addVertexWithUV((double)((float)(x + 1) - f20), (double)((float)(y + 1) + f19), (double)((float)(z + 1) + f19), d12, d14);
            tessellator5.addVertexWithUV((double)((float)(x + 1) - f20), (double)((float)(y + 1) + f19), (double)((float)(z + 0) - f19), d10, d14);
            tessellator5.addVertexWithUV((double)((float)(x + 1) - f20), (double)((float)(y + 0) - f19), (double)((float)(z + 0) - f19), d10, d16);
        }

        if(i18 == 3) {
            tessellator5.addVertexWithUV((double)((float)(x + 1) + f19), (double)((float)(y + 0) - f19), (double)((float)z + f20), d12, d16);
            tessellator5.addVertexWithUV((double)((float)(x + 1) + f19), (double)((float)(y + 1) + f19), (double)((float)z + f20), d12, d14);
            tessellator5.addVertexWithUV((double)((float)(x + 0) - f19), (double)((float)(y + 1) + f19), (double)((float)z + f20), d10, d14);
            tessellator5.addVertexWithUV((double)((float)(x + 0) - f19), (double)((float)(y + 0) - f19), (double)((float)z + f20), d10, d16);
        }

        if(i18 == 2) {
            tessellator5.addVertexWithUV((double)((float)(x + 1) + f19), (double)((float)(y + 1) + f19), (double)((float)(z + 1) - f20), d10, d14);
            tessellator5.addVertexWithUV((double)((float)(x + 1) + f19), (double)((float)(y + 0) - f19), (double)((float)(z + 1) - f20), d10, d16);
            tessellator5.addVertexWithUV((double)((float)(x + 0) - f19), (double)((float)(y + 0) - f19), (double)((float)(z + 1) - f20), d12, d16);
            tessellator5.addVertexWithUV((double)((float)(x + 0) - f19), (double)((float)(y + 1) + f19), (double)((float)(z + 1) - f20), d12, d14);
        }

        return true;
    }

    public boolean renderBlockReed(Block block, int x, int y, int z) {
        Tessellator tessellator5 = Tessellator.instance;
        float f6 = block.getBlockBrightness(this.blockAccess, x, y, z);
        tessellator5.setColorOpaque_F(f6, f6, f6);
        this.renderCrossedSquares(block, this.blockAccess.getBlockMetadata(x, y, z), (double)x, (double)y, (double)z);
        return true;
    }

    public boolean renderBlockCrops(Block block, int x, int y, int z) {
        Tessellator tessellator5 = Tessellator.instance;
        float f6 = block.getBlockBrightness(this.blockAccess, x, y, z);
        tessellator5.setColorOpaque_F(f6, f6, f6);
        this.renderBlockCropsImpl(block, this.blockAccess.getBlockMetadata(x, y, z), (double)x, (double)((float)y - 0.0625F), (double)z);
        return true;
    }

	private void renderTorchAtAngle(Block block, double x, double y, double z, double offsetX, double offsetZ) {
		Tessellator tessellator12 = Tessellator.instance;
		int block1 = block.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			block1 = this.overrideBlockTexture;
		}

		int i13 = (block1 & 15) << 4;
		block1 &= 240;
		float f14 = (float)i13 / 256.0F;
		float f38 = ((float)i13 + 15.99F) / 256.0F;
		float f15 = (float)block1 / 256.0F;
		float block2 = ((float)block1 + 15.99F) / 256.0F;
		double d20 = (double)f14 + 7.0D / 256D;
		double d22 = (double)f15 + 6.0D / 256D;
		double d24 = (double)f14 + 9.0D / 256D;
		double d26 = (double)f15 + 8.0D / 256D;
		x += 0.5D;
		z += 0.5D;
		double d28 = x - 0.5D;
		double d30 = x + 0.5D;
		double d32 = z - 0.5D;
		double d34 = z + 0.5D;
		tessellator12.addVertexWithUV(x + offsetX * 0.375D - 0.0625D, y + 0.625D, z + offsetZ * 0.375D - 0.0625D, d20, d22);
		tessellator12.addVertexWithUV(x + offsetX * 0.375D - 0.0625D, y + 0.625D, z + offsetZ * 0.375D + 0.0625D, d20, d26);
		tessellator12.addVertexWithUV(x + offsetX * 0.375D + 0.0625D, y + 0.625D, z + offsetZ * 0.375D + 0.0625D, d24, d26);
		tessellator12.addVertexWithUV(x + offsetX * 0.375D + 0.0625D, y + 0.625D, z + offsetZ * 0.375D - 0.0625D, d24, d22);
		tessellator12.addVertexWithUV(x - 0.0625D, y + 1.0D, d32, (double)f14, (double)f15);
		tessellator12.addVertexWithUV(x - 0.0625D + offsetX, y, d32 + offsetZ, (double)f14, (double)block2);
		tessellator12.addVertexWithUV(x - 0.0625D + offsetX, y, d34 + offsetZ, (double)f38, (double)block2);
		tessellator12.addVertexWithUV(x - 0.0625D, y + 1.0D, d34, (double)f38, (double)f15);
		tessellator12.addVertexWithUV(x + 0.0625D, y + 1.0D, d34, (double)f14, (double)f15);
		tessellator12.addVertexWithUV(x + offsetX + 0.0625D, y, d34 + offsetZ, (double)f14, (double)block2);
		tessellator12.addVertexWithUV(x + offsetX + 0.0625D, y, d32 + offsetZ, (double)f38, (double)block2);
		tessellator12.addVertexWithUV(x + 0.0625D, y + 1.0D, d32, (double)f38, (double)f15);
		tessellator12.addVertexWithUV(d28, y + 1.0D, z + 0.0625D, (double)f14, (double)f15);
		tessellator12.addVertexWithUV(d28 + offsetX, y, z + 0.0625D + offsetZ, (double)f14, (double)block2);
		tessellator12.addVertexWithUV(d30 + offsetX, y, z + 0.0625D + offsetZ, (double)f38, (double)block2);
		tessellator12.addVertexWithUV(d30, y + 1.0D, z + 0.0625D, (double)f38, (double)f15);
		tessellator12.addVertexWithUV(d30, y + 1.0D, z - 0.0625D, (double)f14, (double)f15);
		tessellator12.addVertexWithUV(d30 + offsetX, y, z - 0.0625D + offsetZ, (double)f14, (double)block2);
		tessellator12.addVertexWithUV(d28 + offsetX, y, z - 0.0625D + offsetZ, (double)f38, (double)block2);
		tessellator12.addVertexWithUV(d28, y + 1.0D, z - 0.0625D, (double)f38, (double)f15);
	}

	private void renderCrossedSquares(Block block, int metadata, double x, double y, double z) {
		Tessellator tessellator9 = Tessellator.instance;
		int block1 = block.getBlockTextureFromSideAndMetadata(0, metadata);
		if(this.overrideBlockTexture >= 0) {
			block1 = this.overrideBlockTexture;
		}

		metadata = (block1 & 15) << 4;
		block1 &= 240;
		double d13 = (double)((float)metadata / 256.0F);
		double d15 = (double)(((float)metadata + 15.99F) / 256.0F);
		double d17 = (double)((float)block1 / 256.0F);
		double d19 = (double)(((float)block1 + 15.99F) / 256.0F);
		double d21 = x + 0.5D - (double)0.45F;
		double d23 = x + 0.5D + (double)0.45F;
		double d25 = z + 0.5D - (double)0.45F;
		double d27 = z + 0.5D + (double)0.45F;
		tessellator9.addVertexWithUV(d21, y + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d21, y, d25, d13, d19);
		tessellator9.addVertexWithUV(d23, y, d27, d15, d19);
		tessellator9.addVertexWithUV(d23, y + 1.0D, d27, d15, d17);
		tessellator9.addVertexWithUV(d23, y + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d23, y, d27, d13, d19);
		tessellator9.addVertexWithUV(d21, y, d25, d15, d19);
		tessellator9.addVertexWithUV(d21, y + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d21, y + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d21, y, d27, d13, d19);
		tessellator9.addVertexWithUV(d23, y, d25, d15, d19);
		tessellator9.addVertexWithUV(d23, y + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, y + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d23, y, d25, d13, d19);
		tessellator9.addVertexWithUV(d21, y, d27, d15, d19);
		tessellator9.addVertexWithUV(d21, y + 1.0D, d27, d15, d17);
	}

	private void renderBlockCropsImpl(Block block, int metadata, double x, double y, double z) {
		Tessellator tessellator9 = Tessellator.instance;
		int block1 = block.getBlockTextureFromSideAndMetadata(0, metadata);
		if(this.overrideBlockTexture >= 0) {
			block1 = this.overrideBlockTexture;
		}

		metadata = (block1 & 15) << 4;
		block1 &= 240;
		double d13 = (double)((float)metadata / 256.0F);
		double d15 = (double)(((float)metadata + 15.99F) / 256.0F);
		double d17 = (double)((float)block1 / 256.0F);
		double d19 = (double)(((float)block1 + 15.99F) / 256.0F);
		double d21 = x + 0.5D - 0.25D;
		double d23 = x + 0.5D + 0.25D;
		double d25 = z + 0.5D - 0.5D;
		double d27 = z + 0.5D + 0.5D;
		tessellator9.addVertexWithUV(d21, y + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d21, y, d25, d13, d19);
		tessellator9.addVertexWithUV(d21, y, d27, d15, d19);
		tessellator9.addVertexWithUV(d21, y + 1.0D, d27, d15, d17);
		tessellator9.addVertexWithUV(d21, y + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d21, y, d27, d13, d19);
		tessellator9.addVertexWithUV(d21, y, d25, d15, d19);
		tessellator9.addVertexWithUV(d21, y + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, y + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d23, y, d27, d13, d19);
		tessellator9.addVertexWithUV(d23, y, d25, d15, d19);
		tessellator9.addVertexWithUV(d23, y + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, y + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d23, y, d25, d13, d19);
		tessellator9.addVertexWithUV(d23, y, d27, d15, d19);
		tessellator9.addVertexWithUV(d23, y + 1.0D, d27, d15, d17);
		d21 = x + 0.5D - 0.5D;
		d23 = x + 0.5D + 0.5D;
		d25 = z + 0.5D - 0.25D;
		d27 = z + 0.5D + 0.25D;
		tessellator9.addVertexWithUV(d21, y + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d21, y, d25, d13, d19);
		tessellator9.addVertexWithUV(d23, y, d25, d15, d19);
		tessellator9.addVertexWithUV(d23, y + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, y + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d23, y, d25, d13, d19);
		tessellator9.addVertexWithUV(d21, y, d25, d15, d19);
		tessellator9.addVertexWithUV(d21, y + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, y + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d23, y, d27, d13, d19);
		tessellator9.addVertexWithUV(d21, y, d27, d15, d19);
		tessellator9.addVertexWithUV(d21, y + 1.0D, d27, d15, d17);
		tessellator9.addVertexWithUV(d21, y + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d21, y, d27, d13, d19);
		tessellator9.addVertexWithUV(d23, y, d27, d15, d19);
		tessellator9.addVertexWithUV(d23, y + 1.0D, d27, d15, d17);
	}

    public boolean renderBlockFluids(Block block, int x, int y, int z) {
        Tessellator tessellator5 = Tessellator.instance;
        boolean z6 = block.shouldSideBeRendered(this.blockAccess, x, y + 1, z, 1);
        boolean z7 = block.shouldSideBeRendered(this.blockAccess, x, y - 1, z, 0);
        boolean[] z8 = new boolean[]{block.shouldSideBeRendered(this.blockAccess, x, y, z - 1, 2), block.shouldSideBeRendered(this.blockAccess, x, y, z + 1, 3), block.shouldSideBeRendered(this.blockAccess, x - 1, y, z, 4), block.shouldSideBeRendered(this.blockAccess, x + 1, y, z, 5)};
        if(!z6 && !z7 && !z8[0] && !z8[1] && !z8[2] && !z8[3]) {
            return false;
        } else {
            boolean z9 = false;
            float f10 = 0.5F;
            float f11 = 1.0F;
            float f12 = 0.8F;
            float f13 = 0.6F;
            double d14 = 0.0D;
            double d16 = 1.0D;
            Material material18 = block.material;
            int i19 = this.blockAccess.getBlockMetadata(x, y, z);
            float f20 = this.getFluidHeight(x, y, z, material18);
            float f21 = this.getFluidHeight(x, y, z + 1, material18);
            float f22 = this.getFluidHeight(x + 1, y, z + 1, material18);
            float f23 = this.getFluidHeight(x + 1, y, z, material18);
            int i24;
            int i27;
            float f32;
            float f33;
            float f34;
            if(this.renderAllFaces || z6) {
                z9 = true;
                i24 = block.getBlockTextureFromSideAndMetadata(1, i19);
                float f25 = (float)BlockFluid.getFlowDirection(this.blockAccess, x, y, z, material18);
                if(f25 > -999.0F) {
                    i24 = block.getBlockTextureFromSideAndMetadata(2, i19);
                }

                int i26 = (i24 & 15) << 4;
                i27 = i24 & 240;
                double d28 = ((double)i26 + 8.0D) / 256.0D;
                double d30 = ((double)i27 + 8.0D) / 256.0D;
                if(f25 < -999.0F) {
                    f25 = 0.0F;
                } else {
                    d28 = (double)((float)(i26 + 16) / 256.0F);
                    d30 = (double)((float)(i27 + 16) / 256.0F);
                }

                f32 = MathHelper.sin(f25) * 8.0F / 256.0F;
                f33 = MathHelper.cos(f25) * 8.0F / 256.0F;
                f34 = block.getBlockBrightness(this.blockAccess, x, y, z);
                tessellator5.setColorOpaque_F(f11 * f34, f11 * f34, f11 * f34);
                tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f20), (double)(z + 0), d28 - (double)f33 - (double)f32, d30 - (double)f33 + (double)f32);
                tessellator5.addVertexWithUV((double)(x + 0), (double)((float)y + f21), (double)(z + 1), d28 - (double)f33 + (double)f32, d30 + (double)f33 + (double)f32);
                tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f22), (double)(z + 1), d28 + (double)f33 + (double)f32, d30 + (double)f33 - (double)f32);
                tessellator5.addVertexWithUV((double)(x + 1), (double)((float)y + f23), (double)(z + 0), d28 + (double)f33 - (double)f32, d30 - (double)f33 - (double)f32);
            }

            if(this.renderAllFaces || z7) {
                float f48 = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
                tessellator5.setColorOpaque_F(f10 * f48, f10 * f48, f10 * f48);
                this.renderBottomFace(block, (double)x, (double)y, (double)z, block.getBlockTextureFromSide(0));
                z9 = true;
            }

            for(i24 = 0; i24 < 4; ++i24) {
                int i49 = x;
                i27 = z;
                if(i24 == 0) {
                    i27 = z - 1;
                }

                if(i24 == 1) {
                    ++i27;
                }

                if(i24 == 2) {
                    i49 = x - 1;
                }

                if(i24 == 3) {
                    ++i49;
                }

                int i50 = block.getBlockTextureFromSideAndMetadata(i24 + 2, i19);
                int i29 = (i50 & 15) << 4;
                int i51 = i50 & 240;
                if(this.renderAllFaces || z8[i24]) {
                    float f31;
                    float f35;
                    float f36;
                    if(i24 == 0) {
                        f31 = f20;
                        f32 = f23;
                        f33 = (float)x;
                        f35 = (float)(x + 1);
                        f34 = (float)z;
                        f36 = (float)z;
                    } else if(i24 == 1) {
                        f31 = f22;
                        f32 = f21;
                        f33 = (float)(x + 1);
                        f35 = (float)x;
                        f34 = (float)(z + 1);
                        f36 = (float)(z + 1);
                    } else if(i24 == 2) {
                        f31 = f21;
                        f32 = f20;
                        f33 = (float)x;
                        f35 = (float)x;
                        f34 = (float)(z + 1);
                        f36 = (float)z;
                    } else {
                        f31 = f23;
                        f32 = f22;
                        f33 = (float)(x + 1);
                        f35 = (float)(x + 1);
                        f34 = (float)z;
                        f36 = (float)(z + 1);
                    }

                    z9 = true;
                    double d37 = (double)((float)(i29 + 0) / 256.0F);
                    double d39 = ((double)(i29 + 16) - 0.01D) / 256.0D;
                    double d41 = (double)(((float)i51 + (1.0F - f31) * 16.0F) / 256.0F);
                    double d43 = (double)(((float)i51 + (1.0F - f32) * 16.0F) / 256.0F);
                    double d45 = ((double)(i51 + 16) - 0.01D) / 256.0D;
                    float f47 = block.getBlockBrightness(this.blockAccess, i49, y, i27);
                    if(i24 < 2) {
                        f47 *= f12;
                    } else {
                        f47 *= f13;
                    }

                    tessellator5.setColorOpaque_F(f11 * f47, f11 * f47, f11 * f47);
                    tessellator5.addVertexWithUV((double)f33, (double)((float)y + f31), (double)f34, d37, d41);
                    tessellator5.addVertexWithUV((double)f35, (double)((float)y + f32), (double)f36, d39, d43);
                    tessellator5.addVertexWithUV((double)f35, (double)(y + 0), (double)f36, d39, d45);
                    tessellator5.addVertexWithUV((double)f33, (double)(y + 0), (double)f34, d37, d45);
                }
            }

            block.minY = d14;
            block.maxY = d16;
            return z9;
        }
    }

    private float getFluidHeight(int x, int y, int z, Material material) {
        int i5 = 0;
        float f6 = 0.0F;

        for(int i7 = 0; i7 < 4; ++i7) {
            int i8 = x - (i7 & 1);
            int i9 = z - (i7 >> 1 & 1);
            if(this.blockAccess.getBlockMaterial(i8, y + 1, i9) == material) {
                return 1.0F;
            }

            Material material10;
            if((material10 = this.blockAccess.getBlockMaterial(i8, y, i9)) != material) {
                if(!material10.isSolid()) {
                    ++f6;
                    ++i5;
                }
            } else {
                if((i8 = this.blockAccess.getBlockMetadata(i8, y, i9)) >= 8 || i8 == 0) {
                    f6 += BlockFluid.getFluidHeightPercent(i8) * 10.0F;
                    i5 += 10;
                }

                f6 += BlockFluid.getFluidHeightPercent(i8);
                ++i5;
            }
        }

        return 1.0F - f6 / (float)i5;
    }

    public void renderBlockFallingSand(Block block, World world, int x, int y, int z) {
        Tessellator tessellator6 = Tessellator.instance;
        Tessellator.instance.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
        float f7 = block.getBlockBrightness(world, x, y, z);
        float f8;
        if((f8 = block.getBlockBrightness(world, x, y - 1, z)) < f7) {
            f8 = f7;
        }

        tessellator6.setColorOpaque_F(0.5F * f8, 0.5F * f8, 0.5F * f8);
        this.renderBottomFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(0));
        if((f8 = block.getBlockBrightness(world, x, y + 1, z)) < f7) {
            f8 = f7;
        }

        tessellator6.setColorOpaque_F(f8 * 1.0F, f8 * 1.0F, f8 * 1.0F);
        this.renderTopFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(1));
        if((f8 = block.getBlockBrightness(world, x, y, z - 1)) < f7) {
            f8 = f7;
        }

        tessellator6.setColorOpaque_F(0.8F * f8, 0.8F * f8, 0.8F * f8);
        this.renderEastFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(2));
        if((f8 = block.getBlockBrightness(world, x, y, z + 1)) < f7) {
            f8 = f7;
        }

        tessellator6.setColorOpaque_F(0.8F * f8, 0.8F * f8, 0.8F * f8);
        this.renderWestFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(3));
        if((f8 = block.getBlockBrightness(world, x - 1, y, z)) < f7) {
            f8 = f7;
        }

        tessellator6.setColorOpaque_F(0.6F * f8, 0.6F * f8, 0.6F * f8);
        this.renderNorthFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(4));
        if((f8 = block.getBlockBrightness(world, x + 1, y, z)) < f7) {
            f8 = f7;
        }

        tessellator6.setColorOpaque_F(0.6F * f8, 0.6F * f8, 0.6F * f8);
        this.renderSouthFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(5));
        tessellator6.draw();
    }

    public boolean renderStandardBlock(Block block, int x, int y, int z) {
        int i5 = block.colorMultiplier(this.blockAccess, x, y, z);
        float f6 = (float)(i5 >> 16 & 255) / 255.0F;
        float f7 = (float)(i5 >> 8 & 255) / 255.0F;
        float f8 = (float)(i5 & 255) / 255.0F;
        return this.renderStandardBlockWithColorMultiplier(block, x, y, z, f6, f7, f8);
    }

    public boolean renderStandardBlockWithColorMultiplier(Block block, int x, int y, int z, float r, float g, float b) {
        Tessellator tessellator8 = Tessellator.instance;
        boolean z9 = false;
        float f10 = 0.5F;
        float f11 = 1.0F;
        float f12 = 0.8F;
        float f13 = 0.6F;
        float f14 = f10 * r;
        float f15 = f11 * r;
        float f16 = f12 * r;
        float f17 = f13 * r;
        float f18 = f10 * g;
        float f19 = f11 * g;
        float f20 = f12 * g;
        float f21 = f13 * g;
        float f22 = f10 * b;
        float f23 = f11 * b;
        float f24 = f12 * b;
        float f25 = f13 * b;
        float f26 = block.getBlockBrightness(this.blockAccess, x, y, z);
        float f27;
        if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y - 1, z, 0)) {
            f27 = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
            tessellator8.setColorOpaque_F(f14 * f27, f18 * f27, f22 * f27);
            this.renderBottomFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 0));
            z9 = true;
        }

        if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y + 1, z, 1)) {
            f27 = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
            if(block.maxY != 1.0D && !block.material.getIsLiquid()) {
                f27 = f26;
            }

            tessellator8.setColorOpaque_F(f15 * f27, f19 * f27, f23 * f27);
            this.renderTopFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 1));
            z9 = true;
        }

        if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z - 1, 2)) {
            f27 = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
            if(block.minZ > 0.0D) {
                f27 = f26;
            }

            tessellator8.setColorOpaque_F(f16 * f27, f20 * f27, f24 * f27);
            this.renderEastFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 2));
            z9 = true;
        }

        if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z + 1, 3)) {
            f27 = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
            if(block.maxZ < 1.0D) {
                f27 = f26;
            }

            tessellator8.setColorOpaque_F(f16 * f27, f20 * f27, f24 * f27);
            this.renderWestFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 3));
            z9 = true;
        }

        if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x - 1, y, z, 4)) {
            f27 = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
            if(block.minX > 0.0D) {
                f27 = f26;
            }

            tessellator8.setColorOpaque_F(f17 * f27, f21 * f27, f25 * f27);
            this.renderNorthFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 4));
            z9 = true;
        }

        if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x + 1, y, z, 5)) {
            f27 = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
            if(block.maxX < 1.0D) {
                f27 = f26;
            }

            tessellator8.setColorOpaque_F(f17 * f27, f21 * f27, f25 * f27);
            this.renderSouthFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 5));
            z9 = true;
        }

        return z9;
    }

    public boolean renderBlockFence(Block block, int x, int y, int z) {
        boolean z5 = false;
        float f6 = 0.375F;
        float f7 = 0.625F;
        block.setBlockBounds(f6, 0.0F, f6, f7, 1.0F, f7);
        this.renderStandardBlock(block, x, y, z);
        boolean z8 = false;
        boolean z9 = false;
        if(this.blockAccess.getBlockId(x - 1, y, z) == block.blockID || this.blockAccess.getBlockId(x + 1, y, z) == block.blockID) {
            z8 = true;
        }

        if(this.blockAccess.getBlockId(x, y, z - 1) == block.blockID || this.blockAccess.getBlockId(x, y, z + 1) == block.blockID) {
            z9 = true;
        }

        if(!z8 && !z9) {
            z8 = true;
        }

        f6 = 0.4375F;
        f7 = 0.5625F;
        float f10 = 0.75F;
        float f11 = 0.9375F;
        if(z8) {
            block.setBlockBounds(0.0F, f10, f6, 1.0F, f11, f7);
            this.renderStandardBlock(block, x, y, z);
        }

        if(z9) {
            block.setBlockBounds(f6, f10, 0.0F, f7, f11, 1.0F);
            this.renderStandardBlock(block, x, y, z);
        }

        f10 = 0.375F;
        f11 = 0.5625F;
        if(z8) {
            block.setBlockBounds(0.0F, f10, f6, 1.0F, f11, f7);
            this.renderStandardBlock(block, x, y, z);
        }

        if(z9) {
            block.setBlockBounds(f6, f10, 0.0F, f7, f11, 1.0F);
            this.renderStandardBlock(block, x, y, z);
        }

        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return z5;
    }

    public boolean renderBlockStairs(Block block, int x, int y, int z) {
        boolean z5 = false;
        int i6 = this.blockAccess.getBlockMetadata(x, y, z);
        if(i6 == 0) {
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
            this.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            this.renderStandardBlock(block, x, y, z);
        } else if(i6 == 1) {
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
            this.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
            this.renderStandardBlock(block, x, y, z);
        } else if(i6 == 2) {
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
            this.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
            this.renderStandardBlock(block, x, y, z);
        } else if(i6 == 3) {
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
            this.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
            this.renderStandardBlock(block, x, y, z);
        }

        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return z5;
    }

    public boolean renderBlockDoor(Block block, int x, int y, int z) {
        Tessellator tessellator5 = Tessellator.instance;
        BlockDoor blockDoor6 = (BlockDoor)block;
        boolean z7 = false;
        float f8 = 0.5F;
        float f9 = 1.0F;
        float f10 = 0.8F;
        float f11 = 0.6F;
        float f12 = block.getBlockBrightness(this.blockAccess, x, y, z);
        float f13 = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
        if(blockDoor6.minY > 0.0D) {
            f13 = f12;
        }

        if(Block.lightValue[block.blockID] > 0) {
            f13 = 1.0F;
        }

        tessellator5.setColorOpaque_F(f8 * f13, f8 * f13, f8 * f13);
        this.renderBottomFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 0));
        z7 = true;
        f13 = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
        if(blockDoor6.maxY < 1.0D) {
            f13 = f12;
        }

        if(Block.lightValue[block.blockID] > 0) {
            f13 = 1.0F;
        }

        tessellator5.setColorOpaque_F(f9 * f13, f9 * f13, f9 * f13);
        this.renderTopFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 1));
        z7 = true;
        f13 = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
        if(blockDoor6.minZ > 0.0D) {
            f13 = f12;
        }

        if(Block.lightValue[block.blockID] > 0) {
            f13 = 1.0F;
        }

        tessellator5.setColorOpaque_F(f10 * f13, f10 * f13, f10 * f13);
        int i14 = block.getBlockTexture(this.blockAccess, x, y, z, 2);
        if(i14 < 0) {
            this.flipTexture = true;
            i14 = -i14;
        }

        this.renderEastFace(block, (double)x, (double)y, (double)z, i14);
        z7 = true;
        this.flipTexture = false;
        f13 = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
        if(blockDoor6.maxZ < 1.0D) {
            f13 = f12;
        }

        if(Block.lightValue[block.blockID] > 0) {
            f13 = 1.0F;
        }

        tessellator5.setColorOpaque_F(f10 * f13, f10 * f13, f10 * f13);
        i14 = block.getBlockTexture(this.blockAccess, x, y, z, 3);
        if(i14 < 0) {
            this.flipTexture = true;
            i14 = -i14;
        }

        this.renderWestFace(block, (double)x, (double)y, (double)z, i14);
        z7 = true;
        this.flipTexture = false;
        f13 = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
        if(blockDoor6.minX > 0.0D) {
            f13 = f12;
        }

        if(Block.lightValue[block.blockID] > 0) {
            f13 = 1.0F;
        }

        tessellator5.setColorOpaque_F(f11 * f13, f11 * f13, f11 * f13);
        i14 = block.getBlockTexture(this.blockAccess, x, y, z, 4);
        if(i14 < 0) {
            this.flipTexture = true;
            i14 = -i14;
        }

        this.renderNorthFace(block, (double)x, (double)y, (double)z, i14);
        z7 = true;
        this.flipTexture = false;
        f13 = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
        if(blockDoor6.maxX < 1.0D) {
            f13 = f12;
        }

        if(Block.lightValue[block.blockID] > 0) {
            f13 = 1.0F;
        }

        tessellator5.setColorOpaque_F(f11 * f13, f11 * f13, f11 * f13);
        i14 = block.getBlockTexture(this.blockAccess, x, y, z, 5);
        if(i14 < 0) {
            this.flipTexture = true;
            i14 = -i14;
        }

        this.renderSouthFace(block, (double)x, (double)y, (double)z, i14);
        z7 = true;
        this.flipTexture = false;
        return z7;
    }

	private void renderBottomFace(Block block, double x, double y, double z, int blockTexture) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			blockTexture = this.overrideBlockTexture;
		}

		int i10 = (blockTexture & 15) << 4;
		blockTexture &= 240;
		double d12 = ((double)i10 + block.minX * 16.0D) / 256.0D;
		double d14 = ((double)i10 + block.maxX * 16.0D - 0.01D) / 256.0D;
		double d16 = ((double)blockTexture + block.minZ * 16.0D) / 256.0D;
		double d18 = ((double)blockTexture + block.maxZ * 16.0D - 0.01D) / 256.0D;
        if(block.minX < 0.0D || block.maxX > 1.0D) {
            d12 = (double)((float)i10 / 256.0F);
            d14 = (double)(((float)i10 + 15.99F) / 256.0F);
        }

        if(block.minZ < 0.0D || block.maxZ > 1.0D) {
            d16 = (double)((float)blockTexture / 256.0F);
            d18 = (double)(((float)blockTexture + 15.99F) / 256.0F);
        }

		double d20 = x + block.minX;
		double d22 = x + block.maxX;
		double d24 = y + block.minY;
		double d26 = z + block.minZ;
		double d28 = z + block.maxZ;
		tessellator9.addVertexWithUV(d20, d24, d28, d12, d18);
		tessellator9.addVertexWithUV(d20, d24, d26, d12, d16);
		tessellator9.addVertexWithUV(d22, d24, d26, d14, d16);
		tessellator9.addVertexWithUV(d22, d24, d28, d14, d18);
	}

	private void renderTopFace(Block block, double x, double y, double z, int blockTexture) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			blockTexture = this.overrideBlockTexture;
		}

		int i10 = (blockTexture & 15) << 4;
		blockTexture &= 240;
		double d12 = ((double)i10 + block.minX * 16.0D) / 256.0D;
		double d14 = ((double)i10 + block.maxX * 16.0D - 0.01D) / 256.0D;
		double d16 = ((double)blockTexture + block.minZ * 16.0D) / 256.0D;
		double d18 = ((double)blockTexture + block.maxZ * 16.0D - 0.01D) / 256.0D;
        if(block.minX < 0.0D || block.maxX > 1.0D) {
            d12 = (double)((float)i10 / 256.0F);
            d14 = (double)(((float)i10 + 15.99F) / 256.0F);
        }

        if(block.minZ < 0.0D || block.maxZ > 1.0D) {
            d16 = (double)((float)blockTexture / 256.0F);
            d18 = (double)(((float)blockTexture + 15.99F) / 256.0F);
        }

		double d20 = x + block.minX;
		double d22 = x + block.maxX;
		double d24 = y + block.maxY;
		double d26 = z + block.minZ;
		double d28 = z + block.maxZ;
		tessellator9.addVertexWithUV(d22, d24, d28, d14, d18);
		tessellator9.addVertexWithUV(d22, d24, d26, d14, d16);
		tessellator9.addVertexWithUV(d20, d24, d26, d12, d16);
		tessellator9.addVertexWithUV(d20, d24, d28, d12, d18);
	}

	private void renderEastFace(Block block, double x, double y, double z, int blockTexture) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			blockTexture = this.overrideBlockTexture;
		}

		int i10 = (blockTexture & 15) << 4;
		blockTexture &= 240;
		double d12 = ((double)i10 + block.minX * 16.0D) / 256.0D;
		double d14 = ((double)i10 + block.maxX * 16.0D - 0.01D) / 256.0D;
		double d16 = ((double)blockTexture + block.minY * 16.0D) / 256.0D;
		double d18 = ((double)blockTexture + block.maxY * 16.0D - 0.01D) / 256.0D;
        double d20;
        if(this.flipTexture) {
            d20 = d12;
            d12 = d14;
            d14 = d20;
        }

        if(block.minX < 0.0D || block.maxX > 1.0D) {
            d12 = (double)((float)i10 / 256.0F);
            d14 = (double)(((float)i10 + 15.99F) / 256.0F);
        }

        if(block.minY < 0.0D || block.maxY > 1.0D) {
            d16 = (double)((float)blockTexture / 256.0F);
            d18 = (double)(((float)blockTexture + 15.99F) / 256.0F);
        }

		d20 = x + block.minX;
		double d22 = x + block.maxX;
		double d24 = y + block.minY;
		double d26 = y + block.maxY;
		double d28 = z + block.minZ;
		tessellator9.addVertexWithUV(d20, d26, d28, d14, d16);
		tessellator9.addVertexWithUV(d22, d26, d28, d12, d16);
		tessellator9.addVertexWithUV(d22, d24, d28, d12, d18);
		tessellator9.addVertexWithUV(d20, d24, d28, d14, d18);
	}

	private void renderWestFace(Block block, double x, double y, double z, int blockTexture) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			blockTexture = this.overrideBlockTexture;
		}

		int i10 = (blockTexture & 15) << 4;
		blockTexture &= 240;
		double d12 = ((double)i10 + block.minX * 16.0D) / 256.0D;
		double d14 = ((double)i10 + block.maxX * 16.0D - 0.01D) / 256.0D;
		double d16 = ((double)blockTexture + block.minY * 16.0D) / 256.0D;
		double d18 = ((double)blockTexture + block.maxY * 16.0D - 0.01D) / 256.0D;
        double d20;
        if(this.flipTexture) {
            d20 = d12;
            d12 = d14;
            d14 = d20;
        }

        if(block.minX < 0.0D || block.maxX > 1.0D) {
            d12 = (double)((float)i10 / 256.0F);
            d14 = (double)(((float)i10 + 15.99F) / 256.0F);
        }

        if(block.minY < 0.0D || block.maxY > 1.0D) {
            d16 = (double)((float)blockTexture / 256.0F);
            d18 = (double)(((float)blockTexture + 15.99F) / 256.0F);
        }

		d20 = x + block.minX;
		double d22 = x + block.maxX;
		double d24 = y + block.minY;
		double d26 = y + block.maxY;
		double d28 = z + block.maxZ;
		tessellator9.addVertexWithUV(d20, d26, d28, d12, d16);
		tessellator9.addVertexWithUV(d20, d24, d28, d12, d18);
		tessellator9.addVertexWithUV(d22, d24, d28, d14, d18);
		tessellator9.addVertexWithUV(d22, d26, d28, d14, d16);
	}

	private void renderNorthFace(Block block, double x, double y, double z, int blockTexture) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			blockTexture = this.overrideBlockTexture;
		}

		int i10 = (blockTexture & 15) << 4;
		blockTexture &= 240;
		double d12 = ((double)i10 + block.minZ * 16.0D) / 256.0D;
		double d14 = ((double)i10 + block.maxZ * 16.0D - 0.01D) / 256.0D;
		double d16 = ((double)blockTexture + block.minY * 16.0D) / 256.0D;
		double d18 = ((double)blockTexture + block.maxY * 16.0D - 0.01D) / 256.0D;
        double d20;
        if(this.flipTexture) {
            d20 = d12;
            d12 = d14;
            d14 = d20;
        }

        if(block.minZ < 0.0D || block.maxZ > 1.0D) {
            d12 = (double)((float)i10 / 256.0F);
            d14 = (double)(((float)i10 + 15.99F) / 256.0F);
        }

        if(block.minY < 0.0D || block.maxY > 1.0D) {
            d16 = (double)((float)blockTexture / 256.0F);
            d18 = (double)(((float)blockTexture + 15.99F) / 256.0F);
        }

		d20 = x + block.minX;
		double d22 = y + block.minY;
		double d24 = y + block.maxY;
		double d26 = z + block.minZ;
		double d28 = z + block.maxZ;
		tessellator9.addVertexWithUV(d20, d24, d28, d14, d16);
		tessellator9.addVertexWithUV(d20, d24, d26, d12, d16);
		tessellator9.addVertexWithUV(d20, d22, d26, d12, d18);
		tessellator9.addVertexWithUV(d20, d22, d28, d14, d18);
	}

	private void renderSouthFace(Block block, double x, double y, double z, int blockTexture) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			blockTexture = this.overrideBlockTexture;
		}

		int i10 = (blockTexture & 15) << 4;
		blockTexture &= 240;
		double d12 = ((double)i10 + block.minZ * 16.0D) / 256.0D;
		double d14 = ((double)i10 + block.maxZ * 16.0D - 0.01D) / 256.0D;
		double d16 = ((double)blockTexture + block.minY * 16.0D) / 256.0D;
		double d18 = ((double)blockTexture + block.maxY * 16.0D - 0.01D) / 256.0D;
        double d20;
        if(this.flipTexture) {
            d20 = d12;
            d12 = d14;
            d14 = d20;
        }

        if(block.minZ < 0.0D || block.maxZ > 1.0D) {
            d12 = (double)((float)i10 / 256.0F);
            d14 = (double)(((float)i10 + 15.99F) / 256.0F);
        }

        if(block.minY < 0.0D || block.maxY > 1.0D) {
            d16 = (double)((float)blockTexture / 256.0F);
            d18 = (double)(((float)blockTexture + 15.99F) / 256.0F);
        }

		d20 = x + block.maxX;
		double d22 = y + block.minY;
		double d24 = y + block.maxY;
		double d26 = z + block.minZ;
		double d28 = z + block.maxZ;
		tessellator9.addVertexWithUV(d20, d22, d28, d12, d18);
		tessellator9.addVertexWithUV(d20, d22, d26, d14, d18);
		tessellator9.addVertexWithUV(d20, d24, d26, d14, d16);
		tessellator9.addVertexWithUV(d20, d24, d28, d12, d16);
	}

	public void renderBlockOnInventory(Block block) {
		Tessellator tessellator2 = Tessellator.instance;
		int i3 = block.getRenderType();
		if(i3 == 0) {
            block.setBlockBoundsForItemRender();
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			tessellator2.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
			tessellator2.draw();
			tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			tessellator2.setNormal(0.0F, 1.0F, 0.0F);
			this.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
			tessellator2.draw();
			tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			tessellator2.setNormal(0.0F, 0.0F, -1.0F);
			this.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
			tessellator2.draw();
			tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			tessellator2.setNormal(0.0F, 0.0F, 1.0F);
			this.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
			tessellator2.draw();
			tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			tessellator2.setNormal(-1.0F, 0.0F, 0.0F);
			this.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
			tessellator2.draw();
			tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			tessellator2.setNormal(1.0F, 0.0F, 0.0F);
			this.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
			tessellator2.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		} else if(i3 == 1) {
			tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			tessellator2.setNormal(0.0F, -1.0F, 0.0F);
			this.renderCrossedSquares(block, -1, -0.5D, -0.5D, -0.5D);
			tessellator2.draw();
		} else if(i3 == 6) {
			tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			tessellator2.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBlockCropsImpl(block, -1, -0.5D, -0.5D, -0.5D);
			tessellator2.draw();
		} else if(i3 == 2) {
			tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			tessellator2.setNormal(0.0F, -1.0F, 0.0F);
			this.renderTorchAtAngle(block, -0.5D, -0.5D, -0.5D, 0.0D, 0.0D);
			tessellator2.draw();
        } else if(i3 == 10) {
            for(int i5 = 0; i5 < 2; ++i5) {
                if(i5 == 0) {
                    block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
                }

                if(i5 == 1) {
                    block.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
                }

                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
                tessellator2.setNormal(0.0F, -1.0F, 0.0F);
                this.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
                tessellator2.draw();
                tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
                tessellator2.setNormal(0.0F, 1.0F, 0.0F);
                this.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
                tessellator2.draw();
                tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
                tessellator2.setNormal(0.0F, 0.0F, -1.0F);
                this.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
                tessellator2.draw();
                tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
                tessellator2.setNormal(0.0F, 0.0F, 1.0F);
                this.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
                tessellator2.draw();
                tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
                tessellator2.setNormal(-1.0F, 0.0F, 0.0F);
                this.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
                tessellator2.draw();
                tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
                tessellator2.setNormal(1.0F, 0.0F, 0.0F);
                this.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
                tessellator2.draw();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }

            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

    }

    public static boolean renderItemIn3d(int renderType) {
        return renderType == 0 ? true : renderType == 10;
    }
}
