package net.minecraft.game.item;

import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.StepSound;

public final class ItemHoe extends Item {
    public ItemHoe(int itemID, int damage) {
        super(itemID);
        this.maxStackSize = 1;
        this.maxDamage = 32 << damage;
    }

    public final boolean onItemUse(ItemStack stack, EntityPlayer playerEntity, World world, int x, int y, int z, int side) {
        int i10 = world.getBlockId(x, y, z);
        if((world.getBlockMaterial(x, y + 1, z).isSolid() || i10 != Block.grass.blockID) && i10 != Block.dirt.blockID) {
            return false;
        } else {
            Block block13 = Block.tilledField;
            double d10001 = (double)((float)x + 0.5F);
            double d10002 = (double)((float)y + 0.5F);
            double d10003 = (double)((float)z + 0.5F);
            String string10004 = block13.stepSound.getStepSound();
            StepSound stepSound8 = block13.stepSound;
            float f10005 = (block13.stepSound.stepSoundVolume + 1.0F) / 2.0F;
            stepSound8 = block13.stepSound;
            world.playSoundEffect(d10001, d10002, d10003, string10004, f10005, block13.stepSound.stepSoundPitch * 0.8F);
            world.setBlockWithNotify(x, y, z, block13.blockID);
            stack.damageItem(1);
            if(world.rand.nextInt(8) == 0 && i10 == Block.grass.blockID) {
                for(int i9 = 0; i9 <= 0; ++i9) {
                    float f11 = world.rand.nextFloat() * 0.7F + 0.15F;
                    float f14 = world.rand.nextFloat() * 0.7F + 0.15F;
                    EntityItem entityItem12;
                    (entityItem12 = new EntityItem(world, (double)((float)x + f11), (double)((float)y + 1.2F), (double)((float)z + f14), new ItemStack(Item.seeds))).delayBeforeCanPickup = 10;
                    world.spawnEntityInWorld(entityItem12);
                }
            }

            return true;
        }
    }
}