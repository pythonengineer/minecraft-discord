package net.minecraft.game.entity;

import com.mojang.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class EntityPainting extends Entity {
	private int tickCounter;
	public int direction;
	private int xPosition;
	private int yPosition;
	private int zPosition;
	public EnumArt art;

	public EntityPainting(World world1) {
		super(world1);
		this.tickCounter = 0;
		this.direction = 0;
		this.yOffset = 0.0F;
		this.setSize(0.5F, 0.5F);
	}

	public EntityPainting(World world, int x, int y, int z, int direction) {
		this(world);
		this.xPosition = x;
		this.yPosition = y;
		this.zPosition = z;
		ArrayList arrayList7 = new ArrayList();
		EnumArt[] enumArt8;
		y = (enumArt8 = EnumArt.values()).length;

		for(z = 0; z < y; ++z) {
			EnumArt enumArt6 = enumArt8[z];
			this.art = enumArt6;
			this.setDirection(direction);
			if(this.onValidSurface()) {
				arrayList7.add(enumArt6);
			}
		}

		if(arrayList7.size() > 0) {
			this.art = (EnumArt)arrayList7.get(this.rand.nextInt(arrayList7.size()));
		}

		this.setDirection(direction);
	}

	private void setDirection(int direction) {
		this.direction = direction;
		this.prevRotationYaw = this.rotationYaw = (float)(direction * 90);
		float f2 = (float)this.art.sizeX;
		float f3 = (float)this.art.sizeY;
		float f4 = (float)this.art.sizeX;
		if(direction != 0 && direction != 2) {
			f2 = 0.5F;
		} else {
			f4 = 0.5F;
		}

		f2 /= 32.0F;
		f3 /= 32.0F;
		f4 /= 32.0F;
		float f5 = (float)this.xPosition + 0.5F;
		float f6 = (float)this.yPosition + 0.5F;
		float f7 = (float)this.zPosition + 0.5F;
		if(direction == 0) {
			f7 -= 0.5625F;
		}

		if(direction == 1) {
			f5 -= 0.5625F;
		}

		if(direction == 2) {
			f7 += 0.5625F;
		}

		if(direction == 3) {
			f5 += 0.5625F;
		}

		if(direction == 0) {
			f5 -= getArtSize(this.art.sizeX);
		}

		if(direction == 1) {
			f7 += getArtSize(this.art.sizeX);
		}

		if(direction == 2) {
			f5 += getArtSize(this.art.sizeX);
		}

		if(direction == 3) {
			f7 -= getArtSize(this.art.sizeX);
		}

		f6 += getArtSize(this.art.sizeY);
		this.setPosition((double)f5, (double)f6, (double)f7);
		this.boundingBox = new AxisAlignedBB((double)(f5 - f2), (double)(f6 - f3), (double)(f7 - f4), (double)(f5 + f2), (double)(f6 + f3), (double)(f7 + f4));
		double d13 = 0.0062500000931322575D;
		double d11 = 0.0062500000931322575D;
		double d9 = 0.0062500000931322575D;
		AxisAlignedBB direction1 = this.boundingBox;
		double d15 = this.boundingBox.minX;
		double d17 = direction1.minY;
		double d19 = direction1.minZ;
		double d21 = direction1.maxX;
		double d23 = direction1.maxY;
		double d25 = direction1.maxZ;
		d21 -= 0.0062500000931322575D;
		d23 -= 0.0062500000931322575D;
		d25 -= 0.0062500000931322575D;
		this.boundingBox = new AxisAlignedBB(d15, d17, d19, d21, d23, d25);
	}

	private static float getArtSize(int textureSize) {
		return textureSize == 32 ? 0.5F : (textureSize == 64 ? 0.5F : 0.0F);
	}

	public final void onUpdate() {
		if(this.tickCounter++ == 100 && !this.onValidSurface()) {
			this.tickCounter = 0;
            this.setEntityDead();
			this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.painting)));
		}

	}

	public final boolean onValidSurface() {
		if(this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() > 0) {
			return false;
		} else {
			int i1 = this.art.sizeX / 16;
			int i2 = this.art.sizeY / 16;
			int i3 = this.xPosition;
			int i5 = this.zPosition;
			if(this.direction == 0) {
				i3 = MathHelper.floor_double(this.posX - (double)((float)this.art.sizeX / 32.0F));
			}

			if(this.direction == 1) {
				i5 = MathHelper.floor_double(this.posZ - (double)((float)this.art.sizeX / 32.0F));
			}

			if(this.direction == 2) {
				i3 = MathHelper.floor_double(this.posX - (double)((float)this.art.sizeX / 32.0F));
			}

			if(this.direction == 3) {
				i5 = MathHelper.floor_double(this.posZ - (double)((float)this.art.sizeX / 32.0F));
			}

			int i4 = MathHelper.floor_double(this.posY - (double)((float)this.art.sizeY / 32.0F));

			int i7;
			for(int i6 = 0; i6 < i1; ++i6) {
				for(i7 = 0; i7 < i2; ++i7) {
					Material material8;
					if(this.direction != 0 && this.direction != 2) {
						material8 = this.worldObj.getBlockMaterial(this.xPosition, i4 + i7, i5 + i6);
					} else {
						material8 = this.worldObj.getBlockMaterial(i3 + i6, i4 + i7, this.zPosition);
					}

					if(!material8.isSolid()) {
						return false;
					}
				}
			}

			List list9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox);

			for(i7 = 0; i7 < list9.size(); ++i7) {
				if(list9.get(i7) instanceof EntityPainting) {
					return false;
				}
			}

			return true;
		}
	}

	public final boolean canBeCollidedWith() {
		return true;
	}

	public final boolean attackEntityFrom(Entity entity, int damage) {
        this.setEntityDead();
		this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.painting)));
		return true;
	}

	public final void writeEntityToNBT(NBTTagCompound compoundTag) {
		compoundTag.setByte("Dir", (byte)this.direction);
		compoundTag.setString("Motive", this.art.title);
		compoundTag.setInteger("TileX", this.xPosition);
		compoundTag.setInteger("TileY", this.yPosition);
		compoundTag.setInteger("TileZ", this.zPosition);
	}

	public final void readEntityFromNBT(NBTTagCompound compoundTag) {
		this.direction = compoundTag.getByte("Dir");
		this.xPosition = compoundTag.getInteger("TileX");
		this.yPosition = compoundTag.getInteger("TileY");
		this.zPosition = compoundTag.getInteger("TileZ");
		String string6 = compoundTag.getString("Motive");
		EnumArt[] enumArt2;
		int i3 = (enumArt2 = EnumArt.values()).length;

		for(int i4 = 0; i4 < i3; ++i4) {
			EnumArt enumArt5;
			if((enumArt5 = enumArt2[i4]).title.equals(string6)) {
				this.art = enumArt5;
			}
		}

		if(this.art == null) {
			this.art = EnumArt.Kebab;
		}

		this.setDirection(this.direction);
	}
}