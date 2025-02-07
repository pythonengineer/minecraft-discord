package net.lax1dude.eaglercraft.lwjgl.opengl;

import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.log4j.LogManager;
import net.lax1dude.eaglercraft.log4j.Logger;
import net.lax1dude.eaglercraft.lwjgl.opengl.DisplayList.ListOperation;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.lax1dude.eaglercraft.opengl.DrawUtils;
import net.lax1dude.eaglercraft.opengl.VertexFormat;
import net.lax1dude.eaglercraft.vector.Matrix4f;
import net.lax1dude.eaglercraft.vector.Vector3f;
import net.lax1dude.eaglercraft.vector.Vector4f;

import java.util.HashMap;
import java.util.Map;

import com.mojang.minecraft.renderer.Tesselator;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.GLObjectMap;
import net.lax1dude.eaglercraft.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.internal.IBufferGL;
import net.lax1dude.eaglercraft.internal.IProgramGL;
import net.lax1dude.eaglercraft.internal.ITextureGL;
import net.lax1dude.eaglercraft.internal.PlatformOpenGL;

import static net.lax1dude.eaglercraft.internal.PlatformOpenGL.*;

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
public class GL11 {

    public static final int GL_ACCUM = 256;
    public static final int GL_LOAD = 257;
    public static final int GL_RETURN = 258;
    public static final int GL_MULT = 259;
    public static final int GL_ADD = 260;
    public static final int GL_NEVER = 512;
    public static final int GL_LESS = 513;
    public static final int GL_EQUAL = 514;
    public static final int GL_LEQUAL = 515;
    public static final int GL_GREATER = 516;
    public static final int GL_NOTEQUAL = 517;
    public static final int GL_GEQUAL = 518;
    public static final int GL_ALWAYS = 519;
    public static final int GL_CURRENT_BIT = 1;
    public static final int GL_POINT_BIT = 2;
    public static final int GL_LINE_BIT = 4;
    public static final int GL_POLYGON_BIT = 8;
    public static final int GL_POLYGON_STIPPLE_BIT = 16;
    public static final int GL_PIXEL_MODE_BIT = 32;
    public static final int GL_LIGHTING_BIT = 64;
    public static final int GL_FOG_BIT = 128;
    public static final int GL_DEPTH_BUFFER_BIT = 256;
    public static final int GL_ACCUM_BUFFER_BIT = 512;
    public static final int GL_STENCIL_BUFFER_BIT = 1024;
    public static final int GL_VIEWPORT_BIT = 2048;
    public static final int GL_TRANSFORM_BIT = 4096;
    public static final int GL_ENABLE_BIT = 8192;
    public static final int GL_COLOR_BUFFER_BIT = 16384;
    public static final int GL_HINT_BIT = 32768;
    public static final int GL_EVAL_BIT = 65536;
    public static final int GL_LIST_BIT = 131072;
    public static final int GL_TEXTURE_BIT = 262144;
    public static final int GL_SCISSOR_BIT = 524288;
    public static final int GL_ALL_ATTRIB_BITS = 1048575;
    public static final int GL_POINTS = 0;
    public static final int GL_LINES = 1;
    public static final int GL_LINE_LOOP = 2;
    public static final int GL_LINE_STRIP = 3;
    public static final int GL_TRIANGLES = 4;
    public static final int GL_TRIANGLE_STRIP = 5;
    public static final int GL_TRIANGLE_FAN = 6;
    public static final int GL_QUADS = 7;
    public static final int GL_QUAD_STRIP = 8;
    public static final int GL_POLYGON = 9;
    public static final int GL_ZERO = 0;
    public static final int GL_ONE = 1;
    public static final int GL_SRC_COLOR = 768;
    public static final int GL_ONE_MINUS_SRC_COLOR = 769;
    public static final int GL_SRC_ALPHA = 770;
    public static final int GL_ONE_MINUS_SRC_ALPHA = 771;
    public static final int GL_DST_ALPHA = 772;
    public static final int GL_ONE_MINUS_DST_ALPHA = 773;
    public static final int GL_DST_COLOR = 774;
    public static final int GL_ONE_MINUS_DST_COLOR = 775;
    public static final int GL_SRC_ALPHA_SATURATE = 776;
    public static final int GL_CONSTANT_COLOR = 32769;
    public static final int GL_ONE_MINUS_CONSTANT_COLOR = 32770;
    public static final int GL_CONSTANT_ALPHA = 32771;
    public static final int GL_ONE_MINUS_CONSTANT_ALPHA = 32772;
    public static final int GL_TRUE = 1;
    public static final int GL_FALSE = 0;
    public static final int GL_CLIP_PLANE0 = 12288;
    public static final int GL_CLIP_PLANE1 = 12289;
    public static final int GL_CLIP_PLANE2 = 12290;
    public static final int GL_CLIP_PLANE3 = 12291;
    public static final int GL_CLIP_PLANE4 = 12292;
    public static final int GL_CLIP_PLANE5 = 12293;
    public static final int GL_BYTE = 5120;
    public static final int GL_UNSIGNED_BYTE = 5121;
    public static final int GL_SHORT = 5122;
    public static final int GL_UNSIGNED_SHORT = 5123;
    public static final int GL_INT = 5124;
    public static final int GL_UNSIGNED_INT = 5125;
    public static final int GL_FLOAT = 5126;
    public static final int GL_2_BYTES = 5127;
    public static final int GL_3_BYTES = 5128;
    public static final int GL_4_BYTES = 5129;
    public static final int GL_DOUBLE = 5130;
    public static final int GL_NONE = 0;
    public static final int GL_FRONT_LEFT = 1024;
    public static final int GL_FRONT_RIGHT = 1025;
    public static final int GL_BACK_LEFT = 1026;
    public static final int GL_BACK_RIGHT = 1027;
    public static final int GL_FRONT = 1028;
    public static final int GL_BACK = 1029;
    public static final int GL_LEFT = 1030;
    public static final int GL_RIGHT = 1031;
    public static final int GL_FRONT_AND_BACK = 1032;
    public static final int GL_AUX0 = 1033;
    public static final int GL_AUX1 = 1034;
    public static final int GL_AUX2 = 1035;
    public static final int GL_AUX3 = 1036;
    public static final int GL_NO_ERROR = 0;
    public static final int GL_INVALID_ENUM = 1280;
    public static final int GL_INVALID_VALUE = 1281;
    public static final int GL_INVALID_OPERATION = 1282;
    public static final int GL_STACK_OVERFLOW = 1283;
    public static final int GL_STACK_UNDERFLOW = 1284;
    public static final int GL_OUT_OF_MEMORY = 1285;
    public static final int GL_2D = 1536;
    public static final int GL_3D = 1537;
    public static final int GL_3D_COLOR = 1538;
    public static final int GL_3D_COLOR_TEXTURE = 1539;
    public static final int GL_4D_COLOR_TEXTURE = 1540;
    public static final int GL_PASS_THROUGH_TOKEN = 1792;
    public static final int GL_POINT_TOKEN = 1793;
    public static final int GL_LINE_TOKEN = 1794;
    public static final int GL_POLYGON_TOKEN = 1795;
    public static final int GL_BITMAP_TOKEN = 1796;
    public static final int GL_DRAW_PIXEL_TOKEN = 1797;
    public static final int GL_COPY_PIXEL_TOKEN = 1798;
    public static final int GL_LINE_RESET_TOKEN = 1799;
    public static final int GL_EXP = 2048;
    public static final int GL_EXP2 = 2049;
    public static final int GL_CW = 2304;
    public static final int GL_CCW = 2305;
    public static final int GL_COEFF = 2560;
    public static final int GL_ORDER = 2561;
    public static final int GL_DOMAIN = 2562;
    public static final int GL_CURRENT_COLOR = 2816;
    public static final int GL_CURRENT_INDEX = 2817;
    public static final int GL_CURRENT_NORMAL = 2818;
    public static final int GL_CURRENT_TEXTURE_COORDS = 2819;
    public static final int GL_CURRENT_RASTER_COLOR = 2820;
    public static final int GL_CURRENT_RASTER_INDEX = 2821;
    public static final int GL_CURRENT_RASTER_TEXTURE_COORDS = 2822;
    public static final int GL_CURRENT_RASTER_POSITION = 2823;
    public static final int GL_CURRENT_RASTER_POSITION_VALID = 2824;
    public static final int GL_CURRENT_RASTER_DISTANCE = 2825;
    public static final int GL_POINT_SMOOTH = 2832;
    public static final int GL_POINT_SIZE = 2833;
    public static final int GL_POINT_SIZE_RANGE = 2834;
    public static final int GL_POINT_SIZE_GRANULARITY = 2835;
    public static final int GL_LINE_SMOOTH = 2848;
    public static final int GL_LINE_WIDTH = 2849;
    public static final int GL_LINE_WIDTH_RANGE = 2850;
    public static final int GL_LINE_WIDTH_GRANULARITY = 2851;
    public static final int GL_LINE_STIPPLE = 2852;
    public static final int GL_LINE_STIPPLE_PATTERN = 2853;
    public static final int GL_LINE_STIPPLE_REPEAT = 2854;
    public static final int GL_LIST_MODE = 2864;
    public static final int GL_MAX_LIST_NESTING = 2865;
    public static final int GL_LIST_BASE = 2866;
    public static final int GL_LIST_INDEX = 2867;
    public static final int GL_POLYGON_MODE = 2880;
    public static final int GL_POLYGON_SMOOTH = 2881;
    public static final int GL_POLYGON_STIPPLE = 2882;
    public static final int GL_EDGE_FLAG = 2883;
    public static final int GL_CULL_FACE = 2884;
    public static final int GL_CULL_FACE_MODE = 2885;
    public static final int GL_FRONT_FACE = 2886;
    public static final int GL_LIGHTING = 2896;
    public static final int GL_LIGHT_MODEL_LOCAL_VIEWER = 2897;
    public static final int GL_LIGHT_MODEL_TWO_SIDE = 2898;
    public static final int GL_LIGHT_MODEL_AMBIENT = 2899;
    public static final int GL_SHADE_MODEL = 2900;
    public static final int GL_COLOR_MATERIAL_FACE = 2901;
    public static final int GL_COLOR_MATERIAL_PARAMETER = 2902;
    public static final int GL_COLOR_MATERIAL = 2903;
    public static final int GL_FOG = 2912;
    public static final int GL_FOG_INDEX = 2913;
    public static final int GL_FOG_DENSITY = 2914;
    public static final int GL_FOG_START = 2915;
    public static final int GL_FOG_END = 2916;
    public static final int GL_FOG_MODE = 2917;
    public static final int GL_FOG_COLOR = 2918;
    public static final int GL_DEPTH_RANGE = 2928;
    public static final int GL_DEPTH_TEST = 2929;
    public static final int GL_DEPTH_WRITEMASK = 2930;
    public static final int GL_DEPTH_CLEAR_VALUE = 2931;
    public static final int GL_DEPTH_FUNC = 2932;
    public static final int GL_ACCUM_CLEAR_VALUE = 2944;
    public static final int GL_STENCIL_TEST = 2960;
    public static final int GL_STENCIL_CLEAR_VALUE = 2961;
    public static final int GL_STENCIL_FUNC = 2962;
    public static final int GL_STENCIL_VALUE_MASK = 2963;
    public static final int GL_STENCIL_FAIL = 2964;
    public static final int GL_STENCIL_PASS_DEPTH_FAIL = 2965;
    public static final int GL_STENCIL_PASS_DEPTH_PASS = 2966;
    public static final int GL_STENCIL_REF = 2967;
    public static final int GL_STENCIL_WRITEMASK = 2968;
    public static final int GL_MATRIX_MODE = 2976;
    public static final int GL_NORMALIZE = 2977;
    public static final int GL_VIEWPORT = 2978;
    public static final int GL_MODELVIEW_STACK_DEPTH = 2979;
    public static final int GL_PROJECTION_STACK_DEPTH = 2980;
    public static final int GL_TEXTURE_STACK_DEPTH = 2981;
    public static final int GL_MODELVIEW_MATRIX = 2982;
    public static final int GL_PROJECTION_MATRIX = 2983;
    public static final int GL_TEXTURE_MATRIX = 2984;
    public static final int GL_ATTRIB_STACK_DEPTH = 2992;
    public static final int GL_CLIENT_ATTRIB_STACK_DEPTH = 2993;
    public static final int GL_ALPHA_TEST = 3008;
    public static final int GL_ALPHA_TEST_FUNC = 3009;
    public static final int GL_ALPHA_TEST_REF = 3010;
    public static final int GL_DITHER = 3024;
    public static final int GL_BLEND_DST = 3040;
    public static final int GL_BLEND_SRC = 3041;
    public static final int GL_BLEND = 3042;
    public static final int GL_LOGIC_OP_MODE = 3056;
    public static final int GL_INDEX_LOGIC_OP = 3057;
    public static final int GL_COLOR_LOGIC_OP = 3058;
    public static final int GL_AUX_BUFFERS = 3072;
    public static final int GL_DRAW_BUFFER = 3073;
    public static final int GL_READ_BUFFER = 3074;
    public static final int GL_SCISSOR_BOX = 3088;
    public static final int GL_SCISSOR_TEST = 3089;
    public static final int GL_INDEX_CLEAR_VALUE = 3104;
    public static final int GL_INDEX_WRITEMASK = 3105;
    public static final int GL_COLOR_CLEAR_VALUE = 3106;
    public static final int GL_COLOR_WRITEMASK = 3107;
    public static final int GL_INDEX_MODE = 3120;
    public static final int GL_RGBA_MODE = 3121;
    public static final int GL_DOUBLEBUFFER = 3122;
    public static final int GL_STEREO = 3123;
    public static final int GL_RENDER_MODE = 3136;
    public static final int GL_PERSPECTIVE_CORRECTION_HINT = 3152;
    public static final int GL_POINT_SMOOTH_HINT = 3153;
    public static final int GL_LINE_SMOOTH_HINT = 3154;
    public static final int GL_POLYGON_SMOOTH_HINT = 3155;
    public static final int GL_FOG_HINT = 3156;
    public static final int GL_TEXTURE_GEN_S = 3168;
    public static final int GL_TEXTURE_GEN_T = 3169;
    public static final int GL_TEXTURE_GEN_R = 3170;
    public static final int GL_TEXTURE_GEN_Q = 3171;
    public static final int GL_PIXEL_MAP_I_TO_I = 3184;
    public static final int GL_PIXEL_MAP_S_TO_S = 3185;
    public static final int GL_PIXEL_MAP_I_TO_R = 3186;
    public static final int GL_PIXEL_MAP_I_TO_G = 3187;
    public static final int GL_PIXEL_MAP_I_TO_B = 3188;
    public static final int GL_PIXEL_MAP_I_TO_A = 3189;
    public static final int GL_PIXEL_MAP_R_TO_R = 3190;
    public static final int GL_PIXEL_MAP_G_TO_G = 3191;
    public static final int GL_PIXEL_MAP_B_TO_B = 3192;
    public static final int GL_PIXEL_MAP_A_TO_A = 3193;
    public static final int GL_PIXEL_MAP_I_TO_I_SIZE = 3248;
    public static final int GL_PIXEL_MAP_S_TO_S_SIZE = 3249;
    public static final int GL_PIXEL_MAP_I_TO_R_SIZE = 3250;
    public static final int GL_PIXEL_MAP_I_TO_G_SIZE = 3251;
    public static final int GL_PIXEL_MAP_I_TO_B_SIZE = 3252;
    public static final int GL_PIXEL_MAP_I_TO_A_SIZE = 3253;
    public static final int GL_PIXEL_MAP_R_TO_R_SIZE = 3254;
    public static final int GL_PIXEL_MAP_G_TO_G_SIZE = 3255;
    public static final int GL_PIXEL_MAP_B_TO_B_SIZE = 3256;
    public static final int GL_PIXEL_MAP_A_TO_A_SIZE = 3257;
    public static final int GL_UNPACK_SWAP_BYTES = 3312;
    public static final int GL_UNPACK_LSB_FIRST = 3313;
    public static final int GL_UNPACK_ROW_LENGTH = 3314;
    public static final int GL_UNPACK_SKIP_ROWS = 3315;
    public static final int GL_UNPACK_SKIP_PIXELS = 3316;
    public static final int GL_UNPACK_ALIGNMENT = 3317;
    public static final int GL_PACK_SWAP_BYTES = 3328;
    public static final int GL_PACK_LSB_FIRST = 3329;
    public static final int GL_PACK_ROW_LENGTH = 3330;
    public static final int GL_PACK_SKIP_ROWS = 3331;
    public static final int GL_PACK_SKIP_PIXELS = 3332;
    public static final int GL_PACK_ALIGNMENT = 3333;
    public static final int GL_MAP_COLOR = 3344;
    public static final int GL_MAP_STENCIL = 3345;
    public static final int GL_INDEX_SHIFT = 3346;
    public static final int GL_INDEX_OFFSET = 3347;
    public static final int GL_RED_SCALE = 3348;
    public static final int GL_RED_BIAS = 3349;
    public static final int GL_ZOOM_X = 3350;
    public static final int GL_ZOOM_Y = 3351;
    public static final int GL_GREEN_SCALE = 3352;
    public static final int GL_GREEN_BIAS = 3353;
    public static final int GL_BLUE_SCALE = 3354;
    public static final int GL_BLUE_BIAS = 3355;
    public static final int GL_ALPHA_SCALE = 3356;
    public static final int GL_ALPHA_BIAS = 3357;
    public static final int GL_DEPTH_SCALE = 3358;
    public static final int GL_DEPTH_BIAS = 3359;
    public static final int GL_MAX_EVAL_ORDER = 3376;
    public static final int GL_MAX_LIGHTS = 3377;
    public static final int GL_MAX_CLIP_PLANES = 3378;
    public static final int GL_MAX_TEXTURE_SIZE = 3379;
    public static final int GL_MAX_PIXEL_MAP_TABLE = 3380;
    public static final int GL_MAX_ATTRIB_STACK_DEPTH = 3381;
    public static final int GL_MAX_MODELVIEW_STACK_DEPTH = 3382;
    public static final int GL_MAX_NAME_STACK_DEPTH = 3383;
    public static final int GL_MAX_PROJECTION_STACK_DEPTH = 3384;
    public static final int GL_MAX_TEXTURE_STACK_DEPTH = 3385;
    public static final int GL_MAX_VIEWPORT_DIMS = 3386;
    public static final int GL_MAX_CLIENT_ATTRIB_STACK_DEPTH = 3387;
    public static final int GL_SUBPIXEL_BITS = 3408;
    public static final int GL_INDEX_BITS = 3409;
    public static final int GL_RED_BITS = 3410;
    public static final int GL_GREEN_BITS = 3411;
    public static final int GL_BLUE_BITS = 3412;
    public static final int GL_ALPHA_BITS = 3413;
    public static final int GL_DEPTH_BITS = 3414;
    public static final int GL_STENCIL_BITS = 3415;
    public static final int GL_ACCUM_RED_BITS = 3416;
    public static final int GL_ACCUM_GREEN_BITS = 3417;
    public static final int GL_ACCUM_BLUE_BITS = 3418;
    public static final int GL_ACCUM_ALPHA_BITS = 3419;
    public static final int GL_NAME_STACK_DEPTH = 3440;
    public static final int GL_AUTO_NORMAL = 3456;
    public static final int GL_MAP1_COLOR_4 = 3472;
    public static final int GL_MAP1_INDEX = 3473;
    public static final int GL_MAP1_NORMAL = 3474;
    public static final int GL_MAP1_TEXTURE_COORD_1 = 3475;
    public static final int GL_MAP1_TEXTURE_COORD_2 = 3476;
    public static final int GL_MAP1_TEXTURE_COORD_3 = 3477;
    public static final int GL_MAP1_TEXTURE_COORD_4 = 3478;
    public static final int GL_MAP1_VERTEX_3 = 3479;
    public static final int GL_MAP1_VERTEX_4 = 3480;
    public static final int GL_MAP2_COLOR_4 = 3504;
    public static final int GL_MAP2_INDEX = 3505;
    public static final int GL_MAP2_NORMAL = 3506;
    public static final int GL_MAP2_TEXTURE_COORD_1 = 3507;
    public static final int GL_MAP2_TEXTURE_COORD_2 = 3508;
    public static final int GL_MAP2_TEXTURE_COORD_3 = 3509;
    public static final int GL_MAP2_TEXTURE_COORD_4 = 3510;
    public static final int GL_MAP2_VERTEX_3 = 3511;
    public static final int GL_MAP2_VERTEX_4 = 3512;
    public static final int GL_MAP1_GRID_DOMAIN = 3536;
    public static final int GL_MAP1_GRID_SEGMENTS = 3537;
    public static final int GL_MAP2_GRID_DOMAIN = 3538;
    public static final int GL_MAP2_GRID_SEGMENTS = 3539;
    public static final int GL_TEXTURE_1D = 3552;
    public static final int GL_TEXTURE_2D = 3553;
    public static final int GL_FEEDBACK_BUFFER_POINTER = 3568;
    public static final int GL_FEEDBACK_BUFFER_SIZE = 3569;
    public static final int GL_FEEDBACK_BUFFER_TYPE = 3570;
    public static final int GL_SELECTION_BUFFER_POINTER = 3571;
    public static final int GL_SELECTION_BUFFER_SIZE = 3572;
    public static final int GL_TEXTURE_WIDTH = 4096;
    public static final int GL_TEXTURE_HEIGHT = 4097;
    public static final int GL_TEXTURE_INTERNAL_FORMAT = 4099;
    public static final int GL_TEXTURE_BORDER_COLOR = 4100;
    public static final int GL_TEXTURE_BORDER = 4101;
    public static final int GL_DONT_CARE = 4352;
    public static final int GL_FASTEST = 4353;
    public static final int GL_NICEST = 4354;
    public static final int GL_LIGHT0 = 16384;
    public static final int GL_LIGHT1 = 16385;
    public static final int GL_LIGHT2 = 16386;
    public static final int GL_LIGHT3 = 16387;
    public static final int GL_LIGHT4 = 16388;
    public static final int GL_LIGHT5 = 16389;
    public static final int GL_LIGHT6 = 16390;
    public static final int GL_LIGHT7 = 16391;
    public static final int GL_AMBIENT = 4608;
    public static final int GL_DIFFUSE = 4609;
    public static final int GL_SPECULAR = 4610;
    public static final int GL_POSITION = 4611;
    public static final int GL_SPOT_DIRECTION = 4612;
    public static final int GL_SPOT_EXPONENT = 4613;
    public static final int GL_SPOT_CUTOFF = 4614;
    public static final int GL_CONSTANT_ATTENUATION = 4615;
    public static final int GL_LINEAR_ATTENUATION = 4616;
    public static final int GL_QUADRATIC_ATTENUATION = 4617;
    public static final int GL_COMPILE = 4864;
    public static final int GL_COMPILE_AND_EXECUTE = 4865;
    public static final int GL_CLEAR = 5376;
    public static final int GL_AND = 5377;
    public static final int GL_AND_REVERSE = 5378;
    public static final int GL_COPY = 5379;
    public static final int GL_AND_INVERTED = 5380;
    public static final int GL_NOOP = 5381;
    public static final int GL_XOR = 5382;
    public static final int GL_OR = 5383;
    public static final int GL_NOR = 5384;
    public static final int GL_EQUIV = 5385;
    public static final int GL_INVERT = 5386;
    public static final int GL_OR_REVERSE = 5387;
    public static final int GL_COPY_INVERTED = 5388;
    public static final int GL_OR_INVERTED = 5389;
    public static final int GL_NAND = 5390;
    public static final int GL_SET = 5391;
    public static final int GL_EMISSION = 5632;
    public static final int GL_SHININESS = 5633;
    public static final int GL_AMBIENT_AND_DIFFUSE = 5634;
    public static final int GL_COLOR_INDEXES = 5635;
    public static final int GL_MODELVIEW = 5888;
    public static final int GL_PROJECTION = 5889;
    public static final int GL_TEXTURE = 5890;
    public static final int GL_COLOR = 6144;
    public static final int GL_DEPTH = 6145;
    public static final int GL_STENCIL = 6146;
    public static final int GL_COLOR_INDEX = 6400;
    public static final int GL_STENCIL_INDEX = 6401;
    public static final int GL_DEPTH_COMPONENT = 6402;
    public static final int GL_RED = 6403;
    public static final int GL_GREEN = 6404;
    public static final int GL_BLUE = 6405;
    public static final int GL_ALPHA = 6406;
    public static final int GL_RGB = 6407;
    public static final int GL_RGBA = 6408;
    public static final int GL_LUMINANCE = 6409;
    public static final int GL_LUMINANCE_ALPHA = 6410;
    public static final int GL_BITMAP = 6656;
    public static final int GL_POINT = 6912;
    public static final int GL_LINE = 6913;
    public static final int GL_FILL = 6914;
    public static final int GL_RENDER = 7168;
    public static final int GL_FEEDBACK = 7169;
    public static final int GL_SELECT = 7170;
    public static final int GL_FLAT = 7424;
    public static final int GL_SMOOTH = 7425;
    public static final int GL_KEEP = 7680;
    public static final int GL_REPLACE = 7681;
    public static final int GL_INCR = 7682;
    public static final int GL_DECR = 7683;
    public static final int GL_VENDOR = 7936;
    public static final int GL_RENDERER = 7937;
    public static final int GL_VERSION = 7938;
    public static final int GL_EXTENSIONS = 7939;
    public static final int GL_S = 8192;
    public static final int GL_T = 8193;
    public static final int GL_R = 8194;
    public static final int GL_Q = 8195;
    public static final int GL_MODULATE = 8448;
    public static final int GL_DECAL = 8449;
    public static final int GL_TEXTURE_ENV_MODE = 8704;
    public static final int GL_TEXTURE_ENV_COLOR = 8705;
    public static final int GL_TEXTURE_ENV = 8960;
    public static final int GL_EYE_LINEAR = 9216;
    public static final int GL_OBJECT_LINEAR = 9217;
    public static final int GL_SPHERE_MAP = 9218;
    public static final int GL_TEXTURE_GEN_MODE = 9472;
    public static final int GL_OBJECT_PLANE = 9473;
    public static final int GL_EYE_PLANE = 9474;
    public static final int GL_NEAREST = 9728;
    public static final int GL_LINEAR = 9729;
    public static final int GL_NEAREST_MIPMAP_NEAREST = 9984;
    public static final int GL_LINEAR_MIPMAP_NEAREST = 9985;
    public static final int GL_NEAREST_MIPMAP_LINEAR = 9986;
    public static final int GL_LINEAR_MIPMAP_LINEAR = 9987;
    public static final int GL_TEXTURE_MAG_FILTER = 10240;
    public static final int GL_TEXTURE_MIN_FILTER = 10241;
    public static final int GL_TEXTURE_WRAP_S = 10242;
    public static final int GL_TEXTURE_WRAP_T = 10243;
    public static final int GL_CLAMP = 10496;
    public static final int GL_REPEAT = 10497;
    public static final int GL_CLIENT_PIXEL_STORE_BIT = 1;
    public static final int GL_CLIENT_VERTEX_ARRAY_BIT = 2;
    public static final int GL_ALL_CLIENT_ATTRIB_BITS = -1;
    public static final int GL_POLYGON_OFFSET_FACTOR = 32824;
    public static final int GL_POLYGON_OFFSET_UNITS = 10752;
    public static final int GL_POLYGON_OFFSET_POINT = 10753;
    public static final int GL_POLYGON_OFFSET_LINE = 10754;
    public static final int GL_POLYGON_OFFSET_FILL = 32823;
    public static final int GL_ALPHA4 = 32827;
    public static final int GL_ALPHA8 = 32828;
    public static final int GL_ALPHA12 = 32829;
    public static final int GL_ALPHA16 = 32830;
    public static final int GL_LUMINANCE4 = 32831;
    public static final int GL_LUMINANCE8 = 32832;
    public static final int GL_LUMINANCE12 = 32833;
    public static final int GL_LUMINANCE16 = 32834;
    public static final int GL_LUMINANCE4_ALPHA4 = 32835;
    public static final int GL_LUMINANCE6_ALPHA2 = 32836;
    public static final int GL_LUMINANCE8_ALPHA8 = 32837;
    public static final int GL_LUMINANCE12_ALPHA4 = 32838;
    public static final int GL_LUMINANCE12_ALPHA12 = 32839;
    public static final int GL_LUMINANCE16_ALPHA16 = 32840;
    public static final int GL_INTENSITY = 32841;
    public static final int GL_INTENSITY4 = 32842;
    public static final int GL_INTENSITY8 = 32843;
    public static final int GL_INTENSITY12 = 32844;
    public static final int GL_INTENSITY16 = 32845;
    public static final int GL_R3_G3_B2 = 10768;
    public static final int GL_RGB4 = 32847;
    public static final int GL_RGB5 = 32848;
    public static final int GL_RGB8 = 32849;
    public static final int GL_RGB10 = 32850;
    public static final int GL_RGB12 = 32851;
    public static final int GL_RGB16 = 32852;
    public static final int GL_RGBA2 = 32853;
    public static final int GL_RGBA4 = 32854;
    public static final int GL_RGB5_A1 = 32855;
    public static final int GL_RGBA8 = 32856;
    public static final int GL_RGB10_A2 = 32857;
    public static final int GL_RGBA12 = 32858;
    public static final int GL_RGBA16 = 32859;
    public static final int GL_TEXTURE_RED_SIZE = 32860;
    public static final int GL_TEXTURE_GREEN_SIZE = 32861;
    public static final int GL_TEXTURE_BLUE_SIZE = 32862;
    public static final int GL_TEXTURE_ALPHA_SIZE = 32863;
    public static final int GL_TEXTURE_LUMINANCE_SIZE = 32864;
    public static final int GL_TEXTURE_INTENSITY_SIZE = 32865;
    public static final int GL_PROXY_TEXTURE_1D = 32867;
    public static final int GL_PROXY_TEXTURE_2D = 32868;
    public static final int GL_TEXTURE_PRIORITY = 32870;
    public static final int GL_TEXTURE_RESIDENT = 32871;
    public static final int GL_TEXTURE_BINDING_1D = 32872;
    public static final int GL_TEXTURE_BINDING_2D = 32873;
    public static final int GL_VERTEX_ARRAY = 32884;
    public static final int GL_NORMAL_ARRAY = 32885;
    public static final int GL_COLOR_ARRAY = 32886;
    public static final int GL_INDEX_ARRAY = 32887;
    public static final int GL_TEXTURE_COORD_ARRAY = 32888;
    public static final int GL_EDGE_FLAG_ARRAY = 32889;
    public static final int GL_VERTEX_ARRAY_SIZE = 32890;
    public static final int GL_VERTEX_ARRAY_TYPE = 32891;
    public static final int GL_VERTEX_ARRAY_STRIDE = 32892;
    public static final int GL_NORMAL_ARRAY_TYPE = 32894;
    public static final int GL_NORMAL_ARRAY_STRIDE = 32895;
    public static final int GL_COLOR_ARRAY_SIZE = 32897;
    public static final int GL_COLOR_ARRAY_TYPE = 32898;
    public static final int GL_COLOR_ARRAY_STRIDE = 32899;
    public static final int GL_INDEX_ARRAY_TYPE = 32901;
    public static final int GL_INDEX_ARRAY_STRIDE = 32902;
    public static final int GL_TEXTURE_COORD_ARRAY_SIZE = 32904;
    public static final int GL_TEXTURE_COORD_ARRAY_TYPE = 32905;
    public static final int GL_TEXTURE_COORD_ARRAY_STRIDE = 32906;
    public static final int GL_EDGE_FLAG_ARRAY_STRIDE = 32908;
    public static final int GL_VERTEX_ARRAY_POINTER = 32910;
    public static final int GL_NORMAL_ARRAY_POINTER = 32911;
    public static final int GL_COLOR_ARRAY_POINTER = 32912;
    public static final int GL_INDEX_ARRAY_POINTER = 32913;
    public static final int GL_TEXTURE_COORD_ARRAY_POINTER = 32914;
    public static final int GL_EDGE_FLAG_ARRAY_POINTER = 32915;
    public static final int GL_V2F = 10784;
    public static final int GL_V3F = 10785;
    public static final int GL_C4UB_V2F = 10786;
    public static final int GL_C4UB_V3F = 10787;
    public static final int GL_C3F_V3F = 10788;
    public static final int GL_N3F_V3F = 10789;
    public static final int GL_C4F_N3F_V3F = 10790;
    public static final int GL_T2F_V3F = 10791;
    public static final int GL_T4F_V4F = 10792;
    public static final int GL_T2F_C4UB_V3F = 10793;
    public static final int GL_T2F_C3F_V3F = 10794;
    public static final int GL_T2F_N3F_V3F = 10795;
    public static final int GL_T2F_C4F_N3F_V3F = 10796;
    public static final int GL_T4F_C4F_N3F_V4F = 10797;
    public static final int GL_LOGIC_OP = 3057;
    public static final int GL_TEXTURE_COMPONENTS = 4099;
    public static final int GL_TEXTURE_BINDING_3D = 32874;
    public static final int GL_PACK_SKIP_IMAGES = 32875;
    public static final int GL_PACK_IMAGE_HEIGHT = 32876;
    public static final int GL_UNPACK_SKIP_IMAGES = 32877;
    public static final int GL_UNPACK_IMAGE_HEIGHT = 32878;
    public static final int GL_TEXTURE_3D = 32879;
    public static final int GL_PROXY_TEXTURE_3D = 32880;
    public static final int GL_TEXTURE_DEPTH = 32881;
    public static final int GL_TEXTURE_WRAP_R = 32882;
    public static final int GL_MAX_3D_TEXTURE_SIZE = 32883;
    public static final int GL_BGR = 32992;
    public static final int GL_BGRA = 32993;
    public static final int GL_UNSIGNED_BYTE_3_3_2 = 32818;
    public static final int GL_UNSIGNED_BYTE_2_3_3_REV = 33634;
    public static final int GL_UNSIGNED_SHORT_5_6_5 = 33635;
    public static final int GL_UNSIGNED_SHORT_5_6_5_REV = 33636;
    public static final int GL_UNSIGNED_SHORT_4_4_4_4 = 32819;
    public static final int GL_UNSIGNED_SHORT_4_4_4_4_REV = 33637;
    public static final int GL_UNSIGNED_SHORT_5_5_5_1 = 32820;
    public static final int GL_UNSIGNED_SHORT_1_5_5_5_REV = 33638;
    public static final int GL_UNSIGNED_INT_8_8_8_8 = 32821;
    public static final int GL_UNSIGNED_INT_8_8_8_8_REV = 33639;
    public static final int GL_UNSIGNED_INT_10_10_10_2 = 32822;
    public static final int GL_UNSIGNED_INT_2_10_10_10_REV = 33640;
    public static final int GL_RESCALE_NORMAL = 32826;
    public static final int GL_LIGHT_MODEL_COLOR_CONTROL = 33272;
    public static final int GL_SINGLE_COLOR = 33273;
    public static final int GL_SEPARATE_SPECULAR_COLOR = 33274;
    public static final int GL_CLAMP_TO_EDGE = 33071;
    public static final int GL_TEXTURE_MIN_LOD = 33082;
    public static final int GL_TEXTURE_MAX_LOD = 33083;
    public static final int GL_TEXTURE_BASE_LEVEL = 33084;
    public static final int GL_TEXTURE_MAX_LEVEL = 33085;
    public static final int GL_MAX_ELEMENTS_VERTICES = 33000;
    public static final int GL_MAX_ELEMENTS_INDICES = 33001;
    public static final int GL_ALIASED_POINT_SIZE_RANGE = 33901;
    public static final int GL_ALIASED_LINE_WIDTH_RANGE = 33902;
    public static final int GL_SMOOTH_POINT_SIZE_RANGE = 2834;
    public static final int GL_SMOOTH_POINT_SIZE_GRANULARITY = 2835;
    public static final int GL_SMOOTH_LINE_WIDTH_RANGE = 2850;
    public static final int GL_SMOOTH_LINE_WIDTH_GRANULARITY = 2851;
    public static final int GL_TEXTURE0 = 33984;
    public static final int GL_TEXTURE1 = 33985;
    public static final int GL_TEXTURE2 = 33986;
    public static final int GL_TEXTURE3 = 33987;
    public static final int GL_TEXTURE4 = 33988;
    public static final int GL_TEXTURE5 = 33989;
    public static final int GL_TEXTURE6 = 33990;
    public static final int GL_TEXTURE7 = 33991;
    public static final int GL_TEXTURE8 = 33992;
    public static final int GL_TEXTURE9 = 33993;
    public static final int GL_TEXTURE10 = 33994;
    public static final int GL_TEXTURE11 = 33995;
    public static final int GL_TEXTURE12 = 33996;
    public static final int GL_TEXTURE13 = 33997;
    public static final int GL_TEXTURE14 = 33998;
    public static final int GL_TEXTURE15 = 33999;
    public static final int GL_TEXTURE16 = 34000;
    public static final int GL_TEXTURE17 = 34001;
    public static final int GL_TEXTURE18 = 34002;
    public static final int GL_TEXTURE19 = 34003;
    public static final int GL_TEXTURE20 = 34004;
    public static final int GL_TEXTURE21 = 34005;
    public static final int GL_TEXTURE22 = 34006;
    public static final int GL_TEXTURE23 = 34007;
    public static final int GL_TEXTURE24 = 34008;
    public static final int GL_TEXTURE25 = 34009;
    public static final int GL_TEXTURE26 = 34010;
    public static final int GL_TEXTURE27 = 34011;
    public static final int GL_TEXTURE28 = 34012;
    public static final int GL_TEXTURE29 = 34013;
    public static final int GL_TEXTURE30 = 34014;
    public static final int GL_TEXTURE31 = 34015;
    public static final int GL_ACTIVE_TEXTURE = 34016;
    public static final int GL_CLIENT_ACTIVE_TEXTURE = 34017;
    public static final int GL_MAX_TEXTURE_UNITS = 34018;
    public static final int GL_NORMAL_MAP = 34065;
    public static final int GL_REFLECTION_MAP = 34066;
    public static final int GL_TEXTURE_CUBE_MAP = 34067;
    public static final int GL_TEXTURE_BINDING_CUBE_MAP = 34068;
    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_X = 34069;
    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_X = 34070;
    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Y = 34071;
    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y = 34072;
    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Z = 34073;
    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z = 34074;
    public static final int GL_PROXY_TEXTURE_CUBE_MAP = 34075;
    public static final int GL_MAX_CUBE_MAP_TEXTURE_SIZE = 34076;
    public static final int GL_COMPRESSED_ALPHA = 34025;
    public static final int GL_COMPRESSED_LUMINANCE = 34026;
    public static final int GL_COMPRESSED_LUMINANCE_ALPHA = 34027;
    public static final int GL_COMPRESSED_INTENSITY = 34028;
    public static final int GL_COMPRESSED_RGB = 34029;
    public static final int GL_COMPRESSED_RGBA = 34030;
    public static final int GL_TEXTURE_COMPRESSION_HINT = 34031;
    public static final int GL_TEXTURE_COMPRESSED_IMAGE_SIZE = 34464;
    public static final int GL_TEXTURE_COMPRESSED = 34465;
    public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS = 34466;
    public static final int GL_COMPRESSED_TEXTURE_FORMATS = 34467;
    public static final int GL_MULTISAMPLE = 32925;
    public static final int GL_SAMPLE_ALPHA_TO_COVERAGE = 32926;
    public static final int GL_SAMPLE_ALPHA_TO_ONE = 32927;
    public static final int GL_SAMPLE_COVERAGE = 32928;
    public static final int GL_SAMPLE_BUFFERS = 32936;
    public static final int GL_SAMPLES = 32937;
    public static final int GL_SAMPLE_COVERAGE_VALUE = 32938;
    public static final int GL_SAMPLE_COVERAGE_INVERT = 32939;
    public static final int GL_MULTISAMPLE_BIT = 536870912;
    public static final int GL_TRANSPOSE_MODELVIEW_MATRIX = 34019;
    public static final int GL_TRANSPOSE_PROJECTION_MATRIX = 34020;
    public static final int GL_TRANSPOSE_TEXTURE_MATRIX = 34021;
    public static final int GL_TRANSPOSE_COLOR_MATRIX = 34022;
    public static final int GL_COMBINE = 34160;
    public static final int GL_COMBINE_RGB = 34161;
    public static final int GL_COMBINE_ALPHA = 34162;
    public static final int GL_SOURCE0_RGB = 34176;
    public static final int GL_SOURCE1_RGB = 34177;
    public static final int GL_SOURCE2_RGB = 34178;
    public static final int GL_SOURCE0_ALPHA = 34184;
    public static final int GL_SOURCE1_ALPHA = 34185;
    public static final int GL_SOURCE2_ALPHA = 34186;
    public static final int GL_OPERAND0_RGB = 34192;
    public static final int GL_OPERAND1_RGB = 34193;
    public static final int GL_OPERAND2_RGB = 34194;
    public static final int GL_OPERAND0_ALPHA = 34200;
    public static final int GL_OPERAND1_ALPHA = 34201;
    public static final int GL_OPERAND2_ALPHA = 34202;
    public static final int GL_RGB_SCALE = 34163;
    public static final int GL_ADD_SIGNED = 34164;
    public static final int GL_INTERPOLATE = 34165;
    public static final int GL_SUBTRACT = 34023;
    public static final int GL_CONSTANT = 34166;
    public static final int GL_PRIMARY_COLOR = 34167;
    public static final int GL_PREVIOUS = 34168;
    public static final int GL_DOT3_RGB = 34478;
    public static final int GL_DOT3_RGBA = 34479;
    public static final int GL_CLAMP_TO_BORDER = 33069;
    public static final int GL_ARRAY_BUFFER = 34962;
    public static final int GL_ELEMENT_ARRAY_BUFFER = 34963;
    public static final int GL_ARRAY_BUFFER_BINDING = 34964;
    public static final int GL_ELEMENT_ARRAY_BUFFER_BINDING = 34965;
    public static final int GL_VERTEX_ARRAY_BUFFER_BINDING = 34966;
    public static final int GL_NORMAL_ARRAY_BUFFER_BINDING = 34967;
    public static final int GL_COLOR_ARRAY_BUFFER_BINDING = 34968;
    public static final int GL_INDEX_ARRAY_BUFFER_BINDING = 34969;
    public static final int GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING = 34970;
    public static final int GL_EDGE_FLAG_ARRAY_BUFFER_BINDING = 34971;
    public static final int GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING = 34972;
    public static final int GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING = 34973;
    public static final int GL_WEIGHT_ARRAY_BUFFER_BINDING = 34974;
    public static final int GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 34975;
    public static final int GL_STREAM_DRAW = 35040;
    public static final int GL_STREAM_READ = 35041;
    public static final int GL_STREAM_COPY = 35042;
    public static final int GL_STATIC_DRAW = 35044;
    public static final int GL_STATIC_READ = 35045;
    public static final int GL_STATIC_COPY = 35046;
    public static final int GL_DYNAMIC_DRAW = 35048;
    public static final int GL_DYNAMIC_READ = 35049;
    public static final int GL_DYNAMIC_COPY = 35050;
    public static final int GL_READ_ONLY = 35000;
    public static final int GL_WRITE_ONLY = 35001;
    public static final int GL_READ_WRITE = 35002;
    public static final int GL_BUFFER_SIZE = 34660;
    public static final int GL_BUFFER_USAGE = 34661;
    public static final int GL_BUFFER_ACCESS = 35003;
    public static final int GL_BUFFER_MAPPED = 35004;
    public static final int GL_BUFFER_MAP_POINTER = 35005;
    public static final int GL_FOG_COORD_SRC = 33872;
    public static final int GL_FOG_COORD = 33873;
    public static final int GL_CURRENT_FOG_COORD = 33875;
    public static final int GL_FOG_COORD_ARRAY_TYPE = 33876;
    public static final int GL_FOG_COORD_ARRAY_STRIDE = 33877;
    public static final int GL_FOG_COORD_ARRAY_POINTER = 33878;
    public static final int GL_FOG_COORD_ARRAY = 33879;
    public static final int GL_FOG_COORD_ARRAY_BUFFER_BINDING = 34973;
    public static final int GL_SRC0_RGB = 34176;
    public static final int GL_SRC1_RGB = 34177;
    public static final int GL_SRC2_RGB = 34178;
    public static final int GL_SRC0_ALPHA = 34184;
    public static final int GL_SRC1_ALPHA = 34185;
    public static final int GL_SRC2_ALPHA = 34186;
    public static final int GL_SAMPLES_PASSED = 35092;
    public static final int GL_QUERY_COUNTER_BITS = 34916;
    public static final int GL_CURRENT_QUERY = 34917;
    public static final int GL_QUERY_RESULT = 34918;
    public static final int GL_QUERY_RESULT_AVAILABLE = 34919;
    public static final int GL_SHADING_LANGUAGE_VERSION = 35724;
    public static final int GL_CURRENT_PROGRAM = 35725;
    public static final int GL_SHADER_TYPE = 35663;
    public static final int GL_DELETE_STATUS = 35712;
    public static final int GL_COMPILE_STATUS = 35713;
    public static final int GL_LINK_STATUS = 35714;
    public static final int GL_VALIDATE_STATUS = 35715;
    public static final int GL_INFO_LOG_LENGTH = 35716;
    public static final int GL_ATTACHED_SHADERS = 35717;
    public static final int GL_ACTIVE_UNIFORMS = 35718;
    public static final int GL_ACTIVE_UNIFORM_MAX_LENGTH = 35719;
    public static final int GL_ACTIVE_ATTRIBUTES = 35721;
    public static final int GL_ACTIVE_ATTRIBUTE_MAX_LENGTH = 35722;
    public static final int GL_SHADER_SOURCE_LENGTH = 35720;
    public static final int GL_SHADER_OBJECT = 35656;
    public static final int GL_FLOAT_VEC2 = 35664;
    public static final int GL_FLOAT_VEC3 = 35665;
    public static final int GL_FLOAT_VEC4 = 35666;
    public static final int GL_INT_VEC2 = 35667;
    public static final int GL_INT_VEC3 = 35668;
    public static final int GL_INT_VEC4 = 35669;
    public static final int GL_BOOL = 35670;
    public static final int GL_BOOL_VEC2 = 35671;
    public static final int GL_BOOL_VEC3 = 35672;
    public static final int GL_BOOL_VEC4 = 35673;
    public static final int GL_FLOAT_MAT2 = 35674;
    public static final int GL_FLOAT_MAT3 = 35675;
    public static final int GL_FLOAT_MAT4 = 35676;
    public static final int GL_SAMPLER_1D = 35677;
    public static final int GL_SAMPLER_2D = 35678;
    public static final int GL_SAMPLER_3D = 35679;
    public static final int GL_SAMPLER_CUBE = 35680;
    public static final int GL_SAMPLER_1D_SHADOW = 35681;
    public static final int GL_SAMPLER_2D_SHADOW = 35682;
    public static final int GL_VERTEX_SHADER = 35633;
    public static final int GL_MAX_VERTEX_UNIFORM_COMPONENTS = 35658;
    public static final int GL_MAX_VARYING_FLOATS = 35659;
    public static final int GL_MAX_VERTEX_ATTRIBS = 34921;
    public static final int GL_MAX_TEXTURE_IMAGE_UNITS = 34930;
    public static final int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS = 35660;
    public static final int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = 35661;
    public static final int GL_MAX_TEXTURE_COORDS = 34929;
    public static final int GL_VERTEX_PROGRAM_POINT_SIZE = 34370;
    public static final int GL_VERTEX_PROGRAM_TWO_SIDE = 34371;
    public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED = 34338;
    public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE = 34339;
    public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE = 34340;
    public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE = 34341;
    public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED = 34922;
    public static final int GL_CURRENT_VERTEX_ATTRIB = 34342;
    public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER = 34373;
    public static final int GL_FRAGMENT_SHADER = 35632;
    public static final int GL_MAX_FRAGMENT_UNIFORM_COMPONENTS = 35657;
    public static final int GL_FRAGMENT_SHADER_DERIVATIVE_HINT = 35723;
    public static final int GL_MAX_DRAW_BUFFERS = 34852;
    public static final int GL_DRAW_BUFFER0 = 34853;
    public static final int GL_DRAW_BUFFER1 = 34854;
    public static final int GL_DRAW_BUFFER2 = 34855;
    public static final int GL_DRAW_BUFFER3 = 34856;
    public static final int GL_DRAW_BUFFER4 = 34857;
    public static final int GL_DRAW_BUFFER5 = 34858;
    public static final int GL_DRAW_BUFFER6 = 34859;
    public static final int GL_DRAW_BUFFER7 = 34860;
    public static final int GL_DRAW_BUFFER8 = 34861;
    public static final int GL_DRAW_BUFFER9 = 34862;
    public static final int GL_DRAW_BUFFER10 = 34863;
    public static final int GL_DRAW_BUFFER11 = 34864;
    public static final int GL_DRAW_BUFFER12 = 34865;
    public static final int GL_DRAW_BUFFER13 = 34866;
    public static final int GL_DRAW_BUFFER14 = 34867;
    public static final int GL_DRAW_BUFFER15 = 34868;
    public static final int GL_POINT_SPRITE = 34913;
    public static final int GL_COORD_REPLACE = 34914;
    public static final int GL_POINT_SPRITE_COORD_ORIGIN = 36000;
    public static final int GL_LOWER_LEFT = 36001;
    public static final int GL_UPPER_LEFT = 36002;
    public static final int GL_STENCIL_BACK_FUNC = 34816;
    public static final int GL_STENCIL_BACK_FAIL = 34817;
    public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL = 34818;
    public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS = 34819;
    public static final int GL_STENCIL_BACK_REF = 36003;
    public static final int GL_STENCIL_BACK_VALUE_MASK = 36004;
    public static final int GL_STENCIL_BACK_WRITEMASK = 36005;
    public static final int GL_BLEND_EQUATION_RGB = 32777;
    public static final int GL_BLEND_EQUATION_ALPHA = 34877;
    public static final int GL_TEXTURE_MAX_ANISOTROPY = 34046;
    public static final int GL_CONTEXT_LOST_WEBGL = -100;

