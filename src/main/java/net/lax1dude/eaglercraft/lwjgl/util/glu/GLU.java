package net.lax1dude.eaglercraft.lwjgl.util.glu;

import static net.lax1dude.eaglercraft.lwjgl.opengl.GL11.GL_CONTEXT_LOST_WEBGL;
import static net.lax1dude.eaglercraft.lwjgl.opengl.GL11.GL_INVALID_ENUM;
import static net.lax1dude.eaglercraft.lwjgl.opengl.GL11.GL_INVALID_OPERATION;
import static net.lax1dude.eaglercraft.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static net.lax1dude.eaglercraft.lwjgl.opengl.GL11.GL_OUT_OF_MEMORY;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public class GLU {

    public static void gluPerspective(float fovy, float aspect, float zNear, float zFar) {
        GL11.gluPerspective(fovy, aspect, zNear, zFar);
    }

    public static String gluErrorString(int i) {
        switch (i) {
            case GL_INVALID_ENUM:
                return "GL_INVALID_ENUM";
            case GL_INVALID_VALUE:
                return "GL_INVALID_VALUE";
            case 1286:
                return "GL_INVALID_FRAMEBUFFER_OPERATION";
            case GL_INVALID_OPERATION:
                return "GL_INVALID_OPERATION";
            case GL_OUT_OF_MEMORY:
                return "GL_OUT_OF_MEMORY";
            case GL_CONTEXT_LOST_WEBGL:
                return "CONTEXT_LOST_WEBGL";
            default:
                return "Unknown Error";
        }
    }
}
