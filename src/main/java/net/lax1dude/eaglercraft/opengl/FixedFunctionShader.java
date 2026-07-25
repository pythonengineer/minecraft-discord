package net.lax1dude.eaglercraft.opengl;

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
public class FixedFunctionShader {

    public class FixedFunctionState {

        public static final int fixedFunctionStatesCount = 12;
        public static final int fixedFunctionStatesBits = (1 << 12) - 1;
        public static final int extentionStateBits = fixedFunctionStatesBits ^ 0xFFFFFFFF;

        public static final int STATE_HAS_ATTRIB_TEXTURE = 1;
        public static final int STATE_HAS_ATTRIB_COLOR = 2;
        public static final int STATE_HAS_ATTRIB_NORMAL = 4;
        public static final int STATE_HAS_ATTRIB_LIGHTMAP = 8;
        public static final int STATE_ENABLE_TEXTURE2D = 16;
        public static final int STATE_ENABLE_LIGHTMAP = 32;
        public static final int STATE_ENABLE_ALPHA_TEST = 64;
        public static final int STATE_ENABLE_MC_LIGHTING = 128;
        public static final int STATE_ENABLE_END_PORTAL = 256;
        public static final int STATE_ENABLE_ANISOTROPIC_FIX = 512;
        public static final int STATE_ENABLE_FOG = 1024;
        public static final int STATE_ENABLE_BLEND_ADD = 2048;

    }

    public class FixedFunctionConstants {

