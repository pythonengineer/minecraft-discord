package net.minecraft.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.internal.IWebSocketClient;

public class NetworkManager {
    private IWebSocketClient networkSocket;
    private DataInputStream socketInputStream;
    private DataOutputStream socketOutputStream;
    private boolean isRunning = true;
    private List readPackets = Collections.synchronizedList(new LinkedList());
    private List dataPackets = Collections.synchronizedList(new LinkedList());
    private List chunkDataPackets = Collections.synchronizedList(new LinkedList());
    private NetHandler netHandler;
    private boolean isServerTerminating = false;
    private Thread writeThread;
    private Thread readThread;
    private boolean isTerminating = false;
    private String terminationReason = "";
    private int timeSinceLastRead = 0;
    private int chunkDataSendCounter = 0;

    public NetworkManager(IWebSocketClient socket, String name, NetHandler netHandler) throws IOException {
        this.networkSocket = socket;
        this.netHandler = netHandler;
        this.socketInputStream = new DataInputStream(socket.getInputStream());
        this.socketOutputStream = new DataOutputStream(socket.getOutputStream());
        this.readThread = new NetworkReaderThread(this, name + " read thread");
        this.writeThread = new NetworkWriterThread(this, name + " write thread");
        this.readThread.start();
        this.writeThread.start();
    }

    public void addToSendQueue(Packet packet) {
        if(!this.isServerTerminating) {
            if(packet.isChunkDataPacket) {
                this.chunkDataPackets.add(packet);
            } else {
                this.dataPackets.add(packet);
            }

        }
    }

    private void sendPacket() {
        try {
            boolean z1 = true;
            if(!this.dataPackets.isEmpty()) {
                z1 = false;
                Packet.writePacket((Packet)this.dataPackets.remove(0), this.socketOutputStream);
            }

            if((z1 || this.chunkDataSendCounter-- <= 0) && !this.chunkDataPackets.isEmpty()) {
                z1 = false;
                Packet.writePacket((Packet)this.chunkDataPackets.remove(0), this.socketOutputStream);
                this.chunkDataSendCounter = 50;
            }

            if(z1) {
                Thread.sleep(10L);
            }
        } catch (InterruptedException interruptedException2) {
        } catch (Exception exception3) {
            this.onNetworkError(exception3);
        }

    }

    private void readPacket() {
        try {
            Packet packet1 = Packet.readPacket(this.socketInputStream);
            if(packet1 != null) {
                this.readPackets.add(packet1);
            } else {
                this.networkShutdown("End of stream");
            }
        } catch (Exception exception2) {
            this.onNetworkError(exception2);
        }

    }

    private void onNetworkError(Exception exception) {
        exception.printStackTrace();
        this.networkShutdown("Internal exception: " + exception.toString());
    }

    public void networkShutdown(String reason) {
        if(this.isRunning) {
            this.isTerminating = true;
            this.terminationReason = reason;
            this.isRunning = false;

            try {
                this.socketInputStream.close();
            } catch (Throwable throwable5) {
            }

            try {
                this.socketOutputStream.close();
            } catch (Throwable throwable4) {
            }

            try {
                this.networkSocket.close();
            } catch (Throwable throwable3) {
            }

        }
    }

    public void processReadPackets() {
        if(this.readPackets.isEmpty()) {
            if(this.timeSinceLastRead++ == 1200) {
                this.networkShutdown("Timed out");
            }
        } else {
            this.timeSinceLastRead = 0;
        }

        int i1 = 100;

        while(!this.readPackets.isEmpty() && i1-- >= 0) {
            Packet packet2 = (Packet)this.readPackets.remove(0);
            packet2.processPacket(this.netHandler);
        }

        if(this.isTerminating && this.readPackets.isEmpty()) {
            this.netHandler.handleErrorMessage(this.terminationReason);
        }

    }

    static boolean isRunning(NetworkManager networkManager0) {
        return networkManager0.isRunning;
    }

    static boolean isServerTerminating(NetworkManager networkManager0) {
        return networkManager0.isServerTerminating;
    }

    static void readNetworkPacket(NetworkManager networkManager0) {
        networkManager0.readPacket();
    }

    static void sendNetworkPacket(NetworkManager networkManager0) {
        networkManager0.sendPacket();
    }
}
