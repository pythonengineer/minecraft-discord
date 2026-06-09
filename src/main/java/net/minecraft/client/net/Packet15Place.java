package net.minecraft.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet15Place extends Packet {
	public int id;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int direction;

	public Packet15Place() {
	}

	public Packet15Place(int i1, int i2, int i3, int i4, int i5) {
		this.id = i1;
		this.xPosition = i2;
		this.yPosition = i3;
		this.zPosition = i4;
		this.direction = i5;
	}

	public int getPacketId() {
		return 51;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.id = dataInputStream1.read();
		this.xPosition = dataInputStream1.readInt();
		this.yPosition = dataInputStream1.read();
		this.zPosition = dataInputStream1.readInt();
		this.direction = dataInputStream1.read();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.write(this.id);
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.write(this.yPosition);
		dataOutputStream1.writeInt(this.zPosition);
		dataOutputStream1.write(this.direction);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handlePlace(this);
	}
}
