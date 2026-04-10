package net.minecraft.game.item;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class ItemBucket extends Item {
    private int isFull;

    public ItemBucket(int itemID, int isFull) {
        super(itemID);
        this.maxStackSize = 1;
        this.maxDamage = 64;
        this.isFull = isFull;
    }

    public final ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer playerEntity) {
        float f4 = playerEntity.prevRotationPitch + (playerEntity.rotationPitch - playerEntity.prevRotationPitch);
        float f5 = playerEntity.prevRotationYaw + (playerEntity.rotationYaw - playerEntity.prevRotationYaw);
        double d6 = playerEntity.prevPosX + (playerEntity.posX - playerEntity.prevPosX);
        double d8 = playerEntity.prevPosY + (playerEntity.posY - playerEntity.prevPosY);
        double d10 = playerEntity.prevPosZ + (playerEntity.posZ - playerEntity.prevPosZ);
        Vec3D vec3D12 = new Vec3D(d6, d8, d10);
        float f17 = MathHelper.cos(-f5 * 0.017453292F - (float)Math.PI);
        f5 = MathHelper.sin(-f5 * 0.017453292F - (float)Math.PI);
        float f7 = -MathHelper.cos(-f4 * 0.017453292F);
        f4 = MathHelper.sin(-f4 * 0.017453292F);
        f5 *= f7;
        f17 *= f7;
        Vec3D vec3D14 = vec3D12.addVector((double)f5 * 5.0D, (double)f4 * 5.0D, (double)f17 * 5.0D);
        MovingObjectPosition movingObjectPosition13;
        if((movingObjectPosition13 = world.rayTraceBlocks_do(vec3D12, vec3D14, this.isFull == 0)) == null) {
            return stack;
        } else {
            if(movingObjectPosition13.typeOfHit == 0) {
                int i15 = movingObjectPosition13.blockX;
                int i16 = movingObjectPosition13.blockY;
                int i18 = movingObjectPosition13.blockZ;
                if(this.isFull == 0) {
                    if(world.getBlockMaterial(i15, i16, i18) == Material.water && world.getBlockMetadata(i15, i16, i18) == 0) {
                        world.setBlockWithNotify(i15, i16, i18, 0);
                        return new ItemStack(Item.bucketWater);
                    }

                    if(world.getBlockMaterial(i15, i16, i18) == Material.lava && world.getBlockMetadata(i15, i16, i18) == 0) {
                        world.setBlockWithNotify(i15, i16, i18, 0);
                        return new ItemStack(Item.bucketLava);
                    }
                } else {
                    if(movingObjectPosition13.sideHit == 0) {
                        --i16;
                    }

                    if(movingObjectPosition13.sideHit == 1) {
                        ++i16;
                    }

                    if(movingObjectPosition13.sideHit == 2) {
                        --i18;
                    }

                    if(movingObjectPosition13.sideHit == 3) {
                        ++i18;
                    }

                    if(movingObjectPosition13.sideHit == 4) {
                        --i15;
                    }

                    if(movingObjectPosition13.sideHit == 5) {
                        ++i15;
                    }

                    if(world.getBlockId(i15, i16, i18) == 0) {
                        world.setBlockWithNotify(i15, i16, i18, this.isFull);
                        return new ItemStack(Item.bucketEmpty);
                    }
                }
            }

            return stack;
        }
    }
}