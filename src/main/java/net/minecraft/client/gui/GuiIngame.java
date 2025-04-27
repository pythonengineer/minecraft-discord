package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.ScaledResolution;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.touch.TouchControls;
import net.lax1dude.eaglercraft.touch.TouchOverlayRenderer;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.ChatLine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.controller.PlayerControllerSP;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;

public final class GuiIngame extends Gui {
    private List chatMessageList = new ArrayList();
	private EaglercraftRandom rand = new EaglercraftRandom();
	private Minecraft mc;
    private int scaledWidth;
    private int scaledHeight;
    private int updateCounter = 0;
	private RenderBlocks blockRenderer = new RenderBlocks(Tessellator.instance);

	public GuiIngame(Minecraft var1, int var2, int var3) {
		this.mc = var1;
	}

	public final void renderGameOverlay() {
        this.scaledWidth = this.mc.scaledResolution.getScaledWidth();
        this.scaledHeight = this.mc.scaledResolution.getScaledHeight();

        FontRenderer var1 = this.mc.fontRenderer;
		this.mc.entityRenderer.setupOverlayRendering();

        onBeginHotbarDraw();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
        InventoryPlayer var2 = this.mc.thePlayer.inventory;
		this.zLevel = -90.0F;
        int i = this.scaledWidth / 2;
		this.drawTexturedModalRect(i - 91, this.scaledHeight - 22, 0, 0, 182, 22);

        if (PointerInputAbstraction.isTouchMode()) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, TouchOverlayRenderer.spriteSheet);
            this.drawTexturedModalRect(i + 89, this.scaledHeight - 22, 234, 0, 22, 22);
            int areaHAdd = 12;
            hotbarAreaX = (i - 91) * this.mc.displayWidth / this.scaledWidth;
            hotbarAreaY = (this.scaledHeight - 22 - areaHAdd) * this.mc.displayHeight / this.scaledHeight;
            hotbarAreaW = 203 * this.mc.displayWidth / this.scaledWidth;
            hotbarAreaH = (22 + areaHAdd) * this.mc.displayHeight / this.scaledHeight;
        } else {
            hotbarAreaX = -1;
            hotbarAreaY = -1;
            hotbarAreaW = -1;
            hotbarAreaH = -1;
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		this.drawTexturedModalRect(i - 91 - 1 + var2.currentItem * 20, this.scaledHeight - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
        boolean var9 = this.mc.thePlayer.heartsLife / 3 % 2 == 1;
        if(this.mc.thePlayer.heartsLife < 10) {
            var9 = false;
		}

        int var3 = this.mc.thePlayer.health;
        int var4 = this.mc.thePlayer.prevHealth;
        this.rand.setSeed((long)(this.updateCounter * 312871));
        int var5;
        int var7;
        int var8;
        int var12;
        if(this.mc.playerController.shouldDrawHUD()) {
            for(var5 = 0; var5 < 10; ++var5) {
                byte var6 = 0;
                if(var9) {
                    var6 = 1;
                }

                var7 = this.scaledWidth / 2 - 91 + (var5 << 3);
                var8 = this.scaledHeight - 32;
                if(var3 <= 4) {
                    var8 += this.rand.nextInt(2);
                }

                this.drawTexturedModalRect(var7, var8, 16 + var6 * 9, 0, 9, 9);
                if(var9) {
                    if((var5 << 1) + 1 < var4) {
                        this.drawTexturedModalRect(var7, var8, 70, 0, 9, 9);
                    }

                    if((var5 << 1) + 1 == var4) {
                        this.drawTexturedModalRect(var7, var8, 79, 0, 9, 9);
                    }
                }

                if((var5 << 1) + 1 < var3) {
                    this.drawTexturedModalRect(var7, var8, 52, 0, 9, 9);
                }

                if((var5 << 1) + 1 == var3) {
                    this.drawTexturedModalRect(var7, var8, 61, 0, 9, 9);
                }
            }

            if(this.mc.thePlayer.isInsideOfMaterial()) {
                var5 = (int)Math.ceil((double)(this.mc.thePlayer.air - 2) * 10.0D / 300.0D);
                var12 = (int)Math.ceil((double)this.mc.thePlayer.air * 10.0D / 300.0D) - var5;

                for(var7 = 0; var7 < var5 + var12; ++var7) {
                    if(var7 < var5) {
                        this.drawTexturedModalRect(this.scaledWidth / 2 - 91 + (var7 << 3), this.scaledHeight - 32 - 9, 16, 18, 9, 9);
                    } else {
                        this.drawTexturedModalRect(this.scaledWidth / 2 - 91 + (var7 << 3), this.scaledHeight - 32 - 9, 25, 18, 9, 9);
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

        for(var5 = 0; var5 < 9; ++var5) {
            var12 = this.scaledWidth / 2 - 90 + var5 * 20 + 2;
            var7 = this.scaledHeight - 16 - 3;
            ItemStack var13 = this.mc.thePlayer.inventory.mainInventory[var5];
            if(var13 == null) {
                if(var5 > 50) {
                    GL11.glDisable(GL11.GL_LIGHTING);
                    var8 = this.mc.renderEngine.getTexture("/gui/items.png");
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, var8);
                    this.drawTexturedModalRect(var12, var7, 240, 63 - var5 << 4, 16, 16);
                    GL11.glEnable(GL11.GL_LIGHTING);
                }
            } else {
                String var14 = null;
                var3 = var13.itemID;
                if(var13.itemID < 256) {
                    var8 = this.mc.renderEngine.getTexture("/terrain.png");
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, var8);
                    Block var10 = Block.blocksList[var3];
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float)(var12 - 2), (float)(var7 + 3), 0.0F);
                    GL11.glScalef(10.0F, 10.0F, 10.0F);
                    GL11.glTranslatef(1.0F, 0.5F, 8.0F);
                    GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    this.blockRenderer.renderBlockOnInventory(var10);
                    GL11.glPopMatrix();
                } else if(var13.getItem().getIconIndex() >= 0) {
                    GL11.glDisable(GL11.GL_LIGHTING);
                    var8 = this.mc.renderEngine.getTexture("/gui/items.png");
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, var8);
                    this.drawTexturedModalRect(var12, var7, var13.getItem().getIconIndex() % 16 << 4, var13.getItem().getIconIndex() / 16 << 4, 16, 16);
                    GL11.glEnable(GL11.GL_LIGHTING);
                }

                if(var13.stackSize > 1) {
                    var14 = "" + var13.stackSize;
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    this.mc.fontRenderer.drawStringWithShadow(var14, var12 + 19 - 2 - this.mc.fontRenderer.getStringWidth(var14), var7 + 6 + 3, 16777215);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                }
            }
        }

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_NORMALIZE);

        onEndHotbarDraw();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
        this.drawTexturedModalRect(i - 7, this.scaledHeight / 2 - 7, 0, 0, 16, 16);
        GL11.glDisable(GL11.GL_BLEND);

        onBeginTouchGUI();

		var1.drawStringWithShadow("0.31", 2, 2, 16777215);
		if(this.mc.options.showFPS) {
			var1.drawStringWithShadow(this.mc.debug, 2, 12, 16777215);
		}

        onEndTouchGUI();

        if (PointerInputAbstraction.isTouchMode()) {
            GL11.glPopMatrix();
        }

		if(this.mc.playerController instanceof PlayerControllerSP) {
			String var17 = "Score: &e" + this.mc.thePlayer.getScore();
            if (PointerInputAbstraction.isTouchMode()) {
                onBeginTouchGUI();
                var1.drawStringWithShadow(var17, (this.scaledWidth + var1.getStringWidth(var17)) / 2, 3, 16777215);
                onEndTouchGUI();
            } else {
                var1.drawStringWithShadow(var17, this.scaledWidth - var1.getStringWidth(var17) - 2, 2, 16777215);
            }
            onBeginHotbarDraw();
			var1.drawStringWithShadow("Arrows: " + this.mc.thePlayer.arrows, this.scaledWidth / 2 + 8, this.scaledHeight - 33, 16777215);
			onEndHotbarDraw();
		}

        for(var7 = 0; var7 < this.chatMessageList.size() && var7 < 10; ++var7) {
            if(((ChatLine)this.chatMessageList.get(var7)).updateCounter < 200) {
                this.chatMessageList.get(var7);
                var1.drawStringWithShadow((String)null, 2, this.scaledHeight - 8 - var7 * 9 - 20, 16777215);
            }
        }

	}

    public final void addChatMessage() {
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
                    this.mc.playerController.openInventory();
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
