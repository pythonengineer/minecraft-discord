package net.minecraft.game.level;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;

public final class EntityMap {
	public int xSlot;
	public int ySlot;
	public int zSlot;
	EntityMapSlot slot0 = new EntityMapSlot(this);
	EntityMapSlot slot1 = new EntityMapSlot(this);
	public List[] entityGrid;
	public List entities = new ArrayList();
    List entitiesExcludingEntity = new ArrayList();

	public EntityMap(int var1, int var2, int var3) {
		this.xSlot = var1 / 16;
		this.ySlot = var2 / 16;
		this.zSlot = var3 / 16;
		if(this.xSlot == 0) {
			this.xSlot = 1;
		}

		if(this.ySlot == 0) {
			this.ySlot = 1;
		}

		if(this.zSlot == 0) {
			this.zSlot = 1;
		}

		this.entityGrid = new ArrayList[this.xSlot * this.ySlot * this.zSlot];

		for(var1 = 0; var1 < this.xSlot; ++var1) {
			for(var2 = 0; var2 < this.ySlot; ++var2) {
				for(var3 = 0; var3 < this.zSlot; ++var3) {
					this.entityGrid[(var3 * this.ySlot + var2) * this.xSlot + var1] = new ArrayList();
				}
			}
		}

	}

    public final List getEntitiesWithinAABBExcludingEntity(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7, List var8) {
        EntityMapSlot var9 = this.slot0.init(var2, var3, var4);
        EntityMapSlot var10 = this.slot1.init(var5, var6, var7);
        List var14 = null;

        for(int var11 = var9.xSlot - 1; var11 <= var10.xSlot + 1; ++var11) {
            for(int var12 = var9.ySlot - 1; var12 <= var10.ySlot + 1; ++var12) {
                for(int var13 = var9.zSlot - 1; var13 <= var10.zSlot + 1; ++var13) {
                    if(var11 >= 0 && var12 >= 0 && var13 >= 0 && var11 < this.xSlot && var12 < this.ySlot && var13 < this.zSlot) {
                        var14 = this.entityGrid[(var13 * this.ySlot + var12) * this.xSlot + var11];

                        for(int var15 = 0; var15 < var14.size(); ++var15) {
                            Entity var16 = (Entity)var14.get(var15);
                            if(var16 != var1) {
                                AxisAlignedBB var17 = null;
                                var17 = var16.boundingBox;
                                if(var5 > var17.x0 && var2 < var17.x1 ? (var6 > var17.y0 && var3 < var17.y1 ? var7 > var17.z0 && var4 < var17.z1 : false) : false) {
                                    var8.add(var16);
                                }
                            }
                        }
                    }
                }
            }
        }

		return var8;
	}

    public final List getEntitiesWithinAABBExcludingEntity(Entity var1, AxisAlignedBB var2) {
        this.entitiesExcludingEntity.clear();
        return this.getEntitiesWithinAABBExcludingEntity(var1, var2.x0, var2.y0, var2.z0, var2.x1, var2.y1, var2.z1, this.entitiesExcludingEntity);
    }
}
