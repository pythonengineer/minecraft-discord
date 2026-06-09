package net.minecraft.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet13PlayerLookMove extends Packet {
	public double a;
	public double b;
	public double c;
	public float d;
	public float e;
	public boolean f;

	public Packet13PlayerLookMove() {
	}

	public Packet13PlayerLookMove(double d1, double d3, double d5, float f7, float f8, boolean z9) {
		this.a = d1;
		this.b = d3;
		this.c = d5;
		this.d = f7;
		this.e = f8;
		this.f = z9;
	}

	public int getPacketId() {
		return 1;
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleFlying(this);
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.a = dataInputStream1.readDouble();
		this.b = dataInputStream1.readDouble();
		this.c = dataInputStream1.readDouble();
		this.d = dataInputStream1.readFloat();
		this.e = dataInputStream1.readFloat();
		this.f = dataInputStream1.read() != 0;
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeDouble(this.a);
		dataOutputStream1.writeDouble(this.b);
		dataOutputStream1.writeDouble(this.c);
		dataOutputStream1.writeFloat(this.d);
		dataOutputStream1.writeFloat(this.e);
		dataOutputStream1.write(this.f ? 1 : 0);
	}
}
