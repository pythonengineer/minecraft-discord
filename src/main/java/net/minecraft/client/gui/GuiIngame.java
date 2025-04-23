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
import net.minecraft.client.player.InventoryPlayer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.block.Block;

public final class GuiIngame extends Gui {
	public List chatMessageList = new ArrayList();
	private EaglercraftRandom rand = new EaglercraftRandom();
	private Minecraft mc;
	private int ingameWidth;
	private int ingameHeight;
	public int updateCounter = 0;
	private RenderBlocks blockRenderer = new RenderBlocks(Tessellator.instance);

	public GuiIngame(Minecraft var1, int var2, int var3) {
		this.mc = var1;
	}

	public final void renderGameOverlay(float var1) {
        this.ingameWidth = this.mc.scaledResolution.getScaledWidth();
        this.ingameHeight = this.mc.scaledResolution.getScaledHeight();
		FontRenderer var2 = this.mc.fontRenderer;
		this.mc.entityRenderer.setupOverlayRendering();

        onBeginHotbarDraw();

		RenderEngine var3 = this.mc.renderEngine;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		InventoryPlayer var4 = this.mc.thePlayer.inventory;
		this.zLevel = -90.0F;
        int i = this.ingameWidth / 2;
		this.drawTexturedModal(i - 91, this.ingameHeight - 22, 0, 0, 182, 22);

        if (PointerInputAbstraction.isTouchMode()) {
            if (this.mc.playerController instanceof PlayerControllerCreative) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, TouchOverlayRenderer.spriteSheet);
                this.drawTexturedModal(i + 89, this.ingameHeight - 22, 234, 0, 22, 22);
            }

