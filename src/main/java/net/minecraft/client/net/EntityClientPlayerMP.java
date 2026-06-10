package net.minecraft.client.net;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Session;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.world.World;

public class EntityClientPlayerMP extends EntityPlayerSP {
    private NetClientHandler sendQueue;
    private double oldPosX;
    private double oldBasePos;
    private double oldPosY;
    private float oldPosZ;
    private float oldRotationYaw;

    public EntityClientPlayerMP(Minecraft minecraft, World worldObj, Session session, NetClientHandler sendQueue) {
        super(minecraft, worldObj, session);
        this.sendQueue = sendQueue;
    }

    public void onUpdate() {
        super.onUpdate();
        double d1 = this.posX - this.oldPosX;
        double d3 = this.posY + (double)this.ySize - this.oldBasePos;
        double d5 = this.posZ - this.oldPosY;
        double d7 = (double)(this.rotationYaw - this.oldPosZ);
        double d9 = (double)(this.rotationPitch - this.oldRotationYaw);
        boolean z11 = d3 != 0.0D || d1 * d1 + d5 * d5 > 0.001D;
        boolean z12 = d7 * d7 + d9 * d9 > 0.001D;
        if(z11 && z12) {
            this.sendQueue.addToSendQueue(new Packet13PlayerLookMove(this.posX, this.posY + (double)this.ySize, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
        } else if(z11) {
            this.sendQueue.addToSendQueue(new Packet11PlayerPosition(this.posX, this.posY + (double)this.ySize, this.posZ, this.onGround));
        } else if(z12) {
            this.sendQueue.addToSendQueue(new Packet12PlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
        } else {
            this.sendQueue.addToSendQueue(new Packet10Flying(this.onGround));
        }

        if(z11) {
            this.oldPosX = this.posX;
            this.oldBasePos = this.posY + (double)this.ySize;
            this.oldPosY = this.posZ;
        }

        if(z12) {
            this.oldPosZ = this.rotationYaw;
            this.oldRotationYaw = this.rotationPitch;
        }

    }

    protected void joinEntityItemWithWorld(EntityItem entityItem) {
        Packet21PickupSpawn packet21PickupSpawn2 = new Packet21PickupSpawn(entityItem);
        this.sendQueue.addToSendQueue(packet21PickupSpawn2);
        entityItem.posX = (double)packet21PickupSpawn2.xPosition / 32.0D;
        entityItem.posY = (double)packet21PickupSpawn2.yPosition / 32.0D;
        entityItem.posZ = (double)packet21PickupSpawn2.zPosition / 32.0D;
        entityItem.motionX = (double)packet21PickupSpawn2.rotation / 128.0D;
        entityItem.motionY = (double)packet21PickupSpawn2.pitch / 128.0D;
        entityItem.motionZ = (double)packet21PickupSpawn2.roll / 128.0D;
    }

    public void sendChatMessage(String chatMessage) {
        this.sendQueue.addToSendQueue(new Packet3Chat(chatMessage));
    }
}
