package net.minecraft.game.level;

import net.minecraft.game.entity.Entity;

final class EntityMapSlot {
	int xSlot;
	int ySlot;
	int zSlot;
	private EntityMap entityMap;

	public EntityMapSlot(EntityMap var1) {
		this.entityMap = var1;
	}

	public final EntityMapSlot init(float var1, float var2, float var3) {
		this.xSlot = (int)(var1 / 16.0F);
		this.ySlot = (int)(var2 / 16.0F);
		this.zSlot = (int)(var3 / 16.0F);
		if(this.xSlot < 0) {
			this.xSlot = 0;
		}

		if(this.ySlot < 0) {
			this.ySlot = 0;
		}

		if(this.zSlot < 0) {
			this.zSlot = 0;
		}

		if(this.xSlot >= this.entityMap.xSlot) {
			this.xSlot = this.entityMap.xSlot - 1;
		}

		if(this.ySlot >= this.entityMap.ySlot) {
			this.ySlot = this.entityMap.ySlot - 1;
		}

		if(this.zSlot >= this.entityMap.zSlot) {
			this.zSlot = this.entityMap.zSlot - 1;
		}

		return this;
	}

	public final void add(Entity var1) {
		if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
			this.entityMap.entityGrid[(this.zSlot * this.entityMap.ySlot + this.ySlot) * this.entityMap.xSlot + this.xSlot].add(var1);
		}

	}

	public final void remove(Entity var1) {
		if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
			this.entityMap.entityGrid[(this.zSlot * this.entityMap.ySlot + this.ySlot) * this.entityMap.xSlot + this.xSlot].remove(var1);
		}

	}
}
