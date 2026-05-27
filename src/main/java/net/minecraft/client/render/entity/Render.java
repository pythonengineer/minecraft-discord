package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public abstract class Render {
	protected RenderManager renderManager;
    private ModelBase modelBase = new ModelBiped();
    private RenderBlocks renderBlocksVar = new RenderBlocks();
    protected float shadowSize = 0.0F;
    protected float shadowOpaque = 1.0F;

	public abstract void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9);

	protected void loadTexture(String textureName) {
		RenderEngine renderEngine2 = this.renderManager.renderEngine;
		renderEngine2.bindTexture(renderEngine2.getTexture(textureName));
	}

	protected void loadDownloadableImageTexture(String url, String textureName) {
		RenderEngine renderEngine3 = this.renderManager.renderEngine;
		renderEngine3.bindTexture(renderEngine3.getTextureForDownloadableImage(url, textureName));
	}

    private void renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks) {
        GL11.glDisable(GL11.GL_LIGHTING);
        int i9 = Block.fire.blockIndexInTexture;
        int i10 = (i9 & 15) << 4;
        int i11 = i9 & 240;
        float f12 = (float)i10 / 256.0F;
        float f13 = ((float)i10 + 15.99F) / 256.0F;
        float f14 = (float)i11 / 256.0F;
        float f15 = ((float)i11 + 15.99F) / 256.0F;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        float f16 = entity.width * 1.4F;
        GL11.glScalef(f16, f16, f16);
        this.loadTexture("/terrain.png");
        Tessellator tessellator17 = Tessellator.instance;
        float f18 = 1.0F;
        float f19 = 0.5F;
        float f20 = 0.0F;
        float f21 = entity.height / entity.width;
        GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, 0.0F, 0.4F + (float)((int)f21) * 0.02F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator17.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);

        while(f21 > 0.0F) {
            tessellator17.addVertexWithUV((double)(f18 - f19), (double)(0.0F - f20), 0.0D, (double)f13, (double)f15);
            tessellator17.addVertexWithUV((double)(0.0F - f19), (double)(0.0F - f20), 0.0D, (double)f12, (double)f15);
            tessellator17.addVertexWithUV((double)(0.0F - f19), (double)(1.4F - f20), 0.0D, (double)f12, (double)f14);
            tessellator17.addVertexWithUV((double)(f18 - f19), (double)(1.4F - f20), 0.0D, (double)f13, (double)f14);
            --f21;
            --f20;
            f18 *= 0.9F;
            GL11.glTranslatef(0.0F, 0.0F, -0.04F);
        }

        tessellator17.draw();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private void renderShadow(Entity entity, double x, double y, double z, float f8, float partialTicks) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderEngine renderEngine10 = this.renderManager.renderEngine;
        renderEngine10.bindTexture(renderEngine10.getTexture("%%/shadow.png"));
        World world11 = this.getWorldFromRenderManager();
        GL11.glDepthMask(false);
        float f12 = this.shadowSize;
        double d13 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        double d15 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double d17 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        int i19 = MathHelper.floor_double(d13 - (double)f12);
        int i20 = MathHelper.floor_double(d13 + (double)f12);
        int i21 = MathHelper.floor_double(d15 - (double)f12);
        int i22 = MathHelper.floor_double(d15);
        int i23 = MathHelper.floor_double(d17 - (double)f12);
        int i24 = MathHelper.floor_double(d17 + (double)f12);
        double d25 = x - d13;
        double d27 = y - d15;
        double d29 = z - d17;
        Tessellator tessellator31 = Tessellator.instance;
        tessellator31.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);

        for(int i32 = i19; i32 <= i20; ++i32) {
            for(int i33 = i21; i33 <= i22; ++i33) {
                for(int i34 = i23; i34 <= i24; ++i34) {
                    int i35 = world11.getBlockId(i32, i33 - 1, i34);
                    if(i35 > 0 && world11.getBlockLightValue(i32, i33, i34) > 3) {
                        this.renderShadowOnBlock(Block.blocksList[i35], x, y, z, i32, i33, i34, f8, f12, d25, d27, d29);
                    }
                }
            }
        }

        tessellator31.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
    }

    private World getWorldFromRenderManager() {
        return this.renderManager.worldObj;
    }

    private void renderShadowOnBlock(Block block1, double d2, double d4, double d6, int i8, int i9, int i10, float f11, float f12, double d13, double d15, double d17) {
        Tessellator tessellator19 = Tessellator.instance;
        if(block1.renderAsNormalBlock()) {
            double d20 = ((double)f11 - (d4 - ((double)i9 + d15)) / 2.0D) * 0.5D * (double)this.getWorldFromRenderManager().getBrightness(i8, i9, i10);
            if(d20 >= 0.0D) {
                if(d20 > 1.0D) {
                    d20 = 1.0D;
                }

                tessellator19.setColorRGBA_F(1.0F, 1.0F, 1.0F, (float)d20);
                double d22 = (double)i8 + block1.minX + d13;
                double d24 = (double)i8 + block1.maxX + d13;
                double d26 = (double)i9 + block1.minY + d15 + 0.015625D;
                double d28 = (double)i10 + block1.minZ + d17;
                double d30 = (double)i10 + block1.maxZ + d17;
                float f32 = (float)((d2 - d22) / 2.0D / (double)f12 + 0.5D);
                float f33 = (float)((d2 - d24) / 2.0D / (double)f12 + 0.5D);
                float f34 = (float)((d6 - d28) / 2.0D / (double)f12 + 0.5D);
                float f35 = (float)((d6 - d30) / 2.0D / (double)f12 + 0.5D);
                tessellator19.addVertexWithUV(d22, d26, d28, (double)f32, (double)f34);
                tessellator19.addVertexWithUV(d22, d26, d30, (double)f32, (double)f35);
                tessellator19.addVertexWithUV(d24, d26, d30, (double)f33, (double)f35);
                tessellator19.addVertexWithUV(d24, d26, d28, (double)f33, (double)f34);
            }
        }
    }

    public static void renderOffsetAABB(AxisAlignedBB aabb, double x, double y, double z) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tessellator7 = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator7.startDrawingQuads(DefaultVertexFormats.POSITION_NORMAL);
        tessellator7.setTranslationD(x, y, z);
        tessellator7.setNormal(0.0F, 0.0F, -1.0F);
        tessellator7.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        tessellator7.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        tessellator7.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        tessellator7.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        tessellator7.setNormal(0.0F, 0.0F, 1.0F);
        tessellator7.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        tessellator7.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        tessellator7.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        tessellator7.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        tessellator7.setNormal(0.0F, -1.0F, 0.0F);
        tessellator7.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        tessellator7.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        tessellator7.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        tessellator7.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        tessellator7.setNormal(0.0F, 1.0F, 0.0F);
        tessellator7.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        tessellator7.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        tessellator7.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        tessellator7.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        tessellator7.setNormal(-1.0F, 0.0F, 0.0F);
        tessellator7.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        tessellator7.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        tessellator7.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        tessellator7.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        tessellator7.setNormal(1.0F, 0.0F, 0.0F);
        tessellator7.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        tessellator7.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        tessellator7.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        tessellator7.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        tessellator7.setTranslationD(0.0D, 0.0D, 0.0D);
        tessellator7.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void renderAABB(AxisAlignedBB aabb) {
        Tessellator tessellator1 = Tessellator.instance;
        tessellator1.startDrawingQuads(DefaultVertexFormats.POSITION);
        tessellator1.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        tessellator1.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        tessellator1.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        tessellator1.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        tessellator1.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        tessellator1.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        tessellator1.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        tessellator1.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        tessellator1.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        tessellator1.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        tessellator1.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        tessellator1.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        tessellator1.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        tessellator1.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        tessellator1.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        tessellator1.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        tessellator1.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        tessellator1.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        tessellator1.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        tessellator1.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        tessellator1.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        tessellator1.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        tessellator1.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        tessellator1.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        tessellator1.draw();
    }

    public void setRenderManager(RenderManager renderManager) {
        this.renderManager = renderManager;
    }

    public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float f8, float partialTicks) {
        if(this.renderManager.options.fancyGraphics && this.shadowSize > 0.0F) {
            double d9 = this.renderManager.getDistanceToCamera(entity.posX, entity.posY, entity.posZ);
            float f12 = (float)((1.0D - d9 / 256.0D) * (double)this.shadowOpaque);
            if(f12 > 0.0F) {
                this.renderShadow(entity, x, y, z, f12, partialTicks);
            }
        }

        if(entity.fire > 0) {
            this.renderEntityOnFire(entity, x, y, z, partialTicks);
        }

    }
}
