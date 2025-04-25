package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.ItemStack;
import net.minecraft.game.level.MobSpawner;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;

public final class PlayerControllerSP extends PlayerController {
    private int hitX = -1;
    private int hitY = -1;
    private int hitZ = -1;
    private int hits = 0;
    private int hardness = 0;
    private int hitDelay = 0;
    private MobSpawner mobSpawner;

    public PlayerControllerSP(Minecraft var1) {
        super(var1);
    }

    public final void displayInventoryGUI() {
        this.a.displayGuiScreen(new GuiInventory());
    }

    public final void onRespawn(EntityPlayer var1) {
        var1.inventory.mainInventory[6] = new ItemStack(Block.torch, 99);
        var1.inventory.mainInventory[7] = new ItemStack(Block.tnt, 99);
        var1.inventory.mainInventory[8] = new ItemStack(Block.bookshelf, 99);

        for(int var2 = 0; var2 < 20; ++var2) {
            int var3 = var2 % 5;
            int var4 = var2 / 5;
            var1.inventory.mainInventory[var2 + 9] = new ItemStack(var3 + (var4 << 4), 1);
        }

    }

    public final void sendBlockRemoved(int var1, int var2, int var3) {
        int var4 = this.a.theWorld.getBlockId(var1, var2, var3);
        Block.blocksList[var4].dropBlockAsItem(this.a.theWorld, var1, var2, var3);
        super.sendBlockRemoved(var1, var2, var3);
    }

    public final boolean canPlace(int var1, int var2, int var3, int var4) {
        super.canPlace(var1, var2, var3, var4);
        return this.a.thePlayer.inventory.removeResource(var4);
    }

    public final void clickBlock(int var1, int var2, int var3) {
        int var4 = this.a.theWorld.getBlockId(var1, var2, var3);
        if(var4 > 0 && Block.blocksList[var4].blockStrength() == 0) {
            this.sendBlockRemoved(var1, var2, var3);
        }

    }

    public final void resetBlockRemoving() {
        this.hits = 0;
        this.hitDelay = 0;
    }

    public final void hitBlock(int var1, int var2, int var3, int var4) {
        if(this.hitDelay > 0) {
            --this.hitDelay;
        } else {
            super.hitBlock(var1, var2, var3, var4);
            if(var1 == this.hitX && var2 == this.hitY && var3 == this.hitZ) {
                var4 = this.a.theWorld.getBlockId(var1, var2, var3);
                if(var4 != 0) {
                    Block var6 = Block.blocksList[var4];
                    this.hardness = var6.blockStrength();
                    if(this.hits % 4 == 0 && var6 != null) {
                        SoundManager var10000 = this.a.sndManager;
                        String var10001 = "step." + var6.stepSound.a;
                        float var10002 = (float)var1 + 0.5F;
                        float var10003 = (float)var2 + 0.5F;
                        float var10004 = (float)var3 + 0.5F;
                        StepSound var5 = var6.stepSound;
                        float var10005 = (var5.speed + 1.0F) / 8.0F;
                        var5 = var6.stepSound;
                        var10000.a(var10001, var10002, var10003, var10004, var10005, var5.pitch * 0.5F);
                    }

                    ++this.hits;
                    if(this.hits == this.hardness + 1) {
                        this.sendBlockRemoved(var1, var2, var3);
                        this.hits = 0;
                        this.hitDelay = 5;
                    }

                }
            } else {
                this.hits = 0;
                this.hitX = var1;
                this.hitY = var2;
                this.hitZ = var3;
            }
        }
    }

    public final void setPartialTime(float var1) {
        if(this.hits <= 0) {
            this.a.renderGlobal.damagePartialTime = 0.0F;
        } else {
            this.a.renderGlobal.damagePartialTime = ((float)this.hits + var1 - 1.0F) / (float)this.hardness;
        }
    }

    public final float getBlockReachDistance() {
        return 4.0F;
    }

    public final void onWorldChange(World var1) {
        super.onWorldChange(var1);
        this.mobSpawner = new MobSpawner(var1);
        int var2 = var1.width * var1.length * var1.height / 64 / 64 / 8;

        for(int var3 = 0; var3 < var2; ++var3) {
            this.mobSpawner.performSpawning(var2, var1.playerEntity, null);
        }

    }

    public final void onUpdate() {
        this.mobSpawner.performSpawning();
    }
}