    public static final int _GL_FRAMEBUFFER = 0x8D40;
    public static final int _GL_READ_FRAMEBUFFER = 0x8CA8;
    public static final int _GL_DRAW_FRAMEBUFFER = 0x8CA9;
    public static final int _GL_RENDERBUFFER = 0x8D41;
    public static final int _GL_COLOR_ATTACHMENT0 = 0x8CE0;
    public static final int _GL_COLOR_ATTACHMENT1 = 0x8CE1;
    public static final int _GL_COLOR_ATTACHMENT2 = 0x8CE2;
    public static final int _GL_COLOR_ATTACHMENT3 = 0x8CE3;
    public static final int _GL_DEPTH_ATTACHMENT = 0x8D00;
    public static final int _GL_DEPTH_COMPONENT = 0x1902;
    public static final int _GL_DEPTH_COMPONENT24 = 0x81A6;
    public static final int _GL_DEPTH_COMPONENT32F = 0x8CAC;
    public static final int _GL_R8 = 0x8229;
    public static final int _GL_RG = 0x8227;
    public static final int _GL_RG8 = 0x822B;
    public static final int _GL_RGB16F = 0x881B;
    public static final int _GL_HALF_FLOAT = 0x140B;
    public static final int _GL_UNIFORM_BUFFER = 0x8A11;
    public static final int _GL_TEXTURE_COMPARE_MODE = 0x884C;
    public static final int _GL_TEXTURE_COMPARE_FUNC = 0x884D;
    public static final int _GL_COMPARE_REF_TO_TEXTURE = 0x884E;

