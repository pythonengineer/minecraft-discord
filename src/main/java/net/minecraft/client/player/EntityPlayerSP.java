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
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.EntityPlayer;
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
        if(session != null && session.username != null && session.username.length() > 0) {
            this.skinUrl = session.username;
            this.username = session.username;
            System.out.println("Loading texture " + this.skinUrl);
        } else {
            this.username = "";
        }
    }

    public void updateEntityActionState() {
        this.moveStrafing = this.movementInput.moveStrafe;
        this.moveForward = this.movementInput.moveForward;
        this.isJumping = this.movementInput.jump;
    }

    public void onLivingUpdate() {
        this.movementInput.updatePlayerMoveState(this);
        super.onLivingUpdate();
    }

    public void resetPlayerKeyState() {
        this.movementInput.resetKeyState();
    }

    public void handleKeyPress(int keyState, boolean isMovementInput) {
        this.movementInput.checkKeyForMovementInput(keyState, isMovementInput);
    }

    public void writeEntityToNBT(NBTTagCompound compoundTag) {
        super.writeEntityToNBT(compoundTag);
        compoundTag.setInteger("Score", this.score);
        compoundTag.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
    }

    public void readEntityFromNBT(NBTTagCompound compoundTag) {
        super.readEntityFromNBT(compoundTag);
        this.score = compoundTag.getInteger("Score");
        NBTTagList nBTTagList2 = compoundTag.getTagList("Inventory");
        this.inventory.readFromNBT(nBTTagList2);
    }

    public void displayGUIChest(IInventory inventory) {
        this.mc.displayGuiScreen(new GuiChest(this.inventory, inventory));
    }

    public void displayGUIEditSign(TileEntitySign signTileEntity) {
        this.mc.displayGuiScreen(new GuiEditSign(signTileEntity));
    }

    public void displayWorkbenchGUI() {
        this.mc.displayGuiScreen(new GuiCrafting(this.inventory));
    }

    public void displayGUIFurnace(TileEntityFurnace furnaceTileEntity) {
        this.mc.displayGuiScreen(new GuiFurnace(this.inventory, furnaceTileEntity));
    }

    public void attackEntity(Entity entity1) {
        int i2 = this.inventory.getDamageVsEntity(entity1);
        if(i2 > 0) {
            entity1.attackEntityFrom(this, i2);
            ItemStack itemStack3 = this.getCurrentEquippedItem();
            if(itemStack3 != null && entity1 instanceof EntityLiving) {
                itemStack3.hitEntity((EntityLiving)entity1);
                if(itemStack3.stackSize <= 0) {
                    itemStack3.onItemDestroyedByUse(this);
                    this.destroyCurrentEquippedItem();
                }
            }
        }

    }

    public void onItemPickup(Entity entity, int i2) {
        this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, entity, this, -0.5F));
    }

    public int getPlayerArmorValue() {
        return this.inventory.getTotalArmorValue();
    }

    public void interactWithEntity(Entity entity1) {
        if(!entity1.interact(this)) {
            ItemStack itemStack2 = this.getCurrentEquippedItem();
            if(itemStack2 != null && entity1 instanceof EntityLiving) {
                itemStack2.useItemOnEntity((EntityLiving)entity1);
                if(itemStack2.stackSize <= 0) {
                    itemStack2.onItemDestroyedByUse(this);
                    this.destroyCurrentEquippedItem();
                }
            }

        }
    }

    public void sendChatMessage(String chatMessage) {
    }
}
