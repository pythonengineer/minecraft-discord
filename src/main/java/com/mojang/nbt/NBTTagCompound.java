package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class NBTTagCompound extends NBTBase {
	private Map tagMap = new HashMap();

	final void writeTagContents(DataOutput dataOutput) throws IOException {
		Iterator iterator2 = this.tagMap.values().iterator();

		while(iterator2.hasNext()) {
			NBTBase.writeNamedTag((NBTBase)iterator2.next(), dataOutput);
		}

		dataOutput.writeByte(0);
	}

	final void readTagContents(DataInput dataInput) throws IOException {
		this.tagMap.clear();

		NBTBase nBTBase2;
		while((nBTBase2 = NBTBase.readNamedTag(dataInput)).getType() != 0) {
			this.tagMap.put(nBTBase2.getKey(), nBTBase2);
		}

	}

	public final byte getType() {
		return (byte)10;
	}

	public final void setTag(String key, NBTBase baseTag) {
		this.tagMap.put(key, baseTag.setKey(key));
	}

	public final void setByte(String key, byte byteValue) {
		this.tagMap.put(key, (new NBTTagByte(byteValue)).setKey(key));
	}

	public final void setShort(String key, short shortValue) {
		this.tagMap.put(key, (new NBTTagShort(shortValue)).setKey(key));
	}

	public final void setInteger(String key, int intValue) {
		this.tagMap.put(key, (new NBTTagInt(intValue)).setKey(key));
	}

	public final void setLong(String key, long longValue) {
		this.tagMap.put(key, (new NBTTagLong(longValue)).setKey(key));
	}

	public final void setFloat(String key, float floatValue) {
		this.tagMap.put(key, (new NBTTagFloat(floatValue)).setKey(key));
	}

	public final void setString(String key, String stringValue) {
		this.tagMap.put(key, (new NBTTagString(stringValue)).setKey(key));
	}

	public final void setByteArray(String key, byte[] byteArray) {
		this.tagMap.put(key, (new NBTTagByteArray(byteArray)).setKey(key));
	}

	public final void setCompoundTag(String key, NBTTagCompound compoundTag) {
		this.tagMap.put(key, compoundTag.setKey(key));
	}

	public final void setBoolean(String key, boolean booleanValue) {
		this.setByte(key, (byte)(booleanValue ? 1 : 0));
	}

	public final boolean hasKey(String key) {
		return this.tagMap.containsKey(key);
	}

	public final byte getByte(String key) {
		return !this.tagMap.containsKey(key) ? 0 : ((NBTTagByte)this.tagMap.get(key)).byteValue;
	}

	public final short getShort(String key) {
		return !this.tagMap.containsKey(key) ? 0 : ((NBTTagShort)this.tagMap.get(key)).shortValue;
	}

	public final int getInteger(String key) {
		return !this.tagMap.containsKey(key) ? 0 : ((NBTTagInt)this.tagMap.get(key)).intValue;
	}

	public final long getLong(String key) {
		return !this.tagMap.containsKey(key) ? 0L : ((NBTTagLong)this.tagMap.get(key)).longValue;
	}

	public final float getFloat(String key) {
		return !this.tagMap.containsKey(key) ? 0.0F : ((NBTTagFloat)this.tagMap.get(key)).floatValue;
	}

	public final String getString(String key) {
		return !this.tagMap.containsKey(key) ? "" : ((NBTTagString)this.tagMap.get(key)).stringValue;
	}

	public final byte[] getByteArray(String key) {
		return !this.tagMap.containsKey(key) ? new byte[0] : ((NBTTagByteArray)this.tagMap.get(key)).byteArray;
	}

	public final NBTTagCompound getCompoundTag(String key) {
		return !this.tagMap.containsKey(key) ? new NBTTagCompound() : (NBTTagCompound)this.tagMap.get(key);
	}

	public final NBTTagList getTagList(String key) {
		return !this.tagMap.containsKey(key) ? new NBTTagList() : (NBTTagList)this.tagMap.get(key);
	}

	public final boolean getBoolean(String key) {
		return this.getByte(key) != 0;
	}

	public final String toString() {
		return "" + this.tagMap.size() + " entries";
	}
}