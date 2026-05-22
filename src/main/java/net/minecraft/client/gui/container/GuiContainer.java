package net.minecraft.client.gui.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;

public abstract class GuiContainer extends GuiScreen {
    private static RenderItem itemRenderer = new RenderItem();
    private ItemStack itemStack = null;
    protected int xSize = 176;
    protected int ySize = 166;
    protected List inventorySlots = new ArrayList();
    protected int guiLeft;
    protected int guiTop;
    private int dragMode = -1;
    private int dragEvent;
    private final Set<Slot> dragSlots = Sets.newHashSet();
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

    public void initGui() {
        super.initGui();
        if (primaryTouchPoint != -1 && Touch.fetchPointIdx(primaryTouchPoint) == -1) {
            primaryTouchPoint = -1;
            mouseReleased(lastTouchX, lastTouchY, 0);
        }
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        this.drawGuiContainerBackgroundLayer();
        GL11.glPushMatrix();
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(this.guiLeft, this.guiTop, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_NORMALIZE);

        for(int i5 = 0; i5 < this.inventorySlots.size(); ++i5) {
            Slot slot6 = (Slot)this.inventorySlots.get(i5);
            int i11 = slot6.yDisplayPosition;
            int i10 = slot6.xDisplayPosition;
            ItemStack itemstack = slot6.getStack();
            boolean flag = false;
            boolean flag1 = slot6 == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick;
            ItemStack itemstack1 = this.itemStack;
            String s = null;
            if (slot6 == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && itemstack != null) {
                itemstack = itemstack.copy();
                itemstack.stackSize /= 2;
            } else if (this.dragSplitting && this.dragSplittingSlots.contains(slot6) && itemstack1 != null) {
                if (this.dragSplittingSlots.size() != 1) {
                    if (canAddItemToSlot(slot6, itemstack1, true)) {
                        itemstack = itemstack1.copy();
                        flag = true;
                        computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack,
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
                    RenderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
                    this.drawTexturedModalRect(i10, i11, var8 % 16 << 4, var8 / 16 << 4, 16, 16);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    flag1 = true;
                }
            }

            if (!flag1) {
                if (flag) {
                    drawRect(i10, i11, i10 + 16, i11 + 16, -2130706433);
                }

                itemRenderer.renderItemIntoGUI(this.mc.renderEngine, itemstack, i10, i11);
                itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, i10, i11, s);
            }

            if(!this.mc.gameSettings.touchscreen && slot6.getIsMouseOverSlot(mouseX, mouseY)) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int i7 = slot6.xDisplayPosition;
                int i12 = slot6.yDisplayPosition;
                drawGradientRect(i7, i12, i7 + 16, i12 + 16, -2130706433, -2130706433);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }

        ItemStack itemstack = this.draggedStack == null ? this.itemStack : this.draggedStack;
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
            itemRenderer.renderItemIntoGUI(this.mc.renderEngine, itemstack, mouseX - this.guiLeft - b0, mouseY - this.guiTop - j2);
            itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, mouseX - this.guiLeft - b0, mouseY - this.guiTop - j2, s);
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
            itemRenderer.renderItemIntoGUI(this.mc.renderEngine, this.returningStack, l1, i2);
            itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.returningStack, l1, i2);
        }

        GL11.glDisable(GL11.GL_NORMALIZE);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        this.drawGuiContainerForegroundLayer();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }

    protected void drawGuiContainerForegroundLayer() {
    }

    protected abstract void drawGuiContainerBackgroundLayer();

    protected final void keyTyped(char typedChar, int keyCode) {
        if(keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.keyCode) {
            this.mc.displayGuiScreen((GuiScreen)null);
        }

    }

    public void onGuiClosed() {
        if(this.itemStack != null) {
            this.mc.thePlayer.dropPlayerItem(this.itemStack);
        }

    }

    public void onCraftMatrixChanged() {
    }

    public final boolean doesGuiPauseGame() {
        return false;
    }

    protected void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType) {
        int mode = clickType;
        EntityPlayer playerIn = this.mc.thePlayer;
        if (slotIn != null) {
            slotId = this.inventorySlots.indexOf(slotIn);
        }

        ItemStack itemstack = null;
        InventoryPlayer inventoryplayer = playerIn.inventory;
        if (mode == 5) {
            int i = this.dragEvent;
            this.dragEvent = getDragEvent(clickedButton);
            if ((i != 1 || this.dragEvent != 2) && i != this.dragEvent) {
                this.resetDrag();
            } else if (this.itemStack == null) {
                this.resetDrag();
            } else if (this.dragEvent == 0) {
                this.dragMode = extractDragMode(clickedButton);
                if (this.isValidDragMode(this.dragMode, playerIn)) {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                } else {
                    this.resetDrag();
                }
            } else if (this.dragEvent == 1) {
                Slot slot = (Slot) this.inventorySlots.get(slotId);
                if (slot != null && canAddItemToSlot(slot, this.itemStack, true)
                        && slot.isItemValid(this.itemStack)
                        && this.itemStack.stackSize > this.dragSlots.size()) {
                    this.dragSlots.add(slot);
                }
            } else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    ItemStack itemstack3 = this.itemStack.copy();
                    int j = this.itemStack.stackSize;

                    for (Slot slot1 : this.dragSlots) {
                        if (slot1 != null && canAddItemToSlot(slot1, this.itemStack, true)
                                && slot1.isItemValid(this.itemStack)
                                && this.itemStack.stackSize >= this.dragSlots.size()) {
                            ItemStack itemstack1 = itemstack3.copy();
                            int k = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
                            computeStackSize(this.dragSlots, this.dragMode, itemstack1, k);
                            if (itemstack1.stackSize > itemstack1.getItem().getItemStackLimit()) {
                                itemstack1.stackSize = itemstack1.getItem().getItemStackLimit();
                            }

                            if (itemstack1.stackSize > slot1.inventory.getInventoryStackLimit()) {
                                itemstack1.stackSize = slot1.inventory.getInventoryStackLimit();
                            }

                            j -= itemstack1.stackSize - k;
                            slot1.putStack(itemstack1);
                        }
                    }

                    itemstack3.stackSize = j;
                    if (itemstack3.stackSize <= 0) {
                        itemstack3 = null;
                    }

                    this.itemStack = itemstack3;
                }

                this.resetDrag();
            } else {
                this.resetDrag();
            }
        } else if (this.dragEvent != 0) {
            this.resetDrag();
        } else if ((mode == 0 || mode == 1) && (clickedButton == 0 || clickedButton == 1)) {
            if (slotId == -999) {
                if (this.itemStack != null) {
                    if (clickedButton == 0) {
                        playerIn.dropPlayerItem(this.itemStack);
                        this.itemStack = null;
                    }

                    if (clickedButton == 1) {
                        playerIn.dropPlayerItem(this.itemStack.splitStack(1));
                        if (this.itemStack.stackSize == 0) {
                            this.itemStack = null;
                        }
                    }
                }
            } else if (mode == 1) {
                if (slotId < 0) {
                    return;
                }

                Slot slot6 = (Slot) this.inventorySlots.get(slotId);
                if (slot6 != null) {
                    ItemStack itemstack8 = this.transferStackInSlot(playerIn, slotId);
                    if (itemstack8 != null) {
                        Item item = itemstack8.getItem();
                        itemstack = itemstack8.copy();
                        if (slot6.getStack() != null && slot6.getStack().getItem() == item) {
                            this.handleMouseClick(null, slotId, clickedButton, 1);
                        }
                    }
                }
            } else {
                if (slotId < 0) {
                    return;
                }

                Slot slot7 = (Slot) this.inventorySlots.get(slotId);
                if (slot7 != null) {
                    ItemStack itemstack9 = slot7.getStack();
                    ItemStack itemstack10 = this.itemStack;
                    if (itemstack9 != null) {
                        itemstack = itemstack9.copy();
                    }

                    if (itemstack9 == null) {
                        if (itemstack10 != null && slot7.isItemValid(itemstack10)) {
                            int k2 = clickedButton == 0 ? itemstack10.stackSize : 1;
                            if (k2 > slot7.inventory.getInventoryStackLimit()) {
                                k2 = slot7.inventory.getInventoryStackLimit();
                            }

                            if (itemstack10.stackSize >= k2) {
                                slot7.putStack(itemstack10.splitStack(k2));
                            }

                            if (itemstack10.stackSize == 0) {
                                this.itemStack = null;
                            }
                        }
                    } else {
                        if (itemstack10 == null) {
                            int j2 = clickedButton == 0 ? itemstack9.stackSize : (itemstack9.stackSize + 1) / 2;
                            ItemStack itemstack12 = slot7.decrStackSize(j2);
                            this.itemStack = itemstack12;
                            if (itemstack9.stackSize == 0) {
                                slot7.putStack((ItemStack) null);
                            }

                            slot7.onPickupFromSlot();
                        } else if (slot7.isItemValid(itemstack10)) {
                            if (itemstack9.getItem() == itemstack10.getItem()) {
                                int i2 = clickedButton == 0 ? itemstack10.stackSize : 1;
                                if (i2 > slot7.inventory.getInventoryStackLimit() - itemstack9.stackSize) {
                                    i2 = slot7.inventory.getInventoryStackLimit() - itemstack9.stackSize;
                                }

                                if (i2 > itemstack10.getItem().getItemStackLimit() - itemstack9.stackSize) {
                                    i2 = itemstack10.getItem().getItemStackLimit() - itemstack9.stackSize;
                                }

                                itemstack10.splitStack(i2);
                                if (itemstack10.stackSize == 0) {
                                    this.itemStack = null;
                                }

                                itemstack9.stackSize += i2;
                            } else if (itemstack10.stackSize <= slot7.inventory.getInventoryStackLimit()) {
                                slot7.putStack(itemstack10);
                                this.itemStack = itemstack9;
                            }
                        } else if (itemstack9.getItem() == itemstack10.getItem() && itemstack10.getItem().getItemStackLimit() > 1) {
                            int l1 = itemstack9.stackSize;
                            if (l1 > 0 && l1 + itemstack10.stackSize <= itemstack10.getItem().getItemStackLimit()) {
                                itemstack10.stackSize += l1;
                                itemstack9 = slot7.decrStackSize(l1);
                                if (itemstack9.stackSize == 0) {
                                    slot7.putStack((ItemStack) null);
                                }

                                slot7.onPickupFromSlot();
                            }
                        }
                    }
                    slot7.onSlotChanged();
                }
            }
        } else if (mode == 2 && clickedButton >= 0 && clickedButton < 9) {
            Slot slot5 = (Slot) this.inventorySlots.get(slotId);
            ItemStack itemstack7 = inventoryplayer.getStackInSlot(clickedButton);
            boolean flag = itemstack7 == null
                    || slot5.inventory == inventoryplayer && slot5.isItemValid(itemstack7);
            int k1 = -1;
            if (!flag) {
                k1 = inventoryplayer.getFirstEmptyStack();
                flag |= k1 > -1;
            }

            if (slot5.getHasStack() && flag) {
                ItemStack itemstack11 = slot5.getStack();
                inventoryplayer.setInventorySlotContents(clickedButton, itemstack11.copy());
                if ((slot5.inventory != inventoryplayer || !slot5.isItemValid(itemstack7)) && itemstack7 != null) {
                    if (k1 > -1) {
                        inventoryplayer.addItemStackToInventory(itemstack7);
                        slot5.decrStackSize(itemstack11.stackSize);
                        slot5.putStack((ItemStack) null);
                        slot5.onPickupFromSlot();
                    }
                } else {
                    slot5.decrStackSize(itemstack11.stackSize);
                    slot5.putStack(itemstack7);
                    slot5.onPickupFromSlot();
                }
            } else if (!slot5.getHasStack() && itemstack7 != null && slot5.isItemValid(itemstack7)) {
                inventoryplayer.setInventorySlotContents(clickedButton, (ItemStack) null);
                slot5.putStack(itemstack7);
            }
        } else if (mode == 3 && this.mc.playerController instanceof PlayerControllerCreative && this.itemStack == null
                && slotId >= 0) {
            Slot slot4 = (Slot) this.inventorySlots.get(slotId);
            if (slot4 != null && slot4.getHasStack()) {
                ItemStack itemstack6 = slot4.getStack().copy();
                itemstack6.stackSize = itemstack6.getItem().getItemStackLimit();
                this.itemStack = itemstack6;
            }
        } else if (mode == 4 && this.itemStack == null && slotId >= 0) {
            Slot slot3 = (Slot) this.inventorySlots.get(slotId);
            if (slot3 != null && slot3.getHasStack()) {
                ItemStack itemstack5 = slot3.decrStackSize(clickedButton == 0 ? 1 : slot3.getStack().stackSize);
                slot3.onPickupFromSlot();
                playerIn.dropPlayerItem(itemstack5);
            }
        } else if (mode == 6 && slotId >= 0) {
            Slot slot2 = (Slot) this.inventorySlots.get(slotId);
            ItemStack itemstack4 = this.itemStack;
            if (itemstack4 != null && (slot2 == null || !slot2.getHasStack())) {
                int i1 = clickedButton == 0 ? 0 : this.inventorySlots.size() - 1;
                int j1 = clickedButton == 0 ? 1 : -1;

                for (int l2 = 0; l2 < 2; ++l2) {
                    for (int i3 = i1; i3 >= 0 && i3 < this.inventorySlots.size()
                            && itemstack4.stackSize < itemstack4.getItem().getItemStackLimit(); i3 += j1) {
                        Slot slot8 = (Slot) this.inventorySlots.get(i3);
                        if (slot8.getHasStack() && canAddItemToSlot(slot8, itemstack4, true)
                                && this.canMergeSlot(itemstack4, slot8)
                                && (l2 != 0 || slot8.getStack().stackSize != slot8.getStack().getItem().getItemStackLimit())) {
                            int l = Math.min(itemstack4.getItem().getItemStackLimit() - itemstack4.stackSize,
                                    slot8.getStack().stackSize);
                            ItemStack itemstack2 = slot8.decrStackSize(l);
                            itemstack4.stackSize += l;
                            if (itemstack2.stackSize <= 0) {
                                slot8.putStack((ItemStack) null);
                            }

                            slot8.onPickupFromSlot();
                        }
                    }
                }
            }
        }
    }

    public boolean canMergeSlot(ItemStack var1, Slot var2) {
        return true;
    }

    public static int extractDragMode(int parInt1) {
        return parInt1 >> 2 & 3;
    }

    public static int getDragEvent(int parInt1) {
        return parInt1 & 3;
    }

    public boolean isValidDragMode(int dragModeIn, EntityPlayer player) {
        return dragModeIn == 0 ? true
                : (dragModeIn == 1 ? true : dragModeIn == 2 && this.mc.playerController instanceof PlayerControllerCreative);
    }

    protected void resetDrag() {
        this.dragEvent = 0;
        this.dragSlots.clear();
    }

    public ItemStack transferStackInSlot(EntityPlayer var1, int i) {
        Slot slot = (Slot) this.inventorySlots.get(i);
        return slot != null ? slot.getStack() : null;
    }

    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (true) { //stack.isStackable()) {
            while (stack.stackSize > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)) {
                Slot slot = (Slot) this.inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();
                if (itemstack != null && itemstack.getItem() == stack.getItem()) {
                    int j = itemstack.stackSize + stack.stackSize;
                    if (j <= stack.getItem().getItemStackLimit()) {
                        stack.stackSize = 0;
                        itemstack.stackSize = j;
                        slot.onSlotChanged();
                        flag = true;
                    } else if (itemstack.stackSize < stack.getItem().getItemStackLimit()) {
                        stack.stackSize -= stack.getItem().getItemStackLimit() - itemstack.stackSize;
                        itemstack.stackSize = stack.getItem().getItemStackLimit();
                        slot.onSlotChanged();
                        flag = true;
                    }
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (stack.stackSize > 0) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex) {
                Slot slot1 = (Slot) this.inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();
                if (itemstack1 == null) {
                    slot1.putStack(stack.copy());
                    slot1.onSlotChanged();
                    stack.stackSize = 0;
                    flag = true;
                    break;
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
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
                        && canAddItemToSlot(slot, this.draggedStack, false)) {
                    long l = EagRuntime.currentTimeMillis();
                    if (this.currentDragTargetSlot == slot) {
                        if (l - this.dragItemDropDelay > 500L) {
                            this.handleMouseClick(this.clickedSlot, this.inventorySlots.indexOf(this.clickedSlot), 0, 0);
                            this.handleMouseClick(slot, this.inventorySlots.indexOf(slot), 1, 0);
                            this.handleMouseClick(this.clickedSlot, this.inventorySlots.indexOf(this.clickedSlot), 0, 0);
                            this.dragItemDropDelay = l + 750L;
                            --this.draggedStack.stackSize;
                        }
                    } else {
                        this.currentDragTargetSlot = slot;
                        this.dragItemDropDelay = l;
                    }
                }
            }
        } else if (this.dragSplitting && slot != null && this.itemStack != null
                && this.itemStack.stackSize > this.dragSplittingSlots.size()
                && canAddItemToSlot(slot, this.itemStack, true) && slot.isItemValid(this.itemStack)) {
            this.dragSplittingSlots.add(slot);
            this.updateDragSplitting();
        }

    }

    private void updateDragSplitting() {
        if (this.itemStack != null && this.dragSplitting) {
            this.dragSplittingRemnant = this.itemStack.stackSize;

            for (Slot slot : this.dragSplittingSlots) {
                ItemStack itemstack1 = this.itemStack.copy();
                int i = slot.getStack() == null ? 0 : slot.getStack().stackSize;
                computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
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
                l = this.inventorySlots.indexOf(slot);
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
                    if (this.itemStack == null) {
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

    protected void mouseReleased(int i, int j, int k) {
        Slot slot = this.getSlotAtPosition(i, j);
        int l = this.guiLeft;
        int i1 = this.guiTop;
        boolean flag = i < l || j < i1 || i >= l + this.xSize || j >= i1 + this.ySize;
        int j1 = -1;
        if (slot != null) {
            j1 = this.inventorySlots.indexOf(slot);
        }

        if (flag) {
            j1 = -999;
        }

        if (this.doubleClick && slot != null && k == 0 && this.canMergeSlot((ItemStack) null, slot)) {
            if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
                if (slot != null && slot.inventory != null && this.shiftClickedSlot != null) {
                    List<Slot> lst = this.inventorySlots;
                    for (int n = 0, m = lst.size(); n < m; ++n) {
                        Slot slot2 = lst.get(n);
                        if (slot2 != null && slot2.getHasStack()
                                && slot2.inventory == slot.inventory
                                && canAddItemToSlot(slot2, this.shiftClickedSlot, true)) {
                            this.handleMouseClick(slot2, this.inventorySlots.indexOf(slot2), k, 1);
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

                    boolean flag2 = canAddItemToSlot(slot, this.draggedStack, false);
                    if (j1 != -1 && this.draggedStack != null && flag2) {
                        this.handleMouseClick(this.clickedSlot, this.inventorySlots.indexOf(this.clickedSlot), k, 0);
                        this.handleMouseClick(slot, j1, 0, 0);
                        if (this.itemStack != null) {
                            this.handleMouseClick(this.clickedSlot, this.inventorySlots.indexOf(this.clickedSlot), k, 0);
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
                this.handleMouseClick((Slot) null, -999, func_94534_d(0, this.dragSplittingLimit), 5);

                for (Slot slot1 : this.dragSplittingSlots) {
                    this.handleMouseClick(slot1, this.inventorySlots.indexOf(slot1), func_94534_d(1, this.dragSplittingLimit),
                            5);
                }

                this.handleMouseClick((Slot) null, -999, func_94534_d(2, this.dragSplittingLimit), 5);
            } else if (this.itemStack != null) {
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

        if (this.itemStack == null) {
            this.lastClickTime = 0L;
        }

        this.dragSplitting = false;
    }

    private Slot getSlotAtPosition(int x, int y) {
        GuiContainer guiContainer5 = this;
        int i7 = 0;
        Slot slot10000;
        while(true) {
            if(i7 >= guiContainer5.inventorySlots.size()) {
                slot10000 = null;
                break;
            }

            Slot slot8 = (Slot)guiContainer5.inventorySlots.get(i7);
            if(slot8.getIsMouseOverSlot(x, y)) {
                slot10000 = slot8;
                break;
            }

            ++i7;
        }

        return slot10000;
    }

    public static int func_94534_d(int parInt1, int parInt2) {
        return parInt1 & 3 | (parInt2 & 3) << 2;
    }

    public static boolean canAddItemToSlot(Slot slotIn, ItemStack stack, boolean stackSizeMatters) {
        boolean flag = slotIn == null || !(slotIn.getStack() != null);
        if (slotIn != null && slotIn.getStack() != null && stack != null && stack.itemID == slotIn.getStack().itemID && stack.itemDmg == slotIn.getStack().itemDmg) {
            flag |= slotIn.getStack().stackSize + (stackSizeMatters ? 0 : stack.stackSize) <= stack.getItem().getItemStackLimit();
        }

        return flag;
    }

    public static void computeStackSize(Set<Slot> parSet, int parInt1, ItemStack parItemStack, int parInt2) {
        switch (parInt1) {
        case 0:
            parItemStack.stackSize = MathHelper.floor_float((float) parItemStack.stackSize / (float) parSet.size());
            break;
        case 1:
            parItemStack.stackSize = 1;
            break;
        case 2:
            parItemStack.stackSize = parItemStack.getItem().getItemStackLimit();
        }

        parItemStack.stackSize += parInt2;
    }

    public void updateScreen() {
        super.updateScreen();
        if (primaryTouchPoint != -1 && Touch.fetchPointIdx(primaryTouchPoint) == -1) {
            primaryTouchPoint = -1;
            mouseReleased(lastTouchX, lastTouchY, 0);
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
            mouseReleased(touchX, touchY, 0);
        }
    }

    protected void touchTapped(int touchX, int touchY, int uid) {
        if (primaryTouchPoint == uid) {
            primaryTouchPoint = -1;
            lastTouchX = touchX;
            lastTouchY = touchY;
            mouseReleased(touchX, touchY, 0);
        }
    }

    protected boolean shouldTouchGenerateMouseEvents() {
        return false;
    }
}
