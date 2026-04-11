package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityMinecart;
import net.minecraft.game.physics.Vec3D;

public final class RenderMinecart extends Render {
    private ModelBase minecartModel;

    public RenderMinecart() {
        this.shadowSize = 0.5F;
        this.minecartModel = new ModelMinecart();
    }

    public final void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        EntityMinecart entityMinecart10001 = (EntityMinecart)entity;
        float f3 = yaw;
        double d16 = z;
        double d14 = y;
        double d12 = x;
        EntityMinecart x1 = entityMinecart10001;
        GL11.glPushMatrix();
        double d20 = x1.lastTickPosX + (x1.posX - x1.lastTickPosX) * (double)partialTicks;
        double d22 = x1.lastTickPosY + (x1.posY - x1.lastTickPosY) * (double)partialTicks;
        double d24 = x1.lastTickPosZ + (x1.posZ - x1.lastTickPosZ) * (double)partialTicks;
        Vec3D vec3D5 = x1.getPos(d20, d22, d24);
        float y1 = x1.prevRotationPitch + (x1.rotationPitch - x1.prevRotationPitch) * partialTicks;
        if(vec3D5 != null) {
            Vec3D z1 = x1.getPosOffset(d20, d22, d24, (double)0.3F);
            Vec3D vec3D7 = x1.getPosOffset(d20, d22, d24, -0.30000001192092896D);
            if(z1 == null) {
                z1 = vec3D5;
            }

            if(vec3D7 == null) {
                vec3D7 = vec3D5;
            }

            d12 += vec3D5.xCoord - d20;
            d14 += (z1.yCoord + vec3D7.yCoord) / 2.0D - d22;
            d16 += vec3D5.zCoord - d24;
            if((vec3D5 = vec3D7.addVector(-z1.xCoord, -z1.yCoord, -z1.zCoord)).lengthVector() != 0.0D) {
                f3 = -((float)(Math.atan2((vec3D5 = vec3D5.normalize()).zCoord, vec3D5.xCoord) * 180.0D / Math.PI));
                y1 = (float)(Math.atan(vec3D5.yCoord) * 80.0D);
            }
        }

        GL11.glTranslatef((float)d12, (float)d14, (float)d16);
        this.loadTexture("/item/cart.png");
        GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(y1, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.minecartModel.render(0.0F, 0.0F, x1.getOccupiedSpace() * 7.1F - 0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
}