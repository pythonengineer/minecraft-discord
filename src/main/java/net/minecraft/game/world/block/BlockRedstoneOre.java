package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockRedstoneOre extends Block {
    public BlockRedstoneOre(int i1, int i2, boolean z3) {
        super(i1, i2, Material.rock);
        if(z3) {
            this.setTickOnLoad(true);
        }

    }

    public int tickRate() {
        return 30;
    }

    public void onBlockClicked(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
        this.glow(world1, i2, i3, i4);
        super.onBlockClicked(world1, i2, i3, i4, entityPlayer5);
    }

    public void onEntityWalking(World world1, int i2, int i3, int i4, Entity entity5) {
        this.glow(world1, i2, i3, i4);
        super.onEntityWalking(world1, i2, i3, i4, entity5);
    }

    public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
        this.glow(world1, i2, i3, i4);
        return super.blockActivated(world1, i2, i3, i4, entityPlayer5);
    }

    private void glow(World world1, int i2, int i3, int i4) {
        if(this.blockID == Block.oreRedstone.blockID) {
            world1.setBlockWithNotify(i2, i3, i4, Block.oreRedstoneGlowing.blockID);
        }

    }

    public void updateTick(World world1, int i2, int i3, int i4, EaglercraftRandom random5) {
        if(this.blockID == Block.oreRedstoneGlowing.blockID) {
            world1.setBlockWithNotify(i2, i3, i4, Block.oreRedstone.blockID);
        }

    }

    public int idDropped(int i1, EaglercraftRandom random2) {
        return Item.redstone.shiftedIndex;
    }

    public int quantityDropped(EaglercraftRandom random1) {
        return 4 + random1.nextInt(2);
    }
}
