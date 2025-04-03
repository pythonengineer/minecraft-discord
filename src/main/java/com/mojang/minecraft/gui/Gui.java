package com.mojang.minecraft.gui;

import com.mojang.minecraft.GuiMessage;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.ScaledResolution;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.touch.TouchControls;
import net.lax1dude.eaglercraft.touch.TouchOverlayRenderer;

import java.util.ArrayList;
import java.util.List;

public final class Gui extends GuiComponent {
	public List messages = new ArrayList();
	private EaglercraftRandom random = new EaglercraftRandom();
	private Minecraft minecraft;
	private int scaledWidth;
	private int scaledHeight;
	public String hoveredUsername = null;
	public int tickCounter = 0;

	public Gui(Minecraft minecraft, int width, int height) {
		this.minecraft = minecraft;
	}

    public final void render(float scale, boolean playerAlive, int w, int h) {
        this.scaledWidth = Minecraft.scaledResolution.getScaledWidth();
        this.scaledHeight = Minecraft.scaledResolution.getScaledHeight();
		Font font5 = this.minecraft.font;
        this.minecraft.gameRenderer.render();

        onBeginHotbarDraw();

		Textures textures6 = this.minecraft.textures;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.textures.loadTexture("/gui/gui.png"));
		Tesselator tesselator7 = Tesselator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		Inventory inventory8 = this.minecraft.player.inventory;
		this.blitOffset = -90.0F;
        int i = this.scaledWidth / 2;
		this.blit(i - 91, this.scaledHeight - 22, 0, 0, 182, 22);

        if (PointerInputAbstraction.isTouchMode()) {
            //GL11.glBindTexture(GL11.GL_TEXTURE_2D, TouchOverlayRenderer.spriteSheet);
            //blit(i + 89, scaledHeight - 22, 234, 0, 22, 22);
            int areaHAdd = 12;
            hotbarAreaX = (i - 91) * this.minecraft.width / this.scaledWidth;
            hotbarAreaY = (this.scaledHeight - 22 - areaHAdd) * this.minecraft.height / this.scaledHeight;
            hotbarAreaW = 203 * this.minecraft.width / this.scaledWidth;
            hotbarAreaH = (22 + areaHAdd) * this.minecraft.height / this.scaledHeight;
        } else {
            hotbarAreaX = -1;
            hotbarAreaY = -1;
            hotbarAreaW = -1;
            hotbarAreaH = -1;
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.textures.loadTexture("/gui/gui.png"));
		this.blit(i - 91 - 1 + inventory8.selected * 20, this.scaledHeight - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.textures.loadTexture("/gui/icons.png"));
		boolean z9 = this.minecraft.player.invulnerableTime / 3 % 2 == 1;
		if(this.minecraft.player.invulnerableTime < 10) {
			z9 = false;
		}

		int i10 = this.minecraft.player.health;
		int i11 = this.minecraft.player.lastHealth;
		this.random.setSeed((long)(this.tickCounter * 312871));

		int i12;
		int i14;
		int i15;
		for(i12 = 0; i12 < 10; ++i12) {
			byte b13 = 0;
			if(z9) {
				b13 = 1;
			}

			i14 = this.scaledWidth / 2 - 91 + (i12 << 3);
			i15 = this.scaledHeight - 32;
			if(i10 <= 4) {
				i15 += this.random.nextInt(2);
			}

			this.blit(i14, i15, 16 + b13 * 9, 0, 9, 9);
			if(z9) {
				if((i12 << 1) + 1 < i11) {
					this.blit(i14, i15, 70, 0, 9, 9);
				}

				if((i12 << 1) + 1 == i11) {
					this.blit(i14, i15, 79, 0, 9, 9);
				}
			}

			if((i12 << 1) + 1 < i10) {
				this.blit(i14, i15, 52, 0, 9, 9);
			}

			if((i12 << 1) + 1 == i10) {
				this.blit(i14, i15, 61, 0, 9, 9);
			}
		}

		int i25;
		if(this.minecraft.player.isUnderWater()) {
			i12 = (int)Math.ceil((double)(this.minecraft.player.airSupply - 2) * 10.0D / 300.0D);
			i25 = (int)Math.ceil((double)this.minecraft.player.airSupply * 10.0D / 300.0D) - i12;

			for(i14 = 0; i14 < i12 + i25; ++i14) {
				if(i14 < i12) {
					this.blit(this.scaledWidth / 2 - 91 + (i14 << 3), this.scaledHeight - 32 - 9, 16, 18, 9, 9);
				} else {
					this.blit(this.scaledWidth / 2 - 91 + (i14 << 3), this.scaledHeight - 32 - 9, 25, 18, 9, 9);
				}
			}
		}

		GL11.glDisable(GL11.GL_BLEND);

