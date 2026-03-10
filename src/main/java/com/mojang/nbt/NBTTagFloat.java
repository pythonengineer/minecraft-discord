package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagFloat extends NBTBase {
	public float floatValue;

	public NBTTagFloat() {
	}

	public NBTTagFloat(float floatValue) {
		this.floatValue = floatValue;
	}

	final void writeTagContents(DataOutput dataOutput) throws IOException {
		dataOutput.writeFloat(this.floatValue);
	}

	final void readTagContents(DataInput dataInput) throws IOException {
		this.floatValue = dataInput.readFloat();
	}

	public final byte getType() {
		return (byte)5;
	}

	public final String toString() {
		return "" + this.floatValue;
	}
}