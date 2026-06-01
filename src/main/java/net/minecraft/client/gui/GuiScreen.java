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
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.touch.TouchControls;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;

public class GuiScreen extends Gui {
	protected Minecraft mc;
	public int width;
	public int height;
	protected List controlList = new ArrayList();
	public boolean allowUserInput = false;
	protected FontRenderer fontRenderer;
    private int eventButton;
    private long lastMouseEvent;
    private int touchValue;
    protected int touchModeCursorPosX = -1;
    protected int touchModeCursorPosY = -1;
    private long lastTouchEvent;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for(int i5 = 0; i5 < this.controlList.size(); ++i5) {
            ((GuiButton)this.controlList.get(i5)).drawButton(this.mc, mouseX, mouseY);
        }

    }

    protected void keyTyped(char typedChar, int keyCode) {
        if(keyCode == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }

    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0) {
            for(mouseButton = 0; mouseButton < this.controlList.size(); ++mouseButton) {
                GuiButton guiButton4;
                if((guiButton4 = (GuiButton)this.controlList.get(mouseButton)).mousePressed(mouseX, mouseY)) {
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                    this.actionPerformed(guiButton4);
                }
            }
        }

    }

    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    protected void mouseClickMove(int mouseX, int mouseY, int mouseButton, long var4) {
    }

    protected void actionPerformed(GuiButton button) {
	}

    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        this.mc = mc;
        this.fontRenderer = mc.fontRenderer;
        this.width = width;
        this.height = height;
		this.controlList.clear();
		this.initGui();
	}

	public void initGui() {
	}

    public void handleInput() {
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

	public void handleMouseInput() {
        float f = getEaglerScale();
        int i = applyEaglerScale(f, Mouse.getEventX() * this.width / this.mc.displayWidth, this.width);
        int j = applyEaglerScale(f, this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1,
                this.height);
        int k = Mouse.getEventButton();
        if (Mouse.getEventButtonState()) {
            PointerInputAbstraction.enterMouseModeHook();
            if (this.mc.options.touchscreen && this.touchValue++ > 0) {
                return;
            }

            this.eventButton = k;
            this.lastMouseEvent = EagRuntime.currentTimeMillis();
            this.mouseClicked(i, j, this.eventButton);
        } else if (k != -1) {
            if (this.mc.options.touchscreen && --this.touchValue > 0) {
                return;
            }

            this.eventButton = -1;
            this.mouseReleased(i, j, k);
        } else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
            long l = EagRuntime.currentTimeMillis() - this.lastMouseEvent;
            this.mouseClickMove(i, j, this.eventButton, l);
        }

	}

	public void handleKeyboardInput() {
		if(Keyboard.getEventKeyState()) {
            if(Keyboard.getEventKey() == Keyboard.KEY_F11) {
                this.mc.toggleFullscreen();
                return;
            }

			this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}

	}

	public void updateScreen() {
	}

    public void onGuiClosed() {
	}

    public void drawDefaultBackground() {
        this.drawWorldBackground(0);
    }

    public void drawWorldBackground(int i1) {
        if(this.mc.theWorld != null) {
            this.drawGradientRect(0, 0, this.width, this.height, 1610941696, -1607454656);
        } else {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_FOG);
            Tessellator tessellator2 = Tessellator.instance;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/dirt.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            float f3 = 32.0F;
            tessellator2.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
            tessellator2.setColorOpaque_I(4210752);
            tessellator2.addVertexWithUV(0.0D, (double)this.height, 0.0D, 0.0D, (double)((float)this.height / f3 + (float)i1));
            tessellator2.addVertexWithUV((double)this.width, (double)this.height, 0.0D, (double)((float)this.width / f3), (double)((float)this.height / f3 + (float)i1));
            tessellator2.addVertexWithUV((double)this.width, 0.0D, 0.0D, (double)((float)this.width / f3), (double)(0 + i1));
            tessellator2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double)(0 + i1));
            tessellator2.draw();
        }
    }

    public boolean doesGuiPauseGame() {
        return true;
    }

    public void deleteWorld(boolean shouldDelete, int worldIndex) {
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
            this.mouseReleased(parInt1, parInt2, 0);
        }
    }

    protected void touchMoved(int parInt1, int parInt2, int parInt3) {
    }

    protected void touchEndMove(int parInt1, int parInt2, int parInt3) {
        if (shouldTouchGenerateMouseEvents()) {
            this.mouseReleased(parInt1, parInt2, 12345);
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
                    this.mouseClickMove(i, j, 0, EagRuntime.currentTimeMillis() - lastTouchEvent);
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
