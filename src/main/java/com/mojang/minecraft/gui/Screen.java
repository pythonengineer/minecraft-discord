package com.mojang.minecraft.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.renderer.Tesselator;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.internal.EnumTouchEvent;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;
import net.lax1dude.eaglercraft.lwjgl.input.Mouse;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.touch.TouchControls;

public class Screen {
	protected Minecraft minecraft;
	protected int width;
	protected int height;
    protected List buttons = new ArrayList();

    protected int touchModeCursorPosX = -1;
    protected int touchModeCursorPosY = -1;
    private long lastTouchEvent;

    public void render(int i1, int i2) {
        for(int i3 = 0; i3 < this.buttons.size(); ++i3) {
            Button button4;
            if((button4 = (Button)this.buttons.get(i3)).visible) {
                if(!button4.enabled) {
                    fill(button4.x - 1, button4.y - 1, button4.x + button4.w + 1, button4.y + button4.h + 1, -8355680);
                    fill(button4.x, button4.y, button4.x + button4.w, button4.y + button4.h, -7303024);
                    this.drawCenteredString(button4.msg, button4.x + button4.w / 2, button4.y + (button4.h - 8) / 2, -6250336);
                } else {
                    fill(button4.x - 1, button4.y - 1, button4.x + button4.w + 1, button4.y + button4.h + 1, 0xFF000000);
                    if(i1 >= button4.x && i2 >= button4.y && i1 < button4.x + button4.w && i2 < button4.y + button4.h) {
                        fill(button4.x - 1, button4.y - 1, button4.x + button4.w + 1, button4.y + button4.h + 1, -6250336);
                        fill(button4.x, button4.y, button4.x + button4.w, button4.y + button4.h, -8355680);
                        this.drawCenteredString(button4.msg, button4.x + button4.w / 2, button4.y + (button4.h - 8) / 2, 16777120);
                    } else {
                        fill(button4.x, button4.y, button4.x + button4.w, button4.y + button4.h, -9408400);
                        this.drawCenteredString(button4.msg, button4.x + button4.w / 2, button4.y + (button4.h - 8) / 2, 14737632);
                    }
                }
            }
        }
	}

    protected void keyPressed(char c1, int i2) {
        if(i2 == 1) {
            this.minecraft.setScreen((Screen)null);
            this.minecraft.grabMouse();
        }

    }

    protected void buttonClicked(Button button1) {
    }

    public final void init(Minecraft minecraft1, int i2, int i3) {
        this.minecraft = minecraft1;
        this.width = i2;
        this.height = i3;
        this.init();
    }

	public void init() {
	}

    protected static void fill(int i0, int i1, int i2, int i3, int i4) {
        float f5 = (float)(i4 >>> 24) / 255.0F;
        float f6 = (float)(i4 >> 16 & 255) / 255.0F;
        float f7 = (float)(i4 >> 8 & 255) / 255.0F;
        float f9 = (float)(i4 & 255) / 255.0F;
        Tesselator tesselator8 = Tesselator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f6, f7, f9, f5);
        tesselator8.begin(DefaultVertexFormats.POSITION);
        tesselator8.vertex((float)i0, (float)i3, 0.0F);
        tesselator8.vertex((float)i2, (float)i3, 0.0F);
        tesselator8.vertex((float)i2, (float)i1, 0.0F);
        tesselator8.vertex((float)i0, (float)i1, 0.0F);
        tesselator8.end();
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected static void fillGradient(int i0, int i1, int i2, int i3, int i4, int i5) {
        float f9 = (float)96 / 255.0F;
        float f10 = (float)5 / 255.0F;
        float f11 = (float)5 / 255.0F;
        float f12 = (float)160 / 255.0F;
        float f6 = (float)48 / 255.0F;
        float f7 = (float)48 / 255.0F;
        float f8 = (float)96 / 255.0F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBegin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        GL11.glColor4f(f10, f11, 0.0F, f9);
        GL11.glVertex2f((float)i2, 0.0F);
        GL11.glVertex2f(0.0F, 0.0F);
        GL11.glColor4f(f6, f7, f8, f12);
        GL11.glVertex2f(0.0F, (float)i3);
        GL11.glVertex2f((float)i2, (float)i3);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
    }

	public void drawCenteredString(String str, int x, int y, int color) {
		Font font = this.minecraft.font;
		font.drawShadow(str, x - font.width(str) / 2, y, color);
	}

	public void drawString(String str, int x, int y, int color) {
		Font font = this.minecraft.font;
		font.drawShadow(str, x, y, color);
	}

