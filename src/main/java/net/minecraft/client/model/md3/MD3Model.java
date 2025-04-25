package net.minecraft.client.model.md3;

import java.util.HashMap;

import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.render.Tessellator;

public final class MD3Model {
    private MD3Vertices vertices;
    private boolean unknownMD3Bool = false;
    private int displayList = 0;

    public MD3Model(MD3Vertices var1) {
        new HashMap();
        BufferUtils.createFloatBuffer(16);
        this.vertices = var1;
    }

    public final void renderModelVertices(int var1, int var2, float var3) {
        if(this.displayList == 0) {
            MD3Model var8 = this;
            this.displayList = GL11.glGenLists(this.vertices.totalFrames);

            for(var2 = 0; var2 < var8.vertices.totalFrames; ++var2) {
                GL11.glNewList(var8.displayList + var2, GL11.GL_COMPILE);
                Tessellator tessellator = Tessellator.instance;

                for(int var9 = 0; var9 < var8.vertices.buffersMD3.length; ++var9) {
                    MD3Buffers var10000 = var8.vertices.buffersMD3[var9];
                    float var7 = 0.0F;
                    MD3Buffers var4 = var10000;
                    var4.setAndClearBuffers(var2, var2, var7);
                    var4.vertices.position(0);
                    var4.triangles.position(0);
                    var4.normals.position(0);
                    var4.xBuffer.position(0);
                    tessellator.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_NORMAL);
                    for (int i = 0; i < var4.verts; ++i) {
                        tessellator.normal(var4.normals.get(), var4.normals.get(), var4.normals.get());
                        tessellator.addVertexWithUV(var4.vertices.get(), var4.vertices.get(), var4.vertices.get(), var4.xBuffer.get(), var4.xBuffer.get());
                    }
                    GL11.glDrawElements(GL11.GL_TRIANGLES, var4.triangles);
                    tessellator.draw();
                }

                GL11.glEndList();
            }
        }

        GL11.glCallList(this.displayList);
    }
}
