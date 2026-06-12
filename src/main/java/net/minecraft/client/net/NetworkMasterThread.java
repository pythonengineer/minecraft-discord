package net.minecraft.client.net;

import net.lax1dude.eaglercraft.EagUtils;

class NetworkMasterThread extends Thread {
    final NetworkManager netManager;

    NetworkMasterThread(NetworkManager networkManager) {
        this.netManager = networkManager;
    }

    public void run() {
        try {
            EagUtils.sleep(5000L);
            if(NetworkManager.getReadThread(this.netManager).isAlive()) {
                try {
                    NetworkManager.getReadThread(this.netManager).interrupt();
                } catch (Throwable throwable3) {
                }
            }

            if(NetworkManager.getWriteThread(this.netManager).isAlive()) {
                try {
                    NetworkManager.getWriteThread(this.netManager).interrupt();
                } catch (Throwable throwable2) {
                }
            }
        } catch (Exception interruptedException4) {
            interruptedException4.printStackTrace();
        }

    }
}
