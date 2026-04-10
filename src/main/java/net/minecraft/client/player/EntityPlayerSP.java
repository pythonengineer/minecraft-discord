package net.minecraft.client.player;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Session;
import net.minecraft.client.effect.EntityPickupFX;
import net.minecraft.client.gui.container.GuiChest;
import net.minecraft.client.gui.container.GuiCrafting;
import net.minecraft.client.gui.container.GuiEditSign;
import net.minecraft.client.gui.container.GuiFurnace;
import net.minecraft.game.IInventory;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.tileentity.TileEntityFurnace;
import net.minecraft.game.world.block.tileentity.TileEntitySign;

public class EntityPlayerSP extends EntityPlayer {
	public MovementInput movementInput;
	private Minecraft mc;

    public EntityPlayerSP(Minecraft mc, World world, Session session) {
        super(world);
        this.mc = mc;
        if(world != null) {
            if(world.playerEntity != null) {
                World.setEntityDead(world.playerEntity);
            }

            world.playerEntity = this;
        }

        if(session != null && session.name != null && session.name.length() > 0) {
            this.skinUrl = session.name;
            this.username = session.name;
        } else {
            this.username = "";
        }
    }

    public final void updatePlayerActionState() {
        this.moveStrafing = this.movementInput.moveStrafe;
        this.moveForward = this.movementInput.moveForward;
        this.isJumping = this.movementInput.jump;
    }

    public final void onLivingUpdate() {
        this.movementInput.updatePlayerMoveState();
        super.onLivingUpdate();
    }

    public final void writeEntityToNBT(NBTTagCompound compoundTag) {
        super.writeEntityToNBT(compoundTag);
        compoundTag.setInteger("Score", this.score);
        InventoryPlayer inventoryPlayer10002 = this.inventory;
        NBTTagList nBTTagList2 = new NBTTagList();
        InventoryPlayer inventoryPlayer5 = inventoryPlayer10002;

        int i3;
        NBTTagCompound nBTTagCompound4;
        for(i3 = 0; i3 < inventoryPlayer5.mainInventory.length; ++i3) {
            if(inventoryPlayer5.mainInventory[i3] != null) {
                (nBTTagCompound4 = new NBTTagCompound()).setByte("Slot", (byte)i3);
                inventoryPlayer5.mainInventory[i3].writeToNBT(nBTTagCompound4);
                nBTTagList2.setTag(nBTTagCompound4);
            }
        }

        for(i3 = 0; i3 < inventoryPlayer5.armorInventory.length; ++i3) {
            if(inventoryPlayer5.armorInventory[i3] != null) {
                (nBTTagCompound4 = new NBTTagCompound()).setByte("Slot", (byte)(i3 + 100));
                inventoryPlayer5.armorInventory[i3].writeToNBT(nBTTagCompound4);
                nBTTagList2.setTag(nBTTagCompound4);
            }
        }

        compoundTag.setTag("Inventory", nBTTagList2);
    }

    public final void readEntityFromNBT(NBTTagCompound compoundTag) {
        super.readEntityFromNBT(compoundTag);
        this.score = compoundTag.getInteger("Score");
        NBTTagList nBTTagList6 = compoundTag.getTagList("Inventory");
        NBTTagList nBTTagList2 = nBTTagList6;
        InventoryPlayer inventoryPlayer7 = this.inventory;
        this.inventory.mainInventory = new ItemStack[36];
        inventoryPlayer7.armorInventory = new ItemStack[4];

        for(int i3 = 0; i3 < nBTTagList2.tagCount(); ++i3) {
            NBTTagCompound nBTTagCompound4;
            int i5;
            if((i5 = (nBTTagCompound4 = (NBTTagCompound)nBTTagList2.tagAt(i3)).getByte("Slot") & 255) >= 0 && i5 < inventoryPlayer7.mainInventory.length) {
                inventoryPlayer7.mainInventory[i5] = new ItemStack(nBTTagCompound4);
            }

            if(i5 >= 100 && i5 < inventoryPlayer7.armorInventory.length + 100) {
                inventoryPlayer7.armorInventory[i5 - 100] = new ItemStack(nBTTagCompound4);
            }
        }

    }

    public final void displayGUIChest(IInventory inventory) {
        this.mc.setGuiScreen(new GuiChest(this.inventory, inventory));
    }

    public final void displayGUIEditSign(TileEntitySign signTileEntity) {
        this.mc.setGuiScreen(new GuiEditSign(signTileEntity));
    }

    public final void displayWorkbenchGUI() {
        this.mc.setGuiScreen(new GuiCrafting(this.inventory));
    }

    public final void displayGUIFurnace(TileEntityFurnace furnaceTileEntity) {
        this.mc.setGuiScreen(new GuiFurnace(this.inventory, furnaceTileEntity));
    }

    public final void destroyCurrentEquippedItem() {
        this.inventory.setInventorySlotContents(this.inventory.currentItem, (ItemStack)null);
    }

    public final void onItemPickup(Entity entity) {
        this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, entity, this, -0.5F));
    }
}