            int areaHAdd = 12;
            hotbarAreaX = (i - 91) * this.mc.displayWidth / this.ingameWidth;
            hotbarAreaY = (this.ingameHeight - 22 - areaHAdd) * this.mc.displayHeight / this.ingameHeight;
            hotbarAreaW = 203 * this.mc.displayWidth / this.ingameWidth;
            hotbarAreaH = (22 + areaHAdd) * this.mc.displayHeight / this.ingameHeight;
        } else {
            hotbarAreaX = -1;
            hotbarAreaY = -1;
            hotbarAreaW = -1;
            hotbarAreaH = -1;
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		this.drawTexturedModal(i - 91 - 1 + var4.currentItem * 20, this.ingameHeight - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
		boolean var5 = this.mc.thePlayer.scoreValue / 3 % 2 == 1;
		if(this.mc.thePlayer.scoreValue < 10) {
			var5 = false;
		}

		int var6 = this.mc.thePlayer.health;
		int var7 = this.mc.thePlayer.prevHealth;
		this.rand.setSeed((long)(this.updateCounter * 312871));
		int var8;
		int var10;
		int var11;
		int var18;
		if(this.mc.playerController.shouldDrawHUD()) {
			for(var8 = 0; var8 < 10; ++var8) {
				byte var9 = 0;
				if(var5) {
					var9 = 1;
				}

				var10 = this.ingameWidth / 2 - 91 + (var8 << 3);
				var11 = this.ingameHeight - 32;
				if(var6 <= 4) {
					var11 += this.rand.nextInt(2);
				}

				this.drawTexturedModal(var10, var11, 16 + var9 * 9, 0, 9, 9);
				if(var5) {
					if((var8 << 1) + 1 < var7) {
						this.drawTexturedModal(var10, var11, 70, 0, 9, 9);
					}

					if((var8 << 1) + 1 == var7) {
						this.drawTexturedModal(var10, var11, 79, 0, 9, 9);
					}
				}

				if((var8 << 1) + 1 < var6) {
					this.drawTexturedModal(var10, var11, 52, 0, 9, 9);
				}

				if((var8 << 1) + 1 == var6) {
					this.drawTexturedModal(var10, var11, 61, 0, 9, 9);
				}
			}

			if(this.mc.thePlayer.isInsideOfMaterial()) {
				var8 = (int)Math.ceil((double)(this.mc.thePlayer.air - 2) * 10.0D / 300.0D);
				var18 = (int)Math.ceil((double)this.mc.thePlayer.air * 10.0D / 300.0D) - var8;

				for(var10 = 0; var10 < var8 + var18; ++var10) {
					if(var10 < var8) {
						this.drawTexturedModal(this.ingameWidth / 2 - 91 + (var10 << 3), this.ingameHeight - 32 - 9, 16, 18, 9, 9);
					} else {
						this.drawTexturedModal(this.ingameWidth / 2 - 91 + (var10 << 3), this.ingameHeight - 32 - 9, 25, 18, 9, 9);
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

		for(var8 = 0; var8 < var4.mainInventory.length; ++var8) {
			var18 = this.ingameWidth / 2 - 90 + var8 * 20;
			var10 = this.ingameHeight - 16;
			var11 = var4.mainInventory[var8];
			if(var11 > 0) {
				GL11.glPushMatrix();
				GL11.glTranslatef((float)var18, (float)var10, -50.0F);
				if(var4.animationsToGo[var8] > 0) {
					float var12;
					float var13 = -MathHelper.sin((var12 = ((float)var4.animationsToGo[var8] - var1) / 5.0F) * var12 * (float)Math.PI) * 8.0F;
					float var16 = MathHelper.sin(var12 * var12 * (float)Math.PI) + 1.0F;
					var12 = MathHelper.sin(var12 * (float)Math.PI) + 1.0F;
					GL11.glTranslatef(10.0F, var13 + 10.0F, 0.0F);
					GL11.glScalef(var16, var12, 1.0F);
					GL11.glTranslatef(-10.0F, -10.0F, 0.0F);
				}

				GL11.glScalef(10.0F, 10.0F, 10.0F);
				GL11.glTranslatef(1.0F, 0.5F, 0.0F);
				GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				int var14 = var3.getTexture("/terrain.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var14);
				this.blockRenderer.renderBlockOnInventory(Block.blocksList[var11]);
				GL11.glPopMatrix();
				if(var4.stackSize[var8] > 1) {
					String var15 = "" + var4.stackSize[var8];
					var2.drawStringWithShadow(var15, var18 + 19 - var2.getWidth(var15), var10 + 6, 16777215);
				}
			}
		}

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_NORMALIZE);

        onEndHotbarDraw();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
        this.drawTexturedModal(i - 7, this.ingameHeight / 2 - 7, 0, 0, 16, 16);
        GL11.glDisable(GL11.GL_BLEND);

		var2.drawStringWithShadow("0.31", 2, 2, 16777215);
		if(this.mc.options.showFPS) {
			var2.drawStringWithShadow(this.mc.debug, 2, 12, 16777215);
		}

		if(this.mc.playerController instanceof PlayerControllerSP) {
			String var17 = "Score: &e" + this.mc.thePlayer.getScore();
            if (PointerInputAbstraction.isTouchMode()) {
                GL11.glPushMatrix();
                GL11.glScalef(1.5f, 1.5f, 1.5f);
                var2.drawStringWithShadow(var17, (this.ingameWidth + var2.getWidth(var17)) / 2, 3, 16777215);
                GL11.glPopMatrix();
            } else {
                var2.drawStringWithShadow(var17, this.ingameWidth - var2.getWidth(var17) - 2, 2, 16777215);
            }
			var2.drawStringWithShadow(var17, this.ingameWidth - var2.getWidth(var17) - 2, 2, 16777215);
            onBeginHotbarDraw();
			var2.drawStringWithShadow("Arrows: " + this.mc.thePlayer.getArrows, this.ingameWidth / 2 + 8, this.ingameHeight - 33, 16777215);
			onEndHotbarDraw();
		}

		for(var10 = 0; var10 < this.chatMessageList.size() && var10 < 10; ++var10) {
			if(((ChatLine)this.chatMessageList.get(var10)).updateCounter < 200) {
				this.chatMessageList.get(var10);
				var2.drawStringWithShadow((String)null, 2, this.ingameHeight - 8 - var10 * 9 - 20, 16777215);
			}
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
                } else if (currentHotbarSlotTouch == 9 && this.mc.playerController instanceof PlayerControllerCreative) {
                    hotbarSlotTouchAlreadySelected = false;
                    currentHotbarSlotTouch = 69;
                    this.mc.displayGuiScreen(new GuiCreativeInventory());
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
        ty = this.mc.displayHeight - ty - 1;
        tx = applyTouchHotbarTransformX(tx, false);
        ty = applyTouchHotbarTransformY(ty, false);
        return (tx >= hotbarAreaX && ty >= hotbarAreaY && tx < hotbarAreaX + hotbarAreaW
                && ty < hotbarAreaY + hotbarAreaH);
    }
}
