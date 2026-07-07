package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.common.collect.Sets;
import net.lax1dude.eaglercraft.util.MathHelper;

public abstract class CraftingInventoryCB {
    public List field_20123_d = new ArrayList();
    public List<Slot> slots = new ArrayList<Slot>();
    public int windowId = 0;
    private short field_20917_a = 0;
    protected List field_20121_g = new ArrayList();
    private Set field_20918_b = new HashSet();
    private int dragMode = -1;
    private int dragEvent;
    private final Set<Slot> dragSlots = Sets.newHashSet();

    protected void func_20117_a(Slot var1) {
        var1.field_20007_a = this.slots.size();
        this.slots.add(var1);
        this.field_20123_d.add((Object)null);
    }

    public void func_20114_a() {
        for(int var1 = 0; var1 < this.slots.size(); ++var1) {
            ItemStack var2 = ((Slot)this.slots.get(var1)).getStack();
            ItemStack var3 = (ItemStack)this.field_20123_d.get(var1);
            if(!ItemStack.areItemStacksEqual(var3, var2)) {
                var3 = var2 == null ? null : var2.copy();
                this.field_20123_d.set(var1, var3);

                for(int var4 = 0; var4 < this.field_20121_g.size(); ++var4) {
                    ((ICrafting)this.field_20121_g.get(var4)).func_20159_a(this, var1, var3);
                }
            }
        }

    }

    public Slot getSlot(int var1) {
        return (Slot)this.slots.get(var1);
    }

