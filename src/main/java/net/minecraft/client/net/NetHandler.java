package net.minecraft.client.net;

public class NetHandler {
    public void handleMapChunk(Packet51MapChunk packet) {
    }

    public void registerPacket(Packet packet) {
    }

    public void handleErrorMessage(String message) {
    }

    public void handleKickDisconnect(Packet255KickDisconnect packet) {
        this.registerPacket(packet);
    }

    public void handleLogin(Packet1Handshake packet1Handshake1) {
        this.registerPacket(packet1Handshake1);
    }

    public void handleFlying(Packet13PlayerLookMove packet) {
        this.registerPacket(packet);
    }

    public void handleMultiBlockChange(Packet52MultiBlockChange packet) {
        this.registerPacket(packet);
    }

    public void handleBlockDig(Packet14BlockDig packet) {
        this.registerPacket(packet);
    }

    public void handleBlockChange(Packet53BlockChange packet) {
        this.registerPacket(packet);
    }

    public void handlePreChunk(Packet50PreChunk packet) {
        this.registerPacket(packet);
    }

    public void handleNamedEntitySpawn(Packet24NamedEntitySpawn packet) {
        this.registerPacket(packet);
    }

    public void handleEntity(Packet20Entity packet) {
        this.registerPacket(packet);
    }

    public void handleEntityTeleport(Packet26EntityTeleport packet) {
        this.registerPacket(packet);
    }

    public void handlePlace(Packet15Place packet) {
        this.registerPacket(packet);
    }

    public void handleBlockItemSwitch(Packet16BlockItemSwitch packet) {
        this.registerPacket(packet);
    }

    public void handleDestroyEntity(Packet25DestroyEntity packet) {
        this.registerPacket(packet);
    }
}
