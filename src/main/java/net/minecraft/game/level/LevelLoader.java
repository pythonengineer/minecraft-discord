package net.minecraft.game.level;

import com.mojang.nbt.NBTBase;
import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;
import com.mojang.nbt.NBTTagShort;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.zip.GZIPOutputStream;

import net.minecraft.client.IProgressUpdate;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.tileentity.TileEntity;
import net.minecraft.game.level.block.tileentity.TileEntityChest;

public abstract class LevelLoader {
	private IProgressUpdate guiLoading;

	public LevelLoader(IProgressUpdate var1) {
		this.guiLoading = var1;
	}

	public final World load(InputStream var1) throws IOException {
		if(this.guiLoading != null) {
			this.guiLoading.displayProgressMessage("Loading level");
		}

		if(this.guiLoading != null) {
			this.guiLoading.displayLoadingString("Reading..");
		}

		NBTTagCompound var11 = LoadingScreenRenderer.writeLevelTags(var1);
		NBTTagCompound var2 = var11.getCompoundTag("About");
		NBTTagCompound var3 = var11.getCompoundTag("Map");
		NBTTagCompound var4 = var11.getCompoundTag("Environment");
		NBTTagList var5 = var11.getTagList("Entities");
		short var6 = var3.getShort("Width");
		short var7 = var3.getShort("Length");
		short var8 = var3.getShort("Height");
		World var9 = new World();
		if(this.guiLoading != null) {
			this.guiLoading.displayLoadingString("Preparing level..");
		}

		NBTTagList var10 = var3.getTagList("Spawn");
		var9.xSpawn = ((NBTTagShort)var10.tagAt(0)).shortValue;
		var9.ySpawn = ((NBTTagShort)var10.tagAt(1)).shortValue;
		var9.zSpawn = ((NBTTagShort)var10.tagAt(2)).shortValue;
		var9.authorName = var2.getString("Author");
		var9.name = var2.getString("Name");
		var9.createTime = var2.getLong("CreatedOn");
		var9.cloudColor = var4.getInteger("CloudColor");
		var9.skyColor = var4.getInteger("SkyColor");
		var9.fogColor = var4.getInteger("FogColor");
		var9.skyBrightness = (float)var4.getByte("SkyBrightness") / 100.0F;
		var9.cloudHeight = var4.getShort("CloudHeight");
		var9.groundLevel = var4.getShort("SurroundingGroundHeight");
		var9.waterLevel = var4.getShort("SurroundingWaterHeight");
		var9.defaultFluid = var4.getByte("SurroundingWaterType");
		var9.generate(var6, var8, var7, var3.getByteArray("Blocks"));
		if(this.guiLoading != null) {
			this.guiLoading.displayLoadingString("Preparing entities..");
		}

		for(int var14 = 0; var14 < var5.tagCount(); ++var14) {
			var3 = (NBTTagCompound)var5.tagAt(var14);
			String var17 = var3.getString("id");
			Entity var20 = this.loadEntity(var9, var17);
			if(var20 != null) {
				var20.readFromNBT(var3);
				var9.spawnEntityInWorld(var20);
			}
		}

		NBTTagList var15 = var11.getTagList("TileEntities");

		for(int var16 = 0; var16 < var15.tagCount(); ++var16) {
			var4 = (NBTTagCompound)var15.tagAt(var16);
			int var21 = var4.getInteger("Pos");
			String var12 = var4.getString("id");
			TileEntityChest var13 = var12.equals("Chest") ? new TileEntityChest() : null;
			if(var13 != null) {
				var13.readFromNBT(var4);
				int var18 = var21 % 1024;
				int var19 = (var21 >> 10) % 1024;
				var21 = (var21 >> 20) % 1024;
				var9.setBlockTileEntity(var18, var19, var21, var13);
			}
		}

		return var9;
	}

	protected Entity loadEntity(World var1, String var2) {
		return null;
	}

	public final void save(World var1, OutputStream var2) throws IOException {
		if(this.guiLoading != null) {
			this.guiLoading.displayProgressMessage("Saving level");
		}

		if(this.guiLoading != null) {
			this.guiLoading.displayLoadingString("Preparing level..");
		}

		NBTTagCompound var3 = new NBTTagCompound();
		var3.setInteger("CloudColor", var1.cloudColor);
		var3.setInteger("SkyColor", var1.skyColor);
		var3.setInteger("FogColor", var1.fogColor);
		var3.setByte("SkyBrightness", (byte)((int)(var1.skyBrightness * 100.0F)));
		var3.setShort("CloudHeight", (short)var1.cloudHeight);
		var3.setShort("SurroundingGroundHeight", (short)var1.groundLevel);
		var3.setShort("SurroundingWaterHeight", (short)var1.waterLevel);
		var3.setByte("SurroundingGroundType", (byte)Block.grass.blockID);
		var3.setByte("SurroundingWaterType", (byte)var1.defaultFluid);
		NBTTagCompound var4 = new NBTTagCompound();
		var4.setShort("Width", (short)var1.width);
		var4.setShort("Length", (short)var1.length);
		var4.setShort("Height", (short)var1.height);
		var4.setByteArray("Blocks", var1.blocks);
		var4.setByteArray("Data", var1.data);
		NBTTagList var5 = new NBTTagList();
		var5.setTag(new NBTTagShort((short)var1.xSpawn));
		var5.setTag(new NBTTagShort((short)var1.ySpawn));
		var5.setTag(new NBTTagShort((short)var1.zSpawn));
		var4.setTag("Spawn", var5);
		NBTTagCompound var15 = new NBTTagCompound();
		var15.setString("Author", var1.authorName);
		var15.setString("Name", var1.name);
		var15.setLong("CreatedOn", var1.createTime);
		if(this.guiLoading != null) {
			this.guiLoading.displayLoadingString("Preparing entities..");
		}

		NBTTagList var6 = new NBTTagList();
		Iterator var7 = var1.entityMap.all.iterator();

		while(var7.hasNext()) {
			Entity var8 = (Entity)var7.next();
			NBTTagCompound var9 = new NBTTagCompound();
			var8.writeToNBT(var9);
			if(!var9.isEmptyNBTMap()) {
				var6.setTag(var9);
			}
		}

		NBTTagList var16 = new NBTTagList();
		Iterator var17 = var1.map.keySet().iterator();

		while(var17.hasNext()) {
			int var19 = ((Integer)var17.next()).intValue();
			NBTTagCompound var10 = new NBTTagCompound();
			var10.setInteger("Pos", var19);
			TileEntity var20 = (TileEntity)var1.map.get(Integer.valueOf(var19));
			var20.writeToNBT(var10);
			var16.setTag(var10);
		}

		NBTTagCompound var18 = new NBTTagCompound();
		var18.setKey("MinecraftLevel");
		var18.setCompoundTag("About", var15);
		var18.setCompoundTag("Map", var4);
		var18.setCompoundTag("Environment", var3);
		var18.setTag("Entities", var6);
		var18.setTag("TileEntities", var16);
		if(this.guiLoading != null) {
			this.guiLoading.displayLoadingString("Writing..");
		}

		NBTTagCompound var13 = var18;
		DataOutputStream var14 = new DataOutputStream(new GZIPOutputStream(var2));

		try {
			NBTBase.writeTag(var13, var14);
		} finally {
			var14.close();
		}

	}
}
