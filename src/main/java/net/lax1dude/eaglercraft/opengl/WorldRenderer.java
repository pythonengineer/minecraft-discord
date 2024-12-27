package net.lax1dude.eaglercraft.opengl;

import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import java.util.Locale;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.log4j.LogManager;
import net.lax1dude.eaglercraft.log4j.Logger;
import net.lax1dude.eaglercraft.util.MathHelper;

/**
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
public class WorldRenderer {

    private static final Logger logger = LogManager.getLogger("WorldRenderer");

    private boolean needsUpdate;
    private int drawMode;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private boolean isDrawing;

    private VertexFormat vertexFormat;

    private int vertexCount;
    private ByteBuffer byteBuffer;
    private IntBuffer intBuffer;

    private boolean hasBeenFreed = false;

    public WorldRenderer(int bufferSizeIn) {
        this.byteBuffer = EagRuntime.allocateByteBuffer(bufferSizeIn << 2);
        this.intBuffer = this.byteBuffer.asIntBuffer();
    }

    public void free() {
        if (!hasBeenFreed) {
            hasBeenFreed = true;
            EagRuntime.freeByteBuffer(byteBuffer);
        }
    }

    public void finalize() {
        free();
    }

    private void grow(int parInt1) {
        int pos = (this.vertexCount * this.vertexFormat.attribStride) >> 2;
        int i = this.byteBuffer.capacity() >> 2;
        if (parInt1 > (i - pos)) {
            int k = (((pos + parInt1 + (parInt1 >> 1)) >> 16) + 1) << 16;
            logger.warn("Needed to grow BufferBuilder buffer: Old size " + (i << 2) + " bytes, new size " + (k << 2)
                    + " bytes.");
            ByteBuffer bytebuffer = EagRuntime.allocateByteBuffer(k << 2);
            this.byteBuffer.position(0);
            bytebuffer.put(this.byteBuffer);
            bytebuffer.rewind();
            EagRuntime.freeByteBuffer(this.byteBuffer);
            this.byteBuffer = bytebuffer;
            this.intBuffer = this.byteBuffer.asIntBuffer();
        }
    }

    public void reset() {
        this.vertexCount = 0;
        this.byteBuffer.clear();
        this.intBuffer.clear();
    }

    public void begin(int parInt1, VertexFormat parVertexFormat) {
        if (this.isDrawing) {
            throw new IllegalStateException("WorldRenderer already building you eagler!");
        } else {
            this.isDrawing = true;
            this.reset();
            this.drawMode = parInt1;
            this.vertexFormat = parVertexFormat;
            this.needsUpdate = false;
            this.byteBuffer.limit(this.byteBuffer.capacity());
        }
    }

    public WorldRenderer tex(double parDouble1, double parDouble2) {
        VertexFormat fmt = this.vertexFormat;
        int i = this.vertexCount * fmt.attribStride + fmt.attribTextureOffset;
        this.byteBuffer.putFloat(i, (float)parDouble1);
        this.byteBuffer.putFloat(i + 4, (float)parDouble2);
        return this;
    }

    public WorldRenderer lightmap(int parInt1, int parInt2) {
        VertexFormat fmt = this.vertexFormat;
        int i = this.vertexCount * fmt.attribStride + fmt.attribLightmapOffset;
        this.byteBuffer.putShort(i, (short)parInt2);
        this.byteBuffer.putShort(i + 2, (short)parInt1);
        return this;
    }

    /**
     * update lightmap color of the last 4 verticies, used in AO calculation
     */
    public void putBrightness4(int parInt1, int parInt2, int parInt3, int parInt4) {
        VertexFormat fmt = this.vertexFormat;
        int j = fmt.attribStride >> 2;
        int i = (this.vertexCount - 4) * j + (fmt.attribLightmapOffset >> 2);
        this.intBuffer.put(i, parInt1);
        this.intBuffer.put(i + j, parInt2);
        this.intBuffer.put(i + j * 2, parInt3);
        this.intBuffer.put(i + j * 3, parInt4);
    }

    /**
     * translates the last 4 verticies to the given position plus current offset
     */
    public void putPosition(double x, double y, double z) {
        int i = this.vertexFormat.attribStride;
        int j = (this.vertexCount - 4) * i;

        for (int k = 0; k < 4; ++k) {
            int l = j + k * i;
            int i1 = l + 4;
            int j1 = i1 + 4;
            this.byteBuffer.putFloat(l, (float)(x + this.xOffset) + this.byteBuffer.getFloat(l));
            this.byteBuffer.putFloat(i1, (float)(y + this.yOffset) + this.byteBuffer.getFloat(i1));
            this.byteBuffer.putFloat(j1, (float)(z + this.zOffset) + this.byteBuffer.getFloat(j1));
        }

    }

    /**
     * gets the color index of a vertex parInt1 indicies before the current vertex
     */
    private int getColorIndex(int parInt1) {
        return ((this.vertexCount - parInt1) * this.vertexFormat.attribStride
                + this.vertexFormat.attribColorOffset) >> 2;
    }

    /**
     * multiplies the color of a vertex parInt1 indicies before the current vertex,
     * skips if !this.needsUpdate
     */
    public void putColorMultiplier(float red, float green, float blue, int parInt1) {
        int i = this.getColorIndex(parInt1);
        int j = -1;
        if (!this.needsUpdate) {
            j = this.intBuffer.get(i);
            int k = (int)((float)(j & 255) * red);
            int l = (int)((float)(j >>> 8 & 255) * green);
            int i1 = (int)((float)(j >>> 16 & 255) * blue);
            j = j & -16777216;
            j = j | i1 << 16 | l << 8 | k;
        }
        this.intBuffer.put(i, j);
    }

    /**
     * sets color multiplier of a vertex parInt1 indicies before the current vertex
     */
    private void putColor(int argb, int parInt2) {
        int i = this.getColorIndex(parInt2);
        int j = argb >>> 16 & 255;
        int k = argb >>> 8 & 255;
        int l = argb & 255;
        int i1 = argb >>> 24 & 255;
        this.putColorRGBA(i, j, k, l, i1);
    }

    /**
     * sets color multiplier of a vertex parInt1 indicies before the current vertex
     */
    public void putColorRGB_F(float red, float green, float blue, int parInt1) {
        int i = this.getColorIndex(parInt1);
        int j = MathHelper.clamp_int((int)(red * 255.0F), 0, 255);
        int k = MathHelper.clamp_int((int)(green * 255.0F), 0, 255);
        int l = MathHelper.clamp_int((int)(blue * 255.0F), 0, 255);
        this.putColorRGBA(i, j, k, l, 255);
    }

    /**
     * sets color multiplier of a vertex parInt1 indicies before the current vertex
     */
    private void putColorRGBA(int index, int red, int parInt3, int parInt4, int parInt5) {
        this.intBuffer.put(index, parInt5 << 24 | parInt4 << 16 | parInt3 << 8 | red);
    }

    /**
     * Marks the current renderer data as dirty (makes it skip certain calls)
     */
    public void markDirty() {
        this.needsUpdate = true;
    }

    /**
     * sets color of current vertex
     */
    public WorldRenderer color(float parFloat1, float parFloat2, float parFloat3, float parFloat4) {
        return this.color((int)(parFloat1 * 255.0F), (int)(parFloat2 * 255.0F), (int)(parFloat3 * 255.0F),
                (int)(parFloat4 * 255.0F));
    }

    /**
     * sets color of current vertex
     */
    public WorldRenderer color(int parInt1, int parInt2, int parInt3, int parInt4) {
        if (this.needsUpdate) {
            return this;
        } else {
            VertexFormat fmt = this.vertexFormat;
            int i = this.vertexCount * fmt.attribStride + fmt.attribColorOffset;
            this.byteBuffer.putInt(i, parInt1 | parInt2 << 8 | parInt3 << 16 | parInt4 << 24);
            return this;
        }
    }

    /**
     * adds cached vertex data to the buffer
     */
    public void addVertexData(int[] vertexData) {
        this.grow(vertexData.length);
        int p = this.intBuffer.position();
        this.intBuffer.position((this.vertexCount * this.vertexFormat.attribStride) >> 2);
        this.intBuffer.put(vertexData);
        this.intBuffer.position(p);
        this.vertexCount += vertexData.length / (this.vertexFormat.attribStride >> 2);
    }

    /**
     * increases the index of the current vertex by 1
     */
    public void endVertex() {
        ++this.vertexCount;
        this.grow(this.vertexFormat.attribStride >> 2);
    }

    /**
     * sets position of current vertex
     */
    public WorldRenderer pos(double parDouble1, double parDouble2, double parDouble3) {
        int i = this.vertexCount * this.vertexFormat.attribStride;
        this.byteBuffer.putFloat(i, (float)(parDouble1 + this.xOffset));
        this.byteBuffer.putFloat(i + 4, (float)(parDouble2 + this.yOffset));
        this.byteBuffer.putFloat(i + 8, (float)(parDouble3 + this.zOffset));
        return this;
    }

    /**
     * sets the normal of the previous 4 verticies in the buffer
     */
    public void putNormal(float x, float y, float z) {
        int i = (byte)((int)(x * 127.0F)) & 255;
        int j = (byte)((int)(y * 127.0F)) & 255;
        int k = (byte)((int)(z * 127.0F)) & 255;
        int l = i | j << 8 | k << 16;
        VertexFormat fmt = this.vertexFormat;
        int i1 = fmt.attribStride;
        int j1 = (this.vertexCount - 4) * i1 + fmt.attribNormalOffset;
        this.byteBuffer.putInt(j1, l);
        this.byteBuffer.putInt(j1 + i1, l);
        this.byteBuffer.putInt(j1 + i1 * 2, l);
        this.byteBuffer.putInt(j1 + i1 * 3, l);
    }

    public void putNormal(float x, float y, float z, int id) {
        int i = (byte)((int)(x * 127.0F)) & 255;
        int j = (byte)((int)(y * 127.0F)) & 255;
        int k = (byte)((int)(z * 127.0F)) & 255;
        int l = i | j << 8 | k << 16 | ((byte)id) << 24;
        VertexFormat fmt = this.vertexFormat;
        int i1 = fmt.attribStride;
        int j1 = (this.vertexCount - 4) * i1 + fmt.attribNormalOffset;
        this.byteBuffer.putInt(j1, l);
        this.byteBuffer.putInt(j1 + i1, l);
        this.byteBuffer.putInt(j1 + i1 * 2, l);
        this.byteBuffer.putInt(j1 + i1 * 3, l);
    }

    /**
     * set normal of current vertex
     */
    public WorldRenderer normal(float parFloat1, float parFloat2, float parFloat3) { // TODO: crash with particles
        VertexFormat fmt = this.vertexFormat;
        int i = this.vertexCount * fmt.attribStride + fmt.attribNormalOffset;
        this.byteBuffer.put(i, (byte)((int)parFloat1 * 127 & 255));
        this.byteBuffer.put(i + 1, (byte)((int)parFloat2 * 127 & 255));
        this.byteBuffer.put(i + 2, (byte)((int)parFloat3 * 127 & 255));
        return this;
    }

    /**
     * sets translation applied to all positions set by functions
     */
    public void setTranslation(double x, double y, double z) {
        this.xOffset = x;
        this.yOffset = y;
        this.zOffset = z;
    }

    public void finishDrawing() {
        if (!this.isDrawing) {
            throw new IllegalStateException("Not building!");
        } else {
            this.isDrawing = false;
            this.byteBuffer.position(0);
            this.byteBuffer.limit(this.vertexCount * this.vertexFormat.attribStride);
        }
    }

    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }

    public VertexFormat getVertexFormat() {
        return this.vertexFormat;
    }

    public int getVertexCount() {
        return this.vertexCount;
    }

    public int getDrawMode() {
        return this.drawMode;
    }

    public void putColor4(int argb) {
        for (int i = 0; i < 4; ++i) {
            this.putColor(argb, i + 1);
        }

    }

    public void putColorRGB_F4(float red, float green, float blue) {
        for (int i = 0; i < 4; ++i) {
            this.putColorRGB_F(red, green, blue, i + 1);
        }

    }

    public WorldRenderer setColorOpaque_I(int color) {
        int red = color >> 16 & 255;
        int green = color >> 8 & 255;
        int blue = color & 255;
        this.color(red, green, blue, 255);
        return this;
    }

    public WorldRenderer begin(int glMode, String vertexFmt) {
        VertexFormat fmt = null;

        switch (vertexFmt.toLowerCase(Locale.ROOT)) {
            case "particle_position_tex_color_lmap":
                fmt = DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP;
                break;
            case "position":
                fmt = DefaultVertexFormats.POSITION;
                break;
            case "position_color":
                fmt = DefaultVertexFormats.POSITION_COLOR;
                break;
            case "position_tex":
                fmt = DefaultVertexFormats.POSITION_TEX;
                break;
            case "position_normal":
                fmt = DefaultVertexFormats.POSITION_NORMAL;
                break;
            case "position_tex_color":
                fmt = DefaultVertexFormats.POSITION_TEX_COLOR;
                break;
            case "position_tex_normal":
                fmt = DefaultVertexFormats.POSITION_TEX_NORMAL;
                break;
            case "position_tex_color_normal":
                fmt = DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL;
                break;
        }

        if (fmt != null) {
            this.begin(glMode, fmt);
        }

        return this;
    }

    public class State {
        private final IntBuffer stateRawBuffer;
        private final VertexFormat stateVertexFormat;
        private int refCount = 1;

        public State(IntBuffer parArrayOfInt, VertexFormat parVertexFormat) {
            this.stateRawBuffer = parArrayOfInt;
            this.stateVertexFormat = parVertexFormat;
        }

        public IntBuffer getRawBuffer() {
            return this.stateRawBuffer;
        }

        public int getVertexCount() {
            return this.stateRawBuffer.remaining() / (this.stateVertexFormat.attribStride >> 2);
        }

        public VertexFormat getVertexFormat() {
            return this.stateVertexFormat;
        }

        public void retain() {
            ++refCount;
        }

        public void release() {
            if (--refCount == 0) {
                EagRuntime.freeIntBuffer(stateRawBuffer);
            }
            if (refCount < 0) {
                logger.error("WorldRenderer.State released multiple times");
            }
        }
    }
}
