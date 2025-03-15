package com.mojang.minecraft.player;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.model.PlayerModel;
import com.mojang.minecraft.renderer.Textures;

import java.util.List;

public class Player extends Mob {
	public static final int MAX_HEALTH = 20;
	private Input input;
	public Inventory inventory = new Inventory();
	public byte userType = 0;
	public float oBob;
	public float bob;
	public int score = 0;

	public Player(Level level, Input input) {
		super(level);
		level.player = this;
		level.removeEntity(this);
		level.addEntity(this);
		System.out.println(level.player);
		this.heightOffset = 1.62F;
		this.input = input;
		this.health = 20;
		this.model = new PlayerModel();
		this.rotOffs = 180.0F;
		this.ai = new PlayerInput(this, input);
	}

	public void resetPos() {
		this.heightOffset = 1.62F;
		this.setSize(0.6F, 1.8F);
		super.resetPos();
		this.level.player = this;
		this.health = 20;
		this.deathTime = 0;
	}

	public void aiStep() {
		Inventory inventory1 = this.inventory;

		int i2;
		for(i2 = 0; i2 < inventory1.popTime.length; ++i2) {
			if(inventory1.popTime[i2] > 0) {
				--inventory1.popTime[i2];
			}
		}

		this.oBob = this.bob;
		this.input.tick();
		super.aiStep();
		float f3 = (float)Math.sqrt((double)(this.xd * this.xd + this.zd * this.zd));
		float f4 = (float)Math.atan((double)(-this.yd * 0.2F)) * 15.0F;
		if(f3 > 0.1F) {
			f3 = 0.1F;
		}

		if(!this.onGround || this.health <= 0) {
			f3 = 0.0F;
		}

		if(this.onGround || this.health <= 0) {
			f4 = 0.0F;
		}

		this.bob += (f3 - this.bob) * 0.4F;
		this.tilt += (f4 - this.tilt) * 0.8F;
		List list5;
		if((list5 = this.level.findEntities(this, this.bb.grow(1.0F, 0.0F, 1.0F))) != null) {
			for(i2 = 0; i2 < list5.size(); ++i2) {
				((Entity)list5.get(i2)).playerTouch(this);
			}
		}

	}

	public void render(Textures texture, float translation) {
	}

	public void releaseAllKeys() {
		this.input.releaseAllKeys();
	}

	public void setKey(int key, boolean state) {
		this.input.setKey(key, state);
	}

	public boolean addResource(int index) {
		int i2;
		Inventory inventory3;
		if((i2 = (inventory3 = this.inventory).containsTileAt(index)) < 0) {
			i2 = inventory3.containsTileAt(-1);
		}

		if(i2 < 0) {
			return false;
		} else if(inventory3.count[i2] >= 99) {
			return false;
		} else {
			inventory3.slots[i2] = index;
			++inventory3.count[i2];
			inventory3.popTime[i2] = 5;
			return true;
		}
	}

	public int getScore() {
		return this.score;
	}

	public HumanoidModel getModel() {
		return (HumanoidModel)this.model;
	}

	public void die(Entity entity1) {
		this.setSize(0.2F, 0.2F);
		this.setPos(this.x, this.y, this.z);
		this.yd = 0.1F;
		if(entity1 != null) {
			this.xd = -((float)Math.cos((double)(this.hurtDir + this.yRot) * Math.PI / 180.0D)) * 0.1F;
			this.zd = -((float)Math.sin((double)(this.hurtDir + this.yRot) * Math.PI / 180.0D)) * 0.1F;
		} else {
			this.xd = this.zd = 0.0F;
		}

		this.heightOffset = 0.1F;
	}

	public void remove() {
	}

	public void awardKillScore(Entity entity1, int i2) {
		this.score += i2;
	}

    public boolean getItemShouldUseOnTouchEagler() {
        Tile tile;
        int tileId = this.inventory.getSelected();
        if (tileId > 0 && ((tile = Tile.tiles[tileId]) == Tile.mushroom1 || tile == Tile.mushroom2)) {
            return true;
        } else {
            return false;
        }
    }
}