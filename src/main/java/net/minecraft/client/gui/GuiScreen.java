package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.internal.EnumTouchEvent;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;
import net.lax1dude.eaglercraft.lwjgl.input.Mouse;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.touch.TouchControls;
import net.minecraft.client.Minecraft;

public class GuiScreen extends Gui {
	protected Minecraft mc;
	protected int width;
	protected int height;
	protected List controlList = new ArrayList();
	public boolean allowUserInput = false;
	protected FontRenderer fontRenderer;
    protected int touchModeCursorPosX = -1;
    protected int touchModeCursorPosY = -1;
    private long lastTouchEvent;

	public void drawScreen(int var1, int var2) {
		for(int var3 = 0; var3 < this.controlList.size(); ++var3) {
			GuiButton var10000 = (GuiButton)this.controlList.get(var3);
            var10000.drawButton(this.mc, var1, var2);
		}

	}

	protected void keyTyped(char var1, int var2) {
		if(var2 == 1) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		}

	}

	protected void mouseClicked(int var1, int var2, int var3) {
		if(var3 == 0) {
			for(var3 = 0; var3 < this.controlList.size(); ++var3) {
				GuiButton var4 = (GuiButton)this.controlList.get(var3);
                if(var4.mousePressed(var1, var2)) {
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					this.actionPerformed(var4);
				}
			}
		}

	}

	protected void actionPerformed(GuiButton var1) {
	}

	public final void setWorldAndResolution(Minecraft var1, int var2, int var3) {
		this.mc = var1;
		this.fontRenderer = var1.fontRenderer;
		this.width = var2;
		this.height = var3;
		this.controlList.clear();
		this.initGui();
	}

	public void initGui() {
	}

    public final void handleInput() {
        boolean noTouch = true;

        while(Touch.next()) {
            noTouch = false;
            this.touchEvent();
        }

        while(noTouch && Mouse.next()) {
            this.handleMouseInput();
        }

        while(Keyboard.next()) {
            this.handleKeyboardInput();
        }

    }

	public final void handleMouseInput() {
		if(Mouse.getEventButtonState()) {
			int var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
			int var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			this.mouseClicked(var1, var2, Mouse.getEventButton());
		}

	}

	public final void handleKeyboardInput() {
		if(Keyboard.getEventKeyState()) {
            if(Keyboard.getEventKey() == Keyboard.KEY_F11) {
                this.mc.toggleFullScreen();
                return;
            }

			this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}

	}

	public void updateScreen() {
	}

    public void onGuiClosed() {
	}

    public void touchEvent() {
        this.handleTouchInput();
        TouchControls.handleInput();
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
            i = applyEaglerScale(scaleFac, i * this.width / this.mc.displayWidth, this.width);
            j = applyEaglerScale(scaleFac, this.height - j * this.height / this.mc.displayHeight - 1, this.height);
            float rad = Touch.getEventTouchRadiusMixed(t);
            float si = rad * this.width / this.mc.displayWidth / scaleFac;
            if (si < 1.0f)
                si = 1.0f;
            float sj = rad * this.height / this.mc.displayHeight / scaleFac;
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
