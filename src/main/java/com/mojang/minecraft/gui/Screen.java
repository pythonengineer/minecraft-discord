package com.mojang.minecraft.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.mojang.minecraft.Minecraft;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.internal.EnumTouchEvent;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;
import net.lax1dude.eaglercraft.lwjgl.input.Mouse;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.touch.TouchControls;

public class Screen extends Gui {
	protected Minecraft minecraft;
    protected int width;
    protected int height;
    protected List buttons = new ArrayList();
    public boolean allowUserInput = false;
    protected Font font;
    protected int touchModeCursorPosX = -1;
    protected int touchModeCursorPosY = -1;
    private long lastTouchEvent;

    public void render(int i1, int i2) {
        for(int i3 = 0; i3 < this.buttons.size(); ++i3) {
            Button button10000 = (Button)this.buttons.get(i3);
            Minecraft minecraft5 = this.minecraft;
            Button button4 = button10000;
            if(button10000.visible) {
                Font font8 = minecraft5.font;
                GL11.glEnable(3553);
                GL11.glBindTexture(3553, minecraft5.textures.getTextureId("/gui.png"));
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                byte b9 = 1;
                boolean z6 = i1 >= button4.w && i2 >= button4.h && i1 < button4.w + button4.x && i2 < button4.h + button4.y;
                if(!button4.enabled) {
                    b9 = 0;
                } else if(z6) {
                    b9 = 2;
                }

                button4.blit(button4.w, button4.h, 0, 46 + b9 * 20, button4.x / 2, button4.y);
                button4.blit(button4.w + button4.x / 2, button4.h, 200 - button4.x / 2, 46 + b9 * 20, button4.x / 2, button4.y);
                if(!button4.enabled) {
                    Button.drawCenteredString(font8, button4.msg, button4.w + button4.x / 2, button4.h + (button4.y - 8) / 2, -6250336);
                } else if(z6) {
                    Button.drawCenteredString(font8, button4.msg, button4.w + button4.x / 2, button4.h + (button4.y - 8) / 2, 16777120);
                } else {
                    Button.drawCenteredString(font8, button4.msg, button4.w + button4.x / 2, button4.h + (button4.y - 8) / 2, 14737632);
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

    protected void mousePressed(int i1, int i2, int i3) {
        if(i3 == 0) {
            for(i3 = 0; i3 < this.buttons.size(); ++i3) {
                Button button4;
                Button button5;
                if((button5 = button4 = (Button)this.buttons.get(i3)).enabled && i1 >= button5.w && i2 >= button5.h && i1 < button5.w + button5.x && i2 < button5.h + button5.y) {
                    this.buttonClicked(button4);
                }
            }
        }

    }

    protected void buttonClicked(Button button1) {
    }

    public final void init(Minecraft minecraft1, int i2, int i3) {
        this.minecraft = minecraft1;
        this.font = minecraft1.font;
        this.width = i2;
        this.height = i3;
        this.init();
    }

	public void init() {
	}

    public final void updateEvents() {
        boolean noTouch = true;

        while(Touch.next()) {
            noTouch = false;
            this.updateTouchEvents();
        }

        while(noTouch && Mouse.next()) {
            this.updateMouseEvents();
        }

        while(Keyboard.next()) {
            this.updateKeyboardEvents();
        }

    }

    public void updateTouchEvents() {
        this.handleTouchInput();
        TouchControls.handleInput();
    }

	public void updateMouseEvents() {
		if(Mouse.getEventButtonState()) {
            int xm = Mouse.getEventX() * this.width / this.minecraft.width;
            int ym = this.height - Mouse.getEventY() * this.height / this.minecraft.height - 1;
			this.mousePressed(xm, ym, Mouse.getEventButton());
		}
	}

	public void updateKeyboardEvents() {
		if(Keyboard.getEventKeyState()) {
			this.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}

	}

    protected void touchStarted(int parInt1, int parInt2, int parInt3) {
        if (shouldTouchGenerateMouseEvents()) {
            this.mousePressed(parInt1, parInt2, 12345);
        }
    }

    protected void touchTapped(int parInt1, int parInt2, int parInt3) {
        if (shouldTouchGenerateMouseEvents()) {
            this.mousePressed(parInt1, parInt2, 0);
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
