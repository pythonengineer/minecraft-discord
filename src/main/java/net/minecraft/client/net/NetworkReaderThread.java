package net.minecraft.client.net;

class NetworkReaderThread extends Thread {
    final NetworkManager netManager;

    NetworkReaderThread(NetworkManager networkManager, String name) {
        super(name);
        this.netManager = networkManager;
    }

    public void run() {
        Object object1 = NetworkManager.threadSyncObject;
        synchronized(NetworkManager.threadSyncObject) {
            ++NetworkManager.numReadThreads;
        }

        while(!Thread.currentThread().isInterrupted()) {
            boolean z11 = false;

            try {
                z11 = true;
                if(NetworkManager.isRunning(this.netManager)) {
                    if(!NetworkManager.isServerTerminating(this.netManager)) {
                        NetworkManager.readNetworkPacket(this.netManager);
                        continue;
                    }

                    z11 = false;
                    break;
                }

                z11 = false;
                break;
            } finally {
                if(z11) {
                    Object object5 = NetworkManager.threadSyncObject;
                    synchronized(NetworkManager.threadSyncObject) {
                        --NetworkManager.numReadThreads;
                    }
                }
            }
        }

        object1 = NetworkManager.threadSyncObject;
        synchronized(NetworkManager.threadSyncObject) {
            --NetworkManager.numReadThreads;
        }
    }
}