    public ItemStack func_20116_a(int var1, int var2, int mode, EntityPlayer var3) {
        int slotId = var1;
        int clickedButton = var2;
        ItemStack itemstack = null;
        InventoryPlayer inventoryplayer = var3.inventory;
        if (mode == 5) {
            int i = this.dragEvent;
            this.dragEvent = getDragEvent(clickedButton);
            if ((i != 1 || this.dragEvent != 2) && i != this.dragEvent) {
                this.resetDrag();
            } else if (var3.inventory.getItemStack() == null) {
                this.resetDrag();
            } else if (this.dragEvent == 0) {
                this.dragMode = extractDragMode(clickedButton);
                if (this.isValidDragMode(this.dragMode, var3)) {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                } else {
                    this.resetDrag();
                }
            } else if (this.dragEvent == 1) {
                Slot slot = (Slot) this.slots.get(slotId);
                if (slot != null && canAddItemToSlot(slot, var3.inventory.getItemStack(), true)
                        && slot.isItemValid(var3.inventory.getItemStack())
                        && var3.inventory.getItemStack().stackSize > this.dragSlots.size()
                        && this.canDragIntoSlot(slot)) {
                    this.dragSlots.add(slot);
                }
            } else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    ItemStack itemstack3 = var3.inventory.getItemStack().copy();
                    int j = var3.inventory.getItemStack().stackSize;

                    for (Slot slot1 : this.dragSlots) {
                        if (slot1 != null && canAddItemToSlot(slot1, var3.inventory.getItemStack(), true)
                                && slot1.isItemValid(var3.inventory.getItemStack())
                                && var3.inventory.getItemStack().stackSize >= this.dragSlots.size()
                                && this.canDragIntoSlot(slot1)) {
                            ItemStack itemstack1 = itemstack3.copy();
                            int k = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
                            computeStackSize(this.dragSlots, this.dragMode, itemstack1, k);
                            if (itemstack1.stackSize > itemstack1.getMaxStackSize()) {
                                itemstack1.stackSize = itemstack1.getMaxStackSize();
                            }

                            if (itemstack1.stackSize > slot1.getSlotStackLimit()) {
                                itemstack1.stackSize = slot1.getSlotStackLimit();
                            }

                            j -= itemstack1.stackSize - k;
                            slot1.putStack(itemstack1);
                        }
                    }

                    itemstack3.stackSize = j;
                    if (itemstack3.stackSize <= 0) {
                        itemstack3 = null;
                    }

                    var3.inventory.setItemStack(itemstack3);
                }

                this.resetDrag();
            } else {
                this.resetDrag();
            }
        } else if (this.dragEvent != 0) {
            this.resetDrag();
        } else if ((mode == 0 || mode == 1) && (clickedButton == 0 || clickedButton == 1)) {
            if (slotId == -999) {
                if (var3.inventory.getItemStack() != null) {
                    if (clickedButton == 0) {
                        var3.dropPlayerItem(var3.inventory.getItemStack());
                        var3.inventory.setItemStack(null);
                    }

                    if (clickedButton == 1) {
                        var3.dropPlayerItem(var3.inventory.getItemStack().splitStack(1));
                        if (var3.inventory.getItemStack().stackSize == 0) {
                            var3.inventory.setItemStack(null);
                        }
                    }
                }
            } else if (mode == 1) {
                if (slotId < 0) {
                    return null;
                }

                Slot slot6 = (Slot) this.slots.get(slotId);
                if (slot6 != null) {
                    ItemStack itemstack8 = this.transferStackInSlot(var3, slotId);
                    if (itemstack8 != null) {
                        Item item = itemstack8.getItem();
                        itemstack = itemstack8.copy();
                        if (slot6.getStack() != null && slot6.getStack().getItem() == item) {
                            this.retrySlotClick(slotId, clickedButton, true, var3);
                        }
                    }
                }
            } else {
                if (slotId < 0) {
                    return null;
                }

                Slot slot7 = (Slot) this.slots.get(slotId);
                if (slot7 != null) {
                    ItemStack itemstack9 = slot7.getStack();
                    ItemStack itemstack10 = var3.inventory.getItemStack();
                    if (itemstack9 != null) {
                        itemstack = itemstack9.copy();
                    }

                    if (itemstack9 == null) {
                        if (itemstack10 != null && slot7.isItemValid(itemstack10)) {
                            int k2 = clickedButton == 0 ? itemstack10.stackSize : 1;
                            if (k2 > slot7.getSlotStackLimit()) {
                                k2 = slot7.getSlotStackLimit();
                            }

                            if (itemstack10.stackSize >= k2) {
                                slot7.putStack(itemstack10.splitStack(k2));
                            }

                            if (itemstack10.stackSize == 0) {
                                var3.inventory.setItemStack(null);
                            }
                        }
                    } else {
                        if (itemstack10 == null) {
                            int j2 = clickedButton == 0 ? itemstack9.stackSize : (itemstack9.stackSize + 1) / 2;
                            ItemStack itemstack12 = slot7.decrStackSize(j2);
                            var3.inventory.setItemStack(itemstack12);
                            if (itemstack9.stackSize == 0) {
                                slot7.putStack((ItemStack) null);
                            }

                            slot7.onPickupFromSlot();
                        } else if (slot7.isItemValid(itemstack10)) {
                            if (itemstack9.getItem() == itemstack10.getItem()) {
                                int i2 = clickedButton == 0 ? itemstack10.stackSize : 1;
                                if (i2 > slot7.getSlotStackLimit() - itemstack9.stackSize) {
                                    i2 = slot7.getSlotStackLimit() - itemstack9.stackSize;
                                }

                                if (i2 > itemstack10.getMaxStackSize() - itemstack9.stackSize) {
                                    i2 = itemstack10.getMaxStackSize()- itemstack9.stackSize;
                                }

                                itemstack10.splitStack(i2);
                                if (itemstack10.stackSize == 0) {
                                    var3.inventory.setItemStack(null);
                                }

                                itemstack9.stackSize += i2;
                            } else if (itemstack10.stackSize <= slot7.getSlotStackLimit()) {
                                slot7.putStack(itemstack10);
                                var3.inventory.setItemStack(itemstack9);
                            }
                        } else if (itemstack9.getItem() == itemstack10.getItem() && itemstack10.getMaxStackSize() > 1) {
                        } else if (itemstack9.getItem() == itemstack10.getItem() && itemstack10.getMaxStackSize() > 1
                                && (!itemstack9.getHasSubtypes()
                                        || itemstack9.getItemDamage() == itemstack10.getItemDamage())) {
                            int l1 = itemstack9.stackSize;
                            if (l1 > 0 && l1 + itemstack10.stackSize <= itemstack10.getMaxStackSize()) {
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
            Slot slot5 = (Slot) this.slots.get(slotId);
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
        } else if (mode == 3 && false && var3.inventory.getItemStack() == null
                && slotId >= 0) { // creative mode
            Slot slot4 = (Slot) this.slots.get(slotId);
            if (slot4 != null && slot4.getHasStack()) {
                ItemStack itemstack6 = slot4.getStack().copy();
                itemstack6.stackSize = itemstack6.getMaxStackSize();
                var3.inventory.setItemStack(itemstack6);
            }
        } else if (mode == 4 && var3.inventory.getItemStack() == null && slotId >= 0) {
            Slot slot3 = (Slot) this.slots.get(slotId);
            if (slot3 != null && slot3.getHasStack()) {
                ItemStack itemstack5 = slot3.decrStackSize(clickedButton == 0 ? 1 : slot3.getStack().stackSize);
                slot3.onPickupFromSlot();
                var3.dropPlayerItem(itemstack5);
            }
        } else if (mode == 6 && slotId >= 0) {
            Slot slot2 = (Slot) this.slots.get(slotId);
            ItemStack itemstack4 = var3.inventory.getItemStack();
            if (itemstack4 != null && (slot2 == null || !slot2.getHasStack())) {
                int i1 = clickedButton == 0 ? 0 : this.slots.size() - 1;
                int j1 = clickedButton == 0 ? 1 : -1;

                for (int l2 = 0; l2 < 2; ++l2) {
                    for (int i3 = i1; i3 >= 0 && i3 < this.slots.size()
                            && itemstack4.stackSize < itemstack4.getMaxStackSize(); i3 += j1) {
                        Slot slot8 = (Slot) this.slots.get(i3);
                        if (slot8.getHasStack() && canAddItemToSlot(slot8, itemstack4, true)
                                && this.canMergeSlot(itemstack4, slot8)
                                && (l2 != 0 || slot8.getStack().stackSize != slot8.getStack().getMaxStackSize())) {
                            int l = Math.min(itemstack4.getMaxStackSize() - itemstack4.stackSize,
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

        return itemstack;
    }

    public List<int[]> translateToBasicClicks(int slotId, int button, int mode, EntityPlayer player) {
        switch (mode) {
        case 1:
            return this.planShiftClick(slotId, button, player);
        case 6:
            return this.planDoubleClick(slotId, player);
        case 3:
            return new ArrayList<int[]>();
        default:
            return null;
        }
    }

    private List<int[]> planShiftClick(int slotId, int button, EntityPlayer player) {
        List<int[]> clicks = new ArrayList<int[]>();
        if (slotId < 0 || slotId >= this.slots.size()) {
            return clicks;
        }

        ItemStack[] before = this.snapshotSlots();
        ItemStack heldBefore = player.inventory.getItemStack();
        heldBefore = heldBefore == null ? null : heldBefore.copy();

        this.func_20116_a(slotId, button, 1, player);

        ItemStack[] after = this.snapshotSlots();
        this.restoreSlots(before, heldBefore, player);

        if (before[slotId] == null) {
            return clicks;
        }

        List<Integer> partials = new ArrayList<Integer>();
        List<Integer> empties = new ArrayList<Integer>();
        for (int i = 0; i < after.length; ++i) {
            if (i == slotId) {
                continue;
            }

            int had = before[i] == null ? 0 : before[i].stackSize;
            int now = after[i] == null ? 0 : after[i].stackSize;
            if (now > had) {
                if (before[i] == null) {
                    empties.add(Integer.valueOf(i));
                } else {
                    partials.add(Integer.valueOf(i));
                }
            }
        }

        if (partials.isEmpty() && empties.isEmpty()) {
            return clicks;
        }

        if (this.slots.get(slotId) instanceof SlotCrafting) {
            return this.planShiftCraft(slotId, before, after, partials, empties);
        }

        clicks.add(new int[] { slotId, 0 });
        for (int i = 0; i < partials.size(); ++i) {
            clicks.add(new int[] { partials.get(i).intValue(), 0 });
        }
        for (int i = 0; i < empties.size(); ++i) {
            clicks.add(new int[] { empties.get(i).intValue(), 0 });
        }
        if (after[slotId] != null) {
            clicks.add(new int[] { slotId, 0 });
        }

        return clicks;
    }

    private List<int[]> planShiftCraft(int slotId, ItemStack[] before, ItemStack[] after, List<Integer> partials,
            List<Integer> empties) {
        List<int[]> clicks = new ArrayList<int[]>();
        int perCraft = before[slotId].stackSize;
        int maxStack = before[slotId].getMaxStackSize();
        if (perCraft <= 0) {
            return clicks;
        }

        List<Integer> dests = new ArrayList<Integer>(partials);
        dests.addAll(empties);
        int[] destCurrent = new int[dests.size()];
        int[] destLimit = new int[dests.size()];
        int totalMoved = 0;
        for (int d = 0; d < dests.size(); ++d) {
            int i = dests.get(d).intValue();
            destCurrent[d] = before[i] == null ? 0 : before[i].stackSize;
            destLimit[d] = Math.min(((Slot) this.slots.get(i)).getSlotStackLimit(), maxStack);
            totalMoved += (after[i] == null ? 0 : after[i].stackSize) - destCurrent[d];
        }

        int crafts = totalMoved / perCraft;
        if (crafts <= 0) {
            crafts = totalMoved > 0 ? 1 : 0;
        }

        int handCount = 0;
        int destPos = 0;
        for (int c = 0; c < crafts; ++c) {
            if (handCount + perCraft > maxStack) {
                destPos = this.flushHand(clicks, dests, destCurrent, destLimit, destPos, handCount);
                handCount = 0;
            }

            clicks.add(new int[] { slotId, 0 });
            handCount += perCraft;
        }

        this.flushHand(clicks, dests, destCurrent, destLimit, destPos, handCount);
        return clicks;
    }

    private int flushHand(List<int[]> clicks, List<Integer> dests, int[] destCurrent, int[] destLimit, int destPos,
            int handCount) {
        int d = destPos;
        while (handCount > 0 && d < dests.size()) {
            int room = destLimit[d] - destCurrent[d];
            if (room <= 0) {
                ++d;
                continue;
            }

            int deposit = Math.min(handCount, room);
            clicks.add(new int[] { dests.get(d).intValue(), 0 });
            destCurrent[d] += deposit;
            handCount -= deposit;
            if (destCurrent[d] >= destLimit[d]) {
                ++d;
            }
        }

        return d;
    }

    private List<int[]> planDoubleClick(int slotId, EntityPlayer player) {
        List<int[]> clicks = new ArrayList<int[]>();
        ItemStack held = player.inventory.getItemStack();
        if (held == null) {
            return clicks;
        }

        int max = held.getMaxStackSize();
        if (held.stackSize >= max) {
            return clicks;
        }

        int acc = -1;
        if (slotId >= 0 && slotId < this.slots.size()) {
            Slot s = (Slot) this.slots.get(slotId);
            if (!s.getHasStack() && s.isItemValid(held)) {
                acc = slotId;
            }
        }
        if (acc < 0) {
            for (int i = 0; i < this.slots.size(); ++i) {
                Slot s = (Slot) this.slots.get(i);
                if (!s.getHasStack() && s.isItemValid(held)) {
                    acc = i;
                    break;
                }
            }
        }
        if (acc < 0) {
            return clicks;
        }

        clicks.add(new int[] { acc, 0 });
        int accCount = held.stackSize;

        for (int pass = 0; pass < 2 && accCount < max; ++pass) {
            for (int i = 0; i < this.slots.size() && accCount < max; ++i) {
                if (i == acc) {
                    continue;
                }

                Slot s = (Slot) this.slots.get(i);
                ItemStack stack = s.getStack();
                if (stack == null || !sameItem(stack, held)) {
                    continue;
                }

                boolean full = stack.stackSize >= stack.getMaxStackSize();
                if ((pass == 0) == full) {
                    continue;
                }

                int take = Math.min(stack.stackSize, max - accCount);
                if (take <= 0) {
                    continue;
                }

                clicks.add(new int[] { i, 0 });
                clicks.add(new int[] { acc, 0 });
                if (stack.stackSize - take > 0) {
                    clicks.add(new int[] { i, 0 });
                }

                accCount += take;
            }
        }

        clicks.add(new int[] { acc, 0 });
        return clicks;
    }

    private ItemStack[] snapshotSlots() {
        ItemStack[] snap = new ItemStack[this.slots.size()];
        for (int i = 0; i < snap.length; ++i) {
            ItemStack s = ((Slot) this.slots.get(i)).getStack();
            snap[i] = s == null ? null : s.copy();
        }

        return snap;
    }

    private void restoreSlots(ItemStack[] snap, ItemStack held, EntityPlayer player) {
        for (int i = 0; i < snap.length; ++i) {
            ((Slot) this.slots.get(i)).putStack(snap[i] == null ? null : snap[i].copy());
        }

        player.inventory.setItemStack(held == null ? null : held.copy());
    }

    private static boolean sameItem(ItemStack a, ItemStack b) {
        return a != null && b != null && a.itemID == b.itemID && a.getItemDamage() == b.getItemDamage();
    }

    public void onCraftGuiClosed(EntityPlayer var1) {
        InventoryPlayer var2 = var1.inventory;
        if(var2.getItemStack() != null) {
            var1.dropPlayerItem(var2.getItemStack());
            var2.setItemStack((ItemStack)null);
        }

    }

    public void onCraftMatrixChanged(IInventory var1) {
        this.func_20114_a();
    }

    public void putStackInSlot(int var1, ItemStack var2) {
        this.getSlot(var1).putStack(var2);
    }

    public void putStacksInSlots(ItemStack[] var1) {
        for(int var2 = 0; var2 < var1.length; ++var2) {
            this.getSlot(var2).putStack(var1[var2]);
        }

    }

    public void func_20112_a(int var1, int var2) {
    }

    public short func_20111_a(InventoryPlayer var1) {
        ++this.field_20917_a;
        return this.field_20917_a;
    }

    public void func_20113_a(short var1) {
    }

    public void func_20110_b(short var1) {
    }

    public abstract boolean func_20120_b(EntityPlayer var1);

    public ItemStack transferStackInSlot(EntityPlayer var1, int i) {
        Slot slot = (Slot) this.slots.get(i);
        return slot != null ? slot.getStack() : null;
    }

    public boolean canMergeSlot(ItemStack var1, Slot var2) {
        return true;
    }

    protected void retrySlotClick(int i, int j, boolean var3, EntityPlayer entityplayer) {
        this.func_20116_a(i, j, 1, entityplayer);
    }

    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (true) { //stack.isStackable()) {
            while (stack.stackSize > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)) {
                Slot slot = (Slot) this.slots.get(i);
                ItemStack itemstack = slot.getStack();
                if (itemstack != null && itemstack.getItem() == stack.getItem()
                        && (!stack.getHasSubtypes() || stack.getItemDamage() == itemstack.getItemDamage())) {
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
                Slot slot1 = (Slot) this.slots.get(i);
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

    public static int extractDragMode(int parInt1) {
        return parInt1 >> 2 & 3;
    }

    public static int getDragEvent(int parInt1) {
        return parInt1 & 3;
    }

    public boolean isValidDragMode(int dragModeIn, EntityPlayer player) {
        return dragModeIn == 0 ? true
                : (dragModeIn == 1 ? true : dragModeIn == 2 && false); // creative mode
    }

    protected void resetDrag() {
        this.dragEvent = 0;
        this.dragSlots.clear();
    }

    public static boolean canAddItemToSlot(Slot slotIn, ItemStack stack, boolean stackSizeMatters) {
        boolean flag = slotIn == null || !(slotIn.getStack() != null);
        if (slotIn != null && slotIn.getStack() != null && stack != null && stack.itemID == slotIn.getStack().itemID && stack.getItemDamage() == slotIn.getStack().getItemDamage()) {
            flag |= slotIn.getStack().stackSize + (stackSizeMatters ? 0 : stack.stackSize) <= stack.getMaxStackSize();
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
            parItemStack.stackSize = parItemStack.getMaxStackSize();
        }

        parItemStack.stackSize += parInt2;
    }

    public boolean canDragIntoSlot(Slot var1) {
        return true;
    }
}
