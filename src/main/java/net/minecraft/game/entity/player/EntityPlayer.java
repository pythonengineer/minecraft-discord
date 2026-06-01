package net.minecraft.game.entity.player;

import com.mojang.nbt.NBTTagCompound;

import java.util.List;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.IInventory;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.monster.EntityMob;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.tileentity.TileEntityFurnace;
import net.minecraft.game.world.block.tileentity.TileEntitySign;
import net.minecraft.game.world.material.Material;

public class EntityPlayer extends EntityLiving {
	public InventoryPlayer inventory = new InventoryPlayer(this);
	public byte unusedMiningCooldown = 0;
	public int score = 0;
	public float prevCameraYaw;
	public float cameraYaw;
	protected String username;
	private int damageRemainder = 0;

	public EntityPlayer(World world1) {
		super(world1);
		this.setLocationAndAngles((double)world1.spawnX + 0.5D, (double)world1.spawnY, (double)world1.spawnZ + 0.5D, 0.0F, 0.0F);
		this.yOffset = 1.62F;
		this.health = 20;
		this.entityType = "humanoid";
		this.unusedRotation = 180.0F;
		this.fireResistance = 20;
		this.texture = "/char.png";
	}

    public void updateRidden() {
        super.updateRidden();
        this.prevCameraYaw = this.cameraYaw;
        this.cameraYaw = 0.0F;
    }

	public void preparePlayerToSpawn() {
		this.yOffset = 1.62F;
		this.setSize(0.6F, 1.8F);
		super.preparePlayerToSpawn();
		this.health = 20;
		this.deathTime = 0;
	}

	public void onLivingUpdate() {
		if(this.worldObj.difficultySetting == 0 && this.health < 20 && this.ticksExisted % 20 * 4 == 0) {
			this.heal(1);
		}

		this.inventory.decrementAnimations();
		this.prevCameraYaw = this.cameraYaw;
		super.onLivingUpdate();
		float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		float f2 = (float)Math.atan(-this.motionY * (double)0.2F) * 15.0F;
		if(f1 > 0.1F) {
			f1 = 0.1F;
		}

		if(!this.onGround || this.health <= 0) {
			f1 = 0.0F;
		}

		if(this.onGround || this.health <= 0) {
			f2 = 0.0F;
		}

		this.cameraYaw += (f1 - this.cameraYaw) * 0.4F;
		this.cameraPitch += (f2 - this.cameraPitch) * 0.8F;
		if(this.health > 0) {
			List list3 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0D, 0.0D, 1.0D));
			if(list3 != null) {
				for(int i4 = 0; i4 < list3.size(); ++i4) {
					this.collideWithPlayer((Entity)list3.get(i4));
				}
			}
		}

	}

	private void collideWithPlayer(Entity entity1) {
		entity1.onCollideWithPlayer(this);
	}

	public int getScore() {
		return this.score;
	}

	public void onDeath(Entity entity) {
		this.setSize(0.2F, 0.2F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionY = (double)0.1F;
		if(this.username.equals("Notch")) {
			this.dropPlayerItemWithRandomChoice(new ItemStack(Item.appleRed, 1), true);
		}

		this.inventory.dropAllItems();
		if(entity != null) {
			this.motionX = (double)(-MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F);
			this.motionZ = (double)(-MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F);
		} else {
			this.motionX = this.motionZ = 0.0D;
		}

		this.yOffset = 0.1F;
	}

    public void addToPlayerScore(Entity entity, int score) {
        this.score += score;
    }

	public void dropPlayerItem(ItemStack stack) {
		this.dropPlayerItemWithRandomChoice(stack, false);
	}

	public void dropPlayerItemWithRandomChoice(ItemStack stack, boolean isRandom) {
		if(stack != null) {
			EntityItem stack1;
			(stack1 = new EntityItem(this.worldObj, this.posX, this.posY - (double)0.3F, this.posZ, stack)).delayBeforeCanPickup = 40;
			float f3;
			float isRandom1;
			if(isRandom) {
				f3 = this.rand.nextFloat() * 0.5F;
				isRandom1 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				stack1.motionX = (double)(-MathHelper.sin(isRandom1) * f3);
				stack1.motionZ = (double)(MathHelper.cos(isRandom1) * f3);
				stack1.motionY = (double)0.2F;
			} else {
				stack1.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * 0.3F);
				stack1.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * 0.3F);
				stack1.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI) * 0.3F + 0.1F);
				f3 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				isRandom1 = 0.02F * this.rand.nextFloat();
				stack1.motionX += Math.cos((double)f3) * (double)isRandom1;
				stack1.motionY += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
				stack1.motionZ += Math.sin((double)f3) * (double)isRandom1;
			}

			this.worldObj.spawnEntityInWorld(stack1);
		}
	}

	public float getCurrentPlayerStrVsBlock(Block block1) {
		float f2 = this.inventory.getStrVsBlock(block1);
		if(this.isInsideOfMaterial(Material.water)) {
			f2 /= 5.0F;
		}

		if(!this.onGround) {
			f2 /= 5.0F;
		}

		return f2;
	}

	public boolean canHarvestBlock(Block block1) {
		return this.inventory.canHarvestBlock(block1);
	}

	public void readEntityFromNBT(NBTTagCompound compoundTag) {
		super.readEntityFromNBT(compoundTag);
	}

	public void writeEntityToNBT(NBTTagCompound compoundTag) {
		super.writeEntityToNBT(compoundTag);
	}

	public void displayGUIChest(IInventory inventory) {
	}

	public void displayWorkbenchGUI() {
	}

	public void onItemPickup(Entity entity) {
	}

	protected float getEyeHeight() {
		return 0.12F;
	}

	public boolean attackEntityFrom(Entity entity, int damage) {
		this.entityAge = 0;
		if(this.health <= 0) {
			return false;
		} else if((float)this.heartsLife > (float)this.heartsHalvesLife / 2.0F) {
			return false;
		} else {
			if(entity instanceof EntityMob || entity instanceof EntityArrow) {
				if(this.worldObj.difficultySetting == 0) {
					damage = 0;
				}

				if(this.worldObj.difficultySetting == 1) {
					damage = damage / 3 + 1;
				}

				if(this.worldObj.difficultySetting == 3) {
					damage = damage * 3 / 2;
				}
			}

			int i3 = 25 - this.inventory.getTotalArmorValue();
			int i4 = damage * i3 + this.damageRemainder;
			this.inventory.damageArmor(damage);
			damage = i4 / 25;
			this.damageRemainder = i4 % 25;
			return damage == 0 ? false : super.attackEntityFrom(entity, damage);
		}
	}

	public void displayGUIFurnace(TileEntityFurnace furnaceTileEntity) {
	}

	public void displayGUIEditSign(TileEntitySign signTileEntity) {
	}

    public void dropOneItem(boolean flag) {
        this.dropPlayerItem(this.inventory.decrStackSize(this.inventory.currentItem,
            flag && this.inventory.getCurrentItem() != null ? this.inventory.getCurrentItem().stackSize : 1));
    }

    public boolean getItemShouldUseOnTouchEagler() {
        ItemStack st = this.inventory.getCurrentItem();
        return st != null && st.getItem().shouldUseOnTouchEagler(st);
    }

	public int getPlayerArmorValue() {
		return this.inventory.getTotalArmorValue();
	}

	public void interactWithEntity(Entity entity1) {
	}
}
