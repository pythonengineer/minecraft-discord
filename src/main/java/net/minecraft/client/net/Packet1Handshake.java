package net.minecraft.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet1Handshake extends Packet {
	public int protocol;
	public String username;
	public String password;

	public Packet1Handshake() {
	}

	public Packet1Handshake(String string1, String string2, int i3) {
		this.username = string1;
		this.password = string2;
		this.protocol = i3;
	}

	public int getPacketId() {
		return 0;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.protocol = dataInputStream1.readInt();
		this.username = dataInputStream1.readUTF();
		this.password = dataInputStream1.readUTF();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.protocol);
		dataOutputStream1.writeUTF(this.username);
		dataOutputStream1.writeUTF(this.password);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleLogin(this);
	}
}
