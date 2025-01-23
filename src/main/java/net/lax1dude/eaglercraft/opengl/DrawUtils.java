package net.lax1dude.eaglercraft.opengl;

import static net.lax1dude.eaglercraft.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.lwjgl.opengl.GL11.*;

import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.internal.IBufferGL;
import net.lax1dude.eaglercraft.internal.IShaderGL;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.lwjgl.opengl.GLSLHeader;

/**
 * Copyright (c) 2022-2025 lax1dude. All Rights Reserved.
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
public class DrawUtils {

    public static final String vertexSource = "#line 2\r\n"
            + "\r\n"
            + "/*\r\n"
            + " * Copyright (c) 2022-2025 lax1dude. All Rights Reserved.\r\n"
            + " * \r\n"
            + " * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND\r\n"
            + " * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED\r\n"
            + " * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.\r\n"
            + " * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,\r\n"
            + " * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT\r\n"
            + " * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR\r\n"
            + " * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,\r\n"
            + " * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)\r\n"
            + " * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE\r\n"
            + " * POSSIBILITY OF SUCH DAMAGE.\r\n"
            + " * \r\n"
            + " */\r\n"
            + "\r\n"
            + "EAGLER_VSH_LAYOUT_BEGIN()\r\n"
            + "EAGLER_IN(0, vec2, a_position2f)\r\n"
            + "EAGLER_VSH_LAYOUT_END()\r\n"
            + "\r\n"
            + "EAGLER_OUT(vec2, v_position2f)\r\n"
            + "\r\n"
            + "void main() {\r\n"
            + "    v_position2f = a_position2f * 0.5 + 0.5;\r\n"
            + "    EAGLER_VERT_POSITION = vec4(a_position2f, 0.0, 1.0);\r\n"
            + "}\r\n"
            + "";
    public static final String vertexShaderPrecision = "precision highp float;\n";

    public static IBufferArrayGL standardQuad2DVAO = null;
    public static IBufferArrayGL standardQuad3DVAO = null;
    public static IBufferGL standardQuadVBO = null;

    public static IShaderGL vshLocal = null;
    public static List<VSHInputLayoutParser.ShaderInput> vshLocalLayout = null;

    public static void init() {
        if (standardQuad2DVAO == null) {
            standardQuad2DVAO = GL11.createGLBufferArray();
            standardQuad3DVAO = GL11.createGLBufferArray();
            standardQuadVBO = _wglGenBuffers();

            FloatBuffer verts = EagRuntime.allocateFloatBuffer(18);
            verts.put(new float[]{-1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, -1.0f, 1.0f, 0.0f});
            verts.flip();

            GL11.bindVAOGLArrayBufferNow(standardQuadVBO);
            _wglBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);
            EagRuntime.freeFloatBuffer(verts);

            GL11.bindGLBufferArray(standardQuad2DVAO);

            GL11.enableVertexAttribArray(0);
            GL11.vertexAttribPointer(0, 2, GL_FLOAT, false, 12, 0);

            GL11.bindGLBufferArray(standardQuad3DVAO);

            GL11.enableVertexAttribArray(0);
            GL11.vertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
        }

        if (vshLocal == null) {
            vshLocalLayout = VSHInputLayoutParser.getShaderInputs(vertexSource);
            vshLocal = _wglCreateShader(GL_VERTEX_SHADER);

            _wglShaderSource(vshLocal, GLSLHeader.getVertexHeaderCompat(vertexSource, vertexShaderPrecision));
            _wglCompileShader(vshLocal);

            if (_wglGetShaderi(vshLocal, GL_COMPILE_STATUS) != GL_TRUE) {
                GL11.logger.error("Failed to compile GL_VERTEX_SHADER local!");
                String log = _wglGetShaderInfoLog(vshLocal);
                if (log != null) {
                    String[] lines = log.split("(\\r\\n|\\r|\\n)");
                    for (int i = 0; i < lines.length; ++i) {
                        GL11.logger.error("[VERT] {}", lines[i]);
                    }
                }
                throw new IllegalStateException("Vertex shader local could not be compiled!");
            }
        }
    }

    public static void drawStandardQuad2D() {
        GL11.bindGLBufferArray(standardQuad2DVAO);
        GL11.doDrawArrays(GL_TRIANGLES, 0, 6);
    }

    public static void drawStandardQuad3D() {
        GL11.bindGLBufferArray(standardQuad3DVAO);
        GL11.doDrawArrays(GL_TRIANGLES, 0, 6);
    }

    public static void destroy() {
        if (standardQuad2DVAO != null) {
            GL11.destroyGLBufferArray(standardQuad2DVAO);
            standardQuad2DVAO = null;
        }
        if (standardQuad3DVAO != null) {
            GL11.destroyGLBufferArray(standardQuad3DVAO);
            standardQuad3DVAO = null;
        }
        if (standardQuadVBO != null) {
            _wglDeleteBuffers(standardQuadVBO);
            standardQuadVBO = null;
        }
        if (vshLocal != null) {
            vshLocal.free();
            vshLocal = null;
            vshLocalLayout = null;
        }
    }
}
