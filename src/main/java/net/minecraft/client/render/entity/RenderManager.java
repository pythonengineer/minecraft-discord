package net.minecraft.client.render.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.misc.EntityTNTPrimed;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.level.World;

public final class RenderManager {
	private Map entityRenderMap = new HashMap();
	public RenderEngine renderEngine;
	public World worldObj;
	public float playerViewY;

	public RenderManager() {
		this.entityRenderMap.put(Entity.class, new RenderEntity());
		this.entityRenderMap.put(EntityArrow.class, new RenderArrow());
		this.entityRenderMap.put(EntityLiving.class, new RenderMD3());
		this.entityRenderMap.put(EntityItem.class, new RenderItem());
		this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed());
		Iterator var1 = this.entityRenderMap.values().iterator();

		while(var1.hasNext()) {
			Render var2 = (Render)var1.next();
			var2.setRenderManager(this);
		}

	}

	public final void renderEntityWithPosYaw(Entity var1, float var2, float var3, float var4, float var5, float var6) {
		Class var8 = var1.getClass();
		Render var9 = (Render)this.entityRenderMap.get(var8);
		if(var9 == null && var8 != Entity.class) {
			var9 = (Render)this.entityRenderMap.get(var8.getSuperclass());
			this.entityRenderMap.put(var8, var9);
		}

		if(var9 != null) {
			var9.doRender(var1, var2, var3, var4, var5, var6);
			var9.renderShadow(var1, var2, var3, var4, var6);
		}

	}
}
