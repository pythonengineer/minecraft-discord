package net.minecraft.client;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public class GLAllocation {
    private static List displayLists = new ArrayList();
    private static List textureNames = new ArrayList();
    private static List c = new ArrayList();
    private static List d = new ArrayList();

    public static synchronized int generateDisplayLists(int i0) {
        int i1 = GL11.glGenLists(i0);
        displayLists.add(i1);
        displayLists.add(i0);
        return i1;
    }

    public static synchronized void generateTextureNames(IntBuffer intBuffer0) {
        GL11.glGenTextures(intBuffer0);

        for(int i1 = intBuffer0.position(); i1 < intBuffer0.limit(); ++i1) {
            textureNames.add(intBuffer0.get(i1));
        }

    }

    public static synchronized void deleteTexturesAndDisplayLists() {
        for(int i0 = 0; i0 < displayLists.size(); i0 += 2) {
            GL11.glDeleteLists(((Integer)displayLists.get(i0)).intValue(), ((Integer)displayLists.get(i0 + 1)).intValue());
        }

        IntBuffer intBuffer2;
        (intBuffer2 = createDirectIntBuffer(textureNames.size())).flip();
        GL11.glDeleteTextures(intBuffer2);

        for(int i1 = 0; i1 < textureNames.size(); ++i1) {
            intBuffer2.put(((Integer)textureNames.get(i1)).intValue());
        }

        intBuffer2.flip();
        GL11.glDeleteTextures(intBuffer2);
        displayLists.clear();
        textureNames.clear();
        d.addAll(c);
        c.clear();
    }

    public static synchronized ByteBuffer createDirectByteBuffer(int capacity) {
        return EagRuntime.allocateByteBuffer(capacity);
    }

    public static IntBuffer createDirectIntBuffer(int capacity) {
        return EagRuntime.allocateIntBuffer(capacity);
    }

    public static FloatBuffer createDirectFloatBuffer(int capacity) {
        return EagRuntime.allocateFloatBuffer(capacity);
    }
}
