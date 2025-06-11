package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;

public final class NBTTagShort extends NBTBase {
	public short shortValue;

	public NBTTagShort() {
	}

	public NBTTagShort(short var1) {
		this.shortValue = var1;
	}

	final void writeTagContents(DataOutput var1) {
        try{
        var1.writeShort(this.shortValue);
    } catch (java.io.IOException exc) {}
	}

	final void readTagContents(DataInput var1) {
        try{
        this.shortValue = var1.readShort();
    } catch (java.io.IOException exc) {}
	}

	public final byte getType() {
		return (byte)2;
	}

	public final String toString() {
		return "" + this.shortValue;
	}
}
