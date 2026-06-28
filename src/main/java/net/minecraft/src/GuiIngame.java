package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.Touch;
import net.minecraft.client.Minecraft;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.touch.TouchControls;
import net.lax1dude.eaglercraft.touch.TouchOverlayRenderer;

public class GuiIngame extends Gui {
	private static RenderItem itemRenderer = new RenderItem();
	private List chatMessageList = new ArrayList();
	private EaglercraftRandom rand = new EaglercraftRandom();
	private Minecraft mc;
	public String field_933_a = null;
	private int updateCounter = 0;
	private String field_9420_i = "";
	private int field_9419_j = 0;
	public float field_6446_b;
	float field_931_c = 1.0F;

	public GuiIngame(Minecraft var1) {
		this.mc = var1;
	}

	public void renderGameOverlay(float var1, boolean var2, int var3, int var4) {
		ScaledResolution var5 = new ScaledResolution(this.mc);
		int var6 = var5.getScaledWidth();
		int var7 = var5.getScaledHeight();
		FontRenderer var8 = this.mc.fontRenderer;
		this.mc.entityRenderer.func_905_b();
		GL11.glEnable(GL11.GL_BLEND);
		if(this.mc.gameSettings.fancyGraphics) {
			this.func_4064_a(this.mc.thePlayer.getEntityBrightness(var1), var6, var7);
		}

		ItemStack var9 = this.mc.thePlayer.inventory.armorItemInSlot(3);
		if(!this.mc.gameSettings.thirdPersonView && var9 != null && var9.itemID == Block.pumpkin.blockID) {
			this.func_4063_a(var6, var7);
		}

		float var10 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * var1;
		if(var10 > 0.0F) {
			this.func_4065_b(var10, var6, var7);
		}

        onBeginHotbarDraw();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		InventoryPlayer var11 = this.mc.thePlayer.inventory;
		this.zLevel = -90.0F;
        int i = var6 / 2;
		this.drawTexturedModalRect(var6 / 2 - 91, var7 - 22, 0, 0, 182, 22);

        if (PointerInputAbstraction.isTouchMode()) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, TouchOverlayRenderer.spriteSheet);
            this.drawTexturedModalRect(i + 89, var7 - 22, 234, 0, 22, 22);
            int areaHAdd = 12;
            hotbarAreaX = (i - 91) * this.mc.displayWidth / var6;
            hotbarAreaY = (var7 - 22 - areaHAdd) * this.mc.displayHeight / var7;
            hotbarAreaW = 203 * this.mc.displayWidth / var6;
            hotbarAreaH = (22 + areaHAdd) * this.mc.displayHeight / var7;
        } else {
            hotbarAreaX = -1;
            hotbarAreaY = -1;
            hotbarAreaW = -1;
            hotbarAreaH = -1;
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		this.drawTexturedModalRect(var6 / 2 - 91 - 1 + var11.currentItem * 20, var7 - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
		boolean var12 = this.mc.thePlayer.field_9306_bj / 3 % 2 == 1;
		if(this.mc.thePlayer.field_9306_bj < 10) {
			var12 = false;
		}

		int var13 = this.mc.thePlayer.health;
		int var14 = this.mc.thePlayer.prevHealth;
		this.rand.setSeed((long)(this.updateCounter * 312871));
		int var15;
		int var16;
		int var17;
		if(this.mc.playerController.shouldDrawHUD()) {
			var15 = this.mc.thePlayer.getPlayerArmorValue();

			int var18;
			for(var16 = 0; var16 < 10; ++var16) {
				var17 = var7 - 32;
				if(var15 > 0) {
					var18 = var6 / 2 + 91 - var16 * 8 - 9;
					if(var16 * 2 + 1 < var15) {
						this.drawTexturedModalRect(var18, var17, 34, 9, 9, 9);
					}

					if(var16 * 2 + 1 == var15) {
						this.drawTexturedModalRect(var18, var17, 25, 9, 9, 9);
					}

					if(var16 * 2 + 1 > var15) {
						this.drawTexturedModalRect(var18, var17, 16, 9, 9, 9);
					}
				}

				byte var27 = 0;
				if(var12) {
					var27 = 1;
				}

				int var19 = var6 / 2 - 91 + var16 * 8;
				if(var13 <= 4) {
					var17 += this.rand.nextInt(2);
				}

				this.drawTexturedModalRect(var19, var17, 16 + var27 * 9, 0, 9, 9);
				if(var12) {
					if(var16 * 2 + 1 < var14) {
						this.drawTexturedModalRect(var19, var17, 70, 0, 9, 9);
					}

					if(var16 * 2 + 1 == var14) {
						this.drawTexturedModalRect(var19, var17, 79, 0, 9, 9);
					}
				}

				if(var16 * 2 + 1 < var13) {
					this.drawTexturedModalRect(var19, var17, 52, 0, 9, 9);
				}

				if(var16 * 2 + 1 == var13) {
					this.drawTexturedModalRect(var19, var17, 61, 0, 9, 9);
				}
			}

			if(this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
				var16 = (int)Math.ceil((double)(this.mc.thePlayer.air - 2) * 10.0D / 300.0D);
				var17 = (int)Math.ceil((double)this.mc.thePlayer.air * 10.0D / 300.0D) - var16;

				for(var18 = 0; var18 < var16 + var17; ++var18) {
					if(var18 < var16) {
						this.drawTexturedModalRect(var6 / 2 - 91 + var18 * 8, var7 - 32 - 9, 16, 18, 9, 9);
					} else {
						this.drawTexturedModalRect(var6 / 2 - 91 + var18 * 8, var7 - 32 - 9, 25, 18, 9, 9);
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

		for(var15 = 0; var15 < 9; ++var15) {
			var16 = var6 / 2 - 90 + var15 * 20 + 2;
			var17 = var7 - 16 - 3;
			this.func_554_a(var15, var16, var17, var1);
		}

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_RESCALE_NORMAL);

        onEndHotbarDraw();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
        this.drawTexturedModalRect(var6 / 2 - 7, var7 / 2 - 7, 0, 0, 16, 16);
        GL11.glDisable(GL11.GL_BLEND);

        onBeginTouchGUI();

		String var23;
		if(Keyboard.isKeyDown(Keyboard.KEY_F3)) {
			var8.drawStringWithShadow("Minecraft Beta 1.1_02 (" + this.mc.debug + ")", 2, 2, 16777215);
			var8.drawStringWithShadow(this.mc.func_6241_m(), 2, 12, 16777215);
			var8.drawStringWithShadow(this.mc.func_6262_n(), 2, 22, 16777215);
			var8.drawStringWithShadow(this.mc.func_6245_o(), 2, 32, 16777215);
			this.drawString(var8, "x: " + this.mc.thePlayer.posX, 2, 64, 14737632);
			this.drawString(var8, "y: " + this.mc.thePlayer.posY, 2, 72, 14737632);
			this.drawString(var8, "z: " + this.mc.thePlayer.posZ, 2, 80, 14737632);
		} else {
			var8.drawStringWithShadow("Minecraft Beta 1.1_02", 2, 2, 16777215);
		}

		if(this.field_9419_j > 0) {
			float var25 = (float)this.field_9419_j - var1;
			var16 = (int)(var25 * 256.0F / 20.0F);
			if(var16 > 255) {
				var16 = 255;
			}

			if(var16 > 0) {
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(var6 / 2), (float)(var7 - 48), 0.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				var17 = HSBtoRGB(var25 / 50.0F, 0.7F, 0.6F) & 16777215;
				var8.drawString(this.field_9420_i, -var8.getStringWidth(this.field_9420_i) / 2, -4, var17 + (var16 << 24));
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glPopMatrix();
			}
		}

        onEndTouchGUI();

		byte var26 = 10;
		boolean var28 = false;
		if(this.mc.currentScreen instanceof GuiChat) {
			var26 = 20;
			var28 = true;
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, (float)(var7 - 48), 0.0F);

		for(var17 = 0; var17 < this.chatMessageList.size() && var17 < var26; ++var17) {
			if(((ChatLine)this.chatMessageList.get(var17)).updateCounter < 200 || var28) {
				double var31 = (double)((ChatLine)this.chatMessageList.get(var17)).updateCounter / 200.0D;
				var31 = 1.0D - var31;
				var31 *= 10.0D;
				if(var31 < 0.0D) {
					var31 = 0.0D;
				}

				if(var31 > 1.0D) {
					var31 = 1.0D;
				}

				var31 *= var31;
				int var20 = (int)(255.0D * var31);
				if(var28) {
					var20 = 255;
				}

				if(var20 > 0) {
					byte var32 = 2;
					int var22 = -var17 * 9;
					var23 = ((ChatLine)this.chatMessageList.get(var17)).message;
					this.drawRect(var32, var22 - 1, var32 + 320, var22 + 8, var20 / 2 << 24);
					GL11.glEnable(GL11.GL_BLEND);
					var8.drawStringWithShadow(var23, var32, var22, 16777215 + (var20 << 24));
				}
			}
		}

		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void func_4063_a(int var1, int var2) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("%blur%/misc/pumpkinblur.png"));
		Tessellator var3 = Tessellator.instance;
		var3.startDrawingQuads();
		var3.addVertexWithUV(0.0D, (double)var2, -90.0D, 0.0D, 1.0D);
		var3.addVertexWithUV((double)var1, (double)var2, -90.0D, 1.0D, 1.0D);
		var3.addVertexWithUV((double)var1, 0.0D, -90.0D, 1.0D, 0.0D);
		var3.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		var3.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void func_4064_a(float var1, int var2, int var3) {
		var1 = 1.0F - var1;
		if(var1 < 0.0F) {
			var1 = 0.0F;
		}

		if(var1 > 1.0F) {
			var1 = 1.0F;
		}

		this.field_931_c = (float)((double)this.field_931_c + (double)(var1 - this.field_931_c) * 0.01D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
		GL11.glColor4f(this.field_931_c, this.field_931_c, this.field_931_c, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("%blur%/misc/vignette.png"));
		Tessellator var4 = Tessellator.instance;
		var4.startDrawingQuads();
		var4.addVertexWithUV(0.0D, (double)var3, -90.0D, 0.0D, 1.0D);
		var4.addVertexWithUV((double)var2, (double)var3, -90.0D, 1.0D, 1.0D);
		var4.addVertexWithUV((double)var2, 0.0D, -90.0D, 1.0D, 0.0D);
		var4.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		var4.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	private void func_4065_b(float var1, int var2, int var3) {
		var1 *= var1;
		var1 *= var1;
		var1 = var1 * 0.8F + 0.2F;
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, var1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
		float var4 = (float)(Block.portal.blockIndexInTexture % 16) / 16.0F;
		float var5 = (float)(Block.portal.blockIndexInTexture / 16) / 16.0F;
		float var6 = (float)(Block.portal.blockIndexInTexture % 16 + 1) / 16.0F;
		float var7 = (float)(Block.portal.blockIndexInTexture / 16 + 1) / 16.0F;
		Tessellator var8 = Tessellator.instance;
		var8.startDrawingQuads();
		var8.addVertexWithUV(0.0D, (double)var3, -90.0D, (double)var4, (double)var7);
		var8.addVertexWithUV((double)var2, (double)var3, -90.0D, (double)var6, (double)var7);
		var8.addVertexWithUV((double)var2, 0.0D, -90.0D, (double)var6, (double)var5);
		var8.addVertexWithUV(0.0D, 0.0D, -90.0D, (double)var4, (double)var5);
		var8.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void func_554_a(int var1, int var2, int var3, float var4) {
		ItemStack var5 = this.mc.thePlayer.inventory.mainInventory[var1];
		if(var5 != null) {
			float var6 = (float)var5.animationsToGo - var4;
			if(var6 > 0.0F) {
				GL11.glPushMatrix();
				float var7 = 1.0F + var6 / 5.0F;
				GL11.glTranslatef((float)(var2 + 8), (float)(var3 + 12), 0.0F);
				GL11.glScalef(1.0F / var7, (var7 + 1.0F) / 2.0F, 1.0F);
				GL11.glTranslatef((float)(-(var2 + 8)), (float)(-(var3 + 12)), 0.0F);
			}

			itemRenderer.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, var5, var2, var3);
			if(var6 > 0.0F) {
				GL11.glPopMatrix();
			}

			itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, var5, var2, var3);
		}
	}

	public void func_555_a() {
		if(this.field_9419_j > 0) {
			--this.field_9419_j;
		}

		++this.updateCounter;

		for(int var1 = 0; var1 < this.chatMessageList.size(); ++var1) {
			++((ChatLine)this.chatMessageList.get(var1)).updateCounter;
		}

	}

	public void addChatMessage(String var1) {
		while(this.mc.fontRenderer.getStringWidth(var1) > 320) {
			int var2;
			for(var2 = 1; var2 < var1.length() && this.mc.fontRenderer.getStringWidth(var1.substring(0, var2 + 1)) <= 320; ++var2) {
			}

			this.addChatMessage(var1.substring(0, var2));
			var1 = var1.substring(var2);
		}

		this.chatMessageList.add(0, new ChatLine(var1));

		while(this.chatMessageList.size() > 50) {
			this.chatMessageList.remove(this.chatMessageList.size() - 1);
		}

	}

	public void func_553_b(String var1) {
		this.field_9420_i = "Now playing: " + var1;
		this.field_9419_j = 60;
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
                    this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
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
