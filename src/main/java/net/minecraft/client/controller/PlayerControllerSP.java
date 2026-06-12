package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.entity.animal.EntityAnimal;
import net.minecraft.game.entity.animal.EntityChicken;
import net.minecraft.game.entity.animal.EntityCow;
import net.minecraft.game.entity.animal.EntityPig;
import net.minecraft.game.entity.animal.EntitySheep;
import net.minecraft.game.entity.monster.EntityCreeper;
import net.minecraft.game.entity.monster.EntityMob;
import net.minecraft.game.entity.monster.EntitySkeleton;
import net.minecraft.game.entity.monster.EntitySlime;
import net.minecraft.game.entity.monster.EntitySpider;
import net.minecraft.game.entity.monster.EntityZombie;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.SpawnerAnimals;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.StepSound;

public class PlayerControllerSP extends PlayerController {
    private int curBlockX = -1;
    private int curBlockY = -1;
    private int curBlockZ = -1;
    private float curBlockDamage = 0.0F;
    private float prevBlockDamage = 0.0F;
    private float blockDestroySoundCounter = 0.0F;
    private int blockHitWait = 0;
    private SpawnerAnimals monsterSpawner = new SpawnerMonsters(this, 200, EntityMob.class, new Class[]{EntityZombie.class, EntitySkeleton.class, EntityCreeper.class, EntitySpider.class, EntitySlime.class});
    private SpawnerAnimals animalSpawner = new SpawnerAnimals(20, EntityAnimal.class, new Class[]{EntitySheep.class, EntityPig.class, EntityCow.class, EntityChicken.class});

    public PlayerControllerSP(Minecraft minecraft1) {
        super(minecraft1);
    }

    public void flipPlayer(EntityPlayer playerEntity) {
        playerEntity.rotationYaw = -180.0F;
    }

    public void init() {
    }

    public boolean sendBlockRemoved(int x, int y, int z, int side) {
        int i4 = this.mc.theWorld.getBlockId(x, y, z);
        int i5 = this.mc.theWorld.getBlockMetadata(x, y, z);
        boolean z6 = super.sendBlockRemoved(x, y, z, side);
        ItemStack itemStack7 = this.mc.thePlayer.getCurrentEquippedItem();
        boolean z8 = this.mc.thePlayer.canHarvestBlock(Block.blocksList[i4]);
        if(itemStack7 != null) {
            itemStack7.onDestroyBlock(i4, x, y, z);
            if(itemStack7.stackSize == 0) {
                itemStack7.onItemDestroyedByUse(this.mc.thePlayer);
                this.mc.thePlayer.destroyCurrentEquippedItem();
            }
        }

        if(z6 && z8) {
            Block.blocksList[i4].harvestBlock(this.mc.theWorld, x, y, z, i5);
        }

        return z6;
    }

    public void clickBlock(int x, int y, int z, int side) {
        int i4;
        if((i4 = this.mc.theWorld.getBlockId(x, y, z)) > 0 && this.curBlockDamage == 0.0F) {
            Block.blocksList[i4].onBlockClicked(this.mc.theWorld, x, y, z, this.mc.thePlayer);
        }

        if(i4 > 0 && Block.blocksList[i4].blockStrength(this.mc.thePlayer) >= 1.0F) {
            this.sendBlockRemoved(x, y, z, side);
        }

    }

    public void resetBlockRemoving() {
        this.curBlockDamage = 0.0F;
        this.blockHitWait = 0;
    }

    public void sendBlockRemoving(int x, int y, int z, int side) {
        if(this.blockHitWait > 0) {
            --this.blockHitWait;
        } else {
            if(x == this.curBlockX && y == this.curBlockY && z == this.curBlockZ) {
                int i5 = this.mc.theWorld.getBlockId(x, y, z);
                if(i5 == 0) {
                    return;
                }

                Block block6 = Block.blocksList[i5];
                this.curBlockDamage += block6.blockStrength(this.mc.thePlayer);
                if(this.blockDestroySoundCounter % 4.0F == 0.0F && block6 != null) {
                    this.mc.sndManager.playSound(block6.stepSound.getStepSound(), (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, (block6.stepSound.getVolume() + 1.0F) / 8.0F, block6.stepSound.getPitch() * 0.5F);
                }

                ++this.blockDestroySoundCounter;
                if(this.curBlockDamage >= 1.0F) {
                    this.sendBlockRemoved(x, y, z, side);
                    this.curBlockDamage = 0.0F;
                    this.prevBlockDamage = 0.0F;
                    this.blockDestroySoundCounter = 0.0F;
                    this.blockHitWait = 5;
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

    public void setPartialTime(float renderPartialTick) {
        if(this.curBlockDamage <= 0.0F) {
            this.mc.ingameGUI.damageGuiPartialTime = 0.0F;
            this.mc.renderGlobal.damagePartialTime = 0.0F;
        } else {
            float f2 = this.prevBlockDamage + (this.curBlockDamage - this.prevBlockDamage) * renderPartialTick;
            this.mc.ingameGUI.damageGuiPartialTime = f2;
            this.mc.renderGlobal.damagePartialTime = f2;
        }

    }

    public float getBlockReachDistance() {
        return 4.0F;
    }

    public void onWorldChange(World world) {
        super.onWorldChange(world);
    }

    public void onUpdate() {
        this.prevBlockDamage = this.curBlockDamage;
        this.monsterSpawner.onUpdate(this.mc.theWorld);
        this.animalSpawner.onUpdate(this.mc.theWorld);
        this.mc.sndManager.playRandomMusicIfReady();
    }
}
