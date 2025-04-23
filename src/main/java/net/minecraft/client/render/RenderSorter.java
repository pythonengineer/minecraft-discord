package net.minecraft.client.render;

import java.util.Comparator;
import net.minecraft.client.player.EntityPlayer;

public final class RenderSorter implements Comparator {
	private EntityPlayer player;

	public RenderSorter(EntityPlayer var1) {
		this.player = var1;
	}

	public final int compare(Object var1, Object var2) {
		WorldRenderer var10001 = (WorldRenderer)var1;
		WorldRenderer var6 = (WorldRenderer)var2;
		WorldRenderer var5 = var10001;
		boolean var3 = var5.isInFrustrum;
		boolean var4 = var6.isInFrustrum;
		return var3 && !var4 ? 1 : ((!var4 || var3) && var5.a(this.player) < var6.a(this.player) ? 1 : -1);
	}
}
