package net.minecraft.client.effect;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.level.World;

public final class EffectRenderer {
	public World worldObj;
	public List[] fxLayers = new List[2];
	public RenderEngine renderEngine;
	public EaglercraftRandom rand = new EaglercraftRandom();

	public EffectRenderer(World var1, RenderEngine var2) {
		if(var1 != null) {
			this.worldObj = var1;
		}

		this.renderEngine = var2;

		for(int var3 = 0; var3 < 2; ++var3) {
			this.fxLayers[var3] = new ArrayList();
		}

	}

	public final void addEffect(EntityFX var1) {
		int var2 = var1.getFXLayer();
		this.fxLayers[var2].add(var1);
	}
}
