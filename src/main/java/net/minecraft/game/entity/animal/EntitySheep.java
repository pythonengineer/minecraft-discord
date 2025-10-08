package net.minecraft.game.entity.animal;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class EntitySheep extends EntityAnimal {
    public boolean sheared;

    private EntitySheep(World var1) {
        super(var1);
    }

    public final boolean attackEntityFrom(Entity var1, int var2) {
        if(!this.sheared && var1 instanceof EntityLiving) {
            this.sheared = true;
            int var3 = 1 + this.rand.nextInt(3);

            for(int var4 = 0; var4 < var3; ++var4) {
                EntityItem var5 = this.entityDropItem(Block.clothGray.blockID, 1, 1.0F);
                var5.motionY += (double)(this.rand.nextFloat() * 0.05F);
                var5.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                var5.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
            }
        }

        return super.attackEntityFrom(var1, var2);
    }

	protected final String getLivingSound() {
		return "mob.sheep";
	}

	protected final String getHurtSound() {
		return "mob.sheep";
	}

	protected final String getDeathSound() {
		return "mob.sheep";
	}
}
