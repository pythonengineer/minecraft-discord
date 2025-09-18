package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public final class BlockTorch extends Block {
    protected BlockTorch(int var1, int var2) {
        super(50, 80, Material.circuits);
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

    public final void randomDisplayTick(World var1, int var2, int var3, int var4, EaglercraftRandom var5) {
        float var8 = (float)var2 + 0.5F;
        float var6 = (float)var3 + 0.7F;
        float var7 = (float)var4 + 0.5F;
        if(var1.isBlockNormalCube(var2 - 1, var3, var4)) {
            var1.spawnParticle("smoke", var8 - 0.27F, var6 + 0.22F, var7, 0.0F, 0.0F, 0.0F);
            var1.spawnParticle("flame", var8 - 0.27F, var6 + 0.22F, var7, 0.0F, 0.0F, 0.0F);
        } else if(var1.isBlockNormalCube(var2 + 1, var3, var4)) {
            var1.spawnParticle("smoke", var8 + 0.27F, var6 + 0.22F, var7, 0.0F, 0.0F, 0.0F);
            var1.spawnParticle("flame", var8 + 0.27F, var6 + 0.22F, var7, 0.0F, 0.0F, 0.0F);
        } else if(var1.isBlockNormalCube(var2, var3, var4 - 1)) {
            var1.spawnParticle("smoke", var8, var6 + 0.22F, var7 - 0.27F, 0.0F, 0.0F, 0.0F);
            var1.spawnParticle("flame", var8, var6 + 0.22F, var7 - 0.27F, 0.0F, 0.0F, 0.0F);
        } else if(var1.isBlockNormalCube(var2, var3, var4 + 1)) {
            var1.spawnParticle("smoke", var8, var6 + 0.22F, var7 + 0.27F, 0.0F, 0.0F, 0.0F);
            var1.spawnParticle("flame", var8, var6 + 0.22F, var7 + 0.27F, 0.0F, 0.0F, 0.0F);
        } else {
            var1.spawnParticle("smoke", var8, var6, var7, 0.0F, 0.0F, 0.0F);
            var1.spawnParticle("flame", var8, var6, var7, 0.0F, 0.0F, 0.0F);
        }
    }
}
