package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityFallingSand;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public final class RenderFallingSand extends Render {
    private RenderBlocks sandRenderBlocks = new RenderBlocks();

    public RenderFallingSand() {
        this.shadowSize = 0.5F;
    }

    public final void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        EntityFallingSand entityFallingSand10001 = (EntityFallingSand)entity;
        double d12 = x;
        EntityFallingSand x1 = entityFallingSand10001;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d12, (float)y, (float)z);
        this.loadTexture("/terrain.png");
        Block block3 = Block.blocksList[x1.blockID];
        World y1 = x1.getWorld();
        GL11.glDisable(GL11.GL_LIGHTING);
        this.sandRenderBlocks.renderBlockFallingSand(block3, y1, MathHelper.floor_double(x1.posX), MathHelper.floor_double(x1.posY), MathHelper.floor_double(x1.posZ));
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}