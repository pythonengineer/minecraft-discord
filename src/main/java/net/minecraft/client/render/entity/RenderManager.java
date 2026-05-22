package net.minecraft.client.render.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.FontRenderer;
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
import net.minecraft.game.entity.misc.EntityFallingSand;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.misc.EntityMinecart;
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
    public GameSettings options;
	private double viewerPosX;
	private double viewerPosY;
	private double viewerPosZ;

	private RenderManager() {
		this.entityRenderMap.put(EntitySpider.class, new RenderSpider());
        this.entityRenderMap.put(EntityPig.class, new RenderPig(new ModelPig(), new ModelPig(0.5F), 0.7F));
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
        this.entityRenderMap.put(EntityFallingSand.class, new RenderFallingSand());
        this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart());
		Iterator iterator1 = this.entityRenderMap.values().iterator();

		while(iterator1.hasNext()) {
			((Render)iterator1.next()).setRenderManager(this);
		}

	}

	private Render getEntityClassRenderObject(Class entityClass) {
		Render render2;
		if((render2 = (Render)this.entityRenderMap.get(entityClass)) == null && entityClass != Entity.class) {
			render2 = this.getEntityClassRenderObject(entityClass.getSuperclass());
			this.entityRenderMap.put(entityClass, render2);
		}

		return render2;
	}

	public final Render getEntityRenderObject(Entity entity) {
		return this.getEntityClassRenderObject(entity.getClass());
	}

    public final void cacheActiveRenderInfo(World world, RenderEngine renderEngine, FontRenderer fontRenderer, EntityPlayer playerEntity, GameSettings options, float partialTicks) {
        this.worldObj = world;
        this.renderEngine = renderEngine;
        this.options = options;
        this.playerViewY = playerEntity.prevRotationYaw + (playerEntity.rotationYaw - playerEntity.prevRotationYaw) * partialTicks;
        this.viewerPosX = playerEntity.lastTickPosX + (playerEntity.posX - playerEntity.lastTickPosX) * (double)partialTicks;
        this.viewerPosY = playerEntity.lastTickPosY + (playerEntity.posY - playerEntity.lastTickPosY) * (double)partialTicks;
        this.viewerPosZ = playerEntity.lastTickPosZ + (playerEntity.posZ - playerEntity.lastTickPosZ) * (double)partialTicks;
    }

	public final void renderEntity(Entity entity, float partialTicks) {
		double d3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
		double d5 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
		double d7 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
		float f9 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
		float f10;
		GL11.glColor3f(f10 = entity.getBrightness(partialTicks), f10, f10);
		this.renderEntityWithPosYaw(entity, d3 - renderPosX, d5 - renderPosY, d7 - renderPosZ, f9, partialTicks);
	}

	public final void renderEntityWithPosYaw(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
		Render render10;
		if((render10 = this.getEntityRenderObject(entity)) != null) {
			render10.doRender(entity, x, y, z, yaw, partialTicks);
			render10.renderShadow(entity, x, y, z, partialTicks);
		}

	}

	public final void set(World world) {
		this.worldObj = world;
	}

	public final double getDistanceToCamera(double x, double y, double z) {
		double d7 = x - this.viewerPosX;
		double d9 = y - this.viewerPosY;
		double d11 = z - this.viewerPosZ;
		return d7 * d7 + d9 * d9 + d11 * d11;
	}
}