package net.minecraft.client.render.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelSheep;
import net.minecraft.client.model.ModelSheepFur;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.EntityPainting;
import net.minecraft.game.entity.animal.EntityPig;
import net.minecraft.game.entity.animal.EntitySheep;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.misc.EntityTNTPrimed;
import net.minecraft.game.entity.monster.EntityCreeper;
import net.minecraft.game.entity.monster.EntityGiantZombie;
import net.minecraft.game.entity.monster.EntitySkeleton;
import net.minecraft.game.entity.monster.EntitySpider;
import net.minecraft.game.entity.monster.EntityZombie;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.world.World;

public final class RenderManager {
	private Map entityRenderMap = new HashMap();
    public static RenderManager instance = new RenderManager();
    public static double renderPosX;
    public static double renderPosY;
    public static double renderPosZ;
	public RenderEngine renderEngine;
	public World worldObj;
	public float playerViewY;
    private double viewerPosX;
    private double viewerPosY;
    private double viewerPosZ;

    private RenderManager() {
        this.entityRenderMap.put(EntitySpider.class, new RenderSpider());
        this.entityRenderMap.put(EntityPig.class, new RenderLiving(new ModelPig(), 0.7F));
        this.entityRenderMap.put(EntitySheep.class, new RenderSheep(new ModelSheep(), new ModelSheepFur(), 0.7F));
        this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper());
        this.entityRenderMap.put(EntitySkeleton.class, new RenderLiving(new ModelSkeleton(), 0.5F));
        this.entityRenderMap.put(EntityZombie.class, new RenderLiving(new ModelZombie(), 0.5F));
        this.entityRenderMap.put(EntityPlayer.class, new RenderPlayer());
        this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(new ModelZombie(), 0.5F, 6.0F));
        this.entityRenderMap.put(EntityLiving.class, new RenderLiving(new ModelBiped(), 0.5F));
        this.entityRenderMap.put(Entity.class, new RenderEntity());
        this.entityRenderMap.put(EntityPainting.class, new RenderPainting());
        this.entityRenderMap.put(EntityArrow.class, new RenderArrow());
        this.entityRenderMap.put(EntityItem.class, new RenderItem());
        this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed());
		Iterator var1 = this.entityRenderMap.values().iterator();

		while(var1.hasNext()) {
			Render var2 = (Render)var1.next();
			var2.setRenderManager(this);
		}

	}

    public final Render getEntityRenderObject(Entity var1) {
        Class var2 = var1.getClass();
        Render var3 = (Render)this.entityRenderMap.get(var2);
        if(var3 == null && var2 != Entity.class) {
            var3 = (Render)this.entityRenderMap.get(var2.getSuperclass());
            this.entityRenderMap.put(var2, var3);
        }

        return var3;
    }

    public final void cacheActiveRenderInfo(World var1, RenderEngine var2, EntityPlayer var3, float var4) {
        this.worldObj = var1;
        this.renderEngine = var2;
        this.playerViewY = var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * var4;
        this.viewerPosX = var3.lastTickPosX + (var3.posX - var3.lastTickPosX) * (double)var4;
        this.viewerPosY = var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * (double)var4;
        this.viewerPosZ = var3.lastTickPosZ + (var3.posZ - var3.lastTickPosZ) * (double)var4;
    }

    public final void renderEntity(Entity var1, float var2) {
        double var3 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var2;
        double var5 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var2;
        double var7 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var2;
        float var9 = var1.prevRotationYaw + (var1.rotationYaw - var1.prevRotationYaw) * var2;
        float var10 = var1.getBrightness(var2);
        GL11.glColor3f(var10, var10, var10);
        this.renderEntityWithPosYaw(var1, var3 - renderPosX, var5 - renderPosY, var7 - renderPosZ, var9, var2);
    }

    public final void renderEntityWithPosYaw(Entity var1, double var2, double var4, double var6, float var8, float var9) {
        Render var10 = this.getEntityRenderObject(var1);
        if(var10 != null) {
            var10.doRender(var1, var2, var4, var6, var8, var9);
            var10.doRenderShadowAndFire(var1, var2, var4, var6, var9);
        }

    }

    public final void set(World var1) {
        this.worldObj = var1;
    }

    public final double getDistanceToCamera(double var1, double var3, double var5) {
        double var7 = var1 - this.viewerPosX;
        double var9 = var3 - this.viewerPosY;
        double var11 = var5 - this.viewerPosZ;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }
}
