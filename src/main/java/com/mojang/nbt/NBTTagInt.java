package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagInt extends NBTBase {
	public int intValue;

	public NBTTagInt() {
	}

	public NBTTagInt(int intValue) {
		this.intValue = intValue;
	}

	final void writeTagContents(DataOutput dataOutput) throws IOException {
		dataOutput.writeInt(this.intValue);
	}

	final void readTagContents(DataInput dataInput) throws IOException {
		this.intValue = dataInput.readInt();
	}

	public final byte getType() {
		return (byte)3;
	}

	public final String toString() {
		return "" + this.intValue;
	}
}