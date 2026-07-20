package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.touch.TouchControls;
import net.lax1dude.eaglercraft.touch.TouchOverlayRenderer;
import net.minecraft.client.ChatLine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.gui.container.GuiInventory;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.material.Material;

public class GuiIngame extends Gui {
    private static RenderItem itemRenderer = new RenderItem();
    private List chatMessageList = new ArrayList();
	private EaglercraftRandom rand = new EaglercraftRandom();
	private Minecraft mc;
	public String testMessage = null;
    private int updateCounter = 0;
    private String recordPlaying = "";
    private int recordPlayingUpFor = 0;
	public float damageGuiPartialTime;
    float prevVignetteBrightness = 1.0F;

	public GuiIngame(Minecraft mc) {
		this.mc = mc;
	}

	public void renderGameOverlay(float renderPartialTick) {
        int scaledWidth = this.mc.scaledResolution.getScaledWidth();
        int scaledHeight = this.mc.scaledResolution.getScaledHeight();

		this.mc.entityRenderer.setupOverlayRendering();

        onBeginHotbarDraw();

        GL11.glEnable(GL11.GL_BLEND);
        if(this.mc.options.fancyGraphics) {
            this.renderVignette(this.mc.thePlayer.getBrightness(renderPartialTick), scaledWidth, scaledHeight);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
        InventoryPlayer inventoryPlayer = this.mc.thePlayer.inventory;
		this.zLevel = -90.0F;
        int i = scaledWidth / 2;
		this.drawTexturedModalRect(i - 91, scaledHeight - 22, 0, 0, 182, 22);

        if (PointerInputAbstraction.isTouchMode()) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, TouchOverlayRenderer.spriteSheet);
            this.drawTexturedModalRect(i + 89, scaledHeight - 22, 234, 0, 22, 22);
            int areaHAdd = 12;
            hotbarAreaX = (i - 91) * this.mc.displayWidth / scaledWidth;
            hotbarAreaY = (scaledHeight - 22 - areaHAdd) * this.mc.displayHeight / scaledHeight;
            hotbarAreaW = 203 * this.mc.displayWidth / scaledWidth;
            hotbarAreaH = (22 + areaHAdd) * this.mc.displayHeight / scaledHeight;
        } else {
            hotbarAreaX = -1;
            hotbarAreaY = -1;
            hotbarAreaW = -1;
            hotbarAreaH = -1;
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		this.drawTexturedModalRect(i - 91 - 1 + inventoryPlayer.currentItem * 20, scaledHeight - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
        boolean z20 = this.mc.thePlayer.heartsLife / 3 % 2 == 1;
        if(this.mc.thePlayer.heartsLife < 10) {
            z20 = false;
		}

        int i6 = this.mc.thePlayer.health;
        int i7 = this.mc.thePlayer.prevHealth;
        this.rand.setSeed((long)(this.updateCounter * 312871));
        int i10;
        int i12;
        if(this.mc.playerController.shouldDrawHUD()) {
            i10 = this.mc.thePlayer.getPlayerArmorValue();

            int i11;
            int i13;
            for(i11 = 0; i11 < 10; ++i11) {
                i12 = scaledHeight - 32;
                if(i10 > 0) {
                    i13 = scaledWidth / 2 + 91 - (i11 << 3) - 9;
                    if((i11 << 1) + 1 < i10) {
                        this.drawTexturedModalRect(i13, i12, 34, 9, 9, 9);
                    }

                    if((i11 << 1) + 1 == i10) {
                        this.drawTexturedModalRect(i13, i12, 25, 9, 9, 9);
                    }

                    if((i11 << 1) + 1 > i10) {
                        this.drawTexturedModalRect(i13, i12, 16, 9, 9, 9);
                    }
                }

                byte b26 = 0;
                if(z20) {
                    b26 = 1;
                }

                int i14 = scaledWidth / 2 - 91 + (i11 << 3);
                if(i6 <= 4) {
                    i12 += this.rand.nextInt(2);
                }

                this.drawTexturedModalRect(i14, i12, 16 + b26 * 9, 0, 9, 9);
                if(z20) {
                    if((i11 << 1) + 1 < i7) {
                        this.drawTexturedModalRect(i14, i12, 70, 0, 9, 9);
                    }

                    if((i11 << 1) + 1 == i7) {
                        this.drawTexturedModalRect(i14, i12, 79, 0, 9, 9);
                    }
                }

                if((i11 << 1) + 1 < i6) {
                    this.drawTexturedModalRect(i14, i12, 52, 0, 9, 9);
                }

                if((i11 << 1) + 1 == i6) {
                    this.drawTexturedModalRect(i14, i12, 61, 0, 9, 9);
                }
            }

            if(this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
                i11 = (int)Math.ceil((double)(this.mc.thePlayer.air - 2) * 10.0D / 300.0D);
                i12 = (int)Math.ceil((double)this.mc.thePlayer.air * 10.0D / 300.0D) - i11;

                for(i13 = 0; i13 < i11 + i12; ++i13) {
                    if(i13 < i11) {
                        this.drawTexturedModalRect(scaledWidth / 2 - 91 + (i13 << 3), scaledHeight - 32 - 9, 16, 18, 9, 9);
                    } else {
                        this.drawTexturedModalRect(scaledWidth / 2 - 91 + (i13 << 3), scaledHeight - 32 - 9, 25, 18, 9, 9);
                    }
                }
            }
        }

		GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_RESCALE_NORMAL);
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();

        for(i10 = 0; i10 < 9; ++i10) {
            int i26 = scaledWidth / 2 - 90 + i10 * 20 + 2;
            int i20 = scaledHeight - 16 - 3;
            this.renderInventorySlot(i10, i26, i20, renderPartialTick);
        }

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_RESCALE_NORMAL);

