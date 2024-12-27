package net.lax1dude.eaglercraft.lwjgl.opengl;

import net.lax1dude.eaglercraft.internal.PlatformOpenGL;

/**
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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
public class GLSLHeader {

    public static final String GLES2_COMPAT = "#line 2 6969\r\n"
            + "\r\n"
            + "/*\r\n"
            + " * Copyright (c) 2024 lax1dude. All Rights Reserved.\r\n"
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
            + "#ifdef EAGLER_HAS_GLES_300\r\n"
            + "\r\n"
            + "// For GLES 3.00+ (WebGL 2.0)\r\n"
            + "#ifdef EAGLER_IS_VERTEX_SHADER\r\n"
            + "\r\n"
            + "// Vertex Shaders:\r\n"
            + "#define EAGLER_VSH_LAYOUT_BEGIN()\r\n"
            + "#define EAGLER_VSH_LAYOUT_END()\r\n"
            + "#define EAGLER_IN(_loc, _type, _name) layout(location = _loc) in _type _name;\r\n"
            + "#define EAGLER_IN_AUTO(_type, _name) in _type _name;\r\n"
            + "#define EAGLER_OUT(_type, _name) out _type _name;\r\n"
            + "#define EAGLER_VERT_POSITION gl_Position\r\n"
            + "\r\n"
            + "#else\r\n"
            + "#ifdef EAGLER_IS_FRAGMENT_SHADER\r\n"
            + "\r\n"
            + "// Fragment Shaders:\r\n"
            + "#define EAGLER_IN(_type, _name) in _type _name;\r\n"
            + "#define EAGLER_FRAG_COLOR eagler_FragColor\r\n"
            + "#define EAGLER_FRAG_DEPTH gl_FragDepth\r\n"
            + "\r\n"
            + "#define EAGLER_FRAG_OUT() layout(location = 0) out vec4 EAGLER_FRAG_COLOR;\r\n"
            + "\r\n"
            + "#endif\r\n"
            + "#endif\r\n"
            + "\r\n"
            + "// All Shaders:\r\n"
            + "\r\n"
            + "#define EAGLER_TEXTURE_2D(tex, coord2f) texture(tex, coord2f)\r\n"
            + "#define EAGLER_TEXTURE_2D_LOD(_tex, _coord2f, _lod1f) textureLod(_tex, _coord2f, _lod1f)\r\n"
            + "#define EAGLER_HAS_TEXTURE_2D_LOD\r\n"
            + "\r\n"
            + "\r\n"
            + "#else\r\n"
            + "#ifdef EAGLER_HAS_GLES_200\r\n"
            + "\r\n"
            + "// For GLES 2.00 (WebGL 1.0)\r\n"
            + "#ifdef EAGLER_IS_VERTEX_SHADER\r\n"
            + "\r\n"
            + "// Vertex Shaders:\r\n"
            + "#define EAGLER_VSH_LAYOUT_BEGIN()\r\n"
            + "#define EAGLER_VSH_LAYOUT_END()\r\n"
            + "#define EAGLER_IN(_loc, _type, _name) attribute _type _name;\r\n"
            + "#define EAGLER_IN_AUTO(_type, _name) attribute _type _name;\r\n"
            + "#define EAGLER_OUT(_type, _name) varying _type _name;\r\n"
            + "#define EAGLER_VERT_POSITION gl_Position\r\n"
            + "\r\n"
            + "#else\r\n"
            + "#ifdef EAGLER_IS_FRAGMENT_SHADER\r\n"
            + "\r\n"
            + "// Fragment Shaders:\r\n"
            + "#define EAGLER_IN(_type, _name) varying _type _name;\r\n"
            + "#define EAGLER_FRAG_COLOR gl_FragColor\r\n"
            + "// TODO: Must require EXT_frag_depth to use this on GLES 2.0 (currently not needed)\r\n"
            + "#define EAGLER_FRAG_DEPTH gl_FragDepth\r\n"
            + "\r\n"
            + "#define EAGLER_FRAG_OUT()\r\n"
            + "\r\n"
            + "#endif\r\n"
            + "#endif\r\n"
            + "\r\n"
            + "// All Shaders:\r\n"
            + "\r\n"
            + "#define EAGLER_TEXTURE_2D(_tex, _coord2f) texture2D(_tex, _coord2f)\r\n"
            + "\r\n"
            + "#ifdef EAGLER_HAS_GLES_200_SHADER_TEXTURE_LOD\r\n"
            + "#define EAGLER_TEXTURE_2D_LOD(_tex, _coord2f, _lod1f) texture2DLodEXT(_tex, _coord2f, _lod1f)\r\n"
            + "#define EAGLER_HAS_TEXTURE_2D_LOD\r\n"
            + "#else\r\n"
            + "// Beware!\r\n"
            + "#define EAGLER_TEXTURE_2D_LOD(_tex, _coord2f, _lod1f) texture2D(_tex, _coord2f)\r\n"
            + "#define EAGLER_HAS_TEXTURE_2D_LOD\r\n"
            + "#endif\r\n"
            + "\r\n"
            + "#else\r\n"
            + "#error Unable to determine API version! (Missing directive EAGLER_HAS_GLES_200 or 300)\r\n"
            + "#endif\r\n"
            + "#endif\r\n"
            + "\r\n"
            + "#line 1 0";

    private static String header = null;

    static void init() {
        int glesVersion = GL11.checkOpenGLESVersion();
        StringBuilder headerBuilder;
        if (glesVersion >= 310) {
            headerBuilder = new StringBuilder("#version 310 es");
            boolean oes5 = PlatformOpenGL.checkOESGPUShader5Capable();
            boolean ext5 = !oes5 && PlatformOpenGL.checkEXTGPUShader5Capable();
            if (oes5) {
                headerBuilder.append("\n#extension GL_OES_gpu_shader5 : enable");
            } else if (ext5) {
                headerBuilder.append("\n#extension GL_EXT_gpu_shader5 : enable");
            }
            headerBuilder.append("\n#define EAGLER_IS_GLES_310");
            headerBuilder.append("\n#define EAGLER_HAS_GLES_310");
            headerBuilder.append("\n#define EAGLER_HAS_GLES_300");
            headerBuilder.append("\n#define EAGLER_HAS_GLES_200");
            if (oes5 || ext5) {
                headerBuilder.append("\n#define EAGLER_HAS_GLES_310_SHADER_5");
            }
        } else if (glesVersion == 300) {
            headerBuilder = new StringBuilder("#version 300 es");
            headerBuilder.append("\n#define EAGLER_IS_GLES_300");
            headerBuilder.append("\n#define EAGLER_HAS_GLES_300");
            headerBuilder.append("\n#define EAGLER_HAS_GLES_200");
        } else if (glesVersion == 200) {
            boolean texLOD = PlatformOpenGL.checkTextureLODCapable();
            headerBuilder = new StringBuilder("#version 100");
            if (texLOD) {
                headerBuilder.append("\n#extension GL_EXT_shader_texture_lod : enable");
            }
            headerBuilder.append("\n#define EAGLER_HAS_GLES_200");
            headerBuilder.append("\n#define EAGLER_IS_GLES_200");
            if (texLOD) {
                headerBuilder.append("\n#define EAGLER_HAS_GLES_200_SHADER_TEXTURE_LOD");
            }
        } else {
            throw new IllegalStateException("Unsupported OpenGL ES version: " + glesVersion);
        }
        header = headerBuilder.append('\n').toString();
    }

    static void destroy() {
        header = null;
    }

    public static String getHeader() {
        if (header == null) throw new IllegalStateException();
        return header;
    }

    public static String getVertexHeader(String shaderSrc) {
        if (header == null) throw new IllegalStateException();
        return header + "#define EAGLER_IS_VERTEX_SHADER\n" + shaderSrc;
    }

    public static String getFragmentHeader(String shaderSrc) {
        if (header == null) throw new IllegalStateException();
        return header + "#define EAGLER_IS_FRAGMENT_SHADER\n" + shaderSrc;
    }

    public static String getVertexHeaderCompat(String shaderSrc, String precisions) {
        if (header == null) throw new IllegalStateException();
        return header + "#define EAGLER_IS_VERTEX_SHADER\n" + (precisions == null ? "" : precisions + "\n")
                + GLES2_COMPAT + "\n" + shaderSrc;
    }

    public static String getFragmentHeaderCompat(String shaderSrc, String precisions) {
        if (header == null) throw new IllegalStateException();
        return header + "#define EAGLER_IS_FRAGMENT_SHADER\n" + (precisions == null ? "" : precisions + "\n")
                + GLES2_COMPAT + "\n" + shaderSrc;
    }
}
