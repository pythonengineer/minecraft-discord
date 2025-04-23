package net.minecraft.client.render;

import java.util.Comparator;
import net.minecraft.client.player.EntityPlayer;

public final class EntitySorter implements Comparator {
	private EntityPlayer player;

	public EntitySorter(EntityPlayer var1) {
		this.player = var1;
	}

	public final int compare(Object var1, Object var2) {
		WorldRenderer var10001 = (WorldRenderer)var1;
		WorldRenderer var4 = (WorldRenderer)var2;
		WorldRenderer var3 = var10001;
		return var3.a(this.player) < var4.a(this.player) ? -1 : 1;
	}
}
