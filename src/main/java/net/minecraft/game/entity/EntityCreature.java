package net.minecraft.game.entity;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.path.PathEntity;

public class EntityCreature extends EntityLiving {
	private PathEntity pathToEntity;
	protected Entity playerToAttack;
	protected boolean hasAttacked = false;

	public EntityCreature(World world1) {
		super(world1);
	}

	protected boolean canEntityBeSeen(Entity entity) {
		return this.worldObj.rayTraceBlocks(Vec3D.createVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), Vec3D.createVector(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ)) == null;
	}

	protected void updatePlayerActionState() {
		this.hasAttacked = false;
		float f1 = 16.0F;
		if(this.playerToAttack == null) {
			this.playerToAttack = this.findPlayerToAttack();
			if(this.playerToAttack != null) {
				this.pathToEntity = this.worldObj.getPathToEntity(this, this.playerToAttack, f1);
			}
		} else if(!this.playerToAttack.isEntityAlive()) {
			this.playerToAttack = null;
		} else {
			float f2 = this.playerToAttack.getDistanceToEntity(this);
			if(this.canEntityBeSeen(this.playerToAttack)) {
				this.attackEntity(this.playerToAttack, f2);
			}
		}

		int i19;
		if(!this.hasAttacked && this.playerToAttack != null && (this.pathToEntity == null || this.rand.nextInt(20) == 0)) {
			this.pathToEntity = this.worldObj.getPathToEntity(this, this.playerToAttack, f1);
		} else if(this.pathToEntity == null || this.rand.nextInt(100) == 0) {
			i19 = -1;
			int i3 = -1;
			int i4 = -1;
			float f5 = -99999.0F;

			for(int i6 = 0; i6 < 50; ++i6) {
				int i7 = MathHelper.floor_double(this.posX + (double)this.rand.nextInt(11) - 5.0D);
				int i8 = MathHelper.floor_double(this.posY + (double)this.rand.nextInt(7) - 3.0D);
				int i9 = MathHelper.floor_double(this.posZ + (double)this.rand.nextInt(11) - 5.0D);
				float f10 = this.getBlockPathWeight(i7, i8, i9);
				if(f10 > f5) {
					f5 = f10;
					i19 = i7;
					i3 = i8;
					i4 = i9;
				}
			}

			if(i19 > 0) {
				this.pathToEntity = this.worldObj.getEntityPathToXYZ(this, i19, i3, i4, f1);
			}
		}

		i19 = MathHelper.floor_double(this.boundingBox.minY);
		boolean z20 = this.handleWaterMovement();
		boolean z21 = this.handleLavaMovement();
		if(this.pathToEntity != null && this.rand.nextInt(100) != 0) {
			Vec3D vec3D22 = this.pathToEntity.getPosition(this);
			float f23 = this.width * 2.0F;

			while(vec3D22 != null && vec3D22.squareDistanceTo(this.posX, this.posY, this.posZ) < (double)(f23 * f23) && vec3D22.yCoord <= (double)i19) {
				this.pathToEntity.incrementPathIndex();
				if(this.pathToEntity.isFinished()) {
					vec3D22 = null;
					this.pathToEntity = null;
				} else {
					vec3D22 = this.pathToEntity.getPosition(this);
				}
			}

			this.isJumping = false;
			if(vec3D22 != null) {
				double d24 = vec3D22.xCoord - this.posX;
				double d25 = vec3D22.zCoord - this.posZ;
				double d11 = vec3D22.yCoord - (double)i19;
				this.rotationYaw = (float)(Math.atan2(d25, d24) * 180.0D / (double)(float)Math.PI) - 90.0F;
				this.moveForward = this.moveSpeed;
				if(this.hasAttacked && this.playerToAttack != null) {
					double d13 = this.playerToAttack.posX - this.posX;
					double d15 = this.playerToAttack.posZ - this.posZ;
					float f17 = this.rotationYaw;
					this.rotationYaw = (float)(Math.atan2(d15, d13) * 180.0D / (double)(float)Math.PI) - 90.0F;
					float f18 = (f17 - this.rotationYaw + 90.0F) * (float)Math.PI / 180.0F;
					this.moveStrafing = -MathHelper.sin(f18) * this.moveForward * 1.0F;
					this.moveForward = MathHelper.cos(f18) * this.moveForward * 1.0F;
				}

				if(d11 != 0.0D) {
					this.isJumping = true;
				}
			}

			if(this.rand.nextFloat() < 0.8F && (z20 || z21)) {
				this.isJumping = true;
			}

		} else {
			super.updatePlayerActionState();
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
