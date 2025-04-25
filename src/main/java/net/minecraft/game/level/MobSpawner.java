package net.minecraft.game.level;

import net.minecraft.client.IProgressUpdate;
import net.minecraft.game.entity.AILiving;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.material.Material;

public class MobSpawner {
	private World a;

	public MobSpawner(World var1) {
		this.a = var1;
	}

	public final void performSpawning() {
		int var1 = this.a.width * this.a.length * this.a.height / 64 / 64 / 64;
		if(this.a.random.nextInt(100) < var1) {
			int var2 = this.a.entitiesInLevelList(EntityLiving.class);
			if(var2 < var1 * 20) {
				this.performSpawning(var1, this.a.playerEntity, null);
			}
		}

	}

	public final int performSpawning(int var1, Entity var2, IProgressUpdate var3) {
		int var19 = 0;

		for(int var4 = 0; var4 < var1; ++var4) {
			this.a.random.nextInt(5);
			int var5 = this.a.random.nextInt(this.a.width);
			int var6 = (int)(Math.min(this.a.random.nextFloat(), this.a.random.nextFloat()) * (float)this.a.height);
			int var7 = this.a.random.nextInt(this.a.length);
			if(!this.a.isBlockNormalCube(var5, var6, var7) && this.a.getBlockMaterial(var5, var6, var7) == Material.air && (!this.a.isHalfLit(var5, var6, var7) || this.a.random.nextInt(5) == 0)) {
				for(int var8 = 0; var8 < 8; ++var8) {
					int var9 = var5;
					int var10 = var6;
					int var11 = var7;

					for(int var12 = 0; var12 < 3; ++var12) {
						var9 += this.a.random.nextInt(6) - this.a.random.nextInt(6);
						var10 += this.a.random.nextInt(1) - this.a.random.nextInt(1);
						var11 += this.a.random.nextInt(6) - this.a.random.nextInt(6);
						if(var9 >= 0 && var11 > 0 && var10 >= 0 && var10 < this.a.height - 2 && var9 < this.a.width && var11 < this.a.length && this.a.isBlockNormalCube(var9, var10 - 1, var11) && !this.a.isBlockNormalCube(var9, var10, var11) && !this.a.isBlockNormalCube(var9, var10 + 1, var11)) {
							float var13 = (float)var9 + 0.5F;
							float var14 = (float)var10 + 1.0F;
							float var15 = (float)var11 + 0.5F;
							float var16;
							float var17;
							float var18;
							if(var2 != null) {
								var16 = var13 - var2.posX;
								var17 = var14 - var2.posY;
								var18 = var15 - var2.posZ;
								var16 = var16 * var16 + var17 * var17 + var18 * var18;
								if(var16 < 256.0F) {
									continue;
								}
							} else {
								var16 = var13 - (float)this.a.xSpawn;
								var17 = var14 - (float)this.a.ySpawn;
								var18 = var15 - (float)this.a.zSpawn;
								var16 = var16 * var16 + var17 * var17 + var18 * var18;
								if(var16 < 256.0F) {
									continue;
								}
							}

							EntityLiving var20 = new EntityLiving(this.a);
							var18 = this.a.random.nextFloat() * 360.0F;
							var20.setLocationAndAngles(var13, var14, var15, var18, 0.0F);
							var20.setEntityAI(new AILiving());
							if(this.a.checkIfAABBIsClear(var20.boundingBox)) {
								++var19;
								this.a.spawnEntityInWorld(var20);
							}
						}
					}
				}
			}
		}

		return var19;
	}
}
