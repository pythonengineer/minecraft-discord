package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class NBTTagCompound extends NBTBase {
	private Map tagMap = new HashMap();

	final void writeTagContents(DataOutput var1) {
		Iterator var2 = this.tagMap.values().iterator();
        try{
		while(var2.hasNext()) {
			NBTBase var3 = (NBTBase)var2.next();
			NBTBase.writeTag(var3, var1);
		}

		var1.writeByte(0);
    } catch (java.io.IOException exc) {}
	}

	final void readTagContents(DataInput var1) {
		this.tagMap.clear();
        try{
		while(true) {
			NBTBase var2 = NBTBase.readTag(var1);
			if(var2.getType() == 0) {
				return;
			}

			this.tagMap.put(var2.getKey(), var2);
		}
    } catch (java.io.IOException exc) {}
	}

	public final byte getType() {
		return (byte)10;
	}

	public final void setTag(String var1, NBTBase var2) {
		this.tagMap.put(var1, var2.setKey(var1));
	}

	public final void setByte(String var1, byte var2) {
		this.tagMap.put(var1, (new NBTTagByte(var2)).setKey(var1));
	}

	public final void setShort(String var1, short var2) {
		this.tagMap.put(var1, (new NBTTagShort(var2)).setKey(var1));
	}

	public final void setInteger(String var1, int var2) {
		this.tagMap.put(var1, (new NBTTagInt(var2)).setKey(var1));
	}

	public final void setLong(String var1, long var2) {
		this.tagMap.put(var1, (new NBTTagLong(var2)).setKey(var1));
	}

	public final void setFloat(String var1, float var2) {
		this.tagMap.put(var1, (new NBTTagFloat(var2)).setKey(var1));
	}

	public final void setString(String var1, String var2) {
		this.tagMap.put(var1, (new NBTTagString(var2)).setKey(var1));
	}

	public final void setByteArray(String var1, byte[] var2) {
		this.tagMap.put(var1, (new NBTTagByteArray(var2)).setKey(var1));
	}

	public final void setCompoundTag(String var1, NBTTagCompound var2) {
		this.tagMap.put(var1, var2.setKey(var1));
	}

	public final byte getByte(String var1) {
		return !this.tagMap.containsKey(var1) ? 0 : ((NBTTagByte)this.tagMap.get(var1)).byteValue;
	}

	public final short getShort(String var1) {
		return !this.tagMap.containsKey(var1) ? 0 : ((NBTTagShort)this.tagMap.get(var1)).shortValue;
	}

	public final int getInteger(String var1) {
		return !this.tagMap.containsKey(var1) ? 0 : ((NBTTagInt)this.tagMap.get(var1)).intValue;
	}

	public final long getLong(String var1) {
		return !this.tagMap.containsKey(var1) ? 0L : ((NBTTagLong)this.tagMap.get(var1)).longValue;
	}

	public final float getFloat(String var1) {
		return !this.tagMap.containsKey(var1) ? 0.0F : ((NBTTagFloat)this.tagMap.get(var1)).floatValue;
	}

	public final String getString(String var1) {
		return !this.tagMap.containsKey(var1) ? "" : ((NBTTagString)this.tagMap.get(var1)).stringValue;
	}

	public final byte[] getByteArray(String var1) {
		return !this.tagMap.containsKey(var1) ? new byte[0] : ((NBTTagByteArray)this.tagMap.get(var1)).byteArray;
	}

	public final NBTTagCompound getCompoundTag(String var1) {
		return !this.tagMap.containsKey(var1) ? new NBTTagCompound() : (NBTTagCompound)this.tagMap.get(var1);
	}

	public final NBTTagList getTagList(String var1) {
		return !this.tagMap.containsKey(var1) ? new NBTTagList() : (NBTTagList)this.tagMap.get(var1);
	}

	public final String toString() {
		return "" + this.tagMap.size() + " entries";
	}

	public final boolean isEmptyNBTMap() {
		return this.tagMap.isEmpty();
	}
}
