package net.minecraft.game.entity.player;

import java.util.List;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public class EntityPlayer extends EntityLiving {
    public InventoryPlayer inventory = new InventoryPlayer();
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
            List var8 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0F, 0.0F, 1.0F));
            if(var8 != null) {
                for(int var9 = 0; var9 < var8.size(); ++var9) {
                    Entity var10 = (Entity)var8.get(var9);
                    if(var10 instanceof EntityItem) {
                        EntityItem var11 = (EntityItem)var10;
                        if(var11.delayBeforeCanPickup == 0) {
                            boolean var10000;
                            label60: {
                                ItemStack var6 = var11.item;
                                InventoryPlayer var5 = this.inventory;
                                Object var7 = null;
                                int var13;
                                if(var6.itemID > 0) {
                                    int var12 = var6.itemID;
                                    var13 = var5.getInventorySlotContainItem(var12);
                                    if(var13 < 0) {
                                        var13 = var5.getFirstEmptyStack();
                                    }

                                    if(var13 >= 0) {
                                        if(var5.mainInventory[var13] == null) {
                                            var5.mainInventory[var13] = new ItemStack(Block.blocksList[var12], 0);
                                        }

                                        if(var5.mainInventory[var13].stackSize < 99) {
                                            ++var5.mainInventory[var13].stackSize;
                                            var5.mainInventory[var13].animationsToGo = 5;
                                            var10000 = true;
                                            break label60;
                                        }
                                    }
                                } else {
                                    var13 = var5.getFirstEmptyStack();
                                    if(var13 >= 0) {
                                        var5.mainInventory[var13] = var6;
                                        var10000 = true;
                                        break label60;
                                    }
                                }

                                var10000 = false;
                            }

                            if(var10000) {
                                this.worldObj.releaseEntitySkin(var11);
                            }
                        }
                    }
                }
            }
        }

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
                var10000 = new ItemStack(var4.mainInventory[var2]);
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
        int var1 = this.inventory.currentItem;
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
                var10000 = new ItemStack(var4.mainInventory[var2]);
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