    static final GLObjectMap<ITextureGL> mapTexturesGL = new GLObjectMap<>(8192);
    private static final HashMap<Integer, DisplayList> displayLists = new HashMap<>();

    public static final Logger logger = LogManager.getLogger("GL11");

    static boolean emulatedVAOs = false;
    static SoftGLBufferState emulatedVAOState = new SoftGLBufferState();

    static boolean stateDepthTest = false;
    static boolean stateDepthTestStash = false;
    static int stateDepthFunc = -1;
    static boolean stateDepthMask = true;

    static boolean stateCull = false;
    static boolean stateCullStash = false;
    static int stateCullFace = GL_BACK;

    static boolean statePolygonOffset = false;
    static float statePolygonOffsetFactor = 0.0f;
    static float statePolygonOffsetUnits = 0.0f;

    static float stateColorR = 1.0f;
    static float stateColorG = 1.0f;
    static float stateColorB = 1.0f;
    static float stateColorA = 1.0f;
    static int stateColorSerial = 0;

    static float stateShaderBlendSrcColorR = 1.0f;
    static float stateShaderBlendSrcColorG = 1.0f;
    static float stateShaderBlendSrcColorB = 1.0f;
    static float stateShaderBlendSrcColorA = 1.0f;
    static float stateShaderBlendAddColorR = 0.0f;
    static float stateShaderBlendAddColorG = 0.0f;
    static float stateShaderBlendAddColorB = 0.0f;
    static float stateShaderBlendAddColorA = 0.0f;
    static int stateShaderBlendColorSerial = 0;
    static boolean stateEnableShaderBlendColor = false;

