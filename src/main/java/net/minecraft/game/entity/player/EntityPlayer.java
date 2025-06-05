package net.minecraft.game.entity.player;

import com.mojang.nbt.NBTTagCompound;
import java.util.List;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.IInventory;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public class EntityPlayer extends EntityLiving {
    public InventoryPlayer inventory = new InventoryPlayer();
    public byte unusedByte = 0;
    public float prevCameraYaw;
    public float cameraYaw;
    public int getScore = 0;

    public EntityPlayer(World var1) {
        super(var1);
        if(var1 != null) {
            var1.playerEntity = this;
            var1.releaseEntitySkin(this);
        }

        this.yOffset = 1.62F;
        this.health = 20;
        this.fireResistance = 20;
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

    public void onLivingUpdate() {
        InventoryPlayer var3 = this.inventory;

        for(int var4 = 0; var4 < var3.mainInventory.length; ++var4) {
            if(var3.mainInventory[var4] != null && var3.mainInventory[var4].animationsToGo > 0) {
                --var3.mainInventory[var4].animationsToGo;
            }
        }

        this.prevCameraYaw = this.cameraYaw;
        super.onLivingUpdate();
        float var1 = MathHelper.sqrt_float(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float var2 = (float)Math.atan((double)(-this.motionY * 0.2F)) * 15.0F;
        if(var1 > 0.1F) {
            var1 = 0.1F;
        }

        if(!this.onGround || this.health <= 0) {
            var1 = 0.0F;
        }

        if(this.onGround || this.health <= 0) {
            var2 = 0.0F;
        }

        this.cameraYaw += (var1 - this.cameraYaw) * 0.4F;
        this.cameraPitch += (var2 - this.cameraPitch) * 0.8F;
        if(this.health > 0) {
            List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0F, 0.0F, 1.0F));
            if(var5 != null) {
                for(int var6 = 0; var6 < var5.size(); ++var6) {
                    Entity var7 = (Entity)var5.get(var6);
                    var7.onCollideWithPlayer(this);
                }
            }
        }

    }

    public final int getScore() {
        return this.getScore;
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

    public final void setEntityDead() {
    }

    public final void dropPlayerItemWithRandomChoice(ItemStack var1) {
        if(var1 != null) {
            EntityItem var4 = new EntityItem(this.worldObj, this.posX, this.posY - 0.3F, this.posZ, var1);
            var4.delayBeforeCanPickup = 40;
            var4.motionX = MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.2F;
            var4.motionZ = -MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.2F;
            var4.motionY = 0.2F;
            float var2 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
            float var3 = this.rand.nextFloat() * 0.1F;
            var4.motionX = (float)((double)var4.motionX + Math.cos((double)var2) * (double)var3);
            var4.motionY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
            var4.motionZ = (float)((double)var4.motionZ + Math.sin((double)var2) * (double)var3);
            this.worldObj.spawnEntityInWorld(var4);
        }
    }

    public final float getStrVsBlock(Block var1) {
        Block var2 = var1;
        InventoryPlayer var4 = this.inventory;
        float var3 = 1.0F;
        if(var4.mainInventory[var4.currentItem] != null) {
            ItemStack var5 = var4.mainInventory[var4.currentItem];
            var3 = 1.0F * var5.getItem().getStrVsBlock(var2);
        }

        return var3;
    }

    protected void writeEntityToNBT(NBTTagCompound var1) {
    }

    protected String getEntityString() {
        return null;
    }

    public void displayGUIChest(IInventory var1) {
    }

    public final void dropOneItem(boolean flag) {
        this.dropPlayerItemWithRandomChoice(this.inventory.decrStackSize(this.inventory.currentItem,
            flag && this.inventory.getCurrentItem() != null ? this.inventory.getCurrentItem().stackSize : 1));
    }

    public boolean getItemShouldUseOnTouchEagler() {
        ItemStack st = this.inventory.getCurrentItem();
        return st != null && st.getItem().shouldUseOnTouchEagler(st);
    }
}
