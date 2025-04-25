package net.minecraft.client.render;

import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class RenderBlocks {
	private Tessellator tessellator;
	private World blockAccess;
    private int overrideBlockTexture = -1;
    private boolean renderSide = false;

	public RenderBlocks(Tessellator var1, World var2) {
		this.tessellator = var1;
		this.blockAccess = var2;
	}

	public RenderBlocks(Tessellator var1) {
		this.tessellator = var1;
	}

    public final void renderBlockAllFaces(Block var1, int var2, int var3, int var4, int var5) {
        this.overrideBlockTexture = var5;
        this.renderBlockByRenderType(var1, var2, var3, var4);
        this.overrideBlockTexture = -1;
    }

    public final void renderBlockAllFaces(Block var1, int var2, int var3, int var4) {
        this.renderSide = true;
        this.renderBlockByRenderType(var1, var2, var3, var4);
        this.renderSide = false;
    }

    public final boolean renderBlockByRenderType(Block var1, int var2, int var3, int var4) {
        int var5 = var1.g();
        float var7;
        if(var5 != 0) {
            if(var5 == 1) {
                var7 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
                this.tessellator.setColorOpaque_F(var7, var7, var7);
                this.renderBlockPlant(var1, (float)var2, (float)var3, (float)var4);
                return true;
            } else if(var5 == 2) {
                var7 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
                this.tessellator.setColorOpaque_F(var7, var7, var7);
                if(this.blockAccess.isBlockNormalCube(var2 - 1, var3, var4)) {
                    this.renderBlockTorch(var1, (float)var2, (float)var3 + 0.2F, (float)var4, -0.5F, 0.0F);
                } else if(this.blockAccess.isBlockNormalCube(var2 + 1, var3, var4)) {
                    this.renderBlockTorch(var1, (float)var2, (float)var3 + 0.2F, (float)var4, 0.5F, 0.0F);
                } else if(this.blockAccess.isBlockNormalCube(var2, var3, var4 - 1)) {
                    this.renderBlockTorch(var1, (float)var2, (float)var3 + 0.2F, (float)var4, 0.0F, -0.5F);
                } else if(this.blockAccess.isBlockNormalCube(var2, var3, var4 + 1)) {
                    this.renderBlockTorch(var1, (float)var2, (float)var3 + 0.2F, (float)var4, 0.0F, 0.5F);
                } else {
                    this.renderBlockTorch(var1, (float)var2, (float)var3, (float)var4, 0.0F, 0.0F);
                }

                return true;
            } else {
                return false;
            }
        } else {
            boolean var6 = false;
            if(this.renderSide || var1.shouldSideBeRendered(this.blockAccess, var2, var3 - 1, var4, 0)) {
                var7 = var1.getBlockBrightness(this.blockAccess, var2, var3 - 1, var4);
                this.tessellator.setColorOpaque_F(0.5F * var7, 0.5F * var7, 0.5F * var7);
                this.renderBlockBottom(var1, var2, var3, var4, var1.getBlockTexture(0));
                var6 = true;
            }

            if(this.renderSide || var1.shouldSideBeRendered(this.blockAccess, var2, var3 + 1, var4, 1)) {
                var7 = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4);
                this.tessellator.setColorOpaque_F(var7 * 1.0F, var7 * 1.0F, var7 * 1.0F);
                this.renderBlockTop(var1, var2, var3, var4, var1.getBlockTexture(1));
                var6 = true;
            }

            if(this.renderSide || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 - 1, 2)) {
                var7 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 - 1);
                this.tessellator.setColorOpaque_F(0.8F * var7, 0.8F * var7, 0.8F * var7);
                this.renderBlockNorth(var1, var2, var3, var4, var1.getBlockTexture(2));
                var6 = true;
            }

            if(this.renderSide || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 + 1, 3)) {
                var7 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 + 1);
                this.tessellator.setColorOpaque_F(0.8F * var7, 0.8F * var7, 0.8F * var7);
                this.renderBlockSouth(var1, var2, var3, var4, var1.getBlockTexture(3));
                var6 = true;
            }

            if(this.renderSide || var1.shouldSideBeRendered(this.blockAccess, var2 - 1, var3, var4, 4)) {
                var7 = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4);
                this.tessellator.setColorOpaque_F(0.6F * var7, 0.6F * var7, 0.6F * var7);
                this.renderBlockWest(var1, var2, var3, var4, var1.getBlockTexture(4));
                var6 = true;
            }

            if(this.renderSide || var1.shouldSideBeRendered(this.blockAccess, var2 + 1, var3, var4, 5)) {
                var7 = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4);
                this.tessellator.setColorOpaque_F(0.6F * var7, 0.6F * var7, 0.6F * var7);
                this.renderBlockEast(var1, var2, var3, var4, var1.getBlockTexture(5));
                var6 = true;
            }

            return var6;
        }
    }

    private void renderBlockTorch(Block var1, float var2, float var3, float var4, float var5, float var6) {
        int var14 = var1.getBlockTexture(0);
        if(this.overrideBlockTexture >= 0) {
            var14 = this.overrideBlockTexture;
        }

        int var7 = (var14 & 15) << 4;
        var14 &= 240;
        float var8 = (float)var7 / 256.0F;
        float var16 = ((float)var7 + 15.99F) / 256.0F;
        float var9 = (float)var14 / 256.0F;
        float var15 = ((float)var14 + 15.99F) / 256.0F;
        var2 += 0.5F;
        var4 += 0.5F;
        float var10 = var2 - 0.5F;
        float var11 = var2 + 0.5F;
        float var12 = var4 - 0.5F;
        float var13 = var4 + 0.5F;
        this.tessellator.addVertexWithUV(var2 - 1.0F / 16.0F, var3 + 1.0F, var12, var8, var9);
        this.tessellator.addVertexWithUV(var2 - 1.0F / 16.0F + var5, var3, var12 + var6, var8, var15);
        this.tessellator.addVertexWithUV(var2 - 1.0F / 16.0F + var5, var3, var13 + var6, var16, var15);
        this.tessellator.addVertexWithUV(var2 - 1.0F / 16.0F, var3 + 1.0F, var13, var16, var9);
        this.tessellator.addVertexWithUV(var2 + 1.0F / 16.0F, var3 + 1.0F, var13, var8, var9);
        this.tessellator.addVertexWithUV(var2 + var5 + 1.0F / 16.0F, var3, var13 + var6, var8, var15);
        this.tessellator.addVertexWithUV(var2 + var5 + 1.0F / 16.0F, var3, var12 + var6, var16, var15);
        this.tessellator.addVertexWithUV(var2 + 1.0F / 16.0F, var3 + 1.0F, var12, var16, var9);
        this.tessellator.addVertexWithUV(var10, var3 + 1.0F, var4 + 1.0F / 16.0F, var8, var9);
        this.tessellator.addVertexWithUV(var10 + var5, var3, var4 + 1.0F / 16.0F + var6, var8, var15);
        this.tessellator.addVertexWithUV(var11 + var5, var3, var4 + 1.0F / 16.0F + var6, var16, var15);
        this.tessellator.addVertexWithUV(var11, var3 + 1.0F, var4 + 1.0F / 16.0F, var16, var9);
        this.tessellator.addVertexWithUV(var11, var3 + 1.0F, var4 - 1.0F / 16.0F, var8, var9);
        this.tessellator.addVertexWithUV(var11 + var5, var3, var4 - 1.0F / 16.0F + var6, var8, var15);
        this.tessellator.addVertexWithUV(var10 + var5, var3, var4 - 1.0F / 16.0F + var6, var16, var15);
        this.tessellator.addVertexWithUV(var10, var3 + 1.0F, var4 - 1.0F / 16.0F, var16, var9);
    }

	private void renderBlockPlant(Block var1, float var2, float var3, float var4) {
		int var10 = var1.getBlockTexture(0);
		if(this.overrideBlockTexture >= 0) {
			var10 = this.overrideBlockTexture;
		}

		int var5 = (var10 & 15) << 4;
		var10 &= 240;
		float var6 = (float)var5 / 256.0F;
		float var12 = ((float)var5 + 15.99F) / 256.0F;
		float var7 = (float)var10 / 256.0F;
		float var11 = ((float)var10 + 15.99F) / 256.0F;
		float var8 = var2 + 0.5F - 0.45F;
		var2 = var2 + 0.5F + 0.45F;
		float var9 = var4 + 0.5F - 0.45F;
		var4 = var4 + 0.5F + 0.45F;
		this.tessellator.addVertexWithUV(var8, var3 + 1.0F, var9, var6, var7);
		this.tessellator.addVertexWithUV(var8, var3, var9, var6, var11);
		this.tessellator.addVertexWithUV(var2, var3, var4, var12, var11);
		this.tessellator.addVertexWithUV(var2, var3 + 1.0F, var4, var12, var7);
		this.tessellator.addVertexWithUV(var2, var3 + 1.0F, var4, var6, var7);
		this.tessellator.addVertexWithUV(var2, var3, var4, var6, var11);
		this.tessellator.addVertexWithUV(var8, var3, var9, var12, var11);
		this.tessellator.addVertexWithUV(var8, var3 + 1.0F, var9, var12, var7);
		this.tessellator.addVertexWithUV(var8, var3 + 1.0F, var4, var6, var7);
		this.tessellator.addVertexWithUV(var8, var3, var4, var6, var11);
		this.tessellator.addVertexWithUV(var2, var3, var9, var12, var11);
		this.tessellator.addVertexWithUV(var2, var3 + 1.0F, var9, var12, var7);
		this.tessellator.addVertexWithUV(var2, var3 + 1.0F, var9, var6, var7);
		this.tessellator.addVertexWithUV(var2, var3, var9, var6, var11);
		this.tessellator.addVertexWithUV(var8, var3, var4, var12, var11);
		this.tessellator.addVertexWithUV(var8, var3 + 1.0F, var4, var12, var7);
	}

	private void renderBlockBottom(Block var1, int var2, int var3, int var4, int var5) {
		if(this.overrideBlockTexture >= 0) {
			var5 = this.overrideBlockTexture;
		}

		int var6 = (var5 & 15) << 4;
		var5 &= 240;
		float var7 = (float)var6 / 256.0F;
		float var14 = ((float)var6 + 15.99F) / 256.0F;
		float var8 = (float)var5 / 256.0F;
		float var15 = ((float)var5 + 15.99F) / 256.0F;
		float var9 = (float)var2 + var1.minX;
		float var12 = (float)var2 + var1.maxX;
		float var13 = (float)var3 + var1.minY;
		float var10 = (float)var4 + var1.minZ;
		float var11 = (float)var4 + var1.maxZ;
		this.tessellator.addVertexWithUV(var9, var13, var11, var7, var15);
		this.tessellator.addVertexWithUV(var9, var13, var10, var7, var8);
		this.tessellator.addVertexWithUV(var12, var13, var10, var14, var8);
		this.tessellator.addVertexWithUV(var12, var13, var11, var14, var15);
	}

	private void renderBlockTop(Block var1, int var2, int var3, int var4, int var5) {
		if(this.overrideBlockTexture >= 0) {
			var5 = this.overrideBlockTexture;
		}

		int var6 = (var5 & 15) << 4;
		var5 &= 240;
		float var7 = (float)var6 / 256.0F;
		float var14 = ((float)var6 + 15.99F) / 256.0F;
		float var8 = (float)var5 / 256.0F;
		float var15 = ((float)var5 + 15.99F) / 256.0F;
		float var9 = (float)var2 + var1.minX;
		float var12 = (float)var2 + var1.maxX;
		float var13 = (float)var3 + var1.maxY;
		float var10 = (float)var4 + var1.minZ;
		float var11 = (float)var4 + var1.maxZ;
		this.tessellator.addVertexWithUV(var12, var13, var11, var14, var15);
		this.tessellator.addVertexWithUV(var12, var13, var10, var14, var8);
		this.tessellator.addVertexWithUV(var9, var13, var10, var7, var8);
		this.tessellator.addVertexWithUV(var9, var13, var11, var7, var15);
	}

    private void renderBlockNorth(Block var1, int var2, int var3, int var4, int var5) {
        if(this.overrideBlockTexture >= 0) {
            var5 = this.overrideBlockTexture;
        }

        int var6 = (var5 & 15) << 4;
        var5 &= 240;
        float var7 = (float)var6 / 256.0F;
        float var14 = ((float)var6 + 15.99F) / 256.0F;
        float var8;
        float var15;
        if(var1.minY >= 0.0F && var1.maxY <= 1.0F) {
            var8 = ((float)var5 + var1.minY * 15.99F) / 256.0F;
            var15 = ((float)var5 + var1.maxY * 15.99F) / 256.0F;
        } else {
            var8 = (float)var5 / 256.0F;
            var15 = ((float)var5 + 15.99F) / 256.0F;
        }

        float var9 = (float)var2 + var1.minX;
        float var12 = (float)var2 + var1.maxX;
        float var10 = (float)var3 + var1.minY;
        float var13 = (float)var3 + var1.maxY;
        float var11 = (float)var4 + var1.minZ;
        this.tessellator.addVertexWithUV(var9, var13, var11, var14, var8);
        this.tessellator.addVertexWithUV(var12, var13, var11, var7, var8);
        this.tessellator.addVertexWithUV(var12, var10, var11, var7, var15);
        this.tessellator.addVertexWithUV(var9, var10, var11, var14, var15);
    }

    private void renderBlockSouth(Block var1, int var2, int var3, int var4, int var5) {
        if(this.overrideBlockTexture >= 0) {
            var5 = this.overrideBlockTexture;
        }

        int var6 = (var5 & 15) << 4;
        var5 &= 240;
        float var7 = (float)var6 / 256.0F;
        float var14 = ((float)var6 + 15.99F) / 256.0F;
        float var8;
        float var15;
        if(var1.minY >= 0.0F && var1.maxY <= 1.0F) {
            var8 = ((float)var5 + var1.minY * 15.99F) / 256.0F;
            var15 = ((float)var5 + var1.maxY * 15.99F) / 256.0F;
        } else {
            var8 = (float)var5 / 256.0F;
            var15 = ((float)var5 + 15.99F) / 256.0F;
        }

        float var9 = (float)var2 + var1.minX;
        float var12 = (float)var2 + var1.maxX;
        float var10 = (float)var3 + var1.minY;
        float var13 = (float)var3 + var1.maxY;
        float var11 = (float)var4 + var1.maxZ;
        this.tessellator.addVertexWithUV(var9, var13, var11, var7, var8);
        this.tessellator.addVertexWithUV(var9, var10, var11, var7, var15);
        this.tessellator.addVertexWithUV(var12, var10, var11, var14, var15);
        this.tessellator.addVertexWithUV(var12, var13, var11, var14, var8);
    }

    private void renderBlockWest(Block var1, int var2, int var3, int var4, int var5) {
        if(this.overrideBlockTexture >= 0) {
            var5 = this.overrideBlockTexture;
        }

        int var6 = (var5 & 15) << 4;
        var5 &= 240;
        float var7 = (float)var6 / 256.0F;
        float var14 = ((float)var6 + 15.99F) / 256.0F;
        float var8;
        float var15;
        if(var1.minY >= 0.0F && var1.maxY <= 1.0F) {
            var8 = ((float)var5 + var1.minY * 15.99F) / 256.0F;
            var15 = ((float)var5 + var1.maxY * 15.99F) / 256.0F;
        } else {
            var8 = (float)var5 / 256.0F;
            var15 = ((float)var5 + 15.99F) / 256.0F;
        }

        float var12 = (float)var2 + var1.minX;
        float var9 = (float)var3 + var1.minY;
        float var13 = (float)var3 + var1.maxY;
        float var10 = (float)var4 + var1.minZ;
        float var11 = (float)var4 + var1.maxZ;
        this.tessellator.addVertexWithUV(var12, var13, var11, var14, var8);
        this.tessellator.addVertexWithUV(var12, var13, var10, var7, var8);
        this.tessellator.addVertexWithUV(var12, var9, var10, var7, var15);
        this.tessellator.addVertexWithUV(var12, var9, var11, var14, var15);
    }

    private void renderBlockEast(Block var1, int var2, int var3, int var4, int var5) {
        if(this.overrideBlockTexture >= 0) {
            var5 = this.overrideBlockTexture;
        }

        int var6 = (var5 & 15) << 4;
        var5 &= 240;
        float var7 = (float)var6 / 256.0F;
        float var14 = ((float)var6 + 15.99F) / 256.0F;
        float var8;
        float var15;
        if(var1.minY >= 0.0F && var1.maxY <= 1.0F) {
            var8 = ((float)var5 + var1.minY * 15.99F) / 256.0F;
            var15 = ((float)var5 + var1.maxY * 15.99F) / 256.0F;
        } else {
            var8 = (float)var5 / 256.0F;
            var15 = ((float)var5 + 15.99F) / 256.0F;
        }

        float var12 = (float)var2 + var1.maxX;
        float var9 = (float)var3 + var1.minY;
        float var13 = (float)var3 + var1.maxY;
        float var10 = (float)var4 + var1.minZ;
        float var11 = (float)var4 + var1.maxZ;
        this.tessellator.addVertexWithUV(var12, var9, var11, var7, var15);
        this.tessellator.addVertexWithUV(var12, var9, var10, var14, var15);
        this.tessellator.addVertexWithUV(var12, var13, var10, var14, var8);
        this.tessellator.addVertexWithUV(var12, var13, var11, var7, var8);
    }

	public final void renderBlockOnInventory(Block var1) {
		int var2 = var1.g();
		if(var2 == 0) {
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            this.tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
            this.tessellator.normal(0.0F, -1.0F, 0.0F);
            this.renderBlockBottom(var1, 0, 0, 0, var1.getBlockTexture(0));
            this.tessellator.draw();
            this.tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
            this.tessellator.normal(0.0F, 1.0F, 0.0F);
            this.renderBlockTop(var1, 0, 0, 0, var1.getBlockTexture(1));
            this.tessellator.draw();
            this.tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
            this.tessellator.normal(0.0F, 0.0F, -1.0F);
            this.renderBlockNorth(var1, 0, 0, 0, var1.getBlockTexture(2));
            this.tessellator.draw();
            this.tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
            this.tessellator.normal(0.0F, 0.0F, 1.0F);
            this.renderBlockSouth(var1, 0, 0, 0, var1.getBlockTexture(3));
            this.tessellator.draw();
            this.tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
            this.tessellator.normal(-1.0F, 0.0F, 0.0F);
            this.renderBlockWest(var1, 0, 0, 0, var1.getBlockTexture(4));
            this.tessellator.draw();
            this.tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
            this.tessellator.normal(1.0F, 0.0F, 0.0F);
            this.renderBlockEast(var1, 0, 0, 0, var1.getBlockTexture(5));
            this.tessellator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		} else if(var2 == 1) {
			this.tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
			this.tessellator.normal(0.0F, -1.0F, 0.0F);
			this.renderBlockPlant(var1, -0.5F, -0.5F, -0.5F);
			this.tessellator.draw();
		} else {
			if(var2 == 2) {
				this.tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
				this.tessellator.normal(0.0F, -1.0F, 0.0F);
				this.renderBlockTorch(var1, -0.5F, -0.5F, -0.5F, 0.0F, 0.0F);
				this.tessellator.draw();
			}

		}
	}
}