        if(this.recordPlayingUpFor > 0) {
            float f23 = (float)this.recordPlayingUpFor - renderPartialTick;
            int i14 = (int)(f23 * 256.0F / 20.0F);
            if(i14 > 255) {
                i14 = 255;
            }

            if(i14 > 0) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(scaledWidth / 2), (float)(scaledHeight - 48), 0.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                int i15 = HSBtoRGB(f23 / 50.0F, 0.7F, 0.6F) & 0xFFFFFF;
                this.mc.fontRenderer.drawString(this.recordPlaying, -this.mc.fontRenderer.getStringWidth(this.recordPlaying) / 2, -4, i15 + (i14 << 24));
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }

        onEndHotbarDraw();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
        this.drawTexturedModalRect(i - 7, scaledHeight / 2 - 7, 0, 0, 16, 16);
        GL11.glDisable(GL11.GL_BLEND);

        onBeginTouchGUI();

        if(this.mc.options.showFPS) {
            this.mc.fontRenderer.drawStringWithShadow("Minecraft Alpha v1.0.17_04 (" + this.mc.debug + ")", 2, 2, 0xFFFFFF);
            this.mc.fontRenderer.drawStringWithShadow(this.mc.debugInfoRenders(), 2, 12, 0xFFFFFF);
            this.mc.fontRenderer.drawStringWithShadow(this.mc.getEntityDebug(), 2, 22, 0xFFFFFF);
            this.mc.fontRenderer.drawStringWithShadow(this.mc.debugInfoEntities(), 2, 32, 0xFFFFFF);
        } else {
            this.mc.fontRenderer.drawStringWithShadow("Minecraft Alpha v1.0.17_04", 2, 2, 0xFFFFFF);
        }

        onEndTouchGUI();

        byte b23 = 10;
        boolean z24 = false;
        if(this.mc.currentScreen instanceof GuiChat) {
            b23 = 20;
            z24 = true;
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, (float)(scaledHeight - 48), 0.0F);

