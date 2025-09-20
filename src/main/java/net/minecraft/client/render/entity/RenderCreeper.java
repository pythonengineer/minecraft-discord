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

    protected final void preRenderCallback(EntityLiving var1, float var2) {
        EntityCreeper var4 = (EntityCreeper)var1;
        float var5 = var4.getCreeperState(var2);
        var2 = 1.0F + MathHelper.sin(var5 * 100.0F) * var5 * 0.01F;
        if(var5 < 0.0F) {
            var5 = 0.0F;
        }

        if(var5 > 1.0F) {
            var5 = 1.0F;
        }

        var5 *= var5;
        var5 *= var5;
        float var3 = (1.0F + var5 * 0.4F) * var2;
        var5 = (1.0F + var5 * 0.1F) / var2;
        GL11.glScalef(var3, var5, var3);
    }

    protected final int getColorMultiplier(EntityLiving var1, float var2) {
        EntityCreeper var3 = (EntityCreeper)var1;
        float var4 = var3.getCreeperState(var2);
        if((int)(var4 * 10.0F) % 2 == 0) {
            return 0;
        } else {
            int var5 = (int)(var4 * 0.2F * 255.0F);
            if(var5 < 0) {
                var5 = 0;
            }

            if(var5 > 255) {
                var5 = 255;
            }

            return var5 << 24 | 16711680 | '\uff00' | 255;
        }
    }
}
