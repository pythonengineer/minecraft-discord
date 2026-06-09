package net.minecraft.client.net;

class NetworkWriterThread extends Thread {
    final NetworkManager netManager;

    NetworkWriterThread(NetworkManager networkManager, String name) {
        super(name);
        this.netManager = networkManager;
    }

    public void run() {
        while(NetworkManager.isRunning(this.netManager)) {
            NetworkManager.sendNetworkPacket(this.netManager);
        }

    }
}