    static boolean stateBlend = false;
    static boolean stateBlendStash = false;
    static boolean stateGlobalBlend = true;
    static int stateBlendEquation = -1;
    static int stateBlendSRC = -1;
    static int stateBlendDST = -1;
    static boolean stateEnableOverlayFramebufferBlending = false;

    static boolean stateAlphaTest = false;
    static float stateAlphaTestRef = 0.1f;

    static boolean stateMaterial = false;
    static boolean stateLighting = false;
    static int stateLightsStackPointer = 0;
    static final boolean[][] stateLightsEnabled = new boolean[4][8];
    static final Vector4f[][] stateLightsStack = new Vector4f[4][8];
    static final int[] stateLightingSerial = new int[4];

    static float stateLightingAmbientR = 0.0f;
    static float stateLightingAmbientG = 0.0f;
    static float stateLightingAmbientB = 0.0f;
    static int stateLightingAmbientSerial = 0;

    static float stateNormalX = 0.0f;
    static float stateNormalY = 0.0f;
    static float stateNormalZ = -1.0f;
    static int stateNormalSerial = 0;

    static boolean stateFog = false;
    static boolean stateFogEXP = false;
    static float stateFogDensity = 1.0f;
    static float stateFogStart = 0.0f;
    static float stateFogEnd = 1.0f;
    static float stateFogColorR = 1.0f;
    static float stateFogColorG = 1.0f;
    static float stateFogColorB = 1.0f;
    static float stateFogColorA = 1.0f;
    static int stateFogSerial = 0;

    static int activeTexture = 0;
    static final boolean[] stateTexture = new boolean[16];
    static final int[] boundTexture = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

    static float stateAnisotropicFixW = -999.0f;
    static float stateAnisotropicFixH = -999.0f;
    static int stateAnisotropicFixSerial = 0;

    static boolean stateTexGen = false;

    static int viewportX = -1;
    static int viewportY = -1;
    static int viewportW = -1;
    static int viewportH = -1;

    static int colorMaskBits = 15;

    static float clearColorR = 0.0f;
    static float clearColorG = 0.0f;
    static float clearColorB = 0.0f;
    static float clearColorA = 1.0f;

    static double clearDepth = -999.0;

    public static enum TexGen {
        S, T, R, Q;

        int source = GL_OBJECT_LINEAR;
        int plane = GL_OBJECT_PLANE;
        Vector4f vector = new Vector4f();
    }

    static float blendConstantR = -999.0f;
    static float blendConstantG = -999.0f;
    static float blendConstantB = -999.0f;
    static float blendConstantA = -999.0f;

    static int stateTexGenSerial = 0;

    static int stateMatrixMode = GL_MODELVIEW;

    static final Matrix4f[] modelMatrixStack = new Matrix4f[48];
    static final int[] modelMatrixStackAccessSerial = new int[48];
    private static int modelMatrixAccessSerial = 0;
    static int modelMatrixStackPointer = 0;

    static final Matrix4f[] projectionMatrixStack = new Matrix4f[8];
    static final int[] projectionMatrixStackAccessSerial = new int[8];
    private static int projectionMatrixAccessSerial = 0;
    static int projectionMatrixStackPointer = 0;

    static final float[] textureCoordsX = new float[8];
    static final float[] textureCoordsY = new float[8];
    static final int[] textureCoordsAccessSerial = new int[8];

    static final Matrix4f[][] textureMatrixStack = new Matrix4f[8][8];
    static final int[][] textureMatrixStackAccessSerial = new int[8][8];
    static int[] textureMatrixAccessSerial = new int[8];
    static int[] textureMatrixStackPointer = new int[8];

    static boolean stateUseExtensionPipeline = false;

    private static final Matrix4f tmpInvertedMatrix = new Matrix4f();

    static {
        populateStack(modelMatrixStack);
        populateStack(projectionMatrixStack);
        populateStack(textureMatrixStack);
        populateStack(stateLightsStack);
    }

    static void populateStack(Matrix4f[] stack) {
        for (int i = 0; i < stack.length; ++i) {
            stack[i] = new Matrix4f();
        }
    }

    static void populateStack(Matrix4f[][] stack) {
        for (int i = 0; i < stack.length; ++i) {
            populateStack(stack[i]);
        }
    }

    static void populateStack(Vector4f[][] stack) {
        for (int i = 0; i < stack.length; ++i) {
            for (int j = 0; j < stack[i].length; ++j) {
                stack[i][j] = new Vector4f(0.0f, -1.0f, 0.0f, 0.0f);
            }
        }
    }

    public static final void pushLightCoords() {
        int push = stateLightsStackPointer + 1;
        if (push < stateLightsStack.length) {
            Vector4f[] copyFrom = stateLightsStack[stateLightsStackPointer];
            boolean[] copyFrom2 = stateLightsEnabled[stateLightsStackPointer];
            Vector4f[] copyTo = stateLightsStack[push];
            boolean[] copyTo2 = stateLightsEnabled[push];
            for (int i = 0; i < copyFrom.length; ++i) {
                if (copyFrom2[i]) {
                    copyTo[i].set(copyFrom[i]);
                    copyTo2[i] = true;
                } else {
                    copyTo2[i] = false;
                }
            }
            stateLightingSerial[push] = stateLightingSerial[stateLightsStackPointer];
            stateLightsStackPointer = push;
        } else {
            Throwable t = new IndexOutOfBoundsException("GL_LIGHT direction stack overflow!" + " Exceeded "
                    + stateLightsStack.length + " calls to GlStateManager.pushLightCoords");
            logger.error(t);
        }
    }

    public static final void popLightCoords() {
        if (stateLightsStackPointer > 0) {
            --stateLightsStackPointer;
        } else {
            Throwable t = new IndexOutOfBoundsException("GL_LIGHT direction stack underflow!"
                    + " Called GlStateManager.popLightCoords on an empty light stack");
            logger.error(t);
        }
    }

    public static final void glEnable(int var) {
        if (currentList != null) {
            currentList.ops.add(currentList.new ListOperation(var, true));
            return;
        }
        switch (var) {
            case GL_FOG:
                enableFog();
                break;
            case GL_LIGHTING:
                enableLighting();
                break;
            case GL_TEXTURE_2D:
                enableTexture2D();
                break;
            case GL_DEPTH_TEST:
                enableDepth();
                break;
            case GL_ALPHA_TEST:
                enableAlpha();
                break;
            case GL_BLEND:
                enableBlend();
                break;
            case GL_COLOR_MATERIAL:
                enableColorMaterial();
                break;
            default:
                _wglEnable(var);
        }
    }

    public static final void glDisable(int var) {
        if (currentList != null) {
            currentList.ops.add(currentList.new ListOperation(var, false));
            return;
        }
        switch (var) {
            case GL_FOG:
                disableFog();
                break;
            case GL_LIGHTING:
                disableLighting();
                break;
            case GL_TEXTURE_2D:
                disableTexture2D();
                break;
            case GL_DEPTH_TEST:
                disableDepth();
                break;
            case GL_ALPHA_TEST:
                disableAlpha();
                break;
            case GL_BLEND:
                disableBlend();
                break;
            case GL_COLOR_MATERIAL:
                disableColorMaterial();
                break;
            default:
                _wglDisable(var);
        }
    }

    public static final void disableAlpha() {
        stateAlphaTest = false;
    }

    public static final void enableAlpha() {
        stateAlphaTest = true;
    }

    public static final void glAlphaFunc(int func, float ref) {
        if (func != GL_GREATER) {
            throw new UnsupportedOperationException("Only GL_GREATER glAlphaFunc is supported");
        } else {
            stateAlphaTestRef = ref;
        }
    }

    public static final void enableLighting() {
        stateLighting = true;
    }

    public static final void disableLighting() {
        stateLighting = false;
    }

    public static final void enableExtensionPipeline() {
        stateUseExtensionPipeline = true;
    }

    public static final void disableExtensionPipeline() {
        stateUseExtensionPipeline = false;
    }

    public static final boolean isExtensionPipeline() {
        return stateUseExtensionPipeline;
    }

    private static final Vector4f paramVector4 = new Vector4f();

    public static final void enableMCLight(int light, float diffuse, double dirX, double dirY, double dirZ,
            double dirW) {
        paramVector4.x = (float)dirX;
        paramVector4.y = (float)dirY;
        paramVector4.z = (float)dirZ;
        paramVector4.w = (float)dirW;
        Matrix4f.transform(modelMatrixStack[modelMatrixStackPointer], paramVector4, paramVector4);
        paramVector4.normalise();
        Vector4f dest = stateLightsStack[stateLightsStackPointer][light];
        dest.x = paramVector4.x;
        dest.y = paramVector4.y;
        dest.z = paramVector4.z;
        dest.w = diffuse;
        stateLightsEnabled[stateLightsStackPointer][light] = true;
        ++stateLightingSerial[stateLightsStackPointer];
    }

    public static final void disableMCLight(int light) {
        stateLightsEnabled[stateLightsStackPointer][light] = false;
        ++stateLightingSerial[stateLightsStackPointer];
    }

    public static final void glLightModel(int type, FloatBuffer vector) {
        if (type != GL11.GL_LIGHT_MODEL_AMBIENT) {
            throw new UnsupportedOperationException("Only GL_LIGHT_MODEL_AMBIENT glLightModel is supported");
        }
        stateLightingAmbientR = vector.get();
        stateLightingAmbientG = vector.get();
        stateLightingAmbientB = vector.get();
        ++stateLightingAmbientSerial;
    }

    public static final void glColorMaterial(int face, int mode) {
    }

    public static final void enableColorMaterial() {
        stateMaterial = true;
    }

    public static final void disableColorMaterial() {
        stateMaterial = false;
    }

    public static final void disableDepth() {
        if (stateDepthTest) {
            _wglDisable(GL_DEPTH_TEST);
            stateDepthTest = false;
        }
    }

    public static final void enableDepth() {
        if (!stateDepthTest) {
            _wglEnable(GL_DEPTH_TEST);
            stateDepthTest = true;
        }
    }

    public static final void eagPushStateForGLES2BlitHack() {
        stateDepthTestStash = stateDepthTest;
        stateCullStash = stateCull;
        stateBlendStash = stateBlend;
    }

    public static final void eagPopStateForGLES2BlitHack() {
        if (stateDepthTestStash) {
            enableDepth();
        } else {
            disableDepth();
        }
        if (stateCullStash) {
            enableCull();
        } else {
            disableCull();
        }
        if (stateBlendStash) {
            enableBlend();
        } else {
            disableBlend();
        }
    }

    public static final void glDepthFunc(int depthFunc) {
        int rev = depthFunc;
        switch (depthFunc) {
            case GL_GREATER:
                rev = GL_LESS;
                break;
            case GL_GEQUAL:
                rev = GL_LEQUAL;
                break;
            case GL_EQUAL:
                rev = GL_EQUAL;
                break;
            case GL_LEQUAL:
                rev = GL_GEQUAL;
                break;
            case GL_LESS:
                rev = GL_GREATER;
                break;
        }
        if (rev != stateDepthFunc) {
            _wglDepthFunc(rev);
            stateDepthFunc = rev;
        }
    }

    public static final void glDepthMask(boolean flagIn) {
        if (flagIn != stateDepthMask) {
            _wglDepthMask(flagIn);
            stateDepthMask = flagIn;
        }
    }

    public static final void disableBlend() {
        if (stateBlend) {
            if (stateGlobalBlend) _wglDisable(GL_BLEND);
            stateBlend = false;
        }
    }

    public static final void enableBlend() {
        if (!stateBlend) {
            if (stateGlobalBlend) _wglEnable(GL_BLEND);
            stateBlend = true;
        }
    }

    public static final void globalDisableBlend() {
        if (stateBlend) {
            _wglDisable(GL_BLEND);
        }
        stateGlobalBlend = false;
    }

    public static final void globalEnableBlend() {
        if (stateBlend) {
            _wglEnable(GL_BLEND);
        }
        stateGlobalBlend = true;
    }

    public static final void glBlendFunc(int srcFactor, int dstFactor) {
        if (currentList != null) {
            currentList.ops.add(currentList.new ListOperation(GL_BLEND, srcFactor, dstFactor));
            return;
        }
        if (stateEnableOverlayFramebufferBlending) {
            tryBlendFuncSeparate(srcFactor, dstFactor, 0, 1);
            return;
        }
        int srcBits = (srcFactor | (srcFactor << 16));
        int dstBits = (dstFactor | (dstFactor << 16));
        if (srcBits != stateBlendSRC || dstBits != stateBlendDST) {
            _wglBlendFunc(srcFactor, dstFactor);
            stateBlendSRC = srcBits;
            stateBlendDST = dstBits;
        }
    }

    public static final void tryBlendFuncSeparate(int srcFactor, int dstFactor, int srcFactorAlpha,
            int dstFactorAlpha) {
        if (stateEnableOverlayFramebufferBlending) { // game overlay framebuffer in EntityRenderer.java
            srcFactorAlpha = GL_ONE;
            dstFactorAlpha = GL_ONE_MINUS_SRC_ALPHA;
        }
        int srcBits = (srcFactor | (srcFactorAlpha << 16));
        int dstBits = (dstFactor | (dstFactorAlpha << 16));
        if (srcBits != stateBlendSRC || dstBits != stateBlendDST) {
            _wglBlendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
            stateBlendSRC = srcBits;
            stateBlendDST = dstBits;
        }
    }