		String string21;
		for(i12 = 0; i12 < inventory8.slots.length; ++i12) {
			i25 = this.scaledWidth / 2 - 90 + i12 * 20;
			i14 = this.scaledHeight - 16;
			if((i15 = inventory8.slots[i12]) > 0) {
				GL11.glPushMatrix();
				GL11.glTranslatef((float)i25, (float)i14, -50.0F);
				if(inventory8.popTime[i12] > 0) {
					float f18;
					float f19 = -((float)Math.sin((double)((f18 = ((float)inventory8.popTime[i12] - scale) / 5.0F) * f18) * Math.PI)) * 8.0F;
					float f23 = (float)Math.sin((double)(f18 * f18) * Math.PI) + 1.0F;
					float f16 = (float)Math.sin((double)f18 * Math.PI) + 1.0F;
					GL11.glTranslatef(10.0F, f19 + 10.0F, 0.0F);
					GL11.glScalef(f23, f16, 1.0F);
					GL11.glTranslatef(-10.0F, -10.0F, 0.0F);
				}

				GL11.glScalef(10.0F, 10.0F, 10.0F);
				GL11.glTranslatef(1.0F, 0.5F, 0.0F);
				GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
				GL11.glScalef(-1.0F, -1.0F, -1.0F);
				int i20 = textures6.loadTexture("/terrain.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, i20);
				tesselator7.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
				Tile.tiles[i15].render(tesselator7, this.minecraft.level, 0, -2, 0, 0);
				tesselator7.end();
				GL11.glPopMatrix();
				if(inventory8.count[i12] > 1) {
					string21 = "" + inventory8.count[i12];
					font5.drawShadow(string21, i25 + 19 - font5.width(string21), i14 + 6, 0xFFFFFF);
				}
			}
		}

        onEndHotbarDraw();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.textures.loadTexture("/gui/icons.png"));
        this.blit(i - 7, this.scaledHeight / 2 - 7, 0, 0, 16, 16);
        GL11.glDisable(GL11.GL_BLEND);

		font5.drawShadow("0.27   SURVIVAL TEST", 2, 2, 0xFFFFFF);
		if(this.minecraft.options.showFramerate) {
			font5.drawShadow(this.minecraft.fpsString, 2, 12, 0xFFFFFF);
		}

        String string26 = "Score: &e" + this.minecraft.player.getScore();
        if (PointerInputAbstraction.isTouchMode()) {
            GL11.glPushMatrix();
            GL11.glScalef(1.5f, 1.5f, 1.5f);
            font5.drawShadow(string26, (this.scaledWidth + font5.width(string26)) / 2, 3, 16777215);
            GL11.glPopMatrix();
        } else {
            font5.drawShadow(string26, this.scaledWidth - font5.width(string26) - 2, 2, 16777215);
        }
        onBeginHotbarDraw();
        font5.drawShadow("Arrows: " + this.minecraft.player.arrows, this.scaledWidth / 2 + 8, this.scaledHeight - 33, 16777215);
        onEndHotbarDraw();
		byte b24 = 10;
		boolean z26 = false;
		if(this.minecraft.screen instanceof ChatScreen) {
			b24 = 20;
			z26 = true;
		}

		for(i14 = 0; i14 < this.messages.size() && i14 < b24; ++i14) {
			if(((GuiMessage)this.messages.get(i14)).counter < 200 || z26) {
				font5.drawShadow(((GuiMessage)this.messages.get(i14)).message, 2, this.scaledHeight - 8 - i14 * 9 - 20, 0xFFFFFF);
			}
		}

		i14 = this.scaledWidth / 2;
		i15 = this.scaledHeight / 2;
		this.hoveredUsername = null;
		if(Keyboard.isKeyDown(Keyboard.KEY_TAB) && this.minecraft.networkClient != null && this.minecraft.networkClient.isConnected()) {
			List list22 = this.minecraft.networkClient.getUsernames();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glBegin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.7F);
			GL11.glVertex2f((float)(i14 + 128), (float)(i15 - 68 - 12));
			GL11.glVertex2f((float)(i14 - 128), (float)(i15 - 68 - 12));
			GL11.glColor4f(0.2F, 0.2F, 0.2F, 0.8F);
			GL11.glVertex2f((float)(i14 - 128), (float)(i15 + 68));
			GL11.glVertex2f((float)(i14 + 128), (float)(i15 + 68));
			GL11.glEnd();
			GL11.glDisable(GL11.GL_BLEND);
			string21 = "Connected players:";
			font5.drawShadow(string21, i14 - font5.width(string21) / 2, i15 - 64 - 12, 0xFFFFFF);

			for(i11 = 0; i11 < list22.size(); ++i11) {
				int i27 = i14 + i11 % 2 * 120 - 120;
				int i17 = i15 - 64 + (i11 / 2 << 3);
				if(playerAlive && w >= i27 && h >= i17 && w < i27 + 120 && h < i17 + 8) {
					this.hoveredUsername = (String)list22.get(i11);
					font5.draw((String)list22.get(i11), i27 + 2, i17, 0xFFFFFF);
				} else {
					font5.draw((String)list22.get(i11), i27, i17, 15658734);
				}
			}
		}

	}

	public final void addMessage(String message) {
		this.messages.add(0, new GuiMessage(message));

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
                    hotbarSlotTouchAlreadySelected = (this.minecraft.player.inventory.selected == currentHotbarSlotTouch);
                    this.minecraft.player.inventory.selected = currentHotbarSlotTouch;
                } else if (currentHotbarSlotTouch == 9) {
                    //hotbarSlotTouchAlreadySelected = false;
                    //currentHotbarSlotTouch = 69;
                    //this.minecraft.setScreen(new InventoryScreen());
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
                                    this.minecraft.player.inventory.selected = slot;
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