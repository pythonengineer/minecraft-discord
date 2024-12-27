package net.lax1dude.eaglercraft.lwjgl;

import net.lax1dude.eaglercraft.internal.buffer.Buffer;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.EaglerBufferAllocator;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.internal.buffer.ShortBuffer;

public final class BufferUtils {

    public static ByteBuffer createByteBuffer(int size) {
        return EaglerBufferAllocator.allocByteBuffer(size);
    }

    public static IntBuffer createIntBuffer(int size) {
        return EaglerBufferAllocator.allocIntBuffer(size);
    }

    public static FloatBuffer createFloatBuffer(int size) {
        return EaglerBufferAllocator.allocFloatBuffer(size);
    }

    public static int getElementSizeExponent(Buffer buf) {
        if (buf instanceof ByteBuffer) return 0;
        if (buf instanceof ShortBuffer) return 1;
        if (buf instanceof FloatBuffer || buf instanceof IntBuffer) return 2;
        throw new IllegalStateException("Unsupported buffer type: " + buf);
    }

    public static int getOffset(Buffer buffer) {
        return buffer.position() << getElementSizeExponent(buffer);
    }

    public static void zeroBuffer(ByteBuffer b) {
        zeroBuffer0(b, b.position(), b.remaining());
    }

    public static void zeroBuffer(ShortBuffer b) {
        zeroBuffer0(b, b.position() * 2L, b.remaining() * 2L);
    }

    public static void zeroBuffer(IntBuffer b) {
        zeroBuffer0(b, b.position() * 4L, b.remaining() * 4L);
    }

    public static void zeroBuffer(FloatBuffer b) {
        zeroBuffer0(b, b.position() * 4L, b.remaining() * 4L);
    }

    private static native void zeroBuffer0(Buffer paramBuffer, long paramLong1, long paramLong2);

    static native long getBufferAddress(Buffer paramBuffer);
}
