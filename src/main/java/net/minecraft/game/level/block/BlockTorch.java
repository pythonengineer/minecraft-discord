package net.minecraft.game.level.block;

import net.minecraft.game.level.World;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public final class BlockTorch extends Block {
    protected BlockTorch(int var1, int var2) {
        super(50, 80);
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
        return 2;
    }

    public final MovingObjectPosition collisionRayTrace(World var1, int var2, int var3, int var4, Vec3D var5, Vec3D var6) {
        if(var1.isBlockNormalCube(var2 - 1, var3, var4)) {
            this.setBlockBounds(0.0F, 0.2F, 0.35F, 0.3F, 0.8F, 0.65F);
        } else if(var1.isBlockNormalCube(var2 + 1, var3, var4)) {
            this.setBlockBounds(0.7F, 0.2F, 0.35F, 1.0F, 0.8F, 0.65F);
        } else if(var1.isBlockNormalCube(var2, var3, var4 - 1)) {
            this.setBlockBounds(0.35F, 0.2F, 0.0F, 0.65F, 0.8F, 0.3F);
        } else if(var1.isBlockNormalCube(var2, var3, var4 + 1)) {
            this.setBlockBounds(0.35F, 0.2F, 0.7F, 0.65F, 0.8F, 1.0F);
        } else {
            this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.6F, 0.6F);
        }

        return super.collisionRayTrace(var1, var2, var3, var4, var5, var6);
    }
}
