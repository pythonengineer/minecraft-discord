package com.mojang.minecraft.gui;

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

    protected int touchModeCursorPosX = -1;
    protected int touchModeCursorPosY = -1;
    private long lastTouchEvent;

	public void render(int xMouse, int yMouse) {
	}

	public void init(Minecraft minecraft, int width, int height) {
		this.minecraft = minecraft;
		this.width = width;
		this.height = height;
		this.init();
	}

	public void init() {
	}

	protected void fill(int x0, int y0, int x1, int y1, int col) {
		float a = (float)(col >> 24 & 255) / 255.0F;
		float r = (float)(col >> 16 & 255) / 255.0F;
		float g = (float)(col >> 8 & 255) / 255.0F;
		float b = (float)(col & 255) / 255.0F;
		Tesselator t = Tesselator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(r, g, b, a);
		t.begin(DefaultVertexFormats.POSITION);
		t.vertex((float)x0, (float)y1, 0.0F);
		t.vertex((float)x1, (float)y1, 0.0F);
		t.vertex((float)x1, (float)y0, 0.0F);
		t.vertex((float)x0, (float)y0, 0.0F);
		t.end();
		GL11.glDisable(GL11.GL_BLEND);
	}

	protected void fillGradient(int x0, int y0, int x1, int y1, int col1, int col2) {
		float a1 = (float)(col1 >> 24 & 255) / 255.0F;
		float r1 = (float)(col1 >> 16 & 255) / 255.0F;
		float g1 = (float)(col1 >> 8 & 255) / 255.0F;
		float b1 = (float)(col1 & 255) / 255.0F;
		float a2 = (float)(col2 >> 24 & 255) / 255.0F;
		float r2 = (float)(col2 >> 16 & 255) / 255.0F;
		float g2 = (float)(col2 >> 8 & 255) / 255.0F;
		float b2 = (float)(col2 & 255) / 255.0F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		GL11.glColor4f(r1, g1, b1, a1);
		GL11.glVertex2f((float)x1, (float)y0);
		GL11.glVertex2f((float)x0, (float)y0);
		GL11.glColor4f(r2, g2, b2, a2);
		GL11.glVertex2f((float)x0, (float)y1);
		GL11.glVertex2f((float)x1, (float)y1);
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

	protected void keyPressed(char eventCharacter, int eventKey) {
	}

	protected void mouseClicked(int x, int y, int button) {
	}

	public void tick() {
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