        public static final String VSH = "#line 2\r\n"
                + "\r\n"
                + "/*\r\n"
                + " * Copyright (c) 2022-2025 lax1dude. All Rights Reserved.\r\n"
                + " *\r\n"
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
                + " *\r\n"
                + " */\r\n"
                + "\r\n"
                + "EAGLER_IN_AUTO(vec3, a_position3f)\r\n"
                + "\r\n"
                + "#if defined(COMPILE_ENABLE_TEX_GEN) || defined(COMPILE_ENABLE_FOG)\r\n"
                + "#define _COMPILE_VARYING_POSITION\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef _COMPILE_VARYING_POSITION\r\n"
                + "EAGLER_OUT(vec4, v_position4f)\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_TEX_GEN\r\n"
                + "EAGLER_OUT(vec3, v_objectPosition3f)\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_TEXTURE_ATTRIB\r\n"
                + "EAGLER_IN_AUTO(vec2, a_texture2f)\r\n"
                + "EAGLER_OUT(vec2, v_texture2f)\r\n"
                + "uniform mat4 u_textureMat4f01;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_COLOR_ATTRIB\r\n"
                + "EAGLER_IN_AUTO(vec4, a_color4f)\r\n"
                + "EAGLER_OUT(vec4, v_color4f)\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_NORMAL_ATTRIB\r\n"
                + "EAGLER_IN_AUTO(vec4, a_normal4f)\r\n"
                + "EAGLER_OUT(vec3, v_normal3f)\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_LIGHTMAP_ATTRIB\r\n"
                + "EAGLER_IN_AUTO(vec2, a_lightmap2f)\r\n"
                + "EAGLER_OUT(vec2, v_lightmap2f)\r\n"
                + "uniform mat4 u_textureMat4f02;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef _COMPILE_VARYING_POSITION\r\n"
                + "uniform mat4 u_modelviewMat4f;\r\n"
                + "uniform mat4 u_projectionMat4f;\r\n"
                + "#else\r\n"
                + "uniform mat4 u_modelviewProjMat4f;\r\n"
                + "#ifdef COMPILE_NORMAL_ATTRIB\r\n"
                + "uniform mat4 u_modelviewMat4f;\r\n"
                + "#endif\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#define TEX_MAT3(mat4In) mat3(mat4In[0].xyw,mat4In[1].xyw,mat4In[3].xyw)\r\n"
                + "\r\n"
                + "void main() {\r\n"
                + "#ifdef COMPILE_ENABLE_TEX_GEN\r\n"
                + "    v_objectPosition3f = a_position3f;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef _COMPILE_VARYING_POSITION\r\n"
                + "    v_position4f = u_modelviewMat4f * vec4(a_position3f, 1.0);\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_TEXTURE_ATTRIB\r\n"
                + "    vec3 v_textureTmp3f = TEX_MAT3(u_textureMat4f01) * vec3(a_texture2f, 1.0);\r\n"
                + "    v_texture2f = v_textureTmp3f.xy / v_textureTmp3f.z;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_COLOR_ATTRIB\r\n"
                + "    v_color4f = a_color4f;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_NORMAL_ATTRIB\r\n"
                + "    mat3 normalMat3f = mat3(u_modelviewMat4f);\r\n"
                + "    vec3 normalIn3f = a_normal4f.xyz;\r\n"
                + "    if(dot(normalMat3f[0], cross(normalMat3f[1], normalMat3f[2])) < 0.0) {\r\n"
                + "        normalIn3f.x = -normalIn3f.x;\r\n"
                + "    }\r\n"
                + "    v_normal3f = normalize(normalMat3f * normalIn3f);\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_LIGHTMAP_ATTRIB\r\n"
                + "    vec3 v_lightmapTmp3f = TEX_MAT3(u_textureMat4f02) * vec3(a_lightmap2f, 1.0);\r\n"
                + "    v_lightmap2f = v_lightmapTmp3f.xy / v_lightmapTmp3f.z;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef _COMPILE_VARYING_POSITION\r\n"
                + "    EAGLER_VERT_POSITION = u_projectionMat4f * v_position4f;\r\n"
                + "#else\r\n"
                + "    EAGLER_VERT_POSITION = u_modelviewProjMat4f * vec4(a_position3f, 1.0);\r\n"
                + "#endif\r\n"
                + "}\r\n"
                + "";
        public static final String FSH = "#line 2\r\n"
                + "\r\n"
                + "/*\r\n"
                + " * Copyright (c) 2022-2025 lax1dude. All Rights Reserved.\r\n"
                + " *\r\n"
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
                + " *\r\n"
                + " */\r\n"
                + "\r\n"
                + "#if defined(COMPILE_ENABLE_TEX_GEN) || defined(COMPILE_ENABLE_FOG)\r\n"
                + "EAGLER_IN(vec4, v_position4f)\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_TEXTURE_ATTRIB\r\n"
                + "EAGLER_IN(vec2, v_texture2f)\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "uniform vec4 u_color4f;\r\n"
                + "\r\n"
                + "#ifdef COMPILE_BLEND_ADD\r\n"
                + "uniform vec4 u_colorBlendSrc4f;\r\n"
                + "uniform vec4 u_colorBlendAdd4f;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_COLOR_ATTRIB\r\n"
                + "EAGLER_IN(vec4, v_color4f)\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_NORMAL_ATTRIB\r\n"
                + "EAGLER_IN(vec3, v_normal3f)\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_LIGHTMAP_ATTRIB\r\n"
                + "EAGLER_IN(vec2, v_lightmap2f)\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_TEXTURE2D\r\n"
                + "uniform sampler2D u_samplerTexture;\r\n"
                + "#if !defined(COMPILE_TEXTURE_ATTRIB) && !defined(COMPILE_ENABLE_TEX_GEN)\r\n"
                + "uniform vec2 u_textureCoords01;\r\n"
                + "#endif\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_LIGHTMAP\r\n"
                + "uniform sampler2D u_samplerLightmap;\r\n"
                + "#ifndef COMPILE_LIGHTMAP_ATTRIB\r\n"
                + "uniform vec2 u_textureCoords02;\r\n"
                + "#endif\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_ALPHA_TEST\r\n"
                + "uniform float u_alphaTestRef1f;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_MC_LIGHTING\r\n"
                + "uniform int u_lightsEnabled1i;\r\n"
                + "uniform vec4 u_lightsDirections4fv[2];\r\n"
                + "uniform vec3 u_lightsAmbient3f;\r\n"
                + "#ifndef COMPILE_NORMAL_ATTRIB\r\n"
                + "uniform vec3 u_uniformNormal3f;\r\n"
                + "#endif\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_FOG\r\n"
                + "uniform vec4 u_fogParameters4f;\r\n"
                + "uniform vec4 u_fogColor4f;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_TEX_GEN\r\n"
                + "EAGLER_IN(vec3, v_objectPosition3f)\r\n"
                + "uniform ivec4 u_texGenPlane4i;\r\n"
                + "uniform vec4 u_texGenS4f;\r\n"
                + "uniform vec4 u_texGenT4f;\r\n"
                + "uniform vec4 u_texGenR4f;\r\n"
                + "uniform vec4 u_texGenQ4f;\r\n"
                + "uniform mat4 u_textureMat4f01;\r\n"
                + "uniform mat4 u_textureMat4f02;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_ANISOTROPIC_FIX\r\n"
                + "uniform vec2 u_textureAnisotropicFix;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "EAGLER_FRAG_OUT()\r\n"
                + "\r\n"
                + "void main() {\r\n"
                + "\r\n"
                + "#ifdef COMPILE_COLOR_ATTRIB\r\n"
                + "    vec4 color = v_color4f;\r\n"
                + "#else\r\n"
                + "    vec4 color = u_color4f;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_TEX_GEN\r\n"
                + "    vec4 tmpVec4 = vec4(v_objectPosition3f, 1.0);\r\n"
                + "    vec4 texGenVector;\r\n"
                + "\r\n"
                + "    texGenVector.x = dot(u_texGenPlane4i.x == 1 ? v_position4f : tmpVec4, u_texGenS4f);\r\n"
                + "    texGenVector.y = dot(u_texGenPlane4i.y == 1 ? v_position4f : tmpVec4, u_texGenT4f);\r\n"
                + "    texGenVector.z = dot(u_texGenPlane4i.z == 1 ? v_position4f : tmpVec4, u_texGenR4f);\r\n"
                + "    texGenVector.w = dot(u_texGenPlane4i.w == 1 ? v_position4f : tmpVec4, u_texGenQ4f);\r\n"
                + "#ifdef EAGLER_HAS_GLES_300\r\n"
                + "    texGenVector.xyz = mat4x3(\r\n"
                + "        u_textureMat4f01[0].xyw,\r\n"
                + "        u_textureMat4f01[1].xyw,\r\n"
                + "        u_textureMat4f01[2].xyw,\r\n"
                + "        u_textureMat4f01[3].xyw\r\n"
                + "    ) * texGenVector;\r\n"
                + "    texGenVector.xy /= texGenVector.z;\r\n"
                + "#else\r\n"
                + "\r\n"
                + "    texGenVector = u_textureMat4f01 * texGenVector;\r\n"
                + "    texGenVector.xy /= texGenVector.w;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_TEXTURE2D\r\n"
                + "#ifdef COMPILE_TEXTURE_ATTRIB\r\n"
                + "#ifdef COMPILE_ENABLE_ANISOTROPIC_FIX\r\n"
                + "    // d3d11 doesn't support GL_NEAREST upscaling with anisotropic\r\n"
                + "    // filtering enabled, so it needs this stupid fix to 'work'\r\n"
                + "    vec2 uv = floor(v_texture2f * u_textureAnisotropicFix) + 0.5;\r\n"
                + "    color *= EAGLER_TEXTURE_2D(u_samplerTexture, uv / u_textureAnisotropicFix);\r\n"
                + "#else\r\n"
                + "    color *= EAGLER_TEXTURE_2D(u_samplerTexture, v_texture2f);\r\n"
                + "#endif\r\n"
                + "#else\r\n"
                + "    color *= EAGLER_TEXTURE_2D(u_samplerTexture, texGenVector.xy);\r\n"
                + "#endif\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_LIGHTMAP\r\n"
                + "    vec4 lightmap;\r\n"
                + "\r\n"
                + "    lightmap.x = dot(u_texGenPlane4i.x == 1 ? v_position4f : tmpVec4, u_texGenS4f);\r\n"
                + "    lightmap.y = dot(u_texGenPlane4i.y == 1 ? v_position4f : tmpVec4, u_texGenT4f);\r\n"
                + "    lightmap.z = 0.0;\r\n"
                + "    lightmap.w = 1.0;\r\n"
                + "    color *= EAGLER_TEXTURE_2D(u_samplerLightmap, (u_textureMat4f02 * lightmap).xy);\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_ALPHA_TEST\r\n"
                + "    if(color.a < u_alphaTestRef1f) discard;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#else\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_TEXTURE2D\r\n"
                + "#ifdef COMPILE_TEXTURE_ATTRIB\r\n"
                + "#ifdef COMPILE_ENABLE_ANISOTROPIC_FIX\r\n"
                + "    // d3d11 doesn't support GL_NEAREST upscaling with anisotropic\r\n"
                + "    // filtering enabled, so it needs this stupid fix to 'work'\r\n"
                + "    vec2 uv = floor(v_texture2f * u_textureAnisotropicFix) + 0.5;\r\n"
                + "    color *= EAGLER_TEXTURE_2D(u_samplerTexture, uv / u_textureAnisotropicFix);\r\n"
                + "#else\r\n"
                + "    color *= EAGLER_TEXTURE_2D(u_samplerTexture, v_texture2f);\r\n"
                + "#endif\r\n"
                + "#else\r\n"
                + "    color *= EAGLER_TEXTURE_2D(u_samplerTexture, u_textureCoords01);\r\n"
                + "#endif\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_LIGHTMAP\r\n"
                + "#ifdef COMPILE_LIGHTMAP_ATTRIB\r\n"
                + "    color *= EAGLER_TEXTURE_2D(u_samplerLightmap, v_lightmap2f);\r\n"
                + "#else\r\n"
                + "    color *= EAGLER_TEXTURE_2D(u_samplerLightmap, u_textureCoords02);\r\n"
                + "#endif\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_BLEND_ADD\r\n"
                + "    color = color * u_colorBlendSrc4f + u_colorBlendAdd4f;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_ALPHA_TEST\r\n"
                + "    if(color.a <= u_alphaTestRef1f) discard;\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_MC_LIGHTING\r\n"
                + "#ifdef COMPILE_NORMAL_ATTRIB\r\n"
                + "    vec3 normal = v_normal3f;\r\n"
                + "#else\r\n"
                + "    vec3 normal = u_uniformNormal3f;\r\n"
                + "#endif\r\n"
                + "    vec4 light;\r\n"
                + "    float diffuse = 0.0;\r\n"
                + "    for(int i = 0; i < 2; ++i) {\r\n"
                + "        if(i >= u_lightsEnabled1i) {\r\n"
                + "            break;\r\n"
                + "        }\r\n"
                + "        light = u_lightsDirections4fv[i];\r\n"
                + "        diffuse += max(dot(light.xyz, normal), 0.0) * light.w;\r\n"
                + "    }\r\n"
                + "    color.rgb *= min(u_lightsAmbient3f + vec3(diffuse), 1.0);\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "#ifdef COMPILE_ENABLE_FOG\r\n"
                + "    vec3 fogPos = v_position4f.xyz / v_position4f.w;\r\n"
                + "    float dist = length(fogPos);\r\n"
                + "    float fogDensity = u_fogParameters4f.y;\r\n"
                + "    float fogStart = u_fogParameters4f.z;\r\n"
                + "    float fogEnd = u_fogParameters4f.w;\r\n"
                + "    float f = u_fogParameters4f.x > 0.0 ? 1.0 - exp(-fogDensity * dist) :\r\n"
                + "        (dist - fogStart) / (fogEnd - fogStart);\r\n"
                + "    color.rgb = mix(color.rgb, u_fogColor4f.rgb, clamp(f, 0.0, 1.0) * u_fogColor4f.a);\r\n"
                + "#endif\r\n"
                + "\r\n"
                + "    EAGLER_FRAG_COLOR = color;\r\n"
                + "}\r\n";

