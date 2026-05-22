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

	protected final boolean canEntityBeSeen(Entity entity) {
		return this.worldObj.rayTraceBlocks(new Vec3D(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), new Vec3D(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ)) == null;
	}

	public void updatePlayerActionState() {
		this.hasAttacked = false;
        float f1;
        if(this.playerToAttack == null) {
            this.playerToAttack = this.findPlayerToAttack();
            if(this.playerToAttack != null) {
                this.pathToEntity = this.worldObj.getPathToEntity(this, this.playerToAttack, 16.0F);
            }
        } else if(!this.playerToAttack.isEntityAlive()) {
            this.playerToAttack = null;
        } else {
            Entity entity6 = this.playerToAttack;
            float f20 = (float)(this.playerToAttack.posX - super.posX);
            float f21 = (float)(entity6.posY - super.posY);
            float f22 = (float)(entity6.posZ - super.posZ);
            f1 = MathHelper.sqrt_float(f20 * f20 + f21 * f21 + f22 * f22);
            if(this.canEntityBeSeen(this.playerToAttack)) {
                this.attackEntity(this.playerToAttack, f1);
            }
        }

        int i31;
        if(this.hasAttacked || this.playerToAttack == null || this.pathToEntity != null && this.rand.nextInt(20) != 0) {
            if(this.pathToEntity == null || this.rand.nextInt(100) == 0) {
                i31 = -1;
                int i2 = -1;
                int i3 = -1;
                float f4 = -99999.0F;

                for(int i5 = 0; i5 < 50; ++i5) {
                    int i36 = MathHelper.floor_double(this.posX + (double)this.rand.nextInt(11) - 5.0D);
                    int i7 = MathHelper.floor_double(this.posY + (double)this.rand.nextInt(7) - 3.0D);
                    int i8 = MathHelper.floor_double(this.posZ + (double)this.rand.nextInt(11) - 5.0D);
                    float f9;
                    if((f9 = this.getBlockPathWeight(i36, i7, i8)) > f4) {
                        f4 = f9;
                        i31 = i36;
                        i2 = i7;
                        i3 = i8;
                    }
                }

                if(i31 > 0) {
                    this.pathToEntity = this.worldObj.getEntityPathToXYZ(this, i31, i2, i3, 16.0F);
                }
            }
        } else {
            this.pathToEntity = this.worldObj.getPathToEntity(this, this.playerToAttack, 16.0F);
        }

        i31 = MathHelper.floor_double(this.boundingBox.minY);
        boolean z32 = this.handleWaterMovement();
        boolean z33 = this.handleLavaMovement();
        if(this.pathToEntity != null && this.rand.nextInt(100) != 0) {
            Vec3D vec3D34 = this.pathToEntity.getPosition(this);
            float f35 = this.width * 2.0F;

            while(vec3D34 != null) {
                double d23 = this.posZ;
                double d39 = this.posY;
                double d19 = this.posX;
                double d25 = d19 - vec3D34.xCoord;
                double d27 = d39 - vec3D34.yCoord;
                double d29 = d23 - vec3D34.zCoord;
                if(d25 * d25 + d27 * d27 + d29 * d29 >= (double)(f35 * f35) || vec3D34.yCoord > (double)i31) {
                    break;
                }

                this.pathToEntity.incrementPathIndex();
                if(this.pathToEntity.isFinished()) {
                    vec3D34 = null;
                    this.pathToEntity = null;
                } else {
                    vec3D34 = this.pathToEntity.getPosition(this);
                }
            }

            this.isJumping = false;
            if(vec3D34 != null) {
                double d37 = vec3D34.xCoord - this.posX;
                double d38 = vec3D34.zCoord - this.posZ;
                double d10 = vec3D34.yCoord - (double)i31;
                this.rotationYaw = (float)(Math.atan2(d38, d37) * 180.0D / (double)(float)Math.PI) - 90.0F;
                this.moveForward = this.moveSpeed;
                if(this.hasAttacked && this.playerToAttack != null) {
                    double d12 = this.playerToAttack.posX - this.posX;
                    double d14 = this.playerToAttack.posZ - this.posZ;
                    f1 = this.rotationYaw;
                    this.rotationYaw = (float)(Math.atan2(d14, d12) * 180.0D / (double)(float)Math.PI) - 90.0F;
                    f1 = (f1 - this.rotationYaw + 90.0F) * (float)Math.PI / 180.0F;
                    this.moveStrafing = -MathHelper.sin(f1) * this.moveForward;
                    this.moveForward = MathHelper.cos(f1) * this.moveForward;
                }

                if(d10 != 0.0D) {
                    this.isJumping = true;
                }
            }

            if(this.rand.nextFloat() < 0.8F && (z32 || z33)) {
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