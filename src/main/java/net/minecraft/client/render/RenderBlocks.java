package net.minecraft.client.render;

import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.BlockDoor;
import net.minecraft.game.world.block.BlockFluid;
import net.minecraft.game.world.material.Material;

public final class RenderBlocks {
	private World worldObj;
	private int overrideBlockTexture = -1;
	private boolean renderAllFaces = false;

	public RenderBlocks(World world) {
		this.worldObj = world;
	}

	public RenderBlocks() {
	}

	public final void renderBlockUsingTexture(Block block, int x, int y, int z, int overrideBlockTextureIndex) {
		this.overrideBlockTexture = overrideBlockTextureIndex;
		this.renderBlockByRenderType(block, x, y, z);
		this.overrideBlockTexture = -1;
	}

	public final void renderBlockAllFaces(Block block, int x, int posY, int z) {
		this.renderAllFaces = true;
		this.renderBlockByRenderType(block, x, posY, z);
		this.renderAllFaces = false;
	}

    public final boolean renderBlockByRenderType(Block block, int x, int y, int z) {
        int i5 = block.getRenderType();
        block.setBlockBoundsBasedOnState(this.worldObj, x, y, z);
        Tessellator tessellator6;
        float f18;
        boolean z57;
        if(i5 == 0) {
            tessellator6 = Tessellator.instance;
            z57 = false;
            float f59 = block.getBlockBrightness(this.worldObj, x, y, z);
            if(this.renderAllFaces || block.getIsBlockSolid(this.worldObj, x, y - 1, z, 0)) {
                f18 = block.getBlockBrightness(this.worldObj, x, y - 1, z);
                if(Block.lightValue[block.blockID] > 0) {
                    f18 = 1.0F;
                }

                tessellator6.setColorOpaque_F(0.5F * f18, 0.5F * f18, 0.5F * f18);
                this.renderBottomFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.worldObj, x, y, z, 0));
                z57 = true;
            }

