package net.lax1dude.eaglercraft.internal;

import java.io.IOException;
import java.io.OutputStream;

class WebSocketOutputStream extends OutputStream {

    private final IWebSocketClient webSocket;

    WebSocketOutputStream(IWebSocketClient webSocket) {
        this.webSocket = webSocket;
    }

    @Override
    public void write(int b) throws IOException {
        webSocket.send(new byte[] { (byte) b });
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (len > 0) {
            byte[] data = new byte[len];
            System.arraycopy(b, off, data, 0, len);
            webSocket.send(data);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            this.webSocket.close();
        } catch (Exception exception) {
        }
    }
}
