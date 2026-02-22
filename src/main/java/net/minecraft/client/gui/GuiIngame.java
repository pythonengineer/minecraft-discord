package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.touch.TouchControls;
import net.lax1dude.eaglercraft.touch.TouchOverlayRenderer;
import net.minecraft.client.ChatLine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.gui.container.GuiInventory;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;

public final class GuiIngame extends Gui {
    private static RenderItem itemRenderer = new RenderItem();
    private List chatMessageList = new ArrayList();
	private EaglercraftRandom rand = new EaglercraftRandom();
	private Minecraft mc;
    private int updateCounter = 0;

	public GuiIngame(Minecraft var1) {
		this.mc = var1;
	}

	public final void renderGameOverlay(float var1) {
        int scaledWidth = this.mc.scaledResolution.getScaledWidth();
        int scaledHeight = this.mc.scaledResolution.getScaledHeight();

		this.mc.entityRenderer.setupOverlayRendering();

        onBeginHotbarDraw();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
        InventoryPlayer var2 = this.mc.thePlayer.inventory;
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
		this.drawTexturedModalRect(i - 91 - 1 + var2.currentItem * 20, scaledHeight - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
        boolean var20 = this.mc.thePlayer.heartsLife / 3 % 2 == 1;
        if(this.mc.thePlayer.heartsLife < 10) {
            var20 = false;
		}

        int var6 = this.mc.thePlayer.health;
        int var7 = this.mc.thePlayer.prevHealth;
        this.rand.setSeed((long)(this.updateCounter * 312871));
        int var10;
        int var12;
        if(this.mc.playerController.shouldDrawHUD()) {
            EntityPlayerSP var8 = this.mc.thePlayer;
            var10 = var8.inventory.getPlayerArmorValue();

            int var11;
            int var13;
            for(var11 = 0; var11 < 10; ++var11) {
                var12 = scaledHeight - 32;
                if(var10 > 0) {
                    var13 = scaledWidth / 2 + 91 - (var11 << 3) - 9;
                    if((var11 << 1) + 1 < var10) {
                        this.drawTexturedModalRect(var13, var12, 34, 9, 9, 9);
                    }

                    if((var11 << 1) + 1 == var10) {
                        this.drawTexturedModalRect(var13, var12, 25, 9, 9, 9);
                    }

                    if((var11 << 1) + 1 > var10) {
                        this.drawTexturedModalRect(var13, var12, 16, 9, 9, 9);
                    }
                }

                byte var26 = 0;
                if(var20) {
                    var26 = 1;
                }

                int var14 = scaledWidth / 2 - 91 + (var11 << 3);
                if(var6 <= 4) {
                    var12 += this.rand.nextInt(2);
                }

                this.drawTexturedModalRect(var14, var12, 16 + var26 * 9, 0, 9, 9);
                if(var20) {
                    if((var11 << 1) + 1 < var7) {
                        this.drawTexturedModalRect(var14, var12, 70, 0, 9, 9);
                    }

                    if((var11 << 1) + 1 == var7) {
                        this.drawTexturedModalRect(var14, var12, 79, 0, 9, 9);
                    }
                }

                if((var11 << 1) + 1 < var6) {
                    this.drawTexturedModalRect(var14, var12, 52, 0, 9, 9);
                }

                if((var11 << 1) + 1 == var6) {
                    this.drawTexturedModalRect(var14, var12, 61, 0, 9, 9);
                }
            }

            if(this.mc.thePlayer.isInsideOfMaterial()) {
                var11 = (int)Math.ceil((double)(this.mc.thePlayer.air - 2) * 10.0D / 300.0D);
                var12 = (int)Math.ceil((double)this.mc.thePlayer.air * 10.0D / 300.0D) - var11;

                for(var13 = 0; var13 < var11 + var12; ++var13) {
                    if(var13 < var11) {
                        this.drawTexturedModalRect(scaledWidth / 2 - 91 + (var13 << 3), scaledHeight - 32 - 9, 16, 18, 9, 9);
                    } else {
                        this.drawTexturedModalRect(scaledWidth / 2 - 91 + (var13 << 3), scaledHeight - 32 - 9, 25, 18, 9, 9);
                    }
                }
            }
        }

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();

        for(var10 = 0; var10 < 9; ++var10) {
            int i25 = scaledWidth / 2 - 90 + var10 * 20 + 2;
            int i21 = scaledHeight - 16 - 3;
            ItemStack var22 = this.mc.thePlayer.inventory.mainInventory[var10];
            if(var22 != null) {
                float var9 = (float)var22.animationsToGo - var1;
                if(var9 > 0.0F) {
                    GL11.glPushMatrix();
                    float var25 = 1.0F + var9 / 5.0F;
                    GL11.glTranslatef((float)(i25 + 8), (float)(i21 + 12), 0.0F);
                    GL11.glScalef(1.0F / var25, (var25 + 1.0F) / 2.0F, 1.0F);
                    GL11.glTranslatef((float)(-(i25 + 8)), (float)(-(i21 + 12)), 0.0F);
                }

                itemRenderer.renderItemIntoGUI(this.mc.renderEngine, var22, i25, i21);
                if(var9 > 0.0F) {
                    GL11.glPopMatrix();
                }

                itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, var22, i25, i21);
            }
        }

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_NORMALIZE);

        onEndHotbarDraw();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
        this.drawTexturedModalRect(i - 7, scaledHeight / 2 - 7, 0, 0, 16, 16);
        GL11.glDisable(GL11.GL_BLEND);

        onBeginTouchGUI();

        if(this.mc.gameSettings.showFPS) {
            this.mc.fontRenderer.drawStringWithShadow("Minecraft Infdev (" + this.mc.debug + ")", 2, 2, 16777215);
            Minecraft var15 = this.mc;
            this.mc.fontRenderer.drawStringWithShadow(var15.renderGlobal.getDebugInfoRenders(), 2, 12, 16777215);
            var15 = this.mc;
            this.mc.fontRenderer.drawStringWithShadow(var15.renderGlobal.getDebugInfoEntities(), 2, 22, 16777215);
            var15 = this.mc;
            this.mc.fontRenderer.drawStringWithShadow("P: " + var15.effectRenderer.getStatistics() + ". T: " + var15.theWorld.getDebugLoadedEntities(), 2, 32, 16777215);
        } else {
            this.mc.fontRenderer.drawStringWithShadow("Minecraft Infdev", 2, 2, 16777215);
        }

        onEndTouchGUI();

        for(var7 = 0; var7 < this.chatMessageList.size() && var7 < 10; ++var7) {
            if(((ChatLine)this.chatMessageList.get(var7)).updateCounter < 200) {
                this.mc.fontRenderer.drawStringWithShadow(((ChatLine)this.chatMessageList.get(var7)).message, 2, scaledHeight - 8 - var7 * 9 - 20, 16777215);
            }
        }

	}

    public final void updateTick() {
        ++this.updateCounter;

        for(int var1 = 0; var1 < this.chatMessageList.size(); ++var1) {
            ++((ChatLine)this.chatMessageList.get(var1)).updateCounter;
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
                    this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer.inventory));
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
}
