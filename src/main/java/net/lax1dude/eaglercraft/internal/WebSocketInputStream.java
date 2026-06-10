package net.lax1dude.eaglercraft.internal;

import java.io.IOException;
import java.io.InputStream;

import net.lax1dude.eaglercraft.EagUtils;

class WebSocketInputStream extends InputStream {

    private final IWebSocketClient webSocket;
    private byte[] currentFrameData = new byte[0];
    private int currentFramePosition = 0;

    WebSocketInputStream(IWebSocketClient webSocket) {
        this.webSocket = webSocket;
    }

    @Override
    public int read() throws IOException {
        if (currentFramePosition < currentFrameData.length) {
            return currentFrameData[currentFramePosition++] & 0xFF;
        }

        while (true) {
            IWebSocketFrame frame = webSocket.getNextFrame();
            if (frame != null) {
                currentFrameData = frame.getByteArray();
                currentFramePosition = 0;

                if (currentFrameData.length > 0) {
                    return currentFrameData[currentFramePosition++] & 0xFF;
                }

                continue;
            }

            if (webSocket.isClosed()) {
                return -1;
            }

            EagUtils.sleep(50);
        }
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        if (currentFramePosition < currentFrameData.length) {
            int remaining = currentFrameData.length - currentFramePosition;
            int toRead = Math.min(remaining, len);
            System.arraycopy(currentFrameData, currentFramePosition, b, off, toRead);
            currentFramePosition += toRead;
            return toRead;
        }

        while (true) {
            IWebSocketFrame frame = webSocket.getNextFrame();
            if (frame != null) {
                currentFrameData = frame.getByteArray();
                currentFramePosition = 0;

                if (currentFrameData.length > 0) {
                    int toRead = Math.min(currentFrameData.length, len);
                    System.arraycopy(currentFrameData, 0, b, off, toRead);
                    currentFramePosition += toRead;
                    return toRead;
                }

                continue;
            }

            if (webSocket.isClosed()) {
                return -1;
            }

            EagUtils.sleep(50);
        }
    }

    @Override
    public void close() {
        try {
            this.webSocket.close();
        } catch (Exception exception) {
        }
    }
}
