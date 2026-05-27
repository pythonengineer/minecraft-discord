package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityFallingSand;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class RenderFallingSand extends Render {
    private RenderBlocks sandRenderBlocks = new RenderBlocks();

    public RenderFallingSand() {
        this.shadowSize = 0.5F;
    }

    public void doRender(EntityFallingSand entity, double x, double y, double z, float yaw, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        this.loadTexture("/terrain.png");
        Block block2 = Block.blocksList[entity.blockID];
        World world3 = entity.getWorld();
        GL11.glDisable(GL11.GL_LIGHTING);
        this.sandRenderBlocks.renderBlockFallingSand(block2, world3, MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ));
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    public void doRender(Entity entityLiving, double xCoord, double sqrt_double, double yCoord, float f8, float f9) {
        this.doRender((EntityFallingSand)entityLiving, xCoord, sqrt_double, yCoord, f8, f9);
    }
}
