package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;

public final class NBTTagByte extends NBTBase {
	public byte byteValue;

	public NBTTagByte() {
	}

	public NBTTagByte(byte var1) {
		this.byteValue = var1;
	}

	final void writeTagContents(DataOutput var1) {
        try {
        var1.writeByte(this.byteValue);
    } catch (java.io.IOException exc) {}
	}

	final void readTagContents(DataInput var1) {
        try{
        this.byteValue = var1.readByte();
    } catch (java.io.IOException exc) {}
	}

	public final byte getType() {
		return (byte)1;
	}

	public final String toString() {
		return "" + this.byteValue;
	}
}
