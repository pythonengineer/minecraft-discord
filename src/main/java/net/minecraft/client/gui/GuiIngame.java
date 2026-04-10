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
import net.minecraft.game.world.material.Material;

public final class GuiIngame extends Gui {
    private static RenderItem itemRenderer = new RenderItem();
    private List chatMessageList = new ArrayList();
	private EaglercraftRandom rand = new EaglercraftRandom();
	private Minecraft mc;
    private int updateCounter = 0;

	public GuiIngame(Minecraft mc) {
		this.mc = mc;
	}

	public final void renderGameOverlay(float partialTicks) {
        int scaledWidth = this.mc.scaledResolution.getScaledWidth();
        int scaledHeight = this.mc.scaledResolution.getScaledHeight();

		this.mc.entityRenderer.setupOverlayRendering();

        onBeginHotbarDraw();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
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
            EntityPlayerSP entityPlayerSP = this.mc.thePlayer;
            i10 = entityPlayerSP.inventory.getPlayerArmorValue();

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
            float f25 = (float)(scaledWidth / 2 - 90 + i10 * 20 + 2);
            i12 = scaledHeight - 16 - 3;
            float f21 = f25;
            ItemStack itemStack22;
            if((itemStack22 = this.mc.thePlayer.inventory.mainInventory[i10]) != null) {
                float f9;
                if((f9 = (float)itemStack22.animationsToGo - partialTicks) > 0.0F) {
                    GL11.glPushMatrix();
                    f25 = 1.0F + f9 / 5.0F;
                    GL11.glTranslatef((float)(f21 + 8), (float)(i12 + 12), 0.0F);
                    GL11.glScalef(1.0F / f25, (f25 + 1.0F) / 2.0F, 1.0F);
                    GL11.glTranslatef((float)(-(f21 + 8)), (float)(-(i12 + 12)), 0.0F);
                }

                itemRenderer.renderItemIntoGUI(this.mc.renderEngine, itemStack22, (int)f21, i12);
                if(f9 > 0.0F) {
                    GL11.glPopMatrix();
                }

                itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, itemStack22, (int)f21, i12);
            }
        }

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_RESCALE_NORMAL);

        onEndHotbarDraw();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
        this.drawTexturedModalRect(i - 7, scaledHeight / 2 - 7, 0, 0, 16, 16);
        GL11.glDisable(GL11.GL_BLEND);

        onBeginTouchGUI();

        if(this.mc.gameSettings.showFPS) {
            this.mc.fontRenderer.drawStringWithShadow("Minecraft Infdev (" + this.mc.debug + ")", 2, 2, 0xFFFFFF);
            this.mc.fontRenderer.drawStringWithShadow(this.mc.debugInfoRenders(), 2, 12, 0xFFFFFF);
            this.mc.fontRenderer.drawStringWithShadow(this.mc.getEntityDebug(), 2, 22, 0xFFFFFF);
            this.mc.fontRenderer.drawStringWithShadow(this.mc.debugInfoEntities(), 2, 32, 0xFFFFFF);
        } else {
            this.mc.fontRenderer.drawStringWithShadow("Minecraft Infdev", 2, 2, 0xFFFFFF);
        }

        onEndTouchGUI();

        for(i7 = 0; i7 < this.chatMessageList.size() && i7 < 10; ++i7) {
            if(((ChatLine)this.chatMessageList.get(i7)).updateCounter < 200) {
                this.mc.fontRenderer.drawStringWithShadow(((ChatLine)this.chatMessageList.get(i7)).message, 2, scaledHeight - 8 - i7 * 9 - 20, 16777215);
            }
        }

	}

    public final void updateTick() {
        ++this.updateCounter;

        for(int i1 = 0; i1 < this.chatMessageList.size(); ++i1) {
            ++((ChatLine)this.chatMessageList.get(i1)).updateCounter;
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
                    this.mc.setGuiScreen(new GuiInventory(this.mc.thePlayer.inventory));
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
