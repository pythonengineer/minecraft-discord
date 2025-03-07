package com.mojang.minecraft.gui;

import com.mojang.minecraft.ChatLine;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.net.ConnectionManager;
import com.mojang.minecraft.net.NetworkPlayer;
import com.mojang.minecraft.player.Inventory;
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

public final class InGameHud extends Gui {
    public List messages = new ArrayList();
    private Minecraft minecraft;
    public String hoveredUsername = null;

    public InGameHud(Minecraft minecraft1) {
        this.minecraft = minecraft1;
    }

    public final void render(boolean z1, int i2, int i3) {
        int scaledWidth = Minecraft.scaledResolution.getScaledWidth();
        int scaledHeight = Minecraft.scaledResolution.getScaledHeight();
        Font font1 = this.minecraft.font;
        this.minecraft.renderHelper.initGui();

        onBeginHotbarDraw();

        Textures textures2 = this.minecraft.textures;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.textures.getTextureId("/gui.png"));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        Tesselator tesselator6 = Tesselator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        Inventory inventory4 = this.minecraft.player.inventory;
        this.zLevel = -90.0F;
        int i = scaledWidth / 2;
        this.blit(i - 91, scaledHeight - 22, 0, 0, 182, 22);

        if (PointerInputAbstraction.isTouchMode()) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, TouchOverlayRenderer.spriteSheet);
            blit(i + 89, scaledHeight - 22, 234, 0, 22, 22);
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

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.textures.getTextureId("/gui.png"));
        this.blit(i - 91 - 1 + inventory4.selectedSlot * 20, scaledHeight - 22 - 1, 0, 22, 24, 22);
        GL11.glDisable(GL11.GL_BLEND);

        int i5;
        int i7;
        for(i5 = 0; i5 < inventory4.slots.length; ++i5) {
            int i6;
            if((i6 = inventory4.slots[i5]) > 0) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(scaledWidth / 2 - 90 + i5 * 20), (float)(scaledHeight - 16), -50.0F);
                GL11.glScalef(10.0F, 10.0F, 10.0F);
                GL11.glTranslatef(1.0F, 0.5F, 0.0F);
                GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
                GL11.glScalef(-1.0F, -1.0F, -1.0F);
                i7 = textures2.getTextureId("/terrain.png");
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, i7);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                tesselator6.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
                Tile.tiles[i6].render(tesselator6, this.minecraft.level, 0, -2, 0, 0);
                tesselator6.end();
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glPopMatrix();
            }
        }

        onEndHotbarDraw();

        font1.drawShadow("0.0.23a_01", 2, 2, 0xFFFFFF);
        if(this.minecraft.options.showFPS) {
            font1.drawShadow(this.minecraft.fpsString, 2, 12, 0xFFFFFF);
        }

        byte b17 = 10;
        boolean z18 = false;
        if(this.minecraft.screen instanceof ChatScreen) {
            b17 = 20;
            z18 = true;
        }

        for(i7 = 0; i7 < this.messages.size() && i7 < b17; ++i7) {
            if(((ChatLine)this.messages.get(i7)).counter < 200 || z18) {
                font1.drawShadow(((ChatLine)this.messages.get(i7)).message, 2, scaledHeight - 8 - i7 * 9 - 20, 0xFFFFFF);
            }
        }

        int i10 = scaledWidth / 2;
        int i11 = scaledHeight / 2;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tesselator6.begin(DefaultVertexFormats.POSITION);
        tesselator6.vertex((float)(i10 + 1), (float)(i11 - 4), 0.0F);
        tesselator6.vertex((float)i10, (float)(i11 - 4), 0.0F);
        tesselator6.vertex((float)i10, (float)(i11 + 5), 0.0F);
        tesselator6.vertex((float)(i10 + 1), (float)(i11 + 5), 0.0F);
        tesselator6.vertex((float)(i10 + 5), (float)i11, 0.0F);
        tesselator6.vertex((float)(i10 - 4), (float)i11, 0.0F);
        tesselator6.vertex((float)(i10 - 4), (float)(i11 + 1), 0.0F);
        tesselator6.vertex((float)(i10 + 5), (float)(i11 + 1), 0.0F);
        tesselator6.end();
        this.hoveredUsername = null;
        if(Keyboard.isKeyDown(15) && this.minecraft.connectionManager != null && this.minecraft.connectionManager.isConnected()) {
            ConnectionManager connectionManager12 = this.minecraft.connectionManager;
            ArrayList arrayList15;
            (arrayList15 = new ArrayList()).add(connectionManager12.minecraft.user.name);
            Iterator iterator13 = connectionManager12.players.values().iterator();

            while(iterator13.hasNext()) {
                NetworkPlayer networkPlayer20 = (NetworkPlayer)iterator13.next();
                arrayList15.add(networkPlayer20.name);
            }

            ArrayList arrayList14 = arrayList15;
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glBegin(7, DefaultVertexFormats.POSITION);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.7F);
            GL11.glVertex2f((float)(i10 + 128), (float)(i11 - 68 - 12));
            GL11.glVertex2f((float)(i10 - 128), (float)(i11 - 68 - 12));
            GL11.glColor4f(0.2F, 0.2F, 0.2F, 0.8F);
            GL11.glVertex2f((float)(i10 - 128), (float)(i11 + 68));
            GL11.glVertex2f((float)(i10 + 128), (float)(i11 + 68));
            GL11.glEnd();
            GL11.glDisable(3042);
            String string16 = "Connected players:";
            font1.drawShadow(string16, i10 - font1.width(string16) / 2, i11 - 64 - 12, 16777215);

            for(int i17 = 0; i17 < arrayList14.size(); ++i17) {
                int i8 = i10 + i17 % 2 * 120 - 120;
                int i9 = i11 - 64 + (i17 / 2 << 3);
                if(z1 && i2 >= i8 && i3 >= i9 && i2 < i8 + 120 && i3 < i9 + 8) {
                    this.hoveredUsername = (String)arrayList14.get(i17);
                    font1.draw((String)arrayList14.get(i17), i8 + 2, i9, 16777215);
                } else {
                    font1.draw((String)arrayList14.get(i17), i8, i9, 15658734);
                }
            }
        }

    }

    public final void addChatMessage(String string1) {
        this.messages.add(0, new ChatLine(string1));

        while(this.messages.size() > 50) {
            this.messages.remove(this.messages.size() - 1);
        }

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
                    hotbarSlotTouchAlreadySelected = (this.minecraft.player.inventory.selectedSlot == currentHotbarSlotTouch);
                    this.minecraft.player.inventory.selectedSlot = currentHotbarSlotTouch;
                } else if (currentHotbarSlotTouch == 9) {
                    hotbarSlotTouchAlreadySelected = false;
                    currentHotbarSlotTouch = 69;
                    this.minecraft.setScreen(new InventoryScreen());
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
                                    this.minecraft.player.inventory.selectedSlot = slot;
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
