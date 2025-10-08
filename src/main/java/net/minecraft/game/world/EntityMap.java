package net.minecraft.game.world;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;

public final class EntityMap {
	public int width;
	public int depth;
	public int height;
	private EntityMapSlot slot = new EntityMapSlot(this);
	private EntityMapSlot slot2 = new EntityMapSlot(this);
	public List[] entityGrid;
	private List entities;

	public EntityMap(int var1, int var2, int var3) {
		new ArrayList();
		this.entities = new ArrayList();
		this.width = 32;
		this.depth = 32;
		this.height = 32;
		if(this.width == 0) {
			this.width = 1;
		}

		if(this.depth == 0) {
			this.depth = 1;
		}

		if(this.height == 0) {
			this.height = 1;
		}

		this.entityGrid = new ArrayList[this.width * this.depth * this.height];

		for(var1 = 0; var1 < this.width; ++var1) {
			for(var2 = 0; var2 < this.depth; ++var2) {
				for(var3 = 0; var3 < this.height; ++var3) {
					this.entityGrid[(var3 * this.depth + var2) * this.width + var1] = new ArrayList();
				}
			}
		}

	}

	public final List getEntitiesWithinAABB(Entity var1, AxisAlignedBB var2) {
		this.entities.clear();
		if(var2 == null) {
			return this.entities;
		} else {
			List var3 = this.entities;
			double var15 = var2.maxZ;
			double var13 = var2.maxY;
			double var11 = var2.maxX;
			double var9 = var2.minZ;
			double var7 = var2.minY;
			double var5 = var2.minX;
			Entity var53 = var1;
			EntityMap var52 = this;
			EntityMapSlot var4 = this.slot.init(var5, var7, var9);
			EntityMapSlot var17 = this.slot2.init(var11, var13, var15);

			for(int var18 = EntityMapSlot.a(var4) - 1; var18 <= EntityMapSlot.a(var17) + 1; ++var18) {
				for(int var19 = EntityMapSlot.b(var4) - 1; var19 <= EntityMapSlot.b(var17) + 1; ++var19) {
					for(int var20 = EntityMapSlot.c(var4) - 1; var20 <= EntityMapSlot.c(var17) + 1; ++var20) {
						if(var18 >= 0 && var19 >= 0 && var20 >= 0 && var18 < var52.width && var19 < var52.depth && var20 < var52.height) {
							List var21 = var52.entityGrid[(var20 * var52.depth + var19) * var52.width + var18];

							for(int var22 = 0; var22 < var21.size(); ++var22) {
								Entity var23 = (Entity)var21.get(var22);
								if(var23 != var53) {
									AxisAlignedBB var24 = var23.boundingBox;
									if(var11 > var24.minX && var5 < var24.maxX ? (var13 > var24.minY && var7 < var24.maxY ? var15 > var24.minZ && var9 < var24.maxZ : false) : false) {
										var3.add(var23);
									}
								}
							}
						}
					}
				}
			}

			return var3;
		}
	}
}
