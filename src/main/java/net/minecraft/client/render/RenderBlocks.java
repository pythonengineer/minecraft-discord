package net.minecraft.client.render;

import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public final class RenderBlocks {
	private World blockAccess;
	private int overrideBlockTexture = -1;
	private boolean renderAllFaces = false;

	public RenderBlocks(World var1) {
		this.blockAccess = var1;
	}

	public RenderBlocks() {
	}

	public final void renderBlockUsingTexture(Block var1, int var2, int var3, int var4, int var5) {
		this.overrideBlockTexture = var5;
		this.renderBlockByRenderType(var1, var2, var3, var4);
		this.overrideBlockTexture = -1;
	}

	public final void renderBlockAllFaces(Block var1, int var2, int var3, int var4) {
		this.renderAllFaces = true;
		this.renderBlockByRenderType(var1, var2, var3, var4);
		this.renderAllFaces = false;
	}

    public final boolean renderBlockByRenderType(Block var1, int var2, int var3, int var4) {
        int var5 = var1.getRenderType();
        Tessellator var6;
        float var17;
        boolean var46;
        if(var5 == 0) {
            var6 = Tessellator.instance;
            var46 = false;
            if(this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3 - 1, var4, 0)) {
                var17 = var1.getBlockBrightness(this.blockAccess, var2, var3 - 1, var4);
                if(Block.lightValue[var1.blockID] > 0) {
                    var17 = 1.0F;
                }

                var6.setColorOpaque_F(0.5F * var17, 0.5F * var17, 0.5F * var17);
                this.renderBottomFace(var1, (double)var2, (double)var3, (double)var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 0));
                var46 = true;
            }

            if(this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3 + 1, var4, 1)) {
                var17 = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4);
                if(Block.lightValue[var1.blockID] > 0) {
                    var17 = 1.0F;
                }

                var6.setColorOpaque_F(var17 * 1.0F, var17 * 1.0F, var17 * 1.0F);
                this.renderTopFace(var1, (double)var2, (double)var3, (double)var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 1));
                var46 = true;
            }

            if(this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 - 1, 2)) {
                var17 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 - 1);
                if(Block.lightValue[var1.blockID] > 0) {
                    var17 = 1.0F;
                }

                var6.setColorOpaque_F(0.8F * var17, 0.8F * var17, 0.8F * var17);
                this.renderEastFace(var1, (double)var2, (double)var3, (double)var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 2));
                var46 = true;
            }

            if(this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 + 1, 3)) {
                var17 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 + 1);
                if(Block.lightValue[var1.blockID] > 0) {
                    var17 = 1.0F;
                }

                var6.setColorOpaque_F(0.8F * var17, 0.8F * var17, 0.8F * var17);
                this.renderWestFace(var1, (double)var2, (double)var3, (double)var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 3));
                var46 = true;
            }

            if(this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2 - 1, var3, var4, 4)) {
                var17 = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4);
                if(Block.lightValue[var1.blockID] > 0) {
                    var17 = 1.0F;
                }

                var6.setColorOpaque_F(0.6F * var17, 0.6F * var17, 0.6F * var17);
                this.renderNorthFace(var1, (double)var2, (double)var3, (double)var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 4));
                var46 = true;
            }

            if(this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2 + 1, var3, var4, 5)) {
                var17 = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4);
                if(Block.lightValue[var1.blockID] > 0) {
                    var17 = 1.0F;
                }

                var6.setColorOpaque_F(0.6F * var17, 0.6F * var17, 0.6F * var17);
                this.renderSouthFace(var1, (double)var2, (double)var3, (double)var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 5));
                var46 = true;
            }

            return var46;
        } else {
            float var21;
            if(var5 == 4) {
                var6 = Tessellator.instance;
                var46 = false;
                double var10 = var1.minY;
                double var11 = var1.maxY;
                var1.maxY = var11 - (double)this.materialNotWater(var2, var3, var4);
                if(this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3 - 1, var4, 0)) {
                    var21 = var1.getBlockBrightness(this.blockAccess, var2, var3 - 1, var4);
                    var6.setColorOpaque_F(0.5F * var21, 0.5F * var21, 0.5F * var21);
                    this.renderBottomFace(var1, (double)var2, (double)var3, (double)var4, var1.getBlockTextureFromSide(0));
                    var46 = true;
                }

                if(this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3 + 1, var4, 1)) {
                    var21 = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4);
                    var6.setColorOpaque_F(var21 * 1.0F, var21 * 1.0F, var21 * 1.0F);
                    this.renderTopFace(var1, (double)var2, (double)var3, (double)var4, var1.getBlockTextureFromSide(1));
                    var46 = true;
                }

                var1.minY = var11 - (double)this.materialNotWater(var2, var3, var4 - 1);
                if(this.renderAllFaces || var1.maxY > var1.minY || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 - 1, 2)) {
                    var21 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 - 1);
                    var6.setColorOpaque_F(0.8F * var21, 0.8F * var21, 0.8F * var21);
                    this.renderEastFace(var1, (double)var2, (double)var3, (double)var4, var1.getBlockTextureFromSide(2));
                    var46 = true;
                }

                var1.minY = var11 - (double)this.materialNotWater(var2, var3, var4 + 1);
                if(this.renderAllFaces || var1.maxY > var1.minY || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 + 1, 3)) {
                    var21 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 + 1);
                    var6.setColorOpaque_F(0.8F * var21, 0.8F * var21, 0.8F * var21);
                    this.renderWestFace(var1, (double)var2, (double)var3, (double)var4, var1.getBlockTextureFromSide(3));
                    var46 = true;
                }

                var1.minY = var11 - (double)this.materialNotWater(var2 - 1, var3, var4);
                if(this.renderAllFaces || var1.maxY > var1.minY || var1.shouldSideBeRendered(this.blockAccess, var2 - 1, var3, var4, 4)) {
                    var21 = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4);
                    var6.setColorOpaque_F(0.6F * var21, 0.6F * var21, 0.6F * var21);
                    this.renderNorthFace(var1, (double)var2, (double)var3, (double)var4, var1.getBlockTextureFromSide(4));
                    var46 = true;
                }

                var1.minY = var11 - (double)this.materialNotWater(var2 + 1, var3, var4);
                if(this.renderAllFaces || var1.maxY > var1.minY || var1.shouldSideBeRendered(this.blockAccess, var2 + 1, var3, var4, 5)) {
                    var21 = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4);
                    var6.setColorOpaque_F(0.6F * var21, 0.6F * var21, 0.6F * var21);
                    this.renderSouthFace(var1, (double)var2, (double)var3, (double)var4, var1.getBlockTextureFromSide(5));
                    var46 = true;
                }

                var1.minY = var10;
                var1.maxY = var11;
                return var46;
            } else {
                float var45;
                if(var5 == 1) {
                    var6 = Tessellator.instance;
                    var45 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
                    var6.setColorOpaque_F(var45, var45, var45);
                    this.renderCrossedSquares(var1, this.blockAccess.getBlockMetadata(var2, var3, var4), (float)var2, (float)var3, (float)var4);
                    return true;
                } else if(var5 == 6) {
                    var6 = Tessellator.instance;
                    var45 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
                    var6.setColorOpaque_F(var45, var45, var45);
                    this.renderBlockCropsImpl(var1, this.blockAccess.getBlockMetadata(var2, var3, var4), (float)var2, (float)var3 - 1.0F / 16.0F, (float)var4);
                    return true;
                } else {
                    float var8;
                    if(var5 == 2) {
                        int var42 = this.blockAccess.getBlockMetadata(var2, var3, var4);
                        Tessellator var24 = Tessellator.instance;
                        var8 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
                        if(Block.lightValue[var1.blockID] > 0) {
                            var8 = 1.0F;
                        }
    
                        var24.setColorOpaque_F(var8, var8, var8);
                        if(var42 == 1) {
                            this.renderTorchAtAngle(var1, (float)var2 - 10.0F * 0.01F, (float)var3 + 0.2F, (float)var4, -0.4F, 0.0F);
                        } else if(var42 == 2) {
                            this.renderTorchAtAngle(var1, (float)var2 + 10.0F * 0.01F, (float)var3 + 0.2F, (float)var4, 0.4F, 0.0F);
                        } else if(var42 == 3) {
                            this.renderTorchAtAngle(var1, (float)var2, (float)var3 + 0.2F, (float)var4 - 10.0F * 0.01F, 0.0F, -0.4F);
                        } else if(var42 == 4) {
                            this.renderTorchAtAngle(var1, (float)var2, (float)var3 + 0.2F, (float)var4 + 10.0F * 0.01F, 0.0F, 0.4F);
                        } else {
                            this.renderTorchAtAngle(var1, (float)var2, (float)var3, (float)var4, 0.0F, 0.0F);
                        }
    
                        return true;
                    } else {
                        int var7;
                        int var43;
                        if(var5 == 3) {
                            var5 = var4;
                            var4 = var3;
                            var3 = var2;
                            var6 = Tessellator.instance;
                            var7 = var1.getBlockTextureFromSide(0);
                            if(this.overrideBlockTexture >= 0) {
                                var7 = this.overrideBlockTexture;
                            }
    
                            var8 = var1.getBlockBrightness(this.blockAccess, var2, var4, var5);
                            var6.setColorOpaque_F(var8, var8, var8);
                            var2 = (var7 & 15) << 4;
                            var43 = var7 & 240;
                            double var47 = (double)((float)var2 / 256.0F);
                            double var49 = (double)(((float)var2 + 15.99F) / 256.0F);
                            double var51 = (double)((float)var43 / 256.0F);
                            double var52 = (double)(((float)var43 + 15.99F) / 256.0F);
                            double var27;
                            double var29;
                            double var31;
                            double var33;
                            if(!this.blockAccess.isBlockNormalCube(var3, var4 - 1, var5) && !Block.fire.canBlockCatchFire(this.blockAccess, var3, var4 - 1, var5)) {
                                if((var3 + var4 + var5 & 1) == 1) {
                                    var47 = (double)((float)var2 / 256.0F);
                                    var49 = (double)(((float)var2 + 15.99F) / 256.0F);
                                    var51 = (double)((float)(var43 + 16) / 256.0F);
                                    var52 = (double)(((float)var43 + 15.99F + 16.0F) / 256.0F);
                                }
    
                                if((var3 / 2 + var4 / 2 + var5 / 2 & 1) == 1) {
                                    var27 = var49;
                                    var49 = var47;
                                    var47 = var27;
                                }
    
                                if(Block.fire.canBlockCatchFire(this.blockAccess, var3 - 1, var4, var5)) {
                                    var6.addVertexWithUV((double)((float)var3 + 0.2F), (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)(var5 + 1), var49, var51);
                                    var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.0F / 16.0F), (double)(var5 + 1), var49, var52);
                                    var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.0F / 16.0F), (double)var5, var47, var52);
                                    var6.addVertexWithUV((double)((float)var3 + 0.2F), (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)var5, var47, var51);
                                    var6.addVertexWithUV((double)((float)var3 + 0.2F), (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)var5, var47, var51);
                                    var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.0F / 16.0F), (double)var5, var47, var52);
                                    var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.0F / 16.0F), (double)(var5 + 1), var49, var52);
                                    var6.addVertexWithUV((double)((float)var3 + 0.2F), (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)(var5 + 1), var49, var51);
                                }
    
                                if(Block.fire.canBlockCatchFire(this.blockAccess, var3 + 1, var4, var5)) {
                                    var6.addVertexWithUV((double)((float)(var3 + 1) - 0.2F), (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)var5, var47, var51);
                                    var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.0F / 16.0F), (double)var5, var47, var52);
                                    var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.0F / 16.0F), (double)(var5 + 1), var49, var52);
                                    var6.addVertexWithUV((double)((float)(var3 + 1) - 0.2F), (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)(var5 + 1), var49, var51);
                                    var6.addVertexWithUV((double)((float)(var3 + 1) - 0.2F), (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)(var5 + 1), var49, var51);
                                    var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.0F / 16.0F), (double)(var5 + 1), var49, var52);
                                    var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.0F / 16.0F), (double)var5, var47, var52);
                                    var6.addVertexWithUV((double)((float)(var3 + 1) - 0.2F), (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)var5, var47, var51);
                                }
    
                                if(Block.fire.canBlockCatchFire(this.blockAccess, var3, var4, var5 - 1)) {
                                    var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)((float)var5 + 0.2F), var49, var51);
                                    var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.0F / 16.0F), (double)var5, var49, var52);
                                    var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.0F / 16.0F), (double)var5, var47, var52);
                                    var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)((float)var5 + 0.2F), var47, var51);
                                    var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)((float)var5 + 0.2F), var47, var51);
                                    var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.0F / 16.0F), (double)var5, var47, var52);
                                    var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.0F / 16.0F), (double)var5, var49, var52);
                                    var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)((float)var5 + 0.2F), var49, var51);
                                }
    
                                if(Block.fire.canBlockCatchFire(this.blockAccess, var3, var4, var5 + 1)) {
                                    var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)((float)(var5 + 1) - 0.2F), var47, var51);
                                    var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.0F / 16.0F), (double)(var5 + 1), var47, var52);
                                    var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.0F / 16.0F), (double)(var5 + 1), var49, var52);
                                    var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)((float)(var5 + 1) - 0.2F), var49, var51);
                                    var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)((float)(var5 + 1) - 0.2F), var49, var51);
                                    var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.0F / 16.0F), (double)(var5 + 1), var49, var52);
                                    var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.0F / 16.0F), (double)(var5 + 1), var47, var52);
                                    var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.4F + 1.0F / 16.0F), (double)((float)(var5 + 1) - 0.2F), var47, var51);
                                }
    
                                if(Block.fire.canBlockCatchFire(this.blockAccess, var3, var4 + 1, var5)) {
                                    var27 = (double)var3 + 0.5D + 0.5D;
                                    var29 = (double)var3 + 0.5D - 0.5D;
                                    var31 = (double)var5 + 0.5D + 0.5D;
                                    var33 = (double)var5 + 0.5D - 0.5D;
                                    var47 = (double)((float)var2 / 256.0F);
                                    var49 = (double)(((float)var2 + 15.99F) / 256.0F);
                                    var51 = (double)((float)var43 / 256.0F);
                                    var52 = (double)(((float)var43 + 15.99F) / 256.0F);
                                    ++var4;
                                    if((var3 + var4 + var5 & 1) == 0) {
                                        var6.addVertexWithUV(var29, (double)((float)var4 + -0.2F), (double)var5, var49, var51);
                                        var6.addVertexWithUV(var27, (double)var4, (double)var5, var49, var52);
                                        var6.addVertexWithUV(var27, (double)var4, (double)(var5 + 1), var47, var52);
                                        var6.addVertexWithUV(var29, (double)((float)var4 + -0.2F), (double)(var5 + 1), var47, var51);
                                        var47 = (double)((float)var2 / 256.0F);
                                        var49 = (double)(((float)var2 + 15.99F) / 256.0F);
                                        var51 = (double)((float)(var43 + 16) / 256.0F);
                                        var52 = (double)(((float)var43 + 15.99F + 16.0F) / 256.0F);
                                        var6.addVertexWithUV(var27, (double)((float)var4 + -0.2F), (double)(var5 + 1), var49, var51);
                                        var6.addVertexWithUV(var29, (double)var4, (double)(var5 + 1), var49, var52);
                                        var6.addVertexWithUV(var29, (double)var4, (double)var5, var47, var52);
                                        var6.addVertexWithUV(var27, (double)((float)var4 + -0.2F), (double)var5, var47, var51);
                                    } else {
                                        var6.addVertexWithUV((double)var3, (double)((float)var4 + -0.2F), var31, var49, var51);
                                        var6.addVertexWithUV((double)var3, (double)var4, var33, var49, var52);
                                        var6.addVertexWithUV((double)(var3 + 1), (double)var4, var33, var47, var52);
                                        var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + -0.2F), var31, var47, var51);
                                        var47 = (double)((float)var2 / 256.0F);
                                        var49 = (double)(((float)var2 + 15.99F) / 256.0F);
                                        var51 = (double)((float)(var43 + 16) / 256.0F);
                                        var52 = (double)(((float)var43 + 15.99F + 16.0F) / 256.0F);
                                        var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + -0.2F), var33, var49, var51);
                                        var6.addVertexWithUV((double)(var3 + 1), (double)var4, var31, var49, var52);
                                        var6.addVertexWithUV((double)var3, (double)var4, var31, var47, var52);
                                        var6.addVertexWithUV((double)var3, (double)((float)var4 + -0.2F), var33, var47, var51);
                                    }
                                }
                            } else {
                                double var25 = (double)var3 + 0.5D + 0.2D;
                                var27 = (double)var3 + 0.5D - 0.2D;
                                var29 = (double)var5 + 0.5D + 0.2D;
                                var31 = (double)var5 + 0.5D - 0.2D;
                                var33 = (double)var3 + 0.5D - 0.3D;
                                double var35 = (double)var3 + 0.5D + 0.3D;
                                double var37 = (double)var5 + 0.5D - 0.3D;
                                double var39 = (double)var5 + 0.5D + 0.3D;
                                var6.addVertexWithUV(var33, (double)((float)var4 + 1.4F), (double)(var5 + 1), var49, var51);
                                var6.addVertexWithUV(var25, (double)var4, (double)(var5 + 1), var49, var52);
                                var6.addVertexWithUV(var25, (double)var4, (double)var5, var47, var52);
                                var6.addVertexWithUV(var33, (double)((float)var4 + 1.4F), (double)var5, var47, var51);
                                var6.addVertexWithUV(var35, (double)((float)var4 + 1.4F), (double)var5, var49, var51);
                                var6.addVertexWithUV(var27, (double)var4, (double)var5, var49, var52);
                                var6.addVertexWithUV(var27, (double)var4, (double)(var5 + 1), var47, var52);
                                var6.addVertexWithUV(var35, (double)((float)var4 + 1.4F), (double)(var5 + 1), var47, var51);
                                var47 = (double)((float)var2 / 256.0F);
                                var49 = (double)(((float)var2 + 15.99F) / 256.0F);
                                var51 = (double)((float)(var43 + 16) / 256.0F);
                                var52 = (double)(((float)var43 + 15.99F + 16.0F) / 256.0F);
                                var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.4F), var39, var49, var51);
                                var6.addVertexWithUV((double)(var3 + 1), (double)var4, var31, var49, var52);
                                var6.addVertexWithUV((double)var3, (double)var4, var31, var47, var52);
                                var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.4F), var39, var47, var51);
                                var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.4F), var37, var49, var51);
                                var6.addVertexWithUV((double)var3, (double)var4, var29, var49, var52);
                                var6.addVertexWithUV((double)(var3 + 1), (double)var4, var29, var47, var52);
                                var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.4F), var37, var47, var51);
                                var25 = (double)var3 + 0.5D - 0.5D;
                                var27 = (double)var3 + 0.5D + 0.5D;
                                var29 = (double)var5 + 0.5D - 0.5D;
                                var31 = (double)var5 + 0.5D + 0.5D;
                                var33 = (double)var3 + 0.5D - 0.4D;
                                var35 = (double)var3 + 0.5D + 0.4D;
                                var37 = (double)var5 + 0.5D - 0.4D;
                                var39 = (double)var5 + 0.5D + 0.4D;
                                var6.addVertexWithUV(var33, (double)((float)var4 + 1.4F), (double)var5, var47, var51);
                                var6.addVertexWithUV(var25, (double)var4, (double)var5, var47, var52);
                                var6.addVertexWithUV(var25, (double)var4, (double)(var5 + 1), var49, var52);
                                var6.addVertexWithUV(var33, (double)((float)var4 + 1.4F), (double)(var5 + 1), var49, var51);
                                var6.addVertexWithUV(var35, (double)((float)var4 + 1.4F), (double)(var5 + 1), var47, var51);
                                var6.addVertexWithUV(var27, (double)var4, (double)(var5 + 1), var47, var52);
                                var6.addVertexWithUV(var27, (double)var4, (double)var5, var49, var52);
                                var6.addVertexWithUV(var35, (double)((float)var4 + 1.4F), (double)var5, var49, var51);
                                var47 = (double)((float)var2 / 256.0F);
                                var49 = (double)(((float)var2 + 15.99F) / 256.0F);
                                var51 = (double)((float)var43 / 256.0F);
                                var52 = (double)(((float)var43 + 15.99F) / 256.0F);
                                var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.4F), var39, var47, var51);
                                var6.addVertexWithUV((double)var3, (double)var4, var31, var47, var52);
                                var6.addVertexWithUV((double)(var3 + 1), (double)var4, var31, var49, var52);
                                var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.4F), var39, var49, var51);
                                var6.addVertexWithUV((double)(var3 + 1), (double)((float)var4 + 1.4F), var37, var47, var51);
                                var6.addVertexWithUV((double)(var3 + 1), (double)var4, var29, var47, var52);
                                var6.addVertexWithUV((double)var3, (double)var4, var29, var49, var52);
                                var6.addVertexWithUV((double)var3, (double)((float)var4 + 1.4F), var37, var49, var51);
                            }
    
                            return true;
                        } else if(var5 == 5) {
                            var5 = var4;
                            var4 = var3;
                            var3 = var2;
                            var6 = Tessellator.instance;
                            var7 = var1.getBlockTextureFromSide(0);
                            if(this.overrideBlockTexture >= 0) {
                                var7 = this.overrideBlockTexture;
                            }
    
                            var8 = var1.getBlockBrightness(this.blockAccess, var2, var4, var5);
                            var6.setColorOpaque_F(var8, var8, var8);
                            var2 = ((var7 & 15) << 4) + 16;
                            var43 = (var7 & 15) << 4;
                            int var16 = var7 & 240;
                            if((var3 + var4 + var5 & 1) == 1) {
                                var2 = (var7 & 15) << 4;
                                var43 = ((var7 & 15) << 4) + 16;
                            }
    
                            var17 = (float)var2 / 256.0F;
                            float var18 = ((float)var2 + 15.99F) / 256.0F;
                            float var19 = (float)var16 / 256.0F;
                            float var20 = ((float)var16 + 15.99F) / 256.0F;
                            var21 = (float)var43 / 256.0F;
                            float var22 = ((float)var43 + 15.99F) / 256.0F;
                            float var23 = (float)var16 / 256.0F;
                            float var41 = ((float)var16 + 15.99F) / 256.0F;
                            if(this.blockAccess.isBlockNormalCube(var3 - 1, var4, var5)) {
                                var6.addVertexWithUV((double)((float)var3 + 0.05F), (double)((float)(var4 + 1) + 2.0F / 16.0F), (double)((float)(var5 + 1) + 2.0F / 16.0F), (double)var17, (double)var19);
                                var6.addVertexWithUV((double)((float)var3 + 0.05F), (double)((float)var4 - 2.0F / 16.0F), (double)((float)(var5 + 1) + 2.0F / 16.0F), (double)var17, (double)var20);
                                var6.addVertexWithUV((double)((float)var3 + 0.05F), (double)((float)var4 - 2.0F / 16.0F), (double)((float)var5 - 2.0F / 16.0F), (double)var18, (double)var20);
                                var6.addVertexWithUV((double)((float)var3 + 0.05F), (double)((float)(var4 + 1) + 2.0F / 16.0F), (double)((float)var5 - 2.0F / 16.0F), (double)var18, (double)var19);
                            }
    
                            if(this.blockAccess.isBlockNormalCube(var3 + 1, var4, var5)) {
                                var6.addVertexWithUV((double)((float)(var3 + 1) - 0.05F), (double)((float)var4 - 2.0F / 16.0F), (double)((float)(var5 + 1) + 2.0F / 16.0F), (double)var18, (double)var20);
                                var6.addVertexWithUV((double)((float)(var3 + 1) - 0.05F), (double)((float)(var4 + 1) + 2.0F / 16.0F), (double)((float)(var5 + 1) + 2.0F / 16.0F), (double)var18, (double)var19);
                                var6.addVertexWithUV((double)((float)(var3 + 1) - 0.05F), (double)((float)(var4 + 1) + 2.0F / 16.0F), (double)((float)var5 - 2.0F / 16.0F), (double)var17, (double)var19);
                                var6.addVertexWithUV((double)((float)(var3 + 1) - 0.05F), (double)((float)var4 - 2.0F / 16.0F), (double)((float)var5 - 2.0F / 16.0F), (double)var17, (double)var20);
                            }
    
                            if(this.blockAccess.isBlockNormalCube(var3, var4, var5 - 1)) {
                                var6.addVertexWithUV((double)((float)(var3 + 1) + 2.0F / 16.0F), (double)((float)var4 - 2.0F / 16.0F), (double)((float)var5 + 0.05F), (double)var22, (double)var41);
                                var6.addVertexWithUV((double)((float)(var3 + 1) + 2.0F / 16.0F), (double)((float)(var4 + 1) + 2.0F / 16.0F), (double)((float)var5 + 0.05F), (double)var22, (double)var23);
                                var6.addVertexWithUV((double)((float)var3 - 2.0F / 16.0F), (double)((float)(var4 + 1) + 2.0F / 16.0F), (double)((float)var5 + 0.05F), (double)var21, (double)var23);
                                var6.addVertexWithUV((double)((float)var3 - 2.0F / 16.0F), (double)((float)var4 - 2.0F / 16.0F), (double)((float)var5 + 0.05F), (double)var21, (double)var41);
                            }
    
                            if(this.blockAccess.isBlockNormalCube(var3, var4, var5 + 1)) {
                                var6.addVertexWithUV((double)((float)(var3 + 1) + 2.0F / 16.0F), (double)((float)(var4 + 1) + 2.0F / 16.0F), (double)((float)(var5 + 1) - 0.05F), (double)var21, (double)var23);
                                var6.addVertexWithUV((double)((float)(var3 + 1) + 2.0F / 16.0F), (double)((float)var4 - 2.0F / 16.0F), (double)((float)(var5 + 1) - 0.05F), (double)var21, (double)var41);
                                var6.addVertexWithUV((double)((float)var3 - 2.0F / 16.0F), (double)((float)var4 - 2.0F / 16.0F), (double)((float)(var5 + 1) - 0.05F), (double)var22, (double)var41);
                                var6.addVertexWithUV((double)((float)var3 - 2.0F / 16.0F), (double)((float)(var4 + 1) + 2.0F / 16.0F), (double)((float)(var5 + 1) - 0.05F), (double)var22, (double)var23);
                            }
    
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
    }

	private void renderTorchAtAngle(Block var1, float var2, float var3, float var4, float var5, float var6) {
		Tessellator var7 = Tessellator.instance;
		int var19 = var1.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			var19 = this.overrideBlockTexture;
		}

		int var8 = (var19 & 15) << 4;
		var19 &= 240;
		float var9 = (float)var8 / 256.0F;
		float var21 = ((float)var8 + 15.99F) / 256.0F;
		float var10 = (float)var19 / 256.0F;
		float var20 = ((float)var19 + 15.99F) / 256.0F;
		float var11 = var9 + 0.02734375F;
		float var12 = var10 + 0.0234375F;
		float var13 = var9 + 0.03515625F;
		float var14 = var10 + 0.03125F;
		var2 += 0.5F;
		var4 += 0.5F;
		float var15 = var2 - 0.5F;
		float var16 = var2 + 0.5F;
		float var17 = var4 - 0.5F;
		float var18 = var4 + 0.5F;
        var7.addVertexWithUV((double)(var2 + var5 * (6.0F / 16.0F) - 1.0F / 16.0F), (double)(var3 + 10.0F / 16.0F), (double)(var4 + var6 * (6.0F / 16.0F) - 1.0F / 16.0F), (double)var11, (double)var12);
        var7.addVertexWithUV((double)(var2 + var5 * (6.0F / 16.0F) - 1.0F / 16.0F), (double)(var3 + 10.0F / 16.0F), (double)(var4 + var6 * (6.0F / 16.0F) + 1.0F / 16.0F), (double)var11, (double)var14);
        var7.addVertexWithUV((double)(var2 + var5 * (6.0F / 16.0F) + 1.0F / 16.0F), (double)(var3 + 10.0F / 16.0F), (double)(var4 + var6 * (6.0F / 16.0F) + 1.0F / 16.0F), (double)var13, (double)var14);
        var7.addVertexWithUV((double)(var2 + var5 * (6.0F / 16.0F) + 1.0F / 16.0F), (double)(var3 + 10.0F / 16.0F), (double)(var4 + var6 * (6.0F / 16.0F) - 1.0F / 16.0F), (double)var13, (double)var12);
        var7.addVertexWithUV((double)(var2 - 1.0F / 16.0F), (double)(var3 + 1.0F), (double)var17, (double)var9, (double)var10);
        var7.addVertexWithUV((double)(var2 - 1.0F / 16.0F + var5), (double)var3, (double)(var17 + var6), (double)var9, (double)var20);
        var7.addVertexWithUV((double)(var2 - 1.0F / 16.0F + var5), (double)var3, (double)(var18 + var6), (double)var21, (double)var20);
        var7.addVertexWithUV((double)(var2 - 1.0F / 16.0F), (double)(var3 + 1.0F), (double)var18, (double)var21, (double)var10);
        var7.addVertexWithUV((double)(var2 + 1.0F / 16.0F), (double)(var3 + 1.0F), (double)var18, (double)var9, (double)var10);
        var7.addVertexWithUV((double)(var2 + var5 + 1.0F / 16.0F), (double)var3, (double)(var18 + var6), (double)var9, (double)var20);
        var7.addVertexWithUV((double)(var2 + var5 + 1.0F / 16.0F), (double)var3, (double)(var17 + var6), (double)var21, (double)var20);
        var7.addVertexWithUV((double)(var2 + 1.0F / 16.0F), (double)(var3 + 1.0F), (double)var17, (double)var21, (double)var10);
        var7.addVertexWithUV((double)var15, (double)(var3 + 1.0F), (double)(var4 + 1.0F / 16.0F), (double)var9, (double)var10);
        var7.addVertexWithUV((double)(var15 + var5), (double)var3, (double)(var4 + 1.0F / 16.0F + var6), (double)var9, (double)var20);
        var7.addVertexWithUV((double)(var16 + var5), (double)var3, (double)(var4 + 1.0F / 16.0F + var6), (double)var21, (double)var20);
        var7.addVertexWithUV((double)var16, (double)(var3 + 1.0F), (double)(var4 + 1.0F / 16.0F), (double)var21, (double)var10);
        var7.addVertexWithUV((double)var16, (double)(var3 + 1.0F), (double)(var4 - 1.0F / 16.0F), (double)var9, (double)var10);
        var7.addVertexWithUV((double)(var16 + var5), (double)var3, (double)(var4 - 1.0F / 16.0F + var6), (double)var9, (double)var20);
        var7.addVertexWithUV((double)(var15 + var5), (double)var3, (double)(var4 - 1.0F / 16.0F + var6), (double)var21, (double)var20);
        var7.addVertexWithUV((double)var15, (double)(var3 + 1.0F), (double)(var4 - 1.0F / 16.0F), (double)var21, (double)var10);
    }

	private void renderCrossedSquares(Block var1, int var2, float var3, float var4, float var5) {
		Tessellator var6 = Tessellator.instance;
		int var11 = var1.getBlockTextureFromSideAndMetadata(0, var2);
		if(this.overrideBlockTexture >= 0) {
			var11 = this.overrideBlockTexture;
		}

		var2 = (var11 & 15) << 4;
		var11 &= 240;
		float var7 = (float)var2 / 256.0F;
		float var12 = ((float)var2 + 15.99F) / 256.0F;
		float var8 = (float)var11 / 256.0F;
		float var13 = ((float)var11 + 15.99F) / 256.0F;
		float var9 = var3 + 0.5F - 0.45F;
		var3 = var3 + 0.5F + 0.45F;
		float var10 = var5 + 0.5F - 0.45F;
		var5 = var5 + 0.5F + 0.45F;
        var6.addVertexWithUV((double)var9, (double)(var4 + 1.0F), (double)var10, (double)var7, (double)var8);
        var6.addVertexWithUV((double)var9, (double)var4, (double)var10, (double)var7, (double)var13);
        var6.addVertexWithUV((double)var3, (double)var4, (double)var5, (double)var12, (double)var13);
        var6.addVertexWithUV((double)var3, (double)(var4 + 1.0F), (double)var5, (double)var12, (double)var8);
        var6.addVertexWithUV((double)var3, (double)(var4 + 1.0F), (double)var5, (double)var7, (double)var8);
        var6.addVertexWithUV((double)var3, (double)var4, (double)var5, (double)var7, (double)var13);
        var6.addVertexWithUV((double)var9, (double)var4, (double)var10, (double)var12, (double)var13);
        var6.addVertexWithUV((double)var9, (double)(var4 + 1.0F), (double)var10, (double)var12, (double)var8);
        var6.addVertexWithUV((double)var9, (double)(var4 + 1.0F), (double)var5, (double)var7, (double)var8);
        var6.addVertexWithUV((double)var9, (double)var4, (double)var5, (double)var7, (double)var13);
        var6.addVertexWithUV((double)var3, (double)var4, (double)var10, (double)var12, (double)var13);
        var6.addVertexWithUV((double)var3, (double)(var4 + 1.0F), (double)var10, (double)var12, (double)var8);
        var6.addVertexWithUV((double)var3, (double)(var4 + 1.0F), (double)var10, (double)var7, (double)var8);
        var6.addVertexWithUV((double)var3, (double)var4, (double)var10, (double)var7, (double)var13);
        var6.addVertexWithUV((double)var9, (double)var4, (double)var5, (double)var12, (double)var13);
        var6.addVertexWithUV((double)var9, (double)(var4 + 1.0F), (double)var5, (double)var12, (double)var8);
    }

	private void renderBlockCropsImpl(Block var1, int var2, float var3, float var4, float var5) {
		Tessellator var6 = Tessellator.instance;
		int var13 = var1.getBlockTextureFromSideAndMetadata(0, var2);
		if(this.overrideBlockTexture >= 0) {
			var13 = this.overrideBlockTexture;
		}

		var2 = (var13 & 15) << 4;
		var13 &= 240;
		float var7 = (float)var2 / 256.0F;
		float var14 = ((float)var2 + 15.99F) / 256.0F;
		float var8 = (float)var13 / 256.0F;
		float var15 = ((float)var13 + 15.99F) / 256.0F;
		float var9 = var3 + 0.5F - 0.25F;
		float var10 = var3 + 0.5F + 0.25F;
		float var11 = var5 + 0.5F - 0.5F;
		float var12 = var5 + 0.5F + 0.5F;
        var6.addVertexWithUV((double)var9, (double)(var4 + 1.0F), (double)var11, (double)var7, (double)var8);
        var6.addVertexWithUV((double)var9, (double)var4, (double)var11, (double)var7, (double)var15);
        var6.addVertexWithUV((double)var9, (double)var4, (double)var12, (double)var14, (double)var15);
        var6.addVertexWithUV((double)var9, (double)(var4 + 1.0F), (double)var12, (double)var14, (double)var8);
        var6.addVertexWithUV((double)var9, (double)(var4 + 1.0F), (double)var12, (double)var7, (double)var8);
        var6.addVertexWithUV((double)var9, (double)var4, (double)var12, (double)var7, (double)var15);
        var6.addVertexWithUV((double)var9, (double)var4, (double)var11, (double)var14, (double)var15);
        var6.addVertexWithUV((double)var9, (double)(var4 + 1.0F), (double)var11, (double)var14, (double)var8);
        var6.addVertexWithUV((double)var10, (double)(var4 + 1.0F), (double)var12, (double)var7, (double)var8);
        var6.addVertexWithUV((double)var10, (double)var4, (double)var12, (double)var7, (double)var15);
        var6.addVertexWithUV((double)var10, (double)var4, (double)var11, (double)var14, (double)var15);
        var6.addVertexWithUV((double)var10, (double)(var4 + 1.0F), (double)var11, (double)var14, (double)var8);
        var6.addVertexWithUV((double)var10, (double)(var4 + 1.0F), (double)var11, (double)var7, (double)var8);
        var6.addVertexWithUV((double)var10, (double)var4, (double)var11, (double)var7, (double)var15);
        var6.addVertexWithUV((double)var10, (double)var4, (double)var12, (double)var14, (double)var15);
        var6.addVertexWithUV((double)var10, (double)(var4 + 1.0F), (double)var12, (double)var14, (double)var8);
        var9 = var3 + 0.5F - 0.5F;
        var10 = var3 + 0.5F + 0.5F;
        var11 = var5 + 0.5F - 0.25F;
        var12 = var5 + 0.5F + 0.25F;
        var6.addVertexWithUV((double)var9, (double)(var4 + 1.0F), (double)var11, (double)var7, (double)var8);
        var6.addVertexWithUV((double)var9, (double)var4, (double)var11, (double)var7, (double)var15);
        var6.addVertexWithUV((double)var10, (double)var4, (double)var11, (double)var14, (double)var15);
        var6.addVertexWithUV((double)var10, (double)(var4 + 1.0F), (double)var11, (double)var14, (double)var8);
        var6.addVertexWithUV((double)var10, (double)(var4 + 1.0F), (double)var11, (double)var7, (double)var8);
        var6.addVertexWithUV((double)var10, (double)var4, (double)var11, (double)var7, (double)var15);
        var6.addVertexWithUV((double)var9, (double)var4, (double)var11, (double)var14, (double)var15);
        var6.addVertexWithUV((double)var9, (double)(var4 + 1.0F), (double)var11, (double)var14, (double)var8);
        var6.addVertexWithUV((double)var10, (double)(var4 + 1.0F), (double)var12, (double)var7, (double)var8);
        var6.addVertexWithUV((double)var10, (double)var4, (double)var12, (double)var7, (double)var15);
        var6.addVertexWithUV((double)var9, (double)var4, (double)var12, (double)var14, (double)var15);
        var6.addVertexWithUV((double)var9, (double)(var4 + 1.0F), (double)var12, (double)var14, (double)var8);
        var6.addVertexWithUV((double)var9, (double)(var4 + 1.0F), (double)var12, (double)var7, (double)var8);
        var6.addVertexWithUV((double)var9, (double)var4, (double)var12, (double)var7, (double)var15);
        var6.addVertexWithUV((double)var10, (double)var4, (double)var12, (double)var14, (double)var15);
        var6.addVertexWithUV((double)var10, (double)(var4 + 1.0F), (double)var12, (double)var14, (double)var8);
    }

    private float materialNotWater(int var1, int var2, int var3) {
        return this.blockAccess.getBlockMaterial(var1, var2, var3) != Material.water ? 1.0F : (float)this.blockAccess.getBlockMetadata(var1, var2, var3) / 9.0F;
    }

    private void renderBottomFace(Block var1, double var2, double var4, double var6, int var8) {
        Tessellator var9 = Tessellator.instance;
        if(this.overrideBlockTexture >= 0) {
            var8 = this.overrideBlockTexture;
        }

        int var10 = (var8 & 15) << 4;
        var8 &= 240;
        double var12 = (double)((float)var10 / 256.0F);
        double var14 = (double)(((float)var10 + 15.99F) / 256.0F);
        double var16 = (double)((float)var8 / 256.0F);
        double var18 = (double)(((float)var8 + 15.99F) / 256.0F);
        double var20 = var2 + var1.minX;
        double var22 = var2 + var1.maxX;
        double var24 = var4 + var1.minY;
        double var26 = var6 + var1.minZ;
        double var28 = var6 + var1.maxZ;
        var9.addVertexWithUV(var20, var24, var28, var12, var18);
        var9.addVertexWithUV(var20, var24, var26, var12, var16);
        var9.addVertexWithUV(var22, var24, var26, var14, var16);
        var9.addVertexWithUV(var22, var24, var28, var14, var18);
    }

	private void renderTopFace(Block var1, double var2, double var4, double var6, int var8) {
        Tessellator var9 = Tessellator.instance;
        if(this.overrideBlockTexture >= 0) {
            var8 = this.overrideBlockTexture;
        }

        int var10 = (var8 & 15) << 4;
        var8 &= 240;
        double var12 = (double)((float)var10 / 256.0F);
        double var14 = (double)(((float)var10 + 15.99F) / 256.0F);
        double var16 = (double)((float)var8 / 256.0F);
        double var18 = (double)(((float)var8 + 15.99F) / 256.0F);
        double var20 = var2 + var1.minX;
        double var22 = var2 + var1.maxX;
        double var24 = var4 + var1.maxY;
        double var26 = var6 + var1.minZ;
        double var28 = var6 + var1.maxZ;
        var9.addVertexWithUV(var22, var24, var28, var14, var18);
        var9.addVertexWithUV(var22, var24, var26, var14, var16);
        var9.addVertexWithUV(var20, var24, var26, var12, var16);
        var9.addVertexWithUV(var20, var24, var28, var12, var18);
    }

	private void renderEastFace(Block var1, double var2, double var4, double var6, int var8) {
        Tessellator var9 = Tessellator.instance;
        if(this.overrideBlockTexture >= 0) {
            var8 = this.overrideBlockTexture;
        }

        int var10 = (var8 & 15) << 4;
        var8 &= 240;
        double var12 = (double)((float)var10 / 256.0F);
        double var14 = ((double)var10 + 15.99D) / 256.0D;
        double var16;
        double var18;
        if(var1.minY >= 0.0D && var1.maxY <= 1.0D) {
            var16 = ((double)var8 + var1.minY * (double)15.99F) / 256.0D;
            var18 = ((double)var8 + var1.maxY * (double)15.99F) / 256.0D;
        } else {
            var16 = (double)((float)var8 / 256.0F);
            var18 = (double)(((float)var8 + 15.99F) / 256.0F);
        }

        double var20 = var2 + var1.minX;
        double var22 = var2 + var1.maxX;
        double var24 = var4 + var1.minY;
        double var26 = var4 + var1.maxY;
        double var28 = var6 + var1.minZ;
        var9.addVertexWithUV(var20, var26, var28, var14, var16);
        var9.addVertexWithUV(var22, var26, var28, var12, var16);
        var9.addVertexWithUV(var22, var24, var28, var12, var18);
        var9.addVertexWithUV(var20, var24, var28, var14, var18);
    }

	private void renderWestFace(Block var1, double var2, double var4, double var6, int var8) {
        Tessellator var9 = Tessellator.instance;
        if(this.overrideBlockTexture >= 0) {
            var8 = this.overrideBlockTexture;
        }

        int var10 = (var8 & 15) << 4;
        var8 &= 240;
        double var12 = (double)((float)var10 / 256.0F);
        double var14 = (double)(((float)var10 + 15.99F) / 256.0F);
        double var16;
        double var18;
        if(var1.minY >= 0.0D && var1.maxY <= 1.0D) {
            var16 = ((double)var8 + var1.minY * (double)15.99F) / 256.0D;
            var18 = ((double)var8 + var1.maxY * (double)15.99F) / 256.0D;
        } else {
            var16 = (double)((float)var8 / 256.0F);
            var18 = (double)(((float)var8 + 15.99F) / 256.0F);
        }

        double var20 = var2 + var1.minX;
        double var22 = var2 + var1.maxX;
        double var24 = var4 + var1.minY;
        double var26 = var4 + var1.maxY;
        double var28 = var6 + var1.maxZ;
        var9.addVertexWithUV(var20, var26, var28, var12, var16);
        var9.addVertexWithUV(var20, var24, var28, var12, var18);
        var9.addVertexWithUV(var22, var24, var28, var14, var18);
        var9.addVertexWithUV(var22, var26, var28, var14, var16);
    }

	private void renderNorthFace(Block var1, double var2, double var4, double var6, int var8) {
        Tessellator var9 = Tessellator.instance;
        if(this.overrideBlockTexture >= 0) {
            var8 = this.overrideBlockTexture;
        }

        int var10 = (var8 & 15) << 4;
        var8 &= 240;
        double var12 = (double)((float)var10 / 256.0F);
        double var14 = (double)(((float)var10 + 15.99F) / 256.0F);
        double var16;
        double var18;
        if(var1.minY >= 0.0D && var1.maxY <= 1.0D) {
            var16 = ((double)var8 + var1.minY * (double)15.99F) / 256.0D;
            var18 = ((double)var8 + var1.maxY * (double)15.99F) / 256.0D;
        } else {
            var16 = (double)((float)var8 / 256.0F);
            var18 = (double)(((float)var8 + 15.99F) / 256.0F);
        }

        double var20 = var2 + var1.minX;
        double var22 = var4 + var1.minY;
        double var24 = var4 + var1.maxY;
        double var26 = var6 + var1.minZ;
        double var28 = var6 + var1.maxZ;
        var9.addVertexWithUV(var20, var24, var28, var14, var16);
        var9.addVertexWithUV(var20, var24, var26, var12, var16);
        var9.addVertexWithUV(var20, var22, var26, var12, var18);
        var9.addVertexWithUV(var20, var22, var28, var14, var18);
    }

	private void renderSouthFace(Block var1, double var2, double var4, double var6, int var8) {
        Tessellator var9 = Tessellator.instance;
        if(this.overrideBlockTexture >= 0) {
            var8 = this.overrideBlockTexture;
        }

        int var10 = (var8 & 15) << 4;
        var8 &= 240;
        double var12 = (double)((float)var10 / 256.0F);
        double var14 = (double)(((float)var10 + 15.99F) / 256.0F);
        double var16;
        double var18;
        if(var1.minY >= 0.0D && var1.maxY <= 1.0D) {
            var16 = ((double)var8 + var1.minY * (double)15.99F) / 256.0D;
            var18 = ((double)var8 + var1.maxY * (double)15.99F) / 256.0D;
        } else {
            var16 = (double)((float)var8 / 256.0F);
            var18 = (double)(((float)var8 + 15.99F) / 256.0F);
        }

        double var20 = var2 + var1.maxX;
        double var22 = var4 + var1.minY;
        double var24 = var4 + var1.maxY;
        double var26 = var6 + var1.minZ;
        double var28 = var6 + var1.maxZ;
        var9.addVertexWithUV(var20, var22, var28, var12, var18);
        var9.addVertexWithUV(var20, var22, var26, var14, var18);
        var9.addVertexWithUV(var20, var24, var26, var14, var16);
        var9.addVertexWithUV(var20, var24, var28, var12, var16);
    }

	public final void renderBlockOnInventory(Block var1) {
		Tessellator var2 = Tessellator.instance;
		int var3 = var1.getRenderType();
		if(var3 == 0) {
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			var2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			var2.normal(0.0F, -1.0F, 0.0F);
			this.renderBottomFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSide(0));
			var2.draw();
			var2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			var2.normal(0.0F, 1.0F, 0.0F);
			this.renderTopFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSide(1));
			var2.draw();
			var2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			var2.normal(0.0F, 0.0F, -1.0F);
			this.renderEastFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSide(2));
			var2.draw();
			var2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			var2.normal(0.0F, 0.0F, 1.0F);
			this.renderWestFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSide(3));
			var2.draw();
			var2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			var2.normal(-1.0F, 0.0F, 0.0F);
			this.renderNorthFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSide(4));
			var2.draw();
			var2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			var2.normal(1.0F, 0.0F, 0.0F);
			this.renderSouthFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSide(5));
			var2.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		} else if(var3 == 1) {
			var2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			var2.normal(0.0F, -1.0F, 0.0F);
			this.renderCrossedSquares(var1, -1, -0.5F, -0.5F, -0.5F);
			var2.draw();
		} else if(var3 == 6) {
			var2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			var2.normal(0.0F, -1.0F, 0.0F);
			this.renderBlockCropsImpl(var1, -1, -0.5F, -0.5F, -0.5F);
			var2.draw();
		} else {
			if(var3 == 2) {
				var2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
				var2.normal(0.0F, -1.0F, 0.0F);
				this.renderTorchAtAngle(var1, -0.5F, -0.5F, -0.5F, 0.0F, 0.0F);
				var2.draw();
			}

		}
	}
}
