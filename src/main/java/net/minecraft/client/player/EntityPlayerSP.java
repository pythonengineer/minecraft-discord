package net.minecraft.client.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Session;
import net.minecraft.client.gui.container.GuiChest;
import net.minecraft.client.gui.container.GuiCrafting;
import net.minecraft.client.gui.container.GuiFurnace;
import net.minecraft.game.IInventory;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.tileentity.TileEntityFurnace;

public class EntityPlayerSP extends EntityPlayer {
	public MovementInput movementInput;
	private Minecraft mc;

	public EntityPlayerSP(Minecraft var1, World var2, Session var3) {
		super(var2);
		this.mc = var1;
        if(var3 != null) {
            this.skinUrl = var3.username;
            this.username = var3.username;
        } else {
            this.username = "";
        }
    }

    public final void updateEntityActionState() {
        this.moveStrafing = this.movementInput.moveStrafe;
        this.moveForward = this.movementInput.moveForward;
        this.isJumping = this.movementInput.jump;
    }

    public final void updatePlayerActionState() {
        this.movementInput.updatePlayerMoveState();
        super.updatePlayerActionState();
	}

	public final void displayGUIChest(IInventory var1) {
		this.mc.displayGuiScreen(new GuiChest(this.inventory, var1));
	}

    public final void displayWorkbenchGUI() {
        this.mc.displayGuiScreen(new GuiCrafting(this.inventory));
    }

    public final void displayGUIFurnace(TileEntityFurnace var1) {
        this.mc.displayGuiScreen(new GuiFurnace(this.inventory, var1));
    }

    public final void destroyCurrentEquippedItem() {
        this.inventory.setInventorySlotContents(this.inventory.currentItem, (ItemStack)null);
    }
}
