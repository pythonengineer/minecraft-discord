package net.minecraft.game.world.terrain.generate;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.tileentity.TileEntityChest;
import net.minecraft.game.world.block.tileentity.TileEntityMobSpawner;
import net.minecraft.game.world.material.Material;

public final class WorldGenDungeons extends WorldGenerator {
    public final boolean generate(World world1, EaglercraftRandom random2, int i3, int i4, int i5) {
        int i6 = random2.nextInt(2) + 2;
        int i7 = random2.nextInt(2) + 2;
        int i8 = 0;

        int i9;
        int i10;
        int i11;
        for(i9 = i3 - i6 - 1; i9 <= i3 + i6 + 1; ++i9) {
            for(i10 = i4 - 1; i10 <= i4 + 3 + 1; ++i10) {
                for(i11 = i5 - i7 - 1; i11 <= i5 + i7 + 1; ++i11) {
                    Material material12 = world1.getBlockMaterial(i9, i10, i11);
                    if(i10 == i4 - 1 && !material12.isSolid()) {
                        return false;
                    }

                    if(i10 == i4 + 3 + 1 && !material12.isSolid()) {
                        return false;
                    }

                    if((i9 == i3 - i6 - 1 || i9 == i3 + i6 + 1 || i11 == i5 - i7 - 1 || i11 == i5 + i7 + 1) && i10 == i4 && world1.getBlockId(i9, i10, i11) == 0 && world1.getBlockId(i9, i10 + 1, i11) == 0) {
                        ++i8;
                    }
                }
            }
        }

        if(i8 > 0 && i8 <= 5) {
            for(i9 = i3 - i6 - 1; i9 <= i3 + i6 + 1; ++i9) {
                for(i10 = i4 + 3; i10 >= i4 - 1; --i10) {
                    for(i11 = i5 - i7 - 1; i11 <= i5 + i7 + 1; ++i11) {
                        if(i9 != i3 - i6 - 1 && i10 != i4 - 1 && i11 != i5 - i7 - 1 && i9 != i3 + i6 + 1 && i10 != i4 + 3 + 1 && i11 != i5 + i7 + 1) {
                            world1.setBlockWithNotify(i9, i10, i11, 0);
                        } else if(i10 >= 0 && !world1.getBlockMaterial(i9, i10 - 1, i11).isSolid()) {
                            world1.setBlockWithNotify(i9, i10, i11, 0);
                        } else if(world1.getBlockMaterial(i9, i10, i11).isSolid()) {
                            if(i10 == i4 - 1 && random2.nextInt(4) != 0) {
                                world1.setBlockWithNotify(i9, i10, i11, Block.cobblestoneMossy.blockID);
                            } else {
                                world1.setBlockWithNotify(i9, i10, i11, Block.cobblestone.blockID);
                            }
                        }
                    }
                }
            }

            int i14;
            label159:
            for(i9 = 0; i9 < 2; ++i9) {
                for(i10 = 0; i10 < 3; ++i10) {
                    i11 = i3 + random2.nextInt((i6 << 1) + 1) - i6;
                    i8 = i5 + random2.nextInt((i7 << 1) + 1) - i7;
                    if(world1.getBlockId(i11, i4, i8) == 0) {
                        i14 = 0;
                        if(world1.getBlockMaterial(i11 - 1, i4, i8).isSolid()) {
                            ++i14;
                        }

                        if(world1.getBlockMaterial(i11 + 1, i4, i8).isSolid()) {
                            ++i14;
                        }

                        if(world1.getBlockMaterial(i11, i4, i8 - 1).isSolid()) {
                            ++i14;
                        }

                        if(world1.getBlockMaterial(i11, i4, i8 + 1).isSolid()) {
                            ++i14;
                        }

                        if(i14 == 1) {
                            world1.setBlockWithNotify(i11, i4, i8, Block.chest.blockID);
                            TileEntityChest tileEntityChest13 = (TileEntityChest)world1.getBlockTileEntity(i11, i4, i8);
                            i10 = 0;

                            while(true) {
                                if(i10 >= 8) {
                                    continue label159;
                                }

                                ItemStack itemStack15;
                                if((itemStack15 = (i14 = random2.nextInt(10)) == 0 ? new ItemStack(Item.saddle) : (i14 == 1 ? new ItemStack(Item.ingotIron, random2.nextInt(4) + 1) : (i14 == 2 ? new ItemStack(Item.bread) : (i14 == 3 ? new ItemStack(Item.wheat, random2.nextInt(4) + 1) : (i14 == 4 ? new ItemStack(Item.gunpowder, random2.nextInt(4) + 1) : (i14 == 5 ? new ItemStack(Item.silk, random2.nextInt(4) + 1) : (i14 == 6 ? new ItemStack(Item.bucketEmpty) : (i14 == 7 && random2.nextInt(100) == 0 ? new ItemStack(Item.appleGold) : null)))))))) != null) {
                                    tileEntityChest13.setInventorySlotContents(random2.nextInt(tileEntityChest13.getSizeInventory()), itemStack15);
                                }

                                ++i10;
                            }
                        }
                    }
                }
            }

            world1.setBlockWithNotify(i3, i4, i5, Block.mobSpawner.blockID);
            ((TileEntityMobSpawner)world1.getBlockTileEntity(i3, i4, i5)).mobID = (i14 = random2.nextInt(4)) == 0 ? "Skeleton" : (i14 == 1 ? "Zombie" : (i14 == 2 ? "Zombie" : (i14 == 3 ? "Spider" : "")));
            return true;
        } else {
            return false;
        }
    }
}