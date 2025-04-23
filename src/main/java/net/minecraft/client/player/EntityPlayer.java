package net.minecraft.client.player;

import java.util.List;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.ImageData;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public class EntityPlayer extends EntityLiving {
	public transient MovementInput movementInput;
	public InventoryPlayer inventory = new InventoryPlayer();
	public float prevCameraYaw;
	public float cameraYaw;
	private int getScore = 0;
	public int getArrows = 20;
	private static int skinID = -1;
	public static ImageData skinData;

	public EntityPlayer(World var1) {
		super(var1);
		if(var1 != null) {
			var1.playerEntity = this;
			var1.releaseEntitySkin(this);
			var1.spawnEntityInWorld(this);
		}

		this.yOffset = 1.62F;
		this.health = 20;
		this.entityAI = new EntityPlayerInput(this);
	}

	public final void preparePlayerToSpawn() {
		this.yOffset = 1.62F;
		this.setSize(0.6F, 1.8F);
		super.preparePlayerToSpawn();
		if(this.worldObj != null) {
			this.worldObj.playerEntity = this;
		}

		this.health = 20;
		this.deathTime = 0;
	}

	public final void onLivingUpdate() {
		InventoryPlayer var1 = this.inventory;

		for(int var2 = 0; var2 < var1.animationsToGo.length; ++var2) {
			if(var1.animationsToGo[var2] > 0) {
				--var1.animationsToGo[var2];
			}
		}

		this.prevCameraYaw = this.cameraYaw;
		this.movementInput.updatePlayerMoveState();
		super.onLivingUpdate();
		float var4 = MathHelper.sqrt_float(this.motionX * this.motionX + this.motionZ * this.motionZ);
		float var5 = (float)Math.atan((double)(-this.motionY * 0.2F)) * 15.0F;
		if(var4 > 0.1F) {
			var4 = 0.1F;
		}

		if(!this.onGround || this.health <= 0) {
			var4 = 0.0F;
		}

		if(this.onGround || this.health <= 0) {
			var5 = 0.0F;
		}

		this.cameraYaw += (var4 - this.cameraYaw) * 0.4F;
		this.cameraPitch += (var5 - this.cameraPitch) * 0.8F;
		if(this.health > 0) {
			List var3 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0F, 0.0F, 1.0F));
			if(var3 != null) {
				for(int var6 = 0; var6 < var3.size(); ++var6) {
					var3.get(var6);
				}
			}
		}

	}

	public final void resetKeyState() {
		this.movementInput.resetKeyState();
	}

	public final void checkKeyForMovementInput(int var1, boolean var2) {
		this.movementInput.checkKeyForMovementInput(var1, var2);
	}

	public final int getScore() {
		return 0;
	}

	public final void onDeath(Entity var1) {
		this.setSize(0.2F, 0.2F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionY = 0.1F;
		if(var1 != null) {
			this.motionX = -MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F;
			this.motionZ = -MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F;
		} else {
			this.motionX = this.motionZ = 0.0F;
		}

		this.yOffset = 0.1F;
	}

	public final void remove() {
	}

	public static void setupSkinImage(RenderEngine var0) {
		if(skinData != null) {
			ImageData var2 = skinData;
			var0.singleIntBuffer.clear();
			GL11.glGenTextures(var0.singleIntBuffer);
			int var3 = var0.singleIntBuffer.get(0);
			var0.setupTexture(var2, var3);
			var0.textureContentsMap.put(Integer.valueOf(var3), var2);
			skinID = var3;
			skinData = null;
		}

		int var4;
		if(skinID < 0) {
			var4 = var0.getTexture("/char.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4);
		} else {
			var4 = skinID;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4);
		}
	}

    public boolean getItemShouldUseOnTouchEagler() {
        Block block;
        int blockId = this.inventory.getCurrentItem();
        if (blockId > 0 && ((block = Block.blocksList[blockId]) == Block.mushroomBrown || block == Block.mushroomRed)) {
            return true;
        } else {
            return false;
        }
    }
}
