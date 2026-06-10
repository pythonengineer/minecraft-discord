package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.monster.EntitySlime;

public class RenderSlime extends RenderLiving {
    public RenderSlime(ModelBase modelBase1, float f2) {
        super(modelBase1, f2);
    }

    protected void squishSlime(EntitySlime entitySlime1, float f2) {
        float f3 = (entitySlime1.prevSquishFactor + (entitySlime1.squishFactor - entitySlime1.prevSquishFactor) * f2) / (float)entitySlime1.size;
        float f4 = 1.0F / (f3 + 1.0F);
        float f5 = (float)entitySlime1.size;
        GL11.glScalef(f4 * f5, 1.0F / f4 * f5, f4 * f5);
        GL11.glEnable(GL11.GL_NORMALIZE);
    }

    protected void preRenderCallback(EntityLiving entityLiving1, float f2) {
        this.squishSlime((EntitySlime)entityLiving1, f2);
    }
}
