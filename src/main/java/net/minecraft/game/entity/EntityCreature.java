package net.minecraft.game.entity;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.path.PathEntity;

public class EntityCreature extends EntityLiving {
	private PathEntity pathToEntity;
	protected Entity entityToAttack;
	protected boolean hasAttacked = false;

	public EntityCreature(World world1) {
		super(world1);
	}

	protected boolean updateEntityActionState(Entity entity) {
		return this.worldObj.rayTraceBlocks(Vec3D.createVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), Vec3D.createVector(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ)) == null;
	}

	protected void updateEntityActionState() {
		this.hasAttacked = false;
		float f1 = 16.0F;
		if(this.entityToAttack == null) {
			this.entityToAttack = this.findPlayerToAttack();
			if(this.entityToAttack != null) {
				this.pathToEntity = this.worldObj.getPathToEntity(this, this.entityToAttack, f1);
			}
		} else if(!this.entityToAttack.isEntityAlive()) {
			this.entityToAttack = null;
		} else {
			float f2 = this.entityToAttack.getDistanceToEntity(this);
			if(this.updateEntityActionState(this.entityToAttack)) {
				this.attackEntity(this.entityToAttack, f2);
			}
		}

		int i19;
		if(!this.hasAttacked && this.entityToAttack != null && (this.pathToEntity == null || this.rand.nextInt(20) == 0)) {
			this.pathToEntity = this.worldObj.getPathToEntity(this, this.entityToAttack, f1);
        } else if(this.pathToEntity == null && this.rand.nextInt(100) == 0 || this.rand.nextInt(100) == 0) {
			i19 = -1;
			int i3 = -1;
			int i4 = -1;
			float f5 = -99999.0F;

			for(int i6 = 0; i6 < 50; ++i6) {
                int i7 = MathHelper.floor_double(this.posX + (double)this.rand.nextInt(13) - 6.0D);
                int i8 = MathHelper.floor_double(this.posY + (double)this.rand.nextInt(7) - 3.0D);
                int i9 = MathHelper.floor_double(this.posZ + (double)this.rand.nextInt(13) - 6.0D);
				float f10 = this.getBlockPathWeight(i7, i8, i9);
				if(f10 > f5) {
					f5 = f10;
					i19 = i7;
					i3 = i8;
					i4 = i9;
				}
			}

			if(i19 > 0) {
				this.pathToEntity = this.worldObj.getEntityPathToXYZ(this, i19, i3, i4, 10.0F);
			}
		}

		int i21 = MathHelper.floor_double(this.boundingBox.minY);
		boolean z20 = this.handleWaterMovement();
		boolean z21 = this.handleLavaMovement();
        this.rotationPitch = 0.0F;
		if(this.pathToEntity != null && this.rand.nextInt(100) != 0) {
            Vec3D vec3D24 = this.pathToEntity.getPosition(this);
            double d25 = (double)(this.width * 2.0F);

            while(vec3D24 != null && vec3D24.squareDistanceTo(this.posX, vec3D24.yCoord, this.posZ) < d25 * d25) {
                this.pathToEntity.incrementPathIndex();
                if(this.pathToEntity.isFinished()) {
                    vec3D24 = null;
                    this.pathToEntity = null;
                } else {
                    vec3D24 = this.pathToEntity.getPosition(this);
                }
            }

            this.isJumping = false;
            if(vec3D24 != null) {
                double d26 = vec3D24.xCoord - this.posX;
                double d27 = vec3D24.zCoord - this.posZ;
                double d12 = vec3D24.yCoord - (double)i21;
                float f14 = (float)(Math.atan2(d27, d26) * 180.0D / (double)(float)Math.PI) - 90.0F;
                float f15 = f14 - this.rotationYaw;

                for(this.moveForward = this.moveSpeed; f15 < -180.0F; f15 += 360.0F) {
                }

                while(f15 >= 180.0F) {
                    f15 -= 360.0F;
                }

                if(f15 > 30.0F) {
                    f15 = 30.0F;
                }

                if(f15 < -30.0F) {
                    f15 = -30.0F;
                }

                this.rotationYaw += f15;
                if(this.hasAttacked && this.entityToAttack != null) {
                    double d16 = this.entityToAttack.posX - this.posX;
                    double d18 = this.entityToAttack.posZ - this.posZ;
                    float f20 = this.rotationYaw;
                    this.rotationYaw = (float)(Math.atan2(d18, d16) * 180.0D / (double)(float)Math.PI) - 90.0F;
                    f15 = (f20 - this.rotationYaw + 90.0F) * (float)Math.PI / 180.0F;
                    this.moveStrafing = -MathHelper.sin(f15) * this.moveForward * 1.0F;
                    this.moveForward = MathHelper.cos(f15) * this.moveForward * 1.0F;
                }

                if(d12 > 0.0D) {
                    this.isJumping = true;
                }
            }

            if(this.entityToAttack != null) {
                this.faceEntity(this.entityToAttack, 30.0F);
            }

            if(this.isCollidedHorizontally) {
                this.isJumping = true;
            }

            if(this.rand.nextFloat() < 0.8F && (z20 || z21)) {
                this.isJumping = true;
            }

		} else {
			super.updateEntityActionState();
			this.pathToEntity = null;
		}
	}

	protected void attackEntity(Entity entity, float damage) {
	}

	protected float getBlockPathWeight(int x, int y, int z) {
		return 0.0F;
	}

	protected Entity findPlayerToAttack() {
		return null;
	}

	public boolean getCanSpawnHere(double x, double y, double z) {
		return super.getCanSpawnHere(x, y, z) && this.getBlockPathWeight((int)x, (int)y, (int)z) >= 0.0F;
	}
}
