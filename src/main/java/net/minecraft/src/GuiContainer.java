package net.minecraft.src;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;

public abstract class GuiContainer extends GuiScreen {
    private static RenderItem itemRenderer = new RenderItem();
    protected int xSize = 176;
    protected int ySize = 166;
    public Container inventorySlots;
    protected int guiLeft;
    protected int guiTop;
    private Slot clickedSlot;
    private boolean isRightMouseClick;
    private ItemStack draggedStack;
    private int touchUpX;
    private int touchUpY;
    private Slot returningStackDestSlot;
    private long returningStackTime;
    private ItemStack returningStack;
    private Slot currentDragTargetSlot;
    private long dragItemDropDelay;
    protected final Set<Slot> dragSplittingSlots = Sets.newHashSet();
    protected boolean dragSplitting;
    private int dragSplittingLimit;
    private int dragSplittingButton;
    private boolean ignoreMouseUp;
    private int dragSplittingRemnant;
    private long lastClickTime;
    private Slot lastClickSlot;
    private int lastClickButton;
    private boolean doubleClick;
    private ItemStack shiftClickedSlot;

    public GuiContainer(Container var1) {
        this.inventorySlots = var1;
    }

    public void initGui() {
        super.initGui();
        this.mc.thePlayer.craftingInventory = this.inventorySlots;
        if (primaryTouchPoint != -1 && Touch.fetchPointIdx(primaryTouchPoint) == -1) {
            primaryTouchPoint = -1;
            mouseMovedOrUp(lastTouchX, lastTouchY, 0);
        }
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    public void drawScreen(int var1, int var2, float var3) {
        this.drawDefaultBackground();
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawGuiContainerBackgroundLayer(var3);
        GL11.glPushMatrix();
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var4, (float)var5, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_RESCALE_NORMAL);
        Slot slot = null;

        for(int i5 = 0; i5 < this.inventorySlots.slots.size(); ++i5) {
            Slot slot6 = (Slot)this.inventorySlots.slots.get(i5);
            int i11 = slot6.yDisplayPosition;
            int i10 = slot6.xDisplayPosition;
            ItemStack itemstack = slot6.getStack();
            boolean flag = false;
            boolean flag1 = slot6 == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick;
            ItemStack itemstack1 = this.mc.thePlayer.inventory.getItemStack();
            String s = null;
            if (slot6 == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && itemstack != null) {
                itemstack = itemstack.copy();
                itemstack.stackSize /= 2;
            } else if (this.dragSplitting && this.dragSplittingSlots.contains(slot6) && itemstack1 != null) {
                if (this.dragSplittingSlots.size() != 1) {
                    if (Container.canAddItemToSlot(slot6, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slot6)) {
                        itemstack = itemstack1.copy();
                        flag = true;
                        Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack,
                                slot6.getStack() == null ? 0 : slot6.getStack().stackSize);
                        if (itemstack.stackSize > itemstack.getItem().getItemStackLimit()) {
                            s = "" + itemstack.getItem().getItemStackLimit();
                            itemstack.stackSize = itemstack.getItem().getItemStackLimit();
                        }

                        if (itemstack.stackSize > slot6.inventory.getInventoryStackLimit()) {
                            s = "" + slot6.inventory.getInventoryStackLimit();
                            itemstack.stackSize = slot6.inventory.getInventoryStackLimit();
                        }
                    } else {
                        this.dragSplittingSlots.remove(slot6);
                        this.updateDragSplitting();
                    }
                } else {
                    flag1 = true;
                }
            }

            if (itemstack == null) {
                int var8 = slot6.getBackgroundIconIndex();
                if(var8 >= 0) {
                    GL11.glDisable(GL11.GL_LIGHTING);
                    this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
                    this.drawTexturedModalRect(i10, i11, var8 % 16 * 16, var8 / 16 * 16, 16, 16);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    flag1 = true;
                }
            }

            if (!flag1) {
                if (flag) {
                    drawRect(i10, i11, i10 + 16, i11 + 16, -2130706433);
                }

                itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, itemstack, i10, i11);
                itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, itemstack, i10, i11, s);
            }

            if(!this.mc.gameSettings.touchscreen && this.getIsMouseOverSlot(slot6, var1, var2)) {
                slot = slot6;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int i7 = slot6.xDisplayPosition;
                int i12 = slot6.yDisplayPosition;
                drawGradientRect(i7, i12, i7 + 16, i12 + 16, -2130706433, -2130706433);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }

        ItemStack itemstack = this.draggedStack == null ? this.mc.thePlayer.inventory.getItemStack() : this.draggedStack;
        if(itemstack != null) {
            byte b0 = 8;
            int j2 = this.draggedStack == null ? 8 : 16;
            String s = null;
            if (this.draggedStack != null && this.isRightMouseClick) {
                itemstack = itemstack.copy();
                itemstack.stackSize = MathHelper.ceiling_float_int((float) itemstack.stackSize / 2.0F);
            } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                itemstack = itemstack.copy();
                itemstack.stackSize = this.dragSplittingRemnant;
                if (itemstack.stackSize == 0) {
                    s = "0";
                }
            }

            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, itemstack, var1 - this.guiLeft - b0, var2 - this.guiTop - j2);
            itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, itemstack, var1 - this.guiLeft - b0, var2 - this.guiTop - j2, s);
        }

        if (this.returningStack != null) {
            float f1 = (float) (EagRuntime.currentTimeMillis() - this.returningStackTime) / 100.0F;
            if (f1 >= 1.0F) {
                f1 = 1.0F;
                this.returningStack = null;
            }

            int k2 = this.returningStackDestSlot.xDisplayPosition - this.touchUpX;
            int l2 = this.returningStackDestSlot.yDisplayPosition - this.touchUpY;
            int l1 = this.touchUpX + (int) ((float) k2 * f1);
            int i2 = this.touchUpY + (int) ((float) l2 * f1);
            itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, this.returningStack, l1, i2);
            itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, this.returningStack, l1, i2);
        }

        GL11.glDisable(GL11.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        this.drawGuiContainerForegroundLayer();
        if(this.mc.thePlayer.inventory.getItemStack() == null && slot != null && slot.getHasStack()) {
            String var13 = ("" + StringTranslate.getInstance().translateNamedKey(slot.getStack().func_20109_f())).trim();
            if(var13.length() > 0) {
                int var9 = var1 - var4 + 12;
                int var10 = var2 - var5 - 12;
                int var11 = this.fontRenderer.getStringWidth(var13);
                this.drawGradientRect(var9 - 3, var10 - 3, var9 + var11 + 3, var10 + 8 + 3, -1073741824, -1073741824);
                this.fontRenderer.drawStringWithShadow(var13, var9, var10, -1);
            }
        }

        GL11.glPopMatrix();
        super.drawScreen(var1, var2, var3);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    protected void drawGuiContainerForegroundLayer() {
    }

    protected abstract void drawGuiContainerBackgroundLayer(float var1);

    protected void keyTyped(char var1, int var2) {
        if(var2 == 1 || var2 == this.mc.gameSettings.keyBindInventory.keyCode) {
            this.mc.thePlayer.func_20059_m();
        }

    }

    public void onGuiClosed() {
        if(this.mc.thePlayer != null) {
            this.mc.playerController.func_20086_a(this.inventorySlots.windowId, this.mc.thePlayer);
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType) {
        int mode = clickType;
        if (slotIn != null) {
            slotId = this.inventorySlots.slots.indexOf(slotIn);
        }

        if (this.isMultiplayer() && mode != 0) {
            List<int[]> basicClicks = this.inventorySlots.translateToBasicClicks(slotId, clickedButton, mode, this.mc.thePlayer);
            if (basicClicks != null) {
                for (int n = 0; n < basicClicks.size(); ++n) {
                    int[] click = basicClicks.get(n);
                    if (click[0] != -1) {
                        this.mc.playerController.func_27174_a(this.inventorySlots.windowId, click[0], click[1], 0, this.mc.thePlayer);
                    }
                }

                return;
            }
        }

        if (slotId != -1) {
            this.mc.playerController.func_27174_a(this.inventorySlots.windowId, slotId, clickedButton, mode, this.mc.thePlayer);
        }
    }

    private boolean isMultiplayer() {
        return this.mc.playerController instanceof PlayerControllerMP;
    }

    protected void mouseClickMove(int i, int j, int k, long var4) {
        Slot slot = this.getSlotAtPosition(i, j);
        if (this.clickedSlot != null) {
            if (k == 0 || k == 1) {
                if (this.draggedStack == null) {
                    if (slot != this.clickedSlot && this.clickedSlot.getStack() != null) {
                        this.draggedStack = this.clickedSlot.getStack().copy();
                    }
                } else if (this.draggedStack.stackSize > 1 && slot != null
                        && Container.canAddItemToSlot(slot, this.draggedStack, false)) {
                    long l = EagRuntime.currentTimeMillis();
                    if (this.currentDragTargetSlot == slot) {
                        if (l - this.dragItemDropDelay > 500L) {
                            this.handleMouseClick(this.clickedSlot, this.inventorySlots.slots.indexOf(this.clickedSlot), 0, 0);
                            this.handleMouseClick(slot, this.inventorySlots.slots.indexOf(slot), 1, 0);
                            this.handleMouseClick(this.clickedSlot, this.inventorySlots.slots.indexOf(this.clickedSlot), 0, 0);
                            this.dragItemDropDelay = l + 750L;
                            --this.draggedStack.stackSize;
                        }
                    } else {
                        this.currentDragTargetSlot = slot;
                        this.dragItemDropDelay = l;
                    }
                }
            }
        } else if (this.dragSplitting && slot != null && this.mc.thePlayer.inventory.getItemStack() != null
                && this.mc.thePlayer.inventory.getItemStack().stackSize > this.dragSplittingSlots.size()
                && Container.canAddItemToSlot(slot, this.mc.thePlayer.inventory.getItemStack(), true) && slot.isItemValid(this.mc.thePlayer.inventory.getItemStack())
                && this.inventorySlots.canDragIntoSlot(slot)) {
            this.dragSplittingSlots.add(slot);
            this.updateDragSplitting();
        }

    }

    private void updateDragSplitting() {
        if (this.mc.thePlayer.inventory.getItemStack() != null && this.dragSplitting) {
            this.dragSplittingRemnant = this.mc.thePlayer.inventory.getItemStack().stackSize;

            for (Slot slot : this.dragSplittingSlots) {
                ItemStack itemstack1 = this.mc.thePlayer.inventory.getItemStack().copy();
                int i = slot.getStack() == null ? 0 : slot.getStack().stackSize;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
                if (itemstack1.stackSize > itemstack1.getItem().getItemStackLimit()) {
                    itemstack1.stackSize = itemstack1.getItem().getItemStackLimit();
                }

                if (itemstack1.stackSize > slot.inventory.getInventoryStackLimit()) {
                    itemstack1.stackSize = slot.inventory.getInventoryStackLimit();
                }

                this.dragSplittingRemnant -= itemstack1.stackSize - i;
            }

        }
    }

    protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
        super.mouseClicked(parInt1, parInt2, parInt3);
        boolean flag = parInt3 == 2;
        Slot slot = this.getSlotAtPosition(parInt1, parInt2);
        long i = EagRuntime.currentTimeMillis();
        this.doubleClick = this.lastClickSlot == slot && i - this.lastClickTime < 250L
                && this.lastClickButton == parInt3;
        this.ignoreMouseUp = false;
        if (parInt3 == 0 || parInt3 == 1 || flag) {
            int j = this.guiLeft;
            int k = this.guiTop;
            boolean flag1 = parInt1 < j || parInt2 < k || parInt1 >= j + this.xSize || parInt2 >= k + this.ySize;
            int l = -1;
            if (slot != null) {
                l = this.inventorySlots.slots.indexOf(slot);
            }

            if (flag1) {
                l = -999;
            }

            if (l != -1) {
                if (this.mc.gameSettings.touchscreen) {
                    if (slot != null && slot.getHasStack()) {
                        this.clickedSlot = slot;
                        this.draggedStack = null;
                        this.isRightMouseClick = parInt3 == 1;
                    } else {
                        this.clickedSlot = null;
                    }
                } else if (!this.dragSplitting) {
                    if (this.mc.thePlayer.inventory.getItemStack() == null) {
                        if (parInt3 == 2) {
                            this.handleMouseClick(slot, l, parInt3, 3);
                        } else {
                            boolean flag2 = l != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                            byte b0 = 0;
                            if (flag2) {
                                this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack() : null;
                                b0 = 1;
                            } else if (l == -999) {
                                b0 = 4;
                            }

                            this.handleMouseClick(slot, l, parInt3, b0);
                        }

                        this.ignoreMouseUp = true;
                    } else {
                        this.dragSplitting = true;
                        this.dragSplittingButton = parInt3;
                        this.dragSplittingSlots.clear();
                        if (parInt3 == 0) {
                            this.dragSplittingLimit = 0;
                        } else if (parInt3 == 1) {
                            this.dragSplittingLimit = 1;
                        } else if (parInt3 == 2) {
                            this.dragSplittingLimit = 2;
                        }
                    }
                }
            }
        }

        this.lastClickSlot = slot;
        this.lastClickTime = i;
        this.lastClickButton = parInt3;
    }

    protected void mouseMovedOrUp(int i, int j, int k) {
        Slot slot = this.getSlotAtPosition(i, j);
        int l = this.guiLeft;
        int i1 = this.guiTop;
        boolean flag = i < l || j < i1 || i >= l + this.xSize || j >= i1 + this.ySize;
        int j1 = -1;
        if (slot != null) {
            j1 = this.inventorySlots.slots.indexOf(slot);
        }

        if (flag) {
            j1 = -999;
        }

        if (this.doubleClick && slot != null && k == 0 && this.inventorySlots.canMergeSlot((ItemStack) null, slot)) {
            if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
                if (slot != null && slot.inventory != null && this.shiftClickedSlot != null) {
                    List<Slot> lst = this.inventorySlots.slots;
                    for (int n = 0, m = lst.size(); n < m; ++n) {
                        Slot slot2 = lst.get(n);
                        if (slot2 != null && slot2.getHasStack()
                                && slot2.inventory == slot.inventory
                                && Container.canAddItemToSlot(slot2, this.shiftClickedSlot, true)) {
                            this.handleMouseClick(slot2, this.inventorySlots.slots.indexOf(slot2), k, 1);
                        }
                    }
                }
            } else {
                this.handleMouseClick(slot, j1, k, 6);
            }

            this.doubleClick = false;
            this.lastClickTime = 0L;
        } else {
            if (this.dragSplitting && this.dragSplittingButton != k) {
                this.dragSplitting = false;
                this.dragSplittingSlots.clear();
                this.ignoreMouseUp = true;
                return;
            }

            if (this.ignoreMouseUp) {
                this.ignoreMouseUp = false;
                return;
            }

            if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
                if (k == 0 || k == 1) {
                    if (this.draggedStack == null && slot != this.clickedSlot) {
                        this.draggedStack = this.clickedSlot.getStack();
                    }

                    boolean flag2 = Container.canAddItemToSlot(slot, this.draggedStack, false);
                    if (j1 != -1 && this.draggedStack != null && flag2) {
                        this.handleMouseClick(this.clickedSlot, this.inventorySlots.slots.indexOf(this.clickedSlot), k, 0);
                        this.handleMouseClick(slot, j1, 0, 0);
                        if (this.mc.thePlayer.inventory.getItemStack() != null) {
                            this.handleMouseClick(this.clickedSlot, this.inventorySlots.slots.indexOf(this.clickedSlot), k, 0);
                            this.touchUpX = i - l;
                            this.touchUpY = j - i1;
                            this.returningStackDestSlot = this.clickedSlot;
                            this.returningStack = this.draggedStack;
                            this.returningStackTime = EagRuntime.currentTimeMillis();
                        } else {
                            this.returningStack = null;
                        }
                    } else if (this.draggedStack != null) {
                        this.touchUpX = i - l;
                        this.touchUpY = j - i1;
                        this.returningStackDestSlot = this.clickedSlot;
                        this.returningStack = this.draggedStack;
                        this.returningStackTime = EagRuntime.currentTimeMillis();
                    }

                    this.draggedStack = null;
                    this.clickedSlot = null;
                }
            } else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty()) {
                if (this.isMultiplayer()) {
                    this.dispatchDragSplittingAsBasicClicks();
                } else {
                    this.handleMouseClick((Slot) null, -999, func_94534_d(0, this.dragSplittingLimit), 5);

                    for (Slot slot1 : this.dragSplittingSlots) {
                        this.handleMouseClick(slot1, this.inventorySlots.slots.indexOf(slot1), func_94534_d(1, this.dragSplittingLimit),
                                5);
                    }

                    this.handleMouseClick((Slot) null, -999, func_94534_d(2, this.dragSplittingLimit), 5);
                }
            } else if (this.mc.thePlayer.inventory.getItemStack() != null) {
                if (k == 2) {
                    this.handleMouseClick(slot, j1, k, 3);
                } else {
                    boolean flag1 = j1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                    if (flag1) {
                        this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack() : null;
                    }

                    this.handleMouseClick(slot, j1, k, flag1 ? 1 : 0);
                }
            }
        }

        if (this.mc.thePlayer.inventory.getItemStack() == null) {
            this.lastClickTime = 0L;
        }

        this.dragSplitting = false;
    }

    private Slot getSlotAtPosition(int x, int y) {
        for(int i3 = 0; i3 < this.inventorySlots.slots.size(); ++i3) {
            Slot slot4 = (Slot)this.inventorySlots.slots.get(i3);
            if(this.getIsMouseOverSlot(slot4, x, y)) {
                return slot4;
            }
        }

        return null;
    }

    private boolean getIsMouseOverSlot(Slot var1, int var2, int var3) {
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        var2 -= var4;
        var3 -= var5;
        return var2 >= var1.xDisplayPosition - 1 && var2 < var1.xDisplayPosition + 16 + 1 && var3 >= var1.yDisplayPosition - 1 && var3 < var1.yDisplayPosition + 16 + 1;
    }

    public static int func_94534_d(int parInt1, int parInt2) {
        return parInt1 & 3 | (parInt2 & 3) << 2;
    }

    private void dispatchDragSplittingAsBasicClicks() {
        ItemStack held = this.mc.thePlayer.inventory.getItemStack();
        if (held == null) {
            return;
        }

        for (Slot slot : this.dragSplittingSlots) {
            ItemStack heldNow = this.mc.thePlayer.inventory.getItemStack();
            if (heldNow == null) {
                break;
            }

            if (!Container.canAddItemToSlot(slot, heldNow, true) || !slot.isItemValid(heldNow)
                    || !this.inventorySlots.canDragIntoSlot(slot)) {
                continue;
            }

            int existing = slot.getHasStack() ? slot.getStack().stackSize : 0;
            ItemStack share = heldNow.copy();
            Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, share, existing);
            int target = Math.min(Math.min(share.stackSize, slot.getSlotStackLimit()), heldNow.getMaxStackSize());
            int deposit = target - existing;
            int idx = this.inventorySlots.slots.indexOf(slot);

            for (int n = 0; n < deposit; ++n) {
                this.handleMouseClick(slot, idx, 1, 0);
            }
        }
    }

    public void updateScreen() {
        super.updateScreen();
        if (primaryTouchPoint != -1 && Touch.fetchPointIdx(primaryTouchPoint) == -1) {
            primaryTouchPoint = -1;
            mouseMovedOrUp(lastTouchX, lastTouchY, 0);
        }
    }

    protected float getTouchModeScale() {
        return 1.25f;
    }

    private int primaryTouchPoint = -1;
    private int lastTouchX = -1;
    private int lastTouchY = -1;

    protected void touchStarted(int touchX, int touchY, int uid) {
        if (primaryTouchPoint == -1) {
            primaryTouchPoint = uid;
            lastTouchX = touchX;
            lastTouchY = touchY;
            mouseClicked(touchX, touchY, 0);
        }
    }

    protected void touchMoved(int touchX, int touchY, int uid) {
        if (primaryTouchPoint == uid) {
            lastTouchX = touchX;
            lastTouchY = touchY;
            mouseClickMove(touchX, touchY, 0, 0l);
        }
    }

    protected void touchEndMove(int touchX, int touchY, int uid) {
        if (primaryTouchPoint == uid) {
            primaryTouchPoint = -1;
            lastTouchX = touchX;
            lastTouchY = touchY;
            mouseMovedOrUp(touchX, touchY, 0);
        }
    }

    protected void touchTapped(int touchX, int touchY, int uid) {
        if (primaryTouchPoint == uid) {
            primaryTouchPoint = -1;
            lastTouchX = touchX;
            lastTouchY = touchY;
            mouseMovedOrUp(touchX, touchY, 0);
        }
    }

    protected boolean shouldTouchGenerateMouseEvents() {
        return false;
    }
}