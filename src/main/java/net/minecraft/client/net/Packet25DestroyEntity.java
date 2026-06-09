package net.minecraft.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet25DestroyEntity extends Packet {
	public int entityId;

	public int getPacketId() {
		return 3;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityId);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleDestroyEntity(this);
	}
}
