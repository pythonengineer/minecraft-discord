package net.lax1dude.eaglercraft.touch;

import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.opengl.GameOverlayFramebuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

import net.lax1dude.eaglercraft.ScaledResolution;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;

import static net.lax1dude.eaglercraft.lwjgl.opengl.GL11.*;

import java.util.Set;

import com.google.common.collect.Sets;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.ChatScreen;
import com.mojang.minecraft.renderer.Tesselator;

/**
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class TouchOverlayRenderer {

    public static final int spriteSheet = Minecraft.minecraft.textures.loadTexture("/eagler/gui/touch_gui.png", GL_NEAREST);

    static final int[] _fuck = new int[2];

    private GameOverlayFramebuffer overlayFramebuffer;
    private boolean invalid = false;
    private boolean invalidDeep = false;
    private int currentWidth = -1;
    private int currentHeight = -1;

    public TouchOverlayRenderer() {
        this.overlayFramebuffer = new GameOverlayFramebuffer(false);
        EnumTouchControl.currentLayout = null;
        EnumTouchControl.setLayoutState(this, EnumTouchLayoutState.IN_GUI);
    }

    public void invalidate() {
        invalid = true;
    }

    public void invalidateDeep() {
        invalid = true;
        invalidDeep = true;
    }

    public void render(int w, int h, ScaledResolution scaledResolution) {
        if (PointerInputAbstraction.isTouchMode()) {
            render0(w, h, scaledResolution);
            if (EnumTouchControl.KEYBOARD.visible) {
                int[] pos = EnumTouchControl.KEYBOARD.getLocation(scaledResolution, _fuck);
                int scale = scaledResolution.getScaleFactor();
                int size = EnumTouchControl.KEYBOARD.size * scale;
                Touch.touchSetOpenKeyboardZone(pos[0] * scale,
                        (scaledResolution.getScaledHeight() - pos[1] - 1) * scale - size, size, size);
            } else {
                Touch.touchSetOpenKeyboardZone(0, 0, 0, 0);
            }
        } else {
            Touch.touchSetOpenKeyboardZone(0, 0, 0, 0);
        }
    }

    private void render0(int w, int h, ScaledResolution scaledResolution) {
        EnumTouchControl.setLayoutState(this, hashLayoutState());
        int sw = scaledResolution.getScaledWidth();
        int sh = scaledResolution.getScaledHeight();
        if (currentWidth != sw || currentHeight != sh) {
            invalidateDeep();
        }
        GL11.disableDepth();
        GL11.disableBlend();
        GL11.enableAlpha();
        GL11.glDepthMask(false);
        if (invalid) {
            GL11.glPushMatrix();
            invalidDeep |= overlayFramebuffer.beginRender(sw, sh);
            GL11.glViewport(0, 0, sw, sh);
            if (invalidDeep) {
                currentWidth = sw;
                currentHeight = sh;
                GL11.glClear(GL_COLOR_BUFFER_BIT);
            }
            Set<EnumTouchControl> controls = Sets.newHashSet(EnumTouchControl._VALUES);
            for (TouchControlInput input : TouchControls.touchControls.values()) {
                controls.remove(input.control);
            }
            for (EnumTouchControl control : controls) {
                if (invalidDeep || control.invalid) {
                    if (control.visible) {
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                        control.getRender().call(control, 0, 0, false, scaledResolution);
                    }
                    control.invalid = false;
                }
            }
            for (TouchControlInput input : TouchControls.touchControls.values()) {
                EnumTouchControl control = input.control;
                if (invalidDeep || control.invalid) {
                    if (control.visible) {
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                        control.getRender().call(control, input.x, input.y, true, scaledResolution);
                    }
                    control.invalid = false;
                }
            }
            overlayFramebuffer.endRender();
            invalid = false;
            invalidDeep = false;
            GL11.glPopMatrix();
            GL11.glViewport(0, 0, w, h);
        }
        GL11.glBindTexture(overlayFramebuffer.getTexture());
        GL11.enableBlend();
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GL11.enableAlpha();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, MathHelper.clamp_float(1.0F, 0.0f, 1.0f));
        Tesselator tessellator = Tesselator.instance;
        tessellator.begin(DefaultVertexFormats.POSITION_TEX);
        tessellator.tex(0.0F, 0.0F);
        tessellator.vertex(0.0F, sh, 500.0F);
        tessellator.tex(1.0F, 0.0F);
        tessellator.vertex(sw, sh, 500.0F);
        tessellator.tex(1.0F, 1.0F);
        tessellator.vertex(sw, 0.0F, 500.0F);
        tessellator.tex(0.0F, 1.0F);
        tessellator.vertex(0.0F, 0.0F, 500.0F);
        tessellator.end();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.enableDepth();
        GL11.glDepthMask(true);
    }

    private EnumTouchLayoutState hashLayoutState() {
        if (Minecraft.minecraft.screen != null) {
            return (Minecraft.minecraft.screen instanceof ChatScreen) ? EnumTouchLayoutState.IN_GUI_TYPING
                    : EnumTouchLayoutState.IN_GUI;
        }
        return showDiagButtons() ? EnumTouchLayoutState.IN_GAME_WALK : EnumTouchLayoutState.IN_GAME;
    }

    private boolean showDiagButtons() {
        return TouchControls.isPressed(EnumTouchControl.DPAD_UP)
                || TouchControls.isPressed(EnumTouchControl.DPAD_UP_LEFT)
                || TouchControls.isPressed(EnumTouchControl.DPAD_UP_RIGHT);
    }

    protected static void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV,
            int scaleFac) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tesselator tessellator = Tesselator.instance;
        tessellator.begin(DefaultVertexFormats.POSITION_TEX);
        tessellator.tex((float)(minU + 0) * f, (float)(minV + maxV) * f1);
        tessellator.vertex(xCoord + 0.0F, yCoord + (float)maxV * scaleFac, 0.0F);
        tessellator.tex((float)(minU + maxU) * f, (float)(minV + maxV) * f1);
        tessellator.vertex(xCoord + (float)maxU * scaleFac, yCoord + (float)maxV * scaleFac, 0.0F);
        tessellator.tex((float)(minU + maxU) * f, (float)(minV + 0) * f1);
        tessellator.vertex(xCoord + (float)maxU * scaleFac, yCoord + 0.0F, 0.0F);
        tessellator.tex((float)(minU + 0) * f, (float)(minV + 0) * f1);
        tessellator.vertex(xCoord + 0.0F, yCoord + 0.0F, 0.0F);
        tessellator.end();
    }
}
