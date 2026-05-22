package net.minecraft.client.render;

import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.BlockDoor;
import net.minecraft.game.world.block.BlockFluid;
import net.minecraft.game.world.material.Material;

public final class RenderBlocks {
    private IBlockAccess blockAccess;
	private int overrideBlockTexture = -1;
    private boolean flipTexture = false;
	private boolean renderAllFaces = false;

    public RenderBlocks(IBlockAccess iBlockAccess) {
        this.blockAccess = iBlockAccess;
    }

	public RenderBlocks() {
	}

	public final void renderBlockUsingTexture(Block block, int x, int y, int z, int overrideBlockTextureIndex) {
		this.overrideBlockTexture = overrideBlockTextureIndex;
		this.renderBlockByRenderType(block, x, y, z);
		this.overrideBlockTexture = -1;
	}

    public final boolean renderBlockByRenderType(Block block, int x, int y, int z) {
        int i5 = block.getRenderType();
        block.setBlockBoundsBasedOnState(this.blockAccess, x, y, z);
        if(i5 == 0) {
            return this.renderStandardBlockWithColorMultiplier(block, x, y, z);
        } else {
            Tessellator tessellator6;
            boolean z63;
            float f18;
            float f26;
            float f27;
            float f28;
            float f29;
            float f30;
            float f31;
            float f37;
            float f57;
            float f62;
            if(i5 == 4) {
                i5 = z;
                z = y;
                y = x;
                Block block54 = block;
                RenderBlocks renderBlocks53 = this;
                tessellator6 = Tessellator.instance;
                z63 = block.shouldSideBeRendered(this.blockAccess, x, z + 1, i5, 1);
                boolean z60 = block.shouldSideBeRendered(this.blockAccess, x, z - 1, i5, 0);
                boolean[] z61;
                (z61 = new boolean[4])[0] = block.shouldSideBeRendered(this.blockAccess, x, z, i5 - 1, 2);
                z61[1] = block.shouldSideBeRendered(this.blockAccess, x, z, i5 + 1, 3);
                z61[2] = block.shouldSideBeRendered(this.blockAccess, x - 1, z, i5, 4);
                z61[3] = block.shouldSideBeRendered(this.blockAccess, x + 1, z, i5, 5);
                if(!z63 && !z60 && !z61[0] && !z61[1] && !z61[2] && !z61[3]) {
                    return false;
                } else {
                    boolean z64 = false;
                    Material material69 = block.blockMaterial;
                    int i70 = this.blockAccess.getBlockMetadata(x, z, i5);
                    f26 = this.getFluidHeight(x, z, i5, material69);
                    f27 = this.getFluidHeight(x, z, i5 + 1, material69);
                    f28 = this.getFluidHeight(x + 1, z, i5 + 1, material69);
                    f29 = this.getFluidHeight(x + 1, z, i5, material69);
                    float f38;
                    float f40;
                    int i73;
                    int i79;
                    float f86;
                    if(z63) {
                        z64 = true;
                        i73 = block.getBlockTextureFromSideAndMetadata(1, i70);
                        if((f31 = (float)BlockFluid.getFlowDirection(this.blockAccess, x, z, i5, material69)) > -999.0F) {
                            i73 = block.getBlockTextureFromSideAndMetadata(2, i70);
                        }

                        int i75 = (i73 & 15) << 4;
                        i79 = i73 & 240;
                        double d77 = ((double)i75 + 8.0D) / 256.0D;
                        double d83 = ((double)i79 + 8.0D) / 256.0D;
                        if(f31 < -999.0F) {
                            f31 = 0.0F;
                        } else {
                            d77 = (double)((float)(i75 + 16) / 256.0F);
                            d83 = (double)((float)(i79 + 16) / 256.0F);
                        }

                        f38 = MathHelper.sin(f31) * 8.0F / 256.0F;
                        f86 = MathHelper.cos(f31) * 8.0F / 256.0F;
                        f40 = block.getBlockBrightness(this.blockAccess, x, z, i5);
                        tessellator6.setColorOpaque_F(f40 * 1.0F, f40 * 1.0F, f40 * 1.0F);
                        tessellator6.addVertexWithUV((double)x, (double)((float)z + f26), (double)i5, d77 - (double)f86 - (double)f38, d83 - (double)f86 + (double)f38);
                        tessellator6.addVertexWithUV((double)x, (double)((float)z + f27), (double)(i5 + 1), d77 - (double)f86 + (double)f38, d83 + (double)f86 + (double)f38);
                        tessellator6.addVertexWithUV((double)(x + 1), (double)((float)z + f28), (double)(i5 + 1), d77 + (double)f86 + (double)f38, d83 + (double)f86 - (double)f38);
                        tessellator6.addVertexWithUV((double)(x + 1), (double)((float)z + f29), (double)i5, d77 + (double)f86 - (double)f38, d83 - (double)f86 - (double)f38);
                    }

                    if(z60) {
                        f30 = block.getBlockBrightness(this.blockAccess, x, z - 1, i5);
                        tessellator6.setColorOpaque_F(0.5F * f30, 0.5F * f30, 0.5F * f30);
                        this.renderBottomFace(block, (double)x, (double)z, (double)i5, block.getBlockTextureFromSide(0));
                        z64 = true;
                    }

                    for(i73 = 0; i73 < 4; ++i73) {
                        int i78 = y;
                        i79 = i5;
                        if(i73 == 0) {
                            i79 = i5 - 1;
                        }

                        if(i73 == 1) {
                            ++i79;
                        }

                        if(i73 == 2) {
                            i78 = y - 1;
                        }

                        if(i73 == 3) {
                            ++i78;
                        }

                        int i81;
                        int i82 = ((i81 = block54.getBlockTextureFromSideAndMetadata(i73 + 2, i70)) & 15) << 4;
                        int i85 = i81 & 240;
                        if(z61[i73]) {
                            if(i73 == 0) {
                                f37 = f26;
                                f38 = f29;
                                f86 = (float)y;
                                f62 = (float)(y + 1);
                                f57 = f40 = (float)i5;
                            } else if(i73 == 1) {
                                f37 = f28;
                                f38 = f27;
                                f86 = (float)(y + 1);
                                f62 = (float)y;
                                f57 = f40 = (float)(i5 + 1);
                            } else if(i73 == 2) {
                                f37 = f27;
                                f38 = f26;
                                f62 = f86 = (float)y;
                                f40 = (float)(i5 + 1);
                                f57 = (float)i5;
                            } else {
                                f37 = f29;
                                f38 = f28;
                                f62 = f86 = (float)(y + 1);
                                f40 = (float)i5;
                                f57 = (float)(i5 + 1);
                            }

                            z64 = true;
                            double d43 = (double)((float)i82 / 256.0F);
                            double d45 = ((double)(i82 + 16) - 0.01D) / 256.0D;
                            double d47 = (double)(((float)i85 + (1.0F - f37) * 16.0F) / 256.0F);
                            double d49 = (double)(((float)i85 + (1.0F - f38) * 16.0F) / 256.0F);
                            double d51 = ((double)(i85 + 16) - 0.01D) / 256.0D;
                            float f11 = block54.getBlockBrightness(renderBlocks53.blockAccess, i78, z, i79);
                            if(i73 < 2) {
                                f11 *= 0.8F;
                            } else {
                                f11 *= 0.6F;
                            }

                            tessellator6.setColorOpaque_F(f11 * 1.0F, f11 * 1.0F, f11 * 1.0F);
                            tessellator6.addVertexWithUV((double)f86, (double)((float)z + f37), (double)f40, d43, d47);
                            tessellator6.addVertexWithUV((double)f62, (double)((float)z + f38), (double)f57, d45, d49);
                            tessellator6.addVertexWithUV((double)f62, (double)z, (double)f57, d45, d51);
                            tessellator6.addVertexWithUV((double)f86, (double)z, (double)f40, d43, d51);
                        }
                    }

                    block54.minY = 0.0D;
                    block54.maxY = 1.0D;
                    return z64;
                }
            } else if(i5 == 1) {
                tessellator6 = Tessellator.instance;
                f62 = block.getBlockBrightness(this.blockAccess, x, y, z);
                tessellator6.setColorOpaque_F(f62, f62, f62);
                this.renderCrossedSquares(block, this.blockAccess.getBlockMetadata(x, y, z), (double)x, (double)y, (double)z);
                return true;
            } else if(i5 == 6) {
                tessellator6 = Tessellator.instance;
                f62 = block.getBlockBrightness(this.blockAccess, x, y, z);
                tessellator6.setColorOpaque_F(f62, f62, f62);
                this.renderBlockCropsImpl(block, this.blockAccess.getBlockMetadata(x, y, z), (double)x, (double)((float)y - 0.0625F), (double)z);
                return true;
            } else if(i5 == 2) {
                int i56 = this.blockAccess.getBlockMetadata(x, y, z);
                Tessellator tessellator59 = Tessellator.instance;
                f57 = block.getBlockBrightness(this.blockAccess, x, y, z);
                if(Block.lightValue[block.blockID] > 0) {
                    f57 = 1.0F;
                }

                tessellator59.setColorOpaque_F(f57, f57, f57);
                if(i56 == 1) {
                    this.renderTorchAtAngle(block, (double)x - 0.09999999403953552D, (double)y + (double)0.2F, (double)z, -0.4000000059604645D, 0.0D);
                } else if(i56 == 2) {
                    this.renderTorchAtAngle(block, (double)x + 0.09999999403953552D, (double)y + (double)0.2F, (double)z, (double)0.4F, 0.0D);
                } else if(i56 == 3) {
                    this.renderTorchAtAngle(block, (double)x, (double)y + (double)0.2F, (double)z - 0.09999999403953552D, 0.0D, -0.4000000059604645D);
                } else if(i56 == 4) {
                    this.renderTorchAtAngle(block, (double)x, (double)y + (double)0.2F, (double)z + 0.09999999403953552D, 0.0D, (double)0.4F);
                } else {
                    this.renderTorchAtAngle(block, (double)x, (double)y, (double)z, 0.0D, 0.0D);
                }

                return true;
            } else {
                int i7;
                int i10;
                double d20;
                double d22;
                double d25;
                int i58;
                double d65;
                double d67;
                double d71;
                double d72;
                double d74;
                if(i5 == 3) {
                    i5 = z;
                    tessellator6 = Tessellator.instance;
                    i7 = block.getBlockTextureFromSide(0);
                    if(this.overrideBlockTexture >= 0) {
                        i7 = this.overrideBlockTexture;
                    }

                    f57 = block.getBlockBrightness(this.blockAccess, x, y, z);
                    tessellator6.setColorOpaque_F(f57, f57, f57);
                    i58 = (i7 & 15) << 4;
                    i10 = i7 & 240;
                    d65 = (double)((float)i58 / 256.0F);
                    d67 = (double)(((float)i58 + 15.99F) / 256.0F);
                    d20 = (double)((float)i10 / 256.0F);
                    d22 = (double)(((float)i10 + 15.99F) / 256.0F);
                    double d76;
                    if(!this.blockAccess.isBlockNormalCube(x, y - 1, z) && !Block.fire.canBlockCatchFire(this.blockAccess, x, y - 1, z)) {
                        if((x + y + z & 1) == 1) {
                            d65 = (double)((float)i58 / 256.0F);
                            d67 = (double)(((float)i58 + 15.99F) / 256.0F);
                            d20 = (double)((float)(i10 + 16) / 256.0F);
                            d22 = (double)(((float)i10 + 15.99F + 16.0F) / 256.0F);
                        }

                        if((x / 2 + y / 2 + z / 2 & 1) == 1) {
                            d71 = d67;
                            d67 = d65;
                            d65 = d71;
                        }

                        if(Block.fire.canBlockCatchFire(this.blockAccess, x - 1, y, z)) {
                            tessellator6.addVertexWithUV((double)((float)x + 0.2F), (double)((float)y + 1.4F + 0.0625F), (double)(z + 1), d67, d20);
                            tessellator6.addVertexWithUV((double)x, (double)((float)y + 0.0625F), (double)(z + 1), d67, d22);
                            tessellator6.addVertexWithUV((double)x, (double)((float)y + 0.0625F), (double)z, d65, d22);
                            tessellator6.addVertexWithUV((double)((float)x + 0.2F), (double)((float)y + 1.4F + 0.0625F), (double)z, d65, d20);
                            tessellator6.addVertexWithUV((double)((float)x + 0.2F), (double)((float)y + 1.4F + 0.0625F), (double)z, d65, d20);
                            tessellator6.addVertexWithUV((double)x, (double)((float)y + 0.0625F), (double)z, d65, d22);
                            tessellator6.addVertexWithUV((double)x, (double)((float)y + 0.0625F), (double)(z + 1), d67, d22);
                            tessellator6.addVertexWithUV((double)((float)x + 0.2F), (double)((float)y + 1.4F + 0.0625F), (double)(z + 1), d67, d20);
                        }

                        if(Block.fire.canBlockCatchFire(this.blockAccess, x + 1, y, z)) {
                            tessellator6.addVertexWithUV((double)((float)(x + 1) - 0.2F), (double)((float)y + 1.4F + 0.0625F), (double)z, d65, d20);
                            tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 0.0625F), (double)z, d65, d22);
                            tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 0.0625F), (double)(z + 1), d67, d22);
                            tessellator6.addVertexWithUV((double)((float)(x + 1) - 0.2F), (double)((float)y + 1.4F + 0.0625F), (double)(z + 1), d67, d20);
                            tessellator6.addVertexWithUV((double)((float)(x + 1) - 0.2F), (double)((float)y + 1.4F + 0.0625F), (double)(z + 1), d67, d20);
                            tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 0.0625F), (double)(z + 1), d67, d22);
                            tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 0.0625F), (double)z, d65, d22);
                            tessellator6.addVertexWithUV((double)((float)(x + 1) - 0.2F), (double)((float)y + 1.4F + 0.0625F), (double)z, d65, d20);
                        }

                        if(Block.fire.canBlockCatchFire(this.blockAccess, x, y, z - 1)) {
                            tessellator6.addVertexWithUV((double)x, (double)((float)y + 1.4F + 0.0625F), (double)((float)z + 0.2F), d67, d20);
                            tessellator6.addVertexWithUV((double)x, (double)((float)y + 0.0625F), (double)z, d67, d22);
                            tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 0.0625F), (double)z, d65, d22);
                            tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 1.4F + 0.0625F), (double)((float)z + 0.2F), d65, d20);
                            tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 1.4F + 0.0625F), (double)((float)z + 0.2F), d65, d20);
                            tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 0.0625F), (double)z, d65, d22);
                            tessellator6.addVertexWithUV((double)x, (double)((float)y + 0.0625F), (double)z, d67, d22);
                            tessellator6.addVertexWithUV((double)x, (double)((float)y + 1.4F + 0.0625F), (double)((float)z + 0.2F), d67, d20);
                        }

                        if(Block.fire.canBlockCatchFire(this.blockAccess, x, y, z + 1)) {
                            tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 1.4F + 0.0625F), (double)((float)(z + 1) - 0.2F), d65, d20);
                            tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 0.0625F), (double)(z + 1), d65, d22);
                            tessellator6.addVertexWithUV((double)x, (double)((float)y + 0.0625F), (double)(z + 1), d67, d22);
                            tessellator6.addVertexWithUV((double)x, (double)((float)y + 1.4F + 0.0625F), (double)((float)(z + 1) - 0.2F), d67, d20);
                            tessellator6.addVertexWithUV((double)x, (double)((float)y + 1.4F + 0.0625F), (double)((float)(z + 1) - 0.2F), d67, d20);
                            tessellator6.addVertexWithUV((double)x, (double)((float)y + 0.0625F), (double)(z + 1), d67, d22);
                            tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 0.0625F), (double)(z + 1), d65, d22);
                            tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 1.4F + 0.0625F), (double)((float)(z + 1) - 0.2F), d65, d20);
                        }

                        if(Block.fire.canBlockCatchFire(this.blockAccess, x, y + 1, z)) {
                            d71 = (double)x + 0.5D + 0.5D;
                            d72 = (double)x + 0.5D - 0.5D;
                            d74 = (double)z + 0.5D + 0.5D;
                            d76 = (double)z + 0.5D - 0.5D;
                            d65 = (double)((float)i58 / 256.0F);
                            d67 = (double)(((float)i58 + 15.99F) / 256.0F);
                            d20 = (double)((float)i10 / 256.0F);
                            d22 = (double)(((float)i10 + 15.99F) / 256.0F);
                            z = y + 1;
                            if((x + z + i5 & 1) == 0) {
                                tessellator6.addVertexWithUV(d72, (double)((float)z + -0.2F), (double)i5, d67, d20);
                                tessellator6.addVertexWithUV(d71, (double)z, (double)i5, d67, d22);
                                tessellator6.addVertexWithUV(d71, (double)z, (double)(i5 + 1), d65, d22);
                                tessellator6.addVertexWithUV(d72, (double)((float)z + -0.2F), (double)(i5 + 1), d65, d20);
                                d65 = (double)((float)i58 / 256.0F);
                                d67 = (double)(((float)i58 + 15.99F) / 256.0F);
                                d20 = (double)((float)(i10 + 16) / 256.0F);
                                d22 = (double)(((float)i10 + 15.99F + 16.0F) / 256.0F);
                                tessellator6.addVertexWithUV(d71, (double)((float)z + -0.2F), (double)(i5 + 1), d67, d20);
                                tessellator6.addVertexWithUV(d72, (double)z, (double)(i5 + 1), d67, d22);
                                tessellator6.addVertexWithUV(d72, (double)z, (double)i5, d65, d22);
                                tessellator6.addVertexWithUV(d71, (double)((float)z + -0.2F), (double)i5, d65, d20);
                            } else {
                                tessellator6.addVertexWithUV((double)x, (double)((float)z + -0.2F), d74, d67, d20);
                                tessellator6.addVertexWithUV((double)x, (double)z, d76, d67, d22);
                                tessellator6.addVertexWithUV((double)(x + 1), (double)z, d76, d65, d22);
                                tessellator6.addVertexWithUV((double)(x + 1), (double)((float)z + -0.2F), d74, d65, d20);
                                d65 = (double)((float)i58 / 256.0F);
                                d67 = (double)(((float)i58 + 15.99F) / 256.0F);
                                d20 = (double)((float)(i10 + 16) / 256.0F);
                                d22 = (double)(((float)i10 + 15.99F + 16.0F) / 256.0F);
                                tessellator6.addVertexWithUV((double)(x + 1), (double)((float)z + -0.2F), d76, d67, d20);
                                tessellator6.addVertexWithUV((double)(x + 1), (double)z, d74, d67, d22);
                                tessellator6.addVertexWithUV((double)x, (double)z, d74, d65, d22);
                                tessellator6.addVertexWithUV((double)x, (double)((float)z + -0.2F), d76, d65, d20);
                            }
                        }
                    } else {
                        d25 = (double)x + 0.5D + 0.2D;
                        d71 = (double)x + 0.5D - 0.2D;
                        d72 = (double)z + 0.5D + 0.2D;
                        d74 = (double)z + 0.5D - 0.2D;
                        d76 = (double)x + 0.5D - 0.3D;
                        double d80 = (double)x + 0.5D + 0.3D;
                        double d84 = (double)z + 0.5D - 0.3D;
                        double d39 = (double)z + 0.5D + 0.3D;
                        tessellator6.addVertexWithUV(d76, (double)((float)y + 1.4F), (double)(z + 1), d67, d20);
                        tessellator6.addVertexWithUV(d25, (double)y, (double)(z + 1), d67, d22);
                        tessellator6.addVertexWithUV(d25, (double)y, (double)z, d65, d22);
                        tessellator6.addVertexWithUV(d76, (double)((float)y + 1.4F), (double)z, d65, d20);
                        tessellator6.addVertexWithUV(d80, (double)((float)y + 1.4F), (double)z, d67, d20);
                        tessellator6.addVertexWithUV(d71, (double)y, (double)z, d67, d22);
                        tessellator6.addVertexWithUV(d71, (double)y, (double)(z + 1), d65, d22);
                        tessellator6.addVertexWithUV(d80, (double)((float)y + 1.4F), (double)(z + 1), d65, d20);
                        d65 = (double)((float)i58 / 256.0F);
                        d67 = (double)(((float)i58 + 15.99F) / 256.0F);
                        d20 = (double)((float)(i10 + 16) / 256.0F);
                        d22 = (double)(((float)i10 + 15.99F + 16.0F) / 256.0F);
                        tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 1.4F), d39, d67, d20);
                        tessellator6.addVertexWithUV((double)(x + 1), (double)y, d74, d67, d22);
                        tessellator6.addVertexWithUV((double)x, (double)y, d74, d65, d22);
                        tessellator6.addVertexWithUV((double)x, (double)((float)y + 1.4F), d39, d65, d20);
                        tessellator6.addVertexWithUV((double)x, (double)((float)y + 1.4F), d84, d67, d20);
                        tessellator6.addVertexWithUV((double)x, (double)y, d72, d67, d22);
                        tessellator6.addVertexWithUV((double)(x + 1), (double)y, d72, d65, d22);
                        tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 1.4F), d84, d65, d20);
                        d25 = (double)x + 0.5D - 0.5D;
                        d71 = (double)x + 0.5D + 0.5D;
                        d72 = (double)z + 0.5D - 0.5D;
                        d74 = (double)z + 0.5D + 0.5D;
                        d76 = (double)x + 0.5D - 0.4D;
                        d80 = (double)x + 0.5D + 0.4D;
                        d84 = (double)z + 0.5D - 0.4D;
                        d39 = (double)z + 0.5D + 0.4D;
                        tessellator6.addVertexWithUV(d76, (double)((float)y + 1.4F), (double)z, d65, d20);
                        tessellator6.addVertexWithUV(d25, (double)y, (double)z, d65, d22);
                        tessellator6.addVertexWithUV(d25, (double)y, (double)(z + 1), d67, d22);
                        tessellator6.addVertexWithUV(d76, (double)((float)y + 1.4F), (double)(z + 1), d67, d20);
                        tessellator6.addVertexWithUV(d80, (double)((float)y + 1.4F), (double)(z + 1), d65, d20);
                        tessellator6.addVertexWithUV(d71, (double)y, (double)(z + 1), d65, d22);
                        tessellator6.addVertexWithUV(d71, (double)y, (double)z, d67, d22);
                        tessellator6.addVertexWithUV(d80, (double)((float)y + 1.4F), (double)z, d67, d20);
                        d65 = (double)((float)i58 / 256.0F);
                        d67 = (double)(((float)i58 + 15.99F) / 256.0F);
                        d20 = (double)((float)i10 / 256.0F);
                        d22 = (double)(((float)i10 + 15.99F) / 256.0F);
                        tessellator6.addVertexWithUV((double)x, (double)((float)y + 1.4F), d39, d65, d20);
                        tessellator6.addVertexWithUV((double)x, (double)y, d74, d65, d22);
                        tessellator6.addVertexWithUV((double)(x + 1), (double)y, d74, d67, d22);
                        tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 1.4F), d39, d67, d20);
                        tessellator6.addVertexWithUV((double)(x + 1), (double)((float)y + 1.4F), d84, d65, d20);
                        tessellator6.addVertexWithUV((double)(x + 1), (double)y, d72, d65, d22);
                        tessellator6.addVertexWithUV((double)x, (double)y, d72, d67, d22);
                        tessellator6.addVertexWithUV((double)x, (double)((float)y + 1.4F), d84, d67, d20);
                    }

                    return true;
                } else {
                    int i16;
                    double d17;
                    double d19;
                    double d21;
                    double d23;
                    if(i5 == 5) {
                        tessellator6 = Tessellator.instance;
                        i7 = block.getBlockTextureFromSide(0);
                        if(this.overrideBlockTexture >= 0) {
                            i7 = this.overrideBlockTexture;
                        }

                        f57 = block.getBlockBrightness(this.blockAccess, x, y, z);
                        tessellator6.setColorOpaque_F(f57, f57, f57);
                        i58 = ((i7 & 15) << 4) + 16;
                        i10 = (i7 & 15) << 4;
                        i16 = i7 & 240;
                        if((x + y + z & 1) == 1) {
                            i58 = (i7 & 15) << 4;
                            i10 = ((i7 & 15) << 4) + 16;
                        }

                        d17 = (double)((float)i58 / 256.0F);
                        d19 = (double)(((float)i58 + 15.99F) / 256.0F);
                        d21 = (double)((float)i16 / 256.0F);
                        d23 = (double)(((float)i16 + 15.99F) / 256.0F);
                        d25 = (double)((float)i10 / 256.0F);
                        d71 = (double)(((float)i10 + 15.99F) / 256.0F);
                        d72 = (double)((float)i16 / 256.0F);
                        d74 = (double)(((float)i16 + 15.99F) / 256.0F);
                        if(this.blockAccess.isBlockNormalCube(x - 1, y, z)) {
                            tessellator6.addVertexWithUV((double)((float)x + 0.05F), (double)((float)(y + 1) + 0.125F), (double)((float)(z + 1) + 0.125F), d17, d21);
                            tessellator6.addVertexWithUV((double)((float)x + 0.05F), (double)((float)y - 0.125F), (double)((float)(z + 1) + 0.125F), d17, d23);
                            tessellator6.addVertexWithUV((double)((float)x + 0.05F), (double)((float)y - 0.125F), (double)((float)z - 0.125F), d19, d23);
                            tessellator6.addVertexWithUV((double)((float)x + 0.05F), (double)((float)(y + 1) + 0.125F), (double)((float)z - 0.125F), d19, d21);
                        }

                        if(this.blockAccess.isBlockNormalCube(x + 1, y, z)) {
                            tessellator6.addVertexWithUV((double)((float)(x + 1) - 0.05F), (double)((float)y - 0.125F), (double)((float)(z + 1) + 0.125F), d19, d23);
                            tessellator6.addVertexWithUV((double)((float)(x + 1) - 0.05F), (double)((float)(y + 1) + 0.125F), (double)((float)(z + 1) + 0.125F), d19, d21);
                            tessellator6.addVertexWithUV((double)((float)(x + 1) - 0.05F), (double)((float)(y + 1) + 0.125F), (double)((float)z - 0.125F), d17, d21);
                            tessellator6.addVertexWithUV((double)((float)(x + 1) - 0.05F), (double)((float)y - 0.125F), (double)((float)z - 0.125F), d17, d23);
                        }

                        if(this.blockAccess.isBlockNormalCube(x, y, z - 1)) {
                            tessellator6.addVertexWithUV((double)((float)(x + 1) + 0.125F), (double)((float)y - 0.125F), (double)((float)z + 0.05F), d71, d74);
                            tessellator6.addVertexWithUV((double)((float)(x + 1) + 0.125F), (double)((float)(y + 1) + 0.125F), (double)((float)z + 0.05F), d71, d72);
                            tessellator6.addVertexWithUV((double)((float)x - 0.125F), (double)((float)(y + 1) + 0.125F), (double)((float)z + 0.05F), d25, d72);
                            tessellator6.addVertexWithUV((double)((float)x - 0.125F), (double)((float)y - 0.125F), (double)((float)z + 0.05F), d25, d74);
                        }

                        if(this.blockAccess.isBlockNormalCube(x, y, z + 1)) {
                            tessellator6.addVertexWithUV((double)((float)(x + 1) + 0.125F), (double)((float)(y + 1) + 0.125F), (double)((float)(z + 1) - 0.05F), d25, d72);
                            tessellator6.addVertexWithUV((double)((float)(x + 1) + 0.125F), (double)((float)y - 0.125F), (double)((float)(z + 1) - 0.05F), d25, d74);
                            tessellator6.addVertexWithUV((double)((float)x - 0.125F), (double)((float)y - 0.125F), (double)((float)(z + 1) - 0.05F), d71, d74);
                            tessellator6.addVertexWithUV((double)((float)x - 0.125F), (double)((float)(y + 1) + 0.125F), (double)((float)(z + 1) - 0.05F), d71, d72);
                        }

                        return true;
                    } else if(i5 == 8) {
                        tessellator6 = Tessellator.instance;
                        i7 = block.getBlockTextureFromSide(0);
                        if(this.overrideBlockTexture >= 0) {
                            i7 = this.overrideBlockTexture;
                        }

                        f57 = block.getBlockBrightness(this.blockAccess, x, y, z);
                        tessellator6.setColorOpaque_F(f57, f57, f57);
                        i58 = (i7 & 15) << 4;
                        i10 = i7 & 240;
                        d65 = (double)((float)i58 / 256.0F);
                        d67 = (double)(((float)i58 + 15.99F) / 256.0F);
                        d20 = (double)((float)i10 / 256.0F);
                        d22 = (double)(((float)i10 + 15.99F) / 256.0F);
                        int i24;
                        if((i24 = this.blockAccess.getBlockMetadata(x, y, z)) == 5) {
                            tessellator6.addVertexWithUV((double)((float)x + 0.05F), (double)((float)(y + 1)), (double)((float)(z + 1)), d65, d20);
                            tessellator6.addVertexWithUV((double)((float)x + 0.05F), (double)((float)y), (double)((float)(z + 1)), d65, d22);
                            tessellator6.addVertexWithUV((double)((float)x + 0.05F), (double)((float)y), (double)((float)z), d67, d22);
                            tessellator6.addVertexWithUV((double)((float)x + 0.05F), (double)((float)(y + 1)), (double)((float)z), d67, d20);
                        }

                        if(i24 == 4) {
                            tessellator6.addVertexWithUV((double)((float)(x + 1) - 0.05F), (double)((float)y), (double)((float)(z + 1)), d67, d22);
                            tessellator6.addVertexWithUV((double)((float)(x + 1) - 0.05F), (double)((float)(y + 1)), (double)((float)(z + 1)), d67, d20);
                            tessellator6.addVertexWithUV((double)((float)(x + 1) - 0.05F), (double)((float)(y + 1)), (double)((float)z), d65, d20);
                            tessellator6.addVertexWithUV((double)((float)(x + 1) - 0.05F), (double)((float)y), (double)((float)z), d65, d22);
                        }

                        if(i24 == 3) {
                            tessellator6.addVertexWithUV((double)((float)(x + 1)), (double)((float)y), (double)((float)z + 0.05F), d67, d22);
                            tessellator6.addVertexWithUV((double)((float)(x + 1)), (double)((float)(y + 1)), (double)((float)z + 0.05F), d67, d20);
                            tessellator6.addVertexWithUV((double)((float)x), (double)((float)(y + 1)), (double)((float)z + 0.05F), d65, d20);
                            tessellator6.addVertexWithUV((double)((float)x), (double)((float)y), (double)((float)z + 0.05F), d65, d22);
                        }

                        if(i24 == 2) {
                            tessellator6.addVertexWithUV((double)((float)(x + 1)), (double)((float)(y + 1)), (double)((float)(z + 1) - 0.05F), d65, d20);
                            tessellator6.addVertexWithUV((double)((float)(x + 1)), (double)((float)y), (double)((float)(z + 1) - 0.05F), d65, d22);
                            tessellator6.addVertexWithUV((double)((float)x), (double)((float)y), (double)((float)(z + 1) - 0.05F), d67, d22);
                            tessellator6.addVertexWithUV((double)((float)x), (double)((float)(y + 1)), (double)((float)(z + 1) - 0.05F), d67, d20);
                        }

                        return true;
                    } else if(i5 == 7) {
                        tessellator6 = Tessellator.instance;
                        BlockDoor blockDoor55 = (BlockDoor)block;
                        f18 = block.getBlockBrightness(this.blockAccess, x, y, z);
                        float f68 = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
                        if(blockDoor55.minY > 0.0D) {
                            f68 = f18;
                        }

                        if(Block.lightValue[block.blockID] > 0) {
                            f68 = 1.0F;
                        }

                        tessellator6.setColorOpaque_F(0.5F * f68, 0.5F * f68, 0.5F * f68);
                        this.renderBottomFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 0));
                        f68 = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
                        if(blockDoor55.maxY < 1.0D) {
                            f68 = f18;
                        }

                        if(Block.lightValue[block.blockID] > 0) {
                            f68 = 1.0F;
                        }

                        tessellator6.setColorOpaque_F(f68 * 1.0F, f68 * 1.0F, f68 * 1.0F);
                        this.renderTopFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 1));
                        f68 = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
                        if(blockDoor55.minZ > 0.0D) {
                            f68 = f18;
                        }

                        if(Block.lightValue[block.blockID] > 0) {
                            f68 = 1.0F;
                        }

                        tessellator6.setColorOpaque_F(0.8F * f68, 0.8F * f68, 0.8F * f68);
                        int i20;
                        if((i20 = block.getBlockTexture(this.blockAccess, x, y, z, 2)) < 0) {
                            this.flipTexture = true;
                            i20 = -i20;
                        }

                        this.renderEastFace(block, (double)x, (double)y, (double)z, i20);
                        this.flipTexture = false;
                        f68 = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
                        if(blockDoor55.maxZ < 1.0D) {
                            f68 = f18;
                        }

                        if(Block.lightValue[block.blockID] > 0) {
                            f68 = 1.0F;
                        }

                        tessellator6.setColorOpaque_F(0.8F * f68, 0.8F * f68, 0.8F * f68);
                        if((i20 = block.getBlockTexture(this.blockAccess, x, y, z, 3)) < 0) {
                            this.flipTexture = true;
                            i20 = -i20;
                        }

                        this.renderWestFace(block, (double)x, (double)y, (double)z, i20);
                        this.flipTexture = false;
                        f68 = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
                        if(blockDoor55.minX > 0.0D) {
                            f68 = f18;
                        }

                        if(Block.lightValue[block.blockID] > 0) {
                            f68 = 1.0F;
                        }

                        tessellator6.setColorOpaque_F(0.6F * f68, 0.6F * f68, 0.6F * f68);
                        if((i20 = block.getBlockTexture(this.blockAccess, x, y, z, 4)) < 0) {
                            this.flipTexture = true;
                            i20 = -i20;
                        }

                        this.renderNorthFace(block, (double)x, (double)y, (double)z, i20);
                        this.flipTexture = false;
                        f68 = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
                        if(blockDoor55.maxX < 1.0D) {
                            f68 = f18;
                        }

                        if(Block.lightValue[block.blockID] > 0) {
                            f68 = 1.0F;
                        }

                        tessellator6.setColorOpaque_F(0.6F * f68, 0.6F * f68, 0.6F * f68);
                        if((i20 = block.getBlockTexture(this.blockAccess, x, y, z, 5)) < 0) {
                            this.flipTexture = true;
                            i20 = -i20;
                        }

                        this.renderSouthFace(block, (double)x, (double)y, (double)z, i20);
                        this.flipTexture = false;
                        return true;
                    } else if(i5 == 9) {
                        tessellator6 = Tessellator.instance;
                        i7 = this.blockAccess.getBlockMetadata(x, y, z);
                        int i8 = block.getBlockTextureFromSideAndMetadata(0, i7);
                        if(this.overrideBlockTexture >= 0) {
                            i8 = this.overrideBlockTexture;
                        }

                        float f9 = block.getBlockBrightness(this.blockAccess, x, y, z);
                        tessellator6.setColorOpaque_F(f9, f9, f9);
                        i10 = (i8 & 15) << 4;
                        i16 = i8 & 240;
                        d17 = (double)((float)i10 / 256.0F);
                        d19 = (double)(((float)i10 + 15.99F) / 256.0F);
                        d21 = (double)((float)i16 / 256.0F);
                        d23 = (double)(((float)i16 + 15.99F) / 256.0F);
                        f27 = f26 = (float)(x + 1);
                        f29 = f28 = (float)x;
                        f30 = (float)z;
                        float f32 = f31 = (float)(z + 1);
                        float f33 = f30;
                        float f34;
                        float f35;
                        float f36;
                        f37 = f36 = f35 = f34 = (float)y + 0.0625F;
                        if(i7 != 1 && i7 != 2 && i7 != 3 && i7 != 7) {
                            if(i7 == 8) {
                                f27 = f29;
                                f26 = f29;
                                f28 = f29 = (float)(x + 1);
                                f33 = f32;
                                f30 = f32;
                                f31 = f32 = (float)z;
                            } else if(i7 == 9) {
                                f26 = f29 = f29;
                                f28 = f27;
                                f27 = f27;
                                f30 = f31 = f30;
                                f33 = f32;
                                f32 = f32;
                            }
                        } else {
                            f29 = f27;
                            f26 = f27;
                            f27 = f28 = f28;
                            f31 = f32;
                            f30 = f32;
                            f32 = f33 = f33;
                        }

                        if(i7 != 2 && i7 != 4) {
                            if(i7 == 3 || i7 == 5) {
                                f36 = f35 = f34 + 1.0F;
                            }
                        } else {
                            f37 = ++f34;
                        }

                        tessellator6.addVertexWithUV((double)f26, (double)f34, (double)f30, d19, d21);
                        tessellator6.addVertexWithUV((double)f27, (double)f35, (double)f31, d19, d23);
                        tessellator6.addVertexWithUV((double)f28, (double)f36, (double)f32, d17, d23);
                        tessellator6.addVertexWithUV((double)f29, (double)f37, (double)f33, d17, d21);
                        tessellator6.addVertexWithUV((double)f29, (double)f37, (double)f33, d17, d21);
                        tessellator6.addVertexWithUV((double)f28, (double)f36, (double)f32, d17, d23);
                        tessellator6.addVertexWithUV((double)f27, (double)f35, (double)f31, d19, d23);
                        tessellator6.addVertexWithUV((double)f26, (double)f34, (double)f30, d19, d21);
                        return true;
                    } else if(i5 == 10) {
                        if((i7 = this.blockAccess.getBlockMetadata(x, y, z)) == 0) {
                            block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
                            this.renderStandardBlockWithColorMultiplier(block, x, y, z);
                            block.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                            this.renderStandardBlockWithColorMultiplier(block, x, y, z);
                        } else if(i7 == 1) {
                            block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
                            this.renderStandardBlockWithColorMultiplier(block, x, y, z);
                            block.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
                            this.renderStandardBlockWithColorMultiplier(block, x, y, z);
                        } else if(i7 == 2) {
                            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
                            this.renderStandardBlockWithColorMultiplier(block, x, y, z);
                            block.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
                            this.renderStandardBlockWithColorMultiplier(block, x, y, z);
                        } else if(i7 == 3) {
                            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
                            this.renderStandardBlockWithColorMultiplier(block, x, y, z);
                            block.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
                            this.renderStandardBlockWithColorMultiplier(block, x, y, z);
                        }

                        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                        return false;
                    } else if(i5 != 11) {
                        return false;
                    } else {
                        block.setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
                        this.renderStandardBlockWithColorMultiplier(block, x, y, z);
                        boolean z9 = false;
                        boolean z10 = false;
                        if(this.blockAccess.getBlockId(x - 1, y, z) == block.blockID || this.blockAccess.getBlockId(x + 1, y, z) == block.blockID) {
                            z9 = true;
                        }

                        if(this.blockAccess.getBlockId(x, y, z - 1) == block.blockID || this.blockAccess.getBlockId(x, y, z + 1) == block.blockID) {
                            z10 = true;
                        }

                        if(!z9 && !z10) {
                            z9 = true;
                        }

                        if(z9) {
                            block.setBlockBounds(0.0F, 0.75F, 0.4375F, 1.0F, 0.9375F, 0.5625F);
                            this.renderStandardBlockWithColorMultiplier(block, x, y, z);
                        }

                        if(z10) {
                            block.setBlockBounds(0.4375F, 0.75F, 0.0F, 0.5625F, 0.9375F, 1.0F);
                            this.renderStandardBlockWithColorMultiplier(block, x, y, z);
                        }

                        if(z9) {
                            block.setBlockBounds(0.0F, 0.375F, 0.4375F, 1.0F, 0.5625F, 0.5625F);
                            this.renderStandardBlockWithColorMultiplier(block, x, y, z);
                        }

                        if(z10) {
                            block.setBlockBounds(0.4375F, 0.375F, 0.0F, 0.5625F, 0.5625F, 1.0F);
                            this.renderStandardBlockWithColorMultiplier(block, x, y, z);
                        }

                        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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
                    f6 += BlockFluid.getPercentAir(i8) * 10.0F;
                    i5 += 10;
                }

                f6 += BlockFluid.getPercentAir(i8);
                ++i5;
            }
        }

        return 1.0F - f6 / (float)i5;
    }

    public final void renderBlockFallingSand(Block block, World world, int x, int y, int z) {
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

    private boolean renderStandardBlockWithColorMultiplier(Block block, int x, int y, int z) {
        float f8 = 1.0F;
        float f7 = 1.0F;
        float f6 = 1.0F;
        Tessellator tessellator9 = Tessellator.instance;
        boolean z10 = false;
        float f11 = 0.5F * f6;
        float f12 = f6 * 1.0F;
        float f13 = 0.8F * f6;
        f6 = 0.6F * f6;
        float f14 = 0.5F * f7;
        float f15 = f7 * 1.0F;
        float f16 = 0.8F * f7;
        f7 = 0.6F * f7;
        float f17 = 0.5F * f8;
        float f18 = f8 * 1.0F;
        float f19 = 0.8F * f8;
        f8 = 0.6F * f8;
        float f20 = block.getBlockBrightness(this.blockAccess, x, y, z);
        float f21;
        if(block.shouldSideBeRendered(this.blockAccess, x, y - 1, z, 0)) {
            f21 = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
            if(Block.lightValue[block.blockID] > 0) {
                f21 = 1.0F;
            }

            tessellator9.setColorOpaque_F(f11 * f21, f14 * f21, f17 * f21);
            this.renderBottomFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 0));
            z10 = true;
        }

        if(block.shouldSideBeRendered(this.blockAccess, x, y + 1, z, 1)) {
            f21 = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
            if(block.maxY != 1.0D && !block.blockMaterial.getIsLiquid()) {
                f21 = f20;
            }

            if(Block.lightValue[block.blockID] > 0) {
                f21 = 1.0F;
            }

            tessellator9.setColorOpaque_F(f12 * f21, f15 * f21, f18 * f21);
            this.renderTopFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 1));
            z10 = true;
        }

        if(block.shouldSideBeRendered(this.blockAccess, x, y, z - 1, 2)) {
            f21 = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
            if(block.minZ > 0.0D) {
                f21 = f20;
            }

            if(Block.lightValue[block.blockID] > 0) {
                f21 = 1.0F;
            }

            tessellator9.setColorOpaque_F(f13 * f21, f16 * f21, f19 * f21);
            this.renderEastFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 2));
            z10 = true;
        }

        if(block.shouldSideBeRendered(this.blockAccess, x, y, z + 1, 3)) {
            f21 = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
            if(block.maxZ < 1.0D) {
                f21 = f20;
            }

            if(Block.lightValue[block.blockID] > 0) {
                f21 = 1.0F;
            }

            tessellator9.setColorOpaque_F(f13 * f21, f16 * f21, f19 * f21);
            this.renderWestFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 3));
            z10 = true;
        }

        if(block.shouldSideBeRendered(this.blockAccess, x - 1, y, z, 4)) {
            f21 = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
            if(block.minX > 0.0D) {
                f21 = f20;
            }

            if(Block.lightValue[block.blockID] > 0) {
                f21 = 1.0F;
            }

            tessellator9.setColorOpaque_F(f6 * f21, f7 * f21, f8 * f21);
            this.renderNorthFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 4));
            z10 = true;
        }

        if(block.shouldSideBeRendered(this.blockAccess, x + 1, y, z, 5)) {
            f21 = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
            if(block.maxX < 1.0D) {
                f21 = f20;
            }

            if(Block.lightValue[block.blockID] > 0) {
                f21 = 1.0F;
            }

            tessellator9.setColorOpaque_F(f6 * f21, f7 * f21, f8 * f21);
            this.renderSouthFace(block, (double)x, (double)y, (double)z, block.getBlockTexture(this.blockAccess, x, y, z, 5));
            z10 = true;
        }

        return z10;
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
			this.renderBlockCropsImpl(block, -1, -0.5D, -0.5D, -0.5D);
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