        public static final String PRECISION_INT = "mediump";
        public static final String PRECISION_FLOAT = "highp";
        public static final String PRECISION_SAMPLER = "mediump";

        public static final String MACRO_ATTRIB_TEXTURE = "COMPILE_TEXTURE_ATTRIB";
        public static final String MACRO_ATTRIB_COLOR = "COMPILE_COLOR_ATTRIB";
        public static final String MACRO_ATTRIB_NORMAL = "COMPILE_NORMAL_ATTRIB";
        public static final String MACRO_ATTRIB_LIGHTMAP = "COMPILE_LIGHTMAP_ATTRIB";

        public static final String MACRO_ENABLE_TEXTURE2D = "COMPILE_ENABLE_TEXTURE2D";
        public static final String MACRO_ENABLE_LIGHTMAP = "COMPILE_ENABLE_LIGHTMAP";
        public static final String MACRO_ENABLE_ALPHA_TEST = "COMPILE_ENABLE_ALPHA_TEST";
        public static final String MACRO_ENABLE_MC_LIGHTING = "COMPILE_ENABLE_MC_LIGHTING";
        public static final String MACRO_ENABLE_END_PORTAL = "COMPILE_ENABLE_TEX_GEN";
        public static final String MACRO_ENABLE_ANISOTROPIC_FIX = "COMPILE_ENABLE_ANISOTROPIC_FIX";
        public static final String MACRO_ENABLE_FOG = "COMPILE_ENABLE_FOG";
        public static final String MACRO_ENABLE_BLEND_ADD = "COMPILE_BLEND_ADD";

