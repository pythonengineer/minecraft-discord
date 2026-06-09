package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityBoat;

public class RenderBoat extends Render {
    protected ModelBase modelBoat;

    public RenderBoat() {
        this.shadowSize = 0.5F;
        this.modelBoat = new ModelBoat();
    }

    public void renderBoat(EntityBoat entityBoat1, double d2, double d4, double d6, float f8, float f9) {
        GL11.glPushMatrix();
        double d10 = (double)0.3F;
        GL11.glTranslatef((float)d2, (float)d4, (float)d6);
        GL11.glRotatef(180.0F - f8, 0.0F, 1.0F, 0.0F);
        float f12 = (float)entityBoat1.timeSinceHit - f9;
        float f13 = (float)entityBoat1.damageTaken - f9;
        if(f13 < 0.0F) {
            f13 = 0.0F;
        }

        if(f12 > 0.0F) {
            GL11.glRotatef(MathHelper.sin(f12) * f12 * f13 / 10.0F * (float)entityBoat1.forwardDirection, 1.0F, 0.0F, 0.0F);
        }

        this.loadTexture("/terrain.png");
        float f14 = 0.75F;
        GL11.glScalef(f14, f14, f14);
        GL11.glScalef(1.0F / f14, 1.0F / f14, 1.0F / f14);
        this.loadTexture("/item/boat.png");
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glTranslatef(-0.25F, 0.0F, 0.0F);
        this.modelBoat.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
        this.renderBoat((EntityBoat)entity1, d2, d4, d6, f8, f9);
    }
}
