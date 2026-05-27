package net.minecraft.game.item;

import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

public class ItemHoe extends Item {
    public ItemHoe(int itemID, int damage) {
        super(itemID);
        this.maxStackSize = 1;
        this.maxDamage = 32 << damage;
    }

    public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int xCoord, int yCoord, int zCoord, int i7) {
        int i8 = world3.getBlockId(xCoord, yCoord, zCoord);
        Material material9 = world3.getBlockMaterial(xCoord, yCoord + 1, zCoord);
        if((material9.isSolid() || i8 != Block.grass.blockID) && i8 != Block.dirt.blockID) {
            return false;
        } else {
            Block block10 = Block.tilledField;
            world3.playSoundEffect((double)((float)xCoord + 0.5F), (double)((float)yCoord + 0.5F), (double)((float)zCoord + 0.5F), block10.stepSound.getStepSound(), (block10.stepSound.getVolume() + 1.0F) / 2.0F, block10.stepSound.getPitch() * 0.8F);
            world3.setBlockWithNotify(xCoord, yCoord, zCoord, block10.blockID);
            itemStack1.damageItem(1);
            if(world3.rand.nextInt(8) == 0 && i8 == Block.grass.blockID) {
                byte b11 = 1;

                for(int i12 = 0; i12 < b11; ++i12) {
                    float f13 = 0.7F;
                    float f14 = world3.rand.nextFloat() * f13 + (1.0F - f13) * 0.5F;
                    float f15 = 1.2F;
                    float f16 = world3.rand.nextFloat() * f13 + (1.0F - f13) * 0.5F;
                    EntityItem entityItem17 = new EntityItem(world3, (double)((float)xCoord + f14), (double)((float)yCoord + f15), (double)((float)zCoord + f16), new ItemStack(Item.seeds));
                    entityItem17.delayBeforeCanPickup = 10;
                    world3.spawnEntityInWorld(entityItem17);
                }
            }

            return true;
        }
    }
}
