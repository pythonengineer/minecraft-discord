package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;

public final class BlockGears extends Block {
    protected BlockGears(int var1, int var2) {
        super(55, 62);
    }

    public final AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
        return null;
    }

    public final boolean isOpaqueCube() {
        return false;
    }

    public final boolean renderAsNormalBlock() {
        return false;
    }

    public final int getRenderType() {
        return 5;
    }

    public final int quantityDropped(EaglercraftRandom var1) {
        return 1;
    }

    public final boolean isCollidable() {
        return false;
    }
}
