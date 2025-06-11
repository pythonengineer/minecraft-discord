package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;

public final class NBTTagLong extends NBTBase {
	public long longValue;

	public NBTTagLong() {
	}

	public NBTTagLong(long var1) {
		this.longValue = var1;
	}

	final void writeTagContents(DataOutput var1) {
        try{
        var1.writeLong(this.longValue);
    } catch (java.io.IOException exc) {}
	}

	final void readTagContents(DataInput var1) {
        try{
        this.longValue = var1.readLong();
    } catch (java.io.IOException exc) {}
	}

	public final byte getType() {
		return (byte)4;
	}

	public final String toString() {
		return "" + this.longValue;
	}
}
