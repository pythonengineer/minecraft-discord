package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.entity.animal.EntityAnimal;
import net.minecraft.game.entity.animal.EntityPig;
import net.minecraft.game.entity.animal.EntitySheep;
import net.minecraft.game.entity.monster.EntityCreeper;
import net.minecraft.game.entity.monster.EntityMob;
import net.minecraft.game.entity.monster.EntitySkeleton;
import net.minecraft.game.entity.monster.EntitySpider;
import net.minecraft.game.entity.monster.EntityZombie;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.SpawnerAnimals;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.StepSound;

public final class PlayerControllerSP extends PlayerController {
    private int curBlockX = -1;
    private int curBlockY = -1;
    private int curBlockZ = -1;
    private float curBlockDamage = 0.0F;
    private float prevBlockDamage = 0.0F;
    private float blockDestroySoundCounter = 0.0F;
    private int blockHitWait = 0;
    private SpawnerAnimals monsterSpawner = new SpawnerMonsters(this, 100, EntityMob.class, new Class[]{EntityZombie.class, EntitySkeleton.class, EntityCreeper.class, EntitySpider.class});
    private SpawnerAnimals animalSpawner = new SpawnerAnimals(20, EntityAnimal.class, new Class[]{EntitySheep.class, EntityPig.class});

    public PlayerControllerSP(Minecraft minecraft1) {
        super(minecraft1);
    }

    public final void flipPlayer(EntityPlayer playerEntity) {
        playerEntity.rotationYaw = -180.0F;
    }

    public final boolean sendBlockRemoved(int x, int y, int z) {
        int i4 = this.mc.theWorld.getBlockId(x, y, z);
        int i5 = this.mc.theWorld.getBlockMetadata(x, y, z);
        boolean z6 = super.sendBlockRemoved(x, y, z);
        EntityPlayerSP entityPlayerSP7 = this.mc.thePlayer;
        ItemStack itemStack9;
        if((itemStack9 = this.mc.thePlayer.inventory.getCurrentItem()) != null) {
            Item.itemsList[itemStack9.itemID].onBlockDestroyed(itemStack9);
            if(itemStack9.stackSize == 0) {
                this.mc.thePlayer.displayGUIInventory();
            }
        }

        if(z6 && this.mc.thePlayer.canHarvestBlock(Block.blocksList[i4])) {
            Block.blocksList[i4].dropBlockAsItem(this.mc.theWorld, x, y, z, i5);
        }

        return z6;
    }

    public final void clickBlock(int x, int y, int z) {
        int i4;
        if((i4 = this.mc.theWorld.getBlockId(x, y, z)) > 0 && this.curBlockDamage == 0.0F) {
            Block.blocksList[i4].onBlockClicked(this.mc.theWorld, x, y, z, this.mc.thePlayer);
        }

        if(i4 > 0 && Block.blocksList[i4].blockStrength(this.mc.thePlayer) >= 1.0F) {
            this.sendBlockRemoved(x, y, z);
        }

    }

    public final void resetBlockRemoving() {
        this.curBlockDamage = 0.0F;
        this.blockHitWait = 0;
    }

    public final void sendBlockRemoving(int x, int y, int z, int blockID) {
        if(this.blockHitWait > 0) {
            --this.blockHitWait;
        } else {
            super.sendBlockRemoving(x, y, z, blockID);
            if(x == this.curBlockX && y == this.curBlockY && z == this.curBlockZ) {
                if((blockID = this.mc.theWorld.getBlockId(x, y, z)) != 0) {
                    Block block6 = Block.blocksList[blockID];
                    this.curBlockDamage += block6.blockStrength(this.mc.thePlayer);
                    if(this.blockDestroySoundCounter % 4.0F == 0.0F && block6 != null) {
                        SoundManager soundManager10000 = this.mc.sndManager;
                        String string10001 = block6.stepSound.getStepSound();
                        float f10002 = (float)x + 0.5F;
                        float f10003 = (float)y + 0.5F;
                        float f10004 = (float)z + 0.5F;
                        StepSound stepSound5 = block6.stepSound;
                        float f10005 = (block6.stepSound.stepSoundVolume + 1.0F) / 8.0F;
                        stepSound5 = block6.stepSound;
                        soundManager10000.playSound(string10001, f10002, f10003, f10004, f10005, block6.stepSound.stepSoundPitch * 0.5F);
                    }

                    ++this.blockDestroySoundCounter;
                    if(this.curBlockDamage >= 1.0F) {
                        this.sendBlockRemoved(x, y, z);
                        this.curBlockDamage = 0.0F;
                        this.prevBlockDamage = 0.0F;
                        this.blockDestroySoundCounter = 0.0F;
                        this.blockHitWait = 5;
                    }

                }
            } else {
                this.curBlockDamage = 0.0F;
                this.prevBlockDamage = 0.0F;
                this.blockDestroySoundCounter = 0.0F;
                this.curBlockX = x;
                this.curBlockY = y;
                this.curBlockZ = z;
            }
        }
    }

    public final void setPartialTime(float partialTime) {
        if(this.curBlockDamage <= 0.0F) {
            this.mc.renderGlobal.damagePartialTime = 0.0F;
        } else {
            partialTime = this.prevBlockDamage + (this.curBlockDamage - this.prevBlockDamage) * partialTime;
            this.mc.renderGlobal.damagePartialTime = partialTime;
        }
    }

    public final float getBlockReachDistance() {
        return 4.0F;
    }

    public final void onUpdate() {
        this.prevBlockDamage = this.curBlockDamage;
        this.monsterSpawner.onUpdate(this.mc.theWorld);
        this.animalSpawner.onUpdate(this.mc.theWorld);
    }
}