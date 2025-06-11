package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;

public final class NBTTagInt extends NBTBase {
	public int intValue;

	public NBTTagInt() {
	}

	public NBTTagInt(int var1) {
		this.intValue = var1;
	}

	final void writeTagContents(DataOutput var1) {
        try{
        var1.writeInt(this.intValue);
    } catch (java.io.IOException exc) {}
	}

	final void readTagContents(DataInput var1) {
        try{
        this.intValue = var1.readInt();
    } catch (java.io.IOException exc) {}
	}

	public final byte getType() {
		return (byte)3;
	}

	public final String toString() {
		return "" + this.intValue;
	}
}
