package net.minecraft.game.entity.player;

import java.util.List;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.level.World;

public class EntityPlayer extends EntityLiving {
    public InventoryPlayer inventory = new InventoryPlayer();
    public byte userType = 0;
    public float prevCameraYaw;
    public float cameraYaw;
    private int getScore = 0;
    public int getArrows = 20;

    public EntityPlayer(World var1) {
        super(var1);
        if(var1 != null) {
            var1.playerEntity = this;
            var1.releaseEntitySkin(this);
            var1.spawnEntityInWorld(this);
        }

        this.yOffset = 1.62F;
        this.health = 20;
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
            List var5 = this.worldObj.a(this, this.boundingBox.expand(1.0F, 0.0F, 1.0F));
            if(var5 != null) {
                for(int var6 = 0; var6 < var5.size(); ++var6) {
                    Entity var7 = (Entity)var5.get(var6);
                    if(var7 instanceof EntityItem) {
                        EntityItem var8 = (EntityItem)var7;
                        if(var8.delayBeforeCanPickup == 0 && this.inventory.addItemStackToInventory(var8.item)) {
                            this.worldObj.playSoundEffect(var8, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                            this.worldObj.releaseEntitySkin(var8);
                        }
                    }
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

    public final void dropPlayerItemWithRandomChoice(int var1) {
        int var2 = var1;
        InventoryPlayer var4 = this.inventory;
        ItemStack var10000;
        if(var4.mainInventory[var2] != null) {
            if(var4.mainInventory[var2].stackSize == 1) {
                ItemStack var3 = var4.mainInventory[var2];
                var4.mainInventory[var2] = null;
                var10000 = var3;
            } else {
                --var4.mainInventory[var2].stackSize;
                var10000 = new ItemStack(var4.mainInventory[var2], 1);
            }
        } else {
            var10000 = null;
        }

        ItemStack var5 = var10000;
        if(var5 != null) {
            EntityItem var6 = new EntityItem(this.worldObj, this.posX, this.posY - 0.3F, this.posZ, var5);
            var6.delayBeforeCanPickup = 40;
            var6.motionX1 = MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.2F;
            var6.motionZ1 = -MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.2F;
            var6.motionY1 = 0.2F;
            float var7 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
            float var8 = this.rand.nextFloat() * 0.1F;
            var6.motionX1 = (float)((double)var6.motionX1 + Math.cos((double)var7) * (double)var8);
            var6.motionY1 += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
            var6.motionZ1 = (float)((double)var6.motionZ1 + Math.sin((double)var7) * (double)var8);
            this.worldObj.spawnEntityInWorld(var6);
        }
    }

    public final void dropOneItem(boolean flag) {
        int var1 = this.inventory.currentSlot;
        int var2 = var1;
        InventoryPlayer var4 = this.inventory;
        ItemStack var10000;
        if(var4.mainInventory[var2] != null) {
            if(var4.mainInventory[var2].stackSize == 1) {
                ItemStack var3 = var4.mainInventory[var2];
                var4.mainInventory[var2] = null;
                var10000 = var3;
            } else {
                --var4.mainInventory[var2].stackSize;
                var10000 = new ItemStack(var4.mainInventory[var2], 1);
            }
        } else {
            var10000 = null;
        }

        ItemStack var5 = var10000;
        if(var5 != null) {
            EntityItem var6 = new EntityItem(this.worldObj, this.posX, this.posY - 0.3F, this.posZ, var5);
            var6.delayBeforeCanPickup = 40;
            var6.motionX1 = MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.2F;
            var6.motionZ1 = -MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.2F;
            var6.motionY1 = 0.2F;
            float var7 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
            float var8 = this.rand.nextFloat() * 0.1F;
            var6.motionX1 = (float)((double)var6.motionX1 + Math.cos((double)var7) * (double)var8);
            var6.motionY1 += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
            var6.motionZ1 = (float)((double)var6.motionZ1 + Math.sin((double)var7) * (double)var8);
            this.worldObj.spawnEntityInWorld(var6);
        }
    }

    public boolean getItemShouldUseOnTouchEagler() {
        ItemStack st = this.inventory.getCurrentItem();
        return st != null && st.shouldUseOnTouchEagler();
    }
}
