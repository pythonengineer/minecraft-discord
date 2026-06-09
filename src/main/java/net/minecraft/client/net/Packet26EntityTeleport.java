package net.minecraft.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;

public class Packet26EntityTeleport extends Packet {
	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte yaw;
	public byte pitch;

	public Packet26EntityTeleport() {
	}

	public Packet26EntityTeleport(Entity entity1) {
		this.entityId = entity1.entityID;
		this.xPosition = MathHelper.floor_double(entity1.posX * 32.0D);
		this.yPosition = MathHelper.floor_double(entity1.posY * 32.0D);
		this.zPosition = MathHelper.floor_double(entity1.posZ * 32.0D);
		this.yaw = (byte)((int)(entity1.rotationYaw * 256.0F / 360.0F));
		this.pitch = (byte)((int)(entity1.rotationPitch * 256.0F / 360.0F));
	}

	public int getPacketId() {
		return 104;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
		this.xPosition = dataInputStream1.readInt();
		this.yPosition = dataInputStream1.readInt();
		this.zPosition = dataInputStream1.readInt();
		this.yaw = (byte)dataInputStream1.read();
		this.pitch = (byte)dataInputStream1.read();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityId);
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.writeInt(this.yPosition);
		dataOutputStream1.writeInt(this.zPosition);
		dataOutputStream1.write(this.yaw);
		dataOutputStream1.write(this.pitch);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleEntityTeleport(this);
	}
}
