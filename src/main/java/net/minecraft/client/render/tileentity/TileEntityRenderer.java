package net.minecraft.client.render.tileentity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.block.tileentity.TileEntityMobSpawner;
import net.minecraft.game.world.block.tileentity.TileEntityMobSpawnerRenderer;
import net.minecraft.game.world.block.tileentity.TileEntitySign;

public final class TileEntityRenderer {
    private Map specialRendererMap = new HashMap();
    public static TileEntityRenderer instance = new TileEntityRenderer();
    private FontRenderer fontRenderer;
    public static double staticPlayerX;
    public static double staticPlayerY;
    public static double staticPlayerZ;
    public RenderEngine renderEngine;
    private World worldObj;
    private double playerX;
    private double playerY;
    private double playerZ;

    private TileEntityRenderer() {
        this.specialRendererMap.put(TileEntitySign.class, new TileEntitySignRenderer());
        this.specialRendererMap.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
        Iterator iterator1 = this.specialRendererMap.values().iterator();

        while(iterator1.hasNext()) {
            ((TileEntitySpecialRenderer)iterator1.next()).setTileEntityRenderer(this);
        }

    }

    private TileEntitySpecialRenderer getSpecialRendererForClass(Class tileEntityClass) {
        TileEntitySpecialRenderer tileEntitySpecialRenderer2;
        if((tileEntitySpecialRenderer2 = (TileEntitySpecialRenderer)this.specialRendererMap.get(tileEntityClass)) == null && tileEntityClass != TileEntity.class) {
            tileEntitySpecialRenderer2 = this.getSpecialRendererForClass(tileEntityClass.getSuperclass());
            this.specialRendererMap.put(tileEntityClass, tileEntitySpecialRenderer2);
        }

        return tileEntitySpecialRenderer2;
    }

    public final boolean hasSpecialRenderer(TileEntity tileEntity) {
        return this.getSpecialRendererForEntity(tileEntity) != null;
    }

    private TileEntitySpecialRenderer getSpecialRendererForEntity(TileEntity tileEntity) {
        return this.getSpecialRendererForClass(tileEntity.getClass());
    }

    public final void cacheActiveRenderInfo(World world, RenderEngine renderEngine, FontRenderer fontRenderer, EntityPlayer playerEntity, float partialTicks) {
        this.worldObj = world;
        this.renderEngine = renderEngine;
        this.fontRenderer = fontRenderer;
        this.playerX = playerEntity.lastTickPosX + (playerEntity.posX - playerEntity.lastTickPosX) * (double)partialTicks;
        this.playerY = playerEntity.lastTickPosY + (playerEntity.posY - playerEntity.lastTickPosY) * (double)partialTicks;
        this.playerZ = playerEntity.lastTickPosZ + (playerEntity.posZ - playerEntity.lastTickPosZ) * (double)partialTicks;
    }

    public final void renderTileEntity(TileEntity tileEntity, float partialTicks) {
        if(tileEntity.getDistanceFrom(this.playerX, this.playerY, this.playerZ) < 4096.0D) {
            float f3;
            GL11.glColor3f(f3 = this.worldObj.getBrightness(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord), f3, f3);
            this.renderTileEntityAt(tileEntity, (double)tileEntity.xCoord - staticPlayerX, (double)tileEntity.yCoord - staticPlayerY, (double)tileEntity.zCoord - staticPlayerZ, partialTicks);
        }

    }

    public final void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        TileEntitySpecialRenderer tileEntitySpecialRenderer9;
        if((tileEntitySpecialRenderer9 = this.getSpecialRendererForEntity(tileEntity)) != null) {
            tileEntitySpecialRenderer9.renderTileEntityMobSpawner(tileEntity, x, y, z, partialTicks);
        }

    }

    public final FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
}