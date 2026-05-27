package net.minecraft.game.world.block.tileentity;

import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.client.render.tileentity.TileEntitySpecialRenderer;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityList;
import net.minecraft.game.world.World;

public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer {
    private Map entityHashMap = new HashMap();

    public void renderTileEntityMobSpawner(TileEntityMobSpawner spawner, double d2, double d4, double d6, float f8) {
        double d11 = d2;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d11 + 0.5F, (float)d4, (float)d6 + 0.5F);
        Entity entity18;
        if((entity18 = (Entity)this.entityHashMap.get(spawner.mobID)) == null) {
            entity18 = EntityList.createEntityInWorld(spawner.mobID, (World)null);
            this.entityHashMap.put(spawner.mobID, entity18);
        }

        if(entity18 != null) {
            entity18.setWorld(spawner.worldObj);
            GL11.glTranslatef(0.0F, 0.4F, 0.0F);
            GL11.glRotatef((float)(spawner.prevYaw + (spawner.yaw - spawner.prevYaw) * (double)f8) * 10.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.4F, 0.0F);
            GL11.glScalef(0.4375F, 0.4375F, 0.4375F);
            RenderManager.instance.renderEntityWithPosYaw(entity18, 0.0D, 0.0D, 0.0D, 0.0F, f8);
        }

        GL11.glPopMatrix();
    }

    public void renderTileEntityMobSpawner(TileEntity tileEntity1, double d2, double d4, double d6, float f8) {
        this.renderTileEntityMobSpawner((TileEntityMobSpawner)tileEntity1, d2, d4, d6, f8);
    }
}
