package net.minecraft.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet13PlayerLookMove extends Packet10Flying {
	public Packet13PlayerLookMove() {
		this.rotating = true;
		this.moving = true;
	}

	public Packet13PlayerLookMove(double d1, double d3, double d5, float f7, float f8, boolean z9) {
		this.xPosition = d1;
		this.yPosition = d3;
		this.stance = d5;
		this.yaw = f7;
		this.pitch = f8;
		this.onGround = z9;
		this.rotating = true;
		this.moving = true;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.xPosition = dataInputStream1.readDouble();
		this.yPosition = dataInputStream1.readDouble();
		this.stance = dataInputStream1.readDouble();
		this.yaw = dataInputStream1.readFloat();
		this.pitch = dataInputStream1.readFloat();
		super.readPacketData(dataInputStream1);
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeDouble(this.xPosition);
		dataOutputStream1.writeDouble(this.yPosition);
		dataOutputStream1.writeDouble(this.stance);
		dataOutputStream1.writeFloat(this.yaw);
		dataOutputStream1.writeFloat(this.pitch);
		super.writePacket(dataOutputStream1);
	}
}
