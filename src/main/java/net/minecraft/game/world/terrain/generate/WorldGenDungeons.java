package net.minecraft.game.world.terrain.generate;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.tileentity.TileEntityChest;
import net.minecraft.game.world.block.tileentity.TileEntityMobSpawner;
import net.minecraft.game.world.material.Material;

public class WorldGenDungeons extends WorldGenerator {
    public boolean generate(World world1, EaglercraftRandom random2, int i3, int i4, int i5) {
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

                                ItemStack itemStack15 = this.pickCheckLootItem(random2);
                                if(itemStack15 != null) {
                                    tileEntityChest13.setInventorySlotContents(random2.nextInt(27), itemStack15);
                                }

                                ++i10;
                            }
                        }
                    }
                }
            }

            world1.setBlockWithNotify(i3, i4, i5, Block.mobSpawner.blockID);
            TileEntityMobSpawner tileEntityMobSpawner19 = (TileEntityMobSpawner)world1.getBlockTileEntity(i3, i4, i5);
            tileEntityMobSpawner19.mobID = this.pickMobSpawner(random2);
            return true;
        } else {
            return false;
        }
    }

    private ItemStack pickCheckLootItem(EaglercraftRandom rand) {
        int i2 = rand.nextInt(10);
        return i2 == 0 ? new ItemStack(Item.saddle) : (i2 == 1 ? new ItemStack(Item.ingotIron, rand.nextInt(4) + 1) : (i2 == 2 ? new ItemStack(Item.bread) : (i2 == 3 ? new ItemStack(Item.wheat, rand.nextInt(4) + 1) : (i2 == 4 ? new ItemStack(Item.gunpowder, rand.nextInt(4) + 1) : (i2 == 5 ? new ItemStack(Item.silk, rand.nextInt(4) + 1) : (i2 == 6 ? new ItemStack(Item.bucketEmpty) : (i2 == 7 && rand.nextInt(100) == 0 ? new ItemStack(Item.appleGold) : null)))))));
    }

    private String pickMobSpawner(EaglercraftRandom rand) {
        int i2 = rand.nextInt(4);
        return i2 == 0 ? "Skeleton" : (i2 == 1 ? "Zombie" : (i2 == 2 ? "Zombie" : (i2 == 3 ? "Spider" : "")));
    }
}
