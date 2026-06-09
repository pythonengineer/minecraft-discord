package net.minecraft.client.net;

import java.io.IOException;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.internal.IWebSocketClient;
import net.lax1dude.eaglercraft.internal.PlatformNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.controller.PlayerControllerMP;
import net.minecraft.client.gui.GuiConnectFailed;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityOtherPlayerMP;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;
import net.minecraft.game.world.chunk.Chunk;

public class NetClientHandler extends NetHandler {
    private boolean disconnected = false;
    private NetworkManager netManager;
    public String loginProgress;
    private Minecraft mc;
    private WorldClient worldClient;
    private boolean posUpdated = false;
    EaglercraftRandom rand = new EaglercraftRandom();

    public NetClientHandler(Minecraft minecraft, String ip, int port) throws IOException {
        this.mc = minecraft;
        IWebSocketClient webSocket;
        if (port != 0) {
            webSocket = PlatformNetworking.openWebSocket(ip + ":" + port);
        } else {
            webSocket = PlatformNetworking.openWebSocket(ip);
        }

        disconnected = !webSocket.connectBlocking(200);
        if (disconnected) {
            if (webSocket != null) {
                webSocket.close();
            }

            throw new IOException();
        }

        this.netManager = new NetworkManager(webSocket, "Client", this);
    }

    public void processReadPackets() {
        if(!this.disconnected) {
            if(this.posUpdated && this.worldClient != null && this.mc.thePlayer != null) {
                EntityPlayerSP entityPlayerSP1 = this.mc.thePlayer;
                this.netManager.addToSendQueue(new Packet13PlayerLookMove(entityPlayerSP1.posX, entityPlayerSP1.posY, entityPlayerSP1.posZ, entityPlayerSP1.rotationYaw, entityPlayerSP1.rotationPitch, entityPlayerSP1.onGround));
            }

            this.netManager.processReadPackets();
        }
    }

    public void handleLogin(Packet1Handshake packet1Handshake1) {
        this.mc.playerController = new PlayerControllerMP(this.mc, this);
        this.worldClient = new WorldClient(this);
        this.worldClient.multiplayerWorld = true;
        this.mc.changeWorld1(this.worldClient);
        this.mc.displayGuiScreen(new GuiDownloadTerrain(this));
    }

    public void handleNamedEntitySpawn(Packet24NamedEntitySpawn packet) {
        double d2 = (double)packet.xPosition / 32.0D;
        double d4 = (double)packet.yPosition / 32.0D;
        double d6 = (double)packet.zPosition / 32.0D;
        float f8 = (float)(packet.rotation * 360) / 256.0F;
        float f9 = (float)(packet.pitch * 360) / 256.0F;
        EntityOtherPlayerMP entityOtherPlayerMP10 = new EntityOtherPlayerMP(this.mc.theWorld, packet.name);
        int i11 = packet.currentItem;
        if(i11 == 0) {
            entityOtherPlayerMP10.inventory.mainInventory[entityOtherPlayerMP10.inventory.currentItem] = null;
        } else {
            entityOtherPlayerMP10.inventory.mainInventory[entityOtherPlayerMP10.inventory.currentItem] = new ItemStack(i11);
        }

        entityOtherPlayerMP10.setPositionAndRotation(d2, d4, d6, f8, f9);
        this.worldClient.spawnEntityInWorld(entityOtherPlayerMP10);
        this.worldClient.addEntityToWorld(packet.entityId, entityOtherPlayerMP10);
    }

    public void handleEntityTeleport(Packet26EntityTeleport packet) {
        Entity entity2 = this.worldClient.getEntityByID(packet.entityId);
        if(entity2 != null) {
            double d3 = (double)packet.xPosition / 32.0D;
            double d5 = (double)packet.yPosition / 32.0D;
            double d7 = (double)packet.zPosition / 32.0D;
            float f9 = (float)(packet.yaw * 360) / 256.0F;
            float f10 = (float)(packet.pitch * 360) / 256.0F;
            entity2.setPositionAndRotation(d3, d5, d7, f9, f10);
        }
    }

