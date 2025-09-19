package net.minecraft.game.level;

import net.minecraft.client.IProgressUpdate;
import net.minecraft.game.entity.AILiving;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.animal.EntityPig;
import net.minecraft.game.entity.animal.EntitySheep;
import net.minecraft.game.entity.monster.EntityCreeper;
import net.minecraft.game.entity.monster.EntitySkeleton;
import net.minecraft.game.entity.monster.EntitySpider;
import net.minecraft.game.entity.monster.EntityZombie;
import net.minecraft.game.level.material.Material;

public class MobSpawner {
	private World worldObj;

	public MobSpawner(World var1) {
		this.worldObj = var1;
	}

	public final void performSpawning() {
		int var1 = this.worldObj.width * this.worldObj.length * this.worldObj.height / 64 / 64 / 64;
		if(this.worldObj.random.nextInt(100) < var1) {
			int var2 = this.worldObj.entitiesInLevelList(EntityLiving.class);
			if(var2 < var1 * 20) {
				this.performSpawning(var1, this.worldObj.playerEntity, (IProgressUpdate)null);
			}
		}

	}

    public final int performSpawning(int var1, Entity var2, IProgressUpdate var3) {
        int var19 = 0;

        for(int var4 = 0; var4 < var1; ++var4) {
            this.worldObj.random.nextInt(6);
            int var6 = this.worldObj.random.nextInt(this.worldObj.width);
            int var7 = (int)(Math.min(this.worldObj.random.nextFloat(), this.worldObj.random.nextFloat()) * (float)this.worldObj.height);
            int var8 = this.worldObj.random.nextInt(this.worldObj.length);
            if(!this.worldObj.isBlockNormalCube(var6, var7, var8) && this.worldObj.getBlockMaterial(var6, var7, var8) == Material.air && (!this.worldObj.isHalfLit(var6, var7, var8) || this.worldObj.random.nextInt(5) == 0)) {
                for(int var9 = 0; var9 < 8; ++var9) {
                    int var10 = var6;
                    int var11 = var7;
                    int var12 = var8;

                    for(int var13 = 0; var13 < 3; ++var13) {
                        var10 += this.worldObj.random.nextInt(6) - this.worldObj.random.nextInt(6);
                        var11 += this.worldObj.random.nextInt(1) - this.worldObj.random.nextInt(1);
                        var12 += this.worldObj.random.nextInt(6) - this.worldObj.random.nextInt(6);
                        if(var10 >= 0 && var12 > 0 && var11 >= 0 && var11 < this.worldObj.height - 2 && var10 < this.worldObj.width && var12 < this.worldObj.length && this.worldObj.isBlockNormalCube(var10, var11 - 1, var12) && !this.worldObj.isBlockNormalCube(var10, var11, var12) && !this.worldObj.isBlockNormalCube(var10, var11 + 1, var12)) {
                            float var14 = (float)var10 + 0.5F;
                            float var15 = (float)var11 + 1.0F;
                            float var16 = (float)var12 + 0.5F;
                            float var5;
                            float var17;
                            float var18;
                            if(var2 != null) {
                                var17 = var14 - var2.posX;
                                var5 = var15 - var2.posY;
                                var18 = var16 - var2.posZ;
                                var5 = var17 * var17 + var5 * var5 + var18 * var18;
                                if(var5 < 256.0F) {
                                    continue;
                                }
                            } else {
                                var17 = var14 - (float)this.worldObj.xSpawn;
                                var5 = var15 - (float)this.worldObj.ySpawn;
                                var18 = var16 - (float)this.worldObj.zSpawn;
                                var5 = var17 * var17 + var5 * var5 + var18 * var18;
                                if(var5 < 256.0F) {
                                    continue;
                                }
                            }

                            Object var21 = null;
                            int var20 = (int)(Math.random() * 7.0D);
                            if(var20 == 0) {
                                var21 = new EntitySkeleton(this.worldObj);
                            }

                            if(var20 == 1) {
                                var21 = new EntityPig(this.worldObj);
                            }

                            if(var20 == 2) {
                                var21 = new EntityCreeper(this.worldObj);
                            }

                            if(var20 == 3) {
                                var21 = new EntitySpider(this.worldObj);
                            }

                            if(var20 == 4) {
                                var21 = new EntitySheep(this.worldObj);
                            }

                            if(var20 == 6) {
                                var21 = new EntityZombie(this.worldObj);
                            }

                            if(var21 != null) {
                                var18 = this.worldObj.random.nextFloat() * 360.0F;
                                ((EntityLiving)var21).setPositionAndRotation(var14, var15, var16, var18, 0.0F);
                                ((EntityLiving)var21).setEntityAI(new AILiving());
                                if(var21 != null && this.worldObj.checkIfAABBIsClear1(((EntityLiving)var21).boundingBox)) {
                                    ++var19;
                                    this.worldObj.spawnEntityInWorld((Entity)var21);
                                }
                            }
                        }
                    }
                }
            }
        }

        return var19;
    }
}