        for(int i15 = 0; i15 < this.chatMessageList.size() && i15 < b23; ++i15) {
            if(((ChatLine)this.chatMessageList.get(i15)).updateCounter < 200 || z24) {
                double d28 = (double)((ChatLine)this.chatMessageList.get(i15)).updateCounter / 200.0D;
                d28 = 1.0D - d28;
                d28 *= 5.0D;
                if(d28 < 0.0D) {
                    d28 = 0.0D;
                }

                if(d28 > 1.0D) {
                    d28 = 1.0D;
                }

                d28 *= d28;
                int i18 = (int)(255.0D * d28);
                if(z24) {
                    i18 = 255;
                }

                if(i18 > 0) {
                    byte b30 = 2;
                    int i20 = -i15 * 9;
                    String string21 = ((ChatLine)this.chatMessageList.get(i15)).message;
                    this.drawRect(b30, i20 - 1, b30 + 320, i20 + 8, i18 / 2 << 24);
                    GL11.glEnable(GL11.GL_BLEND);
                    this.mc.fontRenderer.drawStringWithShadow(string21, b30, i20, 0xFFFFFF + (i18 << 24));
                }
            }
        }

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
	}

    private void renderVignette(float partialTicks, int scaledWidth, int scaledHeight) {
        float f5 = this.mc.thePlayer.getBrightness(partialTicks);
        if((f5 = 1.0F - f5) < 0.0F) {
            f5 = 0.0F;
        }

        if(f5 > 1.0F) {
            f5 = 1.0F;
        }

        this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(f5 - this.prevVignetteBrightness) * 0.01D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
        GL11.glColor4f(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/misc/vignette.png"));
        Tessellator tessellator9 = Tessellator.instance;
        Tessellator.instance.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
        tessellator9.addVertexWithUV(0.0D, (double)scaledHeight, -90.0D, 0.0D, 1.0D);
        tessellator9.addVertexWithUV((double)scaledWidth, (double)scaledHeight, -90.0D, 1.0D, 1.0D);
        tessellator9.addVertexWithUV((double)scaledWidth, 0.0D, -90.0D, 1.0D, 0.0D);
        tessellator9.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        tessellator9.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void renderInventorySlot(int i1, int i2, int i3, float partialTicks) {
        ItemStack itemStack5 = this.mc.thePlayer.inventory.mainInventory[i1];
        if(itemStack5 != null) {
            float f6 = (float)itemStack5.animationsToGo - partialTicks;
            if(f6 > 0.0F) {
                GL11.glPushMatrix();
                float f7 = 1.0F + f6 / 5.0F;
                GL11.glTranslatef((float)(i2 + 8), (float)(i3 + 12), 0.0F);
                GL11.glScalef(1.0F / f7, (f7 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float)(-(i2 + 8)), (float)(-(i3 + 12)), 0.0F);
            }

            itemRenderer.renderItemIntoGUI(this.mc.renderEngine, itemStack5, i2, i3);
            if(f6 > 0.0F) {
                GL11.glPopMatrix();
            }

            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, itemStack5, i2, i3);
        }
    }

    public void updateTick() {
        if(this.recordPlayingUpFor > 0) {
            --this.recordPlayingUpFor;
        }

        ++this.updateCounter;

        for(int i1 = 0; i1 < this.chatMessageList.size(); ++i1) {
            ++((ChatLine)this.chatMessageList.get(i1)).updateCounter;
        }

    }

    public void addChatMessage(String message) {
        while(this.mc.fontRenderer.getStringWidth(message) > 320) {
            int i2;
            for(i2 = 1; i2 < message.length() && this.mc.fontRenderer.getStringWidth(message.substring(0, i2 + 1)) <= 320; ++i2) {
            }

            this.addChatMessage(message.substring(0, i2));
            message = message.substring(i2);
        }

        this.chatMessageList.add(0, new ChatLine(message));

        while(this.chatMessageList.size() > 50) {
            this.chatMessageList.remove(this.chatMessageList.size() - 1);
        }

    }

    public void setRecordPlayingMessage(String record) {
        this.recordPlaying = "Now playing: " + record;
        this.recordPlayingUpFor = 60;
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
            return (posX + this.mc.scaledResolution.getScaledWidth() / 4) * 2 / 3;
        } else {
            return (posX + this.mc.displayWidth / 4) * 2 / 3;
        }
    }

    private int applyTouchHotbarTransformY(int posY, boolean scaled) {
        if (scaled) {
            return (posY + this.mc.scaledResolution.getScaledHeight() / 2) * 2 / 3;
        } else {
            return (posY + this.mc.displayHeight / 2) * 2 / 3;
        }
    }

    private void onBeginTouchGUI() {
        if (PointerInputAbstraction.isTouchMode()) {
            GL11.glPushMatrix();
            GL11.glScalef(1.5f, 1.5f, 1.5f);
        }
    }

    private void onEndTouchGUI() {
        if (PointerInputAbstraction.isTouchMode()) {
            GL11.glPopMatrix();
        }
    }

    private void onBeginHotbarDraw() {
        if (PointerInputAbstraction.isTouchMode()) {
            GL11.glPushMatrix();
            ScaledResolution res = this.mc.scaledResolution;
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
        xx /= 20 * this.mc.scaledResolution.getScaleFactor();
        if (xx < 0)
            xx = 0;
        if (xx > 9)
            xx = 9;
        return xx;
    }

    public boolean handleTouchBeginEagler(int uid, int pointX, int pointY) {
        if (this.mc.thePlayer == null) {
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
                    hotbarSlotTouchAlreadySelected = (this.mc.thePlayer.inventory.currentItem == currentHotbarSlotTouch);
                    this.mc.thePlayer.inventory.currentItem = currentHotbarSlotTouch;
                } else if (currentHotbarSlotTouch == 9) {
                    hotbarSlotTouchAlreadySelected = false;
                    currentHotbarSlotTouch = 69;
                    this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer.inventory, this.mc.thePlayer.inventory.craftingInventory));
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
                        if (this.mc.thePlayer != null) {
                            this.mc.thePlayer.dropOneItem(false);
                        }
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
                    touchVPosY = applyTouchHotbarTransformY(this.mc.displayHeight - Touch.touchPointY(i) - 1, false);
                    long millis = EagRuntime.currentTimeMillis();
                    if (touchEventUID != -1 && hotbarSlotTouchStart != -1l) {
                        if (currentHotbarSlotTouch != 69) {
                            int slot = getHotbarSlotTouched(touchVPosX);
                            if (slot != currentHotbarSlotTouch) {
                                hotbarSlotTouchAlreadySelected = false;
                                currentHotbarSlotTouch = slot;
                                hotbarSlotTouchStart = millis;
                                if (slot >= 0 && slot < 9) {
                                    this.mc.thePlayer.inventory.currentItem = slot;
                                }
                            } else {
                                if (millis - hotbarSlotTouchStart > 1200l) {
                                    hotbarSlotTouchStart = millis;
                                    this.mc.thePlayer.dropOneItem(true);
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
        ty = this.mc.displayHeight - ty - 1;
        tx = applyTouchHotbarTransformX(tx, false);
        ty = applyTouchHotbarTransformY(ty, false);
        return (tx >= hotbarAreaX && ty >= hotbarAreaY && tx < hotbarAreaX + hotbarAreaW
                && ty < hotbarAreaY + hotbarAreaH);
    }

    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float)Math.floor(hue)) * 6.0f;
            float f = h - (float)java.lang.Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
            case 0:
                r = (int) (brightness * 255.0f + 0.5f);
                g = (int) (t * 255.0f + 0.5f);
                b = (int) (p * 255.0f + 0.5f);
                break;
            case 1:
                r = (int) (q * 255.0f + 0.5f);
                g = (int) (brightness * 255.0f + 0.5f);
                b = (int) (p * 255.0f + 0.5f);
                break;
            case 2:
                r = (int) (p * 255.0f + 0.5f);
                g = (int) (brightness * 255.0f + 0.5f);
                b = (int) (t * 255.0f + 0.5f);
                break;
            case 3:
                r = (int) (p * 255.0f + 0.5f);
                g = (int) (q * 255.0f + 0.5f);
                b = (int) (brightness * 255.0f + 0.5f);
                break;
            case 4:
                r = (int) (t * 255.0f + 0.5f);
                g = (int) (p * 255.0f + 0.5f);
                b = (int) (brightness * 255.0f + 0.5f);
                break;
            case 5:
                r = (int) (brightness * 255.0f + 0.5f);
                g = (int) (p * 255.0f + 0.5f);
                b = (int) (q * 255.0f + 0.5f);
                break;
            }
        }

        return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
    }
}