    public void handleDestroyEntity(Packet25DestroyEntity packet25DestroyEntity1) {
        Entity entity2 = this.worldClient.getEntityByID(packet25DestroyEntity1.entityId);
        if(entity2 != null) {
            this.worldClient.removeEntityFromWorld(packet25DestroyEntity1.entityId);
            this.worldClient.setEntityDead(entity2);
        }
    }

    public void handleFlying(Packet13PlayerLookMove packet13PlayerLookMove1) {
        this.mc.thePlayer.setPositionAndRotation(packet13PlayerLookMove1.a, packet13PlayerLookMove1.b, packet13PlayerLookMove1.c, packet13PlayerLookMove1.d, packet13PlayerLookMove1.e);
        this.mc.thePlayer.ySize = 0.0F;
        this.netManager.addToSendQueue(packet13PlayerLookMove1);
        if(!this.posUpdated) {
            this.posUpdated = true;
            this.mc.displayGuiScreen((GuiScreen)null);
        }

    }

    public void handlePreChunk(Packet50PreChunk packet) {
        this.worldClient.doPreChunk(packet.xPosition, packet.yPosition, packet.mode);
    }

    public void handleMultiBlockChange(Packet52MultiBlockChange packet52MultiBlockChange1) {
        Chunk chunk2 = this.worldClient.getChunkFromChunkCoords(packet52MultiBlockChange1.xPosition, packet52MultiBlockChange1.zPosition);
        int i3 = packet52MultiBlockChange1.xPosition * 16;
        int i4 = packet52MultiBlockChange1.zPosition * 16;

        for(int i5 = 0; i5 < packet52MultiBlockChange1.size; ++i5) {
            short s6 = packet52MultiBlockChange1.coordinateArray[i5];
            int i7 = packet52MultiBlockChange1.typeArray[i5] & 255;
            byte b8 = packet52MultiBlockChange1.metadataArray[i5];
            int i9 = s6 >> 12 & 15;
            int i10 = s6 >> 8 & 15;
            int i11 = s6 & 255;
            chunk2.setBlockIDWithMetadata(i9, i11, i10, i7, b8);
            this.worldClient.markBlocksDirty(i9 + i3, i11, i10 + i4, i9 + i3, i11, i10 + i4);
        }

    }

    public void handleMapChunk(Packet51MapChunk packet51MapChunk1) {
        this.worldClient.setChunkData(packet51MapChunk1.xPosition, packet51MapChunk1.yPosition, packet51MapChunk1.zPosition, packet51MapChunk1.xSize, packet51MapChunk1.ySize, packet51MapChunk1.zSize, packet51MapChunk1.chunkData);
    }

    public void handleBlockChange(Packet53BlockChange packet) {
        this.worldClient.setBlockAndMetadataWithNotify(packet.xPosition, packet.yPosition, packet.zPosition, packet.type, packet.metadata);
    }

    public void handleKickDisconnect(Packet255KickDisconnect packet255KickDisconnect1) {
        this.netManager.networkShutdown("Got kicked");
        this.disconnected = true;
        this.mc.changeWorld1((World)null);
        this.mc.displayGuiScreen(new GuiConnectFailed("Disconnected by server", packet255KickDisconnect1.reason));
    }

    public void handleErrorMessage(String string1) {
        if(!this.disconnected) {
            this.disconnected = true;
            this.mc.changeWorld1((World)null);
            this.mc.displayGuiScreen(new GuiConnectFailed("Connection lost", string1));
        }
    }

    public void addToSendQueue(Packet packet) {
        if(!this.disconnected) {
            this.netManager.addToSendQueue(packet);
        }
    }

    public void handleBlockItemSwitch(Packet16BlockItemSwitch packet) {
        Entity entity2 = this.worldClient.getEntityByID(packet.entityId);
        if(entity2 != null) {
            EntityPlayer entityPlayer3 = (EntityPlayer)entity2;
            int i4 = packet.id;
            if(i4 == 0) {
                entityPlayer3.inventory.mainInventory[entityPlayer3.inventory.currentItem] = null;
            } else {
                entityPlayer3.inventory.mainInventory[entityPlayer3.inventory.currentItem] = new ItemStack(i4);
            }

        }
    }
}