    public static final void enableOverlayFramebufferBlending() {
        stateEnableOverlayFramebufferBlending = true;
    }

    public static final void disableOverlayFramebufferBlending() {
        stateEnableOverlayFramebufferBlending = false;
    }

    public static final void setShaderBlendSrc(float r, float g, float b, float a) {
        stateShaderBlendSrcColorR = r;
        stateShaderBlendSrcColorG = g;
        stateShaderBlendSrcColorB = b;
        stateShaderBlendSrcColorA = a;
        ++stateShaderBlendColorSerial;
    }

    public static final void setShaderBlendAdd(float r, float g, float b, float a) {
        stateShaderBlendAddColorR = r;
        stateShaderBlendAddColorG = g;
        stateShaderBlendAddColorB = b;
        stateShaderBlendAddColorA = a;
        ++stateShaderBlendColorSerial;
    }

    public static final void enableShaderBlendAdd() {
        stateEnableShaderBlendColor = true;
    }

    public static final void disableShaderBlendAdd() {
        stateEnableShaderBlendColor = false;
    }

    public static final void setBlendConstants(float r, float g, float b, float a) {
        if (r != blendConstantR || g != blendConstantG || b != blendConstantB || a != blendConstantA) {
            _wglBlendColor(r, g, b, a);
            blendConstantR = r;
            blendConstantG = g;
            blendConstantB = b;
            blendConstantA = a;
        }
    }

    public static final void enableFog() {
        stateFog = true;
    }

    public static final void disableFog() {
        stateFog = false;
    }

    public static final void setFog(int param) {
        stateFogEXP = param == GL_EXP;
        ++stateFogSerial;
    }

    public static final void setFogDensity(float param) {
        stateFogDensity = param;
        ++stateFogSerial;
    }

    public static final void setFogStart(float param) {
        stateFogStart = param;
        ++stateFogSerial;
    }

    public static final void setFogEnd(float param) {
        stateFogEnd = param;
        ++stateFogSerial;
    }

    public static final void enableCull() {
        if (!stateCull) {
            _wglEnable(GL_CULL_FACE);
            stateCull = true;
        }
    }

    public static final void disableCull() {
        if (stateCull) {
            _wglDisable(GL_CULL_FACE);
            stateCull = false;
        }
    }

    public static final void glCullFace(int mode) {
        if (stateCullFace != mode) {
            _wglCullFace(mode);
            stateCullFace = mode;
        }
    }

    public static final void enablePolygonOffset() {
        if (!statePolygonOffset) {
            _wglEnable(GL_POLYGON_OFFSET_FILL);
            statePolygonOffset = true;
        }
    }

    public static final void disablePolygonOffset() {
        if (statePolygonOffset) {
            _wglDisable(GL_POLYGON_OFFSET_FILL);
            statePolygonOffset = false;
        }
    }

    public static final void doPolygonOffset(float factor, float units) {
        if (factor != statePolygonOffsetFactor || units != statePolygonOffsetUnits) {
            _wglPolygonOffset(-factor, units);
            statePolygonOffsetFactor = factor;
            statePolygonOffsetUnits = units;
        }
    }

    public static final void enableColorLogic() {
        throw new UnsupportedOperationException("Color logic op is not supported in OpenGL ES!");
    }

    public static final void disableColorLogic() {

    }

    public static final void colorLogicOp(int opcode) {

    }

    public static final void enableTexGen() {
        stateTexGen = true;
    }

    public static final void disableTexGen() {
        stateTexGen = false;
    }

    public static final void texGen(TexGen coord, int source) {
        coord.source = source;
        ++stateTexGenSerial;
    }

    public static final void func_179105_a(TexGen coord, int plane, FloatBuffer vector) {
        coord.plane = plane;
        coord.vector.load(vector);
        if (plane == GL_EYE_PLANE) {
            tmpInvertedMatrix.load(modelMatrixStack[modelMatrixStackPointer]).invert().transpose();
            Matrix4f.transform(tmpInvertedMatrix, coord.vector, coord.vector);
        }
        ++stateTexGenSerial;
    }

    public static final void setActiveTexture(int texture) {
        int textureIdx = texture - GL_TEXTURE0;
        if (textureIdx != activeTexture) {
            _wglActiveTexture(texture);
            activeTexture = textureIdx;
        }
    }

    public static final void enableTexture2D() {
        stateTexture[activeTexture] = true;
    }

    public static final void disableTexture2D() {
        stateTexture[activeTexture] = false;
    }

    public static final void texCoords2D(float x, float y) {
        textureCoordsX[activeTexture] = x;
        textureCoordsY[activeTexture] = y;
        ++textureCoordsAccessSerial[activeTexture];
    }

    public static final void texCoords2DDirect(int tex, float x, float y) {
        textureCoordsX[tex] = x;
        textureCoordsY[tex] = y;
        ++textureCoordsAccessSerial[tex];
    }

    public static final float getTexCoordX(int tex) {
        return textureCoordsX[tex];
    }

    public static final float getTexCoordY(int tex) {
        return textureCoordsY[tex];
    }

    public static final int glGenTextures() {
        return mapTexturesGL.register(_wglGenTextures());
    }

    public static final int glGenTextures(IntBuffer ib) {
        int i = mapTexturesGL.register(_wglGenTextures());
        ib.put(i);
        return i;
    }

    public static final void glDeleteTexture(int texture) {
        unbindTextureIfCached(texture);
        _wglDeleteTextures(mapTexturesGL.free(texture));
    }

    public static final void glDeleteTextures(IntBuffer buffer) {
        for (int i = 0; i < buffer.remaining(); ++i) {
            glDeleteTexture(buffer.get(i));
        }
    }

    static final void unbindTextureIfCached(int texture) {
        boolean f1, f2 = false;
        for (int i = 0; i < boundTexture.length; ++i) {
            if (boundTexture[i] == texture) {
                f1 = i != activeTexture;
                if (f2 || f1) {
                    _wglActiveTexture(GL_TEXTURE0 + i);
                    f2 = f1;
                }
                _wglBindTexture(GL_TEXTURE_2D, null);
                if (GL11.checkOpenGLESVersion() >= 300) {
                    _wglBindTexture(GL_TEXTURE_3D, null);
                }
                boundTexture[i] = -1;
            }
        }
        if (f2) {
            _wglActiveTexture(GL_TEXTURE0 + activeTexture);
        }
    }

    public static final void glBindTexture(int texture) {
        if (texture != boundTexture[activeTexture]) {
            _wglBindTexture(GL_TEXTURE_2D, mapTexturesGL.get(texture));
            boundTexture[activeTexture] = texture;
        }
    }

    public static final void glBindTexture(int mode, int texture) {
        if (currentList != null) {
            currentList.ops.add(currentList.new ListOperation(texture));
            return;
        }
        glBindTexture(texture);
    }

    public static final void bindTexture3D(int texture) {
        if (texture != boundTexture[activeTexture]) {
            _wglBindTexture(GL_TEXTURE_3D, mapTexturesGL.get(texture));
            boundTexture[activeTexture] = texture;
        }
    }

    public static final void quickBindTexture(int unit, int texture) {
        int unitBase = unit - GL_TEXTURE0;
        if (texture != boundTexture[unitBase]) {
            if (unitBase != activeTexture) {
                _wglActiveTexture(unit);
            }
            _wglBindTexture(GL_TEXTURE_2D, mapTexturesGL.get(texture));
            boundTexture[unitBase] = texture;
            if (unitBase != activeTexture) {
                _wglActiveTexture(GL_TEXTURE0 + activeTexture);
            }
        }
    }

    public static final void glShadeModel(int mode) {

    }

    public static final void enableRescaleNormal() {
        // still not sure what this is for
    }

    public static final void disableRescaleNormal() {

    }

    public static final void glViewport(int x, int y, int w, int h) {
        if (viewportX != x || viewportY != y || viewportW != w || viewportH != h) {
            _wglViewport(x, y, w, h);
            viewportX = x;
            viewportY = y;
            viewportW = w;
            viewportH = h;
        }
    }

