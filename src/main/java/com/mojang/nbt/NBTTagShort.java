package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagShort extends NBTBase {
	public short shortValue;

	public NBTTagShort() {
	}

	public NBTTagShort(short shortValue) {
		this.shortValue = shortValue;
	}

	final void writeTagContents(DataOutput dataOutput) throws IOException {
		dataOutput.writeShort(this.shortValue);
	}

	final void readTagContents(DataInput dataInput) throws IOException {
		this.shortValue = dataInput.readShort();
	}

	public final byte getType() {
		return (byte)2;
	}

	public final String toString() {
		return "" + this.shortValue;
	}
}