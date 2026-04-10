package net.minecraft.game.entity;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.path.PathEntity;

public class EntityCreature extends EntityLiving {
	private PathEntity pathToEntity;
	protected Entity playerToAttack;
	protected boolean powered = false;

	public EntityCreature(World world1) {
		super(world1);
	}

	protected final boolean getClosestPlayerToEntity(Entity entity) {
		return this.worldObj.rayTraceBlocks(new Vec3D(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), new Vec3D(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ)) == null;
	}

	protected void updatePlayerActionState() {
		this.powered = false;
		if(this.playerToAttack == null) {
			this.playerToAttack = this.findPlayerToAttack();
			if(this.playerToAttack != null) {
				this.pathToEntity = this.worldObj.pathFinder.createEntityPathTo(this, this.playerToAttack, 16.0F);
			}
		} else if(!this.playerToAttack.isEntityAlive()) {
			this.playerToAttack = null;
		} else {
			Entity entity5 = this.playerToAttack;
			float f13 = (float)(this.playerToAttack.posX - super.posX);
			float f14 = (float)(entity5.posY - super.posY);
			float f15 = (float)(entity5.posZ - super.posZ);
			float f1 = MathHelper.sqrt_float(f13 * f13 + f14 * f14 + f15 * f15);
			if(this.getClosestPlayerToEntity(this.playerToAttack)) {
				this.attackEntity(this.playerToAttack, f1);
			}
		}

		if(this.powered) {
			this.moveStrafing = 0.0F;
			this.moveForward = 0.0F;
			this.isJumping = false;
		} else {
			float f4;
			if(this.playerToAttack != null && (this.pathToEntity == null || this.rand.nextInt(20) == 0)) {
				this.pathToEntity = this.worldObj.pathFinder.createEntityPathTo(this, this.playerToAttack, 16.0F);
			} else if(this.pathToEntity == null || this.rand.nextInt(100) == 0) {
				int i24 = -1;
				int i2 = -1;
				int i3 = -1;
				f4 = -99999.0F;

				for(int i28 = 0; i28 < 200; ++i28) {
					int i6 = MathHelper.floor_double(this.posX + (double)this.rand.nextInt(21) - 10.0D);
					int i7 = MathHelper.floor_double(this.posY + (double)this.rand.nextInt(9) - 4.0D);
					int i8 = MathHelper.floor_double(this.posZ + (double)this.rand.nextInt(21) - 10.0D);
					float f9;
					if((f9 = this.getBlockPathWeight(i6, i7, i8)) > f4) {
						f4 = f9;
						i24 = i6;
						i2 = i7;
						i3 = i8;
					}
				}

				if(i24 > 0) {
					this.pathToEntity = this.worldObj.pathFinder.createEntityPathTo(this, i24, i2, i3, 16.0F);
				}
			}

			boolean z25 = this.handleWaterMovement();
			boolean z26 = this.handleLavaMovement();
			if(this.pathToEntity != null && this.rand.nextInt(100) != 0) {
				Vec3D vec3D27 = this.pathToEntity.getPosition(this);
				f4 = this.width * 2.0F;

				while(vec3D27 != null) {
					double d16 = this.posZ;
					double d32 = this.posY;
					double d12 = this.posX;
					double d18 = d12 - vec3D27.xCoord;
					double d20 = d32 - vec3D27.yCoord;
					double d22 = d16 - vec3D27.zCoord;
					if(d18 * d18 + d20 * d20 + d22 * d22 >= (double)(f4 * f4) || vec3D27.yCoord > this.posY) {
						break;
					}

					this.pathToEntity.incrementPathIndex();
					if(this.pathToEntity.isFinished()) {
						vec3D27 = null;
						this.pathToEntity = null;
					} else {
						vec3D27 = this.pathToEntity.getPosition(this);
					}
				}

				this.isJumping = false;
				if(vec3D27 != null) {
					double d29 = vec3D27.xCoord - this.posX;
					double d30 = vec3D27.zCoord - this.posZ;
					double d31 = vec3D27.yCoord - this.posY;
					this.rotationYaw = (float)(Math.atan2(d30, d29) * 180.0D / (double)(float)Math.PI) - 90.0F;
					this.moveForward = this.moveSpeed;
					if(d31 > 0.0D) {
						this.isJumping = true;
					}
				}

				if(this.rand.nextFloat() < 0.8F && (z25 || z26)) {
					this.isJumping = true;
				}

			} else {
				super.updatePlayerActionState();
				this.pathToEntity = null;
			}
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

	public boolean getCanSpawnHere(float x, float y, float z) {
		return super.getCanSpawnHere(x, y, z) && this.getBlockPathWeight((int)x, (int)y, (int)z) >= 0.0F;
	}
}