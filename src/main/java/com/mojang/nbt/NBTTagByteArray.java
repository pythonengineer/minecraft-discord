package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;

public final class NBTTagByteArray extends NBTBase {
	public byte[] byteArray;

	public NBTTagByteArray() {
	}

	public NBTTagByteArray(byte[] var1) {
		this.byteArray = var1;
	}

	final void writeTagContents(DataOutput var1) {
        try{
        var1.writeInt(this.byteArray.length);
        var1.write(this.byteArray);
    } catch (java.io.IOException exc) {}
	}

	final void readTagContents(DataInput var1) {
        try{
        int var2 = var1.readInt();
        this.byteArray = new byte[var2];
        var1.readFully(this.byteArray);
    } catch (java.io.IOException exc) {}
	}

	public final byte getType() {
		return (byte)7;
	}

	public final String toString() {
		return "[" + this.byteArray.length + " bytes]";
	}
}
