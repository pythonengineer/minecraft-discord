package net.lax1dude.eaglercraft.lwjgl.opengl;

import java.util.ArrayList;

import net.lax1dude.eaglercraft.internal.IVertexArrayGL;
import net.lax1dude.eaglercraft.internal.IBufferGL;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
public class DisplayList {

    public class ListOperation {
        boolean hasTex = false;
        boolean hasColor = false;
        boolean hasCount = false;
        boolean hasSetting = false;
        boolean doBlend = false;
        boolean enabled;
        IntBuffer indices = null;
        int setting;
        int count;
        int offset;
        int tex;
        int srcFactor;
        int dstFactor;
        float r;
        float g;
        float b;
        float a;

        public ListOperation(int tex) {
            this.hasTex = true;
            this.tex = tex;
        }

        public ListOperation(int setting, boolean enabled) {
            this.hasSetting = true;
            this.setting = setting;
            this.enabled = enabled;
        }

        public ListOperation(int offset, int count, IntBuffer indices) {
            this.hasCount = true;
            this.offset = offset;
            this.count = count;
            this.indices = indices;
        }

        public ListOperation(int mode, int srcFactor, int dstFactor) {
            this.doBlend = true;
            this.srcFactor = srcFactor;
            this.dstFactor = dstFactor;
        }

        public ListOperation(float r, float g, float b, float a) {
            this.hasColor = true;
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }
    }
    IVertexArrayGL vertexArray = null;
    IBufferGL vertexBuffer = null;
    int attribs = -1;
    int mode = -1;
    int count = 0;
    final int id;
    ArrayList<ListOperation> ops = new ArrayList<ListOperation>();
    IntBuffer indices = null;
    byte bindQuad = 0;

    DisplayList(int id) {
        this.id = id;
    }
}
