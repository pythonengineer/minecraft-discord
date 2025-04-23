package net.minecraft.game.level;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.game.entity.AILiving;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.material.Material;

public final class MobSpawner {
	private World worldObj;

	public MobSpawner(World var1) {
		this.worldObj = var1;
	}

	public final void performSpawning() {
		int var1 = this.worldObj.width * this.worldObj.length * this.worldObj.height / 64 / 64 / 64;
		if(this.worldObj.random.nextInt(100) < var1 && this.worldObj.entitiesInLevelList(EntityLiving.class) < var1 * 20) {
			this.performSpawning(var1, this.worldObj.playerEntity, (LoadingScreenRenderer)null);
		}

	}

	public final int performSpawning(int var1, Entity var2, LoadingScreenRenderer var3) {
		int var19 = 0;

		for(int var4 = 0; var4 < var1; ++var4) {
			this.worldObj.random.nextInt(5);
			int var5 = this.worldObj.random.nextInt(this.worldObj.width);
			int var6 = (int)(Math.min(this.worldObj.random.nextFloat(), this.worldObj.random.nextFloat()) * (float)this.worldObj.height);
			int var7 = this.worldObj.random.nextInt(this.worldObj.length);
			if(!this.worldObj.isBlockNormalCube(var5, var6, var7) && this.worldObj.getBlockMaterial(var5, var6, var7) == Material.air && (!this.worldObj.isHalfLit(var5, var6, var7) || this.worldObj.random.nextInt(5) == 0)) {
				for(int var8 = 0; var8 < 8; ++var8) {
					int var9 = var5;
					int var10 = var6;
					int var11 = var7;

					for(int var12 = 0; var12 < 3; ++var12) {
						var9 += this.worldObj.random.nextInt(6) - this.worldObj.random.nextInt(6);
						var10 += this.worldObj.random.nextInt(1) - this.worldObj.random.nextInt(1);
						var11 += this.worldObj.random.nextInt(6) - this.worldObj.random.nextInt(6);
						if(var9 >= 0 && var11 >= 1 && var10 >= 0 && var10 < this.worldObj.height - 2 && var9 < this.worldObj.width && var11 < this.worldObj.length && this.worldObj.isBlockNormalCube(var9, var10 - 1, var11) && !this.worldObj.isBlockNormalCube(var9, var10, var11) && !this.worldObj.isBlockNormalCube(var9, var10 + 1, var11)) {
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
								if(var16 * var16 + var17 * var17 + var18 * var18 < 256.0F) {
									continue;
								}
							} else {
								var16 = var13 - (float)this.worldObj.xSpawn;
								var17 = var14 - (float)this.worldObj.ySpawn;
								var18 = var15 - (float)this.worldObj.zSpawn;
								if(var16 * var16 + var17 * var17 + var18 * var18 < 256.0F) {
									continue;
								}
							}

							EntityLiving var21 = new EntityLiving(this.worldObj);
							var18 = this.worldObj.random.nextFloat() * 360.0F;
							var21.prevPosX = var21.posX = var13;
							var21.prevPosY = var21.posY = var14;
							var21.prevPosZ = var21.posZ = var15;
							var21.rotationYaw = var18;
							var21.rotationPitch = 0.0F;
							var21.setPosition(var13, var14, var15);
							AILiving var20 = new AILiving();
							var21.entityAI = var20;
							if(this.worldObj.checkIfAABBIsClear(var21.boundingBox)) {
								++var19;
								this.worldObj.spawnEntityInWorld(var21);
							}
						}
					}
				}
			}
		}

		return var19;
	}
}
