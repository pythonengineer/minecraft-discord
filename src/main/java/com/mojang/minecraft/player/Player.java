package com.mojang.minecraft.player;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;

public class Player extends Entity {
	private MovementInput input;
    public Inventory inventory = new Inventory();
    public byte userType = 0;

	public Player(Level level1, MovementInput movementInput2) {
		super(level1);
		this.heightOffset = 1.62F;
		this.input = movementInput2;
	}

	public void tick() {
		super.tick();
		boolean z1 = this.isInWater();
		boolean z2 = this.isInLava();
		this.input.updatePlayerMoveState();
		if(this.input.jumpHeld) {
			if(z1) {
				this.yd += 0.04F;
			} else if(z2) {
				this.yd += 0.04F;
			} else if(this.onGround && !this.input.jump) {
				this.yd = 0.42F;
				this.input.jump = true;
			}
		} else {
			this.input.jump = false;
		}

		float f3;
		if(z1) {
			f3 = this.y;
			this.moveRelative(this.input.moveStrafe, this.input.moveForward, 0.02F);
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.8F;
			this.yd *= 0.8F;
			this.zd *= 0.8F;
			this.yd = (float)((double)this.yd - 0.02D);
			if(this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6F - this.y + f3, this.zd)) {
				this.yd = 0.3F;
			}

		} else if(z2) {
			f3 = this.y;
			this.moveRelative(this.input.moveStrafe, this.input.moveForward, 0.02F);
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.5F;
			this.yd *= 0.5F;
			this.zd *= 0.5F;
			this.yd = (float)((double)this.yd - 0.02D);
			if(this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6F - this.y + f3, this.zd)) {
				this.yd = 0.3F;
			}

		} else {
			this.moveRelative(this.input.moveStrafe, this.input.moveForward, this.onGround ? 0.1F : 0.02F);
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.91F;
			this.yd *= 0.98F;
			this.zd *= 0.91F;
			this.yd = (float)((double)this.yd - 0.08D);
			if(this.onGround) {
                f3 = 0.6F;
                this.xd *= f3;
                this.zd *= f3;
			}

		}
	}

	public void releaseAllKeys() {
		this.input.releaseAllKeys();
	}

	public void setKey(int i1, boolean z2) {
		this.input.setKey(i1, z2);
	}
}