    public static final void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        int bits = (red ? 1 : 0) | (green ? 2 : 0) | (blue ? 4 : 0) | (alpha ? 8 : 0);
        if (bits != colorMaskBits) {
            _wglColorMask(red, green, blue, alpha);
            colorMaskBits = bits;
        }
    }

    public static final void glClearDepth(double depth) {
        depth = 1.0f - depth;
        if (depth != clearDepth) {
            _wglClearDepth((float)depth);
            clearDepth = depth;
        }
    }

    public static final void glClearColor(float red, float green, float blue, float alpha) {
        if (red != clearColorR || green != clearColorG || blue != clearColorB || alpha != clearColorA) {
            _wglClearColor(red, green, blue, alpha);
            clearColorR = red;
            clearColorG = green;
            clearColorB = blue;
            clearColorA = alpha;
        }
    }

    public static final void glClear(int mask) {
        _wglClear(mask);
    }

    public static final void glMatrixMode(int mode) {
        stateMatrixMode = mode;
    }

    public static final void glLoadIdentity() {
        switch (stateMatrixMode) {
            case GL_MODELVIEW:
            default:
                modelMatrixStack[modelMatrixStackPointer].setIdentity();
                modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
                break;
            case GL_PROJECTION:
                projectionMatrixStack[projectionMatrixStackPointer].setIdentity();
                projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
                break;
            case GL_TEXTURE:
                textureMatrixStack[activeTexture][textureMatrixStackPointer[activeTexture]].setIdentity();
                textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] = ++textureMatrixAccessSerial[activeTexture];
                break;
        }
    }

    public static final void glPushMatrix() {
        int push;
        switch (stateMatrixMode) {
            case GL_MODELVIEW:
            default:
                push = modelMatrixStackPointer + 1;
                if (push < modelMatrixStack.length) {
                    modelMatrixStack[push].load(modelMatrixStack[modelMatrixStackPointer]);
                    modelMatrixStackAccessSerial[push] = modelMatrixStackAccessSerial[modelMatrixStackPointer];
                    modelMatrixStackPointer = push;
                } else {
                    Throwable t = new IndexOutOfBoundsException("GL_MODELVIEW matrix stack overflow!" + " Exceeded "
                            + modelMatrixStack.length + " calls to GlStateManager.pushMatrix");
                    logger.error(t);
                }
                break;
            case GL_PROJECTION:
                push = projectionMatrixStackPointer + 1;
                if (push < projectionMatrixStack.length) {
                    projectionMatrixStack[push].load(projectionMatrixStack[projectionMatrixStackPointer]);
                    projectionMatrixStackAccessSerial[push] = projectionMatrixStackAccessSerial[projectionMatrixStackPointer];
                    projectionMatrixStackPointer = push;
                } else {
                    Throwable t = new IndexOutOfBoundsException("GL_PROJECTION matrix stack overflow!" + " Exceeded "
                            + projectionMatrixStack.length + " calls to GlStateManager.pushMatrix");
                    logger.error(t);
                }
                break;
            case GL_TEXTURE:
                push = textureMatrixStackPointer[activeTexture] + 1;
                if (push < textureMatrixStack.length) {
                    int ptr = textureMatrixStackPointer[activeTexture];
                    textureMatrixStack[activeTexture][push].load(textureMatrixStack[activeTexture][ptr]);
                    textureMatrixStackAccessSerial[activeTexture][push] = textureMatrixStackAccessSerial[activeTexture][ptr];
                    textureMatrixStackPointer[activeTexture] = push;
                } else {
                    Throwable t = new IndexOutOfBoundsException(
                            "GL_TEXTURE #" + activeTexture + " matrix stack overflow!" + " Exceeded "
                                    + textureMatrixStack.length + " calls to GlStateManager.pushMatrix");
                    logger.error(t);
                }
                break;
        }
    }

    public static final void glPopMatrix() {
        switch (stateMatrixMode) {
            case GL_MODELVIEW:
            default:
                if (modelMatrixStackPointer > 0) {
                    --modelMatrixStackPointer;
                } else {
                    Throwable t = new IndexOutOfBoundsException("GL_MODELVIEW matrix stack underflow!"
                            + " Called GlStateManager.popMatrix on an empty matrix stack");
                    logger.error(t);
                }
                break;
            case GL_PROJECTION:
                if (projectionMatrixStackPointer > 0) {
                    --projectionMatrixStackPointer;
                } else {
                    Throwable t = new IndexOutOfBoundsException("GL_PROJECTION matrix stack underflow!"
                            + " Called GlStateManager.popMatrix on an empty matrix stack");
                    logger.error(t);
                }
                break;
            case GL_TEXTURE:
                if (textureMatrixStackPointer[activeTexture] > 0) {
                    --textureMatrixStackPointer[activeTexture];
                } else {
                    Throwable t = new IndexOutOfBoundsException("GL_TEXTURE #" + activeTexture
                            + " matrix stack underflow!  Called GlStateManager.popMatrix on an empty matrix stack");
                    logger.error(t);
                }
                break;
        }
    }

    public static final void glGetFloat(int pname, float[] params) {
        switch (pname) {
            case GL_MODELVIEW_MATRIX:
                modelMatrixStack[modelMatrixStackPointer].store(params);
                break;
            case GL_PROJECTION_MATRIX:
                projectionMatrixStack[projectionMatrixStackPointer].store(params);
                break;
            case GL_TEXTURE_MATRIX:
                textureMatrixStack[activeTexture][textureMatrixStackPointer[activeTexture]].store(params);
                break;
            default:
                throw new UnsupportedOperationException("glGetFloat can only be used to retrieve matricies!");
        }
    }

    public static final void glGetFloat(int pname, FloatBuffer params) {
        switch (pname) {
            case GL_MODELVIEW_MATRIX:
                modelMatrixStack[modelMatrixStackPointer].store(params);
                break;
            case GL_PROJECTION_MATRIX:
                projectionMatrixStack[projectionMatrixStackPointer].store(params);
                break;
            case GL_TEXTURE_MATRIX:
                textureMatrixStack[activeTexture][textureMatrixStackPointer[activeTexture]].store(params);
                break;
            default:
                throw new UnsupportedOperationException("glGetFloat can only be used to retrieve matricies!");
        }
    }

    public static final void glOrtho(double left, double right, double bottom, double top, double zNear, double zFar) {
        Matrix4f matrix;
        switch (stateMatrixMode) {
            case GL_MODELVIEW:
                matrix = modelMatrixStack[modelMatrixStackPointer];
                modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
                break;
            case GL_PROJECTION:
            default:
                matrix = projectionMatrixStack[projectionMatrixStackPointer];
                projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
                break;
            case GL_TEXTURE:
                int ptr = textureMatrixStackPointer[activeTexture];
                matrix = textureMatrixStack[activeTexture][ptr];
                textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] = ++textureMatrixAccessSerial[activeTexture];
                break;
        }
        paramMatrix.m00 = 2.0f / (float)(right - left);
        paramMatrix.m01 = 0.0f;
        paramMatrix.m02 = 0.0f;
        paramMatrix.m03 = 0.0f;
        paramMatrix.m10 = 0.0f;
        paramMatrix.m11 = 2.0f / (float)(top - bottom);
        paramMatrix.m12 = 0.0f;
        paramMatrix.m13 = 0.0f;
        paramMatrix.m20 = 0.0f;
        paramMatrix.m21 = 0.0f;
        paramMatrix.m22 = 2.0f / (float)(zFar - zNear);
        paramMatrix.m23 = 0.0f;
        paramMatrix.m30 = (float)(-(right + left) / (right - left));
        paramMatrix.m31 = (float)(-(top + bottom) / (top - bottom));
        paramMatrix.m32 = (float)((zFar + zNear) / (zFar - zNear));
        paramMatrix.m33 = 1.0f;
        Matrix4f.mul(matrix, paramMatrix, matrix);
    }

    private static final Vector3f paramVector = new Vector3f();
    private static final float toRad = 0.0174532925f;

    public static final void glRotatef(float angle, float x, float y, float z) {
        paramVector.x = x;
        paramVector.y = y;
        paramVector.z = z;
        switch (stateMatrixMode) {
            case GL_MODELVIEW:
            default:
                modelMatrixStack[modelMatrixStackPointer].rotate(angle * toRad, paramVector);
                modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
                break;
            case GL_PROJECTION:
                projectionMatrixStack[projectionMatrixStackPointer].rotate(angle * toRad, paramVector);
                projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
                break;
            case GL_TEXTURE:
                int ptr = textureMatrixStackPointer[activeTexture];
                textureMatrixStack[activeTexture][ptr].rotate(angle * toRad, paramVector);
                textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] = ++textureMatrixAccessSerial[activeTexture];
                break;
        }
    }

    public static final void glScalef(float x, float y, float z) {
        paramVector.x = x;
        paramVector.y = y;
        paramVector.z = z;
        switch (stateMatrixMode) {
            case GL_MODELVIEW:
            default:
                modelMatrixStack[modelMatrixStackPointer].scale(paramVector);
                modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
                break;
            case GL_PROJECTION:
                projectionMatrixStack[projectionMatrixStackPointer].scale(paramVector);
                projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
                break;
            case GL_TEXTURE:
                int ptr = textureMatrixStackPointer[activeTexture];
                textureMatrixStack[activeTexture][ptr].scale(paramVector);
                textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] = ++textureMatrixAccessSerial[activeTexture];
                break;
        }
    }

    public static final void glScalef(double x, double y, double z) {
        paramVector.x = (float)x;
        paramVector.y = (float)y;
        paramVector.z = (float)z;
        switch (stateMatrixMode) {
            case GL_MODELVIEW:
            default:
                modelMatrixStack[modelMatrixStackPointer].scale(paramVector);
                modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
                break;
            case GL_PROJECTION:
                projectionMatrixStack[projectionMatrixStackPointer].scale(paramVector);
                projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
                break;
            case GL_TEXTURE:
                int ptr = textureMatrixStackPointer[activeTexture];
                textureMatrixStack[activeTexture][ptr].scale(paramVector);
                textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] = ++textureMatrixAccessSerial[activeTexture];
                break;
        }
    }

    public static final void glTranslatef(float x, float y, float z) {
        paramVector.x = x;
        paramVector.y = y;
        paramVector.z = z;
        switch (stateMatrixMode) {
            case GL_MODELVIEW:
            default:
                modelMatrixStack[modelMatrixStackPointer].translate(paramVector);
                modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
                break;
            case GL_PROJECTION:
                projectionMatrixStack[projectionMatrixStackPointer].translate(paramVector);
                projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
                break;
            case GL_TEXTURE:
                int ptr = textureMatrixStackPointer[activeTexture];
                textureMatrixStack[activeTexture][ptr].translate(paramVector);
                textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] = ++textureMatrixAccessSerial[activeTexture];
                break;
        }
    }

    public static final void glTranslatef(double x, double y, double z) {
        paramVector.x = (float)x;
        paramVector.y = (float)y;
        paramVector.z = (float)z;
        switch (stateMatrixMode) {
            case GL_MODELVIEW:
            default:
                modelMatrixStack[modelMatrixStackPointer].translate(paramVector);
                modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
                break;
            case GL_PROJECTION:
                projectionMatrixStack[projectionMatrixStackPointer].translate(paramVector);
                projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
                break;
            case GL_TEXTURE:
                int ptr = textureMatrixStackPointer[activeTexture];
                textureMatrixStack[activeTexture][ptr].translate(paramVector);
                textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] = ++textureMatrixAccessSerial[activeTexture];
                break;
        }
    }

    private static final Matrix4f paramMatrix = new Matrix4f();

    public static final void glMultMatrix(float[] matrix) {
        Matrix4f modeMatrix;

        switch (stateMatrixMode) {
            case GL_MODELVIEW:
            default:
                modeMatrix = modelMatrixStack[modelMatrixStackPointer];
                modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
                break;
            case GL_PROJECTION:
                modeMatrix = projectionMatrixStack[projectionMatrixStackPointer];
                projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
                break;
            case GL_TEXTURE:
                int ptr = textureMatrixStackPointer[activeTexture];
                modeMatrix = textureMatrixStack[activeTexture][ptr];
                textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] = ++textureMatrixAccessSerial[activeTexture];
                break;
        }

        paramMatrix.load(matrix);

        Matrix4f.mul(modeMatrix, paramMatrix, modeMatrix);
    }

    public static final void glMultMatrix(Matrix4f matrix) {
        Matrix4f modeMatrix;

        switch (stateMatrixMode) {
            case GL_MODELVIEW:
            default:
                modeMatrix = modelMatrixStack[modelMatrixStackPointer];
                modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
                break;
            case GL_PROJECTION:
                modeMatrix = projectionMatrixStack[projectionMatrixStackPointer];
                projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
                break;
            case GL_TEXTURE:
                int ptr = textureMatrixStackPointer[activeTexture];
                modeMatrix = textureMatrixStack[activeTexture][ptr];
                textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] = ++textureMatrixAccessSerial[activeTexture];
                break;
        }

        Matrix4f.mul(modeMatrix, matrix, modeMatrix);
    }

    public static final void glColor4f(float colorRed, float colorGreen, float colorBlue, float colorAlpha) {
        if (currentList != null) {
            currentList.ops.add(currentList.new ListOperation(colorRed, colorGreen, colorBlue, colorAlpha));
            return;
        }
        stateColorR = colorRed;
        stateColorG = colorGreen;
        stateColorB = colorBlue;
        stateColorA = colorAlpha;
        ++stateColorSerial;
    }

    public static final void glColor3f(float colorRed, float colorGreen, float colorBlue) {
        if (currentList != null) {
            currentList.ops.add(currentList.new ListOperation(colorRed, colorGreen, colorBlue, 1.0f));
            return;
        }
        stateColorR = colorRed;
        stateColorG = colorGreen;
        stateColorB = colorBlue;
        stateColorA = 1.0f;
        ++stateColorSerial;
    }

    public static final void resetColor() {
        stateColorR = 1.0f;
        stateColorG = 1.0f;
        stateColorB = 1.0f;
        stateColorA = 1.0f;
        ++stateColorSerial;
    }

    public static final void gluPerspective(float fovy, float aspect, float zNear, float zFar) {
        Matrix4f matrix;
        switch (stateMatrixMode) {
            case GL_MODELVIEW:
                matrix = modelMatrixStack[modelMatrixStackPointer];
                modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
                break;
            case GL_PROJECTION:
            default:
                matrix = projectionMatrixStack[projectionMatrixStackPointer];
                projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
                break;
            case GL_TEXTURE:
                int ptr = textureMatrixStackPointer[activeTexture];
                matrix = textureMatrixStack[activeTexture][ptr];
                textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] = ++textureMatrixAccessSerial[activeTexture];
                break;
        }
        float cotangent = (float)Math.cos(fovy * toRad * 0.5f) / (float)Math.sin(fovy * toRad * 0.5f);
        paramMatrix.m00 = cotangent / aspect;
        paramMatrix.m01 = 0.0f;
        paramMatrix.m02 = 0.0f;
        paramMatrix.m03 = 0.0f;
        paramMatrix.m10 = 0.0f;
        paramMatrix.m11 = cotangent;
        paramMatrix.m12 = 0.0f;
        paramMatrix.m13 = 0.0f;
        paramMatrix.m20 = 0.0f;
        paramMatrix.m21 = 0.0f;
        paramMatrix.m22 = (zFar + zNear) / (zFar - zNear);
        paramMatrix.m23 = -1.0f;
        paramMatrix.m30 = 0.0f;
        paramMatrix.m31 = 0.0f;
        paramMatrix.m32 = 2.0f * zFar * zNear / (zFar - zNear);
        paramMatrix.m33 = 0.0f;
        Matrix4f.mul(matrix, paramMatrix, matrix);
    }

    public static final void gluLookAt(Vector3f eye, Vector3f center, Vector3f up) {
        Matrix4f matrix;
        switch (stateMatrixMode) {
            case GL_MODELVIEW:
                matrix = modelMatrixStack[modelMatrixStackPointer];
                modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
                break;
            case GL_PROJECTION:
            default:
                matrix = projectionMatrixStack[projectionMatrixStackPointer];
                projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
                break;
            case GL_TEXTURE:
                int ptr = textureMatrixStackPointer[activeTexture];
                matrix = textureMatrixStack[activeTexture][ptr];
                textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] = ++textureMatrixAccessSerial[activeTexture];
                break;
        }
        float x = center.x - eye.x;
        float y = center.y - eye.y;
        float z = center.z - eye.z;
        float xyzLen = (float)Math.sqrt(x * x + y * y + z * z);
        x /= xyzLen;
        y /= xyzLen;
        z /= xyzLen;
        float ux = up.x;
        float uy = up.y;
        float uz = up.z;
        xyzLen = (float)Math.sqrt(ux * ux + uy * uy + uz * uz);
        ux /= xyzLen;
        uy /= xyzLen;
        uz /= xyzLen;
        float lxx = y * uz - z * uy;
        float lxy = ux * z - uz * x;
        float lxz = x * uy - y * ux;
        float lyx = lxy * z - lxz * y;
        float lyy = x * lxz - z * lxx;
        float lyz = lxx * y - lxy * x;
        paramMatrix.m00 = lxx;
        paramMatrix.m01 = lyx;
        paramMatrix.m02 = -x;
        paramMatrix.m03 = 0.0f;
        paramMatrix.m10 = lxy;
        paramMatrix.m11 = lyy;
        paramMatrix.m12 = -y;
        paramMatrix.m13 = 0.0f;
        paramMatrix.m20 = lxz;
        paramMatrix.m21 = lyz;
        paramMatrix.m22 = -z;
        paramMatrix.m23 = 0.0f;
        paramMatrix.m30 = -eye.x;
        paramMatrix.m31 = -eye.y;
        paramMatrix.m32 = -eye.z;
        paramMatrix.m33 = 1.0f;
        Matrix4f.mul(matrix, paramMatrix, matrix);
    }

    public static final void transform(Vector4f vecIn, Vector4f vecOut) {
        Matrix4f matrix;
        switch (stateMatrixMode) {
            case GL_MODELVIEW:
                matrix = modelMatrixStack[modelMatrixStackPointer];
                break;
            case GL_PROJECTION:
            default:
                matrix = projectionMatrixStack[projectionMatrixStackPointer];
                break;
            case GL_TEXTURE:
                matrix = textureMatrixStack[activeTexture][textureMatrixStackPointer[activeTexture]];
                break;
        }
        Matrix4f.transform(matrix, vecIn, vecOut);
    }

    private static final Matrix4f unprojA = new Matrix4f();
    private static final Matrix4f unprojB = new Matrix4f();
    private static final Vector4f unprojC = new Vector4f();

    public static final void gluUnProject(float p1, float p2, float p3, float[] modelview, float[] projection,
            int[] viewport, float[] objectcoords) {
        unprojA.load(modelview);
        unprojB.load(projection);
        Matrix4f.mul(unprojA, unprojB, unprojB);
        unprojB.invert();
        unprojC.set(((p1 - (float)viewport[0]) / (float)viewport[2]) * 2f - 1f,
                ((p2 - (float)viewport[1]) / (float)viewport[3]) * 2f - 1f, p3, 1.0f);
        Matrix4f.transform(unprojB, unprojC, unprojC);
        objectcoords[0] = unprojC.x / unprojC.w;
        objectcoords[1] = unprojC.y / unprojC.w;
        objectcoords[2] = unprojC.z / unprojC.w;
    }

    public static final void getMatrix(Matrix4f mat) {
        switch (stateMatrixMode) {
            case GL_MODELVIEW:
                mat.load(modelMatrixStack[modelMatrixStackPointer]);
                break;
            case GL_PROJECTION:
            default:
                mat.load(projectionMatrixStack[projectionMatrixStackPointer]);
                break;
            case GL_TEXTURE:
                mat.load(textureMatrixStack[activeTexture][textureMatrixStackPointer[activeTexture]]);
                break;
        }
    }

    public static final void loadMatrix(Matrix4f mat) {
        switch (stateMatrixMode) {
            case GL_MODELVIEW:
                modelMatrixStack[modelMatrixStackPointer].load(mat);
                modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
                break;
            case GL_PROJECTION:
            default:
                projectionMatrixStack[projectionMatrixStackPointer].load(mat);
                projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
                break;
            case GL_TEXTURE:
                textureMatrixStack[activeTexture][textureMatrixStackPointer[activeTexture]].load(mat);
                textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] = ++textureMatrixAccessSerial[activeTexture];
                break;
        }
    }

    public static final int getModelViewSerial() {
        return modelMatrixStackAccessSerial[modelMatrixStackPointer];
    }

    public static final Matrix4f getModelViewReference() {
        return modelMatrixStack[modelMatrixStackPointer];
    }

    public static void recompileShaders() {
        FixedFunctionPipeline.flushCache();
    }

    public static final void glTexParameteri(int target, int param, int value) {
        _wglTexParameteri(target, param, value);
    }

    public static final void glTexParameterf(int target, int param, float value) {
        _wglTexParameterf(target, param, value);
    }

    public static final void glCopyTexSubImage2D(int target, int level, int sx, int sy, int dx, int dy, int w, int h) {
        _wglCopyTexSubImage2D(target, level, sx, sy, dx, dy, w, h);
    }

    private static DisplayList currentList = null;
    private static ByteBuffer displayListBuffer = EagRuntime.allocateByteBuffer(0x100000);

    public static final void glNewList(int target, int op) {
        if (currentList != null) {
            throw new IllegalStateException("A display list is already being compiled you eagler!");
        }
        if (op != GL_COMPILE) {
            throw new UnsupportedOperationException("Only GL_COMPILE is supported by glNewList");
        }
        DisplayList dp = currentList = displayLists.get(target);
        if (dp == null) {
            throw new IllegalArgumentException("Unknown display list: " + target);
        }
        if (dp.vertexArray != null && dp.attribs > 0) {
            bindGLBufferArray(dp.vertexArray);
            int c = 0;
            if ((dp.attribs & ATTRIB_TEXTURE) == ATTRIB_TEXTURE) {
                GL11.disableVertexAttribArray(++c);
            }
            if ((dp.attribs & ATTRIB_COLOR) == ATTRIB_COLOR) {
                GL11.disableVertexAttribArray(++c);
            }
        }
        dp.ops.clear();
        dp.attribs = -1;
        dp.mode = -1;
        dp.count = 0;
    }

    private static final void growDisplayListBuffer(int len) {
        int wantSize = displayListBuffer.position() + len;
        if (displayListBuffer.capacity() < wantSize) {
            int newSize = (wantSize & 0xFFFE0000) + 0x40000;
            ByteBuffer newBuffer = EagRuntime.allocateByteBuffer(newSize);
            newBuffer.put((ByteBuffer)displayListBuffer.flip());
            EagRuntime.freeByteBuffer(displayListBuffer);
            displayListBuffer = newBuffer;
        }
    }

    public static final void glEndList() {
        DisplayList dp = currentList;
        if (dp == null) {
            throw new IllegalStateException("No list is currently being compiled!");
        }

        if (dp.attribs == -1) {
            if (dp.vertexArray != null) {
                GL11.destroyGLBufferArray(dp.vertexArray);
                dp.vertexArray = null;
            }
            if (dp.vertexBuffer != null) {
                _wglDeleteBuffers(dp.vertexBuffer);
                dp.vertexBuffer = null;
            }
            currentList = null;
            return;
        }

        if (dp.vertexArray == null) {
            dp.vertexArray = createGLBufferArray();
            dp.bindQuad16 = false;
            dp.bindQuad32 = false;
        }
        if (dp.vertexBuffer == null) {
            dp.vertexBuffer = _wglGenBuffers();
        }

        bindVAOGLArrayBufferNow(dp.vertexBuffer);
        displayListBuffer.flip();
        _wglBufferData(GL_ARRAY_BUFFER, displayListBuffer, GL_STATIC_DRAW);
        displayListBuffer.clear();

        FixedFunctionPipeline.setupDisplayList(dp);
        currentList = null;
    }

    public static final void glCallList(int displayList) {
        DisplayList dp = displayLists.get(displayList);
        if (dp == null) {
            throw new NullPointerException("Tried to call a display list that does not exist: " + displayList);
        }
        for (int i = 0; i < dp.ops.size(); ++i) {
            ListOperation op = dp.ops.get(i);
            if (op.hasSetting) {
                if (op.enabled) {
                    glEnable(op.setting);
                } else {
                    glDisable(op.setting);
                }
            }
            if (op.hasTex) {
                glBindTexture(GL_TEXTURE_2D, op.tex);
            }
            if (op.hasColor) {
                glColor4f(op.r, op.g, op.b, op.a);
            }
            if (op.doBlend) {
                glBlendFunc(op.srcFactor, op.dstFactor);
            }
            if (op.hasCount && dp.attribs != -1) {
                FixedFunctionPipeline p = FixedFunctionPipeline.setupRenderDisplayList(dp.attribs).update();
                bindGLBufferArray(dp.vertexArray);
                int cnt = op.count;
                if (dp.mode == GL_QUADS) {
                    if (cnt > 0xFFFF) {
                        if (!dp.bindQuad32) {
                            dp.bindQuad16 = false;
                            dp.bindQuad32 = true;
                            attachQuad32EmulationBuffer(cnt, true);
                        } else {
                            attachQuad32EmulationBuffer(cnt, false);
                        }
                        p.drawElements(GL_TRIANGLES, cnt + (cnt >> 1), GL_UNSIGNED_INT, 0);
                    } else {
                        if (!dp.bindQuad16) {
                            dp.bindQuad16 = true;
                            dp.bindQuad32 = false;
                            attachQuad16EmulationBuffer(cnt, true);
                        } else {
                            attachQuad16EmulationBuffer(cnt, false);
                        }
                        p.drawElements(GL_TRIANGLES, cnt + (cnt >> 1), GL_UNSIGNED_SHORT, 0);
                    }
                } else {
                    p.drawArrays(dp.mode, op.offset, cnt);
                }
            }
        }
    }

    public static final void glCallLists(IntBuffer buffer) {
        for (int i = 0; i < buffer.remaining(); ++i) {
            glCallList(buffer.get(i));
        }
    }

    public static final void flushDisplayList(int displayList) {
        DisplayList dp = displayLists.get(displayList);
        if (dp == null) {
            throw new NullPointerException("Tried to flush a display list that does not exist: " + displayList);
        }
        dp.ops.clear();
        dp.attribs = -1;
        if (dp.vertexArray != null) {
            GL11.destroyGLBufferArray(dp.vertexArray);
            dp.vertexArray = null;
        }
        if (dp.vertexBuffer != null) {
            _wglDeleteBuffers(dp.vertexBuffer);
            dp.vertexBuffer = null;
        }
    }

    public static final void glNormal3f(float x, float y, float z) {
        stateNormalX = x;
        stateNormalY = y;
        stateNormalZ = z;
        ++stateNormalSerial;
    }

    private static final Map<Integer, String> stringCache = new HashMap<>();

    public static final String glGetString(int param) {
        String str = stringCache.get(param);
        if (str == null) {
            str = _wglGetString(param);
            stringCache.put(param, str);
        }
        return str;
    }

    public static final void glGetInteger(int param, int[] values) {
        switch (param) {
            case GL_VIEWPORT:
                values[0] = viewportX;
                values[1] = viewportY;
                values[2] = viewportW;
                values[3] = viewportH;
                break;
            default:
                throw new UnsupportedOperationException("glGetInteger only accepts GL_VIEWPORT as a parameter");
        }
    }

    public static final void glGetInteger(int param, IntBuffer values) {
        switch (param) {
            case GL_VIEWPORT:
                values.put(viewportX);
                values.put(viewportY);
                values.put(viewportW);
                values.put(viewportH);
                break;
            default:
                throw new UnsupportedOperationException("glGetInteger only accepts GL_VIEWPORT as a parameter");
        }
    }

    public static final int glGetInteger(int param) {
        return _wglGetInteger(param);
    }

    public static final void glTexImage2D(int target, int level, int internalFormat, int w, int h, int unused,
            int format, int type, ByteBuffer pixels) {
        if (glesVers >= 300) {
            _wglTexImage2D(target, level, internalFormat, w, h, unused, format, type, pixels);
        } else {
            int tv = TextureFormatHelper.trivializeInternalFormatToGLES20(internalFormat);
            _wglTexImage2D(target, level, tv, w, h, unused, tv, type, pixels);
        }
    }

    public static final void glTexImage2D(int target, int level, int internalFormat, int w, int h, int unused,
            int format, int type, IntBuffer pixels) {
        if (glesVers >= 300) {
            _wglTexImage2D(target, level, internalFormat, w, h, unused, format, type, pixels);
        } else {
            int tv = TextureFormatHelper.trivializeInternalFormatToGLES20(internalFormat);
            _wglTexImage2D(target, level, tv, w, h, unused, tv, type, pixels);
        }
    }

    public static final void glTexSubImage2D(int target, int level, int x, int y, int w, int h, int format, int type,
            ByteBuffer pixels) {
        _wglTexSubImage2D(target, level, x, y, w, h, format, type, pixels);
    }

    public static final void glTexSubImage2D(int target, int level, int x, int y, int w, int h, int format, int type,
            IntBuffer pixels) {
        _wglTexSubImage2D(target, level, x, y, w, h, format, type, pixels);
    }

    public static final void glTexStorage2D(int target, int levels, int internalFormat, int w, int h) {
        if (texStorageCapable
                && (glesVers >= 300 || levels == 1 || (MathHelper.calculateLogBaseTwo(Math.max(w, h)) + 1) == levels)) {
            _wglTexStorage2D(target, levels, internalFormat, w, h);
        } else {
            int tv = TextureFormatHelper.trivializeInternalFormatToGLES20(internalFormat);
            int type = TextureFormatHelper.getTypeFromInternal(internalFormat);
            for (int i = 0; i < levels; ++i) {
                _wglTexImage2D(target, i, tv, Math.max(w >> i, 1), Math.max(h >> i, 1), 0, tv, type, (ByteBuffer)null);
            }
        }
    }

    public static final void glReadPixels(int x, int y, int width, int height, int format, int type,
            ByteBuffer buffer) {
        switch (type) {
            case GL_FLOAT:
                _wglReadPixels(x, y, width, height, format, GL_FLOAT, buffer.asFloatBuffer());
                break;
            case 0x140B: // GL_HALF_FLOAT
                _wglReadPixels_u16(x, y, width, height, format, glesVers == 200 ? 0x8D61 : 0x140B, buffer);
                break;
            case GL_UNSIGNED_BYTE:
            default:
                _wglReadPixels(x, y, width, height, format, type, buffer);
                break;
        }
    }

    public static final void glLineWidth(float f) {
        _wglLineWidth(f);
    }

    public static final void glFog(int param, FloatBuffer valueBuffer) {
        int pos = valueBuffer.position();
        switch (param) {
            case GL_FOG_COLOR:
                stateFogColorR = valueBuffer.get();
                stateFogColorG = valueBuffer.get();
                stateFogColorB = valueBuffer.get();
                stateFogColorA = valueBuffer.get();
                ++stateFogSerial;
                break;
            default:
                throw new UnsupportedOperationException("Only GL_FOG_COLOR is configurable!");
        }
        valueBuffer.position(pos);
    }

    public static final void glFogi(int param, int value) {
        if (param == GL_FOG_MODE) {
            setFog(value);
        }
    }

    public static final void glFogf(int param, float value) {
        if (param == GL_FOG_DENSITY) {
            setFogDensity(value);
        } else if (param == GL_FOG_START) {
            setFogStart(value);
        } else if (param == GL_FOG_END) {
            setFogEnd(value);
        } else {
            throw new UnsupportedOperationException("Unsupported glFogf param");
        }
    }

    private static int displayListId = 0;

    public static final int glGenLists(int size) {
        int base = displayListId + 1;
        for (int i = 0; i < size; i++) {
            int id = ++displayListId;
            displayLists.put(id, new DisplayList(id));
        }
        return base;
    }

    public static final void glDeleteLists(int id) {
        DisplayList d = displayLists.remove(id);
        if (d != null) {
            if (d.vertexArray != null) {
                GL11.destroyGLBufferArray(d.vertexArray);
            }
            if (d.vertexBuffer != null) {
                _wglDeleteBuffers(d.vertexBuffer);
            }
        }
    }

    public static final void glDeleteLists(int id, int range) {
        for (int i = id; i < id + range; ++i) {
            glDeleteLists(i);
        }
    }

    public static final int glGetError() {
        return _wglGetError();
    }

    public static final void glBlendEquation(int equation) {
        if (equation != stateBlendEquation) {
            _wglBlendEquation(equation);
            stateBlendEquation = equation;
        }
    }

    public static final boolean areVAOsEmulated() {
        return emulatedVAOs;
    }

    public static final IBufferArrayGL createGLBufferArray() {
        if (emulatedVAOs) {
            return new SoftGLBufferArray();
        } else {
            return _wglGenVertexArrays();
        }
    }

    public static final void destroyGLBufferArray(IBufferArrayGL buffer) {
        if (!emulatedVAOs) {
            _wglDeleteVertexArrays(buffer);
        }
    }

    public static final void enableVertexAttribArray(int index) {
        if (emulatedVAOs) {
            if (currentBufferArray == null) {
                logger.warn("Skipping enable attrib with emulated VAO because no known VAO is bound!");
                return;
            }
            ((SoftGLBufferArray)currentBufferArray).enableAttrib(index, true);
        } else {
            _wglEnableVertexAttribArray(index);
        }
    }

    public static final void disableVertexAttribArray(int index) {
        if (emulatedVAOs) {
            if (currentBufferArray == null) {
                logger.warn("Skipping disable attrib with emulated VAO because no known VAO is bound!");
                return;
            }
            ((SoftGLBufferArray)currentBufferArray).enableAttrib(index, false);
        } else {
            _wglDisableVertexAttribArray(index);
        }
    }

    public static final void vertexAttribPointer(int index, int size, int format, boolean normalized, int stride,
            int offset) {
        if (emulatedVAOs) {
            if (currentBufferArray == null) {
                logger.warn("Skipping vertexAttribPointer with emulated VAO because no known VAO is bound!");
                return;
            }
            if (currentVAOArrayBuffer == null) {
                logger.warn("Skipping vertexAttribPointer with emulated VAO because no VAO array buffer is bound!");
                return;
            }
            ((SoftGLBufferArray)currentBufferArray).setAttrib(currentVAOArrayBuffer, index, size, format, normalized,
                    stride, offset);
        } else {
            _wglVertexAttribPointer(index, size, format, normalized, stride, offset);
        }
    }

    public static final void vertexAttribDivisor(int index, int divisor) {
        if (emulatedVAOs) {
            if (currentBufferArray == null) {
                logger.warn("Skipping vertexAttribPointer with emulated VAO because no known VAO is bound!");
                return;
            }
            ((SoftGLBufferArray)currentBufferArray).setAttribDivisor(index, divisor);
        } else {
            _wglVertexAttribDivisor(index, divisor);
        }
    }

    public static final void doDrawArrays(int mode, int first, int count) {
        if (emulatedVAOs) {
            if (currentBufferArray == null) {
                logger.warn("Skipping draw call with emulated VAO because no known VAO is bound!");
                return;
            }
            ((SoftGLBufferArray)currentBufferArray).transitionToState(emulatedVAOState, false);
        }
        _wglDrawArrays(mode, first, count);
    }

    public static final void doDrawElements(int mode, int count, int type, int offset) {
        if (emulatedVAOs) {
            if (currentBufferArray == null) {
                logger.warn("Skipping draw call with emulated VAO because no known VAO is bound!");
                return;
            }
            ((SoftGLBufferArray)currentBufferArray).transitionToState(emulatedVAOState, true);
        }
        _wglDrawElements(mode, count, type, offset);
    }

    public static final void doDrawArraysInstanced(int mode, int first, int count, int instances) {
        if (emulatedVAOs) {
            if (currentBufferArray == null) {
                logger.warn("Skipping instanced draw call with emulated VAO because no known VAO is bound!");
                return;
            }
            ((SoftGLBufferArray)currentBufferArray).transitionToState(emulatedVAOState, false);
        }
        _wglDrawArraysInstanced(mode, first, count, instances);
    }

    public static final void doDrawElementsInstanced(int mode, int count, int type, int offset, int instances) {
        if (emulatedVAOs) {
            if (currentBufferArray == null) {
                logger.warn("Skipping instanced draw call with emulated VAO because no known VAO is bound!");
                return;
            }
            ((SoftGLBufferArray)currentBufferArray).transitionToState(emulatedVAOState, true);
        }
        _wglDrawElementsInstanced(mode, count, type, offset, instances);
    }

    static IBufferArrayGL currentBufferArray = null;

    public static final void bindGLBufferArray(IBufferArrayGL buffer) {
        if (emulatedVAOs) {
            currentBufferArray = buffer;
        } else {
            if (currentBufferArray != buffer) {
                _wglBindVertexArray(buffer);
                currentBufferArray = buffer;
            }
        }
    }

    static IBufferGL currentArrayBuffer = null;

    // only used when VAOs are emulated
    static IBufferGL currentVAOArrayBuffer = null;

    public static final void bindVAOGLArrayBuffer(IBufferGL buffer) {
        if (emulatedVAOs) {
            currentVAOArrayBuffer = buffer;
        } else {
            if (currentArrayBuffer != buffer) {
                _wglBindBuffer(GL_ARRAY_BUFFER, buffer);
                currentArrayBuffer = buffer;
            }
        }
    }

    public static final void bindVAOGLArrayBufferNow(IBufferGL buffer) {
        if (emulatedVAOs) {
            currentVAOArrayBuffer = buffer;
        }
        if (currentArrayBuffer != buffer) {
            _wglBindBuffer(GL_ARRAY_BUFFER, buffer);
            currentArrayBuffer = buffer;
        }
    }

    public static final void bindVAOGLElementArrayBuffer(IBufferGL buffer) {
        if (emulatedVAOs) {
            if (currentBufferArray == null) {
                logger.warn("Skipping set element array buffer with emulated VAO because no known VAO is bound!");
                return;
            }
            ((SoftGLBufferArray)currentBufferArray).setIndexBuffer(buffer);
        } else {
            _wglBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer);
        }
    }

    static final void bindVAOGLElementArrayBufferNow(IBufferGL buffer) {
        if (emulatedVAOs) {
            if (currentBufferArray == null) {
                logger.warn("Skipping set element array buffer with emulated VAO because no known VAO is bound!");
                return;
            }
            ((SoftGLBufferArray)currentBufferArray).setIndexBuffer(buffer);
            if (currentEmulatedVAOIndexBuffer != buffer) {
                _wglBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer);
                currentEmulatedVAOIndexBuffer = buffer;
            }
        } else {
            _wglBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer);
        }
    }

    static IBufferGL currentEmulatedVAOIndexBuffer = null;

    static final void bindEmulatedVAOIndexBuffer(IBufferGL buffer) {
        if (currentEmulatedVAOIndexBuffer != buffer) {
            _wglBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer);
            currentEmulatedVAOIndexBuffer = buffer;
        }
    }

    public static final void bindGLArrayBuffer(IBufferGL buffer) {
        if (currentArrayBuffer != buffer) {
            _wglBindBuffer(GL_ARRAY_BUFFER, buffer);
            currentArrayBuffer = buffer;
        }
    }

    static IBufferGL currentUniformBuffer = null;

    public static final void bindGLUniformBuffer(IBufferGL buffer) {
        if (currentUniformBuffer != buffer) {
            _wglBindBuffer(0x8A11, buffer);
            currentUniformBuffer = buffer;
        }
    }

    static IProgramGL currentShaderProgram = null;

    public static final void bindGLShaderProgram(IProgramGL prog) {
        if (currentShaderProgram != prog) {
            _wglUseProgram(prog);
            currentShaderProgram = prog;
        }
    }

    private static final IBufferGL[] currentUniformBlockBindings = new IBufferGL[16];
    private static final int[] currentUniformBlockBindingOffset = new int[16];
    private static final int[] currentUniformBlockBindingSize = new int[16];

    public static final void bindUniformBufferRange(int index, IBufferGL buffer, int offset, int size) {
        if (currentUniformBlockBindings[index] != buffer || currentUniformBlockBindingOffset[index] != offset
                || currentUniformBlockBindingSize[index] != size) {
            _wglBindBufferRange(0x8A11, index, buffer, offset, size);
            currentUniformBlockBindings[index] = buffer;
            currentUniformBlockBindingOffset[index] = offset;
            currentUniformBlockBindingSize[index] = size;
        }
    }

    public static final int CLEAR_BINDING_TEXTURE = 1;
    public static final int CLEAR_BINDING_TEXTURE0 = 2;
    public static final int CLEAR_BINDING_ACTIVE_TEXTURE = 4;
    public static final int CLEAR_BINDING_BUFFER_ARRAY = 8;
    public static final int CLEAR_BINDING_ARRAY_BUFFER = 16;
    public static final int CLEAR_BINDING_SHADER_PROGRAM = 32;

    public static final void clearCurrentBinding(int mask) {
        if ((mask & CLEAR_BINDING_TEXTURE) != 0) {
            int[] i = GL11.boundTexture;
            for (int j = 0; j < i.length; ++j) {
                i[j] = -1;
            }
        }
        if ((mask & CLEAR_BINDING_TEXTURE0) != 0) {
            GL11.boundTexture[0] = -1;
        }
        if ((mask & CLEAR_BINDING_ACTIVE_TEXTURE) != 0) {
            GL11.activeTexture = 0;
            _wglActiveTexture(GL_TEXTURE0);
        }
        if ((mask & CLEAR_BINDING_BUFFER_ARRAY) != 0) {
            currentBufferArray = null;
        }
        if ((mask & CLEAR_BINDING_ARRAY_BUFFER) != 0) {
            currentArrayBuffer = currentVAOArrayBuffer = null;
        }
        if ((mask & CLEAR_BINDING_SHADER_PROGRAM) != 0) {
            currentShaderProgram = null;
        }
    }

    public static final int ATTRIB_TEXTURE = 1;
    public static final int ATTRIB_COLOR = 2;
    public static final int ATTRIB_NORMAL = 4;
    public static final int ATTRIB_LIGHTMAP = 8;

    public static final void renderBuffer(ByteBuffer buffer, int attrib, int mode, int count) {
        if (currentList != null) {
            if (currentList.attribs == -1) {
                currentList.attribs = attrib;
            } else if (currentList.attribs != attrib) {
                throw new UnsupportedOperationException(
                        "Inconsistent vertex format in display list (only one is allowed)");
            }
            if (currentList.mode == -1) {
                currentList.mode = mode;
            } else if (currentList.mode != mode) {
                throw new UnsupportedOperationException("Inconsistent draw mode in display list (only one is allowed)");
            }
            currentList.ops.add(currentList.new ListOperation(currentList.count, currentList.count + count));
            currentList.count += count;
            if (buffer.remaining() > displayListBuffer.remaining()) {
                growDisplayListBuffer(buffer.remaining());
            }
            displayListBuffer.put(buffer);
            lastRender = null;
        } else {
            lastRender = FixedFunctionPipeline.setupDirect(buffer, attrib).update();
            lastRender.drawDirectArrays(mode, 0, count);
            lastMode = mode;
            lastCount = count;
        }
    }

    public static final void optimize() {
        FixedFunctionPipeline.optimize();
    }

    private static FixedFunctionPipeline lastRender = null;
    private static int lastMode = 0;
    private static int lastCount = 0;

    public static final void renderAgain() {
        if (lastRender == null) {
            throw new UnsupportedOperationException(
                    "Cannot render the same verticies twice while generating display list");
        }
        bindGLBufferArray(lastRender.getDirectModeBufferArray());
        lastRender.update().drawDirectArrays(lastMode, 0, lastCount);
    }

    private static IBufferGL quad16EmulationBuffer = null;
    private static int quad16EmulationBufferSize = 0;

    private static IBufferGL quad32EmulationBuffer = null;
    private static int quad32EmulationBufferSize = 0;

    public static final void attachQuad16EmulationBuffer(int vertexCount, boolean bind) {
        IBufferGL buf = quad16EmulationBuffer;
        if (buf == null) {
            quad16EmulationBuffer = buf = _wglGenBuffers();
            int newSize = quad16EmulationBufferSize = (vertexCount & 0xFFFFF000) + 0x2000;
            if (newSize > 0xFFFF) {
                newSize = 0xFFFF;
            }
            GL11.bindVAOGLElementArrayBufferNow(buf);
            resizeQuad16EmulationBuffer(newSize >> 2);
        } else {
            int cnt = quad16EmulationBufferSize;
            if (cnt < vertexCount) {
                int newSize = quad16EmulationBufferSize = (vertexCount & 0xFFFFF000) + 0x2000;
                if (newSize > 0xFFFF) {
                    newSize = 0xFFFF;
                }
                GL11.bindVAOGLElementArrayBufferNow(buf);
                resizeQuad16EmulationBuffer(newSize >> 2);
            } else if (bind) {
                GL11.bindVAOGLElementArrayBuffer(buf);
            }
        }
    }

    public static final void attachQuad32EmulationBuffer(int vertexCount, boolean bind) {
        IBufferGL buf = quad32EmulationBuffer;
        if (buf == null) {
            quad32EmulationBuffer = buf = _wglGenBuffers();
            int newSize = quad32EmulationBufferSize = (vertexCount & 0xFFFFC000) + 0x8000;
            GL11.bindVAOGLElementArrayBufferNow(buf);
            resizeQuad32EmulationBuffer(newSize >> 2);
        } else {
            int cnt = quad32EmulationBufferSize;
            if (cnt < vertexCount) {
                int newSize = quad32EmulationBufferSize = (vertexCount & 0xFFFFC000) + 0x8000;
                GL11.bindVAOGLElementArrayBufferNow(buf);
                resizeQuad32EmulationBuffer(newSize >> 2);
            } else if (bind) {
                GL11.bindVAOGLElementArrayBuffer(buf);
            }
        }
    }

    private static final void resizeQuad16EmulationBuffer(int quadCount) {
        IntBuffer buf = EagRuntime.allocateIntBuffer(quadCount * 3);
        int v1, v2, v3, v4;
        for (int i = 0; i < quadCount; ++i) {
            v1 = i << 2;
            v2 = v1 + 1;
            v3 = v2 + 1;
            v4 = v3 + 1;
            buf.put(v1 | (v2 << 16));
            buf.put(v4 | (v2 << 16));
            buf.put(v3 | (v4 << 16));
        }
        buf.flip();
        _wglBufferData(GL_ELEMENT_ARRAY_BUFFER, buf, GL_STATIC_DRAW);
        EagRuntime.freeIntBuffer(buf);
    }

    private static final void resizeQuad32EmulationBuffer(int quadCount) {
        IntBuffer buf = EagRuntime.allocateIntBuffer(quadCount * 6);
        int v1, v2, v3, v4;
        for (int i = 0; i < quadCount; ++i) {
            v1 = i << 2;
            v2 = v1 + 1;
            v3 = v2 + 1;
            v4 = v3 + 1;
            buf.put(v1);
            buf.put(v2);
            buf.put(v4);
            buf.put(v2);
            buf.put(v3);
            buf.put(v4);
        }
        buf.flip();
        _wglBufferData(GL_ELEMENT_ARRAY_BUFFER, buf, GL_STATIC_DRAW);
        EagRuntime.freeIntBuffer(buf);
    }

    public static final ITextureGL getNativeTexture(int tex) {
        return mapTexturesGL.get(tex);
    }

    public static final void regenerateTexture(int tex) {
        ITextureGL webglTex = mapTexturesGL.get(tex);
        if (webglTex != null) {
            unbindTextureIfCached(tex);
            _wglDeleteTextures(webglTex);
            mapTexturesGL.set(tex, _wglGenTextures());
        } else {
            logger.error("Tried to regenerate a missing texture!");
        }
    }

    static int glesVers = -1;
    static boolean hasFramebufferHDR16FSupport = false;
    static boolean hasFramebufferHDR32FSupport = false;
    static boolean hasLinearHDR16FSupport = false;
    static boolean hasLinearHDR32FSupport = false;
    static boolean fboRenderMipmapCapable = false;
    static boolean vertexArrayCapable = false;
    static boolean instancingCapable = false;
    static boolean texStorageCapable = false;
    static boolean textureLODCapable = false;
    static boolean shader5Capable = false;
    static boolean npotCapable = false;
    static int uniformBufferOffsetAlignment = -1;

    public static final void createFramebufferHDR16FTexture(int target, int level, int w, int h, int format,
            boolean allow32bitFallback) {
        createFramebufferHDR16FTexture(target, level, w, h, format, allow32bitFallback, null);
    }

    public static final void createFramebufferHDR16FTexture(int target, int level, int w, int h, int format,
            ByteBuffer pixelData) {
        createFramebufferHDR16FTexture(target, level, w, h, format, false, pixelData);
    }

    private static final void createFramebufferHDR16FTexture(int target, int level, int w, int h, int format,
            boolean allow32bitFallback, ByteBuffer pixelData) {
        if (hasFramebufferHDR16FSupport) {
            int internalFormat;
            switch (format) {
                case GL_RED:
                    if (glesVers == 200) {
                        format = GL_LUMINANCE;
                        internalFormat = GL_LUMINANCE;
                    } else {
                        internalFormat = glesVers == 200 ? GL_LUMINANCE : 0x822D; // GL_R16F
                    }
                    break;
                case 0x8227: // GL_RG
                    internalFormat = glesVers == 200 ? 0x8227 : 0x822F; // GL_RG16F
                case GL_RGB:
                    throw new UnsupportedOperationException(
                            "GL_RGB16F isn't supported specifically in WebGL 2.0 for some goddamn reason");
                case GL_RGBA:
                    internalFormat = glesVers == 200 ? GL_RGBA : 0x881A; // GL_RGBA16F
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown format: " + format);
            }
            _wglTexImage2Du16(target, level, internalFormat, w, h, 0, format, glesVers == 200 ? 0x8D61 : 0x140B,
                    pixelData);
        } else {
            if (allow32bitFallback) {
                if (hasFramebufferHDR32FSupport) {
                    createFramebufferHDR32FTexture(target, level, w, h, format, false, null);
                } else {
                    throw new UnsupportedOperationException(
                            "No fallback 32-bit HDR (floating point) texture support is available on this device");
                }
            } else {
                throw new UnsupportedOperationException(
                        "16-bit HDR (floating point) textures are not supported on this device");
            }
        }
    }

    public static final void createFramebufferHDR32FTexture(int target, int level, int w, int h, int format,
            boolean allow16bitFallback) {
        createFramebufferHDR32FTexture(target, level, w, h, format, allow16bitFallback, null);
    }

    public static final void createFramebufferHDR32FTexture(int target, int level, int w, int h, int format,
            ByteBuffer pixelData) {
        createFramebufferHDR32FTexture(target, level, w, h, format, false, pixelData);
    }

    private static final void createFramebufferHDR32FTexture(int target, int level, int w, int h, int format,
            boolean allow16bitFallback, ByteBuffer pixelData) {
        if (hasFramebufferHDR32FSupport) {
            int internalFormat;
            switch (format) {
                case GL_RED:
                    internalFormat = 0x822E; // GL_R32F
                    break;
                case 0x8227: // GL_RG
                    internalFormat = 0x8230; // GL_RG32F
                case GL_RGB:
                    throw new UnsupportedOperationException(
                            "GL_RGB32F isn't supported specifically in WebGL 2.0 for some goddamn reason");
                case GL_RGBA:
                    internalFormat = 0x8814; // GL_RGBA32F
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown format: " + format);
            }
            _wglTexImage2Df32(target, level, internalFormat, w, h, 0, format, GL_FLOAT, pixelData);
        } else {
            if (allow16bitFallback) {
                if (hasFramebufferHDR16FSupport) {
                    createFramebufferHDR16FTexture(target, level, w, h, format, false);
                } else {
                    throw new UnsupportedOperationException(
                            "No fallback 16-bit HDR (floating point) texture support is available on this device");
                }
            } else {
                throw new UnsupportedOperationException(
                        "32-bit HDR (floating point) textures are not supported on this device");
            }
        }
    }

    public static final void warmUpCache() {
        glGetString(7936);
        glGetString(7937);
        glGetString(7938);
        glesVers = PlatformOpenGL.checkOpenGLESVersion();
        vertexArrayCapable = PlatformOpenGL.checkVAOCapable();
        emulatedVAOs = !vertexArrayCapable;
        fboRenderMipmapCapable = PlatformOpenGL.checkFBORenderMipmapCapable();
        instancingCapable = PlatformOpenGL.checkInstancingCapable();
        texStorageCapable = PlatformOpenGL.checkTexStorageCapable();
        textureLODCapable = PlatformOpenGL.checkTextureLODCapable();
        shader5Capable = PlatformOpenGL.checkOESGPUShader5Capable() || PlatformOpenGL.checkEXTGPUShader5Capable();
        npotCapable = PlatformOpenGL.checkNPOTCapable();
        uniformBufferOffsetAlignment = glesVers >= 300 ? _wglGetInteger(0x8A34) : -1;
        if (!npotCapable) {
            logger.warn(
                    "NPOT texture support detected as false, texture wrapping must be set to GL_CLAMP_TO_EDGE if the texture's width or height is not a power of 2");
        }
        hasFramebufferHDR16FSupport = PlatformOpenGL.checkHDRFramebufferSupport(16);
        if (hasFramebufferHDR16FSupport) {
            logger.info("16-bit HDR render target support: true");
        } else {
            logger.error("16-bit HDR render target support: false");
        }
        hasLinearHDR16FSupport = PlatformOpenGL.checkLinearHDRFilteringSupport(16);
        if (hasLinearHDR16FSupport) {
            logger.info("16-bit HDR linear filter support: true");
        } else {
            logger.error("16-bit HDR linear filter support: false");
        }
        hasFramebufferHDR32FSupport = PlatformOpenGL.checkHDRFramebufferSupport(32);
        if (hasFramebufferHDR32FSupport) {
            logger.info("32-bit HDR render target support: true");
        } else {
            logger.error("32-bit HDR render target support: false");
        }
        hasLinearHDR32FSupport = PlatformOpenGL.checkLinearHDRFilteringSupport(32);
        if (hasLinearHDR32FSupport) {
            logger.info("32-bit HDR linear filter support: true");
        } else {
            logger.error("32-bit HDR linear filter support: false");
        }
        if (!checkHasHDRFramebufferSupportWithFilter()) {
            logger.error("No HDR render target support was detected! Shaders will be disabled.");
        }
        if (emulatedVAOs) {
            logger.info("Note: Could not unlock VAOs via OpenGL extensions, emulating them instead");
        }
        if (!instancingCapable) {
            logger.info(
                    "Note: Could not unlock instancing via OpenGL extensions, using slow vanilla font and particle rendering");
        }
        emulatedVAOState = emulatedVAOs ? new SoftGLBufferState() : null;
        PlatformOpenGL.enterVAOEmulationHook();
        GLSLHeader.init();
        DrawUtils.init();
        DrawUtils.vshLocal.free();
        DrawUtils.vshLocal = null;
    }

    public static final void destroyCache() {
        GLSLHeader.destroy();
        DrawUtils.destroy();
        FixedFunctionPipeline.flushCache();
        StreamBuffer.destroyPool();
        emulatedVAOs = false;
        emulatedVAOState = null;
        glesVers = -1;
        fboRenderMipmapCapable = false;
        vertexArrayCapable = false;
        instancingCapable = false;
        hasFramebufferHDR16FSupport = false;
        hasFramebufferHDR32FSupport = false;
        hasLinearHDR32FSupport = false;
        stringCache.clear();
        mapTexturesGL.clear();
        displayLists.clear();
    }

    public static final int checkOpenGLESVersion() {
        return glesVers;
    }

    public static final boolean checkFBORenderMipmapCapable() {
        return fboRenderMipmapCapable;
    }

    public static final boolean checkVAOCapable() {
        return vertexArrayCapable;
    }

    public static final boolean checkInstancingCapable() {
        return instancingCapable;
    }

    public static final boolean checkTexStorageCapable() {
        return texStorageCapable;
    }

    public static final boolean checkTextureLODCapable() {
        return textureLODCapable;
    }

    public static final boolean checkShader5Capable() {
        return shader5Capable;
    }

    public static final boolean checkNPOTCapable() {
        return npotCapable;
    }

    public static final int getUniformBufferOffsetAlignment() {
        return uniformBufferOffsetAlignment;
    }

    public static final boolean checkHDRFramebufferSupport(int bits) {
        switch (bits) {
            case 16:
                return hasFramebufferHDR16FSupport;
            case 32:
                return hasFramebufferHDR32FSupport;
            default:
                return false;
        }
    }

    public static final boolean checkLinearHDRFilteringSupport(int bits) {
        switch (bits) {
            case 16:
                return hasLinearHDR16FSupport;
            case 32:
                return hasLinearHDR32FSupport;
            default:
                return false;
        }
    }

    public static final boolean checkHasHDRFramebufferSupport() {
        return hasFramebufferHDR16FSupport || hasFramebufferHDR32FSupport;
    }

    public static final boolean checkHasHDRFramebufferSupportWithFilter() {
        return (hasFramebufferHDR16FSupport && hasLinearHDR16FSupport)
                || (hasFramebufferHDR32FSupport && hasLinearHDR32FSupport);
    }

    // legacy
    public static final boolean checkLinearHDR32FSupport() {
        return hasLinearHDR32FSupport;
    }

    public static final void glBegin(int mode, VertexFormat fmt) {
        Tesselator.instance.begin(mode, fmt);
    }

    public static final void glEnd() {
        Tesselator.instance.end();
    }

    public static final void glTexCoord2f(float u, float v) {
        Tesselator.instance.tex(u, v);
    }

    public static final void glVertex3f(float x, float y, float z) {
        Tesselator.instance.vertex(x, y, z);
    }

    public static final void glVertex2f(float x, float y) {
        Tesselator.instance.vertex(x, y, 0.0F);
    }
}
