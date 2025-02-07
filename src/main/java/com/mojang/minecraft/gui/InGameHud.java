package com.mojang.minecraft.gui;

import com.mojang.minecraft.ChatLine;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.User;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.net.ConnectionManager;
import com.mojang.minecraft.net.NetworkPlayer;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.ScaledResolution;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.touch.TouchControls;
import net.lax1dude.eaglercraft.touch.TouchOverlayRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class InGameHud {
    public List messages = new ArrayList();
    private Minecraft minecraft;

    public InGameHud(Minecraft minecraft1) {
        this.minecraft = minecraft1;
    }

    public final void render() {
        int scaledWidth = Minecraft.scaledResolution.getScaledWidth();
        int scaledHeight = Minecraft.scaledResolution.getScaledHeight();
        Font font1 = this.minecraft.font;
        this.minecraft.initGui();

        onBeginHotbarDraw();

        Textures textures2 = this.minecraft.textures;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.textures.getTextureId("/gui.png"));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        Tesselator tesselator3 = Tesselator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        blit(scaledWidth / 2 - 91, scaledHeight - 22, 0, 0, 182, 22);

        if (PointerInputAbstraction.isTouchMode()) {
            int i = scaledWidth / 2;
            //GL11.glBindTexture(GL11.GL_TEXTURE_2D, TouchOverlayRenderer.spriteSheet);
            //blit(i + 89, scaledHeight - 22, 234, 0, 22, 22);
            int areaHAdd = 12;
            hotbarAreaX = (i - 91) * this.minecraft.width / scaledWidth;
            hotbarAreaY = (scaledHeight - 22 - areaHAdd) * this.minecraft.height / scaledHeight;
            hotbarAreaW = 203 * this.minecraft.width / scaledWidth;
            hotbarAreaH = (22 + areaHAdd) * this.minecraft.height / scaledHeight;
        } else {
            hotbarAreaX = -1;
            hotbarAreaY = -1;
            hotbarAreaW = -1;
            hotbarAreaH = -1;
        }

        int i10000 = scaledWidth / 2 - 91 - 1;
        Minecraft minecraft4 = this.minecraft;
        int i5 = 0;

        int i10001;
        while(true) {
            if(i5 >= User.creativeTiles.length) {
                i10001 = 0;
                break;
            }

            if(User.creativeTiles[i5] == minecraft4.paintTexture) {
                i10001 = i5;
                break;
            }

            ++i5;
        }

        blit(i10000 + i10001 * 20, scaledHeight - 22 - 1, 0, 22, 24, 22);
        GL11.glDisable(GL11.GL_BLEND);

        int i6;
        int i13;
        for(i13 = 0; i13 < 9; ++i13) {
            i5 = User.creativeTiles[i13];
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(scaledWidth / 2 - 90 + i13 * 20), (float)(scaledHeight - 16), -50.0F);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 0.0F);
            GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
            GL11.glScalef(-1.0F, -1.0F, -1.0F);
            i6 = textures2.getTextureId("/terrain.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, i6);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            tesselator3.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
            Tile.tiles[i5].render(tesselator3, this.minecraft.level, 0, -2, 0, 0);
            tesselator3.end();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }

        onEndHotbarDraw();

        font1.drawShadow("0.0.19a_06", 2, 2, 0xFFFFFF);
        font1.drawShadow(this.minecraft.fpsString, 2, 12, 0xFFFFFF);
        byte b14 = 10;
        boolean z15 = false;
        if(this.minecraft.screen instanceof ChatScreen) {
            b14 = 20;
            z15 = true;
        }

        for(i6 = 0; i6 < this.messages.size() && i6 < b14; ++i6) {
            if(((ChatLine)this.messages.get(i6)).counter < 200 || z15) {
                font1.drawShadow(((ChatLine)this.messages.get(i6)).message, 2, scaledHeight - 8 - (i6 << 3) - 16, 0xFFFFFF);
            }
        }

        i6 = scaledWidth / 2;
        int i9 = scaledHeight / 2;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tesselator3.begin(DefaultVertexFormats.POSITION);
        tesselator3.vertex((float)(i6 + 1), (float)(i9 - 4), 0.0F);
        tesselator3.vertex((float)i6, (float)(i9 - 4), 0.0F);
        tesselator3.vertex((float)i6, (float)(i9 + 5), 0.0F);
        tesselator3.vertex((float)(i6 + 1), (float)(i9 + 5), 0.0F);
        tesselator3.vertex((float)(i6 + 5), (float)i9, 0.0F);
        tesselator3.vertex((float)(i6 - 4), (float)i9, 0.0F);
        tesselator3.vertex((float)(i6 - 4), (float)(i9 + 1), 0.0F);
        tesselator3.vertex((float)(i6 + 5), (float)(i9 + 1), 0.0F);
        tesselator3.end();
        if(Keyboard.isKeyDown(Keyboard.KEY_TAB) && this.minecraft.connectionManager != null && this.minecraft.connectionManager.isConnected()) {
            ConnectionManager connectionManager16 = this.minecraft.connectionManager;
            ArrayList arrayList17;
            (arrayList17 = new ArrayList()).add(connectionManager16.minecraft.user.name);
            Iterator iterator7 = connectionManager16.players.values().iterator();

            while(iterator7.hasNext()) {
                NetworkPlayer networkPlayer10 = (NetworkPlayer)iterator7.next();
                arrayList17.add(networkPlayer10.name);
            }

            ArrayList arrayList8 = arrayList17;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glBegin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.7F);
            GL11.glVertex2f((float)(i6 + 128), (float)(i9 - 68 - 12));
            GL11.glVertex2f((float)(i6 - 128), (float)(i9 - 68 - 12));
            GL11.glColor4f(0.2F, 0.2F, 0.2F, 0.8F);
            GL11.glVertex2f((float)(i6 - 128), (float)(i9 + 68));
            GL11.glVertex2f((float)(i6 + 128), (float)(i9 + 68));
            GL11.glEnd();
            GL11.glDisable(GL11.GL_BLEND);
            String string11 = "Connected players:";
            font1.drawShadow(string11, i6 - font1.width(string11) / 2, i9 - 64 - 12, 0xFFFFFF);

            for(int i12 = 0; i12 < arrayList8.size(); ++i12) {
                i13 = i6 + i12 % 2 * 120 - 120;
                i5 = i9 - 64 + (i12 / 2 << 3);
                font1.draw((String)arrayList8.get(i12), i13, i5, 0xFFFFFF);
            }
        }

    }

    private static void blit(int i0, int i1, int i2, int i3, int i4, int i5) {
        float f7 = 0.00390625F;
        float f8 = 0.015625F;
        Tesselator tesselator6 = Tesselator.instance;
        Tesselator.instance.begin(DefaultVertexFormats.POSITION_TEX);
        tesselator6.vertexUV((float)i0, (float)(i1 + 22), -90.0F, 0.0F, (float)(i3 + 22) * f8);
        tesselator6.vertexUV((float)(i0 + i4), (float)(i1 + 22), -90.0F, (float)(i4 + 0) * f7, (float)(i3 + 22) * f8);
        tesselator6.vertexUV((float)(i0 + i4), (float)i1, -90.0F, (float)(i4 + 0) * f7, (float)i3 * f8);
        tesselator6.vertexUV((float)i0, (float)i1, -90.0F, 0.0F, (float)i3 * f8);
        tesselator6.end();
    }

    private int hotbarAreaX = -1;
    private int hotbarAreaY = -1;
    private int hotbarAreaW = -1;
    private int hotbarAreaH = -1;
    private int currentHotbarSlotTouch = -1;
    private long hotbarSlotTouchStart = -1l;
    private boolean hotbarSlotTouchAlreadySelected = false;
    private int touchVPosX = -1;
    private int touchVPosY = -1;
    private int touchEventUID = -1;

    private int applyTouchHotbarTransformX(int posX, boolean scaled) {
        if (scaled) {
            return (posX + Minecraft.scaledResolution.getScaledWidth() / 4) * 2 / 3;
        } else {
            return (posX + this.minecraft.width / 4) * 2 / 3;
        }
    }

    private int applyTouchHotbarTransformY(int posY, boolean scaled) {
        if (scaled) {
            return (posY + Minecraft.scaledResolution.getScaledHeight() / 2) * 2 / 3;
        } else {
            return (posY + this.minecraft.height / 2) * 2 / 3;
        }
    }

    private void onBeginHotbarDraw() {
        if (PointerInputAbstraction.isTouchMode()) {
            GL11.glPushMatrix();
            ScaledResolution res = Minecraft.scaledResolution;
            GL11.glTranslatef(res.getScaledWidth() / -4, res.getScaledHeight() / -2, 10);
            GL11.glScalef(1.5f, 1.5f, 1.5f);
        }
    }

    private void onEndHotbarDraw() {
        if (PointerInputAbstraction.isTouchMode()) {
            GL11.glPopMatrix();
        }
    }

    private int getHotbarSlotTouched(int pointX) {
        int xx = pointX - hotbarAreaX - 2;
        xx /= 20 * Minecraft.scaledResolution.getScaleFactor();
        if (xx < 0)
            xx = 0;
        if (xx > 9)
            xx = 9;
        return xx;
    }

    public boolean handleTouchBeginEagler(int uid, int pointX, int pointY) {
        if (this.minecraft.player == null) {
            return false;
        }
        if (touchEventUID == -1) {
            pointX = applyTouchHotbarTransformX(pointX, false);
            pointY = applyTouchHotbarTransformY(pointY, false);
            if (pointX >= hotbarAreaX && pointY >= hotbarAreaY && pointX < hotbarAreaX + hotbarAreaW
                    && pointY < hotbarAreaY + hotbarAreaH) {
                touchEventUID = uid;
                currentHotbarSlotTouch = getHotbarSlotTouched(pointX);
                hotbarSlotTouchStart = EagRuntime.currentTimeMillis();
                if (currentHotbarSlotTouch >= 0 && currentHotbarSlotTouch < 9) {
                    for (int i = 0; i < User.creativeTiles.length; i++) {
                        if (User.creativeTiles[i] == this.minecraft.paintTexture
                            && i == currentHotbarSlotTouch) { 
                            hotbarSlotTouchAlreadySelected = true;
                        }
                    }
                    this.minecraft.paintTexture = User.creativeTiles[currentHotbarSlotTouch];
                } else if (currentHotbarSlotTouch == 9) {
                    //hotbarSlotTouchAlreadySelected = false;
                    //currentHotbarSlotTouch = 69;
                    //mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
                }
                return true;
            }
        }
        return false;
    }

    public boolean handleTouchEndEagler(int uid, int pointX, int pointY) {
        if (uid == touchEventUID) {
            if (hotbarSlotTouchStart != -1l && currentHotbarSlotTouch != 69) {
                if (EagRuntime.currentTimeMillis() - hotbarSlotTouchStart < 350l) {
                    if (hotbarSlotTouchAlreadySelected) {
                        //if (mc.thePlayer != null) {
                        //    mc.thePlayer.dropOneItem(false);
                        //}
                    }
                }
            }
            touchVPosX = -1;
            touchVPosY = -1;
            touchEventUID = -1;
            currentHotbarSlotTouch = -1;
            hotbarSlotTouchStart = -1l;
            hotbarSlotTouchAlreadySelected = false;
            return true;
        }
        return false;
    }

    public void updateTouchEagler(boolean screenTouched) {
        if (screenTouched) {
            int pointCount = Touch.touchPointCount();
            for (int i = 0; i < pointCount; ++i) {
                int uid = Touch.touchPointUID(i);
                if (TouchControls.touchControls.containsKey(uid)) {
                    continue;
                }
                if (touchEventUID == -1 || touchEventUID == uid) {
                    touchVPosX = applyTouchHotbarTransformX(Touch.touchPointX(i), false);
                    touchVPosY = applyTouchHotbarTransformY(this.minecraft.height - Touch.touchPointY(i) - 1, false);
                    long millis = EagRuntime.currentTimeMillis();
                    if (touchEventUID != -1 && hotbarSlotTouchStart != -1l) {
                        if (currentHotbarSlotTouch != 69) {
                            int slot = getHotbarSlotTouched(touchVPosX);
                            if (slot != currentHotbarSlotTouch) {
                                hotbarSlotTouchAlreadySelected = false;
                                currentHotbarSlotTouch = slot;
                                hotbarSlotTouchStart = millis;
                                if (slot >= 0 && slot < 9) {
                                    this.minecraft.paintTexture = User.creativeTiles[slot];
                                }
                            } else {
                                if (millis - hotbarSlotTouchStart > 1200l) {
                                    //hotbarSlotTouchStart = millis;
                                    //this.mc.thePlayer.dropOneItem(true);
                                }
                            }
                        }
                    }
                    return;
                }
            }
        }
        if (touchEventUID != -1) {
            handleTouchEndEagler(touchEventUID, touchVPosX, touchVPosY);
        }
        touchVPosX = -1;
        touchVPosY = -1;
        touchEventUID = -1;
        currentHotbarSlotTouch = -1;
        hotbarSlotTouchStart = -1l;
        hotbarSlotTouchAlreadySelected = false;
    }

    public boolean isTouchOverlapEagler(int uid, int tx, int ty) {
        if (touchEventUID == uid) {
            return true;
        }
        ty = this.minecraft.height - ty - 1;
        tx = applyTouchHotbarTransformX(tx, false);
        ty = applyTouchHotbarTransformY(ty, false);
        return (tx >= hotbarAreaX && ty >= hotbarAreaY && tx < hotbarAreaX + hotbarAreaW
                && ty < hotbarAreaY + hotbarAreaH);
    }
}
