package net.minecraft.client.net;

class NetworkReaderThread extends Thread {
    final NetworkManager netManager;

    NetworkReaderThread(NetworkManager networkManager, String name) {
        super(name);
        this.netManager = networkManager;
    }

    public void run() {
        while(NetworkManager.isRunning(this.netManager) && !NetworkManager.isServerTerminating(this.netManager)) {
            NetworkManager.readNetworkPacket(this.netManager);
        }

    }
}
