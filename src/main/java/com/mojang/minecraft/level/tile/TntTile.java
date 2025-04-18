package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.item.PrimedTnt;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.particle.ParticleEngine;

public final class TntTile extends Tile {
	public TntTile(int i1, int i2) {
		super(46, 8);
	}

	protected final int getTexture(int face) {
		return face == 0 ? this.tex + 2 : (face == 1 ? this.tex + 1 : this.tex);
	}

	public final int resourceCount() {
		return 0;
	}

	public final void wasExploded(Level level, int x, int y, int z) {
		if(!level.creativeMode) {
			PrimedTnt primedTnt5;
			(primedTnt5 = new PrimedTnt(level, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F)).life = random.nextInt(primedTnt5.life / 4) + primedTnt5.life / 8;
			level.addEntity(primedTnt5);
		}

	}

	public final void destroy(Level level, int x, int y, int z, ParticleEngine particleEngine) {
		if(!level.creativeMode) {
			level.addEntity(new PrimedTnt(level, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F));
		} else {
			super.destroy(level, x, y, z, particleEngine);
		}
	}
}