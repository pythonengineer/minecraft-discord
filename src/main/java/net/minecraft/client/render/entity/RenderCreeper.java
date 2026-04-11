package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.monster.EntityCreeper;

public final class RenderCreeper extends RenderLiving {
	public RenderCreeper() {
		super(new ModelCreeper(), 0.5F);
	}

    protected final void preRenderCallback(EntityLiving livingEntity, float partialTicks) {
        float f4 = ((EntityCreeper)livingEntity).getCreeperFlashTime(partialTicks);
        partialTicks = 1.0F + MathHelper.sin(f4 * 100.0F) * f4 * 0.01F;
        if(f4 < 0.0F) {
            f4 = 0.0F;
        }

        if(f4 > 1.0F) {
            f4 = 1.0F;
        }

        f4 = (f4 *= f4) * f4;
        float f3 = (1.0F + f4 * 0.4F) * partialTicks;
        f4 = (1.0F + f4 * 0.1F) / partialTicks;
        GL11.glScalef(f3, f4, f3);
    }

	protected final int getColorMultiplier(EntityLiving livingEntity, float brightness, float partialTicks) {
		float livingEntity1;
		if((int)((livingEntity1 = ((EntityCreeper)livingEntity).getCreeperFlashTime(partialTicks)) * 10.0F) % 2 == 0) {
			return 0;
		} else {
			int livingEntity2;
			if((livingEntity2 = (int)(livingEntity1 * 0.2F * 255.0F)) < 0) {
				livingEntity2 = 0;
			}

			if(livingEntity2 > 255) {
				livingEntity2 = 255;
			}

			return livingEntity2 << 24 | 16711680 | 65280 | 255;
		}
	}
}