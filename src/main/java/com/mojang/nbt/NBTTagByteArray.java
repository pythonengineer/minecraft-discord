package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagByteArray extends NBTBase {
	public byte[] byteArray;

	public NBTTagByteArray() {
	}

	public NBTTagByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

	final void writeTagContents(DataOutput dataOutput) throws IOException {
		dataOutput.writeInt(this.byteArray.length);
		dataOutput.write(this.byteArray);
	}

	final void readTagContents(DataInput dataInput) throws IOException {
		int i2 = dataInput.readInt();
		this.byteArray = new byte[i2];
		dataInput.readFully(this.byteArray);
	}

	public final byte getType() {
		return (byte)7;
	}

	public final String toString() {
		return "[" + this.byteArray.length + " bytes]";
	}
}