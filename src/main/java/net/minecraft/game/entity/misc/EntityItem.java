package net.minecraft.game.entity.misc;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.ItemStack;
import net.minecraft.game.level.World;

public final class EntityItem extends Entity {
    public float motionX1;
    public float motionY1;
    public float motionZ1;
    public ItemStack item;
    private int unknownEntityItemInt;
    private int age = 0;
    public int delayBeforeCanPickup;

    public EntityItem(World var1, float var2, float var3, float var4, ItemStack var5) {
        super(var1);
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.bbHeight / 2.0F;
        this.setPosition(var2, var3, var4);
        this.item = var5;
        Math.random();
        this.motionX1 = (float)(Math.random() * (double)0.2F - (double)0.1F);
        this.motionY1 = 0.2F;
        this.motionZ1 = (float)(Math.random() * (double)0.2F - (double)0.1F);
        this.makeStepSound = false;
    }

    public final void onEntityUpdate() {
        if(this.delayBeforeCanPickup > 0) {
            --this.delayBeforeCanPickup;
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY1 -= 0.04F;
        this.moveEntity(this.motionX1, this.motionY1, this.motionZ1);
        this.motionX1 *= 0.98F;
        this.motionY1 *= 0.98F;
        this.motionZ1 *= 0.98F;
        if(this.onGround) {
            this.motionX1 *= 0.7F;
            this.motionZ1 *= 0.7F;
            this.motionY1 *= -0.5F;
        }

        ++this.unknownEntityItemInt;
        ++this.age;
        if(this.age >= 6000) {
            this.setEntityDead();
        }

    }
}
