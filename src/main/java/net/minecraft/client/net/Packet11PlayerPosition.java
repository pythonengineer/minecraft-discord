package net.minecraft.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet11PlayerPosition extends Packet10Flying {
    public Packet11PlayerPosition() {
        this.moving = true;
    }

    public Packet11PlayerPosition(double x, double minY, double y, boolean z7) {
        this.xPosition = x;
        this.yPosition = minY;
        this.stance = y;
        this.onGround = z7;
        this.moving = true;
    }

    public void readPacketData(DataInputStream dataInputStream1) throws IOException {
        this.xPosition = dataInputStream1.readDouble();
        this.yPosition = dataInputStream1.readDouble();
        this.stance = dataInputStream1.readDouble();
        super.readPacketData(dataInputStream1);
    }

    public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
        dataOutputStream1.writeDouble(this.xPosition);
        dataOutputStream1.writeDouble(this.yPosition);
        dataOutputStream1.writeDouble(this.stance);
        super.writePacket(dataOutputStream1);
    }
}
