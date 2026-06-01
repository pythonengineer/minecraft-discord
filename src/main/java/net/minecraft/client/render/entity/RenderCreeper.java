package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.monster.EntityCreeper;

public class RenderCreeper extends RenderLiving {
	public RenderCreeper() {
		super(new ModelCreeper(), 0.5F);
	}

    protected void updateCreeperScale(EntityCreeper creeper, float partialTicks) {
        float f4 = creeper.getCreeperFlashTime(partialTicks);
        float f5 = 1.0F + MathHelper.sin(f4 * 100.0F) * f4 * 0.01F;
        if(f4 < 0.0F) {
            f4 = 0.0F;
        }

        if(f4 > 1.0F) {
            f4 = 1.0F;
        }

        f4 *= f4;
        f4 *= f4;
        float f6 = (1.0F + f4 * 0.4F) * f5;
        float f7 = (1.0F + f4 * 0.1F) / f5;
        GL11.glScalef(f6, f7, f6);
    }

	protected int updateCreeperColorMultiplier(EntityCreeper creeper, float brightness, float partialTicks) {
		float f5 = creeper.getCreeperFlashTime(partialTicks);
		if((int)(f5 * 10.0F) % 2 == 0) {
			return 0;
		} else {
			int i6 = (int)(f5 * 0.2F * 255.0F);
			if(i6 < 0) {
				i6 = 0;
			}

			if(i6 > 255) {
				i6 = 255;
			}

			short s7 = 255;
			short s8 = 255;
			short s9 = 255;
			return i6 << 24 | s7 << 16 | s8 << 8 | s9;
		}
	}

    protected void preRenderCallback(EntityLiving livingEntity, float partialTicks) {
        this.updateCreeperScale((EntityCreeper)livingEntity, partialTicks);
    }

    protected int getColorMultiplier(EntityLiving livingEntity, float brightness, float partialTicks) {
        return this.updateCreeperColorMultiplier((EntityCreeper)livingEntity, brightness, partialTicks);
    }
}
