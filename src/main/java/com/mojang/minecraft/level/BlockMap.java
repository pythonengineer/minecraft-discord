package com.mojang.minecraft.level;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.Textures;

import java.util.ArrayList;
import java.util.List;

public final class BlockMap {
    public int width;
    public int depth;
    public int height;
    Slot slot = new Slot(this);
    Slot slot2 = new Slot(this);
    public List[] entityGrid;
    public List all = new ArrayList();
    List tmp = new ArrayList();

    public BlockMap(int w, int d, int h) {
        this.width = w / 16;
        this.depth = d / 16;
        this.height = h / 16;
        if(this.width == 0) {
            this.width = 1;
        }

        if(this.depth == 0) {
            this.depth = 1;
        }

        if(this.height == 0) {
            this.height = 1;
        }

        this.entityGrid = new ArrayList[this.width * this.depth * this.height];

        for(w = 0; w < this.width; ++w) {
            for(d = 0; d < this.depth; ++d) {
                for(h = 0; h < this.height; ++h) {
                    this.entityGrid[(h * this.depth + d) * this.width + w] = new ArrayList();
                }
            }
        }

    }

    public final List getEntities(Entity entity, float x0, float y0, float z0, float x1, float y2, float z2, List entities) {
        Slot slot9 = this.slot.init(x0, y0, z0);
        Slot slot10 = this.slot2.init(x1, y2, z2);

        for(int i11 = slot9.xSlot - 1; i11 <= slot10.xSlot + 1; ++i11) {
            for(int i12 = slot9.ySlot - 1; i12 <= slot10.ySlot + 1; ++i12) {
                for(int i13 = slot9.zSlot - 1; i13 <= slot10.zSlot + 1; ++i13) {
                    if(i11 >= 0 && i12 >= 0 && i13 >= 0 && i11 < this.width && i12 < this.depth && i13 < this.height) {
                        List list14 = this.entityGrid[(i13 * this.depth + i12) * this.width + i11];

                        for(int i15 = 0; i15 < list14.size(); ++i15) {
                            Entity entity16;
                            if((entity16 = (Entity)list14.get(i15)) != entity && entity16.intersects(x0, y0, z0, x1, y2, z2)) {
                                entities.add(entity16);
                            }
                        }
                    }
                }
            }
        }

        return entities;
    }

    public final List getEntities(Entity entity, AABB box) {
        this.tmp.clear();
        return this.getEntities(entity, box.x0, box.y0, box.z0, box.x1, box.y1, box.z1, this.tmp);
    }

    public final void render(Frustum frustrum, Textures textures, float a) {
        for(int i4 = 0; i4 < this.width; ++i4) {
            float f5 = (float)((i4 << 4) - 2);
            float f6 = (float)((i4 + 1 << 4) + 2);

            for(int i7 = 0; i7 < this.depth; ++i7) {
                float f8 = (float)((i7 << 4) - 2);
                float f9 = (float)((i7 + 1 << 4) + 2);

                for(int i10 = 0; i10 < this.height; ++i10) {
                    List list11;
                    if((list11 = this.entityGrid[(i10 * this.depth + i7) * this.width + i4]).size() != 0) {
                        float f12 = (float)((i10 << 4) - 2);
                        float f13 = (float)((i10 + 1 << 4) + 2);
                        boolean z14;
                        boolean z15 = (z14 = frustrum.cubeInFrustum(f5, f8, f12, f6, f9, f13)) && frustrum.cubeFullyInFrustrum(f5, f8, f12, f6, f9, f13);
                        if(z14) {
                            for(int i16 = 0; i16 < list11.size(); ++i16) {
                                Entity entity17 = (Entity)list11.get(i16);
                                if(z15 || frustrum.isVisible(entity17.bb)) {
                                    ((Entity)list11.get(i16)).render(textures, a);
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}