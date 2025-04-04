package com.mojang.minecraft.item;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public class Item extends Entity {
	public static final long serialVersionUID = 0L;
	private static ItemModel[] models = new ItemModel[256];
	private float xd;
	private float yd;
	private float zd;
	private float rot;
	private int resource;
	private int tickCount;
	private int age = 0;

	public static void initModels() {
		for(int i0 = 0; i0 < 256; ++i0) {
			Tile tile1;
			if((tile1 = Tile.tiles[i0]) != null) {
				models[i0] = new ItemModel(tile1.tex);
			}
		}

	}

	public Item(Level level, float x, float y, float z, int res) {
		super(level);
		this.setSize(0.25F, 0.25F);
		this.heightOffset = this.bbHeight / 2.0F;
		this.setPos(x, y, z);
		this.resource = res;
		this.rot = (float)(Math.random() * 360.0D);
		this.xd = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D);
		this.yd = 0.2F;
		this.zd = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D);
		this.makeStepSound = false;
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		this.yd -= 0.04F;
		this.move(this.xd, this.yd, this.zd);
		this.xd *= 0.98F;
		this.yd *= 0.98F;
		this.zd *= 0.98F;
		if(this.onGround) {
			this.xd *= 0.7F;
			this.zd *= 0.7F;
			this.yd *= -0.5F;
		}

		++this.tickCount;
		++this.age;
		if(this.age >= 6000) {
			this.remove();
		}

	}

	public void render(Textures textures, float translation) {
        this.textureId = textures.loadTexture("/terrain.png");
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textureId);
        float textures1 = this.level.getBrightness((int)this.x, (int)this.y, (int)this.z);
        float f3 = this.rot + ((float)this.tickCount + translation) * 3.0F;
        GL11.glPushMatrix();
        GL11.glColor4f(textures1, textures1, textures1, 1.0F);
        float f4 = (textures1 = (float)Math.sin((double)(f3 / 10.0F))) * 0.1F + 0.1F;
        GL11.glTranslatef(this.xo + (this.x - this.xo) * translation, this.yo + (this.y - this.yo) * translation + f4, this.zo + (this.z - this.zo) * translation);
        GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
        models[this.resource].render();
        textures1 = (textures1 = (textures1 = textures1 * 0.5F + 0.5F) * textures1) * textures1;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, textures1 * 0.4F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        models[this.resource].render();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

    public void playerTouch(Entity entity1) {
        Player player2;
        if((player2 = (Player)entity1).addResource(this.resource)) {
            this.level.addEntity(new TakeEntityAnim(this.level, this, player2));
            this.remove();
        }

    }
}