        public static final String ATTRIB_POSITION = "a_position3f";
        public static final String ATTRIB_TEXTURE = "a_texture2f";
        public static final String ATTRIB_COLOR = "a_color4f";
        public static final String ATTRIB_NORMAL = "a_normal4f";
        public static final String ATTRIB_LIGHTMAP = "a_lightmap2f";

        public static final String UNIFORM_COLOR_NAME = "u_color4f";
        public static final String UNIFORM_BLEND_SRC_COLOR_NAME = "u_colorBlendSrc4f";
        public static final String UNIFORM_BLEND_ADD_COLOR_NAME = "u_colorBlendAdd4f";
        public static final String UNIFORM_ALPHA_TEST_NAME = "u_alphaTestRef1f";
        public static final String UNIFORM_LIGHTS_ENABLED_NAME = "u_lightsEnabled1i";
        public static final String UNIFORM_LIGHTS_VECTORS_NAME = "u_lightsDirections4fv";
        public static final String UNIFORM_LIGHTS_AMBIENT_NAME = "u_lightsAmbient3f";
        public static final String UNIFORM_CONSTANT_NORMAL_NAME = "u_uniformNormal3f";
        public static final String UNIFORM_FOG_PARAM_NAME = "u_fogParameters4f";
        public static final String UNIFORM_FOG_COLOR_NAME = "u_fogColor4f";
        public static final String UNIFORM_TEX_GEN_S_NAME = "u_texGenS4f";
        public static final String UNIFORM_TEX_GEN_T_NAME = "u_texGenT4f";
        public static final String UNIFORM_TEX_GEN_R_NAME = "u_texGenR4f";
        public static final String UNIFORM_TEX_GEN_Q_NAME = "u_texGenQ4f";
        public static final String UNIFORM_MODEL_MATRIX_NAME = "u_modelviewMat4f";
        public static final String UNIFORM_TEX_GEN_PLANE_NAME = "u_texGenPlane4i";
        public static final String UNIFORM_PROJECTION_MATRIX_NAME = "u_projectionMat4f";
        public static final String UNIFORM_MODEL_PROJECTION_MATRIX_NAME = "u_modelviewProjMat4f";
        public static final String UNIFORM_TEXTURE_COORDS_01_NAME = "u_textureCoords01";
        public static final String UNIFORM_TEXTURE_MATRIX_01_NAME = "u_textureMat4f01";
        public static final String UNIFORM_TEXTURE_COORDS_02_NAME = "u_textureCoords02";
        public static final String UNIFORM_TEXTURE_MATRIX_02_NAME = "u_textureMat4f02";
        public static final String UNIFORM_TEXTURE_ANISOTROPIC_FIX = "u_textureAnisotropicFix";

        public static final String UNIFORM_TEXTURE_UNIT_01_NAME = "u_samplerTexture";
        public static final String UNIFORM_TEXTURE_UNIT_02_NAME = "u_samplerLightmap";

        public static final String OUTPUT_COLOR = "output4f";
    }
}
