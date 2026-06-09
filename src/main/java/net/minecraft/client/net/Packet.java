package net.minecraft.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Packet {
    public static Packet getNewPacket(int i0) {
        switch(i0) {
        case 0:
            return new Packet1Handshake();
        case 1:
            return new Packet13PlayerLookMove();
        case 2:
            return new Packet24NamedEntitySpawn();
        case 3:
            return new Packet25DestroyEntity();
        case 9:
            return new Packet50PreChunk();
        case 10:
            return new Packet51MapChunk();
        case 11:
            return new Packet52MultiBlockChange();
        case 12:
            return new Packet53BlockChange();
        case 50:
            return new Packet14BlockDig();
        case 51:
            return new Packet15Place();
        case 52:
            return new Packet16BlockItemSwitch();
        case 100:
            return new Packet23PickupSpawn();
        case 101:
            return new Packet21RelEntityMove();
        case 102:
            return new Packet22RelEntityMove();
        case 103:
            return new Packet20Entity();
        case 104:
            return new Packet26EntityTeleport();
        case 254:
            return new Packet0KeepAlive();
        case 255:
            return new Packet255KickDisconnect();
        default:
            return null;
        }
    }

    public static Packet readPacket(DataInputStream dataInputStream0) throws IOException {
        int i1 = dataInputStream0.read();
        if(i1 == -1) {
            return null;
        } else {
            Packet packet2 = getNewPacket(i1);
            if(packet2 == null) {
                throw new IOException("Bad packet id " + i1);
            } else {
                packet2.readPacketData(dataInputStream0);
                return packet2;
            }
        }
    }

    public static void writePacket(Packet packet0, DataOutputStream dataOutputStream1) throws IOException {
        dataOutputStream1.write(packet0.getPacketId());
        packet0.writePacket(dataOutputStream1);
    }

    public abstract void readPacketData(DataInputStream dataInputStream1) throws IOException;

    public abstract void writePacket(DataOutputStream dataOutputStream1) throws IOException;

    public abstract int getPacketId() throws IOException;

    public abstract void processPacket(NetHandler netHandler1);
}