            if(this.renderAllFaces || block.getIsBlockSolid(this.worldObj, x, y + 1, z, 1)) {
                f18 = block.getBlockBrightness(this.worldObj, x, y + 1, z);
                if(block.maxY != 1.0D && !block.blockMaterial.getIsLiquid()) {
                    f18 = f59;
                }

                if(Block.lightValue[block.blockID] > 0) {
                    f18 = 1.0F;
                }

                tessellator6.setColorOpaque_F(f18 * 1.0F, f18 * 1.0F, f18 * 1.0F);
                this.renderTopFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.worldObj, x, y, z, 1));
                z57 = true;
            }

            if(this.renderAllFaces || block.getIsBlockSolid(this.worldObj, x, y, z - 1, 2)) {
                f18 = block.getBlockBrightness(this.worldObj, x, y, z - 1);
                if(Block.lightValue[block.blockID] > 0) {
                    f18 = 1.0F;
                }

                tessellator6.setColorOpaque_F(0.8F * f18, 0.8F * f18, 0.8F * f18);
                this.renderEastFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.worldObj, x, y, z, 2));
                z57 = true;
            }

            if(this.renderAllFaces || block.getIsBlockSolid(this.worldObj, x, y, z + 1, 3)) {
                f18 = block.getBlockBrightness(this.worldObj, x, y, z + 1);
                if(Block.lightValue[block.blockID] > 0) {
                    f18 = 1.0F;
                }

                tessellator6.setColorOpaque_F(0.8F * f18, 0.8F * f18, 0.8F * f18);
                this.renderWestFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.worldObj, x, y, z, 3));
                z57 = true;
            }

            if(this.renderAllFaces || block.getIsBlockSolid(this.worldObj, x - 1, y, z, 4)) {
                f18 = block.getBlockBrightness(this.worldObj, x - 1, y, z);
                if(Block.lightValue[block.blockID] > 0) {
                    f18 = 1.0F;
                }

                tessellator6.setColorOpaque_F(0.6F * f18, 0.6F * f18, 0.6F * f18);
                this.renderNorthFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.worldObj, x, y, z, 4));
                z57 = true;
            }

            if(this.renderAllFaces || block.getIsBlockSolid(this.worldObj, x + 1, y, z, 5)) {
                f18 = block.getBlockBrightness(this.worldObj, x + 1, y, z);
                if(Block.lightValue[block.blockID] > 0) {
                    f18 = 1.0F;
                }

                tessellator6.setColorOpaque_F(0.6F * f18, 0.6F * f18, 0.6F * f18);
                this.renderSouthFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.worldObj, x, y, z, 5));
                z57 = true;
            }

            return z57;
        } else {
            float f8;
            double d31;
            double d33;
            if(i5 == 4) {
                i5 = z;
                z = y;
                y = x;
                Block block51 = block;
                RenderBlocks renderBlocks50 = this;
                tessellator6 = Tessellator.instance;
                z57 = false;
                Material material61 = block.blockMaterial;
                int i64 = this.worldObj.getBlockMetadata(x, z, i5);
                float f62 = this.getFluidHeight(x, z, i5, material61);
                float f63 = this.getFluidHeight(x, z, i5 + 1, material61);
                float f65 = this.getFluidHeight(x + 1, z, i5 + 1, material61);
                float f26 = this.getFluidHeight(x + 1, z, i5, material61);
                int i30;
                float f36;
                int i67;
                float f73;
                float f74;
                if(this.renderAllFaces || block.getIsBlockSolid(this.worldObj, x, z + 1, i5, 1)) {
                    z57 = true;
                    i67 = block.getBlockTextureFromSideAndMetadata(1, i64);
                    float f28;
                    if((f28 = (float)BlockFluid.getFlowDirection(this.worldObj, x, z, i5, material61)) > -999.0F) {
                        i67 = block.getBlockTextureFromSideAndMetadata(2, i64);
                    }

                    int i70 = (i67 & 15) << 4;
                    i30 = i67 & 240;
                    d31 = ((double)i70 + 8.0D) / 256.0D;
                    d33 = ((double)i30 + 8.0D) / 256.0D;
                    if(f28 < -999.0F) {
                        f28 = 0.0F;
                    } else {
                        d31 = (double)((float)(i70 + 16) / 256.0F);
                        d33 = (double)((float)(i30 + 16) / 256.0F);
                    }

                    f73 = MathHelper.sin(f28) * 8.0F / 256.0F;
                    f36 = MathHelper.cos(f28) * 8.0F / 256.0F;
                    f74 = block.getBlockBrightness(this.worldObj, x, z, i5);
                    tessellator6.setColorOpaque_F(f74 * 1.0F, f74 * 1.0F, f74 * 1.0F);
                    tessellator6.addVertexWithUV((double)x, (double)((float)z + f62), (double)i5, d31 - (double)f36 - (double)f73, d33 - (double)f36 + (double)f73);
                    tessellator6.addVertexWithUV((double)x, (double)((float)z + f63), (double)(i5 + 1), d31 - (double)f36 + (double)f73, d33 + (double)f36 + (double)f73);
                    tessellator6.addVertexWithUV((double)(x + 1), (double)((float)z + f65), (double)(i5 + 1), d31 + (double)f36 + (double)f73, d33 + (double)f36 - (double)f73);
                    tessellator6.addVertexWithUV((double)(x + 1), (double)((float)z + f26), (double)i5, d31 + (double)f36 - (double)f73, d33 - (double)f36 - (double)f73);
                }

                if(this.renderAllFaces || block.getIsBlockSolid(this.worldObj, x, z - 1, i5, 0)) {
                    float f69 = block.getBlockBrightness(this.worldObj, x, z - 1, i5);
                    tessellator6.setColorOpaque_F(0.5F * f69, 0.5F * f69, 0.5F * f69);
                    this.renderBottomFace(block, (double)x, (double)z, (double)i5, block.getBlockTextureFromSide(0));
                    z57 = true;
                }

                for(i67 = 0; i67 < 4; ++i67) {
                    int i68 = y;
                    i30 = i5;
                    if(i67 == 0) {
                        i30 = i5 - 1;
                    }

                    if(i67 == 1) {
                        ++i30;
                    }

                    if(i67 == 2) {
                        i68 = y - 1;
                    }

                    if(i67 == 3) {
                        ++i68;
                    }

                    int i71;
                    int i32 = ((i71 = block51.getBlockTextureFromSideAndMetadata(i67 + 2, i64)) & 15) << 4;
                    int i72 = i71 & 240;
                    if(renderBlocks50.renderAllFaces || block51.getIsBlockSolid(renderBlocks50.worldObj, i68, z, i30, i67 + 2)) {
                        float f34;
                        float f38;
                        float f75;
                        if(i67 == 0) {
                            f34 = f62;
                            f73 = f26;
                            f36 = (float)y;
                            f38 = (float)(y + 1);
                            f75 = f74 = (float)i5;
                        } else if(i67 == 1) {
                            f34 = f65;
                            f73 = f63;
                            f36 = (float)(y + 1);
                            f38 = (float)y;
                            f75 = f74 = (float)(i5 + 1);
                        } else if(i67 == 2) {
                            f34 = f63;
                            f73 = f62;
                            f38 = f36 = (float)y;
                            f74 = (float)(i5 + 1);
                            f75 = (float)i5;
                        } else {
                            f34 = f26;
                            f73 = f65;
                            f38 = f36 = (float)(y + 1);
                            f74 = (float)i5;
                            f75 = (float)(i5 + 1);
                        }

                        double d40 = (double)((float)i32 / 256.0F);
                        double d42 = ((double)(i32 + 16) - 0.01D) / 256.0D;
                        double d44 = (double)(((float)i72 + (1.0F - f34) * 16.0F) / 256.0F);
                        double d46 = (double)(((float)i72 + (1.0F - f73) * 16.0F) / 256.0F);
                        double d48 = ((double)(i72 + 16) - 0.01D) / 256.0D;
                        f8 = block51.getBlockBrightness(renderBlocks50.worldObj, i68, z, i30);
                        if(i67 < 2) {
                            f8 *= 0.8F;
                        } else {
                            f8 *= 0.6F;
                        }

                        tessellator6.setColorOpaque_F(f8 * 1.0F, f8 * 1.0F, f8 * 1.0F);
                        tessellator6.addVertexWithUV((double)f36, (double)((float)z + f34), (double)f74, d40, d44);
                        tessellator6.addVertexWithUV((double)f38, (double)((float)z + f73), (double)f75, d42, d46);
                        tessellator6.addVertexWithUV((double)f38, (double)z, (double)f75, d42, d48);
                        tessellator6.addVertexWithUV((double)f36, (double)z, (double)f74, d40, d48);
                    }
                }

                block51.minY = 0.0D;
                block51.maxY = 1.0D;
                return z57;
            } else {
                float f56;
                if(i5 == 1) {
                    tessellator6 = Tessellator.instance;
                    f56 = block.getBlockBrightness(this.worldObj, x, y, z);
                    tessellator6.setColorOpaque_F(f56, f56, f56);
                    this.renderCrossedSquares(block, this.worldObj.getBlockMetadata(x, y, z), (double)x, (double)y, (double)z);
                    return true;
                } else if(i5 == 6) {
                    tessellator6 = Tessellator.instance;
                    f56 = block.getBlockBrightness(this.worldObj, x, y, z);
                    tessellator6.setColorOpaque_F(f56, f56, f56);
                    this.renderBlockCrops(block, this.worldObj.getBlockMetadata(x, y, z), (double)x, (double)((float)y - 0.0625F), (double)z);
                    return true;
                } else if(i5 == 2) {
                    int i52 = this.worldObj.getBlockMetadata(x, y, z);
                    Tessellator tessellator55 = Tessellator.instance;
                    f8 = block.getBlockBrightness(this.worldObj, x, y, z);
                    if(Block.lightValue[block.blockID] > 0) {
                        f8 = 1.0F;
                    }

                    tessellator55.setColorOpaque_F(f8, f8, f8);
                    if(i52 == 1) {
                        this.renderTorchAtAngle(block, (double)x - 0.09999999403953552D, (double)y + (double)0.2F, (double)z, -0.4000000059604645D, 0.0D);
                    } else if(i52 == 2) {
                        this.renderTorchAtAngle(block, (double)x + 0.09999999403953552D, (double)y + (double)0.2F, (double)z, (double)0.4F, 0.0D);
                    } else if(i52 == 3) {
                        this.renderTorchAtAngle(block, (double)x, (double)y + (double)0.2F, (double)z - 0.09999999403953552D, 0.0D, -0.4000000059604645D);
                    } else if(i52 == 4) {
                        this.renderTorchAtAngle(block, (double)x, (double)y + (double)0.2F, (double)z + 0.09999999403953552D, 0.0D, (double)0.4F);
                    } else {
                        this.renderTorchAtAngle(block, (double)x, (double)y, (double)z, 0.0D, 0.0D);
                    }

                    return true;
                } else {
                    double d16;
                    double d20;
                    double d22;
                    double d25;
                    double d27;
                    double d29;
                    int i53;
                    int i54;
                    double d60;
                    if(i5 == 3) {
                        i5 = z;
                        z = y;
                        y = x;
                        tessellator6 = Tessellator.instance;
                        i53 = block.getBlockTextureFromSide(0);
                        if(this.overrideBlockTexture >= 0) {
                            i53 = this.overrideBlockTexture;
                        }

                        f8 = block.getBlockBrightness(this.worldObj, x, z, i5);
                        tessellator6.setColorOpaque_F(f8, f8, f8);
                        x = (i53 & 15) << 4;
                        i54 = i53 & 240;
                        d16 = (double)((float)x / 256.0F);
                        d60 = (double)(((float)x + 15.99F) / 256.0F);
                        d20 = (double)((float)i54 / 256.0F);
                        d22 = (double)(((float)i54 + 15.99F) / 256.0F);
                        if(!this.worldObj.isBlockNormalCube(y, z - 1, i5) && !Block.fire.getChanceToEncourageFire(this.worldObj, y, z - 1, i5)) {
                            if((y + z + i5 & 1) == 1) {
                                d16 = (double)((float)x / 256.0F);
                                d60 = (double)(((float)x + 15.99F) / 256.0F);
                                d20 = (double)((float)(i54 + 16) / 256.0F);
                                d22 = (double)(((float)i54 + 15.99F + 16.0F) / 256.0F);
                            }

                            if((y / 2 + z / 2 + i5 / 2 & 1) == 1) {
                                d27 = d60;
                                d60 = d16;
                                d16 = d27;
                            }

                            if(Block.fire.getChanceToEncourageFire(this.worldObj, y - 1, z, i5)) {
                                tessellator6.addVertexWithUV((double)((float)y + 0.2F), (double)((float)z + 1.4F + 0.0625F), (double)(i5 + 1), d60, d20);
                                tessellator6.addVertexWithUV((double)y, (double)((float)z + 0.0625F), (double)(i5 + 1), d60, d22);
                                tessellator6.addVertexWithUV((double)y, (double)((float)z + 0.0625F), (double)i5, d16, d22);
                                tessellator6.addVertexWithUV((double)((float)y + 0.2F), (double)((float)z + 1.4F + 0.0625F), (double)i5, d16, d20);
                                tessellator6.addVertexWithUV((double)((float)y + 0.2F), (double)((float)z + 1.4F + 0.0625F), (double)i5, d16, d20);
                                tessellator6.addVertexWithUV((double)y, (double)((float)z + 0.0625F), (double)i5, d16, d22);
                                tessellator6.addVertexWithUV((double)y, (double)((float)z + 0.0625F), (double)(i5 + 1), d60, d22);
                                tessellator6.addVertexWithUV((double)((float)y + 0.2F), (double)((float)z + 1.4F + 0.0625F), (double)(i5 + 1), d60, d20);
                            }

                            if(Block.fire.getChanceToEncourageFire(this.worldObj, y + 1, z, i5)) {
                                tessellator6.addVertexWithUV((double)((float)(y + 1) - 0.2F), (double)((float)z + 1.4F + 0.0625F), (double)i5, d16, d20);
                                tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 0.0625F), (double)i5, d16, d22);
                                tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 0.0625F), (double)(i5 + 1), d60, d22);
                                tessellator6.addVertexWithUV((double)((float)(y + 1) - 0.2F), (double)((float)z + 1.4F + 0.0625F), (double)(i5 + 1), d60, d20);
                                tessellator6.addVertexWithUV((double)((float)(y + 1) - 0.2F), (double)((float)z + 1.4F + 0.0625F), (double)(i5 + 1), d60, d20);
                                tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 0.0625F), (double)(i5 + 1), d60, d22);
                                tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 0.0625F), (double)i5, d16, d22);
                                tessellator6.addVertexWithUV((double)((float)(y + 1) - 0.2F), (double)((float)z + 1.4F + 0.0625F), (double)i5, d16, d20);
                            }

                            if(Block.fire.getChanceToEncourageFire(this.worldObj, y, z, i5 - 1)) {
                                tessellator6.addVertexWithUV((double)y, (double)((float)z + 1.4F + 0.0625F), (double)((float)i5 + 0.2F), d60, d20);
                                tessellator6.addVertexWithUV((double)y, (double)((float)z + 0.0625F), (double)i5, d60, d22);
                                tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 0.0625F), (double)i5, d16, d22);
                                tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 1.4F + 0.0625F), (double)((float)i5 + 0.2F), d16, d20);
                                tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 1.4F + 0.0625F), (double)((float)i5 + 0.2F), d16, d20);
                                tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 0.0625F), (double)i5, d16, d22);
                                tessellator6.addVertexWithUV((double)y, (double)((float)z + 0.0625F), (double)i5, d60, d22);
                                tessellator6.addVertexWithUV((double)y, (double)((float)z + 1.4F + 0.0625F), (double)((float)i5 + 0.2F), d60, d20);
                            }

                            if(Block.fire.getChanceToEncourageFire(this.worldObj, y, z, i5 + 1)) {
                                tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 1.4F + 0.0625F), (double)((float)(i5 + 1) - 0.2F), d16, d20);
                                tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 0.0625F), (double)(i5 + 1), d16, d22);
                                tessellator6.addVertexWithUV((double)y, (double)((float)z + 0.0625F), (double)(i5 + 1), d60, d22);
                                tessellator6.addVertexWithUV((double)y, (double)((float)z + 1.4F + 0.0625F), (double)((float)(i5 + 1) - 0.2F), d60, d20);
                                tessellator6.addVertexWithUV((double)y, (double)((float)z + 1.4F + 0.0625F), (double)((float)(i5 + 1) - 0.2F), d60, d20);
                                tessellator6.addVertexWithUV((double)y, (double)((float)z + 0.0625F), (double)(i5 + 1), d60, d22);
                                tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 0.0625F), (double)(i5 + 1), d16, d22);
                                tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 1.4F + 0.0625F), (double)((float)(i5 + 1) - 0.2F), d16, d20);
                            }

                            if(Block.fire.getChanceToEncourageFire(this.worldObj, y, z + 1, i5)) {
                                d27 = (double)y + 0.5D + 0.5D;
                                d29 = (double)y + 0.5D - 0.5D;
                                d31 = (double)i5 + 0.5D + 0.5D;
                                d33 = (double)i5 + 0.5D - 0.5D;
                                d16 = (double)((float)x / 256.0F);
                                d60 = (double)(((float)x + 15.99F) / 256.0F);
                                d20 = (double)((float)i54 / 256.0F);
                                d22 = (double)(((float)i54 + 15.99F) / 256.0F);
                                ++z;
                                if((y + z + i5 & 1) == 0) {
                                    tessellator6.addVertexWithUV(d29, (double)((float)z + -0.2F), (double)i5, d60, d20);
                                    tessellator6.addVertexWithUV(d27, (double)z, (double)i5, d60, d22);
                                    tessellator6.addVertexWithUV(d27, (double)z, (double)(i5 + 1), d16, d22);
                                    tessellator6.addVertexWithUV(d29, (double)((float)z + -0.2F), (double)(i5 + 1), d16, d20);
                                    d16 = (double)((float)x / 256.0F);
                                    d60 = (double)(((float)x + 15.99F) / 256.0F);
                                    d20 = (double)((float)(i54 + 16) / 256.0F);
                                    d22 = (double)(((float)i54 + 15.99F + 16.0F) / 256.0F);
                                    tessellator6.addVertexWithUV(d27, (double)((float)z + -0.2F), (double)(i5 + 1), d60, d20);
                                    tessellator6.addVertexWithUV(d29, (double)z, (double)(i5 + 1), d60, d22);
                                    tessellator6.addVertexWithUV(d29, (double)z, (double)i5, d16, d22);
                                    tessellator6.addVertexWithUV(d27, (double)((float)z + -0.2F), (double)i5, d16, d20);
                                } else {
                                    tessellator6.addVertexWithUV((double)y, (double)((float)z + -0.2F), d31, d60, d20);
                                    tessellator6.addVertexWithUV((double)y, (double)z, d33, d60, d22);
                                    tessellator6.addVertexWithUV((double)(y + 1), (double)z, d33, d16, d22);
                                    tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + -0.2F), d31, d16, d20);
                                    d16 = (double)((float)x / 256.0F);
                                    d60 = (double)(((float)x + 15.99F) / 256.0F);
                                    d20 = (double)((float)(i54 + 16) / 256.0F);
                                    d22 = (double)(((float)i54 + 15.99F + 16.0F) / 256.0F);
                                    tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + -0.2F), d33, d60, d20);
                                    tessellator6.addVertexWithUV((double)(y + 1), (double)z, d31, d60, d22);
                                    tessellator6.addVertexWithUV((double)y, (double)z, d31, d16, d22);
                                    tessellator6.addVertexWithUV((double)y, (double)((float)z + -0.2F), d33, d16, d20);
                                }
                            }
                        } else {
                            d25 = (double)y + 0.5D + 0.2D;
                            d27 = (double)y + 0.5D - 0.2D;
                            d29 = (double)i5 + 0.5D + 0.2D;
                            d31 = (double)i5 + 0.5D - 0.2D;
                            d33 = (double)y + 0.5D - 0.3D;
                            double d35 = (double)y + 0.5D + 0.3D;
                            double d37 = (double)i5 + 0.5D - 0.3D;
                            double d39 = (double)i5 + 0.5D + 0.3D;
                            tessellator6.addVertexWithUV(d33, (double)((float)z + 1.4F), (double)(i5 + 1), d60, d20);
                            tessellator6.addVertexWithUV(d25, (double)z, (double)(i5 + 1), d60, d22);
                            tessellator6.addVertexWithUV(d25, (double)z, (double)i5, d16, d22);
                            tessellator6.addVertexWithUV(d33, (double)((float)z + 1.4F), (double)i5, d16, d20);
                            tessellator6.addVertexWithUV(d35, (double)((float)z + 1.4F), (double)i5, d60, d20);
                            tessellator6.addVertexWithUV(d27, (double)z, (double)i5, d60, d22);
                            tessellator6.addVertexWithUV(d27, (double)z, (double)(i5 + 1), d16, d22);
                            tessellator6.addVertexWithUV(d35, (double)((float)z + 1.4F), (double)(i5 + 1), d16, d20);
                            d16 = (double)((float)x / 256.0F);
                            d60 = (double)(((float)x + 15.99F) / 256.0F);
                            d20 = (double)((float)(i54 + 16) / 256.0F);
                            d22 = (double)(((float)i54 + 15.99F + 16.0F) / 256.0F);
                            tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 1.4F), d39, d60, d20);
                            tessellator6.addVertexWithUV((double)(y + 1), (double)z, d31, d60, d22);
                            tessellator6.addVertexWithUV((double)y, (double)z, d31, d16, d22);
                            tessellator6.addVertexWithUV((double)y, (double)((float)z + 1.4F), d39, d16, d20);
                            tessellator6.addVertexWithUV((double)y, (double)((float)z + 1.4F), d37, d60, d20);
                            tessellator6.addVertexWithUV((double)y, (double)z, d29, d60, d22);
                            tessellator6.addVertexWithUV((double)(y + 1), (double)z, d29, d16, d22);
                            tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 1.4F), d37, d16, d20);
                            d25 = (double)y + 0.5D - 0.5D;
                            d27 = (double)y + 0.5D + 0.5D;
                            d29 = (double)i5 + 0.5D - 0.5D;
                            d31 = (double)i5 + 0.5D + 0.5D;
                            d33 = (double)y + 0.5D - 0.4D;
                            d35 = (double)y + 0.5D + 0.4D;
                            d37 = (double)i5 + 0.5D - 0.4D;
                            d39 = (double)i5 + 0.5D + 0.4D;
                            tessellator6.addVertexWithUV(d33, (double)((float)z + 1.4F), (double)i5, d16, d20);
                            tessellator6.addVertexWithUV(d25, (double)z, (double)i5, d16, d22);
                            tessellator6.addVertexWithUV(d25, (double)z, (double)(i5 + 1), d60, d22);
                            tessellator6.addVertexWithUV(d33, (double)((float)z + 1.4F), (double)(i5 + 1), d60, d20);
                            tessellator6.addVertexWithUV(d35, (double)((float)z + 1.4F), (double)(i5 + 1), d16, d20);
                            tessellator6.addVertexWithUV(d27, (double)z, (double)(i5 + 1), d16, d22);
                            tessellator6.addVertexWithUV(d27, (double)z, (double)i5, d60, d22);
                            tessellator6.addVertexWithUV(d35, (double)((float)z + 1.4F), (double)i5, d60, d20);
                            d16 = (double)((float)x / 256.0F);
                            d60 = (double)(((float)x + 15.99F) / 256.0F);
                            d20 = (double)((float)i54 / 256.0F);
                            d22 = (double)(((float)i54 + 15.99F) / 256.0F);
                            tessellator6.addVertexWithUV((double)y, (double)((float)z + 1.4F), d39, d16, d20);
                            tessellator6.addVertexWithUV((double)y, (double)z, d31, d16, d22);
                            tessellator6.addVertexWithUV((double)(y + 1), (double)z, d31, d60, d22);
                            tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 1.4F), d39, d60, d20);
                            tessellator6.addVertexWithUV((double)(y + 1), (double)((float)z + 1.4F), d37, d16, d20);
                            tessellator6.addVertexWithUV((double)(y + 1), (double)z, d29, d16, d22);
                            tessellator6.addVertexWithUV((double)y, (double)z, d29, d60, d22);
                            tessellator6.addVertexWithUV((double)y, (double)((float)z + 1.4F), d37, d60, d20);
                        }

                        return true;
                    } else if(i5 == 5) {
                        i5 = z;
                        z = y;
                        y = x;
                        tessellator6 = Tessellator.instance;
                        i53 = block.getBlockTextureFromSide(0);
                        if(this.overrideBlockTexture >= 0) {
                            i53 = this.overrideBlockTexture;
                        }

                        f8 = block.getBlockBrightness(this.worldObj, x, z, i5);
                        tessellator6.setColorOpaque_F(f8, f8, f8);
                        x = ((i53 & 15) << 4) + 16;
                        i54 = (i53 & 15) << 4;
                        int i58 = i53 & 240;
                        if((y + z + i5 & 1) == 1) {
                            x = (i53 & 15) << 4;
                            i54 = ((i53 & 15) << 4) + 16;
                        }

                        double d17 = (double)((float)x / 256.0F);
                        double d66 = (double)(((float)x + 15.99F) / 256.0F);
                        double d21 = (double)((float)i58 / 256.0F);
                        double d23 = (double)(((float)i58 + 15.99F) / 256.0F);
                        d25 = (double)((float)i54 / 256.0F);
                        d27 = (double)(((float)i54 + 15.99F) / 256.0F);
                        d29 = (double)((float)i58 / 256.0F);
                        d31 = (double)(((float)i58 + 15.99F) / 256.0F);
                        if(this.worldObj.isBlockNormalCube(y - 1, z, i5)) {
                            tessellator6.addVertexWithUV((double)((float)y + 0.05F), (double)((float)(z + 1) + 0.125F), (double)((float)(i5 + 1) + 0.125F), d17, d21);
                            tessellator6.addVertexWithUV((double)((float)y + 0.05F), (double)((float)z - 0.125F), (double)((float)(i5 + 1) + 0.125F), d17, d23);
                            tessellator6.addVertexWithUV((double)((float)y + 0.05F), (double)((float)z - 0.125F), (double)((float)i5 - 0.125F), d66, d23);
                            tessellator6.addVertexWithUV((double)((float)y + 0.05F), (double)((float)(z + 1) + 0.125F), (double)((float)i5 - 0.125F), d66, d21);
                        }

                        if(this.worldObj.isBlockNormalCube(y + 1, z, i5)) {
                            tessellator6.addVertexWithUV((double)((float)(y + 1) - 0.05F), (double)((float)z - 0.125F), (double)((float)(i5 + 1) + 0.125F), d66, d23);
                            tessellator6.addVertexWithUV((double)((float)(y + 1) - 0.05F), (double)((float)(z + 1) + 0.125F), (double)((float)(i5 + 1) + 0.125F), d66, d21);
                            tessellator6.addVertexWithUV((double)((float)(y + 1) - 0.05F), (double)((float)(z + 1) + 0.125F), (double)((float)i5 - 0.125F), d17, d21);
                            tessellator6.addVertexWithUV((double)((float)(y + 1) - 0.05F), (double)((float)z - 0.125F), (double)((float)i5 - 0.125F), d17, d23);
                        }

                        if(this.worldObj.isBlockNormalCube(y, z, i5 - 1)) {
                            tessellator6.addVertexWithUV((double)((float)(y + 1) + 0.125F), (double)((float)z - 0.125F), (double)((float)i5 + 0.05F), d27, d31);
                            tessellator6.addVertexWithUV((double)((float)(y + 1) + 0.125F), (double)((float)(z + 1) + 0.125F), (double)((float)i5 + 0.05F), d27, d29);
                            tessellator6.addVertexWithUV((double)((float)y - 0.125F), (double)((float)(z + 1) + 0.125F), (double)((float)i5 + 0.05F), d25, d29);
                            tessellator6.addVertexWithUV((double)((float)y - 0.125F), (double)((float)z - 0.125F), (double)((float)i5 + 0.05F), d25, d31);
                        }

                        if(this.worldObj.isBlockNormalCube(y, z, i5 + 1)) {
                            tessellator6.addVertexWithUV((double)((float)(y + 1) + 0.125F), (double)((float)(z + 1) + 0.125F), (double)((float)(i5 + 1) - 0.05F), d25, d29);
                            tessellator6.addVertexWithUV((double)((float)(y + 1) + 0.125F), (double)((float)z - 0.125F), (double)((float)(i5 + 1) - 0.05F), d25, d31);
                            tessellator6.addVertexWithUV((double)((float)y - 0.125F), (double)((float)z - 0.125F), (double)((float)(i5 + 1) - 0.05F), d27, d31);
                            tessellator6.addVertexWithUV((double)((float)y - 0.125F), (double)((float)(z + 1) + 0.125F), (double)((float)(i5 + 1) - 0.05F), d27, d29);
                        }

                        return true;
                    } else if(i5 == 8) {
                        i5 = z;
                        z = y;
                        y = x;
                        tessellator6 = Tessellator.instance;
                        i53 = block.getBlockTextureFromSide(0);
                        if(this.overrideBlockTexture >= 0) {
                            i53 = this.overrideBlockTexture;
                        }

                        f8 = block.getBlockBrightness(this.worldObj, x, z, i5);
                        tessellator6.setColorOpaque_F(f8, f8, f8);
                        x = (i53 & 15) << 4;
                        i54 = i53 & 240;
                        d16 = (double)((float)x / 256.0F);
                        d60 = (double)(((float)x + 15.99F) / 256.0F);
                        d20 = (double)((float)i54 / 256.0F);
                        d22 = (double)(((float)i54 + 15.99F) / 256.0F);
                        int i24;
                        if((i24 = this.worldObj.getBlockMetadata(y, z, i5)) == 5) {
                            tessellator6.addVertexWithUV((double)((float)y + 0.05F), (double)((float)(z + 1)), (double)((float)(i5 + 1)), d16, d20);
                            tessellator6.addVertexWithUV((double)((float)y + 0.05F), (double)((float)z), (double)((float)(i5 + 1)), d16, d22);
                            tessellator6.addVertexWithUV((double)((float)y + 0.05F), (double)((float)z), (double)((float)i5), d60, d22);
                            tessellator6.addVertexWithUV((double)((float)y + 0.05F), (double)((float)(z + 1)), (double)((float)i5), d60, d20);
                        }

                        if(i24 == 4) {
                            tessellator6.addVertexWithUV((double)((float)(y + 1) - 0.05F), (double)((float)z), (double)((float)(i5 + 1)), d60, d22);
                            tessellator6.addVertexWithUV((double)((float)(y + 1) - 0.05F), (double)((float)(z + 1)), (double)((float)(i5 + 1)), d60, d20);
                            tessellator6.addVertexWithUV((double)((float)(y + 1) - 0.05F), (double)((float)(z + 1)), (double)((float)i5), d16, d20);
                            tessellator6.addVertexWithUV((double)((float)(y + 1) - 0.05F), (double)((float)z), (double)((float)i5), d16, d22);
                        }

                        if(i24 == 3) {
                            tessellator6.addVertexWithUV((double)((float)(y + 1)), (double)((float)z), (double)((float)i5 + 0.05F), d60, d22);
                            tessellator6.addVertexWithUV((double)((float)(y + 1)), (double)((float)(z + 1)), (double)((float)i5 + 0.05F), d60, d20);
                            tessellator6.addVertexWithUV((double)((float)y), (double)((float)(z + 1)), (double)((float)i5 + 0.05F), d16, d20);
                            tessellator6.addVertexWithUV((double)((float)y), (double)((float)z), (double)((float)i5 + 0.05F), d16, d22);
                        }

                        if(i24 == 2) {
                            tessellator6.addVertexWithUV((double)((float)(y + 1)), (double)((float)(z + 1)), (double)((float)(i5 + 1) - 0.05F), d16, d20);
                            tessellator6.addVertexWithUV((double)((float)(y + 1)), (double)((float)z), (double)((float)(i5 + 1) - 0.05F), d16, d22);
                            tessellator6.addVertexWithUV((double)((float)y), (double)((float)z), (double)((float)(i5 + 1) - 0.05F), d60, d22);
                            tessellator6.addVertexWithUV((double)((float)y), (double)((float)(z + 1)), (double)((float)(i5 + 1) - 0.05F), d60, d20);
                        }

                        return true;
                    } else if(i5 == 7) {
                        tessellator6 = Tessellator.instance;
                        BlockDoor blockDoor7 = (BlockDoor)block;
                        f18 = block.getBlockBrightness(this.worldObj, x, y, z);
                        float f19 = block.getBlockBrightness(this.worldObj, x, y - 1, z);
                        if(blockDoor7.minY > 0.0D) {
                            f19 = f18;
                        }

                        if(Block.lightValue[block.blockID] > 0) {
                            f19 = 1.0F;
                        }

                        tessellator6.setColorOpaque_F(0.5F * f19, 0.5F * f19, 0.5F * f19);
                        this.renderBottomFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.worldObj, x, y, z, 0));
                        f19 = block.getBlockBrightness(this.worldObj, x, y + 1, z);
                        if(blockDoor7.maxY < 1.0D) {
                            f19 = f18;
                        }

                        if(Block.lightValue[block.blockID] > 0) {
                            f19 = 1.0F;
                        }

                        tessellator6.setColorOpaque_F(f19 * 1.0F, f19 * 1.0F, f19 * 1.0F);
                        this.renderTopFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.worldObj, x, y, z, 1));
                        f19 = block.getBlockBrightness(this.worldObj, x, y, z - 1);
                        if(blockDoor7.minZ > 0.0D) {
                            f19 = f18;
                        }

                        if(Block.lightValue[block.blockID] > 0) {
                            f19 = 1.0F;
                        }

                        tessellator6.setColorOpaque_F(0.8F * f19, 0.8F * f19, 0.8F * f19);
                        this.renderEastFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.worldObj, x, y, z, 2));
                        f19 = block.getBlockBrightness(this.worldObj, x, y, z + 1);
                        if(blockDoor7.maxZ < 1.0D) {
                            f19 = f18;
                        }

                        if(Block.lightValue[block.blockID] > 0) {
                            f19 = 1.0F;
                        }

                        tessellator6.setColorOpaque_F(0.8F * f19, 0.8F * f19, 0.8F * f19);
                        this.renderWestFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.worldObj, x, y, z, 3));
                        f19 = block.getBlockBrightness(this.worldObj, x - 1, y, z);
                        if(blockDoor7.minX > 0.0D) {
                            f19 = f18;
                        }

                        if(Block.lightValue[block.blockID] > 0) {
                            f19 = 1.0F;
                        }

                        tessellator6.setColorOpaque_F(0.6F * f19, 0.6F * f19, 0.6F * f19);
                        this.renderNorthFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.worldObj, x, y, z, 4));
                        f19 = block.getBlockBrightness(this.worldObj, x + 1, y, z);
                        if(blockDoor7.maxX < 1.0D) {
                            f19 = f18;
                        }

                        if(Block.lightValue[block.blockID] > 0) {
                            f19 = 1.0F;
                        }

                        tessellator6.setColorOpaque_F(0.6F * f19, 0.6F * f19, 0.6F * f19);
                        this.renderSouthFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.worldObj, x, y, z, 5));
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
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

	private void renderBlockCrops(Block block, int metadata, double x, double y, double z) {
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

    private float getFluidHeight(int x, int y, int z, Material material) {
        int i5 = 0;
        float f6 = 0.0F;

        int i7;
        for(i7 = 0; i7 < 4; ++i7) {
            int i8 = x - (i7 & 1);
            int i9 = z - (i7 >> 1 & 1);
            if(this.worldObj.getBlockMaterial(i8, y + 1, i9) == material) {
                return 1.0F;
            }

            Material material10;
            if((material10 = this.worldObj.getBlockMaterial(i8, y, i9)) != material) {
                if(!material10.isSolid()) {
                    ++f6;
                    ++i5;
                }
            } else {
                if((i8 = this.worldObj.getBlockMetadata(i8, y, i9)) >= 8 || i8 == 0) {
                    f6 += BlockFluid.getFluidHeightPercent(i8) * 10.0F;
                    i5 += 10;
                }

                f6 += BlockFluid.getFluidHeightPercent(i8);
                ++i5;
            }
        }

        float f11 = (float)((i7 = x * 3111 + z * 31827) * i7 * 31287123 + i7 * 318127 & 7) / 7.0F / 9.0F;
        return 1.0F - f6 / (float)i5 + f11;
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
        if(block.minX < 0.0D || block.maxX > 1.0D) {
            d12 = (double)((float)i10 / 256.0F);
            d14 = (double)(((float)i10 + 15.99F) / 256.0F);
        }

        if(block.minY < 0.0D || block.maxY > 1.0D) {
            d16 = (double)((float)blockTexture / 256.0F);
            d18 = (double)(((float)blockTexture + 15.99F) / 256.0F);
        }

		double d20 = x + block.minX;
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
        if(block.minX < 0.0D || block.maxX > 1.0D) {
            d12 = (double)((float)i10 / 256.0F);
            d14 = (double)(((float)i10 + 15.99F) / 256.0F);
        }

        if(block.minY < 0.0D || block.maxY > 1.0D) {
            d16 = (double)((float)blockTexture / 256.0F);
            d18 = (double)(((float)blockTexture + 15.99F) / 256.0F);
        }

		double d20 = x + block.minX;
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
        if(block.minZ < 0.0D || block.maxZ > 1.0D) {
            d12 = (double)((float)i10 / 256.0F);
            d14 = (double)(((float)i10 + 15.99F) / 256.0F);
        }

        if(block.minY < 0.0D || block.maxY > 1.0D) {
            d16 = (double)((float)blockTexture / 256.0F);
            d18 = (double)(((float)blockTexture + 15.99F) / 256.0F);
        }

		double d20 = x + block.minX;
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
        if(block.minZ < 0.0D || block.maxZ > 1.0D) {
            d12 = (double)((float)i10 / 256.0F);
            d14 = (double)(((float)i10 + 15.99F) / 256.0F);
        }

        if(block.minY < 0.0D || block.maxY > 1.0D) {
            d16 = (double)((float)blockTexture / 256.0F);
            d18 = (double)(((float)blockTexture + 15.99F) / 256.0F);
        }

		double d20 = x + block.maxX;
		double d22 = y + block.minY;
		double d24 = y + block.maxY;
		double d26 = z + block.minZ;
		double d28 = z + block.maxZ;
		tessellator9.addVertexWithUV(d20, d22, d28, d12, d18);
		tessellator9.addVertexWithUV(d20, d22, d26, d14, d18);
		tessellator9.addVertexWithUV(d20, d24, d26, d14, d16);
		tessellator9.addVertexWithUV(d20, d24, d28, d12, d16);
	}

	public final void renderBlockOnInventory(Block block) {
		Tessellator tessellator2 = Tessellator.instance;
		int i3;
		if((i3 = block.getRenderType()) == 0) {
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
			this.renderBlockCrops(block, -1, -0.5D, -0.5D, -0.5D);
			tessellator2.draw();
		} else {
			if(i3 == 2) {
				tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
				tessellator2.setNormal(0.0F, -1.0F, 0.0F);
				this.renderTorchAtAngle(block, -0.5D, -0.5D, -0.5D, 0.0D, 0.0D);
				tessellator2.draw();
			}

		}
	}
}