	public void updateEvents() {
        boolean noTouch = true;
        while (Touch.next()) {
            noTouch = false;
            this.handleTouchInput();
            TouchControls.handleInput();
        }

		while(Mouse.next()) {
			if(noTouch && Mouse.getEventButtonState()) {
				int xm = Mouse.getEventX() * this.width / this.minecraft.width;
				int ym = this.height - Mouse.getEventY() * this.height / this.minecraft.height - 1;
				this.mouseClicked(xm, ym, Mouse.getEventButton());
			}
		}

		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				this.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			}
		}

	}

    protected void touchStarted(int parInt1, int parInt2, int parInt3) {
        if (shouldTouchGenerateMouseEvents()) {
            this.mouseClicked(parInt1, parInt2, 12345);
        }
    }

    protected void touchTapped(int parInt1, int parInt2, int parInt3) {
        if (shouldTouchGenerateMouseEvents()) {
            this.mouseClicked(parInt1, parInt2, 0);
            //this.mouseReleased(parInt1, parInt2, 0);
        }
    }

    protected void touchMoved(int parInt1, int parInt2, int parInt3) {
    }

    protected void touchEndMove(int parInt1, int parInt2, int parInt3) {
        if (shouldTouchGenerateMouseEvents()) {
            //this.mouseReleased(parInt1, parInt2, 12345);
        }
    }

    public final Map<Integer, int[]> touchStarts = new HashMap<>();

    public void handleTouchInput() {
        EnumTouchEvent et = Touch.getEventType();
        if (et == EnumTouchEvent.TOUCHSTART) {
            PointerInputAbstraction.enterTouchModeHook();
        }
        float scaleFac = getEaglerScale();
        for (int t = 0, c = Touch.getEventTouchPointCount(); t < c; ++t) {
            int u = Touch.getEventTouchPointUID(t);
            int i = Touch.getEventTouchX(t);
            int j = Touch.getEventTouchY(t);
            if (et == EnumTouchEvent.TOUCHSTART) {
                if (TouchControls.handleTouchBegin(u, i, j)) {
                    continue;
                }
            } else if (et == EnumTouchEvent.TOUCHEND) {
                if (TouchControls.handleTouchEnd(u, i, j)) {
                    continue;
                }
            }
            i = applyEaglerScale(scaleFac, i * this.width / this.minecraft.width, this.width);
            j = applyEaglerScale(scaleFac, this.height - j * this.height / this.minecraft.height - 1, this.height);
            float rad = Touch.getEventTouchRadiusMixed(t);
            float si = rad * this.width / this.minecraft.width / scaleFac;
            if (si < 1.0f)
                si = 1.0f;
            float sj = rad * this.height / this.minecraft.height / scaleFac;
            if (sj < 1.0f)
                sj = 1.0f;
            int[] ck = touchStarts.remove(u);
            switch (et) {
            case TOUCHSTART:
                if (t == 0) {
                    touchModeCursorPosX = i;
                    touchModeCursorPosY = j;
                }
                lastTouchEvent = EagRuntime.currentTimeMillis();
                touchStarts.put(u, new int[] { i, j, 0 });
                this.touchStarted(i, j, u);
                break;
            case TOUCHMOVE:
                if (t == 0) {
                    touchModeCursorPosX = i;
                    touchModeCursorPosY = j;
                }
                if (ck != null && Math.abs(ck[0] - i) < si && Math.abs(ck[1] - j) < sj) {
                    touchStarts.put(u, ck);
                    break;
                }
                touchStarts.put(u, new int[] { i, j, (ck != null && isTouchDraggingStateLocked(u)) ? ck[2] : 1 });
                this.touchMoved(i, j, u);
                if (t == 0 && shouldTouchGenerateMouseEvents()) {
                    //this.mouseClickMove(i, j, 0, EagRuntime.currentTimeMillis() - lastTouchEvent);
                }
                break;
            case TOUCHEND:
                if (ck == null)
                    break;
                if (t == 0) {
                    touchModeCursorPosX = -1;
                    touchModeCursorPosY = -1;
                }
                if (ck != null && ck[2] == 1) {
                    this.touchEndMove(i, j, u);
                } else {
                    if (ck != null) {
                        i = ck[0];
                        j = ck[1];
                    }
                    this.touchTapped(i, j, u);
                }
                break;
            }
        }
    }

    public boolean isTouchPointDragging(int uid) {
        int[] ret = touchStarts.get(uid);
        return ret != null && ret[2] == 1;
    }

	protected void mouseClicked(int x, int y, int button) {
        int i3 = y;
        int i2 = x;
        int i4 = button;
        Screen screen6 = this;
        if(i4 != 0) {
            return;
        }

        i4 = 0;

        while(true) {
            if(i4 >= screen6.buttons.size()) {
                break;
            }

            Button button5 = (Button)screen6.buttons.get(i4);
            if(i2 >= button5.x && i3 >= button5.y && i2 < button5.x + button5.w && i3 < button5.y + button5.h) {
                screen6.buttonClicked(button5);
            }

            ++i4;
        }
	}

    public void tick() {
    }

    public void closeScreen() {
    }

    public static int applyEaglerScale(float scaleFac, int coord, int screenDim) {
        return (int) ((coord - (1.0f - scaleFac) * screenDim * 0.5f) / scaleFac);
    }

    public float getEaglerScale() {
        return PointerInputAbstraction.isTouchMode() ? getTouchModeScale() : 1.0f;
    }

    protected float getTouchModeScale() {
        return 1.0f;
    }

    protected boolean isTouchDraggingStateLocked(int uid) {
        return false;
    }

    protected boolean shouldTouchGenerateMouseEvents() {
        return true;
    }
}
